/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.List;

/**
 * Container for the result of a OslcQuery.
 * @author gug2wi
 */
public class OslcQueryResult {

    final private String title;
    final private List<OslcQueryResultRecord> records;

    public OslcQueryResult(String title, List<OslcQueryResultRecord> records) {
        assert (title != null);
        assert (records != null);

        this.title = title;
        this.records = records;
    }

    /**
     * Returns the title of the query.
     * @return The title of the query.
     */
    public String getTitle() {
        return (title);
    }

    /**
     * Returns the records found by the query.
     * @return The records found by the query.
     */
    public List<OslcQueryResultRecord> getRecords() {
        return (records);
    }

}
