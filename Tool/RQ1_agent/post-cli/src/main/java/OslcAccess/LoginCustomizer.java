/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import Customization.EcvCustomizer;

/**
 *
 * @author gug2wi
 */
public final class LoginCustomizer {

    final private EcvCustomizer customizer;

    @SuppressWarnings("deprecation")
    public LoginCustomizer() {
        customizer = new EcvCustomizer("/DgsEcIpe/UserInterface/Login");
    }

    public String getLoginName() {
        return (customizer.getString("LoginName", ""));
    }

    public void setLoginName(String loginName) {
        assert (loginName != null);
        customizer.setString("LoginName", loginName);
    }

    public boolean getLowerLoginName() {
        return (customizer.getBoolean("LowerLoginName", true));
    }

    public void setLowerLoginName(boolean selected) {
        customizer.setBoolean("LowerLoginName", selected);
    }

}
