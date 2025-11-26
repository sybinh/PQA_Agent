/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

/**
 *
 * @author GUG2WI
 */
public interface RestServerDescriptionI {

    /**
     * Returns the URL for the service that will be accessed.
     *
     * @return
     */
    public String getServiceUrl();

    /**
     * Returns the URL for deleting (closing) of the connection to the OSLC
     * server.
     *
     * @return The URL for closing the connection or null if no closing is
     * necessary.
     */
    default public String getLogoutUrl() {
        return (null);
    }

    /**
     * Returns the maximum number of requests that are allowed in one session.
     * After reaching the maximum number, connection to the server shall be
     * closed and a new connection shall be established.
     *
     * @return The maximum Number of -1 if no maximum is defined.
     */
    default public int getMaxRequestNumberPerSession() {
        return (-1);
    }

}
