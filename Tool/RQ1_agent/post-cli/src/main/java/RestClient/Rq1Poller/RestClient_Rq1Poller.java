/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Rq1Poller;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import RestClient.Authentication.EmptyAuthenticationProvider;
import RestClient.Exceptions.NoLoginDataException;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestResponseException;
import RestClient.RestGetRequest;
import RestClient.RestRequestExecutor;
import RestClient.RestResponseJson;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvJsonBoolean;
import util.EcvJsonObject;
import util.EcvJsonTopLevelValue;
import util.EcvLoginData;

/**
 *
 * @author hfi5wi
 */
public class RestClient_Rq1Poller {
    
    private static final Logger LOGGER = Logger.getLogger(RestClient_Rq1Poller.class.getCanonicalName());

    public static final RestClient_Rq1Poller client = new RestClient_Rq1Poller();

    private RestServerDescription_Rq1Poller serverDescription = null;
    private RestRequestExecutor executor = null;
    
    private RestClient_Rq1Poller() {
    }

    private void connect() {
        if (executor != null) {
            return;
        }
        
        //
        // Get login data
        //
        EcvLoginData loginData;
        try {
            loginData = EcvLoginManager.getFirstLoginData();
        } catch (NoLoginDataException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(RestClient_Rq1Poller.class.getCanonicalName(), ex);
            return;
        }
        if (serverDescription == null) {
            serverDescription = loginData.getServerDescription().getRq1PollerServer();
        }
        executor = new RestRequestExecutor(serverDescription, new EmptyAuthenticationProvider());
    }

    //--------------------------------------------------------------------------
    //
    // Execution of server requests
    //
    //--------------------------------------------------------------------------
    private RestResponseJson executeGetRequest(String request) throws RestException {
        assert (request != null);

        connect();
        RestResponseJson response = (RestResponseJson) executor.executeRequest(new RestGetRequest(request));
        LOGGER.finer(response.getTopLevelValue().toJsonString());
        return (response);
    }
    
    //--------------------------------------------------------------------------
    //
    // Get specific responses from RQ1 Poller
    //
    //--------------------------------------------------------------------------
    public RestClient_Rq1PollerResponse checkPverForINMA(String rq1Number) throws RestException {
        assert (rq1Number != null);
        assert (rq1Number.trim().isEmpty() == false);
        
        //
        // Execute the GET request
        //
        RestResponseJson response = executeGetRequest(rq1Number);
        
        //
        // Get and check the top level element
        //
        EcvJsonTopLevelValue topLevelValue = response.getTopLevelValue();
        if (topLevelValue instanceof EcvJsonObject == false) {
            throw new RestResponseException("JSON object expected on the top level.", topLevelValue);
        }
        EcvJsonObject topLevelObject = (EcvJsonObject) topLevelValue;
        
        //
        // Extract information from the response
        //
        try {
            boolean sutitableForINMA = topLevelObject.getValue("sutitableForINMA", EcvJsonBoolean.class).getValue();
            String inmacomment = topLevelObject.getString("inmacomment");
            return (new RestClient_Rq1PollerResponse(sutitableForINMA, inmacomment));
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.log(Level.WARNING, "Mandatory field for INMA missing.", ex);
            LOGGER.log(Level.WARNING, "JSON-Object without mandatory field:\n{0}", topLevelObject.toJsonString());
        }
        return null;
    }

}
