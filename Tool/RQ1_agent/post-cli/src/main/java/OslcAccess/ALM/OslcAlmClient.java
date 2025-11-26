/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import static OslcAccess.ALM.OslcAlmQueryResponse.buildResult;
import OslcAccess.ALM.OslcAlmResponse.Field;
import OslcAccess.OslcCommand;
import OslcAccess.OslcCommandExecutor;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import OslcAccess.OslcProtocolVersion;
import OslcAccess.OslcResponse;
import RestClient.Authentication.EmptyAuthenticationProvider;
import RestClient.Authentication.OAuthResponse;
import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestInternalException;
import RestClient.Exceptions.TemporaryServerError;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import util.EcvLoginData;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class OslcAlmClient {

    static private final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(OslcAlmClient.class.getCanonicalName());

    static final public OslcAlmClient client = new OslcAlmClient();

    private final Map<OslcAlmServerDescription, OslcCommandExecutor> executorMap;

    private OslcAlmClient() {
        executorMap = new IdentityHashMap<>();
    }

    public String getCurrentDatabaseName() {
        Set<OslcAlmServerDescription> serverDescriptions = executorMap.keySet();
        if (serverDescriptions.isEmpty() == false) {
            return (serverDescriptions.iterator().next().getName());
        }
        return (null);
    }

    //--------------------------------------------------------------------------
    //
    // Execution of server requests
    //
    //--------------------------------------------------------------------------
    static private final int[] repetitionIntervalForConnect = {1, 1, 1};
        
    private OslcCommandExecutor connect(OslcAlmServerDescription serverDescription) throws RestException, ConstraintViolationReportedByServer {
        assert(serverDescription != null);
        
        TemporaryServerError lastEx = null;
        for (int repetitionNumber = 0; repetitionNumber < repetitionIntervalForConnect.length; repetitionNumber++) {
            try {
                OslcCommandExecutor executor = connectWithoutRepetition(serverDescription);
                return (executor);
            } catch (TemporaryServerError ex) {
                lastEx = ex;
                try {
                    Thread.sleep(repetitionNumber*1000);
                } catch (InterruptedException ex1) {
                    LOGGER.log(Level.SEVERE, "Failed to sleep. ", ex1);
                    ToolUsageLogger.logError(OslcAlmClient.class.getCanonicalName(), ex);
                }
                LOGGER.log(Level.SEVERE, "Connect failed. Repetition No. " + repetitionNumber, ex);
            }
        }
        LOGGER.severe("Giving up on connect.");
        throw (lastEx);
    }
    
    private OslcCommandExecutor connectWithoutRepetition(OslcAlmServerDescription serverDescription) throws RestException, ConstraintViolationReportedByServer {
        
        OslcCommandExecutor existingExecutor = executorMap.get(serverDescription);
        if (existingExecutor != null) {
            return (existingExecutor);
        }

        //---------------------------
        // Set server and login data
        //---------------------------
        EcvLoginData loginData = EcvLoginManager.getFirstLoginData();

        //-------------------------------------------------------------
        // Start OAuth sequence (mainly done by the underlying layers)
        //-------------------------------------------------------------
        OslcCommandExecutor oAuthExecutor = new OslcCommandExecutor(serverDescription, new EmptyAuthenticationProvider(), new OslcAlmResponseFactory());

        //------------------------------------------------------------
        // Create POST request with login data for the OAuth sequence
        //------------------------------------------------------------
        Map<String, String> loginParameters = new TreeMap<>();
        loginParameters.put("j_username", loginData.getLoginName());
        loginParameters.put("j_password", new String(loginData.getPassword()));
        OAuthResponse requestTokenResponse = new OAuthResponse(oAuthExecutor.performPostRequest(OslcProtocolVersion.NONE, serverDescription.getSecurityCheckUrl(), loginParameters));
        
        executorMap.put(serverDescription, oAuthExecutor);
        
        return (oAuthExecutor);
    }

    private OslcResponse executeCommand(OslcAlmServerDescription serverDescription, OslcCommand command) throws RestException {
        assert (serverDescription != null);
        assert (command != null);

        try {
            OslcCommandExecutor executor = connect(serverDescription);
            return (executor.executeCommand(command));
        } catch (ConstraintViolationReportedByServer ex) {
            LOGGER.log(Level.SEVERE, "Unexpected constraint error.", ex);
            ToolUsageLogger.logError(OslcAlmClient.class.getCanonicalName(), ex);
            RestInternalException ie = new RestInternalException("Unexpected constraint error for " + command.getAddressForUi(), ex);
            throw (ie);
        }
    }   

    public void shutdown() {
        for (OslcCommandExecutor executor : executorMap.values()) {
            executor.shutdownHttpConnection();
        }
        executorMap.clear();
    }

    //--------------------------------------------------------------------------
    //
    // Get specific responses from database
    //
    //--------------------------------------------------------------------------
    public OslcResponse getXmlResponseByUrl(String url) throws RestException {
        assert (url != null);
        assert (url.isEmpty() == false);

        OslcAlmServerDescription server = OslcAlmServerDescription.getServerDescriptionForUrl(url);
        if (server == null) {
            throw (new InvalidAlmUrlException(url));
        }
        
        OslcResponse response = executeCommand(server, new OslcAlmGetCommand(url, OslcProtocolVersion.OSLC_20_RDF_XML));
        return (response);
    }

    public OslcAlmResponse getResourceByUrl(String url) throws RestException {
        assert (url != null);
        assert (url.isEmpty() == false);

        OslcAlmServerDescription server = OslcAlmServerDescription.getServerDescriptionForUrl(url);
        if (server == null) {
            throw (new InvalidAlmUrlException(url));
        }
        OslcResponse response = executeCommand(server, new OslcAlmGetCommand(url, OslcProtocolVersion.OSLC_20_RDF_XML));
        OslcAlmResponse almResponse = OslcAlmResponse.build(response);
        return (almResponse);
    }

    public List<OslcAlmQueryResponse> getQueryByUrl(String url) throws RestException {
        assert (url != null);
        assert (url.isEmpty() == false);

        OslcAlmServerDescription server = OslcAlmServerDescription.getServerDescriptionForUrl(url);
        if (server == null) {
            throw (new InvalidAlmUrlException(url));
        }
        OslcResponse response = executeCommand(server, new OslcAlmGetCommand(url, OslcProtocolVersion.OSLC_20_RDF_XML));
        List<OslcAlmQueryResponse> almResponseList = buildResult(response);
        return (almResponseList);
    }

    public OslcAlmEnumerationResponse getEnumerationValues(String url) throws RestException {
        assert (url != null);
        assert (url.isEmpty() == false);

        OslcAlmServerDescription server = OslcAlmServerDescription.getServerDescriptionForUrl(url);
        if (server == null) {
            throw (new InvalidAlmUrlException(url));
        }
        OslcResponse response = executeCommand(server, new OslcAlmGetCommand(url, OslcProtocolVersion.OSLC_20));
        OslcAlmEnumerationResponse almResponse = OslcAlmEnumerationResponse.build(response);
        return (almResponse);
    }

    public List<EcvXmlContainerElement> getProjectAreaList() throws InvalidAlmUrlException, RestException, EcvXmlElement.NotfoundException {
        String path = "/ccm/oslc/workitems/catalog";
        EcvLoginData loginData = EcvLoginManager.getFirstLoginData();

        OslcAlmServerDescription server = loginData.getServerDescription().getAlmServer();
        String url = server.getOslcUrl() + path;

        if (server == null) {
            throw (new InvalidAlmUrlException(url));
        }

        OslcResponse response = executeCommand(server, new OslcAlmGetCommand(url, OslcProtocolVersion.OSLC_20_RDF_XML));

        List<EcvXmlContainerElement> reponseBodyList = response.getResponseBodyList();
        EcvXmlContainerElement xmlResponse = reponseBodyList.get(0);
        List<EcvXmlContainerElement> service_provider = xmlResponse.getContainerElementList();
        EcvXmlContainerElement elementContainer;
        elementContainer = service_provider.get(0);
        List<EcvXmlContainerElement> elementContainerList = elementContainer.getContainerElementList();
        elementContainerList.remove(0); //remove Publisher

        return elementContainerList;
    }

    public List<String> getUrlListFromXml(String url) throws RestException {
        List<String> urlList = new ArrayList<>();
        OslcResponse response = OslcAlmClient.client.getXmlResponseByUrl(url);
        List<EcvXmlContainerElement> responseContainerList = response.getResponseBodyList().get(0).getContainerElementList();

        List<EcvXmlElement> elementList;

        elementList = responseContainerList.get(0).getElementList();

        for (EcvXmlElement element : elementList) {
            if (element.getName().equals("oslc:allowedValue")) {
                String workitem_url = element.getAttribute("rdf:resource");
                urlList.add(workitem_url);
            }
        }

        return urlList;
    }

    /**
     * send PUT request for partial update of work item
     * @param url
     * @param fieldList List of modified fields in Record
     * @param etag tag verifying, if the resource has been changed (for avoiding conflicts, while updating data in database) 
     * @throws RestClient.Exceptions.RestException
     */
    public void updateResource(String url, List<Field> fieldList, String etag) throws RestException {
        assert (url != null);
        assert (url.isEmpty() == false);
        assert (fieldList != null);

        OslcAlmServerDescription server = OslcAlmServerDescription.getServerDescriptionForUrl(url);
        if (server == null) {
            throw (new InvalidAlmUrlException(url));
        }

        OslcAlmPutCommand command = new OslcAlmPutCommand(url, fieldList);

        command.addHeaderData("If-Match", etag);
        executeCommand(server, command);
    }
    
    public String createResource(List<Field> fieldList) throws RestException {
        assert (fieldList != null);

        EcvLoginData loginData = EcvLoginManager.getFirstLoginData();
        
        OslcAlmServerDescription server = loginData.getServerDescription().getAlmServer();
        String serverDescriptionUrl = server.getOslcUrl();
        
        if (server == null) {
            throw (new InvalidAlmUrlException(serverDescriptionUrl));
        }
        
        OslcAlmPostCommand command = new OslcAlmPostCommand(fieldList);
        command.setProtocolVersion(OslcProtocolVersion.OSLC_20_RDF_XML);
        
        OslcResponse response = executeCommand(server, command);

        String workitemUrl = response.getResponseHeader("Location");
        
        return(workitemUrl);
    }

    public OslcResponse testLoadResource(OslcAlmServerDescription server, String path) throws RestException, ConstraintViolationReportedByServer {
        assert (server != null);
        assert (path != null);

        connect(server);

        return (executeCommand(server, new OslcAlmGetCommand(server.getOslcUrl() + path, OslcProtocolVersion.OSLC_20_RDF_XML)));
    }

    public OslcResponse testModifyResource(OslcAlmServerDescription server, String path, String body, String etag) throws RestException, ConstraintViolationReportedByServer {
        assert (server != null);
        assert (path != null);

        OslcCommandExecutor executor = connect(server);
        Map<String, String> headerData = new TreeMap<>();
        headerData.put("If-Match", etag);

        return (executor.performPutRequest(OslcProtocolVersion.OSLC_20_RDF_XML, server.getOslcUrl() + path, body, headerData));
    }
    
    public OslcResponse testCreateResource(OslcAlmServerDescription server, String path, String body) throws RestException, ConstraintViolationReportedByServer {
        assert(server != null);
        assert(path != null);
        
        OslcCommandExecutor executor = connect(server);
        
        return (executor.performPostRequest(OslcProtocolVersion.OSLC_20_RDF_XML, server.getOslcUrl() + path, body));
               
    }
}