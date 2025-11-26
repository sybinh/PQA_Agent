/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

import RestClient.Authentication.AuthenticationProviderI;
import RestClient.Exceptions.AuthorizationException;
import RestClient.Exceptions.BadRequestException;
import RestClient.Exceptions.ConnectionRefusedException;
import RestClient.Exceptions.FileBufferOverrunException;
import RestClient.Exceptions.FileSizeException;
import RestClient.Exceptions.NotFoundException;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestInternalException;
import RestClient.Exceptions.RestResponseException;
import RestClient.Exceptions.SocketException;
import RestClient.Exceptions.TemporaryServerError;
import RestClient.Exceptions.UnknownHostException;
import RestClient.RestRequest.ResponseFormat;
import RestClient.RestTemporaryServerErrorDialog.DialogResult;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.UiThreadChecker;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import util.EcvApplication;
import util.EcvJsonParser;
import util.EcvJsonTopLevelValue;
import util.EcvPositionAwarePushbackReader;
import util.EcvXmlElement;
import util.EcvXmlParser;

/**
 *
 * @author GUG2WI
 */
public class RestRequestExecutor {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(RestRequestExecutor.class.getCanonicalName());
    private static final java.util.logging.Logger LOGGER_STACK = java.util.logging.Logger.getLogger(RestRequestExecutor.class.getCanonicalName() + "_Stack");
    private static final java.util.logging.Logger LOGGER_DELETE = java.util.logging.Logger.getLogger(RestRequestExecutor.class.getCanonicalName() + "_Delete");
    //
    final private RestServerDescriptionI serverDescription;
    private AuthenticationProviderI authProvider;
    //
    private HttpClient httpClient;
    final private CookieHandler cockieHandler;
    //
    private boolean isConnectionEstablished = false;
    private int requestCounter = 0;

    //--------------------------------------------------------------------------
    //
    // Construction and parameter setting
    //
    //--------------------------------------------------------------------------
    public RestRequestExecutor(RestServerDescriptionI serverDescription, AuthenticationProviderI authProvider) {
        assert (serverDescription != null);
        assert (authProvider != null);

        this.serverDescription = serverDescription;
        this.authProvider = authProvider;
        cockieHandler = new CookieHandler();
        httpClient = null;

        RestCertificateManager.setStore();
    }

    void setAuthenticationProvider(AuthenticationProviderI authProvider) {
        this.authProvider = authProvider;
    }

    //--------------------------------------------------------------------------
    //
    // Sending requests and receiving responses
    //
    //--------------------------------------------------------------------------
    protected RestResponseI performGetRequest(String urlString, ResponseFormat responseFormat) throws RestException {
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (urlString.startsWith("http")) : urlString;
        assert (responseFormat != null);

        LOGGER.log(Level.INFO, "URL = >{0}<", urlString);

        //
        // Create URI object for this request-response cycle
        //
        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String protocol = urlValue.getProtocol();
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
           uri = new URI(protocol, authority, path, query, null);

        } catch (URISyntaxException ex) {

            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        //
        // Create GetMethod for this request-response cycle
        //
        HttpGet getMethod = new HttpGet();

        getMethod.setURI(uri);

        //
        // Perform the http communication for this request-response cycle
        //
        RestResponseI response = performHttpRequest(getMethod, responseFormat);

        return (response);
    }

    protected RestResponseI performPutRequest(String urlString, String body, ResponseFormat responseFormat, boolean hideBody) throws RestException {
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (body != null);
        assert (body.isEmpty() == false);
        assert (responseFormat != null);

        LOGGER.log(Level.INFO, "URL = >{0}<", urlString);
        if (hideBody == false) {
            LOGGER.log(Level.INFO, "INFO:\n{0}", body);
        }

        URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException ex) {
            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        }

        StringEntity requestEntity = null;

        requestEntity = new StringEntity(body, "utf-8");

        HttpPut putMethod = new HttpPut();

        putMethod.setURI(uri);

        putMethod.setEntity(requestEntity);

