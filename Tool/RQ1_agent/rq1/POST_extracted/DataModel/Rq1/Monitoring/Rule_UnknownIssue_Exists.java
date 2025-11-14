/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1UnknownIssue;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_UnknownIssue_Exists extends DmRule<DmRq1UnknownIssue> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Unknown Issue Type",
            "A record of type 'Issue' was read from RQ1 database, but the field 'Type' contains unexpected values.");

    public Rule_UnknownIssue_Exists(DmRq1UnknownIssue myIssue) {
        super(description, myIssue);
    }

    @Override
    public void executeRule() {
        dmElement.setMarker(new Warning(this, "Unknown Issue Type", "Unknown Issue Type"));
    }
}
