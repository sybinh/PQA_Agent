/*
 * Copyright (c) 2009, 2023 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under the terms of the Bosch Internal Open Source License v4 which accompanies this distribution, and is available at http://bios.intranet.bosch.com/bioslv4.txt
 */

package util;

import OslcAccess.Rq1.OslcRq1Client;
import RestClient.Exceptions.RestException;
import RestClient.GeneralServerDescription;
import Rq1Cache.Records.Rq1RecordInterface;
import model.Model;
import util.EcvApplication;
import util.EcvLoginData;

public class Rq1Service {

    private String username = "";
    private String password = "";
    private GeneralServerDescription database = GeneralServerDescription.PRODUCTIVE;

    private static final String TOOL_NAME = "OfficeUtils";
    private static final String VERSION_FOR_LOGGING = "1.0";
    private static final String VERSION_ACCESS_CONTROL = "1.0";

    public Rq1Service() {
    }

    public Rq1Service(String username, String password, String database) {
        this.username = username;
        this.password = password;
        switch (database) {
            case "RQ1_PRODUCTIVE":
                this.database = GeneralServerDescription.PRODUCTIVE;
                break;
            case "RQ1_APPROVAL":
                this.database = GeneralServerDescription.APPROVAL;
                break;
            case "RQ1_ACCEPTANCE":
                this.database = GeneralServerDescription.ACCEPTANCE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + database);
        }
    }

    public static String getToolName() {
        return (TOOL_NAME);
    }

    public static String getVersionForLogging() {
        return (VERSION_FOR_LOGGING);
    }


    public static String getVersionForAccessControl() {
        return (VERSION_ACCESS_CONTROL);
    }

    public void connectToRq1(boolean loginECVInterface) throws RestException {

        if (loginECVInterface){
            loginUserViaUserInterface();
        } else {
            setLoginApplicationDataConnection();
        }

        OslcRq1Client<Rq1RecordInterface> client = OslcRq1Client.getOslcClient();

        client.loadLoginUser();
        Model.client = client;
    }

    private void loginUserViaUserInterface() {
        EcvApplication.setApplicationData("OfficeUtils",
                "1.0", "1.0",
                EcvApplication.ApplicationType.UserInterface);
    }

    private void setLoginApplicationDataConnection() {
        EcvLoginData loginData = new EcvLoginData(username, password.toCharArray(), database);
        EcvApplication.setLoginData(loginData);
        EcvApplication.setApplicationData(getToolName(),
                getVersionForLogging(),
                getVersionForAccessControl(),
                EcvApplication.ApplicationType.DaemonProcess);
    }
}