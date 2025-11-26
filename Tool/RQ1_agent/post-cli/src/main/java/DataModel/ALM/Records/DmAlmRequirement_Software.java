/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataStore.ALM.DsAlmRecord_Requirement;

/**
 *
 * @author GUG2WI
 */
public class DmAlmRequirement_Software extends DmAlmRequirement {

    public static final String REQ_TYPE = "SOFTWARE_REQ";

    public DmAlmRequirement_Software(DsAlmRecord_Requirement dsAlmRecord) {
        super(REQ_TYPE, dsAlmRecord);

        ignoreField("rdf:type");

        checkForUnusedFields();
    }

}
