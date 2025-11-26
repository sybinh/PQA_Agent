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
import Rq1Data.Enumerations.LifeCycleState_WorkItem;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;
import util.EcvEnumeration;

/**
 *
 * @author frt83wi
 */
public class Rule_Workitem_Close extends DmRule<DmRq1WorkItem> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "Workitem_03__CLOSE",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC),
            "Workitem Check Closure",
            "Creates a warning if workitem is not yet closed although planned date is in the past.\n");

    public Rule_Workitem_Close(DmRq1WorkItem workItem) {
        super(description, workItem);
    }

    @Override
    public void executeRule() {

        EcvEnumeration wiLcs = dmElement.LIFE_CYCLE_STATE.getValue();

        // rule is NOT for canceled or conflicted WIs
        // rule affects only new and started WIs
        if (wiLcs != LifeCycleState_WorkItem.STARTED && wiLcs != LifeCycleState_WorkItem.NEW) {
            return;
        }

        if (dmElement.getPlannedDate().isInThePast()) {
            Warning warning = new Warning(this, "Planned date in the past but Workitem not yet closed.", "PlannedDate: " + dmElement.getPlannedDate().toString() + "LifeCycleState: " + dmElement.getLifeCycleState().getText());
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }
    }
}
