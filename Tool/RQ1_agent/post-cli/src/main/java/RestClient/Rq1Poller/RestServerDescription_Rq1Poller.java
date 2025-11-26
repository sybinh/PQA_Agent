/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Rq1Poller;

import RestClient.RestServerDescriptionI;

/**
 *
 * @author hfi5wi
 */
public enum RestServerDescription_Rq1Poller implements RestServerDescriptionI {

    PRODUCTIVE("http://rb-powertrain-jenkins.de.bosch.com/rqp/check");
    
    final private String serviceUrl;

    private RestServerDescription_Rq1Poller(String serviceUrl) {
        assert (serviceUrl != null);
        assert (serviceUrl.isEmpty() == false);

        this.serviceUrl = serviceUrl;
    }
    
    @Override
    public String getServiceUrl() {
        return (serviceUrl);
    }
    
}
