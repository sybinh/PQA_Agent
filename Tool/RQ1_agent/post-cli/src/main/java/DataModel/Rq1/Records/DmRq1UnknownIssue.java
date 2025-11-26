/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_UnknownIssue_Exists;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1UnknownIssue;

/**
 *
 * @author GUG2WI
 */
public class DmRq1UnknownIssue extends DmRq1Issue {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1UnknownIssue(Rq1UnknownIssue rq1UnknownIssue) {
        super("Unknown-Issue", rq1UnknownIssue);

        PROJECT_CONFIG.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);

        //
        // Create and add fields
        //
        addField(new DmRq1Field_Text(this, rq1UnknownIssue.TYPE, "Type"));

        //
        // Create and add rules
        //
        addRule(new Rule_UnknownIssue_Exists(this));
    }

}
