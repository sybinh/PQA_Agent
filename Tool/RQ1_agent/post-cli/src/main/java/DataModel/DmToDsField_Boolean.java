/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI_Boolean;
import DataStore.DsRecordI;

/**
 *
 * @author gug2wi
 * @param <T_DS_RECORD> Type of data source parent record.
 */
public class DmToDsField_Boolean<T_DS_RECORD extends DsRecordI<?>> extends DmToDsValueField<T_DS_RECORD, Boolean> implements DmValueFieldI_Boolean {

    public DmToDsField_Boolean(DsFieldI_Boolean<T_DS_RECORD> rq1TextField, String nameForUserInterface) {
        super(rq1TextField, nameForUserInterface);
    }

    @Override
    final public String toString() {
        return (getNameForUserInterface() + "= " + getValue());
    }

}
