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
 *
 * @author GUG2WI
 */
public class Rq1UnknownRelease extends Rq1Release {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1UnknownRelease.class.getCanonicalName());
    //
    final public Rq1DatabaseField_Text DOMAIN;
    final public Rq1DatabaseField_Text TYPE;

    public Rq1UnknownRelease() {
        super(Rq1NodeDescription.UNKNOWN_RELEASE);

        addField(DOMAIN = new Rq1DatabaseField_Text(this, "Domain"));
        addField(TYPE = new Rq1DatabaseField_Text(this, "Type"));
    }

    @Override
    public void setOslcRecordIdentifier(OslcRecordIdentifier oslcRecordReference) {
        super.setOslcRecordIdentifier(oslcRecordReference);

        logger.log(Level.WARNING, "Unknown release type found. RQ1-ID: {0}", oslcRecordReference.getShortTitle());
    }
}
