/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.HIS;

import RestClient.Exceptions.NoLoginDataException;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestResponseException;
import RestClient.HIS.RestClient_HisMilestone.Type;
import RestClient.Authentication.EmptyAuthenticationProvider;
import RestClient.RestGetRequest;
import RestClient.RestPostRequest;
import RestClient.RestRequestExecutor;
import RestClient.RestResponseI;
import RestClient.RestResponseJson;
import RestClient.RestResponseText;
import UiSupport.EcvUserMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvDate;
import util.EcvJsonArray;
import util.EcvJsonObject;
import util.EcvJsonObject.JsonNotFound;
import util.EcvJsonObject.JsonWrongType;
import util.EcvJsonString;
import util.EcvJsonTopLevelValue;
import util.EcvJsonValue;
import util.EcvLoginData;

/**
 *
 * @author GUG2WI
 */
public class RestClient_HIS {

    private static final Logger LOGGER = Logger.getLogger(RestClient_HIS.class.getCanonicalName());

    public static final RestClient_HIS client = new RestClient_HIS();

    private RestServerDescription_HIS serverDescription = null;
    private RestRequestExecutor executor = null;
    
    hisUserLoginStatus hisUserLogin = hisUserLoginStatus.NO_LOGIN_ATTEMPT;
    
    
    enum hisUserLoginStatus{
      UNAUTHORIZED,
      LOGGED_IN,
      NO_LOGIN_ATTEMPT;
    };

    private RestClient_HIS() {

    }

    /**
     * To set the server for HIS explicitly. Shall only used for tests.
     *
     * @param server
     */
    public void setServer(RestServerDescription_HIS server) {
        serverDescription = server;
    }

    //--------------------------------------------------------------------------
    //
    // Execution of server requests
    //
    //--------------------------------------------------------------------------
    public void connect() {

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
            ToolUsageLogger.logError(RestClient_HIS.class.getCanonicalName(), ex);
            return;
        }
        if (serverDescription == null) {
            serverDescription = loginData.getServerDescription().getHisServer();
        }
        executor = new RestRequestExecutor(serverDescription, new EmptyAuthenticationProvider());

        //
        // Login
        //
        EcvJsonObject loginBody = new EcvJsonObject();
        loginBody.addJsonString("name", loginData.getLoginName());
        loginBody.addJsonString("password", new String(loginData.getPassword()));
        RestPostRequest loginRequest = new RestPostRequest("auth/login", loginBody.toJsonLine());
        loginRequest.setHideBody(true);
        try {
            executor.executeRequest(loginRequest);
        } catch (RestException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(RestClient_HIS.class.getCanonicalName(), ex);
        }

