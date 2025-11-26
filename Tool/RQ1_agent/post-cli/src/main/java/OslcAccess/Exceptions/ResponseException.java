/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Exceptions;

import util.EcvXmlElement;

/**
 *
 * @author gug2wi
 */
public class ResponseException extends OslcException {

    final private String txt;
    final private EcvXmlElement serverResponse;

    public ResponseException(String function, String action) {
        super("Response Error in " + function + "(): " + action);
        txt = "Response Error in " + function + "(): " + action;
        this.serverResponse = null;
    }

    public ResponseException(String function, String action, EcvXmlElement serverResponse) {
        super("Response Error in " + function + "(): " + action);
        txt = "Response Error in " + function + "(): " + action;
        this.serverResponse = serverResponse;
    }

    public ResponseException(String function, String action, Throwable cause) {
        super("Response Error in " + function + "(): " + action, cause);
        txt = "Response Error in " + function + "(): " + action;
        this.serverResponse = null;
    }

    public ResponseException(String function, String action, EcvXmlElement serverResponse, Throwable cause) {
        super("Response Error in " + function + "(): " + action, cause);
        txt = "Response Error in " + function + "(): " + action;
        this.serverResponse = serverResponse;
    }

    public ResponseException(String action) {
        super(action);
        txt = action;
        this.serverResponse = null;
    }

    public ResponseException(String action, Throwable cause) {
        super(action, cause);
        txt = action;
        this.serverResponse = null;
    }

    public ResponseException(String action, EcvXmlElement serverResponse) {
        super(action);
        txt = action;
        this.serverResponse = serverResponse;
    }

    public ResponseException(String action, EcvXmlElement serverResponse, Throwable cause) {
        super(action, cause);
        txt = action;
        this.serverResponse = serverResponse;
    }

    @Override
    public String getMessage() {
        if (serverResponse != null) {
            return txt + "\n" + serverResponse.getUiString();
        } else {
            return super.getMessage();
        }
    }
}
