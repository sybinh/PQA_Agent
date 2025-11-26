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
public class UnknownHostException extends TemporaryServerError {

    final private String urlString;

    public UnknownHostException(String urlString, Throwable cause) {
        super("Get method for urlString=\"" + urlString + "\".", cause);
        this.urlString = urlString;
    }

    @Override
    public String getMessageForUi() {
        StringBuilder b = new StringBuilder(100);
        b.append("Unknown Host\n"
                + "\n"
                + "URL: ");
        b.append(urlString);
        return (b.toString());
    }
}