        //
        // Check login status
        //
        RestGetRequest checkLoginRequest = new RestGetRequest("auth/status");
        try {
            RestResponseText text = (RestResponseText) executor.executeRequest(checkLoginRequest);
            
            if(text.getTextResponse().equals("Unauthorized")){
                hisUserLogin = hisUserLoginStatus.UNAUTHORIZED;
            }else {
                hisUserLogin = hisUserLoginStatus.LOGGED_IN;
            }
            
            if (hisUserLogin == hisUserLoginStatus.UNAUTHORIZED) {
                EcvUserMessage.showMessageDialog("You are not allowed to connect to HIS."
                        + "\nTherefore, elements from HIS are loaded from the data cached in RQ1.",
                        "Login to HIS failed",
                        EcvUserMessage.MessageType.ERROR_MESSAGE);
            }
        } catch (RestException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(RestClient_HIS.class.getCanonicalName(), ex);
        }

    }

    private RestResponseI executePostRequest(String servicePath, String body) throws RestException {
        assert (servicePath != null);
        assert (body != null);

        RestPostRequest request = new RestPostRequest(servicePath, body);

        connect();
        RestResponseI response = new RestResponseI() {
        };
        if (hisUserLogin == hisUserLoginStatus.LOGGED_IN) {
            response = executor.executeRequest(request);
            return (response);
        }
        return response;
    }

    //--------------------------------------------------------------------------
    //
    // Get specific responses from HIS
    //
    //--------------------------------------------------------------------------
    public List<RestClient_HisMilestone> getMilestones(Collection<String> ecuHardwareIds) throws RestException {
        assert (ecuHardwareIds != null);
        
        if (ecuHardwareIds.isEmpty() || hisUserLogin == hisUserLoginStatus.UNAUTHORIZED) {
            return (new ArrayList<>());
        }

        //
        // Build body for request
        //
        EcvJsonObject body = new EcvJsonObject();
        EcvJsonArray idList = body.addJsonArray("ecuhwids");
        for (String ecuHardwareId : ecuHardwareIds) {
            idList.addJsonString(ecuHardwareId);
        }

        //
        // Execute POST request
        //
        RestResponseI response = executePostRequest("tecus/dates", body.toJsonLine());

        //
        // Check and decode response
        //
        if ( hisUserLogin != hisUserLoginStatus.UNAUTHORIZED) {
            if (response instanceof RestResponseJson == false) {
                String problem;
                if (response != null) {
                    problem = "Invalid content type in response: " + response.getClass().getCanonicalName();
                } else {
                    problem = "No content in response.";
                }
                throw (new RestResponseException(problem));
            }

            List<RestClient_HisMilestone> result = decodeMilestoneResponse((RestResponseJson) response);

            return (result);
        }
        return null;
    }

    static List<RestClient_HisMilestone> decodeMilestoneResponse(RestResponseJson jsonResponse) throws RestResponseException {
        assert (jsonResponse != null);

        //-------------------------------
        // Get and check top level value
        //-------------------------------
        EcvJsonTopLevelValue topLevelValue = jsonResponse.getTopLevelValue();
        if (topLevelValue instanceof EcvJsonObject == false) {
            throw (new RestResponseException("Top level value is not a JsonObject.", topLevelValue));
        }
        EcvJsonObject topJsonObject = (EcvJsonObject) topLevelValue;

        //--------------------------------
        // Get and check 'result' element
        //--------------------------------
        EcvJsonValue resultsValue;
        try {
            resultsValue = topJsonObject.getValue("results");
        } catch (JsonNotFound ex) {
            throw (new RestResponseException("Object 'results' missing.", topLevelValue, ex));
        }
        if (resultsValue instanceof EcvJsonString) {
            if ("no values found".equals(((EcvJsonString) resultsValue).getValue()) == true) {
                return (new ArrayList<>());
            } else {
                throw (new RestResponseException("Object 'results' has unexpected value.", topLevelValue));
            }
        } else if (resultsValue instanceof EcvJsonArray == false) {
            throw (new RestResponseException("Object 'results' has invalid type.", topLevelValue));
        }

        //--------------------------------
        // Get and check values from list
        //--------------------------------
        List<RestClient_HisMilestone> result = new ArrayList<>();
        List<EcvJsonValue> resultsValueElements = ((EcvJsonArray) resultsValue).getElements();
        for (EcvJsonValue resultValue : resultsValueElements) {
            if (resultValue instanceof EcvJsonObject == false) {
                throw (new RestResponseException("'results' contains elements which are not JsonObjects.", topLevelValue));
            }
            try {
                result.addAll(extractOneEcuHwId((EcvJsonObject) resultValue));
            } catch (JsonNotFound | RestResponseException ex) {
                throw (new RestResponseException("'results' contains invalid elements.", topLevelValue, ex));
            }
        }
        return (result);
    }

    private static List<RestClient_HisMilestone> extractOneEcuHwId(EcvJsonObject oneResult) throws JsonNotFound, RestResponseException {
        assert (oneResult != null);

        String ecuHwId = oneResult.getString("ecuhwid");

        try {
            String targetEcuName = oneResult.getString("targetEcuName");
            List<EcvJsonObject> milestones = oneResult.getAllValuesFromArray("milestones", EcvJsonObject.class);
            List<EcvJsonObject> qualitygates = oneResult.getAllValuesFromArray("qualitygates", EcvJsonObject.class);

            List<RestClient_HisMilestone> result = new ArrayList<>();

            for (EcvJsonObject milestone : milestones) {
                result.add(extractMilestone(Type.MILESTONE, ecuHwId, targetEcuName, milestone));
            }

            for (EcvJsonObject qualitygate : qualitygates) {
                result.add(extractMilestone(Type.QUALITY_GATE, ecuHwId, targetEcuName, qualitygate));
            }

            return (result);

        } catch (JsonNotFound | JsonWrongType | RestResponseException ex) {
            throw (new RestResponseException("Extract values for ECU HW ID = " + ecuHwId));
        }
    }

    private static RestClient_HisMilestone extractMilestone(Type type, String ecuHwId, String targetEcuName, EcvJsonObject milestoneObject) throws JsonNotFound, RestResponseException {

        String name = milestoneObject.getString("name");
        String dateString = milestoneObject.getString("date");

        try {
            EcvDate date = EcvDate.parseXmlValue(dateString);
            return (new RestClient_HisMilestone(type, ecuHwId, targetEcuName, name, date));

        } catch (EcvDate.DateParseException ex) {
            throw (new RestResponseException("Invalid date value '" + dateString + "' for " + name, ex));
        }
    }
}
