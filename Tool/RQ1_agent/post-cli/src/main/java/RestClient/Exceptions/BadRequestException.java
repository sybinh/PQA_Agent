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
public class BadRequestException extends RestException {

    final String message;

    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessageDialogText() {

        return ("The following response was received from the RQ1 server:\n\n" + message);
    }

    @Override
    public String getMessageDialogTitle() {
        return ("Bad Request");
    }

}
