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
import Rq1Data.Enumerations.LifeCycleState_WorkItem;
import java.util.EnumSet;

/**
 *
 * @author frt83wi
 */
public class Rule_Workitem_Started extends DmRule<DmRq1WorkItem> {

    //
    // This rule is enforced by RQ1. Therefore, it is not needed in the Planning Rules for Workitems
    //
    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "Workitem_02__STARTED",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC),
            "Workitem Check Started",
            "Creates a warning if no planned date for workitem entered although workitem is in started date.\n");

    public Rule_Workitem_Started(DmRq1WorkItem workItem) {
        super(description, workItem);
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted WIs
        // rule affects only started WIs
        if (dmElement.LIFE_CYCLE_STATE.getValue() != LifeCycleState_WorkItem.STARTED) {
            return;
        }

        if (dmElement.getPlannedDate().isEmpty()) {
            Warning warning = new Warning(this, "Planned date for Workitem missing.", "LifeCycleState: " + dmElement.getLifeCycleState().getText());
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }
    }
}
