/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI_Text;
import DataStore.DsRecordI;

/**
 *
 * @author gug2wi
 * @param <T_DS_RECORD> Type of data source parent record.
 */
public class DmToDsField_Text<T_DS_RECORD extends DsRecordI<?>> extends DmToDsValueField<T_DS_RECORD, String> implements DmValueFieldI_Text {

    public DmToDsField_Text(DsFieldI_Text<T_DS_RECORD> rq1TextField, String nameForUserInterface) {
        super(rq1TextField, nameForUserInterface);
    }

    /**
     * Checks, if the field is empty. The field is empty, if no text is set or
     * an empty text is set,
     *
     * @return false if a non empty text is set. true otherwise.
     */
    final public boolean isEmpty() {
        return ((getValue() == null) || (getValue().isEmpty() == true));
    }

    @Override
    final public String toString() {
        return (getNameForUserInterface() + "='" + getValue() + "'");
    }

}
