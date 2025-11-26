/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1NodeDescription;
import OslcAccess.OslcRecordIdentifier;
import java.util.logging.Level;

/**
 * Implements the cache for an Issue SW record of the RQ1 database.
 *
 * @author gug2wi
 */
public class Rq1UnknownIssue extends Rq1Issue {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1UnknownIssue.class.getCanonicalName());
    //
    final public Rq1DatabaseField_Text TYPE;

    public Rq1UnknownIssue() {
        super(Rq1NodeDescription.UNKNOWN_ISSUE);

        addField(TYPE = new Rq1DatabaseField_Text(this, "Type"));
    }

    @Override
    public void setOslcRecordIdentifier(OslcRecordIdentifier oslcRecordIdentifier) {
        super.setOslcRecordIdentifier(oslcRecordIdentifier);

        logger.log(Level.WARNING, "Unknown issue type found. RQ1-ID: {0}", oslcRecordIdentifier.getShortTitle());
    }
}
