/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.Map;
import java.util.TreeMap;

/**
 * Container for a result record of a query in the OSLC interface
 *
 * @author gug2wi
 */
public class OslcQueryResultRecord {

    final private OslcRecordIdentifier oslcRecordReference;
    final private String title;
    final private String recordType;
    final private Map<String, String> fieldList;

    public OslcQueryResultRecord(OslcRecordIdentifier oslcRecordReference, String recordType, String title) {
        assert (oslcRecordReference != null);
        assert (recordType != null);
        assert (recordType.isEmpty() == false);
        assert (title != null);

        this.oslcRecordReference = oslcRecordReference;
        this.title = title;
        this.recordType = recordType;
        this.fieldList = new TreeMap<>();
    }

    public OslcQueryResultRecord(OslcRecordIdentifier oslcRecordReference, String recordType, String title, Map<String, String> fieldList) {
        assert (oslcRecordReference != null);
        assert (recordType != null);
        assert (recordType.isEmpty() == false);
        assert (title != null);
        assert (fieldList != null);

        this.oslcRecordReference = oslcRecordReference;
        this.title = title;
        this.recordType = recordType;
        this.fieldList = fieldList;
    }

    /**
     * Returns the record identifier of the received record.
     *
     * @return The record identifier of the received record.
     */
    public OslcRecordIdentifier getOslcRecordReference() {
        return oslcRecordReference;
    }

    /**
     * Returns the title of the received record.
     *
     * @return The title of the received record.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the RQ1 record type of the received record.
     *
     * @return The RQ1 record type of the received record.
     */
    public String getRecordType() {
        return recordType;
    }

    /**
     * Returns the fields of the received record in a name/value map.
     *
     * @return The fields of the received record in a name/value map.
     */
    public Map<String, String> getFieldList() {
        return (fieldList);
    }

}
