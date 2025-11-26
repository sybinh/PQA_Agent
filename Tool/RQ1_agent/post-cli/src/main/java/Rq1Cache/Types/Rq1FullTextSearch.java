/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
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
import Rq1Cache.Rq1RecordType;
import java.util.ArrayList;
import java.util.List;
import util.EcvListenerManager;

/**
 *
 * @author gug2wi
 */
public class Rq1FullTextSearch implements Rq1ReferenceList_ReadOnlyI {

    final private EcvListenerManager<Rq1ListI> listenerManager;
    final String searchString;
    final Rq1RecordType wantedRecordType;
    private List<Rq1Reference> lastResult = null;

    /**
     * Create a query object for a full text search.
     *
     * @param searchString The text for which the query shall be executed.
     * @param wantedRecordType The record type, if only records of a specific
     * type are wanted or null, if all record types shall be searched.
     */
    public Rq1FullTextSearch(String searchString, Rq1RecordType wantedRecordType) {
        assert (searchString != null);
        assert (searchString.length() > 2);

        this.listenerManager = new EcvListenerManager<>((Rq1ListI) this);
        this.searchString = searchString;
        this.wantedRecordType = wantedRecordType;
    }

    @Override
    public List<Rq1Reference> getReferenceList(boolean loadRecords) {
        return (getReferenceList());
    }

    /**
     * Returns the search result. If this is the first call of the method for
     * the query, then the query will be executed in this call.
     *
     * @return
     */
    @Override
    public List<Rq1Reference> getReferenceList() {
        if (lastResult == null) {
            if (wantedRecordType == null) {
                lastResult = Rq1Client.client.fullTextSearch(searchString);
                if (lastResult == null){
                    lastResult = new ArrayList<>(0);
                }
                assert (lastResult != null);
            } else {
                lastResult = Rq1Client.client.fullTextSearch(searchString, wantedRecordType);
                if (lastResult == null){
                    lastResult = new ArrayList<>(0);
                }
                assert (lastResult != null);
            }

        }
        return (lastResult);
    }
    
    @Override
    public List<Rq1Reference> getReferenceListOrNull() {
        if (lastResult == null) {
            if (wantedRecordType != null) {
                lastResult = Rq1Client.client.fullTextSearch(searchString, wantedRecordType);
            }
        }
        return (lastResult);
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

}
