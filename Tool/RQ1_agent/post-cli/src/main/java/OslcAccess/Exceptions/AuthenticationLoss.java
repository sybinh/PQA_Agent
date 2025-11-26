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
 * @author GUG2WI
 */
public class AuthenticationLoss extends TemporaryServerError {

    @Override
    public String getMessageForUi() {
      return( "Authentication has failed during an established connection.");
    }

}
