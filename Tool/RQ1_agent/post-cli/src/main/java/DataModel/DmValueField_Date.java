/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvDate;

/**
 * A field that stores a date value.
 *
 * @author gug2wi
 */
public class DmValueField_Date extends DmValueField<EcvDate> implements DmValueFieldI_Date {

    public DmValueField_Date(String nameForUserInterface, EcvDate value) {
        super(nameForUserInterface, value);
    }
}
