/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataStore.ALM.DsAlmEnumerationList;

/**
 *
 * @author GUG2WI
 */
public class DmAlmEnumerationList {

    private final DmAlmEnumerationValue[] values;

    DmAlmEnumerationList() {
        values = new DmAlmEnumerationValue[0];
    }

    DmAlmEnumerationList(DsAlmEnumerationList dsList) {
        assert (dsList != null);

        int size = dsList.getValues().size();
        values = new DmAlmEnumerationValue[size];

        for (int ordinal = 0; ordinal < size; ordinal++) {
            values[ordinal] = new DmAlmEnumerationValue(dsList.getValues().get(ordinal), ordinal);
        }
    }

    public DmAlmEnumerationValue[] getArray() {
        return (values);
    }

}
