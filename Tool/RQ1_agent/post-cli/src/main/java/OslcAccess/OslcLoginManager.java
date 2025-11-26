/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.Exceptions.NoLoginDataException;
import UiSupport.EcvUserMessage;
import UiSupport.EcvUserMessage.MessageType;
import UiSupport.UiDispatchTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvApplication;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public class OslcLoginManager {

    final static private OslcLoginData loginData = new OslcLoginData();

    static public void shutdown() {
        OslcRq1Client.getOslcClient().shutdown();
        OslcDoorsClient.client.shutdown();
    }

    /**
     * Get login data from the user or the login configuration and returns it.
     *
     * @return Login data provided by user or login configuration. null, if no
     * login data is available.
     * @throws OslcAccess.Exceptions.NoLoginDataException
     */
    static public synchronized OslcLoginData getFirstLoginData() throws NoLoginDataException {

        EcvApplication.LoginData configuredLoginData = EcvApplication.getLoginData();

        if (configuredLoginData != null) {
            loginData.set(configuredLoginData.getLoginName(), configuredLoginData.getPassword(), configuredLoginData.getServerDescription());
            return (loginData);
        } else {
            return (getNextLoginData());
        }
    }

    static public synchronized OslcLoginData getNextLoginData() throws NoLoginDataException {

        String originalTaskAction = UiWorker.getMyTaskAction();
        UiWorker.setMyTaskAction("Waiting for login data");
        //
        // Ask user for login data.
        //
        if (loginData.getState() == OslcLoginData.State.EMPTY) {
            UiDispatchTask.invokeAndWait(new UiDispatchTask<Void, Void, Void>() {
                @Override
                protected Void doTask(Void p1, Void p2) {
                    NewLoginWindow dialog = new NewLoginWindow(loginData, OslcRq1ServerDescription.values());
                    dialog.setVisible(true);
                    return (null);
                }
            });

        }
        //
        // Continue only, if user entered login data.
        //
        if (loginData.getState() == OslcLoginData.State.EMPTY) {
            EcvUserMessage.showMessageDialog(
                    "No login data provided. '" + originalTaskAction + "' canceled.",
                    "Login cancelled.",
                    MessageType.INFORMATION_MESSAGE);
            throw (new NoLoginDataException("connect", "Get login data from user"));
        }
        UiWorker.setMyTaskAction("Connect to server");

        return (loginData);
    }

    static public OslcLoginData getTestLoginData() {
        try {
            return (getFirstLoginData());
        } catch (NoLoginDataException ex) {
            Logger.getLogger(OslcLoginManager.class.getName()).log(Level.SEVERE, null, ex);
            return (null);
        }
    }

}
