/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI;
import DataStore.DsFieldI_Enumeration;
import DataStore.DsRecordI;
import util.EcvEnumeration;
import util.EcvEnumerationFieldI;

/**
 *
 * @author gug2wi
 * @param <T_DS_RECORD> Type of data source parent record.
 */
public class DmToDsField_Enumeration<T_DS_RECORD extends DsRecordI<? extends DsFieldI>> extends DmToDsValueField<T_DS_RECORD, EcvEnumeration> implements DmValueFieldI_Enumeration_EagerLoad {

    public DmToDsField_Enumeration(DsFieldI_Enumeration<T_DS_RECORD> dsField, String nameForUserInterface) {
        super(dsField, nameForUserInterface);
        assert (dsField instanceof EcvEnumerationFieldI);
        // temporary standard widthInCharacter. Currently (04/2019) the only usage for this is UserTab,
        // this will later be replaced by prorietary standardWidths for different fields.
        // remove this if it it's necessity is not given any more.
        setWidthInCharacter(28);
    }

    public DmToDsField_Enumeration(DsFieldI_Enumeration<T_DS_RECORD> dsField, String nameForUserInterface, int widthInCharacter) {
        super(dsField, nameForUserInterface);
        assert (dsField instanceof EcvEnumerationFieldI);
        setWidthInCharacter(widthInCharacter);
    }

    /**
     * Sets the value of the field to the enumeration that fits to the provided
     * string.
     *
     * @param newValue
     * @return The matching EvcEnumeration, if the given string matches a valid
     * value. null, if no matching value was found.
     */
    public EcvEnumeration setValueAsText(String newValue) {
        for (EcvEnumeration validValue : getValidInputValues()) {
            if (validValue.getText().equals(newValue)) {
                setValue(validValue);
                return (validValue);
            }
        }
        return (null);
    }

    @Override
    public EcvEnumeration[] getValidInputValues() {
        return (((EcvEnumerationFieldI) dsField).getValidInputValues());
    }

    @Override
    protected boolean switchToAlternativeField(EcvEnumeration value) {
        if (value == null) {
            return (true);
        } else if (value.getText().isEmpty()) {
            return (true);
        } else {
            return (false);
        }

    }

}
