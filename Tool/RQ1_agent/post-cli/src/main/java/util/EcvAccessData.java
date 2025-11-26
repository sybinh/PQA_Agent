/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import DataStore.MCR.MCR_ServerDescription;
import OslcAccess.Doors.OslcDoorsServerDescription;
import RestClient.iCDM.RestServerDescription_iCDM;
import SharePoint.SPCommandExecutorCRP;
import ToolUsageLogger.ToolUsageLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Supports the management of data needed to access other databases then RQ1.
 *
 * @author GUG2WI
 */
public class EcvAccessData {

    final private static Logger LOGGER = Logger.getLogger(EcvAccessData.class.getCanonicalName());

    protected final EcvJsonObject jsonData;

    /**
     * Create the access data object from a JSON string with the access data.
     *
     * @param jsonString
     * @throws util.EcvParser.ParseException
     */
    public EcvAccessData(String jsonString) throws EcvParser.ParseException {
        try {
            jsonData = (EcvJsonObject) new EcvJsonParser(jsonString).parse();
        } catch (EcvParser.ParseException ex) {
            LOGGER.log(Level.SEVERE, "Error when parsing the access data.", ex);
            ToolUsageLogger.logError(EcvAccessData.class.getCanonicalName(), ex);
            throw (ex);
        }
    }

    /**
     * Set the key values to access the DOORS database.
     */
    public void setDoorsKeys() {

        try {
            EcvJsonObject productive = jsonData.getObject("Doors").getObject("Productive");
            OslcDoorsServerDescription.PRODUCTIVE.setCustomerKeyAndSecret(productive.getString("Key"), productive.getString("Secret"));
            EcvJsonObject test = jsonData.getObject("Doors").getObject("Test");
            OslcDoorsServerDescription.TEST.setCustomerKeyAndSecret(test.getString("Key"), test.getString("Secret"));
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.log(Level.SEVERE, "Reading keys for Doors failed.", ex);
            ToolUsageLogger.logError(EcvAccessData.class.getCanonicalName(), ex);
        }

    }

    /**
     * Set the credentials for accessing the MCR database.
     */
    public void setMcrCredentials() {
        try {
            EcvJsonObject productive = jsonData.getObject("MCR").getObject("Productive");
            MCR_ServerDescription.PRODUCTIVE.setCredentials(productive.getString("user"), productive.getString("password"));
            EcvJsonObject test = jsonData.getObject("MCR").getObject("Test");
            MCR_ServerDescription.QUA2_TEST.setCredentials(test.getString("user"), test.getString("password"));
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.log(Level.SEVERE, "Reading credentials for MCR failed.", ex);
            ToolUsageLogger.logError(EcvAccessData.class.getCanonicalName(), ex);
        }
    }

    /**
     * Set the access data for accessing the iCDM database.
     */
    public void setiCDMData() {
        try {
            EcvJsonObject productive = jsonData.getObject("iCDM").getObject("Productive");
            RestServerDescription_iCDM.PRODUCTIVE.setAccessData(productive.getString("password"));
            EcvJsonObject test = jsonData.getObject("iCDM").getObject("Productive");
            RestServerDescription_iCDM.TEST.setAccessData(test.getString("password"));
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.log(Level.SEVERE, "Reading data for iCDM failed.", ex);
            ToolUsageLogger.logError(EcvAccessData.class.getCanonicalName(), ex);
        }
    }

    public void setSuperOPLKey() {
        try {
            String key = jsonData.getObject("SuperOPL").getString("key");
            EcvTableColumn_SecretString.SecretKeys.SUPER_OPL_KEY.setKey(key);
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.log(Level.SEVERE, "Reading data for SuperOPL failed.", ex);
            ToolUsageLogger.logError(EcvAccessData.class.getCanonicalName(), ex);
        }
    }

    /**
     * Set the credentials for accessing the SharePoint.
     */
    public void setSharePointData() {
        try {
            EcvJsonObject curUser = jsonData.getObject("CRP-SharePoint").getObject("currentUser");
            SPCommandExecutorCRP.UserData.CURUSER.setCredentials(curUser.getString("user"), curUser.getString("password"));
            EcvJsonObject nextUser = jsonData.getObject("CRP-SharePoint").getObject("nextUser");
            SPCommandExecutorCRP.UserData.NEXTUSER.setCredentials(nextUser.getString("user"), nextUser.getString("password"));
        } catch (EcvJsonObject.JsonNotFound ex) {
            LOGGER.log(Level.SEVERE, "Reading credentials for SharePoint failed.", ex);
            ToolUsageLogger.logError(EcvAccessData.class.getCanonicalName(), ex);
        }
    }

}
