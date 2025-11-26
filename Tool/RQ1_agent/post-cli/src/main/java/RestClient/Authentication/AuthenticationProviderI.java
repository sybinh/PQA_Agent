/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Authentication;

import RestClient.Exceptions.RestInternalException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

/**
 *
 * @author gug2wi
 */
public interface AuthenticationProviderI {

    /**
     * Sets the authentication data for the given HttpClient.
     *
     * @param httpClient Http client on which the authentication data shall be
     * set.
     */
    void setAuthData(HttpClient httpClient);

    /**
     * Sets the authentication data for the given HttpMethod.
     *
     * @param httpMethod Http method on which the authentication data shall be
     * set.
     * @throws OslcAccess.Exceptions.InternalException
     */
    void setAuthData(HttpRequestBase httpMethod) throws RestInternalException;

}
