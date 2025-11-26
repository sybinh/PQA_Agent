/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Interfaces.Rq1ReferenceList_ReadOnlyI;
import Rq1Cache.Rq1Client;
import java.util.List;
import util.EcvListenerManager;

/**
 *
 * @author gug2wi
 */
public class Rq1StoredQuery implements Rq1ReferenceList_ReadOnlyI {

    final private EcvListenerManager<Rq1ListI> listenerManager;
    final String queryId;
    private Rq1Client.QueryResult lastResult = null;

    public Rq1StoredQuery(String queryId) {
        assert (queryId != null);

        this.listenerManager = new EcvListenerManager<>((Rq1ListI) this);
        this.queryId = queryId;
    }

    @Override
    public List<Rq1Reference> getReferenceList(boolean loadRecords) {
        return (getReferenceList());
    }

    @Override
    public List<Rq1Reference> getReferenceList() {
        if (lastResult == null) {
            lastResult = Rq1Client.client.executeStoredQuery(queryId);
            if (lastResult == null) {
                return (null);
            }
        }
        return (lastResult.getRecords());
    }

    public String getTitle() {
        if (lastResult == null) {
            return ("");
        } else {
            return (lastResult.getTitle());
        }
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
        lastResult = null;
        getReferenceList();
        listenerManager.fireChange();
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
