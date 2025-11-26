/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsFieldI_EnumerationSet;
import DataStore.DsManager_EnumerationSet;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_EnumerationSet;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1MetadataChoiceList;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvEnumeration;
import util.EcvEnumerationSet;

/**
 * Represents a field of an RQ1 record which exists in this form in the RQ1
 * database and holds a text value out of a defined list of values.
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_EnumerationSet extends Rq1DatabaseField_StringAccess<EcvEnumerationSet> implements DsFieldI_EnumerationSet<Rq1RecordInterface>, Rq1FieldI_EnumerationSet {

    final private DsManager_EnumerationSet dsManager;

    public Rq1DatabaseField_EnumerationSet(Rq1RecordInterface parent, Rq1AttributeName fieldName, EcvEnumeration[] allowedValues) {
        super(parent, fieldName.getName(), null);
        assert (allowedValues != null);
        dsManager = new DsManager_EnumerationSet(this, new EcvEnumerationSet(allowedValues));
    }

    /**
     * Create an enumeration field that takes the valid values from the RQ1 meta
     * data deployed with IPE.
     *
     * @param parent Parent record. The record type of the parent record is
     * taken as key to search for valid input values in the RQ1 meta data.
     * @param fieldName The name of the field in the RQ1 database. This name is
     * also taken as key to search for valid input values in the RQ1 meta data.
     * @param criteria Criteria to select the list within the meta data record.
     */
    public Rq1DatabaseField_EnumerationSet(Rq1RecordInterface parent, Rq1AttributeName fieldName, String criteria) {
        super(parent, fieldName.getName(), null);
        assert (criteria != null);

        List<String> listOfValidInputValues = Rq1MetadataChoiceList.getValidInputValues(parent.getRecordDescription().getRecordType(), fieldName, criteria);

        assert (listOfValidInputValues != null) : parent.getRecordDescription().getRecordType().toString() + ":" + fieldName.getName();

        dsManager = new DsManager_EnumerationSet(this, new EcvEnumerationSet(listOfValidInputValues));
    }

    public Rq1DatabaseField_EnumerationSet acceptInvalidValuesInDatastore() {
        dsManager.acceptInvalidValuesInDatastore();
        return (this);
    }

    @Override
    final public List<EcvEnumeration> getValidInputValues() {
        return (dsManager.getValidInputValues());
    }

    @Override
    public synchronized void setDataModelValue(EcvEnumerationSet newValue) {
        if (newValue != null) {
            if (newValue.equals(getDataModelValue()) == true) {
                return;
            }
        } else if (getDataModelValue() == null) {
            return;
        }
        super.setDataModelValue(newValue);
    }

    @Override
    public boolean setOslcValue_Internal(String dbValue, Source source) {
        assert (dbValue != null);
        assert (source != null);

        return (setDataSourceValue(dsManager.decodeValueFromDatastore(dbValue), source));
    }

    @Override
    protected String getOslcValue_Internal() {

        EcvEnumerationSet dmValue = getDataModelValue();
        if (dmValue != null) {
            return (dsManager.encodeValueForDatastore(dmValue));
        } else {
            Logger.getLogger(this.getClass().getCanonicalName()).log(Level.WARNING, "Value == null for {0}", this.getFieldName());
            return ("");
        }
    }

}
