/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Monitoring;

import Monitoring.LoadFromDatabaseFailure;
import Monitoring.RuleI;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 *
 * @author GUG2WI
 */
public class Rq1UnexpectedDataFailure extends LoadFromDatabaseFailure {

    public Rq1UnexpectedDataFailure(RuleI rule, Rq1RecordInterface rq1Record, Rq1FieldI rq1Field, Exception ex) {
        super(rule, "Unexpected data in field " + rq1Field.getFieldName() + " read from RQ1 database.");

        assert (rq1Record != null);
        assert (rq1Field != null);
        assert (ex != null);

        StringBuilder s = new StringBuilder(200);
        s.append("Unexpected data detected when reading data for field ").append(rq1Field.getFieldName()).append(" of record ").append(rq1Record.getId()).append(".\n");
        s.append("Record type: ").append(rq1Record.getClass().getName()).append("\n");
        s.append("Field type: ").append(rq1Field.getClass().getName()).append("\n");
        s.append(ex.getMessage());
        super.setDescription(s.toString());
    }
}
