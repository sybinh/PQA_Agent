/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Interfaces.Rq1QueryI;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1Client;
import OslcAccess.OslcSelection;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Rq1RecordDescription.FixedRecordValue;
import Rq1Cache.Rq1RecordType;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;
import util.EcvEnumeration;
import util.EcvListenerManager;

/**
 *
 * @author GUG2WI
 */
public class Rq1Query implements Rq1QueryI {

    final private EcvListenerManager<Rq1ListI> listenerManager;
    final private Rq1QueryDescription selectionManager;
    private List<Rq1Reference> lastResult = null;
    private OslcLoadHint loadHint = null;

    public Rq1Query() {
        this.listenerManager = new EcvListenerManager<>((Rq1ListI) this);
        this.selectionManager = new Rq1QueryDescription();
    }

    public Rq1Query(Rq1RecordType referencedRecordType) {
        assert (referencedRecordType != null);

        this.listenerManager = new EcvListenerManager<>((Rq1ListI) this);
        this.selectionManager = new Rq1QueryDescription(referencedRecordType);
    }

    public void setLoadHint(OslcLoadHint loadHint) {
        assert (loadHint != null);
        this.loadHint = loadHint;
    }

    @Override
    public List<Rq1Reference> getReferenceList(boolean loadRecords) {
        if (lastResult == null) {
            if (loadHint == null) {
                lastResult = Rq1Client.client.loadRecordsAsList(selectionManager.getOslcSelection(), true);
            } else {
                lastResult = Rq1Client.client.loadRecordsAsList(selectionManager.getOslcSelection(), loadHint);
            }
        }
        return (lastResult);
    }

    @Override
    public List<Rq1Reference> getReferenceList() {
        return (getReferenceList(true));
    }
    
