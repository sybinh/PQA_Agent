/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Rq1LinkDescription;
import OslcAccess.OslcRecordIdentifier;
import java.util.logging.Level;

/**
 *
 * @author gug2wi
 */
public class Rq1Rrm_Unknown_Bc extends Rq1Rrm {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1Rrm_Unknown_Bc.class.getCanonicalName());

    public Rq1Rrm_Unknown_Bc() {
        super(Rq1LinkDescription.RRM_UNKNOWN_BC);
    }

    @Override
    public void setOslcRecordIdentifier(OslcRecordIdentifier oslcRecordReference) {
        super.setOslcRecordIdentifier(oslcRecordReference);

        logger.log(Level.WARNING, "Map to unknown release type found. Short Title: {0}", oslcRecordReference.getShortTitle());
    }
}
