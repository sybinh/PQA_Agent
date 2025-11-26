/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
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
public class OslcException extends Exception {

    final static int maxLineLength = 180;

    public OslcException() {
    }

    public OslcException(String s) {
        super(s);
    }

    public OslcException(String s, Throwable cause) {
        super(s, cause);
    }

    public String getMessageDialogTitle() {
        return ("Request to RQ1 server failed.");
    }

    public String getMessageDialogText() {
        StringBuilder messageText = new StringBuilder(100);

        messageText.append("Request to RQ1 server failed. Received data might by incomplete!");

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
