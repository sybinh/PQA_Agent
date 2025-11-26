/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
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
public class TemporaryRejectionException extends TemporaryServerError {

    public TemporaryRejectionException(int statusCode, String action) {
        super("Request rejected with status code " + statusCode + " during " + action);
    }

    public TemporaryRejectionException(String function, int statusCode, String action) {
        super(function + "(): Request rejected with status code " + statusCode + " during " + action);
    }

    @Override
    public String getMessageForUi() {
        return (super.getMessage());
    }
}
