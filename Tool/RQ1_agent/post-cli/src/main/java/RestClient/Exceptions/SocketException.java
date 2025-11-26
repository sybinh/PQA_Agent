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
public class SocketException extends TemporaryServerError {

    final private String urlString;
    final private Throwable cause;

    public SocketException(String urlString, Throwable cause) {
        super("Get method for urlString=\"" + urlString + "\".", cause);
        this.urlString = urlString;
        this.cause = cause;
    }

    @Override
    public String getMessageForUi() {
        StringBuilder b = new StringBuilder(100);
        if (cause != null) {
            b.append("Problem on socket interface: ").append(cause.getMessage()).append("\n\n");
        } else {
            b.append("Problem on socket interface.\n\n");
        }
        b.append("URL: ").append(urlString);
        return (b.toString());
    }
}
