/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Records.Rq1IssueSW;
import Rq1Cache.Rq1RecordType;

/**
 *
 * @author fal83wi
 */
public class Rq1QueryForExternalIdIssue extends Rq1Query {

    public Rq1QueryForExternalIdIssue(String externalId) {
        super(Rq1RecordType.ISSUE);
        assert (externalId != null);
        assert (externalId.isEmpty() == false);

        super.addCriteria_Value(Rq1IssueSW.FIELDNAME_EXTERNAL_ID, externalId);
    }

}