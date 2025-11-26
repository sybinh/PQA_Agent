/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Interfaces.Rq1ListField_DmInterface;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Records.Rq1Subject;
import Rq1Cache.Types.Rq1MappedReference;
import java.util.ArrayList;
import util.EcvListenerManager;

/**
 *
 * @author gug2wi
 */
public class Rq1MappedReferenceListField_FilterByClass extends DsField<Rq1RecordInterface, Rq1MappedReference[]> implements Rq1ListField_DmInterface<Rq1MappedReference>, Rq1ListI, Rq1ListI.ChangeListener {

    final private Rq1DatabaseField_MappedReferenceList sourceField;
    final private Class<Object> wantedClass;
    final private EcvListenerManager<Rq1ListI> listenerManager;

    @SuppressWarnings({"unchecked", "LeakingThisInConstructor"})
    public Rq1MappedReferenceListField_FilterByClass(Rq1Subject parent, Rq1DatabaseField_MappedReferenceList sourceField, Class wantedClass) {
        super(parent, sourceField.getFieldName() + "." + wantedClass.getName(), null);

        this.sourceField = sourceField;
        this.wantedClass = wantedClass;
        this.listenerManager = new EcvListenerManager(this);

        this.sourceField.addChangeListener(this);
    }

    private void loadValuesFromSourceField() {
        ArrayList<Rq1MappedReference> result = new ArrayList<>();
        for (Rq1MappedReference r : sourceField.getDataModelValue()) {
            if (wantedClass.isAssignableFrom(r.getRecord().getClass())) {
                result.add(r);
            }
        }
        super.setDataSourceValue(result.toArray(new Rq1MappedReference[0]), Source.DATABASE);
    }

    @Override
    final public Rq1MappedReference[] getDataModelValue() {
        if (isNull() == true) {
            loadValuesFromSourceField();
        }
        return (super.getDataModelValue());
    }

    @Override
    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        sourceField.loadCache(loadHint);
    }

    @Override
    final public void reload() {
        sourceField.reload();
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
    final public void changed(Rq1ListI changedElement) {
        loadValuesFromSourceField();
        listenerManager.fireChange();
    }

    @Override
    final public void addElement(Rq1MappedReference newElement) {
        sourceField.addElement(newElement);
    }

    @Override
    final public boolean removeElement(Rq1MappedReference elementToRemove) {
        if (sourceField.removeElement(elementToRemove) == true) {
            return (true);
        } else {
            return (false);
        }
    }

}
