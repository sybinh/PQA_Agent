/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField;
import OslcAccess.OslcDirectReferenceListField;
import OslcAccess.OslcDirectReferenceListFieldI;
import OslcAccess.OslcDirectReferenceListWriteableFieldI;
import OslcAccess.OslcLoadHint;
import OslcAccess.OslcProtocolVersion;
import OslcAccess.OslcRecordReference;
import OslcAccess.OslcRecordTypeI;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1ReferenceList_Writeable;
import java.util.ArrayList;
import java.util.List;
import util.EcvListenerManager;

/**
 *
 * @author hfi5wi
 */
public class Rq1DatabaseField_ReferenceList_Writeable 
        extends DsField<Rq1RecordInterface, Rq1ReferenceList_Writeable<Rq1Reference>> 
        implements Rq1FieldI<Rq1ReferenceList_Writeable<Rq1Reference>>, Rq1ListI, 
            OslcDirectReferenceListFieldI<Rq1RecordInterface>, 
            OslcDirectReferenceListWriteableFieldI<Rq1RecordInterface> {
    
    final private OslcDirectReferenceListField referenceField;
    final private EcvListenerManager<Rq1ListI> listenerManager;
    final private OslcProtocolVersion oslcProtocolVersion;
    private List<Rq1Reference> lastValuesFromDb;
    private List<Rq1Reference> lastValuesProvidedForDb;
    
    public Rq1DatabaseField_ReferenceList_Writeable(Rq1RecordInterface parent, Rq1AttributeName dbAttribute, Rq1RecordType referencedRecordType) {
        this(parent, dbAttribute.getName(), referencedRecordType);
    }

    public Rq1DatabaseField_ReferenceList_Writeable(Rq1RecordInterface parent, String dbFieldName, Rq1RecordType referencedRecordType) {
        this(parent, dbFieldName, referencedRecordType, OslcProtocolVersion.OSLC_20);
    }
    
    public Rq1DatabaseField_ReferenceList_Writeable(Rq1RecordInterface parent, String dbFieldName, Rq1RecordType referencedRecordType, OslcProtocolVersion oslcProtocolVersion) {
        super(parent, dbFieldName, null);

        assert (referencedRecordType != null);
        assert (oslcProtocolVersion != null);

        this.listenerManager = new EcvListenerManager<>(this);
        this.oslcProtocolVersion = oslcProtocolVersion;
        this.referenceField = new OslcDirectReferenceListField(dbFieldName, referencedRecordType);
        this.lastValuesFromDb = null;
        this.lastValuesProvidedForDb = null;
    }
    
    @Override
    public OslcRecordTypeI getReferencedRecordType() {
        return referenceField.getReferencedRecordType();
    }

    @Override
    public String getOslcPropertyName() {
        return referenceField.getOslcPropertyName();
    }

    @Override
    public OslcProtocolVersion getOslcProtocolVersion() {
        return oslcProtocolVersion;
    }

    @Override
    public boolean setOslcValue(List<OslcRecordReference<Rq1RecordInterface>> recordReferenceList) {
        assert (recordReferenceList != null);

        List<Rq1Reference> list = new ArrayList<>();
        for (OslcRecordReference<Rq1RecordInterface> recordReference : recordReferenceList) {
            list.add(new Rq1Reference(recordReference));
        }
        setDbValue(list);
        return (false);
    }

    @Override
    public boolean copyOslcValue(OslcDirectReferenceListFieldI<Rq1RecordInterface> field) {
        assert (field instanceof Rq1DatabaseField_ReferenceList);
        setDbValue(((Rq1DatabaseField_ReferenceList_Writeable) field).provideValueAsReferenceListForDb());
        return (false);
    }
    
    private boolean setDbValue(List<Rq1Reference> dbValues) {
        lastValuesFromDb = dbValues;
        return (setDataSourceValue(new Rq1ReferenceList_Writeable<>(dbValues), Source.DATABASE));
    }
    
    @Override
    final public Rq1ReferenceList_Writeable<Rq1Reference> getDataModelValue() {
        if (isNull() == true) {
            if (getParentRecord().existsInDatabase() == true) {
                // Note: loadReferenceField calls setValue() for this field.
                // So it is enought to call the method.
                Rq1Client.client.loadReferenceField(this, true, oslcProtocolVersion);
                assert (super.getDataModelValue() != null) : toString();
            } else {
                setDbValue(new ArrayList<>());
            }
        }
        return (super.getDataModelValue());
    }

    @Override
    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        assert (getParentRecord().existsInDatabase() == true);

        Rq1Client.client.loadReferenceField(this, loadHint, oslcProtocolVersion);
    }

    @Override
    public void reload() {
        // Note: loadReferenceField calls setValue() for this field.
        // So it is enought to call the method.
        switch (oslcProtocolVersion) {
            case OSLC_10:
                Rq1Client.client.loadReferenceField(this, true, oslcProtocolVersion);
                break;
            case OSLC_20:
                Rq1Client.client.loadReferenceField(this, true, oslcProtocolVersion);
                break;
            default:
                throw (new Error("Unknown OslcProtocolVersion:" + oslcProtocolVersion.toString()));
        }

        listenerManager.fireChange();
    }

    @Override
    public void addChangeListener(ChangeListener newListener) {
        listenerManager.addChangeListener(newListener);
    }

    @Override
    public void removeChangeListener(ChangeListener obsoletListener) {
        listenerManager.removeChangeListener(obsoletListener);
    }
    
    @Override
    public boolean isOslcWritePending() {
        return (isWritePending());
    }

    @Override
    public void setOslcWriteSuccessfull() {
        setDbValue(lastValuesProvidedForDb);
        dbValueWasWrittenToDb();
    }
    
    @Override
    public List<Rq1Reference> provideLastValuesFromDbAsReferenceListForDb() {
        return (lastValuesFromDb);
    }

    @Override
    public List<Rq1Reference> provideValueAsReferenceListForDb() {
        return (lastValuesProvidedForDb = getDataSourceValue().getElementList());
    }
    
    @Override
    public List<Rq1Reference> getValuesToDeleteAtSave() {
        return (getDataSourceValue().getTempElementsToDelete());
    }

    @Override
    public List<Rq1Reference> getValuesToAddAtSave() {
        return (getDataSourceValue().getTempElementsToAdd());
    }

}
