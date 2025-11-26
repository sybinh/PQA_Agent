/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Implements a connection without any authentication.
 * @author gug2wi
 */
public class EmptyAuthenticationProvider implements AuthenticationProviderI {

    @Override
    public void setAuthData(HttpClient httpClient) {
    }

    @Override
    public void setAuthData(HttpMethod httpMethod) {
    }

}
