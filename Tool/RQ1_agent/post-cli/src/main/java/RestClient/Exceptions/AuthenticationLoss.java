/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

import RestClient.Exceptions.TemporaryServerError;

/**
 *
 * @author GUG2WI
 */
public class AuthenticationLoss extends TemporaryServerError {

    public AuthenticationLoss(String action) {
        super("Authorization Error: " + action);
    }

    @Override
    public String getMessageForUi() {
        return ("Authentication has failed during an established connection.");
    }

}
