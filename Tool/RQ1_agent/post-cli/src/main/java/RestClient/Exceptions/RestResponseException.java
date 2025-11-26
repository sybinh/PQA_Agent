/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

import util.EcvJsonTopLevelValue;

/**
 * Report a problem with the response. E.g. invalid encoding, wrong syntax, ...
 *
 * @author gug2wi
 */
public class RestResponseException extends RestException {

    final private EcvJsonTopLevelValue jsonResponse;

    public RestResponseException(String message) {
        super(message);
        this.jsonResponse = null;
    }

    public RestResponseException(String action, String problem) {
        super(action + ": " + problem);
        this.jsonResponse = null;
    }

    public RestResponseException(String action, Throwable cause) {
        super(action, cause);
        this.jsonResponse = null;
    }

    public RestResponseException(String message, EcvJsonTopLevelValue jsonResponse) {
        super(message);
        this.jsonResponse = jsonResponse;
    }

    public RestResponseException(String message, EcvJsonTopLevelValue jsonResponse, Throwable cause) {
        super(message, cause);
        this.jsonResponse = jsonResponse;
    }

    @Override
    public String getMessage() {
        if (jsonResponse != null) {
            return super.getMessage() + "\n" + jsonResponse.toJsonString();
        } else {
            return super.getMessage();
        }
    }
}
