/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField;
import OslcAccess.OslcIndirectReferenceListFieldI;
import OslcAccess.OslcLoadHint;
import OslcAccess.OslcProtocolVersion;
import OslcAccess.OslcRecordTypeI;
import Rq1Cache.Fields.Interfaces.Rq1ListField_DmInterface;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1LinkInterface;
import Rq1Cache.Records.Rq1NodeInterface;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Records.Rq1SubjectInterface;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1MappedReference;
import java.util.Arrays;
import java.util.List;
import util.EcvListenerManager;

/**
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_MappedReferenceList extends DsField<Rq1RecordInterface, Rq1MappedReference[]> implements Rq1ListField_DmInterface<Rq1MappedReference>, Rq1ListI, OslcIndirectReferenceListFieldI<Rq1RecordInterface, Rq1RecordInterface> {

    final private String dbSubFieldName;
    final private Rq1RecordType mapRecordType;
    final private Rq1RecordType referencedRecordType;
    final private EcvListenerManager<Rq1ListI> listenerManager;

    public Rq1DatabaseField_MappedReferenceList(Rq1SubjectInterface parent,
            Rq1AttributeName dbFieldName, String dbSubFieldName, Rq1RecordType mapRecordType, Rq1RecordType referencedRecordType) {
        this(parent, dbFieldName.getName(), dbSubFieldName, mapRecordType, referencedRecordType);
    }

    @SuppressWarnings("unchecked")
    public Rq1DatabaseField_MappedReferenceList(Rq1SubjectInterface parent,
            String dbFieldName, String dbSubFieldName, Rq1RecordType mapRecordType, Rq1RecordType referencedRecordType) {
        super(parent, dbFieldName, null);

        assert (dbSubFieldName != null);
        assert (!dbSubFieldName.isEmpty());
        assert (mapRecordType != null);
        assert (referencedRecordType != null);

        this.dbSubFieldName = dbSubFieldName;
        this.mapRecordType = mapRecordType;
        this.referencedRecordType = referencedRecordType;
        this.listenerManager = new EcvListenerManager<>(this);
    }

    @Override
    public String getIndirectOslcPropertyName() {
        return (dbSubFieldName);
    }

    @Override
    public OslcRecordTypeI getIndirectReferencedRecordType() {
        return (referencedRecordType);
    }

    @Override
    public String getOslcPropertyName() {
        return (super.getFieldName());
    }

    @Override
    final public OslcRecordTypeI getReferencedRecordType() {
        return (mapRecordType);
    }

    @Override
    final public Rq1MappedReference[] getDataModelValue() {
        if (isNull() == true) {
            if (getParentRecord().existsInDatabase() == true) {
                // Note: loadMappedReferenceListAndRecords calls setValue() for this field.
                // So it is enought to call the method.
                Rq1Client.client.loadMappedReferenceList(this, true);
                assert (super.getDataModelValue() != null) : toString();
            } else {
                setDataSourceValue(new Rq1MappedReference[0], Source.DATABASE);
            }
        }
        return (super.getDataModelValue());
    }

    @Override
    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        assert (getParentRecord().existsInDatabase() == true);

        Rq1Client.client.loadMappedReferenceList(this, loadHint);
    }

    @Override
    public void reload() {
        // Note: loadReferenceAndSubjects calls setValue() for this field.
        // So it is enought to call the method.
        Rq1Client.client.loadMappedReferenceList(this, true);
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
    final public void addElement(Rq1MappedReference newElement) {
        Rq1MappedReference[] oldArray = super.getDataModelValue();
        Rq1MappedReference[] newArray = Arrays.copyOf(oldArray, oldArray.length + 1);
        newArray[newArray.length - 1] = newElement;
        setDataModelValue(newArray);
        listenerManager.fireChange();
    }

    @Override
    final public boolean removeElement(Rq1MappedReference elementToRemove) {

        Rq1MappedReference[] oldArray = super.getDataModelValue();
        //        
        //Find element to remove
        //        
        int indexOfReferenceToRemove;
        for (indexOfReferenceToRemove = 0; indexOfReferenceToRemove < oldArray.length; indexOfReferenceToRemove++) {
            if (oldArray[indexOfReferenceToRemove].equals(elementToRemove)) {
                break;
            }
        }
        if (indexOfReferenceToRemove >= oldArray.length) {
            //
            // Not found. -> No change.
            //
            return (false);
        }

        //
        // Create new array
        //
        Rq1MappedReference[] newArray = new Rq1MappedReference[oldArray.length - 1];
        for (int oldIndex = 0, newIndex = 0; oldIndex < oldArray.length; oldIndex++) {
            if (indexOfReferenceToRemove != oldIndex) {
                newArray[newIndex] = oldArray[oldIndex];
                newIndex++;
            }
        }
        setDataModelValue(newArray);
        listenerManager.fireChange();

        return (true);
    }

    @Override
    public OslcProtocolVersion getOslcProtocolVersion() {
        return (OslcProtocolVersion.OSLC_20);
    }

    @Override
    public boolean setOslcValue(List<Entry<Rq1RecordInterface, Rq1RecordInterface>> recordReferenceList) {
        assert (recordReferenceList != null);
        // TODO: gug2wi - Reuse old list entries to prevent superflous changes.
        // TODO: gug2wi - Add trigger for listeners

        Rq1MappedReference[] newValue = new Rq1MappedReference[recordReferenceList.size()];
        int index = 0;
        for (Entry<Rq1RecordInterface, Rq1RecordInterface> entry : recordReferenceList) {
            if ((entry.getIndirectReference().getRecord() != null) && (entry.getDirectReference().getRecord()) != null) {
                newValue[index] = new Rq1MappedReference(
                        (Rq1NodeInterface) entry.getIndirectReference().getRecord(),
                        (Rq1LinkInterface) entry.getDirectReference().getRecord());
            } else {
                newValue[index] = new Rq1MappedReference(
                        entry.getIndirectReference().getRecordIdentifier(),
                        entry.getDirectReference().getRecordIdentifier());
            }
            index++;
        }

        super.setDataSourceValue(newValue, Source.DATABASE);

        return (true);
    }

    @Override
    public boolean copyOslcValue(OslcIndirectReferenceListFieldI<Rq1RecordInterface, Rq1RecordInterface> field) {
        assert (field instanceof Rq1DatabaseField_MappedReferenceList);
        Rq1MappedReference[] source = ((Rq1DatabaseField_MappedReferenceList) field).superGetDataModelValue();
        Rq1MappedReference[] copy = null;
        if (source != null) {
            Arrays.copyOf(source, source.length);
        }
        setDataSourceValue(copy, Source.DATABASE);
        return (false);
    }

    private Rq1MappedReference[] superGetDataModelValue() {
        return (super.getDataModelValue());
    }
}
