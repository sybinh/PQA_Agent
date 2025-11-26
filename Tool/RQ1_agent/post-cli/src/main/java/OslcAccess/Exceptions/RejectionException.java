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
public class RejectionException extends OslcException {

    public RejectionException(int statusCode, String action) {
        super("Request rejected with status code " + statusCode + " during " + action);
    }

    public RejectionException(String function, int statusCode, String action) {
        super(function + "(): Request rejected with status code " + statusCode + " during " + action);
    }
}
