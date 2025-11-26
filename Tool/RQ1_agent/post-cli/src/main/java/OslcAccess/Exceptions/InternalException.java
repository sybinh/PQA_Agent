/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Exceptions;

/**
 *
 * @author gug2wi
 */
public class InternalException extends OslcException {

    public InternalException(String action) {
        super("Internal Error: " + action);
    }

    public InternalException(String function, String action) {
        super("Internal Error in " + function + "(): " + action);
    }

    public InternalException(String function, String action, Throwable cause) {
        super("Internal Error in " + function + "(): " + action, cause);
    }

    public InternalException(String action, Throwable cause) {
        super("Internal Error in: " + action, cause);
    }
}
