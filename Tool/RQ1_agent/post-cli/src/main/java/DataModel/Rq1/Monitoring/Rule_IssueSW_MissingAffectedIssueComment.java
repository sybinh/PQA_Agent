/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Project;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.SoftwareIssueCategory;
import java.util.EnumSet;
import util.EcvDate;

/**
 * Implements rule for ECVTOOL-2524
 *
 * @author gug2wi
 */
public class Rule_IssueSW_MissingAffectedIssueComment extends DmRule<DmRq1IssueSW> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription DESCRIPTION = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Check for missing affected issue comment.",
            "For a defect I-SW, the comment for affected issues has to be set, if no affected issue is attached to the defect issue.\n"
            + "\n"
            + "This check is only done if the following conditions are true:"
            + "- Issue was submitted after 1/2017.\n"
            + "- Issue is in life cycle state evaluated or later.\n"
            + "- Issue has scope external.\n"
            + "- Issue is in the VW Group.\n"
            + "\n"
            + "The warning 'Missing affected issue comment' is set on the defect issue, if the comment and the link is missing.");

    public Rule_IssueSW_MissingAffectedIssueComment(DmRq1IssueSW myIssue) {
        super(DESCRIPTION, myIssue);
    }

    @Override
    public void executeRule() {

        //
        // Skip non defect issues
        //
        if (dmElement.CATEGORY.getValue() != SoftwareIssueCategory.DEFECT) {
            return;
        }

        //
        // Skip internal issues
        //
        if (dmElement.SCOPE.getValue() != Scope.EXTERNAL) {
            return;
        }

        //
        // Skip non matching life cycle states
        //
        if (dmElement.isInLifeCycleState(
                LifeCycleState_Issue.EVALUATED,
                LifeCycleState_Issue.COMMITTED,
                LifeCycleState_Issue.IMPLEMENTED,
                LifeCycleState_Issue.CLOSED) == false) {
            return;
        }

        //
        // Skip non empty comments
        //
        if (dmElement.AFFECTED_ISSUE_COMMENT.isEmpty() == false) {
            return;
        }

        //
        // Skip issues before 2/2017
        //
        if (dmElement.SUBMIT_DATE.getValue().isEarlierThen(EcvDate.getDate(2017, 2, 1))) {
            return;
        }

        //
        // Skip issues with affected issue
        //
        if (dmElement.AFFECTED_ISSUE.isElementSet() == true) {
            return;
        }

        //
        // Skip issues not in wanted project.
        //
        DmRq1Project project = dmElement.PROJECT.getElement();
        if ((DmRq1Project.PoolProject.VW.isInPoolProject(project) == false)
                && (DmRq1Project.PoolProject.JLR.isInPoolProject(project) == false)) {
            return;
        }

        //
        // Add warning
        //
        addMarker(dmElement, new Warning(this, "Missing affected issue comment", "No affected issue is set and not comment for affected issue is given."));

    }

}
