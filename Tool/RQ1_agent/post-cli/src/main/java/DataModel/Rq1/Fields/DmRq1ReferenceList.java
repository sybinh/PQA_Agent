/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementListField_ReadOnlyI;
import DataModel.DmField;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Interfaces.Rq1ReferenceList_ReadOnlyI;
import Rq1Cache.Types.Rq1Reference;
import java.util.*;
import java.util.logging.Level;

/**
 *
 * @author gug2wi
 * @param <T_ELEMENT>
 */
public class DmRq1ReferenceList<T_ELEMENT extends DmRq1ElementInterface> extends DmField implements Rq1ListI.ChangeListener, DmElementListField_ReadOnlyI<T_ELEMENT> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmRq1ReferenceList.class.getCanonicalName());

    final protected Rq1ReferenceList_ReadOnlyI rq1ReferenceList;
    private boolean listLoaded;

    @SuppressWarnings("LeakingThisInConstructor")
    public DmRq1ReferenceList(Rq1ReferenceList_ReadOnlyI rq1ReferenceList, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (rq1ReferenceList != null);

        this.rq1ReferenceList = rq1ReferenceList;
        this.listLoaded = false;
        rq1ReferenceList.addChangeListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T_ELEMENT> getElementList() {
        List<Rq1Reference> rq1List = rq1ReferenceList.getReferenceList(true);
        listLoaded = true;
        if (rq1List != null) {
            ArrayList<T_ELEMENT> result = new ArrayList<>();
            for (Rq1Reference r : rq1List) {
                DmRq1ElementInterface e = DmRq1ElementCache.getElement(r.getRecord());
                try {
                    result.add((T_ELEMENT) e);
                } catch (ClassCastException ex) {
                    logger.log(Level.WARNING, "Cannot convert {0} of class {1}", new Object[]{r.getOslcRecordReference().getShortTitle(), e.getClass().getName()});
                }
            }
            return (result);
        } else {
            return (null);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<T_ELEMENT> getElementListForOmm() {
        List<Rq1Reference> rq1List = rq1ReferenceList.getReferenceListOrNull();
        if (rq1List != null) {
            ArrayList<T_ELEMENT> result = new ArrayList<>();
            for (Rq1Reference r : rq1List) {
                DmRq1ElementInterface e = DmRq1ElementCache.getElement(r.getRecord());
                try {
                    result.add((T_ELEMENT) e);
                } catch (ClassCastException ex) {
                    logger.log(Level.WARNING, "Cannot convert {0} of class {1}", new Object[]{r.getOslcRecordReference().getShortTitle(), e.getClass().getName()});
                }
            }
            return (result);
        } else {
            return (null);
        }
    }

    @Override
    public void reload() {
        if (listLoaded == true) {
            ((Rq1ListI) rq1ReferenceList).reload();
        }
    }

    @Override
    public void changed(Rq1ListI changedElement) {
        fireFieldChanged();
    }

}
