/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Authentication;

import RestClient.Exceptions.RestInternalException;
import java.util.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

/**
 *
 * @author GUG2WI
 */
public class BasicAuthenticationProvider implements AuthenticationProviderI {

    final private String loginName;
    final private String pw;

    public BasicAuthenticationProvider(String loginName, String pw) {

        this.loginName = (loginName != null) ? loginName : "";
        this.pw = (pw != null) ? pw : "";
    }

    @Override
    public void setAuthData(HttpClient httpClient) {
        assert (httpClient != null);

    }

    @Override
    public void setAuthData(HttpRequestBase httpMethod) throws RestInternalException {
        String encoding = new String(Base64.getEncoder().encode((loginName + ":" + pw).getBytes()));
        httpMethod.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
    }

}
