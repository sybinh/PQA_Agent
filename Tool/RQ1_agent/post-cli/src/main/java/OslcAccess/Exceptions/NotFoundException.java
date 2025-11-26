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
public class NotFoundException extends OslcException {

    final String addressForUi;

    public NotFoundException(String addressForUi) {
        super("Not Found Error for " + addressForUi);
        this.addressForUi = addressForUi;
    }

    public NotFoundException() {
        super("Not Found Error.");
        addressForUi = "";
    }

    public String getAddressForUi() {
        return addressForUi;
    }

}
