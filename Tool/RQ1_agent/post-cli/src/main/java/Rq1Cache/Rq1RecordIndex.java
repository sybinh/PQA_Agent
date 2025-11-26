/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.RestException;
import OslcAccess.OslcRecordIdentifier;
import OslcAccess.Rq1.OslcRq1Client;
import Rq1Cache.Records.Rq1LinkInterface;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Records.Rq1SubjectInterface;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import Rq1Cache.Types.Rq1XmlTableColumn_SuperOplKey;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvApplication;

/**
 * This class manages access to Rq1Records.
 *
 * @author GUG2WI
 */
public class Rq1RecordIndex {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1RecordIndex.class.getCanonicalName());
    //
    static final private HashMap<OslcRecordIdentifier, Rq1RecordInterface> recordByRecordReference = new HashMap<>();
    static final private HashMap<String, Rq1RecordInterface> recordById = new HashMap<>();
    //
    static final private Object getRecordByIdentifierLock = new Object();

    static public int getCacheSize() {
        synchronized (recordByRecordReference) {
            return (recordByRecordReference.size());
        }
    }

    /**
     * Returns the RQ1-Subject identified by the RQ1-ID.
     *
     * @param rq1Id
     * @return The record or null if no record exists with this RQ1-ID.
     */
    static public Rq1SubjectInterface getSubjectByRq1Id(String rq1Id) {
        assert (rq1Id != null);
        assert (rq1Id.matches(Rq1SubjectInterface.patternRq1Id)) : rq1Id;

        Rq1RecordInterface record = getRecordById(rq1Id);

        if (record instanceof Rq1SubjectInterface) {
            return ((Rq1SubjectInterface) record);
        }

        return (null);
    }

    static public Rq1RecordInterface getRecordById(String id) {
        assert (id != null);
        assert (id.isEmpty() == false);

        Rq1RecordInterface record;

        //
        // Check if record already exists in cache
        //
        synchronized (recordById) {
            record = recordById.get(id);
        }
        if (record != null) {
            logger.finer("Found in cache.");
            return (record);
        }

        //
        // Fetch record from database
        //
        Rq1Client.client.loadRecordById(id);

        //
        // Now the subject should be in cache.
        //
        synchronized (recordById) {
            record = recordById.get(id);
        }
        if (record != null) {
            logger.finer("Found in cache after fetching from database.");
            return (record);
        }

        //
        // Subject not found in cache. This means that no subject with the given RQ1-ID was found in database.
        // Return null to indicate this
        //
        return (null);
    }

    /**
     * Returns the RQ1-Record identified by the reference object.
     *
     * @param reference
     * @return The record or null if no record exists for this reference.
     */
    static public Rq1RecordInterface getRecord(OslcRecordIdentifier reference) {
        assert (reference != null);

        //
        // Synchronize loading of objects to prevent parallel reads for the same target record.
        // This is a very simple way to prevent the parallel (and therefore double) loading of records.
        // But it is fine for us, because we do onla have one database connection.
        //
        synchronized (getRecordByIdentifierLock) {

            Rq1RecordInterface record;

            //
            // Check if record already exists in cache
            //
            synchronized (recordByRecordReference) {
                record = recordByRecordReference.get(reference);
            }
            if (record != null) {
                return (record);
            }

            //
            // Load record from database
            //
            Rq1Client.client.loadRecordByIdentifier(reference);

            //
            // Now the subject should be in cache.
            //
            synchronized (recordByRecordReference) {
                record = recordByRecordReference.get(reference);
                assert (record != null) : reference.toString();
            }

            return (record);
        }
    }

    /**
     * Adds the new record to the cache.
     *
     * @param newRecord
     */
    static public void addRecord(Rq1RecordInterface newRecord) {
        assert (newRecord != null);

        synchronized (recordByRecordReference) {
            recordByRecordReference.put(newRecord.getOslcRecordIdentifier(), newRecord);
        }

        synchronized (recordById) {
            recordById.put(newRecord.getId(), newRecord);
        }
    }

    static public Rq1RecordInterface getRecord_NoFetch(OslcRecordIdentifier reference) {
        assert (reference != null);

        //
        // Return record from cache.
        //
        synchronized (recordByRecordReference) {
            return (recordByRecordReference.get(reference));
        }
    }

    static public Rq1RecordInterface getRecord_NoFetch(String id) {
        assert (id != null);
        assert (id.isEmpty() == false);

        //
        // Return record from cache.
        //
        synchronized (recordById) {
            return (recordById.get(id));
        }
    }

    static public Rq1LinkInterface getMap(OslcRecordIdentifier mapIdentifier) {
        assert (mapIdentifier != null);

        Rq1RecordInterface record;

        //
        // Check if record already exists in cache
        //
        synchronized (recordByRecordReference) {
            record = recordByRecordReference.get(mapIdentifier);
        }
        if (record != null) {
            logger.finer("Found in cache.");
            assert (record instanceof Rq1LinkInterface) : record.getOslcShortTitle();
            return ((Rq1LinkInterface) record);
        }

        try {
            //
            // Load record from database
            //
            OslcRq1Client.getOslcClient().loadRecordByIdentifier(mapIdentifier);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1RecordIndex.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1RecordIndex.class.getName(), ex);
            EcvApplication.handleException(ex);
        }

        //
        // Now the map should be in cache.
        //
        synchronized (recordByRecordReference) {
            record = recordByRecordReference.get(mapIdentifier);
        }
        if (record != null) {
            logger.finer("Found in cache after fetching from database.");
            assert (record instanceof Rq1LinkInterface) : record.getOslcShortTitle();
            return ((Rq1LinkInterface) record);
        }

        //
        // Map not found in cache. This means that no subject with the given RQ1-ID was found in database.
        // Return null to indicate this
        //
        return (null);
    }

    static public void loadLoginUser() {
        try {
            OslcRq1Client.getOslcClient().loadLoginUser();
        } catch (RestException ex) {
            Logger.getLogger(Rq1RecordIndex.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1RecordIndex.class.getName(), ex);
            EcvApplication.handleException(ex);
        }
    }

}
