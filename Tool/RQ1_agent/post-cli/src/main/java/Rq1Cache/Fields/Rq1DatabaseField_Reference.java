/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField;
import OslcAccess.OslcDirectReferenceFieldI;
import OslcAccess.OslcDirectReferenceListField;
import OslcAccess.OslcForwardableFieldI;
import OslcAccess.OslcRecordReference;
import OslcAccess.OslcRecordTypeI;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Templates.Rq1TemplateFieldI;

/**
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_Reference extends DsField<Rq1RecordInterface, Rq1Reference> implements Rq1FieldI<Rq1Reference>, OslcDirectReferenceFieldI<Rq1RecordInterface>, Rq1TemplateFieldI,OslcForwardableFieldI {

    final private OslcDirectReferenceListField referenceField;

    private Rq1Reference lastValueFromDb;
    private Rq1Reference lastValueProvidedForDb;

    public Rq1DatabaseField_Reference(Rq1RecordInterface parent, Rq1AttributeName dbAttribute, Rq1RecordType referencedRecordType) {
        this(parent, dbAttribute.getName(), referencedRecordType);
    }

    public Rq1DatabaseField_Reference(Rq1RecordInterface parent, String dbFieldName, Rq1RecordType referencedRecordType) {
        super(parent, dbFieldName, null);
        lastValueFromDb = null;
        lastValueProvidedForDb = null;
        referenceField = new OslcDirectReferenceListField(dbFieldName, referencedRecordType);
    }

    //--------------------------------------------------------------------------
    //
    // Implementation for OslcDirectReferenceFieldI
    //
    //--------------------------------------------------------------------------
    @Override
    public OslcRecordTypeI getReferencedRecordType() {
        return referenceField.getReferencedRecordType();
    }

    @Override
    public String getOslcPropertyName() {
        return referenceField.getOslcPropertyName();
    }

    @Override
    public boolean setOslcValue(OslcRecordReference<Rq1RecordInterface> oslcRecordReference) {
        Rq1Reference rq1Reference = null;
        if (oslcRecordReference != null) {
            rq1Reference = new Rq1Reference(oslcRecordReference);
        }
        return (setDbValue(rq1Reference));
    }

    @Override
    public boolean copyOslcValue(OslcDirectReferenceFieldI<Rq1RecordInterface> field) {
        assert (field instanceof Rq1DatabaseField_Reference);
        return (setDbValue(((Rq1DatabaseField_Reference) field).provideValueAsReferenceForDb()));
    }

    private boolean setDbValue(Rq1Reference dbValue) {
        // Note: Value is null if no record is referenced.
        lastValueFromDb = dbValue;
        return (setDataSourceValue(dbValue, Source.DATABASE));
    }

    @Override
    final public void setOslcWriteSuccessfull() {
        lastValueFromDb = lastValueProvidedForDb;
        dbValueWasWrittenToDb();
    }

    @Override
    public Rq1Reference provideValueAsReferenceForDb() {
        return (lastValueProvidedForDb = getDataSourceValue());
    }

    @Override
    public Rq1Reference provideLastValueFromDbAsReferenceForDb() {
        return (lastValueFromDb);
    }

    @Override
    final public boolean isOslcWritePending() {
        return (isWritePending());
    }

    //--------------------------------------------------------------------------
    //
    // Implementation for Rq1TemplateFieldI
    //
    //--------------------------------------------------------------------------
    @Override
    public void setValueFromTemplate(String value) {
        if ((value != null) && (value.isEmpty() == false)) {
            Rq1Reference valueAsReference = Rq1Reference.buildFromStringRepresentation(value);
            setDataSourceValue(valueAsReference, Source.TEMPLATE);
            return;
        }
        setDataSourceValue(null, Source.TEMPLATE);
    }

    @Override
    final public String getValueForTemplate() {
        Rq1Reference valueAsReference = getDataSourceValue();
        if (valueAsReference != null) {
            return (valueAsReference.getStringRepresentation());
        }
        return ("");
    }

    //--------------------------------------------------------------------------
    //
    // for debugging
    //
    //--------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getParentRecord().getRecordDescription().getRecordType()).append(".").append(getFieldName()); 
        return (b.toString());
    }

    @Override
    public String getOslcValue() {
        return provideValueAsReferenceForDb().getOslcRecordReference().getRdfAbout();
    }

}
