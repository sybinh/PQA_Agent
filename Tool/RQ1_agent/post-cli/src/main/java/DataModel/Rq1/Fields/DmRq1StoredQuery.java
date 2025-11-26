/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1QueryHit;
import Rq1Cache.Types.Rq1StoredQuery;

/**
 *
 * @author miw83wi
 */
public class DmRq1StoredQuery extends DmRq1TopReferenceList<DmRq1QueryHit> {

    final private String id;

    public DmRq1StoredQuery(String nameForUserInterface, String id) {
        super(new Rq1StoredQuery(id), nameForUserInterface);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return ("Query " + id + " - " + ((Rq1StoredQuery) rq1ReferenceList).getTitle());
    }

    @Override
    public String getNameForUserInterface() {
        return ("Query " + id + " - " + ((Rq1StoredQuery) rq1ReferenceList).getTitle());
    }

    @Override
    public String getViewTitle() {
        return getNameForUserInterface();
    }

}
