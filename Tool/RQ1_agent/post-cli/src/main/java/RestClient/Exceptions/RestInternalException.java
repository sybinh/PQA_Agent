/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

/**
 *
 * @author gug2wi
 */
public class RestInternalException extends RestException {

    public RestInternalException(String action) {
        super("Internal Error: " + action);
    }

    public RestInternalException(String function, String action) {
        super("Internal Error in " + function + "(): " + action);
    }

    public RestInternalException(String function, String action, Throwable cause) {
        super("Internal Error in " + function + "(): " + action, cause);
    }

    public RestInternalException(String action, Throwable cause) {
        super("Internal Error in: " + action, cause);
    }
}
