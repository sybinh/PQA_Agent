/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1UnknownProject;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_UnknownProject_Exists extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Unknown Project Type",
            "A record of type 'Project' was read from RQ1 database, but the field 'Type' and/or 'Domain' contain unexpected values.");

    DmRq1UnknownProject myProject;

    public Rule_UnknownProject_Exists(DmRq1UnknownProject myProject) {
        super(description, myProject);
        this.myProject = myProject;
    }

    @Override
    public void executeRule() {
        myProject.setMarker(new Warning(this, "Unknown Project Type", "Unknown Project Type"));
    }
}
