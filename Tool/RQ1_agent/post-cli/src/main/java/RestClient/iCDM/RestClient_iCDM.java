/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.iCDM;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import RestClient.Authentication.BasicAuthenticationProvider;
import RestClient.Exceptions.NoLoginDataException;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestResponseException;
import RestClient.RestGetRequest;
import RestClient.RestRequestExecutor;
import RestClient.RestResponseJson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvJsonNumber;
import util.EcvJsonObject;
import util.EcvJsonTopLevelValue;
import util.EcvJsonValue;
import util.EcvLoginData;

/**
 *
 * @author GUG2WI
 */
public class RestClient_iCDM {

    enum UserLoginStatus {
        UNAUTHORIZED,
        LOGGED_IN,
        NO_LOGIN_ATTEMPT;
    };

    private static final Logger LOGGER = Logger.getLogger(RestClient_iCDM.class.getCanonicalName());

    public static final RestClient_iCDM client = new RestClient_iCDM();

    private RestServerDescription_iCDM serverDescription = null;
    private RestRequestExecutor executor = null;

    UserLoginStatus loginStatus = UserLoginStatus.NO_LOGIN_ATTEMPT;

    private RestClient_iCDM() {

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
            ToolUsageLogger.logError(RestClient_iCDM.class.getCanonicalName(), ex);
            return;
        }
        if (serverDescription == null) {
            serverDescription = loginData.getServerDescription().getiCDMServer();
        }
        executor = new RestRequestExecutor(serverDescription, new BasicAuthenticationProvider(loginData.getLoginName(), serverDescription.getPassword()));

    }

    //--------------------------------------------------------------------------
    //
    // Execution of server requests
    //
    //--------------------------------------------------------------------------
    RestResponseJson executeGetRequest(String request) throws RestException {
        assert (request != null);

        connect();
        try {
            RestResponseJson response = (RestResponseJson) executor.executeRequest(new RestGetRequest(request));
            LOGGER.finer(response.getTopLevelValue().toJsonString());
            return (response);
        } catch (RestClient.Exceptions.NotFoundException notFound) {
            executor = null; // This prevents the blocking of next requests.
            throw (notFound);
        }

    }

    //--------------------------------------------------------------------------
    //
    // Get specific responses from iCDM
    //
    //--------------------------------------------------------------------------
    final private static String ATTR_GRP_ID = "attrGrpId";
    final private static int ATTR_GRP_ID_FOR_MILESTONES = 124;

    private static List<RestClient_iCDM_MilestoneType> milestoneTypes = null;

    protected List<RestClient_iCDM_MilestoneType> getMilestoneTypes() throws RestException {

        RestResponseJson response = executeGetRequest("APIC_WS2/rest/apic/attribute/getall?includedeleted=N");

        List<RestClient_iCDM_MilestoneType> result = new ArrayList<>();

        EcvJsonTopLevelValue topLevelValue = response.getTopLevelValue();
        for (EcvJsonValue value : topLevelValue.getElements()) {
            if (value instanceof EcvJsonObject) {
                EcvJsonObject object = (EcvJsonObject) value;
                EcvJsonNumber attGrpId = object.getValueOrNull(ATTR_GRP_ID, EcvJsonNumber.class);
                if ((attGrpId != null) && (attGrpId.getNumber().intValue() == ATTR_GRP_ID_FOR_MILESTONES)) {
                    try {
                        int id = object.getValue("id", EcvJsonNumber.class).getNumber().intValue();
                        String name = object.getString("name");
                        String description = object.getString("description");
                        String format = object.getString("format");
                        String valueType = object.getString("valueType");
                        result.add(new RestClient_iCDM_MilestoneType(id, name, description, format, valueType));
                    } catch (EcvJsonObject.JsonNotFound ex) {
                        LOGGER.log(Level.WARNING, "Mandatory field for milestone type missing.", ex);
                        LOGGER.log(Level.WARNING, "JSON-Object without mandatory field:\n{0}", object.toJsonString());
                    }
                }
            }
        }

        return (result);
    }

    protected List<RestClient_iCDM_Milestone> getMilestones(Collection<RestClient_iCDM_MilestoneType> milestoneTypes, String pidcVerID) throws RestException {
        assert (milestoneTypes != null);
        assert (pidcVerID != null);
        assert (pidcVerID.isEmpty() == false);

        //
        // Put milestone types in map for later access
        //
        Map<Integer, RestClient_iCDM_MilestoneType> typeMap = new TreeMap<>();
        milestoneTypes.forEach((t) -> {
            typeMap.put(t.getId(), t);
        });

        //
        // Execute the GET request
        //
        RestResponseJson response = executeGetRequest("/APIC_WS2/rest/apic/pidc/pidcwithattributes?pidcVerID=" + pidcVerID);

        executor = null;

        //
        // Get and check the top level element
        //
        EcvJsonTopLevelValue topLevelValue = response.getTopLevelValue();
        if (topLevelValue instanceof EcvJsonObject == false) {
            throw new RestResponseException("JSON object expected on the top level.", topLevelValue);
        }
        EcvJsonObject topLevelObject = (EcvJsonObject) topLevelValue;

        //
        // Get version info and extract useful data
        //
        String pidcName = "";
        try {
            EcvJsonObject pidcVersionInfo = topLevelObject.getValue("pidcVersionInfo", EcvJsonObject.class);
            EcvJsonObject pidcVersion = pidcVersionInfo.getValue("pidcVersion", EcvJsonObject.class);
            pidcName = pidcVersion.getString("name");
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.log(Level.SEVERE, "No version info found.", ex);
            ToolUsageLogger.logError(RestClient_iCDM.class.getCanonicalName(), ex);
            LOGGER.warning("No version info found:\n" + topLevelValue.toJsonString());
        }

        //
        // Get the attribute map
        //
        EcvJsonObject jsonAttributeMap;
        try {
            jsonAttributeMap = topLevelObject.getValue("pidcAttributeMap", EcvJsonObject.class);
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.severe("Attribute map missing in result:\n" + topLevelValue.toJsonString());
            ToolUsageLogger.logError(RestClient_iCDM.class.getCanonicalName(), ex);
            throw new RestResponseException("Attribute map missing in result.", ex);
        }

        //
        // Extract the milestones from the attribute map
        //
        List<RestClient_iCDM_Milestone> result = new ArrayList<>();
        for (EcvJsonValue jsonAttribute : jsonAttributeMap.getElements()) {
            if (jsonAttribute instanceof EcvJsonObject) {
                EcvJsonObject object = (EcvJsonObject) jsonAttribute;
                EcvJsonNumber attrId = object.getValueOrNull("attrId", EcvJsonNumber.class);
                if (attrId != null) {
                    RestClient_iCDM_MilestoneType milestoneType = typeMap.get(attrId.getNumber().intValue());
                    if (milestoneType != null) {
                        try {
                            int id = object.getValue("attrId", EcvJsonNumber.class).getNumber().intValue();
                            String name = object.getString("name");
                            String description = object.getStringOrNullValue("description");
                            String value = object.getStringOrNullValue("value");
                            result.add(new RestClient_iCDM_Milestone(pidcVerID, pidcName, milestoneType, id, name, description, value));
                        } catch (EcvJsonObject.JsonNotFound ex) {
                            LOGGER.log(Level.WARNING, "Mandatory field for milestone type missing.", ex);
                            LOGGER.log(Level.WARNING, "JSON-Object without mandatory field:\n{0}", object.toJsonString());
                        }
                    }
                }
            }
        }
        return (result);
    }

    public List<RestClient_iCDM_Milestone> getMilestones(String pidcVerID) throws RestException {
        assert (pidcVerID != null);
        assert (pidcVerID.isEmpty() == false);

        if (milestoneTypes == null) {
            milestoneTypes = getMilestoneTypes();
        }

        return (getMilestones(milestoneTypes, pidcVerID));
    }
}
