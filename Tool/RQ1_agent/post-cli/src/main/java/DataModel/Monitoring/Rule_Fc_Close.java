/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1FcRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;

/**
 * excITED Rule 2.5.0
 *
 * @author frt83wi
 */
public class Rule_Fc_Close extends DmRule<DmRq1FcRelease> {

    static final private String warningTitle = "Release is not closed after planned date for FC (QAM ID-2.5.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "FC_02__CLOSE",
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY, RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.FC_PLANNING),
            warningTitle,
            "Implements the check for QAM ID-2.5.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if the FC ist not closed after its planned date.\n"
            + "\n"
            + "The rule is not executed for FC in state Canceled and Conflicted."
    );

    public Rule_Fc_Close(DmRq1FcRelease fcRelease) {
        super(description, fcRelease);
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted FCRs
        if (dmElement.isCanceledOrConflicted()) {
            return;
        }

        // threshold for closure of planned or developed FCRs is planned date
        if (dmElement.PLANNED_DATE.getValue().isInThePast()
                && (dmElement.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.PLANNED
                || dmElement.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.REQUESTED
                || dmElement.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.DEVELOPED)) {

            Warning warning = new Warning(this, warningTitle, "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " after planned date (" + dmElement.PLANNED_DATE.getValueAsText() + ").");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }
    }
}
