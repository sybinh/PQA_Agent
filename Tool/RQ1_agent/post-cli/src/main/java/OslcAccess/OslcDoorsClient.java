/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import Doors.DoorsRecordFactory;
import OslcAccess.Exceptions.ConstraintViolationReportedByServer;
import OslcAccess.Exceptions.InternalException;
import OslcAccess.Exceptions.OslcException;
import OslcAccess.OslcCommandExecutor.ResponseFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gug2wi
 */
public class OslcDoorsClient {

    static private final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(OslcDoorsClient.class.getCanonicalName());

    //--------------------------------------------------------------------------
    //
    // Handle singleton client object
    //
    //--------------------------------------------------------------------------
    static final public OslcDoorsClient client = new OslcDoorsClient();

    private OslcDoorsServerDescription serverDescription = null;
    private OslcCommandExecutor executor = null;

    private OslcDoorsClient() {
    }

    static public String getCurrentDatabaseName() {
        if (client.serverDescription != null) {
            return (client.serverDescription.getName());
        } else {
            return (null);
        }
    }

    public OslcDoorsServerDescription getServerDescription() throws OslcException {
        if (serverDescription == null) {
            try {
                connect();
            } catch (ConstraintViolationReportedByServer ex) {
                LOGGER.log(Level.SEVERE, "Unexpected constraint error.", ex);
                InternalException ie = new InternalException("Unexpected during connecting", ex);
                throw (ie);
            }
        }
        return serverDescription;
    }

    public boolean isDxlAvailable(OslcDoorsServerDescription.DxlFeature dxlVersion) {
        assert (dxlVersion != null);

        try {
            OslcDoorsServerDescription sd = OslcDoorsClient.client.getServerDescription();
            return (sd.isDxlAvailable(dxlVersion));
        } catch (OslcException ex) {
            Logger.getLogger(DoorsRecordFactory.class.getName()).log(Level.SEVERE, "Could not load server description.", ex);
            return (false);
        }
    }

    //--------------------------------------------------------------------------
    //
    // Execution of server requests
    //
    //--------------------------------------------------------------------------
    public void connect() throws OslcException, ConstraintViolationReportedByServer {

        //--------------------------------------------------
        // Do nothing, if connection is already established
        //--------------------------------------------------
        if (executor != null) {
            return;
        }

        //---------------------------
        // Set server and login data
        //---------------------------
        OslcLoginData loginData = OslcLoginManager.getFirstLoginData();
        serverDescription = loginData.getServerDescription().getDoorsServer();

        //-------------------
        // Get request token
        //-------------------
        OslcCommandExecutor oAuthExecutor = new OslcCommandExecutor(serverDescription, new EmptyAuthenticationProvider());
        String requestTokenUrl = serverDescription.getOslcUrl() + "/dwa/oauth-request-token";
        OAuthAuthenticationProvider oAuth = new OAuthAuthenticationProvider(serverDescription.getConsumerKey(), serverDescription.getConsumerSecret());
        oAuthExecutor.setAuthenticationProvider(oAuth);
        OAuthResponse requestTokenResponse = new OAuthResponse(oAuthExecutor.performPostRequest(OslcProtocolVersion.NONE, requestTokenUrl));
        String requestToken = requestTokenResponse.getAuthToken();
        String requestTokenSecret = requestTokenResponse.getAuthTokenSecret();

        //---------------
        // Login as user
        //---------------
        String loginUrl1 = serverDescription.getOslcUrl() + "/dwa/oauth-authorize-token" + "?oauth_token=" + OAuthAuthenticationProvider.urlEncode(requestToken);

        OslcCommandExecutor loginExecutor = new OslcCommandExecutor(serverDescription, new EmptyAuthenticationProvider());
        loginExecutor.performGetRequest(OslcProtocolVersion.NONE, serverDescription.getOslcUrl() + "/dwa/oauth-authorize-token" + "?oauth_token=" + OAuthAuthenticationProvider.urlEncode(requestToken), ResponseFormat.TEXT_OR_XML);

        String loginUrl2 = serverDescription.getOslcUrl() + "/dwa/oauth/j_acegi_security_check";
        Map<String, String> loginParameter2 = new TreeMap<>();
        loginParameter2.put("j_username", loginData.getLoginName());
        loginParameter2.put("j_password", new String(loginData.getPassword()));
        loginExecutor.performPostRequest(OslcProtocolVersion.NONE, loginUrl2, loginParameter2);

        //------------------
        // Authorize token
        //------------------
        String authorizeUrl = serverDescription.getOslcUrl() + "/dwa/oauth-authorize-token?oauth_token=" + requestToken;
        OAuthResponse authorizeResponse = new OAuthResponse(loginExecutor.performPostRequest(OslcProtocolVersion.NONE, authorizeUrl));
        String reuqestTokenReplied = authorizeResponse.getAccessToken();
        String accessVerifier = authorizeResponse.getAccessVerifier();

        //------------------
        // Get access token
        //------------------
        oAuth.setOAuthToken(requestToken, requestTokenSecret);
        String accessTokenUrl = serverDescription.getOslcUrl() + "/dwa/oauth-access-token";
        OAuthResponse accessTokenResponse = new OAuthResponse(oAuthExecutor.performGetRequest(OslcProtocolVersion.NONE, accessTokenUrl, ResponseFormat.TEXT_OR_XML));
        String accessToken = accessTokenResponse.getAuthToken();
        String accessTokenSecret = accessTokenResponse.getAuthTokenSecret();

        //------------------
        // Set access token
        //------------------
        oAuth.setOAuthToken(accessToken, accessTokenSecret);
        executor = oAuthExecutor;
    }

