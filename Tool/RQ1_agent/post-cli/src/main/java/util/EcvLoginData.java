/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import RestClient.GeneralServerDescription;
import ToolUsageLogger.ToolUsageLogger;

/**
 * Container for holding the login data used within EcvTool.
 *
 * @author GUG2WI
 */
public class EcvLoginData {

    private final String loginName;
    private final char[] password;
    //
    private final GeneralServerDescription serverDescription;

    public EcvLoginData(String loginName, char[] password, GeneralServerDescription serverDescription) {
        assert (loginName != null);
        assert (loginName.isEmpty() == false);
        assert (password != null);
        assert (password.length > 0);
        assert (serverDescription != null);
        this.loginName = loginName;
        this.password = password;
        this.serverDescription = serverDescription;
        ToolUsageLogger.setDbname(serverDescription);
    }

    public String getLoginName() {
        return loginName;
    }

    public char[] getPassword() {
        return password;
    }

    public GeneralServerDescription getServerDescription() {
        return serverDescription;
    }

    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + ": " + loginName);
    }

}
