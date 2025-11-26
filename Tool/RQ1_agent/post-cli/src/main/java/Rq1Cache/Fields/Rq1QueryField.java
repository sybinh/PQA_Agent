/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField;
import OslcAccess.OslcLoadHint;
import OslcAccess.OslcSelection;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Interfaces.Rq1QueryI;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;
import util.EcvEnumeration;
import util.EcvListenerManager;

/**
 * Wraps a Rq1Query as Rq1Field.
 *
 * @author GUG2WI
 */
public class Rq1QueryField extends DsField<Rq1RecordInterface, List<Rq1Reference>> implements Rq1FieldI<List<Rq1Reference>>, Rq1QueryI, Rq1QueryI.ChangeListener {

    final private EcvListenerManager<Rq1ListI> listenerManager;
    final private Rq1Query query;

    public Rq1QueryField(Rq1RecordInterface parent, String fieldName, Rq1RecordType referencedRecordType) {
        super(parent, fieldName, null);

        this.listenerManager = new EcvListenerManager<>((Rq1ListI) this);

        query = new Rq1Query(referencedRecordType);
        query.addChangeListener((ChangeListener) this);
    }

    final public void setDbValueAsReferenceList(List<Rq1Reference> list) {
        assert (list != null);
        setDataSourceValue(list, Source.DATABASE);
    }

    @Override
    public List<Rq1Reference> getDataModelValue() {
        if (isNull() == true) {
            if (getParentRecord().existsInDatabase() == true) {
                // Note: loadQueryAndSubjects calls setValue() for this field.
                // So it is enought to call the method.
                setDataSourceValue(query.getReferenceList(), Source.DATABASE);
            } else {
                setDataSourceValue(new ArrayList<Rq1Reference>(0), Source.DATABASE);
            }
        }
        return (super.getDataModelValue());
    }

    @Override
    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        assert (getParentRecord().existsInDatabase() == true);

        query.loadCache(loadHint);
    }

    @Override
    public void reload() {
        query.reload();
    }

    @Override
    public List<Rq1Reference> getReferenceList(boolean loadRecords) {
        return (query.getReferenceList(loadRecords));
    }

    @Override
    public List<Rq1Reference> getReferenceList() {
        return (query.getReferenceList());
    }

    @Override
    public final void addCriteria_ValueList(String fieldname, EnumSet<? extends EcvEnumeration> allowedValues) {
        query.addCriteria_ValueList(fieldname, allowedValues);
    }

    public final void addCriteria_ValueList(Rq1AttributeName attributeName, String value1, String value2) {
        query.addCriteria_ValueList(attributeName, value1, value2);
    }

    @Override
    public final void addCriteria_Value(String fieldname, String wantedValue) {
        query.addCriteria_Value(fieldname, wantedValue);
    }

    @Override
    public final void addCriteria_Reference(String fieldname, Rq1RecordInterface referencedRecord) {
        query.addCriteria_Reference(fieldname, referencedRecord);
    }

    public final void addCriteria_Reference(Rq1AttributeName attributeName, Rq1RecordInterface referencedRecord) {
        query.addCriteria_Reference(attributeName, referencedRecord);
    }

    @Override
    public final void addCriteria_isLaterOrEqualThen(String fieldname, EcvDate testDate) {
        query.addCriteria_isLaterOrEqualThen(fieldname, testDate);
    }

    @Override
    public final OslcSelection getCriterias() {
        return query.getCriterias();
    }

    @Override
    public final void addChangeListener(ChangeListener newListener) {
        listenerManager.addChangeListener(newListener);
    }

    @Override
    public final void removeChangeListener(ChangeListener obsoletListener) {
        listenerManager.removeChangeListener(obsoletListener);
    }

    @Override
    public void addCriteria_ValueList(String fieldname, String[] allowedValues) {
        query.addCriteria_ValueList(fieldname, allowedValues);
    }

    @Override
    public void addCriteria_ValueList(String fieldname, String allowedValue1, String allowedValue2) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (allowedValue1 != null);
        assert (allowedValue2 != null);

        String allowedValues[] = new String[2];
        allowedValues[0] = allowedValue1;
        allowedValues[1] = allowedValue2;

        query.addCriteria_ValueList(fieldname, allowedValues);
    }

    @Override
    public void changed(Rq1ListI changedElement) {
        setDataSourceValue(query.getReferenceList(), Source.DATABASE);
        listenerManager.fireChange();
    }

    public void setLoadHint(OslcLoadHint loadHint) {
        assert (loadHint != null);
        query.setLoadHint(loadHint);
    }

    @Override
    public List<Rq1Reference> getReferenceListOrNull() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
