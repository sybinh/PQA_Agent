/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.HIS;

import RestClient.RestServerDescriptionI;

/**
 *
 * @author GUG2WI
 */
public enum RestServerDescription_HIS implements RestServerDescriptionI {

    PRODUCTIVE("https://wi-his.emea.bosch.com/his/service"),
    REGRESSION_TEST("https://wi0dgs13.emea.bosch.com/his/service"),
    DEVELOPMENT("https://wi0dgs13.emea.bosch.com:7533/his/service");

    final private String serviceUrl;

    private RestServerDescription_HIS(String serviceUrl) {
        assert (serviceUrl != null);
        assert (serviceUrl.isEmpty() == false);

        this.serviceUrl = serviceUrl;
    }

    @Override
    public String getServiceUrl() {
        return (serviceUrl);
    }

    @Override
    public String getLogoutUrl() {
        return (serviceUrl + "/auth/logout");
    }

    @Override
    public int getMaxRequestNumberPerSession() {
        return (-1);
    }

}
