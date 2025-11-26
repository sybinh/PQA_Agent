/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.iCDM;

import RestClient.RestServerDescriptionI;

/**
 *
 * @author GUG2WI
 */
public enum RestServerDescription_iCDM implements RestServerDescriptionI {

    PRODUCTIVE("https://si-cdm02.de.bosch.com"),
    TEST("https://si-cdm01.de.bosch.com:8643");

    final private String serviceUrl;
    private String password;

    private RestServerDescription_iCDM(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setAccessData(String password) {
        this.password = password;
    }

    public String getPassword() {
        return (password);
    }

    @Override
    public String getServiceUrl() {
        return (serviceUrl);
    }

}
