/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Reference;
import Rq1Cache.Records.Rq1Irm_Unknown_IssueSw;
import Ipe.Annotations.IpeFactoryConstructor;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Irm_Unknown_IssueSw extends DmRq1Irm {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Irm_Unknown_IssueSw(Rq1Irm_Unknown_IssueSw rq1Irm_Unknown_IssueSw) {
        super("IRM-UNKNOWN-ISSUE_SW", rq1Irm_Unknown_IssueSw);

        //
        // Create and add fields
        //
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Unknown_IssueSw.HAS_MAPPED_ISSUE, "Issue SW"));
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Unknown_IssueSw.HAS_MAPPED_RELEASE, "Unknown Release"));
    }
}
