/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import java.util.logging.Logger;
import util.EcvEnumeration;

/**
 * A field that holds a constant string value. The constant value is set during
 * the construction of the field.
 *
 * @author gug2wi
 */
public class DmConstantField_Enumeration extends DmConstantField<EcvEnumeration> implements DmValueFieldI_Enumeration_EagerLoad {

    final static private Logger LOGGER = Logger.getLogger(DmConstantField_Enumeration.class.getCanonicalName());

    final private EcvEnumeration[] validValues;

    public DmConstantField_Enumeration(String nameForUserInterface, EcvEnumeration value) {
        super(nameForUserInterface, value);

        if (value != null) {
            validValues = new EcvEnumeration[1];
            validValues[0] = value;
        } else {
            validValues = new EcvEnumeration[0];
        }
    }

    @Override
    public EcvEnumeration[] getValidInputValues() {
        return (validValues);
    }

}
