/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import util.EcvApplication;

/**
 * Implements the authentication for the RQ1 OSLC interface.
 *
 * @author gug2wi
 */
public class OslcRq1AuthenticationProvider implements AuthenticationProviderI {

    final private OslcLoginData loginData;

    OslcRq1AuthenticationProvider(OslcLoginData loginData) {
        assert (loginData != null);
        this.loginData = loginData;
    }

    @Override
    public void setAuthData(HttpClient httpClient) {
        assert (httpClient != null);

        UsernamePasswordCredentials credentials;
        credentials = new UsernamePasswordCredentials(loginData.getLoginName(), new String(loginData.getPassword()));
        httpClient.getParams().setAuthenticationPreemptive(true);
        httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        httpClient.getHttpConnectionManager().getParams().setStaleCheckingEnabled(false);
    }

    @Override
    public void setAuthData(HttpMethod httpMethod) {
        assert (httpMethod != null);
        httpMethod.setRequestHeader("x-requester", "toolname=" + EcvApplication.getToolName() + ";toolversion=" + EcvApplication.getToolVersionForAccessControl() + ";user=" + loginData.getLoginName());
    }

}
