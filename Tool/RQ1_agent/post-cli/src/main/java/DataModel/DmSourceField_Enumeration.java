/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvEnumeration;

/**
 * Supports the implementation of a field whose text value is taken from some
 * source. The access to the source has to be implemented in the subclass.
 *
 * @author gug2wi
 */
public abstract class DmSourceField_Enumeration extends DmSourceField<EcvEnumeration> implements DmValueFieldI_Enumeration {

    public DmSourceField_Enumeration(String nameForUserInterface) {
        super(nameForUserInterface);
    }

}
