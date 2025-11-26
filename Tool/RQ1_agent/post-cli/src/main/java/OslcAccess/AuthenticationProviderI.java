/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.Exceptions.InternalException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

/**
 *
 * @author gug2wi
 */
interface AuthenticationProviderI {

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
    void setAuthData(HttpMethod httpMethod) throws InternalException;

}
