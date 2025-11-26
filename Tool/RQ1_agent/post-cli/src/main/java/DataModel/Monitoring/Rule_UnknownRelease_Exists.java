/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1UnknownRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_UnknownRelease_Exists extends DmRule {

    DmRq1UnknownRelease myRelease;

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Unknown Release Type",
            "A record of type 'Release' was read from RQ1 database, but the fields 'Domain', 'Type' and 'Category' contain unexpected values.");

    public Rule_UnknownRelease_Exists(DmRq1UnknownRelease myRelease) {
        super(description, myRelease);
        this.myRelease = myRelease;
    }

    @Override
    public void executeRule() {
        myRelease.setMarker(new Warning(this, "Unknown Release Type", "Unknown Release Type"));
    }
}
