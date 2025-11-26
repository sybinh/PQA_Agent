/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.TemporaryServerErrorDialog.DialogResult;
import RestClient.Authentication.AuthenticationProviderI;
import RestClient.Authentication.OAuthAuthenticationProvider;
import RestClient.CookieHandler;
import RestClient.Exceptions.AuthenticationLoss;
import RestClient.Exceptions.AuthorizationException;
import RestClient.Exceptions.BadRequestException;
import RestClient.Exceptions.ConnectionRefusedException;
import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.FileBufferOverrunException;
import RestClient.Exceptions.FileSizeException;
import RestClient.Exceptions.InternalServerErrorException;
import RestClient.Exceptions.NoLoginDataException;
import RestClient.Exceptions.NotFoundException;
import RestClient.Exceptions.OutOfMemoryException;
import RestClient.Exceptions.OverloadException;
import RestClient.Exceptions.RejectionException;
import RestClient.Exceptions.ResponseException;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestInternalException;
import RestClient.Exceptions.SocketException;
import RestClient.Exceptions.TemporaryRejectionException;
import RestClient.Exceptions.TemporaryServerError;
import RestClient.Exceptions.ToolAccessException;
import RestClient.Exceptions.UnknownHostException;
import RestClient.Exceptions.UnprocessableEntityException;
import RestClient.Exceptions.WorkaroundForServerErrorException;
import RestClient.RestCertificateManager;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.UiThreadChecker;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import util.*;

/**
 * Implements the access to a OSLC server.
 *
 * @author gug2wi
 */
public class OslcCommandExecutor {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(OslcCommandExecutor.class.getCanonicalName());
    private static final java.util.logging.Logger LOGGER_STACK = java.util.logging.Logger.getLogger(OslcCommandExecutor.class.getCanonicalName() + "_Stack");
    private static final java.util.logging.Logger LOGGER_DELETE = java.util.logging.Logger.getLogger(OslcCommandExecutor.class.getCanonicalName() + "_Delete");
    //
    final private OslcServerDescriptionI serverDescription;
    private AuthenticationProviderI authProvider;
    final private OslcResponseFactoryI responseFactory;
    //
    private HttpClient httpClient;
    private final CookieHandler cockieHandler;
    private boolean isConnectionEstablished = false;
    private int requestCounter = 0;

    public static enum ResponseFormat {
        BINARY,
        TEXT_OR_XML
    }

    static final CharSequence[][] nextUrlDecodeTable = {
            {"&", "&amp;"}, // &amp has to be the first element to encode and the last to decode when replace() is used !
            {"<", "&lt;"},
            {">", "&gt;"},
            {"\"", "&quot;"},
            {"'", "&apos;"},
            {"\r", "&#13;"},
            {"/", "%2F"},
            {":", "%3A"},
            {"=", "%3D"},
            {"<", "%3C"},
            {">", "%3E"},
            {" ", "%20"},
            {",", "%2C"},
            {"\"", "%22"},
            {"[", "%5B"},
            {"]", "%5D"},
            {"{", "%7B"},
            {"}", "%7D"}}; // CR

    //
    // Package private to enable testing
    //
    public OslcCommandExecutor(OslcServerDescriptionI serverDescription, AuthenticationProviderI authProvider, OslcResponseFactoryI responseFactory) {
        assert (serverDescription != null);
        assert (authProvider != null);
        assert (responseFactory != null);

        this.serverDescription = serverDescription;
        this.authProvider = authProvider;
        this.responseFactory = responseFactory;
        cockieHandler = new CookieHandler();
        httpClient = null;

        RestCertificateManager.setStore();
    }

    public void setAuthenticationProvider(OAuthAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    //
    // Protected to enable testing
    //
    public OslcResponse performGetRequest(OslcProtocolVersion version, String urlString, ResponseFormat responseFormat) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (urlString.startsWith("http")) : urlString;
        assert (responseFormat != null);

        boolean isLongCommand = false;

        //
        // Set parameters for first request-response cycle
        //
        OslcResponse completeResponse = null;
        String nextUrlString = urlString;

        do {
            logger.log(Level.INFO, "URL = >{0}<", nextUrlString);
            logger.log(Level.INFO, "Version = >{0}<", version.name());

            //
            // Create URI object for this request-response cycle
            //
            URI uri = null;
            try {
                java.net.URL urlValue = new java.net.URL(nextUrlString);
                String authority = urlValue.getAuthority();
                String path = urlValue.getPath();
                String query = urlValue.getQuery();
                uri = new URI("https", authority, path, query, null);

            } catch (URISyntaxException ex) {
                ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_URISyntaxException");
                throw (new RestInternalException("Creation of URI for urlString=\"" + nextUrlString + "\" failed.", ex));
            } catch (MalformedURLException ex) {
                ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_MalformedURLException");
                throw (new RestInternalException("Creation of URL for urlString=\"" + nextUrlString + "\" failed." + ex));
            }

            //
            // Create GetMethod for this request-response cycle
            //
            HttpGet getMethod = new HttpGet();
            getMethod.setURI(uri);
            version.addRequestHeader(getMethod);

            //
            // Perform the http communication for this request-response cycle
            //
            OslcResponse response = performHttpRequest(getMethod, responseFormat);

            //
            // Add response of the cycle to the complete response
            //
            if (completeResponse == null) {
                completeResponse = response;
            } else {
                completeResponse.addResponse(response);
            }

            //
            // Log total count
            //
            logger.info("Total Count = " + response.getTotalCount());

            //
            // Get url for next cycle
            //
            nextUrlString = null;
            String nextUrlStringProposedByServer = response.getUrlForNextPage();
            if (nextUrlStringProposedByServer != null) {
                nextUrlString = EcvXmlElement.decodeXml(nextUrlStringProposedByServer, nextUrlDecodeTable);
                isLongCommand = true;
                int i_start = nextUrlString.indexOf("startIndex=");
                if (i_start >= 0) {
                    int i_end = nextUrlString.indexOf("&", i_start + 1);
                    if (i_end < 0) {
                        i_end = nextUrlString.length();
                    }
                    logger.warning("Long command running (" + nextUrlString.substring(i_start + 11, i_end) + "/" + response.getTotalCount() + "): " + nextUrlString);
                } else {
                    logger.warning("Long command running (?/" + response.getTotalCount() + "): " + nextUrlString);
                }
            }

        } while (nextUrlString != null);

        if (isLongCommand == true) {
            logger.warning("Long command done.");
        }

        return (completeResponse);
    }