        return (performHttpRequest(putMethod, responseFormat));
    }

    protected RestResponseI performDeleteRequest(String urlString, ResponseFormat responseFormat) throws RestException {
        assert (urlString != null);
        assert (urlString.isEmpty() == false);

        LOGGER.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String protocol = urlValue.getProtocol();
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI(protocol, authority, path, query, null);

        } catch (URISyntaxException ex) {

            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpDelete deleteMethod = new HttpDelete();
        try {
            deleteMethod.setURI(uri);
        } catch (Exception ex) {
            throw (new RestInternalException("Setting URI in get method for urlString=\"" + urlString + "\" failed.", ex));
        }

        return (performHttpRequest(deleteMethod, responseFormat));
    }

    protected RestResponseI performPostRequest(RestPostRequest postRequest) throws RestException {
        assert (postRequest != null);

        String requestUrl = postRequest.buildRequestUrl(serverDescription.getServiceUrl());
        String body = postRequest.buildBodyString();
        if (postRequest.isHideBody() == false) {
            LOGGER.log(Level.INFO, "BODY:\n{0}", body);
        }
        String contentType = postRequest.getContentType().getTextForHeader();

        StringEntity requestEntity = null;

        requestEntity = new StringEntity(body, ContentType.create(contentType, "utf-8"));

        return (performPostRequest(requestUrl, requestEntity, postRequest.getResponseFormat()));
    }

    private RestResponseI performPostRequest(String urlString, File file, Map<String, String> parameter, ResponseFormat responseFormat) throws RestException {
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (parameter != null);
        assert (file != null);
        assert (file.canRead());

        LOGGER.log(Level.INFO, "PATH:\n{0}", file.getAbsolutePath());

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

        return (performPostRequest(urlString, requestEntity, responseFormat));
    }

    private RestResponseI performPostRequest(String urlString, HttpEntity requestEntity, ResponseFormat responseFormat) throws RestException {
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (requestEntity != null);

        LOGGER.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String protocol = urlValue.getProtocol();
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI(protocol, authority, path, query, null);

        } catch (URISyntaxException ex) {

            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpPost postMethod = new HttpPost();

        postMethod.setURI(uri);

        postMethod.setEntity(requestEntity);

        return (performHttpRequest(postMethod, responseFormat));
    }

    RestResponseI performPostRequest(String urlString, Map<String, String> parameters, ResponseFormat responseFormat) throws RestException {
        assert (urlString != null);
        assert (urlString.isEmpty() == false);
        assert (parameters != null);

        LOGGER.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String protocol = urlValue.getProtocol();
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI(protocol, authority, path, query, null);

        } catch (URISyntaxException ex) {

            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpPost postMethod = new HttpPost();

        postMethod.setURI(uri);

        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            postMethod.addHeader(parameter.getKey(), parameter.getValue());
        }

        return (performHttpRequest(postMethod, responseFormat));
    }

    RestResponseI performPostRequest(String urlString, ResponseFormat responseFormat) throws RestException {
        assert (urlString != null);
        assert (urlString.isEmpty() == false);

        LOGGER.log(Level.INFO, "URL = >{0}<", urlString);

        URI uri = null;
        try {
            java.net.URL urlValue = new java.net.URL(urlString);
            String protocol = urlValue.getProtocol();
            String authority = urlValue.getAuthority();
            String path = urlValue.getPath();
            String query = urlValue.getQuery();
            uri = new URI(protocol, authority, path, query, null);

        } catch (URISyntaxException ex) {

            throw (new RestInternalException("Creation of URI for urlString=\"" + urlString + "\" failed.", ex));
        } catch (MalformedURLException ex) {
            throw (new RestInternalException("Creation of URL for urlString=\"" + urlString + "\" failed." + ex));
        }

        HttpPost postMethod = new HttpPost();

        postMethod.setURI(uri);

        return (performHttpRequest(postMethod, responseFormat));
    }

    private RestResponseI performHttpRequest(HttpRequestBase method, ResponseFormat responseFormat) throws RestException {
        assert (method != null);
        assert (responseFormat != null);
        assert (authProvider != null);

        requestCounter++;

        if (LOGGER_STACK.isLoggable(Level.FINEST) == true) {
            LOGGER_STACK.log(Level.FINEST, "Start. Request = " + method.getRequestLine(), new Exception("Stack trace for request (no error)"));
        } else {
            LOGGER.log(Level.INFO, "Start. Request = {0}", method.getRequestLine());
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

        if (LOGGER.isLoggable(Level.FINER)) {
            StringBuilder b = new StringBuilder();
            for (Header header : method.getAllHeaders()) {
                if (b.length() > 0) {
                    b.append('&');
                }
                b.append(header.getName()).append('=').append(header.getValue());
            }
            LOGGER.finer("Header = " + b.toString());
        }

        //
        // Execute method
        //
        int statusCode;
        HttpResponse httpResponse;
        try {

            httpResponse = httpClient.execute(method);
            statusCode = httpResponse.getStatusLine().getStatusCode();
        } catch (java.net.UnknownHostException ex) {
            LOGGER.log(Level.WARNING, "Unknown host exception catched.", ex);
            LOGGER.log(Level.WARNING, "URL= ", method.getRequestLine());
            throw (new UnknownHostException("Get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        } catch (java.net.ConnectException ex) {
            LOGGER.log(Level.WARNING, "Connect exception catched.", ex);
            LOGGER.log(Level.WARNING, "URL= ", method.getRequestLine());
            throw (new ConnectionRefusedException("Get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        } catch (java.net.SocketException ex) {
//            logger.log(Level.WARNING, "Socket Exception catched.", ex);
//            logger.log(Level.WARNING, "URL= ", method.getQueryString());
            throw (new SocketException(method.getRequestLine().toString(), ex));
        } catch (IOException ex) {
            if (ex instanceof javax.net.ssl.SSLHandshakeException) {
                LOGGER.severe("Connection refused by server.\nHint: Check certificates in your JRE. Certificates needed to connect to server may be missing.");
                ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                throw (new RestInternalException("Get method for urlString=\"" + method.getRequestLine() + "\".\n"
                        + "Hint: Check certificates in your JRE. Certificates needed to connect to RQ1 server may be missing.", ex));
            }
            throw (new RestInternalException("Get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        }
        cockieHandler.pushCookies(httpResponse);

        LOGGER.log(Level.FINE, "statusCode={0}", Integer.toString(statusCode));

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
        InputStream responseInputStream = null;
        try {
            if (httpResponse.getEntity() != null && httpResponse.getEntity().getContent() != null) {
                responseInputStream = httpResponse.getEntity().getContent();
            }
        } catch (IOException ex) {
            method.releaseConnection();
            throw (new RestInternalException("Retrieve response from get method for urlString=\"" + method.getRequestLine() + "\".", ex));
        }

        //
        // Save receiving time
        //
        Date receiveDate = new Date();
        LOGGER.log(Level.INFO, "Received. Duration = {0} ms.", (receiveDate.getTime() - startDate.getTime()));

        RestResponseI response;
        if ((responseFormat == ResponseFormat.BINARY) && (statusCode == HttpStatus.SC_OK)) {
            response = readResponseInBinaryFormat(responseHeader, responseInputStream);
        } else {

            InputStreamReader responseReader = null;
            try {
                responseReader = new InputStreamReader(responseInputStream, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                method.releaseConnection();
                throw (new RestInternalException("Decode response from get method for urlString=\"" + method.getRequestLine() + "\".", ex));
            }

            EcvPositionAwarePushbackReader pushBackReader = new EcvPositionAwarePushbackReader(responseReader);
            int firstCharacter;
            try {
                firstCharacter = pushBackReader.read();
                if (firstCharacter >= 0) {
                    pushBackReader.unread(firstCharacter);
                }
            } catch (IOException ex) {
                method.releaseConnection();
                throw (new RestInternalException("Decode response from get method for urlString=\"" + method.getRequestLine() + "\".", ex));
            }
            EcvXmlElement xmlResponse = null;
            EcvJsonTopLevelValue jsonResponse = null;
            String textResponse = null;
            try {
                switch (firstCharacter) {
                    case '<':
                        // Response begins like XML. Parse as XML
                        xmlResponse = parseXmlResponse(method.getRequestLine().toString(), statusCode, pushBackReader);
                        break;

                    case '{':
                        // Response begins like JSON. Parse as JSON
                        jsonResponse = parseJsonResponse(method.getRequestLine().toString(), statusCode, pushBackReader);
                        break;

                    default:
                        textResponse = readTextResponse(method.getRequestLine().toString(), statusCode, pushBackReader);
                        break;
                }
            } catch (RestException ex) {
                method.releaseConnection();
                throw (ex);
            }

            response = analyseResponse(statusCode, method.getRequestLine().toString(), xmlResponse, jsonResponse, textResponse);

        }

        //
        // Release connection
        //
        method.releaseConnection();

        //
        // Save end time
        //
        Date endDate = new Date();

        LOGGER.log(Level.INFO,
                "Processed. Duration = {0} ms.", (endDate.getTime() - startDate.getTime()));

        isConnectionEstablished = true;
        return (response);
    }

    //--------------------------------------------------------------------------
    //
    // Handle response
    //
    //--------------------------------------------------------------------------
    final private static int BUFFER_SIZE = 16352;
    final private static int MAX_NUMBER_OF_BUFFERS = 10000;

    private RestBinaryResponse readResponseInBinaryFormat(Map<String, String> responseHeader, InputStream byteInput) throws FileSizeException, RestResponseException {
        assert (responseHeader != null);
        assert (byteInput != null);

        List<byte[]> responseInBinaryFormat = new ArrayList<>();
        int numberOfReceivedBytes = 0;
        do {
            byte[] buffer = new byte[BUFFER_SIZE];
            try {
                numberOfReceivedBytes = byteInput.read(buffer);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                throw (new RestResponseException("Load file content", "Read failed (" + responseInBinaryFormat.size() + ")"));
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

        return (new RestBinaryResponse(responseHeader, responseInBinaryFormat));
    }

    private EcvXmlElement parseXmlResponse(String queryString, int statusCode, EcvPositionAwarePushbackReader pushBackReader) throws RestException {
        assert (pushBackReader != null);

        EcvXmlElement result = null;

        try {
            EcvXmlParser parser = new EcvXmlParser(pushBackReader);
            result = parser.parse();
            if (LOGGER.isLoggable(Level.FINEST) == true) {
                LOGGER.log(Level.FINEST, "Raw response:\n{0}", parser.getReadData());
            }
        } catch (java.lang.OutOfMemoryError ex) {
            LOGGER.severe(queryString);
            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
            throw (ex);
        } catch (EcvXmlParser.ParseException ex) {
            LOGGER.log(Level.SEVERE, "statusCode={0}", Integer.toString(statusCode));
            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
            LOGGER.log(Level.WARNING, "--- ErrorLog Start -------------------------");
            LOGGER.log(Level.WARNING, ex.getErrorLog());
            LOGGER.log(Level.WARNING, "--- ErrorLog End ---------------------------");
            if (ex.endOfInputReached() == true) {
                throw (new FileBufferOverrunException(queryString));
            } else {
                throw (new RestResponseException("Parse server response for urlString=\"" + queryString + "\".", ex));
            }
        }

        if (LOGGER.isLoggable(Level.FINER)) {
            if (result != null) {
                LOGGER.log(Level.FINER, "XML response:\n{0}", result.getUiString());
            } else {
                LOGGER.finer("XML response is empty.");
            }
        }

        return (result);
    }

    private EcvJsonTopLevelValue parseJsonResponse(String queryString, int statusCode, EcvPositionAwarePushbackReader pushBackReader) throws RestException {
        assert (pushBackReader != null);

        EcvJsonTopLevelValue result = null;

        try {
            EcvJsonParser parser = new EcvJsonParser(pushBackReader);
            result = parser.parse();
            if (LOGGER.isLoggable(Level.FINEST) == true) {
                LOGGER.log(Level.FINEST, "Raw response:\n{0}", parser.getReadData());
            }
        } catch (java.lang.OutOfMemoryError ex) {
            LOGGER.severe(queryString);
            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
            throw (ex);
        } catch (EcvJsonParser.ParseException ex) {
            LOGGER.log(Level.SEVERE, "statusCode={0}", Integer.toString(statusCode));
            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
            LOGGER.log(Level.WARNING, "--- ErrorLog Start -------------------------");
            LOGGER.log(Level.WARNING, ex.getErrorLog());
            LOGGER.log(Level.WARNING, "--- ErrorLog End ---------------------------");
            if (ex.endOfInputReached() == true) {
                throw (new FileBufferOverrunException(queryString));
            } else {
                throw (new RestResponseException("Parse server response for urlString=\"" + queryString + "\".", ex));
            }
        }

        if (LOGGER.isLoggable(Level.FINER)) {
            if (result != null) {
                LOGGER.log(Level.FINER, "XML response:\n{0}", result.toJsonString());
            } else {
                LOGGER.finer("XML response is empty.");
            }
        }

        return (result);
    }

    private String readTextResponse(String queryString, int statusCode, EcvPositionAwarePushbackReader pushBackReader) throws RestException {
        assert (pushBackReader != null);

        try {
            String result = pushBackReader.getNextData(1000);
            if (LOGGER.isLoggable(Level.FINEST) == true) {
                LOGGER.log(Level.FINEST, "Raw response:\n{0}", result);
            }
            return (result);
        } catch (IOException ex) {
            throw (new RestResponseException("Parse server response for urlString=\"" + queryString + "\".", ex));
        }
    }

    private RestResponseI analyseResponse(int statusCode, String queryString, EcvXmlElement xmlResponse, EcvJsonTopLevelValue jsonResponse, String textResponse) throws RestException {

        switch (statusCode) {

            case HttpStatus.SC_OK: // 200
            case HttpStatus.SC_ACCEPTED: // 202
                if (xmlResponse != null) {
                    return (new RestResponseXml(xmlResponse));
                } else if (jsonResponse != null) {
                    return (new RestResponseJson(jsonResponse));
                } else if (textResponse != null) {
                    return (new RestResponseText(textResponse));
                } else {
                    throw (new RestInternalException("No response set."));
                }

            case HttpStatus.SC_MOVED_TEMPORARILY: // 302
                LOGGER.log(Level.WARNING, "url={0}", queryString);
                LOGGER.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
                throw (new BadRequestException("Moved temporarely."));

            case HttpStatus.SC_NOT_FOUND: // 404
                LOGGER.log(Level.WARNING, "url={0}", queryString);
                LOGGER.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
                throw (new NotFoundException(queryString));

            case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE: // 415
                LOGGER.log(Level.WARNING, "url={0}", queryString);
                LOGGER.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
                throw (new BadRequestException("Unsupported Media Type."));

            default:
                LOGGER.log(Level.WARNING, "url={0}", queryString);
                LOGGER.log(Level.WARNING, "statusCode={0}", Integer.toString(statusCode));
                throw (new RestInternalException("Check status code from server. Status code " + statusCode + " unknown."));
        }

    }

    //--------------------------------------------------------------------------
    //
    // Execution control
    //
    //--------------------------------------------------------------------------
    private RestResponseI executeRequestWithoutRepetition(RestRequest request) throws RestException {
        assert (request != null);

        RestResponseI response = null;

        if (request instanceof RestGetRequest) {

            String requestUrl = request.buildRequestUrl(serverDescription.getServiceUrl());
            response = performGetRequest(requestUrl, request.getResponseFormat());

        } else if (request instanceof RestPutRequest) {

            String requestUrl = request.buildRequestUrl(serverDescription.getServiceUrl());
            RestPutRequest putRequest = (RestPutRequest) request;
            response = performPutRequest(requestUrl, putRequest.buildBodyString(), putRequest.getResponseFormat(), putRequest.isHideBody());

        } else if (request instanceof RestPostRequest) {

            response = performPostRequest((RestPostRequest) request);

        } else {

            throw new Error("Unexpected request class " + request.getClass().getCanonicalName());

        }

        return (response);
    }

    static private final int[] repetitionIntervalForBackground = {1, 2, 3, 10, 20, 30, 60, 120, 240};
    static private final int[] repetitionIntervalForUserInterface = {1, 2, 3, 10, 20, 30};

    synchronized public RestResponseI executeRequest(final RestRequest command) throws RestException {
        assert (command != null);
        UiThreadChecker.ensureBackgroundThread();

        RestResponseI response = null;
        int repetitionNumber = 0;

        do {
            try {
                response = executeRequestWithoutRepetition(command);
            } catch (TemporaryServerError ex) {
                if ((ex instanceof SocketException) && (repetitionNumber == 0)) {
                    //
                    // Suppress first socket exception, because this exception comes after a connection timeout
                    //
                    LOGGER.info("Connection time out reached.");
                    repetitionNumber++;
                    shutdownHttpConnection();
                } else {
                    LOGGER.severe("RestTemporaryServerError for " + command.buildRequestUrl(serverDescription.getServiceUrl()));
                    LOGGER.severe(ex.toString());
                    ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);

                    if (EcvApplication.hasMainWindow() == true) {
                        //
                        // Handle temporary failure in UI application
                        //
                        if (ex instanceof AuthorizationException) {
                            LOGGER.severe("Authorization exception at repetition number " + repetitionNumber + ". Giving up.");
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                            throw (ex);
                        }

                        if (repetitionNumber < repetitionIntervalForUserInterface.length) {
                            LOGGER.severe("Repetition number " + repetitionNumber + ", now sleeping for " + repetitionIntervalForUserInterface[repetitionNumber] + " s");
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                        } else {
                            LOGGER.severe("Repetition number " + repetitionNumber + ". Giving up.");
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                        }
                        if (repetitionNumber < repetitionIntervalForUserInterface.length) {
                            shutdownHttpConnection();
                            try {
                                assert (SwingUtilities.isEventDispatchThread() == false);
                                Thread.sleep(repetitionIntervalForUserInterface[repetitionNumber] * 1000);
                            } catch (InterruptedException ex1) {
                                LOGGER.severe("Sleep interupted.");
                                ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                            }
                            repetitionNumber++;
                            LOGGER.severe("Starting repetition number " + repetitionNumber);
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                        } else {
                            assert (SwingUtilities.isEventDispatchThread() == false);
                            LOGGER.severe("ASK USER");
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
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
                            LOGGER.severe("Repetition number " + repetitionNumber + ", now sleeping for " + repetitionIntervalForBackground[repetitionNumber] + " s");
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                            shutdownHttpConnection();
                            try {
                                Thread.sleep(repetitionIntervalForBackground[repetitionNumber] * 1000);
                            } catch (InterruptedException ex1) {
                                LOGGER.severe("Sleep interupted.");
                                ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                            }
                            repetitionNumber++;
                            LOGGER.severe("Starting repetition number " + repetitionNumber);
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                        } else {
                            LOGGER.severe("Repetition number " + repetitionNumber + ". Giving up.");
                            ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                            throw (ex);
                        }
                    }
                }
            } catch (NotFoundException ex) {
                LOGGER.finer(ex.toString());
                throw (ex);
            } catch (RestException ex) {
                if (serverDescription != null) {
                    LOGGER.severe("RestException catched for " + command.buildRequestUrl(serverDescription.getServiceUrl()));
                    ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                }
                LOGGER.severe(ex.toString());
                ToolUsageLogger.logError(RestRequestExecutor.class.getCanonicalName(), ex);
                throw (ex);
            }
        } while (response == null);

        if (repetitionNumber > 0) {
            LOGGER.severe("Repetition number " + repetitionNumber + " was successful.");
        }

        if (serverDescription.getMaxRequestNumberPerSession() > 0) {
            if (requestCounter > serverDescription.getMaxRequestNumberPerSession()) {
                shutdownHttpConnection();
            }
        }

        return (response);
    }

    protected void shutdownHttpConnection() {

        if ((httpClient != null) && (serverDescription.getLogoutUrl() != null)) {

            httpClient = null;
        }
        requestCounter = 0;
    }

    private class UserResult {
        private boolean result = false;
    }
    
    private boolean askUserForRepetition(final TemporaryServerError ex) {
        UserResult userResult = new UserResult();
        
        SwingUtilities.invokeLater(() -> {
            RestTemporaryServerErrorDialog dialog = new RestTemporaryServerErrorDialog(ex);
            dialog.setVisible(true);
            if (dialog.getResult() == DialogResult.REPEAT) {
                LOGGER.warning("REPEAT");
                // Nothing to do. Simply repeat the command.
                userResult.result = true;
            } else if (dialog.getResult() == DialogResult.RECONNECT_AND_REPEAT) {
                LOGGER.warning("RECONNECT_AND_REPEAT");
                shutdownHttpConnection();
                userResult.result = true;
            } else {
                LOGGER.warning("CANCEL");
                userResult.result = false;
            }
        });
        return (userResult.result);
    }

}
