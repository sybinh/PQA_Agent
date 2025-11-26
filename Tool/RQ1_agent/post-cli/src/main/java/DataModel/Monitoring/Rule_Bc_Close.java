/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1BcRelease;
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
public class Rule_Bc_Close extends DmRule {

    static final private String warningTitle = "Release is not closed after planned date for BC (QAM ID-2.5.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "BC_04__CLOSE",
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY, RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.BC_PLANNING),
            warningTitle,
            "Implements the check for QAM ID-2.5.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if the BC ist not closed after its planned date.\n"
            + "\n"
            + "The rule is not executed for BC in state Canceled and Conflicted."
    );

    private final DmRq1BcRelease bcRelease;

    public Rule_Bc_Close(DmRq1BcRelease bcRelease) {
        super(description, bcRelease);
        assert (bcRelease != null);
        this.bcRelease = bcRelease;
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted BCRs
        if (bcRelease.isCanceledOrConflicted()) {
            return;
        }

        // threshold for closure of planned or developed BCRs is planned date
        if (bcRelease.PLANNED_DATE.getValue().isInThePast()
                && (bcRelease.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.PLANNED
                || bcRelease.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.REQUESTED
                || bcRelease.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.DEVELOPED)) {

            Warning warning = new Warning(this, warningTitle, "LifeCycleState " + bcRelease.LIFE_CYCLE_STATE.getValueAsText() + " after planned date (" + bcRelease.PLANNED_DATE.getValueAsText() + ").");
            warning.addAffectedElement(bcRelease);
            addMarker(bcRelease, warning);
        }
    }
}
