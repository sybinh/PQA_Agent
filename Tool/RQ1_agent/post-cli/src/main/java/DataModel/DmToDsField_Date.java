/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI;
import DataStore.DsFieldI_Date;
import DataStore.DsRecordI;
import util.EcvDate;

/**
 *
 * @author gug2wi
 * @param <T_DS_RECORD> Type of data source parent record.
 */
public class DmToDsField_Date<T_DS_RECORD extends DsRecordI<? extends DsFieldI>> extends DmToDsValueField<T_DS_RECORD, EcvDate> implements DmValueFieldI_Date {

    public DmToDsField_Date(DsFieldI_Date<T_DS_RECORD> dsField, String nameForUserInterface) {
        super(dsField, nameForUserInterface);
    }

    @Override
    final public String toString() {
        return (getNameForUserInterface() + "='" + getValue() + "'");
    }

    /**
     * Checks that the fields holds a non empty date. A null value is considered
     * as an empty date
     *
     * @return
     */
    final public boolean isNotEmpty() {
        return (EcvDate.isNotEmpty(getDate()));
    }
}
