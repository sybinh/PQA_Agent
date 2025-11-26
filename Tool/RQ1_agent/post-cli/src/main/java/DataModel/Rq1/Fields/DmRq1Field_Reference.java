/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementFieldI;
import DataModel.DmToDsField;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataStore.DsRecordI;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1NodeInterface;
import Rq1Cache.Types.Rq1Reference;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gug2wi
 * @param <T_CONTENT>
 */
public class DmRq1Field_Reference<T_CONTENT extends DmRq1ElementInterface>
        extends DmToDsField<Rq1Reference>
        implements DmElementFieldI<T_CONTENT>, DsRecordI.ChangeListener {

    final private static Logger LOGGER = Logger.getLogger(DmRq1Field_Reference.class.getCanonicalName());

    public DmRq1Field_Reference(DmRq1Element parent, Rq1FieldI<Rq1Reference> rq1Field, String nameForUserInterface) {
        this(rq1Field, nameForUserInterface);
    }

    public DmRq1Field_Reference(Rq1FieldI<Rq1Reference> rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
        rq1Field.getParentRecord().addChangeListener(this);
    }

    /**
     * Returns the referenced element or null if no element is referenced. This
     * method triggers a database access, if the element is not yet in the
     * cache. Do not use this method if only a check to null/not null is needed.
     * Use isElementSet() instead, because this method prevents any database
     * access.
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public T_CONTENT getElement() {
        Rq1Reference rq1Reference = dsField.getDataModelValue();
        T_CONTENT result = null;
        if (rq1Reference != null) {
            Rq1NodeInterface rq1Node = (Rq1NodeInterface) rq1Reference.getRecord();
            assert (rq1Node != null);
            try {
                result = (T_CONTENT) DmRq1ElementCache.getElement(rq1Node);
            } catch (ClassCastException ex) {
                LOGGER.log(Level.WARNING, "Cannot convert {0} of class {1}", new Object[]{rq1Node.getId(), rq1Node.getClass().getName()});
            }
        }
        return (result);
    }

    /**
     * Checks whether or not an element is set on the reference field. This
     * check is always done without database access. It is therefore the
     * preferred solution if only the check is needed.
     *
     * @return true, if an element is set; false otherwise.
     */
    public boolean isElementSet() {
        return (dsField.getDataModelValue() != null);
    }

    /**
     * Checks whether or not the field contains the same element as given as
     * parameter. This check is always done without database access. It is
     * therefore the preferred solution if only the check is needed.
     *
     * @param elementToCompare
     * @return
     */
    public boolean isElementEqual(T_CONTENT elementToCompare) {
        if (elementToCompare != null) {
            Rq1Reference value = dsField.getDataModelValue();
            if (value == null) {
                return (false);
            } else {
                return (value.equals(elementToCompare.getRq1Record()));
            }
        } else {
            return (dsField.getDataModelValue() == null);
        }
    }

    /**
     * Checks whether or not the field contains one of the elements in the given
     * collection. This check is always done without database access. It is
     * therefore the preferred solution if only the check is needed.
     *
     * @param elementsToCompare
     * @return
     */
    public boolean isElementInCollection(Collection<? extends T_CONTENT> elementsToCompare) {
        assert (elementsToCompare != null);
        for (T_CONTENT elementToCompare : elementsToCompare) {
            if (isElementEqual(elementToCompare) == true) {
                return (true);
            }
        }
        return (false);
    }

    @Override
    public void setElement(T_CONTENT element) {
        assert (element != null);
        dsField.setDataModelValue(new Rq1Reference(element.getRq1Record()));
        fireFieldChanged();
    }

    @Override
    public void removeElement() {
        dsField.setDataModelValue(null);
        fireFieldChanged();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reload() {
        ((Rq1FieldI<Rq1Reference>) dsField).getParentRecord().reload();
    }

    @Override
    public void changed(DsRecordI changedElement) {
        fireFieldChanged();
    }
}
