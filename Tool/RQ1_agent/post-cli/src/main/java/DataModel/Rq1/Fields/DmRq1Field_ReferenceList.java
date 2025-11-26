/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmElementListFieldI;
import DataModel.DmToDsField;
import DataModel.Rq1.Records.DmRq1BcRelease;
import DataModel.Rq1.Records.DmRq1EcuRelease;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueHwMod;
import DataModel.Rq1.Records.DmRq1ModRelease;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1WorkItem;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Types.Rq1Reference;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.SafeArrayList;

/**
 *
 * @author gug2wi
 * @param <T>
 */
public class DmRq1Field_ReferenceList<T extends DmRq1ElementInterface> extends DmToDsField<List<Rq1Reference>> implements Rq1ListI.ChangeListener, DmElementListFieldI<T> {

    final private static Logger LOGGER = Logger.getLogger(DmRq1Field_ReferenceList.class.getCanonicalName());

    private SafeArrayList<T> list = null;

    public <F extends Rq1FieldI<List<Rq1Reference>> & Rq1ListI> DmRq1Field_ReferenceList(
            DmElementI parent, F rq1ReferenceListField, String nameForUserInterface) {
        this(rq1ReferenceListField, nameForUserInterface);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public <F extends Rq1FieldI<List<Rq1Reference>> & Rq1ListI> DmRq1Field_ReferenceList(
            F rq1ReferenceListField, String nameForUserInterface) {

        super(rq1ReferenceListField, nameForUserInterface);
        rq1ReferenceListField.addChangeListener(this);
    }

    @Override
    public List<T> getElementList() {
        SafeArrayList<T> alreadyLoadedList = list;
        if (alreadyLoadedList != null) {
            return (alreadyLoadedList.getImmutableList());
        } else {
            return (getElementList_Synchronized().getImmutableList());
        }
    }

    @SuppressWarnings("unchecked")
    protected synchronized SafeArrayList<T> getElementList_Synchronized() {

        if (list == null) {
            SafeArrayList<T> newList = new SafeArrayList<>();
            List<Rq1Reference> rq1List = dsField.getDataModelValue();
            assert (rq1List != null) : toString();
            for (Rq1Reference r : rq1List) {
                DmRq1ElementInterface e = DmRq1ElementCache.getElement(r.getRecord());
                try {
                    newList.add((T) e);
                } catch (ClassCastException ex) {
                    LOGGER.log(Level.WARNING, "Cannot convert {0} of class {1}", new Object[]{r.getOslcRecordReference().getShortTitle(), e.getClass().getName()});
                }
            }
            list = newList;
        }
        return (list);
    }

    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        ((Rq1ListI) dsField).loadCache(loadHint);
    }

    @Override
    public void reload() {
        if (list != null) {
            // Flow Reload option clear internal rank
            final List<T> elementList = getElementList();
            for (T dmElement : elementList) {
                if (dmElement instanceof DmRq1Irm) {
                    ((DmRq1Irm) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1Rrm) {
                    ((DmRq1Rrm) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1IssueFD) {
                    ((DmRq1IssueFD) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1WorkItem) {
                    ((DmRq1WorkItem) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1BcRelease) {
                    ((DmRq1BcRelease) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1Pst) {
                    ((DmRq1Pst) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1EcuRelease) {
                    ((DmRq1EcuRelease) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1ModRelease) {
                    ((DmRq1ModRelease) dmElement).internalRank = null;
                } else if (dmElement instanceof DmRq1IssueHwMod) {
                    ((DmRq1IssueHwMod) dmElement).internalRank = null;
                }
            }

            ((Rq1ListI) dsField).reload();
        }
    }

    @Override
    public void addElement(T newElement) {
        addElement_Synchronized(newElement);
        fireFieldChanged();
    }

    private synchronized void addElement_Synchronized(T newElement) {
        assert (newElement != null);

        getElementList_Synchronized().add(newElement);
        List<Rq1Reference> array = new ArrayList<>(dsField.getDataModelValue());
        array.add(new Rq1Reference(newElement.getRq1Record()));
        dsField.setDataModelValue(array);
    }

    /**
     * Add the a new element, if it is not yet in the list.
     *
     * @param newElement Element to add.
     * @return true, if the element was added; false, if the element was already
     * in the list
     */
    public boolean addElementUnique(T newElement) {
        if (addElementUnique_Synchronized(newElement) == true) {
            fireFieldChanged();
            return (true);
        }
        return (false);
    }

    private synchronized boolean addElementUnique_Synchronized(T newElement) {
        assert (newElement != null);

        if (getElementList_Synchronized().contains(newElement) == true) {
            return (false);
        }

        addElement_Synchronized(newElement);
        return (true);
    }

    public boolean removeElement(T elementToRemove) {
        if (removeElement_Synchronized(elementToRemove) == true) {
            fireFieldChanged();
            return (true);
        } else {
            return (false);
        }
    }

    private synchronized boolean removeElement_Synchronized(T elementToRemove) {
        assert (elementToRemove != null);

        if (getElementList_Synchronized().remove(elementToRemove) == true) {
            List<Rq1Reference> array = new ArrayList<>(dsField.getDataModelValue());
            array.remove(new Rq1Reference(elementToRemove.getRq1Record()));
            dsField.setDataModelValue(array);
            return (true);
        } else {
            return (false);
        }
    }

    @Override
    public void changed(Rq1ListI changedElement) {
        list = null;
        fireFieldChanged();
    }

    public boolean isLoaded() {
        return ((list != null) || (dsField.isNull() == false));
    }

    public void addElementIfLoaded(T newElement) {
        if (isLoaded() == true) {
            addElement(newElement);
        }
    }

    public void reloadEnforced() {
        if (list != null) {
            reload();
        } else {
            getElementList();
        }
    }
}
