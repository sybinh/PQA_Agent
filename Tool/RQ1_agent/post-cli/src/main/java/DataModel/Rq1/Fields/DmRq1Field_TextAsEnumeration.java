/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmValueFieldI_Enumeration_LazyLoad;
import DataModel.DmToDsField;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Records.Rq1RecordInterface;
import util.EcvEnumeration;
import util.EcvEnumerationInstance;

/**
 * Implements a field that is based on a RQ1 text field and acts as enumeration
 * field on the data model level.
 *
 * @author gug2wi
 */
public abstract class DmRq1Field_TextAsEnumeration extends DmToDsField<Rq1RecordInterface, String> implements DmValueFieldI_Enumeration_LazyLoad {

    public DmRq1Field_TextAsEnumeration(DmElementI parent, Rq1FieldI_Text rq1Field, String nameForUserInterface) {
        super(parent, rq1Field, nameForUserInterface);
    }

    @Override
    public String getValueAsText() {
        String value = dsField.getDataModelValue();
        if (value != null) {
            return (value);
        } else {
            return ("");
        }
    }

    @Override
    public EcvEnumeration getValue() {
        String value = dsField.getDataModelValue();
        if (value != null) {
            return (new EcvEnumerationInstance(value, 0));
        } else {
            return (null);
        }
    }

    @Override
    public void setValue(EcvEnumeration value) {
        if (value != null) {
            dsField.setDataModelValue(value.getText());
        } else {
            dsField.setDataModelValue(null);
        }

    }

}
