/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmToDsField;
import DataModel.DmValueFieldI_Enumeration_EagerLoad;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Data.Enumerations.Category_YesNo;
import util.EcvEnumeration;

/**
 * Field for work around introduced with ECVTOOL-5517.
 *
 * @author GUG2WI
 */
public class DmRq1Field_ECVTOOL_5517_CategoryExhaust extends DmToDsField<EcvEnumeration> implements DmValueFieldI_Enumeration_EagerLoad {

    final private Rq1DatabaseField_Enumeration rq1Field;
    final private Rq1XmlSubField_Enumeration tagField;

    public DmRq1Field_ECVTOOL_5517_CategoryExhaust(Rq1DatabaseField_Enumeration rq1Field, Rq1XmlSubField_Enumeration tagField, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);

        assert (rq1Field != null);
        assert (tagField != null);

        this.rq1Field = rq1Field;
        this.tagField = tagField;
    }

    @Override
    public EcvEnumeration getValue() {
        EcvEnumeration value = rq1Field.getDataModelValue();
        if ((value == null) || (value.getText().isEmpty())) {
            value = tagField.getDataModelValue();
        }
        return (value);
    }

    @Override
    public void setValue(EcvEnumeration value) {
        rq1Field.setDataModelValue(value);
        switch (value.getText()) {
            case "Y":
            case "Yes":
                tagField.setDataModelValue(Category_YesNo.Y);
                break;
            case "N":
            case "No":
                tagField.setDataModelValue(Category_YesNo.N);
                break;
            default:
                tagField.setDataModelValue(Category_YesNo.EMPTY);
                break;
        }

    }

    @Override
    public EcvEnumeration[] getValidInputValues() {
        return (rq1Field.getValidInputValues());
    }

    public EcvEnumeration setValueAsText(String newValue) {
        for (EcvEnumeration validValue : getValidInputValues()) {
            if (validValue.getText().equals(newValue)) {
                setValue(validValue);
                return (validValue);
            }
        }
        return (null);
    }

}
