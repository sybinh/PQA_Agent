/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import OslcAccess.OslcLoadHint;
import OslcAccess.OslcSelection;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Interfaces.Rq1ReferenceList_ReadOnlyI;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1RecordType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvListenerManager;

/**
 *
 * @author gug2wi
 */
public class Rq1FreeQuery implements Rq1ReferenceList_ReadOnlyI {

    final private EcvListenerManager<Rq1ListI> listenerManager;
    final private OslcSelection selection;
    final private Collection<String> requestedProperties;
    private Rq1Client.QueryResult lastResult = null;

    public Rq1FreeQuery(Rq1RecordType recordType) {
        assert (recordType != null);

        listenerManager = new EcvListenerManager<>((Rq1ListI) this);
        selection = new OslcSelection().setRecordType(recordType);
        requestedProperties = new ArrayList<>();
    }

    @Override
    public List<Rq1Reference> getReferenceList(boolean loadRecords) {
        return (getReferenceList());
    }

    @Override
    public List<Rq1Reference> getReferenceList() {
        if (lastResult == null) {
            lastResult = Rq1Client.client.executeFreeQuery(selection, requestedProperties);
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

    public void addReferenceCriteria(Rq1AttributeName attribute1, Rq1AttributeName attribute2, Rq1RecordInterface rq1Record) {
        assert (attribute1 != null);
        assert (attribute2 != null);
        assert (rq1Record != null);
        assert (rq1Record.existsInDatabase() == true);

        selection.addReference(attribute1.getName() + "." + attribute2.getName(), rq1Record.getOslcRecordIdentifier().getRdfAbout());
    }

    public void addRequestedAttribute(Rq1AttributeName attribute) {
        assert (attribute != null);
        requestedProperties.add(attribute.getName());
    }

    @Override
    public List<Rq1Reference> getReferenceListOrNull() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
