/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Interfaces.Rq1QueryI;
import Rq1Cache.Fields.Interfaces.Rq1ReferenceList_ReadOnlyI;
import java.util.ArrayList;
import java.util.List;
import util.EcvListenerManager;

/**
 *
 * @author GUG2WI
 */
public class Rq1CombinedQuery implements Rq1ReferenceList_ReadOnlyI {

    final private EcvListenerManager<Rq1ListI> listenerManager = new EcvListenerManager<>((Rq1ListI) this);
    final private List<Rq1QueryI> subQueries = new ArrayList<>();
    private List<Rq1Reference> lastResult = null;

    public Rq1CombinedQuery() {

    }

    public void addQuery(Rq1QueryI subQuery) {
        assert (subQuery != null);

        subQueries.add(subQuery);
    }

    @Override
    public List<Rq1Reference> getReferenceList(boolean loadRecords) {
        if (lastResult == null) {
            lastResult = new ArrayList<>();
            for (Rq1QueryI subQuery : subQueries) {
                lastResult.addAll(subQuery.getReferenceList(loadRecords));
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
        for (Rq1QueryI subQuery : subQueries) {
            subQuery.loadCache(loadHint);
        }
    }


    @Override
    public void reload() {
        lastResult = new ArrayList<>();
        for (Rq1QueryI subQuery : subQueries) {
            subQuery.reload();
            lastResult.addAll(subQuery.getReferenceList());
        }
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
