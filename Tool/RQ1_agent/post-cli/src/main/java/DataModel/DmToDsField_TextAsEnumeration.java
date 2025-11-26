/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI;
import DataStore.DsRecordI;
import java.util.Objects;
import util.EcvEnumeration;

/**
 *
 * @author CNI83WI
 */
public class DmToDsField_TextAsEnumeration<T_DS_RECORD extends DsRecordI<? extends DsFieldI>> extends DmToDsField<String> implements DmValueFieldI_Enumeration_EagerLoad {

    final private EcvEnumeration[] validValues;
    
    public DmToDsField_TextAsEnumeration(DsFieldI<? extends DsRecordI<?>, String> dsField, String nameForUserInterface, EcvEnumeration[] validValues) {
        super(dsField, nameForUserInterface);
        assert(validValues != null);
        
        this.validValues = validValues;
    }

    @Override
    public EcvEnumeration[] getValidInputValues() {
        return (validValues);
    }

    @Override
    public void setValue(EcvEnumeration value) {
       if (value != null) {
           dsField.setDataModelValue(value.getText());
       } else {
           dsField.setDataModelValue(null);
       }
    }

    @Override
    public EcvEnumeration getValue() {
        String dsValue = dsField.getDataModelValue();
        for (EcvEnumeration v : validValues) {
            if (Objects.equals(v.getText(), dsValue)) {
                return (v);
            }
        }
        return (null);
    }
    
}