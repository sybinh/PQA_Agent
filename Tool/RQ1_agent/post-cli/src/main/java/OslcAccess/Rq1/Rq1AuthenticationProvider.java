/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Rq1;

import RestClient.Authentication.AuthenticationProviderI;
import java.util.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import util.EcvApplication;
import util.EcvLoginData;

/**
 * Implements the authentication for the RQ1 OSLC interface.
 *
 * @author gug2wi
 */
public class Rq1AuthenticationProvider implements AuthenticationProviderI {

    final private EcvLoginData loginData;

    public Rq1AuthenticationProvider(EcvLoginData loginData) {
        assert (loginData != null);
        this.loginData = loginData;
    }

    @Override
    public void setAuthData(HttpClient httpClient) {
        assert (httpClient != null);
        

    }

    @Override
    public void setAuthData(HttpRequestBase httpMethod) {
        assert (httpMethod != null);
        String pw = String.valueOf(loginData.getPassword()); 
        String encoding = new String(Base64.getEncoder().encode((loginData.getLoginName() + ":" + pw).getBytes()));
        httpMethod.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        httpMethod.setHeader("x-requester", "toolname=" + EcvApplication.getToolName() + ";toolversion=" + EcvApplication.getToolVersionForAccessControl() + ";user=" + loginData.getLoginName());
    }

}
