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
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Records.Rq1NodeInterface;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Types.Rq1Reference;
import java.util.ArrayList;
import java.util.List;
import util.EcvListenerManager;

/**
 *
 * @author gug2wi
 */
public class Rq1ReferenceListField_FilterByClass extends DsField<Rq1RecordInterface, List<Rq1Reference>> implements Rq1FieldI<List<Rq1Reference>>, Rq1ListI, Rq1ListI.ChangeListener {

    final private DsField<Rq1RecordInterface, List<Rq1Reference>> sourceField_Field;

    final private Class wantedClass;
    final private EcvListenerManager<Rq1ListI> listenerManager;

    @SuppressWarnings({"unchecked", "LeakingThisInConstructor"})
    public <R extends DsField<Rq1RecordInterface, List<Rq1Reference>> & Rq1ListI> Rq1ReferenceListField_FilterByClass(Rq1NodeInterface parent, R sourceField, Class wantedClass) {
        super(parent, sourceField.getFieldName() + "." + wantedClass.getName(), null);

        this.sourceField_Field = sourceField;
//        this.sourceField_List = sourceField;
        this.wantedClass = wantedClass;
        this.listenerManager = new EcvListenerManager(this);

        ((Rq1ListI) sourceField_Field).addChangeListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Rq1Reference> getDataModelValue() {
        if (isNull() == true) {
            ArrayList<Rq1Reference> result = new ArrayList<>();
            for (Rq1Reference r : sourceField_Field.getDataModelValue()) {
                if (wantedClass.isAssignableFrom(r.getRecord().getClass())) {
                    result.add(r);
                }
            }
            super.setDataSourceValue(result, Source.DATABASE);
        }
        return (super.getDataModelValue());
    }

    @Override
    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        ((Rq1ListI) sourceField_Field).loadCache(loadHint);
    }

    @Override
    public void reload() {
        ((Rq1ListI) sourceField_Field).reload();
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
    @SuppressWarnings("unchecked")
    public void changed(Rq1ListI changedElement) {
        ArrayList<Rq1Reference> result = new ArrayList<>();
        for (Rq1Reference r : sourceField_Field.getDataModelValue()) {
            if (wantedClass.isAssignableFrom(r.getRecord().getClass())) {
                result.add(r);
            }
        }
        super.setDataSourceValue(result, Source.DATABASE);
        //
        listenerManager.fireChange();
    }

}