    private OslcResponse executeGetCommand(String path) throws OslcException {
        assert (path != null);
        assert (path.isEmpty() == false);

        try {
            connect();
            return (executor.executeCommand(new OslcDoorsGetCommand(path)));
        } catch (ConstraintViolationReportedByServer ex) {
            LOGGER.log(Level.SEVERE, "Unexpected constraint error.", ex);
            InternalException ie = new InternalException("Unexpected constraint error for " + path, ex);
            throw (ie);
        }
    }

    public OslcDoorsDxlResponse executeDxlScript(String command, String parameter) throws OslcException {
        assert (command != null);
        assert (parameter != null);

        OslcResponse oslcResponse = null;
        try {
            connect();
            oslcResponse = executor.executeCommand(new OslcDoorsDxlCommand(serverDescription.getDxlUrl(), command, parameter));
        } catch (ConstraintViolationReportedByServer ex) {
            LOGGER.log(Level.SEVERE, "Unexpected constraint error.", ex);
            InternalException ie = new InternalException("Unexpected constraint error for " + serverDescription.getDxlUrl() + " " + command, ex);
            throw (ie);
        }

        return (new OslcDoorsDxlResponse(oslcResponse));
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdownHttpConnection();
            executor = null;
        }
    }

    //--------------------------------------------------------------------------
    //
    // Get specific responses from database
    //
    //--------------------------------------------------------------------------
    public OslcDoorsCatalogResponse getDatabase() throws OslcException {
        return (new OslcDoorsCatalogResponse(executeGetCommand("dwa/rm/discovery/catalog")));
    }

    public OslcDoorsCatalogResponse getServiceProviderCatalog(String moduleServiceRdfAbout) throws OslcException {
        assert (moduleServiceRdfAbout != null);
        assert (moduleServiceRdfAbout.isEmpty() == false);

        return (new OslcDoorsCatalogResponse(executeGetCommand(getPathFromRdfAbout(moduleServiceRdfAbout))));
    }

    public OslcDoorsModuleResponse getModule(String moduleRdfAbout) throws OslcException {
        assert (moduleRdfAbout != null);
        assert (moduleRdfAbout.isEmpty() == false);

        return (new OslcDoorsModuleResponse(executeGetCommand(moduleRdfAbout)));
    }

    public OslcDoorsModuleServiceResponse getModuleService(String rdfAbout) throws OslcException {
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);

        return (new OslcDoorsModuleServiceResponse(executeGetCommand(getPathFromRdfAbout(rdfAbout))));
    }

    public OslcDoorsPropertyResponse getModuleProperties(String rdfResourceShape) throws OslcException {
        assert (rdfResourceShape != null);
        assert (rdfResourceShape.isEmpty() == false);

        return (new OslcDoorsPropertyResponse(executeGetCommand(getPathFromRdfAbout(rdfResourceShape))));
    }

    public OslcDoorsObjectQueryResponse getObjectsFromModule(String queryUrl) throws OslcException {
        assert (queryUrl != null);
        assert (queryUrl.isEmpty() == false);

        OslcResponse oslcResponse = executeGetCommand(getPathFromRdfAbout(queryUrl));
        OslcDoorsObjectQueryResponse queryResponse = new OslcDoorsObjectQueryResponse(oslcResponse);
        return (queryResponse);
    }

    public OslcDoorsObjectQueryResponse getObject(String objectRdfAbout) throws OslcException {
        assert (objectRdfAbout != null);
        assert (objectRdfAbout.isEmpty() == false);

        return (new OslcDoorsObjectQueryResponse(executeGetCommand(getPathFromRdfAbout(objectRdfAbout))));
    }

    /**
     * Module private to enable testing.
     */
    static String getPathFromRdfAbout(String rdfAbout) {
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);
        assert (rdfAbout.startsWith("https://")) : rdfAbout;

        int pathIndex = rdfAbout.indexOf("/", 8);

        return (rdfAbout.substring(pathIndex + 1));
    }

}
