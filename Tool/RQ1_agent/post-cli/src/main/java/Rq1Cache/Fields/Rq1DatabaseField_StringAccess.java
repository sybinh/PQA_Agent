/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField;
import OslcAccess.OslcStringFieldI;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Templates.Rq1TemplateFieldI;

/**
 * Use to identify and define the interface for a field simple, plain field.
 *
 * Interface for setting and getting DB values for simple plain fields (e.g.
 * text, XML, Date).
 *
 * @author GUG2WI
 * @param <T>
 */
abstract public class Rq1DatabaseField_StringAccess<T> extends DsField<Rq1RecordInterface, T> implements OslcStringFieldI, Rq1TemplateFieldI {

    final private String dbFieldName;
    private String lastValueReceivedFromDb;
    private String lastValueProvidedForDb;

    protected Rq1DatabaseField_StringAccess(Rq1RecordInterface parent, String dbFieldName, T initValue) {
        this(parent, dbFieldName, dbFieldName, initValue);
    }

    protected Rq1DatabaseField_StringAccess(Rq1RecordInterface parent, String fieldName, String dbFieldName, T initValue) {
        super(parent, fieldName, initValue);
        assert (dbFieldName != null);
        assert (dbFieldName.isEmpty() == false) : fieldName;

        lastValueReceivedFromDb = null;
        lastValueProvidedForDb = null;
        this.dbFieldName = dbFieldName;
    }

    @Override
    final public String getOslcPropertyName() {
        return (dbFieldName);
    }

    @Override
    final public boolean setOslcValue(String dbValue) {
        assert (dbValue != null) : dbFieldName;

        //
        // Do not bother the sub fields, if the value is unchanged.
        //
        if ((lastValueReceivedFromDb != null) && (lastValueReceivedFromDb.equals(dbValue) == true) && (isChangedByDataModel() == false)) {
            return (false);
        }

        boolean result = setOslcValue_Internal(dbValue, Source.DATABASE);
        lastValueReceivedFromDb = dbValue;
        return (result);
    }

    @Override
    final public void setValueFromTemplate(String value) {
        setOslcValue_Internal(value, Source.TEMPLATE);
    }

    @Override
    final public String getValueForTemplate() {
        String value = getOslcValue_Internal();
        return (value != null ? value : "");
    }

    abstract protected boolean setOslcValue_Internal(String dbValue, Source source);

    @Override
    public boolean copyOslcValue(OslcStringFieldI field) {
        assert (field instanceof Rq1DatabaseField_StringAccess);
        return (setOslcValue(((Rq1DatabaseField_StringAccess) field).lastValueReceivedFromDb));
    }

    @Override
    final public String getOslcValue() {
        String oslcValue = getOslcValue_Internal();
        //
        // Prevent null values on DATABASE interface
        //
        if (oslcValue == null) {
            lastValueProvidedForDb = "";
        } else {
            lastValueProvidedForDb = oslcValue.trim(); // Trim added, because RQ1 database does the trim internal. If we do not trim here, we get an overwrite exception in the next write.

        }
        return (lastValueProvidedForDb);
    }

    abstract protected String getOslcValue_Internal();

    @Override
    final public String provideLastValueFromDbAsStringForDb() {
        return (lastValueReceivedFromDb);
    }

    @Override
    public boolean equalDbValues(String value1, String value2) {
        return (value1.equals(value2));
    }

    @Override
    final public boolean isOslcWritePending() {
        return (isWritePending());
    }

    @Override
    final public void setOslcWriteSuccessfull() {
        lastValueReceivedFromDb = lastValueProvidedForDb;
        dbValueWasWrittenToDb();
    }

}
