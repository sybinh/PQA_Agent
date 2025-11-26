/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

import util.EcvXmlElement;

/**
 *
 * @author gug2wi
 */
public class FieldNotFoundException extends ResponseException {

    public FieldNotFoundException(String action, EcvXmlElement serverResponse, Throwable ex) {
        super(action, serverResponse, ex);
    }

    public FieldNotFoundException(String action, EcvXmlElement serverResponse) {
        super(action, serverResponse);
    }

    public FieldNotFoundException(String action) {
        super(action);
    }

}