    //
    // Protected to enable testing
    //
    public OslcResponse performPutRequest(OslcProtocolVersion version, String urlString, String body, Map<String, String> headerData) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (body != null);
        assert (body.isEmpty() == false);

        logger.log(Level.INFO, "URL = >{0}<", urlString);
        logger.log(Level.INFO, "INFO:\n{0}", body);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI("https", authority, path, query, null);
        } catch (URISyntaxException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_URISyntaxException");
            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_MalformedURLException");
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        StringEntity requestEntity = null;

        requestEntity = new StringEntity(body, ContentType.create("application/rdf+xml", "utf-8"));

        HttpPut putMethod = new HttpPut();

        putMethod.setURI(uri);

        putMethod.setEntity(requestEntity);
        version.addRequestHeader(putMethod);

        if (headerData != null) {
            for (Map.Entry<String, String> entry : headerData.entrySet()) {
                putMethod.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return (performHttpRequest(putMethod, ResponseFormat.TEXT_OR_XML));
    }

    //
    // Protected to enable testing
    //
    protected OslcResponse performDeleteRequest(OslcProtocolVersion version, String urlString) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);

        logger.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI("https", authority, path, query, null);
        } catch (URISyntaxException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_URISyntaxException");
            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_MalformedURLException");
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpDelete deleteMethod = new HttpDelete();

        deleteMethod.setURI(uri);

        version.addRequestHeader(deleteMethod);

        return (performHttpRequest(deleteMethod, ResponseFormat.TEXT_OR_XML));
    }

    public OslcResponse performPostRequest(OslcProtocolVersion version, String urlString, String body) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (body != null);
        assert (body.isEmpty() == false);

        logger.log(Level.INFO, "BODY:\n{0}", body);

        StringEntity requestEntity = null;

        requestEntity = new StringEntity(body, ContentType.create("application/rdf+xml", "utf-8"));
        return (performPostRequest(version, urlString, requestEntity));
    }

    private OslcResponse performPostRequest(OslcProtocolVersion version, String urlString, File file, Map<String, String> parameter) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (parameter != null);
        assert (file != null);
        assert (file.canRead());

        logger.log(Level.INFO, "PATH:\n{0}", file.getAbsolutePath());

        //
        // Create part list
        //
        //
        // Create and add part for file
        //
        String fileName = Normalizer.normalize(file.getName(), Normalizer.Form.NFD).replaceAll("[^\\x00-\\x7F]", "").replaceAll("\\?", "_");
        MultipartEntityBuilder entitybuilder = MultipartEntityBuilder.create();
        entitybuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entitybuilder.addBinaryBody("File", file, ContentType.DEFAULT_BINARY, fileName);

        //
        // Create and add parts for parameters
        //
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            entitybuilder.addTextBody(entry.getKey(), entry.getValue());
        }

        HttpEntity requestEntity = entitybuilder.build();
        //
        // Send command
        //

        return (performPostRequest(version, urlString, requestEntity));
    }

    private OslcResponse performPostRequest(OslcProtocolVersion version, String urlString, HttpEntity requestEntity) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (requestEntity != null);

        logger.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI("https", authority, path, query, null);
        } catch (URISyntaxException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_URISyntaxException");
            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_MalformedURLException");
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpPost postMethod = new HttpPost();

        postMethod.setURI(uri);

        postMethod.setEntity(requestEntity);
        version.addRequestHeader(postMethod);

        return (performHttpRequest(postMethod, ResponseFormat.TEXT_OR_XML));
    }

    public OslcResponse performPostRequest(OslcProtocolVersion version, String urlString, Map<String, String> parameters) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (parameters != null);

        logger.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI("https", authority, path, query, null);
        } catch (URISyntaxException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_URISyntaxException");
            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_MalformedURLException");
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpPost postMethod = new HttpPost();

        postMethod.setURI(uri);

        version.addRequestHeader(postMethod);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_UnsupportedEncodingException");
            throw (new RestInternalException("Exception while encoding parameters for request entity" + nameValuePairs + ex));
        }

        return (performHttpRequest(postMethod, ResponseFormat.TEXT_OR_XML));
    }

    public OslcResponse performPostRequest(OslcProtocolVersion version, String urlString) throws RestException, ConstraintViolationReportedByServer {
        assert (version != null);
        assert (urlString != null);
        assert (urlString.isEmpty() == false);

        logger.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI("https", authority, path, query, null);
        } catch (URISyntaxException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_URISyntaxException");
            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_MalformedURLException");
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpPost postMethod = new HttpPost();

        postMethod.setURI(uri);

        version.addRequestHeader(postMethod);

        return (performHttpRequest(postMethod, ResponseFormat.TEXT_OR_XML));
    }

    private OslcResponse performHttpRequest(HttpRequestBase method, ResponseFormat responseFormat) throws RestException, ConstraintViolationReportedByServer {

        assert (method != null);
        assert (responseFormat != null);
        assert (authProvider != null);
        HttpResponse httpResponse;
        requestCounter++;

        if (LOGGER_STACK.isLoggable(Level.FINEST) == true) {
            LOGGER_STACK.log(Level.FINEST, "Start. Request = " + method.getRequestLine(), new Exception("Stack trace for request (no error)"));
        } else {
            logger.log(Level.INFO, "Start. Request = {0}", method.getRequestLine());
        }

        //
        // Save starting time
        //
        Date startDate = new Date();

        //
        // Prepare connection
        //
        if (httpClient == null) {

            httpClient = HttpClientBuilder.create().build();
            authProvider.setAuthData(httpClient);

        }

        //
        // Prepare method
        //
        cockieHandler.popCookies(method);
        authProvider.setAuthData(method);

        if (logger.isLoggable(Level.FINER)) {
            StringBuilder b = new StringBuilder();
            for (Header header : method.getAllHeaders()) {
                if (b.length() > 0) {
                    b.append('&');
                }
                b.append(header.getName()).append('=').append(header.getValue());
            }
            logger.finer("Header = " + b.toString());
        }

        //
        // Execute method
        //
        int statusCode;
        try {
//
            httpResponse = httpClient.execute(method);
            statusCode = httpResponse.getStatusLine().getStatusCode();
        } catch (java.net.UnknownHostException ex) {
            logger.log(Level.WARNING, "Unknown host exception catched.", ex);
            logger.log(Level.WARNING, "URL= ", method.getRequestLine());
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_UnknownHostException");
            throw (new UnknownHostException("Get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        } catch (java.net.ConnectException ex) {
            logger.log(Level.WARNING, "Connect exception catched.", ex);
            logger.log(Level.WARNING, "URL= ", method.getRequestLine());
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_ConnectionRefusedException");
            throw (new ConnectionRefusedException("Get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        } catch (java.net.SocketException ex) {
//            logger.log(Level.WARNING, "Socket Exception catched.", ex);
//            logger.log(Level.WARNING, "URL= ", method.getQueryString());
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_SocketException");
            throw (new SocketException(method.getRequestLine().toString(), ex));
        } catch (IOException ex) {
            if (ex instanceof javax.net.ssl.SSLHandshakeException) {
                logger.severe("Connection refused by server.\nHint: Check certificates in your JRE. Certificates needed to connect to RQ1 server may be missing.");
                ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_SSLHandshakeException");
                throw (new RestInternalException("Get method for urlString=\"" + method.getRequestLine() + "\".\n"
                        + "Hint: Check certificates in your JRE. Certificates needed to connect to RQ1 server may be missing.", ex));
            }
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_RestInternalException");
            throw (new RestInternalException("Get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        }
        cockieHandler.pushCookies(httpResponse);

        //
        // Get headers
        //
        Map<String, String> responseHeader = new TreeMap<>();
        for (Header header : httpResponse.getAllHeaders()) {
            responseHeader.put(header.getName(), header.getValue());
        }

        //
        // Read response
        //
        InputStream byteInput = null;
        try {
            if (httpResponse.getEntity() != null && httpResponse.getEntity().getContent() != null) {
                byteInput = httpResponse.getEntity().getContent();
            }
        } catch (IOException ex) {
            method.releaseConnection();
            ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_RestInternalException");
            throw (new RestInternalException("Retrieve response from get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        }
        if ((responseFormat == ResponseFormat.BINARY) && (statusCode == HttpStatus.SC_OK)) {
            return (processResponseInBinaryFormat(responseHeader, byteInput));
        }

        InputStreamReader inputReader = null;
        if (byteInput != null) {
            try {
                inputReader = new InputStreamReader(byteInput, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                method.releaseConnection();
                ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_RestInternalException");
                throw (new RestInternalException("Decode response from get method for urlString=\"" + method.getRequestLine() + "\".", ex));
            }
        }

        //
        // Save receiving time
        //
        Date receiveDate = new Date();
        logger.log(Level.INFO, "Received. Duration = {0} ms.", (receiveDate.getTime() - startDate.getTime()));

        //
        // Parse response
        //
        EcvXmlElement xmlServerResponse = null;
        String stringServerResponse = null;
        if (inputReader != null) {
            EcvPositionAwarePushbackReader pushBackReader = new EcvPositionAwarePushbackReader(inputReader);
            int firstCharacter;
            try {
                firstCharacter = pushBackReader.read();
                if (firstCharacter >= 0) {
                    pushBackReader.unread(firstCharacter);
                }
            } catch (IOException ex) {
                method.releaseConnection();
                ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_RestInternalException");
                throw (new RestInternalException("Decode response from get method for urlString=\"" + method.getRequestLine() + "\".", ex));
            }
            if (firstCharacter == '<') {
                //
                // Response begins like XML. Parse as XML
                //
                try {
                    EcvXmlParser parser = new EcvXmlParser(pushBackReader);
                    xmlServerResponse = parser.parse();
                    if (logger.isLoggable(Level.FINEST) == true) {
                        logger.log(Level.FINEST, "Raw response:\n{0}", parser.getReadData());
                    }
                } catch (java.lang.OutOfMemoryError ex) {
                    logger.severe(method.getRequestLine().toString());
                    ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_OutOfMemoryError");
                    throw (ex);
                } catch (EcvXmlParser.ParseException ex) {
                    method.releaseConnection();
                    logger.log(Level.SEVERE, "statusCode={0}", Integer.toString(statusCode));
                    ToolUsageLogger.logError(OslcCommandExecutor.class.getCanonicalName(), ex);
                    logger.log(Level.WARNING, "--- ErrorLog Start -------------------------");
                    logger.log(Level.WARNING, ex.getErrorLog());
                    logger.log(Level.WARNING, "--- ErrorLog End ---------------------------");
                    if (ex.endOfInputReached() == true) {
                        throw (new FileBufferOverrunException(method.getRequestLine().toString()));
                    } else {
                        ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_ResponseException");
                        throw (new ResponseException("Parse server response for urlString=\"" + method.getRequestLine() + "\".", ex));
                    }
                }
                if (logger.isLoggable(Level.FINER)) {
                    if (xmlServerResponse != null) {
                        logger.log(Level.FINER, "XML response:\n{0}", xmlServerResponse.getUiString());
                    } else {
                        logger.finer("XML response is empty.");
                    }
                }
            } else {
                //
                // Response does not begin like XML. Read non-XML server response
                //
                try {
                    stringServerResponse = pushBackReader.getNextData(1000);
                } catch (IOException ex) {
                    method.releaseConnection();
                    ToolUsageLogger.addCount(ToolUsageLogger.COMPONENT_CORE, "HttpError", "Http_Error_ResponseException");
                    throw (new ResponseException("Parse server response for urlString=\"" + method.getRequestLine() + "\".", ex));
                }
            }
        }

        //
        // Release connection
        //
        method.releaseConnection();

        //
        // Save end time
        //
        Date endDate = new Date();
        logger.log(Level.INFO, "Processed. Duration = {0} ms.", (endDate.getTime() - startDate.getTime()));

        OslcResponse response = analyseResponse(responseFactory, isConnectionEstablished, method, statusCode, responseHeader, xmlServerResponse, stringServerResponse);
        isConnectionEstablished = true;
        return (response);
    }

    final private static int BUFFER_SIZE = 16352;
    final private static int MAX_NUMBER_OF_BUFFERS = 10000;

    private OslcResponse processResponseInBinaryFormat(Map<String, String> responseHeader, InputStream byteInput) throws FileSizeException, ResponseException {
        assert (responseHeader != null);
        assert (byteInput != null);

        List<byte[]> responseInBinaryFormat = new ArrayList<>();
        int numberOfReceivedBytes = 0;
        do {
            byte[] buffer = new byte[BUFFER_SIZE];
            try {
                numberOfReceivedBytes = byteInput.read(buffer);
            } catch (IOException ex) {
                Logger.getLogger(OslcCommandExecutor.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(OslcCommandExecutor.class.getCanonicalName(), ex);
                throw (new ResponseException("Load file content", "Read failed (" + responseInBinaryFormat.size() + ")"));
            }
            if (numberOfReceivedBytes == BUFFER_SIZE) {
                responseInBinaryFormat.add(buffer);
            } else if (numberOfReceivedBytes > 0) {
                byte[] part = Arrays.copyOf(buffer, numberOfReceivedBytes);
                responseInBinaryFormat.add(part);
            }
        } while ((numberOfReceivedBytes > 0) && (responseInBinaryFormat.size() < MAX_NUMBER_OF_BUFFERS));

        if (numberOfReceivedBytes == BUFFER_SIZE) {
            throw (new FileSizeException());
            // Loading was stopped because response is to big
        }

        return (responseFactory.build(responseHeader, responseInBinaryFormat));
    }

    //
    // Seperated in method and made public to simplify testing.
    //
    static public OslcResponse analyseResponse(OslcResponseFactoryI responseFactory, boolean isConnectionEstablished, HttpRequestBase method, int statusCode, Map<String, String> responseHeader, EcvXmlElement xmlServerResponse, String stringServerResponse) throws AuthorizationException, NotFoundException, RejectionException, RestInternalException, ConstraintViolationReportedByServer, TemporaryServerError, BadRequestException, OutOfMemoryException, ToolAccessException {
        assert (responseFactory != null);
        assert (method != null);
        assert (responseHeader != null);

        String queryString = method.getRequestLine().toString();
        URI uri = method.getURI();
        if (uri != null) {
            queryString = uri.toString();
        }
        logger.log(Level.FINE, "statusCode={0}", Integer.toString(statusCode));
        logger.log(Level.FINER, "response={0}", stringServerResponse);

        switch (statusCode) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_CREATED:
            case HttpStatus.SC_NO_CONTENT:
            case HttpStatus.SC_MOVED_TEMPORARILY:
                return (responseFactory.build(responseHeader, xmlServerResponse, stringServerResponse));

            case HttpStatus.SC_UNAUTHORIZED:
                logger.log(Level.FINER, "response body ={0}", stringServerResponse);
                logger.log(Level.FINER, "response header ={0}", responseHeader);
                if (isConnectionEstablished) {
                    throw (new AuthenticationLoss("Execute get method for urlString=\"" + queryString + "\"."));
                } else {
                    throw (new AuthorizationException("Execute get method for urlString=\"" + queryString + "\"."));
                }

            case HttpStatus.SC_FORBIDDEN:
                analyseForbidden_Http_403(queryString, statusCode, xmlServerResponse, stringServerResponse);

            case HttpStatus.SC_NOT_FOUND:
                analyseNotFound_Http_404(queryString, statusCode, responseHeader, xmlServerResponse, stringServerResponse);

            case HttpStatus.SC_CONFLICT:
                analyseConflict_Http_409(queryString, statusCode, xmlServerResponse, stringServerResponse);

            case HttpStatus.SC_BAD_REQUEST:
                logger.log(Level.WARNING, "url={0}", queryString);
                logger.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
                if (xmlServerResponse != null) {
                    logger.warning(xmlServerResponse.getUiString());
                }
                if (stringServerResponse != null) {
                    logger.warning(stringServerResponse);
                }
                if (xmlServerResponse != null) {
                    if (xmlServerResponse.getUiString().indexOf("OutOfMemory") >= 0) {
                        throw (new OutOfMemoryException(queryString));
                    }
                    if (xmlServerResponse instanceof EcvXmlContainerElement) {
                        EcvXmlContainerElement xmlContainerResponse = (EcvXmlContainerElement) xmlServerResponse;
                        try {
                            String message = xmlContainerResponse.getText("message");
                            throw (new BadRequestException(message));
                        } catch (EcvXmlElement.NotfoundException ex) {
                            //
                        }
                    }
                }
                throw (new RejectionException(statusCode, "Execute get method for urlString=\"" + queryString + "\"."));

            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                analyseInternalServerError_Http_500(queryString, statusCode, xmlServerResponse);

            case HttpStatus.SC_UNPROCESSABLE_ENTITY:
                analyseUnprocessable_Http_422(queryString, statusCode, xmlServerResponse);

            case HttpStatus.SC_GONE:
                analyseGone_Http_410(queryString, statusCode, responseHeader, xmlServerResponse, stringServerResponse);

            default:
                logger.log(Level.WARNING, "url={0}", queryString);
                logger.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
                if (xmlServerResponse != null) {
                    logger.warning(xmlServerResponse.getUiString());
                }
                if (stringServerResponse != null) {
                    logger.warning(stringServerResponse);
                }
                throw (new RestInternalException("Check status code from server. Status code " + statusCode + " unknown."));
        }

    }

    static private void analyseForbidden_Http_403(String queryString, int statusCode, EcvXmlElement xmlServerResponse, String stringServerResponse) throws ConstraintViolationReportedByServer, OutOfMemoryException, NotFoundException, RejectionException, ToolAccessException, WorkaroundForServerErrorException {
        if (xmlServerResponse != null) {
            if (xmlServerResponse.getName().equals("rdf:RDF")) {
                if (xmlServerResponse instanceof EcvXmlContainerElement) {
                    try {
                        EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse).getContainerElement("Error");
                        String topMessage = error.getTextElement("message").getText();
                        if ((statusCode == HttpStatus.SC_CONFLICT) && (error.containsElement("details"))) {
                            List<ConstraintViolationReportedByServer.FieldConstraintViolation> fields = new ArrayList<>();
                            EcvXmlContainerElement invalidFields = error.getContainerElement("details").getContainerElement("invalidFields");
                            for (EcvXmlContainerElement invalidField : invalidFields.getContainerElementList()) {
                                fields.add(new ConstraintViolationReportedByServer.FieldConstraintViolation(invalidField.getName(), invalidField.getTextElement("message").getText()));
                            }
                            throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, fields));
                        } else if (error.containsElement("cq:error")) {
                            String detailedMessage = error.getContainerElement("cq:error").getContainerElement("Error").getTextElement("message").getText();
                            ConstraintViolationReportedByServer constraintViolation = new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, detailedMessage);
                            if (detailedMessage.contains("Simple.pm line 414")) {
                                throw (new WorkaroundForServerErrorException("Workaround for Simple.pm line 414", constraintViolation));
                            }
                            throw (constraintViolation);
                        } else if (error.containsElement("OutOfMemory")) {
                            logger.warning("Query: " + queryString);
                            logger.warning("Status Code: " + statusCode);
                            logger.warning("Error: " + error);
                            logger.warning("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                            throw (new OutOfMemoryException(topMessage));
                        } else if ((topMessage != null) && (topMessage.contains("OutOfMemory"))) {
                            logger.warning("Query: " + queryString);
                            logger.warning("Status Code: " + statusCode);
                            logger.warning("Error: " + error);
                            logger.warning("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                            throw (new OutOfMemoryException(topMessage));
                        } else {
                            logger.severe("Query: " + queryString);
                            logger.severe("Status Code: " + statusCode);
                            logger.severe("Error: " + error);
                            logger.severe("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                            throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, topMessage));
                        }
                    } catch (EcvXmlElement.NotfoundException ex) {
                        // Do nothing. Simply jump to the warning trace below.
                    }
                }
            } else if (xmlServerResponse.getName().equals("Error")) {
                try {
                    EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse);
                    String topMessage = error.getTextElement("message").getText();
                    logger.severe("Query: " + queryString);
                    logger.severe("Status Code: " + statusCode);
                    logger.severe("Error: " + error);
                    logger.severe("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                    if (topMessage.startsWith("CRVSV0078E Fehler vom RPC-Server:CRVSV1094E Not-found error")) {
                        throw (new NotFoundException());
                    } else if (error.containsContainer("Error")) {
                        String detailedMessage = "";
                        if (error.getContainerElement("Error").containsElement("message")) {
                            detailedMessage += (error.getContainerElement("Error").getText("message"));
                        }

                        if (error.getContainerElement("Error").containsElement("statusCode")) {
                            detailedMessage += ("\n");
                            detailedMessage += ("StatusCode: " + error.getContainerElement("Error").getText("statusCode"));
                        }
                        if (!detailedMessage.isEmpty()) {
                            throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, detailedMessage));
                        }
                    } else {
                        throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, topMessage));
                    }

                } catch (EcvXmlElement.NotfoundException ex) {
                    // Do nothing. Simply jump to the warning trace below.
                }
            } else if (xmlServerResponse.getName().equals("html")) {
                String s = xmlServerResponse.toString();
                if (s.contains("You don&apos;t have permission to access ") == true) {
                    logger.warning("Connection rejected because of invalid toolname and/or toolversion.");
                    throw (new ToolAccessException());
                }
            }
        }
        logger.warning("Unknown response for SC_NOT_FOUND/SC_CONFLICT/SC_FORBIDDEN");
        logger.log(Level.WARNING, "url={0}", queryString);
        logger.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
        if (xmlServerResponse != null) {
            logger.warning(xmlServerResponse.getUiString());
        }
        if (stringServerResponse != null) {
            logger.warning(stringServerResponse);
        }
        throw (new RejectionException(statusCode, "Execute get method for urlString=\"" + queryString + "\"."));
    }

    static private void analyseNotFound_Http_404(String queryString, int statusCode, Map<String, String> responseHeader, EcvXmlElement xmlServerResponse, String stringServerResponse) throws NotFoundException, ConstraintViolationReportedByServer, RejectionException, AuthenticationLoss, TemporaryRejectionException {
        if (xmlServerResponse != null) {
            if (xmlServerResponse.getName().equals("rdf:RDF")) {
                if (xmlServerResponse instanceof EcvXmlContainerElement) {
                    try {
                        EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse).getContainerElement("Error");
                        String topMessage = error.getTextElement("message").getText();
                        if ((topMessage != null) && (topMessage.startsWith("CRRCM9996E:"))) {
                            logger.warning("Query: " + queryString);
                            logger.warning("Status Code: " + statusCode);
                            logger.warning("Error: " + topMessage);
                            logger.warning("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                            throw (new NotFoundException());
                        }
                    } catch (EcvXmlElement.NotfoundException ex) {
                        // Do nothing. Simply jump to the warning trace below.
                    }
                    try {
                        // Special handling for DOORS authentication problem
                        EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse).getContainerElement("oslc:Error");
                        String message = error.getTextElement("oslc:message").getText();
                        if ("Authentication has failed because the user name is not recognised.".equals(message)) {
                            logger.warning("DOORS server delivers 'Authentication has failed because the user name is not recognised.'");
                            throw (new TemporaryRejectionException(statusCode, message));
                        } else if ((message != null) && (message.startsWith("The DXL Service [")) && (message.endsWith("] was not found"))) {
                            logger.warning("DOORS server delivers '" + message + "'");
                            throw (new TemporaryRejectionException(statusCode, message));
                        }
                    } catch (EcvXmlElement.NotfoundException ex) {
                        // Do nothing. Simply jump to the warning trace below.
                    }
                }
            } else if (xmlServerResponse.getName().equals("Error")) {
                try {
                    EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse);
                    String topMessage = error.getTextElement("message").getText();
                    logger.severe("Query: " + queryString);
                    logger.severe("Status Code: " + statusCode);
                    logger.severe("Error: " + error);
                    logger.severe("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                    if (topMessage.startsWith("CRVSV0078E Fehler vom RPC-Server:CRVSV1094E Not-found error")) {
                        throw (new NotFoundException());
                    } else {
                        throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, topMessage));
                    }
                } catch (EcvXmlElement.NotfoundException ex) {
                    // Do nothing. Simply jump to the warning trace below.
                }
            } else if (xmlServerResponse instanceof EcvXmlContainerElement) {
                EcvXmlContainerElement responseContainer = (EcvXmlContainerElement) xmlServerResponse;
                if (responseContainer.containsContainer("oslc:Error")) {
                    try {
                        EcvXmlContainerElement oslcError = (EcvXmlContainerElement) responseContainer.getContainerElement("oslc:Error");
                        if (oslcError.getText("oslc:Message").equals("Authentication has failed because the user name is not recognised.")) {
                            throw (new AuthenticationLoss("Authentication has failed because the user name is not recognised."));
                        }
                    } catch (EcvXmlElement.NotfoundException ex) {
                        logger.log(Level.SEVERE, null, ex);
                        ToolUsageLogger.logError(OslcCommandExecutor.class.getCanonicalName(), ex);
                        // Now jump to the warning trace below.
                    }
                }
            }
        }

        logger.warning("Unknown response for SC_NOT_FOUND");
        logger.log(Level.WARNING, "url={0}", queryString);
        logger.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
        if (responseHeader != null) {
            logger.warning("Response Header\n" + responseHeader.toString());
        }
        if (xmlServerResponse != null) {
            logger.warning("XML Server Response\n" + xmlServerResponse.getUiString());
        }
        if (stringServerResponse != null) {
            logger.warning("String Server Response\n" + stringServerResponse);
        }
        throw (new NotFoundException(queryString));
    }

    static private void analyseConflict_Http_409(String queryString, int statusCode, EcvXmlElement xmlServerResponse, String stringServerResponse) throws ConstraintViolationReportedByServer, RejectionException, NotFoundException, TemporaryServerError {
        if (xmlServerResponse instanceof EcvXmlContainerElement) {
            switch (xmlServerResponse.getName()) {
                case "rdf:RDF":
                    try {
                        EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse).getContainerElement("Error");
                        String topMessage = error.getTextElement("message").getText();

                        if (error.containsElement("details")) {
                            List<ConstraintViolationReportedByServer.FieldConstraintViolation> fields = new ArrayList<>();
                            EcvXmlContainerElement invalidFields = error.getContainerElement("details").getContainerElement("invalidFields");
                            for (EcvXmlContainerElement invalidField : invalidFields.getContainerElementList()) {
                                fields.add(new ConstraintViolationReportedByServer.FieldConstraintViolation(invalidField.getName(), invalidField.getTextElement("message").getText()));
                            }
                            throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, fields));

                        } else if ((topMessage != null) && (topMessage.contains("Serverauslastung ist zu hoch"))) {
                            logger.warning("Query: " + queryString);
                            logger.warning("Status Code: " + statusCode);
                            logger.warning("Error: " + error);
                            logger.warning("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                            throw (new OverloadException(topMessage));

                        } else if (error.containsElement("cq:error")) {
                            Set<String> detailedMessages = new TreeSet<>();
                            for (EcvXmlContainerElement cq_error : error.getContainerElementList("cq:error")) {
                                String detailedMessage = cq_error.getContainerElement("Error").getTextElement("message").getText();
                                detailedMessages.add(detailedMessage);
                            }
                            throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, detailedMessages));

                        } else if ((error.containsElement("OutOfMemory"))
                                || ((topMessage != null) && (topMessage.contains("OutOfMemory")))) {
                            logger.warning("Query: " + queryString);
                            logger.warning("Status Code: " + statusCode);
                            logger.warning("Error: " + error);
                            logger.warning("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                            throw (new OutOfMemoryException(topMessage));

                        } else {
                            logger.severe("Query: " + queryString);
                            logger.severe("Status Code: " + statusCode);
                            logger.severe("Error: " + error);
                            logger.severe("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                            throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, topMessage));
                        }
                    } catch (EcvXmlElement.NotfoundException ex) {
                        // Do nothing. Simply jump to the warning trace below.
                    }
                    break;

                case "Error":
                    try {
                        EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse);
                        String topMessage = error.getTextElement("message").getText();
                        logger.severe("Query: " + queryString);
                        logger.severe("Status Code: " + statusCode);
                        logger.severe("Error: " + error);
                        logger.severe("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                        if (topMessage.startsWith("CRVSV0078E Fehler vom RPC-Server:CRVSV1094E Not-found error")) {
                            throw (new NotFoundException());
                        } else {
                            if (error.containsContainer("Error")) {
                                ArrayList<String> fields = new ArrayList<>();

                                if (error.getContainerElement("Error").containsElement("message")) {
                                    topMessage += ("\n");
                                    topMessage += (error.getContainerElement("Error").getText("message"));
                                }
                                for (EcvXmlContainerElement errors : error.getContainerElementList("Error")) {
                                    if (errors.containsContainer("Error")) {
                                        if (errors.containsElement("message")) {
                                            fields.add(errors.getContainerElement("Error").getText("message"));
                                        }
                                        if (errors.containsElement("statusCode")) {
                                            if (topMessage.contains("StatusCode: " + errors.getContainerElement("Error").getText("statusCode")) == false) {
                                                topMessage += ("\n");
                                                topMessage += ("StatusCode: " + errors.getContainerElement("Error").getText("statusCode"));

                                            }
                                        }

                                    }
                                }
                                List<ConstraintViolationReportedByServer.FieldConstraintViolation> result = new ArrayList<>();
                                for (String field : fields) {
                                    if (field.split("\"")[1] != null) {
                                        result.add(new ConstraintViolationReportedByServer.FieldConstraintViolation(field.split("\"")[1], field));
                                    }
                                }
                                if (result.size() > 0) {
                                    System.out.println(result.size());
                                    throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, result));
                                }

                            }
                            throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, topMessage));
                        }
                    } catch (EcvXmlElement.NotfoundException ex) {
                        // Do nothing. Simply jump to the warning trace below.
                    }
                    break;
            }
        }

        logger.warning("Unknown response for SC_NOT_FOUND/SC_CONFLICT/SC_FORBIDDEN");
        logger.log(Level.WARNING, "url={0}", queryString);
        logger.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
        if (xmlServerResponse != null) {
            logger.warning(xmlServerResponse.getUiString());
        }
        if (stringServerResponse != null) {
            logger.warning(stringServerResponse);
        }
        throw (new RejectionException(statusCode, "Execute get method for urlString=\"" + queryString + "\"."));

    }

    static private void analyseGone_Http_410(String queryString, int statusCode, Map<String, String> responseHeader, EcvXmlElement xmlServerResponse, String stringServerResponse) throws NotFoundException, ConstraintViolationReportedByServer, RejectionException, AuthenticationLoss, TemporaryRejectionException {

        logger.warning("Unknown response for SC_GONE");
        logger.log(Level.WARNING, "url={0}", queryString);
        logger.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
        if (responseHeader != null) {
            logger.warning("Response Header\n" + responseHeader.toString());
        }
        if (xmlServerResponse != null) {
            logger.warning("XML Server Response\n" + xmlServerResponse.getUiString());
        }
        if (stringServerResponse != null) {
            logger.warning("String Server Response\n" + stringServerResponse);
        }
        throw (new NotFoundException(queryString));
    }

    static private void analyseInternalServerError_Http_500(String queryString, int statusCode, EcvXmlElement xmlServerResponse) throws InternalServerErrorException, ConstraintViolationReportedByServer {

        logger.warning("Query: " + queryString);
        logger.warning("Status Code: " + statusCode);

        //
        // Check for known errors
        //
        if (xmlServerResponse instanceof EcvXmlContainerElement) {
            if (xmlServerResponse.getName().equals("Error")) {
                try {
                    EcvXmlContainerElement error = ((EcvXmlContainerElement) xmlServerResponse);
                    String topMessage = error.getTextElement("message").getText();
                    logger.severe("Query: " + queryString);
                    logger.severe("Status Code: " + statusCode);
                    logger.severe("Error: " + error);
                    logger.severe("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
                    throw (new ConstraintViolationReportedByServer(statusCode, (EcvXmlContainerElement) xmlServerResponse, topMessage, topMessage));
                } catch (EcvXmlElement.NotfoundException ex) {
                    // No error message found. Do nothing. Continue with the non specific error handling below.
                }
            }

            StringBuilder b = new StringBuilder(100);
            try {
                EcvXmlContainerElement body = ((EcvXmlContainerElement) xmlServerResponse).getContainerElement("body");
                for (EcvXmlElement xmlElement : body.getElementList("p")) {
                    if (xmlElement instanceof EcvXmlTextElement) {
                        if (b.length() > 0) {
                            b.append("\n");
                        }
                        b.append(((EcvXmlTextElement) xmlElement).getText());
                    }
                }
            } catch (EcvXmlElement.NotfoundException e) {
            }

            String topMessage = b.toString();

            logger.warning("Error: " + topMessage);
            logger.warning("XML-Response:\n" + ((EcvXmlContainerElement) xmlServerResponse).getXmlString());
            throw (new InternalServerErrorException(topMessage));
        }

        throw (new InternalServerErrorException("No additional information available."));
    }

    static private void analyseUnprocessable_Http_422(String queryString, int statusCode, EcvXmlElement xmlServerResponse) throws ToolAccessException, UnprocessableEntityException {

        logger.warning("Query: " + queryString);
        logger.warning("Status Code: " + statusCode);

        String responseAsText = xmlServerResponse.getXmlString();

        if (responseAsText.contains("<p>The server understands the media type of the\n"
                + "request entity, but was unable to process the\n"
                + "contained instructions.</p>") == true) {
            //
            // Till now, we see this only when the version sent to the server is invalid.
            //
            throw (new ToolAccessException());
        }

        throw (new UnprocessableEntityException(responseAsText));
    }

    //
    // Protected to enable testing
    //
    public void shutdownHttpConnection() {
        if ((httpClient != null) && (serverDescription.getDeleteUrl() != null)) {

            //
            // Send DELETE command
            //
            if (serverDescription != null) {
                try {
                    performDeleteRequest(OslcProtocolVersion.OSLC_20, serverDescription.getDeleteUrl());
                    LOGGER_DELETE.info("DELETE request sent");
                } catch (RestException | ConstraintViolationReportedByServer ex) {
                    Logger.getLogger(OslcCommandExecutor.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(OslcCommandExecutor.class.getName(), ex);
                }
            }

            //
            // Close connection
            //
            httpClient = null;
        }
        requestCounter = 0;
    }

    private OslcResponse executeCommandWithoutRepetition(OslcCommand command) throws RestException, ConstraintViolationReportedByServer {
        assert (command != null);

        StringBuilder queryString = new StringBuilder(150);
        OslcResponse serverResponse = null;

        // Set URL (server name/address)
        queryString.append(command.buildCommandString(serverDescription.getOslcUrl()));

        if (command instanceof OslcGetCommand) {
            serverResponse = performGetRequest(command.getProtocolVersion(), queryString.toString(), ((OslcGetCommand) command).getResponseFormat());
        } else if (command instanceof OslcPutCommand) {
            serverResponse = performPutRequest(command.getProtocolVersion(), queryString.toString(), ((OslcPutCommand) command).buildBodyString(), command.getHeaderData());
        } else if (command instanceof OslcPostCommand) {
            serverResponse = performPostRequest(command.getProtocolVersion(), queryString.toString(), ((OslcPostCommand) command).buildBodyString());
        } else if (command instanceof OslcUploadCommand) {
            OslcUploadCommand uploadCommand = (OslcUploadCommand) command;
            serverResponse = performPostRequest(command.getProtocolVersion(), queryString.toString(), uploadCommand.getFile(), uploadCommand.getParameter());
        } else {
            throw new Error("Unexpected command class " + command.getClass().getCanonicalName());
        }

        return (serverResponse);
    }

    static private final int[] repetitionIntervalForBackground = {1, 2, 3, 10, 20, 30, 60, 120, 240};
    static public final int[] repetitionIntervalForUserInterface = {1, 2, 3, 10, 20, 30};

    public synchronized OslcResponse executeCommand(final OslcCommand command) throws RestException, ConstraintViolationReportedByServer {
        assert (command != null);
        UiThreadChecker.ensureBackgroundThread();

        OslcResponse response = null;
        int repetitionNumber = 0;

        do {
            try {
                response = executeCommandWithoutRepetition(command);
            } catch (TemporaryServerError ex) {
                if ((ex instanceof SocketException) && (repetitionNumber == 0)) {
                    //
                    // Suppress first socket exception, because this exception comes after a connection timeout
                    //
                    logger.info("Connection time out reached.");
                    repetitionNumber++;
                    shutdownHttpConnection();
                } else {
                    logger.severe("TemporaryServerError for " + command.buildCommandString(serverDescription.getOslcUrl()));
                    if (repetitionNumber < repetitionIntervalForUserInterface.length) {
                        logger.severe("Repetition number " + repetitionNumber + ", now sleeping for " + repetitionIntervalForUserInterface[repetitionNumber] + " s");
                    } else {
                        logger.severe("Repetition number " + repetitionNumber + ". Giving up.");
                    }
                    logger.severe(ex.toString());
                    ToolUsageLogger.logError(OslcCommandExecutor.class.getCanonicalName(), ex);
                    if (EcvApplication.hasMainWindow() == true) {
                        //
                        // Handle temporary failure in UI application
                        //
                        if (ex instanceof AuthorizationException) {
                            throw (ex);
                        }
                        if (repetitionNumber < repetitionIntervalForUserInterface.length) {
                            shutdownHttpConnection();
                            try {
                                assert (SwingUtilities.isEventDispatchThread() == false);
                                Thread.sleep(repetitionIntervalForUserInterface[repetitionNumber] * 1000);
                            } catch (InterruptedException ex1) {
                                logger.severe("Sleep interupted.");
                                ToolUsageLogger.logError(OslcCommandExecutor.class.getCanonicalName(), ex1);
                            }
                            repetitionNumber++;
                            logger.severe("Starting repetition number " + repetitionNumber);
                        } else {
                            assert (SwingUtilities.isEventDispatchThread() == false);
                            logger.severe("ASK USER");
                            if (askUserForRepetition(ex) == true) {
                                repetitionNumber = 0;
                            } else {
                                throw (ex);
                            }
                        }

                    } else {
                        //
                        // Handle temporary failure in background application
                        //
                        if (repetitionNumber < repetitionIntervalForBackground.length) {
                            shutdownHttpConnection();
                            try {
                                Thread.sleep(repetitionIntervalForBackground[repetitionNumber] * 1000);
                            } catch (InterruptedException ex1) {
                                logger.severe("Sleep interupted.");
                                ToolUsageLogger.logError(OslcCommandExecutor.class.getCanonicalName(), ex1);
                            }
                            repetitionNumber++;
                            logger.severe("Starting repetition number " + repetitionNumber);
                        } else {
                            logger.severe("GIVING UP");
                            throw (ex);
                        }
                    }
                }

            } catch (NotFoundException ex) {

                throw (new NotFoundException(command.getAddressForUi()));

            } catch (NoLoginDataException ex) {

                throw (ex);

            } catch (RestException ex) {
                if (serverDescription != null) {
                    logger.severe("RestException catched for " + command.buildCommandString(serverDescription.getOslcUrl()));
                    ToolUsageLogger.logError(OslcCommandExecutor.class.getCanonicalName(), ex);
                }
                logger.severe(ex.toString());
                throw (ex);
            }
        } while (response == null);

        if (repetitionNumber > 0) {
            logger.severe("Repetition number " + repetitionNumber + " was successful.");
        }

        if (serverDescription.getMaxRequestNumberPerSession() > 0) {
            if (requestCounter > serverDescription.getMaxRequestNumberPerSession()) {
                shutdownHttpConnection();
            }
        }

        return (response);
    }

    private class UserResult {

        private boolean result = false;
    }

    private boolean askUserForRepetition(final TemporaryServerError ex) {
        final UserResult result = new UserResult();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                TemporaryServerErrorDialog dialog = new TemporaryServerErrorDialog(ex);
                dialog.setVisible(true);
                if (dialog.getResult() == DialogResult.REPEAT) {
                    logger.warning("REPEAT");
                    // Nothing to do. Simply repeat the command.
                    result.result = true;
                } else if (dialog.getResult() == DialogResult.RECONNECT_AND_REPEAT) {
                    logger.warning("RECONNECT_AND_REPEAT");
                    shutdownHttpConnection();
                    result.result = true;
                } else {
                    logger.warning("CANCEL");
                    result.result = false;
                }
            }
        });
        return (result.result);

    }

    public int getRequestCounter() {
        return requestCounter;
    }

}
