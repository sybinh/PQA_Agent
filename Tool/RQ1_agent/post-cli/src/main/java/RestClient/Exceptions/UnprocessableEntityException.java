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
public class UnprocessableEntityException extends TemporaryServerError {

    final private String responseAsText;

    public UnprocessableEntityException(String responseAsText) {
        super("Get method for urlString=\"" + responseAsText + "\".");
        this.responseAsText = responseAsText;
    }

    @Override
    public String getMessageForUi() {
        StringBuilder b = new StringBuilder(100);
        b.append("Unprocessable Entity Exception.\n"
                + "\n"
                + "Details:\n");
        b.append(responseAsText);
        return (b.toString());
    }
}
