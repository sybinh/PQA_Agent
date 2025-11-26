/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmMappedElementListFieldI;
import DataModel.DmToDsField;
import DataModel.Flow.FlowCopy;
import DataModel.Rq1.DmRq1LinkInterface;
import DataModel.Rq1.DmRq1NodeInterface;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.DmMappedElement;
import Rq1Cache.Fields.Interfaces.Rq1ListField_DmInterface;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Records.Rq1LinkInterface;
import Rq1Cache.Records.Rq1SubjectInterface;
import Rq1Cache.Types.Rq1MappedReference;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.SafeArrayList;

/**
 *
 * @author gug2wi
 * @param <T_MAP>
 * @param <T_TARGET>
 */
public class DmRq1Field_MappedReferenceList<T_MAP extends DmRq1LinkInterface, T_TARGET extends DmRq1NodeInterface>
        extends DmToDsField<Rq1MappedReference[]> implements Rq1ListI.ChangeListener, DmMappedElementListFieldI<T_MAP, T_TARGET>, FlowCopy {

    final private static Logger LOGGER = Logger.getLogger(DmRq1Field_MappedReferenceList.class.getCanonicalName());

    final private Rq1ListField_DmInterface<Rq1MappedReference> listField;

    private SafeArrayList<DmMappedElement<T_MAP, T_TARGET>> list = null;
    private SafeArrayList<DmMappedElement<T_MAP, T_TARGET>> oldList = null; // Used for the reuse of maps
    private SafeArrayList<DmMappedElement<T_MAP, T_TARGET>> listForFlow = null; // used for flow concept

    public DmRq1Field_MappedReferenceList(DmElementI parent, Rq1ListField_DmInterface<Rq1MappedReference> rq1Field, String nameForUserInterface) {
        this(rq1Field, nameForUserInterface);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public DmRq1Field_MappedReferenceList(Rq1ListField_DmInterface<Rq1MappedReference> rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
        rq1Field.addChangeListener(this);
        listField = rq1Field;
    }

    @Override
    final public boolean isLoaded() {
        return ((list != null) || (oldList != null) || (dsField.isNull() != true));
    }

    @Override
    public List<DmMappedElement<T_MAP, T_TARGET>> getElementList() {
        SafeArrayList<DmMappedElement<T_MAP, T_TARGET>> alreadyLoadedList = list;
        if (alreadyLoadedList != null) {
            return (alreadyLoadedList.getImmutableList());
        } else {
            return (getElementList_Synchronized().getImmutableList());
        }
    }

    @SuppressWarnings("unchecked")
    protected synchronized SafeArrayList<DmMappedElement<T_MAP, T_TARGET>> getElementList_Synchronized() {
        if (list == null) {
            SafeArrayList<DmMappedElement<T_MAP, T_TARGET>> newList = new SafeArrayList<>();
            Rq1MappedReference[] rq1List = dsField.getDataModelValue();
            if (rq1List != null) { // List is null, if reading from database fails
                for (Rq1MappedReference r : rq1List) {
                    assert (r != null);
                    T_MAP m = null;
                    T_TARGET t = null;
                    try {
                        m = (T_MAP) DmRq1ElementCache.getElement(r.getLink());
                    } catch (ClassCastException ex) {
                        LOGGER.log(Level.SEVERE, "Cannot convert {0} of class {1}", new Object[]{r.getLink().getId(), r.getLink().getClass().getName()});
                        ToolUsageLogger.logError(DmRq1Field_MappedReferenceList.class.getCanonicalName(), ex);
                    }
                    try {
                        t = (T_TARGET) DmRq1ElementCache.getElement(r.getRecord());
                    } catch (ClassCastException ex) {
                        LOGGER.log(Level.SEVERE, "Cannot convert {0} of class {1}", new Object[]{r.getRecord().getId(), r.getRecord().getClass().getName()});
                        ToolUsageLogger.logError(DmRq1Field_MappedReferenceList.class.getCanonicalName(), ex);
                    }
                    if ((m != null) && (t != null)) {
                        newList.add(createMappedElement(m, t));
                    }
                }
            }
            list = newList;
        }
        return (list);
    }

    protected DmMappedElement<T_MAP, T_TARGET> createMappedElement(T_MAP newMap, T_TARGET newTarget) {
        assert (newMap != null);
        assert (newTarget != null);

        if (oldList != null) {
            for (DmMappedElement<T_MAP, T_TARGET> oldMappedElement : oldList) {
                if ((oldMappedElement.getMap() == newMap) && (oldMappedElement.getTarget() == newTarget)) {
                    return (oldMappedElement);
                }
            }
        }

        return (new DmMappedElement<>(newMap, newTarget));
    }

    @Override
    final public void addElement(DmMappedElement<T_MAP, T_TARGET> e) {
        assert (e != null);
        addElement_Synchronized(e.getMap(), e.getTarget());
        fireFieldChanged();
    }

    final public void addElement(T_MAP newMap, T_TARGET newTarget) {
        addElement_Synchronized(newMap, newTarget);
        fireFieldChanged();
    }

    private synchronized void addElement_Synchronized(T_MAP newMap, T_TARGET newTarget) {
        assert (newMap != null);
        assert (newTarget != null);

        getElementList_Synchronized().add(new DmMappedElement<>(newMap, newTarget));
        listField.addElement(new Rq1MappedReference((Rq1SubjectInterface) newTarget.getRq1Record(), (Rq1LinkInterface) newMap.getRq1Record()));
    }

    final public boolean removeElement(T_TARGET obsoletTarget) {
        if (removeElement_Synchronized(obsoletTarget) == true) {
            fireFieldChanged();
            return (true);
        } else {
            return (false);
        }
    }

    private synchronized boolean removeElement_Synchronized(T_TARGET obsoletTarget) {
        assert (obsoletTarget != null);

        for (DmMappedElement<T_MAP, T_TARGET> targetMap : getElementList_Synchronized()) {
            if (targetMap.getTarget() == obsoletTarget) {
                list.remove(targetMap);
                listField.removeElement(new Rq1MappedReference((Rq1SubjectInterface) targetMap.getTarget().getRq1Record(), (Rq1LinkInterface) targetMap.getMap().getRq1Record()));
                return (true);
            }
        }
        return (false);
    }

    /**
     * Find the mapped element for the given target. The target in the map
     * matches the searched target, if both are the same object.
     *
     * @param searchedTarget Target to be searched.
     * @return The map for the target, if the target is contained in the list.
     * Otherwise null.
     */
    final public DmMappedElement<T_MAP, T_TARGET> findElement(T_TARGET searchedTarget) {
        assert (searchedTarget != null);

        for (DmMappedElement<T_MAP, T_TARGET> dmMappedElement : getElementList()) {
            if (dmMappedElement.getTarget() == searchedTarget) {
                return (dmMappedElement);
            }
        }
        return (null);
    }

    @Override
    final public void changed(Rq1ListI changedElement) {
        changed_Synchronized(changedElement);
        fireFieldChanged();
    }

    private synchronized void changed_Synchronized(Rq1ListI changedElement) {
        if (list != null) {
            oldList = list;
            list = null;

        }
        if (listForFlow != null) {
            listForFlow = null;
        }
    }

    @Override
    public void reload() {
        if (list != null) {
            listField.reload();
        }
    }

    // For flow framework
    final public synchronized void addElementForFlow(DmMappedElement<T_MAP, T_TARGET> e) {
        assert (e != null);
        if (list == null) {
            list = new SafeArrayList<>();
        }
        if (listForFlow == null) {
            listForFlow = new SafeArrayList<>();
        }
        list.add(
                new DmMappedElement<>(e.getMap(), e.getTarget()));
        listForFlow.add(
                new DmMappedElement<>(e.getMap(), e.getTarget()));
    }

    // For Flow framework     
    final synchronized public SafeArrayList<DmMappedElement<T_MAP, T_TARGET>> getElementListForFlow() {

        if (listForFlow != null && !listForFlow.getImmutableList().isEmpty()) {

            return listForFlow;
        } else {

            return new SafeArrayList<>();
        }

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DmRq1Field_MappedReferenceList cloned = (DmRq1Field_MappedReferenceList) super.clone();
        return cloned;
    }

    @Override
    public String toString() {
        return ("MappedField:" + super.getNameForUserInterface());
    }

}
