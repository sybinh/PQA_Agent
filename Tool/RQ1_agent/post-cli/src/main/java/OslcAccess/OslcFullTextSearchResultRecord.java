/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 *
 * @author gug2wi
 */
public class OslcFullTextSearchResultRecord {

    final private OslcRecordIdentifier oslcRecordReference;
    final private String fullTextSearchTitle;
    final private String dcType;

    public OslcFullTextSearchResultRecord(OslcRecordIdentifier oslcRecordReference, String dcType, String fullTextSearchTitle) {
        assert (oslcRecordReference != null);
        assert (fullTextSearchTitle != null);

        this.oslcRecordReference = oslcRecordReference;
        this.fullTextSearchTitle = fullTextSearchTitle;
        this.dcType = dcType;
    }

    public OslcRecordIdentifier getOslcRecordReference() {
        return oslcRecordReference;
    }

    public String getFullTextSearchTitle() {
        return fullTextSearchTitle;
    }

    public String getDcType() {
        return dcType;
    }

}