    @Override
    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        lastResult = Rq1Client.client.loadRecordsAsList(selectionManager.getOslcSelection(), loadHint);
    }

    //--------------------------------------------------------------------------
    //
    // Value Lists
    //
    //--------------------------------------------------------------------------
    @Override
    public void addCriteria_ValueList(String fieldname, String[] allowedValues) {
        selectionManager.addCriteria(fieldname, allowedValues);
    }

    @Override
    public void addCriteria_ValueList(String fieldname, String allowedValue1, String allowedValue2) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (allowedValue1 != null);
        assert (allowedValue1.isEmpty() == false);
        assert (allowedValue2 != null);
        assert (allowedValue2.isEmpty() == false);

        String allowedValues[] = new String[2];

        allowedValues[0] = allowedValue1;
        allowedValues[1] = allowedValue2;

        addCriteria_ValueList(fieldname, allowedValues);
    }

    @Override
    final public void addCriteria_ValueList(String fieldname, EnumSet<? extends EcvEnumeration> allowedValues) {
        selectionManager.addCriteria(fieldname, allowedValues.toArray(new EcvEnumeration[0]));
    }

    public void addCriteria_ValueList(Rq1AttributeName attributeName, String value1, String value2) {
        assert (attributeName != null);
        addCriteria_ValueList(attributeName.getName(), value1, value2);
    }

    public void addCriteria_ValueList(Rq1AttributeName attributeName, EnumSet<? extends EcvEnumeration> allowedValues) {
        addCriteria_ValueList(attributeName.getName(), allowedValues);
    }

    public void addCriteria_ValueList(Rq1AttributeName attribute1, Rq1AttributeName attribute2, String value1, String value2) {
        String values[] = new String[2];
        values[0] = value1;
        values[1] = value2;
        selectionManager.addCriteria(attribute1.getName() + "." + attribute2.getName(), values);
    }

    //--------------------------------------------------------------------------
    //
    // Values
    //
    //--------------------------------------------------------------------------
    @Override
    final public void addCriteria_Value(String fieldname, String wantedValue) {
        selectionManager.addCriteria(fieldname, wantedValue);
    }

    final public void addCriteria_Value(Rq1AttributeName attribute1, Rq1AttributeName attribute2, String wantedValue) {
        assert (attribute1 != null);
        assert (attribute2 != null);

        addCriteria_Value(attribute1.getName() + "." + attribute2.getName(), wantedValue);
    }

    final public void addCriteria_Value(Rq1AttributeName attribute1, Rq1AttributeName attribute2, Rq1AttributeName attribute3, String wantedValue) {
        assert (attribute1 != null);
        assert (attribute2 != null);
        assert (attribute3 != null);

        addCriteria_Value(attribute1.getName() + "." + attribute2.getName() + "." + attribute3.getName(), wantedValue);
    }

    final public void addCriteria_Value(Rq1AttributeName attribute1, Rq1AttributeName attribute2, Rq1AttributeName attribute3, Rq1AttributeName attribute4, String wantedValue) {
        assert (attribute1 != null);
        assert (attribute2 != null);
        assert (attribute3 != null);
        assert (attribute4 != null);

        addCriteria_Value(attribute1.getName() + "." + attribute2.getName() + "." + attribute3.getName() + "." + attribute4.getName(), wantedValue);
    }

    final public void addCriteria_Value(Rq1AttributeName attribute1, Rq1AttributeName attribute2, Rq1AttributeName attribute3,
            Rq1AttributeName attribute4, Rq1AttributeName attribute5, String wantedValue) {
        assert (attribute1 != null);
        assert (attribute2 != null);
        assert (attribute3 != null);
        assert (attribute4 != null);
        assert (attribute5 != null);

        addCriteria_Value(attribute1.getName() + "." + attribute2.getName() + "." + attribute3.getName() + "." + attribute4.getName() + "." + attribute5.getName(), wantedValue);
    }

    final public void addCriteria_Value(Rq1AttributeName attribute1, Rq1AttributeName attribute2, Rq1AttributeName attribute3,
            Rq1AttributeName attribute4, Rq1AttributeName attribute5, Rq1AttributeName attribute6, String wantedValue) {
        assert (attribute1 != null);
        assert (attribute2 != null);
        assert (attribute3 != null);
        assert (attribute4 != null);
        assert (attribute5 != null);
        assert (attribute6 != null);

        addCriteria_Value(attribute1.getName() + "." + attribute2.getName() + "." + attribute3.getName() + "." + attribute4.getName() + "." + attribute5.getName() + "." + attribute6.getName(), wantedValue);
    }

    @Override
    final public void addCriteria_Reference(String fieldname, Rq1RecordInterface referencedRecord) {
        selectionManager.addCriteria(fieldname, referencedRecord);
    }

    final public void addCriteria_Reference(Rq1AttributeName fieldname, Rq1RecordInterface referencedRecord) {
        selectionManager.addCriteria(fieldname, referencedRecord);
    }

    public void addCriteria_Reference(Rq1AttributeName fieldname1, Rq1AttributeName fieldname2, Rq1RecordInterface referencedRecord) {
        selectionManager.addCriteria(fieldname1, fieldname2, referencedRecord);
    }

    @Override
    final public void addCriteria_isLaterOrEqualThen(String fieldname, EcvDate testDate) {
        selectionManager.addCriteria_isLaterOrEqualThen(fieldname, testDate);
    }

    final public void addCriteria_isLaterOrEqualThen(Rq1AttributeName attribute, EcvDate testDate) {
        selectionManager.addCriteria_isLaterOrEqualThen(attribute.getName(), testDate);
    }

    public void addCriteria_FixedValues(FixedRecordValue[] fixedRecordValues) {
        selectionManager.addCriteria(fixedRecordValues);
    }

    @Override
    final public OslcSelection getCriterias() {
        return (selectionManager.getOslcSelection());
    }

    @Override
    public void reload() {
        boolean loadRecords = (lastResult == null);
        lastResult = Rq1Client.client.loadRecordsAsList(selectionManager.getOslcSelection(), true);
        listenerManager.fireChange();
    }

    @Override
    final public void addChangeListener(ChangeListener newListener) {
        listenerManager.addChangeListener(newListener);
    }

    @Override
    final public void removeChangeListener(ChangeListener obsoletListener) {
        listenerManager.removeChangeListener(obsoletListener);
    }

    @Override
    public List<Rq1Reference> getReferenceListOrNull() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
