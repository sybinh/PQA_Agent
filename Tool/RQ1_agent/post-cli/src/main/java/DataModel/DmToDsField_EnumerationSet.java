/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI;
import DataStore.DsFieldI_EnumerationSet;
import DataStore.DsRecordI;
import java.util.List;
import util.EcvEnumeration;
import util.EcvEnumerationSet;

/**
 *
 * @author gug2wi
 * @param <T_DS_RECORD> Type of data source parent record.
 */
public class DmToDsField_EnumerationSet<T_DS_RECORD extends DsRecordI<? extends DsFieldI>> extends DmToDsValueField<T_DS_RECORD, EcvEnumerationSet> implements DmValueFieldI_EnumerationSet {

    public DmToDsField_EnumerationSet(DsFieldI_EnumerationSet<T_DS_RECORD> dsField, String nameForUserInterface) {
        super(dsField, nameForUserInterface);
    }

    public DmToDsField_EnumerationSet(DsFieldI_EnumerationSet<T_DS_RECORD> dsField, String nameForUserInterface, int widthInCharacter) {
        super(dsField, nameForUserInterface);
        setWidthInCharacter(widthInCharacter);
    }

    @Override
    final public String getValueAsText() {
        if (getValue() != null) {
            return (getValue().getText());
        } else {
            return ("");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EcvEnumeration> getValidInputValues() {
        return (((DsFieldI_EnumerationSet<T_DS_RECORD>) dsField).getValidInputValues());
    }

}
