/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Tracking;

import ConfigurableRules.ConfigRule.util.Constants;
import ConfigurableRules.ConfigRule.util.Utils;
import OslcAccess.OslcCommandExecutor;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ULV81HC
 */
public class TrackingUsage {

    static String trackingUsageUrl = "https://sgpvmc0521.apac.bosch.com:8443/portal/api/tracking/save?toolId=%s&userId=%s";
    static String trackingFeatureUrl = "https://sgpvmc0521.apac.bosch.com:8443/portal/api/tracking/save?toolId=%s&functionName=%s&userId=%s";
    
//     static String trackingFeatureUrlTestServer = "https://sgpvmc0521.apac.bosch.com:8447/portal/api/tracking/save?toolId=%s&functionName=%s&userId=%s";
//     static String testUrl = "http://localhost:8090/api/tracking/save?toolId=%s&functionName=%s&userId=%s";

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(OslcCommandExecutor.class.getCanonicalName());

    public static boolean trackingUsage(String userId) {
        try {
            // If the tool is opened by developer -> not tracking.
            if (userId.equalsIgnoreCase(Constants.DEV_ID_ULV81HC)) {
                return false;
            }
        
            // If login user is NOT from RBEI / RBVH -> not tracking usage
            if (!Utils.isUsingFullMode()) {
                return false;
            }
        
            String stringUrl = String.format(trackingUsageUrl, Constants.USER_ASSISTANT_RULE_ID, userId);
            URL url = new URL(stringUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("OSLC-Core-Version", "2.0");
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true);
            conn.connect();

            String message = conn.getResponseCode() + Constants.SPACE + new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
            logger.log(Level.INFO, "Tracking Tool: {0}", message);

            conn.disconnect();

        } catch (Exception e) {
            Logger.getLogger(TrackingUsage.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(TrackingUsage.class.getName(), e);
            return false;
        }
        return true;
    }

    /**
     * Track when the function is used. Functions belong to User Assistant (ToolId="IPE_UserAssistant")
     * @param featureName - Name of the function to be tracked
     * @return True when call tracking API successfully.
     */
    public static boolean trackingFeature(String featureName) {
        try {
            // Login Name is user Id
            String userId = EcvLoginManager.getFirstLoginData().getLoginName();
            
            // If the tool is opened by developer -> not tracking.
            if (userId.equalsIgnoreCase(Constants.DEV_ID_ULV81HC)) {
                return false;
            }
            // If login user is not from RBEI / RBVH -> not tracking usage
            if (!Utils.isUsingFullMode()) {
                return false;
            }
        
            String stringUrl = String.format(trackingFeatureUrl, Constants.USER_ASSISTANT_RULE_ID, featureName, userId);
            URL url = new URL(stringUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("OSLC-Core-Version", "2.0");
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true);
            conn.connect();

            String message = conn.getResponseCode() + Constants.SPACE + new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
            logger.log(Level.INFO, "Tracking Tool: {0}", message);

            conn.disconnect();

        } catch (Exception e) {
            Logger.getLogger(TrackingUsage.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(TrackingUsage.class.getName(), e);
        }
        return true;
    }
    
}
