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
 * A field that holds a constant string value. The constant value is set during
 * the construction of the field.
 *
 * @author gug2wi
 */
public class DmConstantField_Date extends DmConstantField<EcvDate> implements DmValueFieldI_Date {

    public DmConstantField_Date(String nameForUserInterface, EcvDate value) {
        super(nameForUserInterface, value);
    }
}
