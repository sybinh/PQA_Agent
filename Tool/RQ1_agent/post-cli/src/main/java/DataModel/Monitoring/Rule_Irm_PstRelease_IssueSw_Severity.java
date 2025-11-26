/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.Priority;
import Rq1Data.Enumerations.Severity;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Irm_PstRelease_IssueSw_Severity extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Check that Severity and Priority are plausible.",
            "If the severity is HIGH, then also the priority has to be HIGH.\n"
            + "If this is not the case, a comment has to be entered in the priority comment.\n"
            + "\n"
            + "The rule creates a warning on an IRM, if Severity == HIGH and Priority != HIGH and Priority Comment is empty.\n"
            + "\n"
            + "The rule is only checked for IRM that are not canceled and not conflicted."
    );

    final private DmRq1Irm_Pst_IssueSw myIrm;

    public Rule_Irm_PstRelease_IssueSw_Severity(DmRq1Irm_Pst_IssueSw irm_Pver_IssueSW) {
        super(description, irm_Pver_IssueSW);
        this.myIrm = irm_Pver_IssueSW;
    }

    @Override
    public void executeRule() {

        //
        // Check only for IRM not canceled and not conflicted.
        //
        if (myIrm.isCanceledOrConflicted() == true) {
            return;
        }

        DmRq1Issue issue = myIrm.HAS_MAPPED_ISSUE.getElement();
        if (issue instanceof DmRq1IssueSW) {
            addDependency(issue);

            if (((DmRq1IssueSW) issue).SEVERITY.getValue() == Severity.HIGH) {
                if ((myIrm.PRIORITY.getValue() != Priority.HIGH) && (myIrm.PRIORITY_COMMENT.getValueAsText().isEmpty() == true)) {
                    addMarker(myIrm, new Warning(this,
                            "Priority comment missing.",
                            "Priority does not fit to severity, but no comment is given for the priority."));
                }
            }
        }
    }
}
