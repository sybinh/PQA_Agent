/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import OslcAccess.ALM.OslcAlmClient;
import OslcAccess.Rq1.OslcRq1Client;
import OslcAccess.Doors.OslcDoorsClient;
import RestClient.Exceptions.NoLoginDataException;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.UiDispatchTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import RestClient.GeneralServerDescription;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the login data for EcvTool.
 *
 * @author GUG2WI
 */
public class EcvLoginManager {

    static private EcvLoginData loginData = null;
    //
    static private String loginUserFullName;
    //
    static private List<String> loginNameVariants = null;
    static private Iterator<String> lastUsedLoginNameVariant = null;

    static public synchronized EcvLoginData getFirstLoginData() throws NoLoginDataException {

        if (loginData != null) {
            return (loginData);
        }

        loginData = EcvApplication.getLoginData();

        if (loginData != null) {
            return (loginData);
        }

        return (getNextLoginData());
    }

    static public synchronized EcvLoginData getNextLoginData() throws NoLoginDataException {

        UiWorkerManager.setMyTaskAction("Waiting for login data");
        //
        // Ask user for login data.
        //
        if (loginData == null) {
            UiDispatchTask.invokeAndWait(new UiDispatchTask<Void, Void, Void>() {
                @Override
                protected Void doTask(Void p1, Void p2) {
                    EcvLoginWindow dialog = new EcvLoginWindow(GeneralServerDescription.values());
                    dialog.setVisible(true);
                    loginData = dialog.getLoginData();
                    return (null);
                }
            });
        }
        //
        // Continue only, if user entered login data.
        //
        if (loginData == null) {
            throw (new NoLoginDataException("connect", "Login window closed."));
        }
        UiWorkerManager.setMyTaskAction("Connect to server");

        return (loginData);
    }

    static public EcvLoginData getLoginDataVariant() {

        if (loginData == null) {
            return (null);
        }

        if (lastUsedLoginNameVariant == null) {
            loginNameVariants = new ArrayList<>();
            loginNameVariants.add(loginData.getLoginName().toUpperCase());
            loginNameVariants.add(loginData.getLoginName().toLowerCase());
            lastUsedLoginNameVariant = loginNameVariants.iterator();
        }

        if (lastUsedLoginNameVariant.hasNext()) {
            var newLoginData = new EcvLoginData(lastUsedLoginNameVariant.next(), loginData.getPassword(), loginData.getServerDescription());
            loginData = newLoginData;
            return (loginData);
        } else {
            loginData = null;
            loginNameVariants = null;
            lastUsedLoginNameVariant = null;
            return (null);
        }
    }

    static public EcvLoginData getTestLoginData() {
        try {
            return (getFirstLoginData());
        } catch (NoLoginDataException ex) {
            Logger.getLogger(EcvLoginManager.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(EcvLoginManager.class.getCanonicalName(), ex);
            return (null);
        }
    }

    static public void confirmSuccessFullLogin(EcvLoginData confirmedLoginData) {
        assert (loginData != null);
        loginData = confirmedLoginData;
        new EcvLoginCustomizer().setLoginName(loginData.getLoginName());
        ToolUsageLogger.checkLoggingActive();
    }

    static public void setCurrentUserFullName(String fullName) {
        loginUserFullName = fullName;
    }

    static public String getCurrentDatabaseName() {
        StringBuilder databaseName = new StringBuilder();
        String rq1Database = OslcRq1Client.getOslcClient().getCurrentDatabaseName();
        String doorsDatabase = OslcDoorsClient.getCurrentDatabaseName();
        String almDatabase = OslcAlmClient.client.getCurrentDatabaseName();
        if (rq1Database != null) {
            databaseName.append(rq1Database);
        }
        if (doorsDatabase != null) {
            if (databaseName.length() > 0) {
                databaseName.append(" & ");
            }
            databaseName.append(doorsDatabase);
        }
        if (almDatabase != null) {
            if (databaseName.length() > 0) {
                databaseName.append(" & ");
            }
            databaseName.append(almDatabase);
        }
        if (databaseName.length() == 0) {
            databaseName.append("No database");
        }
        return (databaseName.toString());
    }

    static public String getCurrentLoginName() {
        if (loginData != null) {
            return (loginData.getLoginName());
        }
        return ("No user");
    }

    static public String getCurrentUserFullName() {
        return (loginUserFullName);
    }

    static public void shutdown() {
        OslcRq1Client.getOslcClient().shutdown();
        OslcDoorsClient.client.shutdown();
        OslcAccess.ALM.OslcAlmClient.client.shutdown();
    }

}
