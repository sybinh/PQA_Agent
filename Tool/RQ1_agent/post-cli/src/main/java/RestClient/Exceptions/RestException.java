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
public class RestException extends Exception {

    final static int maxLineLength = 180;

    public RestException() {
    }

    public RestException(String s) {
        super(s);
    }

    public RestException(String s, Throwable cause) {
        super(s, cause);
    }

    public String getMessageDialogTitle() {
        return ("Request to server failed.");
    }

    public String getMessageDialogText() {
        StringBuilder messageText = new StringBuilder(100);

        messageText.append("Request to server failed. Received data might by incomplete!");

        String s = getMessage();
        if (s != null) {
            messageText.append("\n");
            messageText.append("\n");
            int lineNumber = 0;
            while ((s.length() > maxLineLength) && (lineNumber < 15)) {
                messageText.append(s.substring(0, maxLineLength)).append("\n");
                s = s.substring(maxLineLength);
                lineNumber++;
            }
            if (s.length() <= maxLineLength) {
                messageText.append(s);
            } else {
                messageText.append("...");
            }
        }

        return (messageText.toString());
    }
}
