/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_UnknownProject_Exists;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1UnknownProject;

/**
 *
 * @author GUG2WI
 */
public class DmRq1UnknownProject extends DmRq1Project {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1UnknownProject(Rq1UnknownProject rq1UnknownProject) {
        super("Unknown-Project", rq1UnknownProject);

        //
        // Create and add fields
        //
        addField(new DmRq1Field_Text(this, rq1UnknownProject.TYPE, "Type"));
        addField(new DmRq1Field_Text(this, rq1UnknownProject.DOMAIN, "Domain"));

        //
        // Create and add rules
        //
        addRule(new Rule_UnknownProject_Exists(this));
    }
}
