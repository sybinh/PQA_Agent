/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsManager_Enumeration;
import DataStore.DsFieldI_Enumeration;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Enumeration;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1MetadataChoiceList;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvEnumeration;
import util.EcvEnumerationFieldI;
import util.EcvEnumerationValue;

/**
 * Represents a field of an RQ1 record which exists in this form in the RQ1
 * database and holds a text value out of a defined list of values.
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_Enumeration extends Rq1DatabaseField_StringAccess<EcvEnumeration> implements DsFieldI_Enumeration<Rq1RecordInterface>, EcvEnumerationFieldI, Rq1FieldI_Enumeration {

    final private DsManager_Enumeration enumerationHandler;

    /**
     * Create an enumeration field that takes the valid values from the
     * parameters provided to the constructor.
     */
    public <T extends Enum<T> & EcvEnumeration> Rq1DatabaseField_Enumeration(Rq1RecordInterface parent, Rq1AttributeName attribute, T[] validValues, T initValue) {
        this(parent, attribute.getName(), validValues, initValue);
    }

    /**
     * Create an enumeration field that takes the valid values from the
     * parameters provided to the constructor.
     */
    public <T extends Enum<T> & EcvEnumeration> Rq1DatabaseField_Enumeration(Rq1RecordInterface parent, String dbFieldName, T[] validValues, T initValue) {
        super(parent, dbFieldName, initValue);
        assert (initValue != null);
        assert (validValues != null);
        assert (validValues.length > 0);

        enumerationHandler = new DsManager_Enumeration(this, validValues);
    }

    /**
     * Create an enumeration field that takes the valid values from the
     * parameters provided to the constructor.
     */
    public <T extends Enum<T> & EcvEnumeration> Rq1DatabaseField_Enumeration(Rq1RecordInterface parent, Rq1AttributeName attribute, T[] validValues) {
        this(parent, attribute.getName(), validValues);
    }

    /**
     * Create an enumeration field that takes the valid values from the
     * parameters provided to the constructor.
     */
    public <T extends Enum<T> & EcvEnumeration> Rq1DatabaseField_Enumeration(Rq1RecordInterface parent, String dbFieldName, T[] validValues) {
        super(parent, dbFieldName, validValues[0]);
        assert (validValues != null);
        assert (validValues.length > 0);

        enumerationHandler = new DsManager_Enumeration(this, validValues);
    }

    /**
     * Create an enumeration field that takes the valid values from the RQ1 meta
     * data deployed with IPE.
     *
     * @param parent Parent record. The record type of the parent record is
     * taken as key to search for valid input values in the RQ1 meta data.
     * @param fieldName The name of the field in the RQ1 database. This name *
     * is also taken as key to search for valid input values in the RQ1 meta
     * data.
     * @param criteria Criteria to select the list within the meta data record.
     * @param initValue The initial value that shall be used for the field.
     */
    public Rq1DatabaseField_Enumeration(Rq1RecordInterface parent, Rq1AttributeName fieldName, String criteria, String initValue) {
        super(parent, fieldName.getName(), null);
        assert (criteria != null);

        //
        // Load values from RQ1 meta data
        //
        List<String> listOfValidInputValues = Rq1MetadataChoiceList.getValidInputValues(parent.getRecordDescription().getRecordType(), fieldName, criteria);
        assert (listOfValidInputValues != null) : parent.getRecordDescription().getRecordType().toString() + ":" + fieldName.getName();

        //
        // Add initValue, if not already in the value list
        //
        if (initValue != null) {
            if (listOfValidInputValues.contains(initValue) == false) {
                listOfValidInputValues.add(0, initValue);
            }
        }

        //
        // Convert string list into enumeration array
        //
        EcvEnumeration[] arrayOfValidInputValues = EcvEnumerationValue.createArray(listOfValidInputValues);

        //
        // Set init value
        //
        if (initValue != null) {
            for (EcvEnumeration value : arrayOfValidInputValues) {
                if (value.getText().equals(initValue)) {
                    setInitValue(value);
                }
            }
        }

        enumerationHandler = new DsManager_Enumeration(this, arrayOfValidInputValues);
    }

    public Rq1DatabaseField_Enumeration acceptInvalidValuesInDatabase() {
        enumerationHandler.acceptInvalidValuesInDatabase();
        return (this);
    }

    @Override
    public boolean setOslcValue_Internal(String dbValue, Source source) {
        assert (dbValue != null);
        assert (source != null);

        return (setDataSourceValue(enumerationHandler.convertDbValue(dbValue), source));
    }

    @Override
    final public EcvEnumeration[] getValidInputValues() {
        return (enumerationHandler.getValidInputValues());
    }

    @Override
    protected String getOslcValue_Internal() {
        // Value might be null, Check ECVTOOL-2669
        if (getValue() != null) {
            return (getValue().getText());
        } else {
            Logger.getLogger(this.getClass().getCanonicalName()).log(Level.WARNING, "Value == null for {0}", this.getFieldName());
            return ("");
        }
    }

    @Override
    public void setValue(EcvEnumeration v) {
        super.setDataModelValue(v);
    }

    @Override
    public EcvEnumeration getValue() {
        return (super.getDataModelValue());
    }
}
