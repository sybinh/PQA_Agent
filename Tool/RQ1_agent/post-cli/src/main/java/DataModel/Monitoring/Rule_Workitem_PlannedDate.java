/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.PlanningMethod;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Workitem_PlannedDate extends DmRule<DmRq1WorkItem> {

    static final private String warningTitle = "Workitem is not closed after planned date (QAM ID-2.6.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            warningTitle,
            "Implements the check for QAM ID-2.6.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if the workitem is not closed after the planned date.\n"
            + "\n"
            + "The rule is not executed for workitem in state Canceled and Conflicted."
    );

    public Rule_Workitem_PlannedDate(DmRq1WorkItem myRelease) {
        super(description, myRelease);
    }

    @Override
    public void executeRule() {
        //
        // Do not check if element is conflicted.
        // When PlannedMethod is set to flow no PlannedDate is needed
        //
        if (dmElement.isClosed() || dmElement.isCanceled() || dmElement.isConflicted() || dmElement.PLANNING_METHOD.getValue().equals(PlanningMethod.FLOW)) {
            return;
        }

        if (dmElement.getPlannedDate().isEmpty()) {
            //
            // Planned date not set
            //
            addMarker(dmElement, new Warning(this, "Planned date for Workitem missing.",
                    dmElement.getTypeIdTitle() + "\n"
                    + "LifeCycleState: " + dmElement.getLifeCycleState().getText())
                    .addAffectedElement(dmElement));
        } else if (dmElement.getPlannedDate().isInThePast()) {
            //
            // Overdue
            //
            addMarker(dmElement, new Warning(this, warningTitle,
                    dmElement.getTypeIdTitle() + "\n"
                    + "PlannedDate: " + dmElement.getPlannedDate().toString() + "\n"
                    + "LifeCycleState: " + dmElement.getLifeCycleState().getText())
                    .addAffectedElement(dmElement));
        }

    }
}
