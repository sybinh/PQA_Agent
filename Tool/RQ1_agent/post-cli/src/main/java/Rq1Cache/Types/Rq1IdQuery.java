/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Interfaces.Rq1ReferenceList_ReadOnlyI;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import util.EcvListenerManager;

/**
 *
 * @author gug2wi
 */
public class Rq1IdQuery implements Rq1ReferenceList_ReadOnlyI {
    
    private static final Logger LOGGER = Logger.getLogger(Rq1IdQuery.class.getCanonicalName());


    final private EcvListenerManager<Rq1ListI> listenerManager;
    final Set<String> rq1Ids;
    List<Rq1Reference> lastResult = null;

    public Rq1IdQuery(Set<String> rq1Ids) {
        assert (rq1Ids != null);

        this.listenerManager = new EcvListenerManager<>((Rq1ListI) this);
        this.rq1Ids = rq1Ids;
    }

    @Override
    public List<Rq1Reference> getReferenceList(boolean loadRecords) {
        return (getReferenceList());
    }

    @Override
    public List<Rq1Reference> getReferenceList() {
        if (lastResult == null) {
            List<Rq1Reference> result = new ArrayList<>();
            for (String rq1Id : rq1Ids) {
                Rq1RecordInterface rq1Record = Rq1Cache.Rq1RecordIndex.getRecordById(rq1Id);
                if (rq1Record != null) {
                    result.add(new Rq1Reference(rq1Record));
                }
            }
            lastResult = result;
        }
        return (lastResult);
    }

    public String getTitle() {
        return ("IDs");
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
    public void reload() {
        LOGGER.severe("This method is not implemented because it is not needed for SearchForRq1Ids");
    }

    @Override
    public void loadCache(OslcLoadHint loadHint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Rq1Reference> getReferenceListOrNull() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
