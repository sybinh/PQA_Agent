/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmToDsField;
import DataModel.DmValueFieldI_EnumerationSet;
import DataStore.DsManager_EnumerationSet;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import util.EcvEnumerationSet;

/**
 * Implements a field that is based on a RQ1 text field and acts as enumeration
 * field on the data model level.
 *
 * @author gug2wi
 */
public abstract class DmRq1Field_TextAsEnumerationSet extends DmToDsField<String> implements DmValueFieldI_EnumerationSet {

    final private DsManager_EnumerationSet dsManager;

    public DmRq1Field_TextAsEnumerationSet(Rq1FieldI_Text rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
        dsManager = new DsManager_EnumerationSet(rq1Field, new EcvEnumerationSet());
        dsManager.acceptInvalidValuesInDatastore();
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
    public EcvEnumerationSet getValue() {
        String value = dsField.getDataModelValue();
        if (value != null) {
            return (dsManager.decodeValueFromDatastore(value));
        } else {
            return (new EcvEnumerationSet());
        }
    }

    @Override
    public void setValue(EcvEnumerationSet value) {
        if (value != null) {
            dsField.setDataModelValue(dsManager.encodeValueForDatastore(value));
        } else {
            dsField.setDataModelValue(null);
        }
    }

}
