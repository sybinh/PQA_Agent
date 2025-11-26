/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementListFieldI;
import DataModel.DmToDsField;
import DataModel.Rq1.Records.DmRq1BcRelease;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1WorkItem;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1ReferenceList_Writeable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hfi5wi
 * @param <T>
 */
public class DmRq1Field_ReferenceList_Writeable<T extends DmRq1ElementInterface>
        extends DmToDsField<Rq1ReferenceList_Writeable<Rq1Reference>> implements Rq1ListI.ChangeListener, DmElementListFieldI<T> {

    final private static Logger LOGGER = Logger.getLogger(DmRq1Field_ReferenceList_Writeable.class.getCanonicalName());

    private Rq1ReferenceList_Writeable<T> referenceList_writeable = null;

    public <F extends Rq1FieldI<Rq1ReferenceList_Writeable<Rq1Reference>> & Rq1ListI> DmRq1Field_ReferenceList_Writeable(
            F rq1ReferenceList_Writeable, String nameForUserInterface) {
        super(rq1ReferenceList_Writeable, nameForUserInterface);
        rq1ReferenceList_Writeable.addChangeListener(this);
    }

    @Override
    public void addElement(T newElement) {
        addElement_Synchronized(newElement);
        fireFieldChanged();
    }

    private synchronized void addElement_Synchronized(T newElement) {
        assert (newElement != null);

        getElementList_Synchronized().addElement(newElement);
        Rq1ReferenceList_Writeable<Rq1Reference> rq1List = dsField.getDataModelValue();
        rq1List.addElement(new Rq1Reference(newElement.getRq1Record()));
        dsField.setDataModelValue(rq1List);
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

        if (getElementList_Synchronized().getElementList().contains(newElement) == true) {
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

        if (getElementList_Synchronized().removeElement(elementToRemove) == true) {
            Rq1ReferenceList_Writeable<Rq1Reference> rq1List = dsField.getDataModelValue();
            rq1List.removeElement(new Rq1Reference(elementToRemove.getRq1Record()));
            dsField.setDataModelValue(rq1List);
            return (true);
        } else {
            return (false);
        }
    }

    @Override
    public List<T> getElementList() {
        Rq1ReferenceList_Writeable<T> alreadyLoadedList = referenceList_writeable;
        if (alreadyLoadedList != null) {
            return (alreadyLoadedList.getElementList());
        } else {
            return (getElementList_Synchronized().getElementList());
        }
    }

    @SuppressWarnings("unchecked")
    protected synchronized Rq1ReferenceList_Writeable<T> getElementList_Synchronized() {
        if (referenceList_writeable == null) {
            Rq1ReferenceList_Writeable<T> newList = new Rq1ReferenceList_Writeable<>();
            Rq1ReferenceList_Writeable<Rq1Reference> rq1List = dsField.getDataModelValue();
            assert (rq1List != null) : toString();
            for (Rq1Reference r : rq1List.getElementList()) {
                DmRq1ElementInterface e = DmRq1ElementCache.getElement(r.getRecord());
                try {
                    newList.addElement((T) e);
                } catch (ClassCastException ex) {
                    LOGGER.log(Level.WARNING, "Cannot convert {0} of class {1}", new Object[]{r.getOslcRecordReference().getShortTitle(), e.getClass().getName()});
                }
            }
            for (Rq1Reference r : rq1List.getTempElementsToDelete()) {
                DmRq1ElementInterface e = DmRq1ElementCache.getElement(r.getRecord());
                try {
                    newList.removeElement((T) e);
                } catch (ClassCastException ex) {
                    LOGGER.log(Level.WARNING, "Cannot convert {0} of class {1}", new Object[]{r.getOslcRecordReference().getShortTitle(), e.getClass().getName()});
                }
            }
            referenceList_writeable = newList;
        }
        return (referenceList_writeable);
    }

    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);
        ((Rq1ListI) dsField).loadCache(loadHint);
    }

    @Override
    public void reload() {
        if (referenceList_writeable != null) {
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
                }
            }

            ((Rq1ListI) dsField).reload();
        }
    }

    @Override
    public void changed(Rq1ListI changedElement) {
        referenceList_writeable = null;
        fireFieldChanged();
    }

    public boolean isLoaded() {
        return ((referenceList_writeable != null) || (dsField.isNull() == false));
    }

    public boolean containsElement(T elementToFind) {
        if (elementToFind != null) {
            for (T elementInList : getElementList()) {
                if (elementToFind == elementInList) {
                    return (true);
                }
            }
        }
        return (false);
    }

}
