/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1WorkItem_SoftwareIssue;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1WorkItem_SoftwareIssue extends DmRq1WorkItem_Software {

    final public DmRq1Field_Text COMPL_LINK;
    final public DmRq1Field_Text COMPL_RESULT;
    final public DmRq1Field_Text COMPL_MEASURES_ID;

    public DmRq1WorkItem_SoftwareIssue(String subjectType, Rq1WorkItem_SoftwareIssue rq1WorkItem) {
        super(subjectType, rq1WorkItem);

        //
        // Create and add fields
        //
        addField(COMPL_LINK = new DmRq1Field_Text(this, rq1WorkItem.COMPL_LINK, "Compliance Link (double click to open)"));
        addField(COMPL_RESULT = new DmRq1Field_Text(this, rq1WorkItem.COMPL_RESULT, "Compliance Result"));
        addField(COMPL_MEASURES_ID = new DmRq1Field_Text(this, rq1WorkItem.COMPL_MEASURES_ID, "Compliance Measures ID"));
    }

}
