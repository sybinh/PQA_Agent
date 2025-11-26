/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.EnumSet;
import util.EcvEnumeration;

/**
 *
 * @author frt83wi, sas82wi
 */
public class Rule_IssueFD_Commit extends DmRule<DmRq1IssueFD> {

    static final private String warningTitle = "I-FD is not committed, eventhough attached I-SW is committed (QAM ID-2.4.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_FD_04__COMMIT",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.I_FD_PLANNING),
            warningTitle,
            "Implements the check for QAM ID-2.4.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if I-FD is not committed, even though I-SW is committed.\n"
            + "\n"
            + "The rule is not executed for I-FD in state Canceled and Conflicted."
    );

    public Rule_IssueFD_Commit(DmRq1IssueFD issueFD) {
        super(description, issueFD);
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted IFDs
        if (dmElement.isCanceledOrConflicted()) {
            return;
        }

        EcvEnumeration ifdLcs = dmElement.LIFE_CYCLE_STATE.getValue();

        // rule affects IFDs with life cycle state before "COMMITTED", "IMPLEMENTED" and "CLOSED"
        if (ifdLcs == LifeCycleState_Issue.COMMITTED || ifdLcs == LifeCycleState_Issue.IMPLEMENTED || ifdLcs == LifeCycleState_Issue.CLOSED) {
            return;
        }

        DmRq1Issue parent = dmElement.PARENT.getElement();

        if (parent instanceof DmRq1IssueSW) {
            DmRq1IssueSW issueSW = (DmRq1IssueSW) parent;

            if (issueSW.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Issue.COMMITTED) {
                Warning warning = new Warning(this, warningTitle, "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + ", even though I-SW is committed.");
                warning.addAffectedElement(dmElement);
                addMarker(dmElement, warning);
            }
        }
    }
}
