/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Irm_Bc_IssueFd;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.DmMappedElement;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_IRM;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;
import java.util.List;
import util.EcvEnumeration;

/**
 *
 * @author frt83wi, sas82wi
 */
public class Rule_IssueFD_Close extends DmRule<DmRq1IssueFD> {

    static final private String warningTitle = "I-FD is not closed, even though all the BC mapped to it are closed or cancelled (QAM ID-2.2.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_FD_02__CLOSE",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.I_FD_PLANNING),
            warningTitle,
            "Implements the check for QAM ID-2.2.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if I-FD is not closed, even though all of the following conditions are met.\n"
            + "\n"
            + "1) All IRM are qualified or canceled.\n"
            + "2) All mapped BC are closed or canceled."
            + "\n"
            + "The rule is not executed for I-FD in state Cancelled and Conflicted."
    );

    public Rule_IssueFD_Close(DmRq1IssueFD issueFD) {
        super(description, issueFD);
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted IFDs
        if (dmElement.isCanceledOrConflicted()) {
            return;
        }

        // rule affects IFDs with life cycle state before "CLOSED"
        if (dmElement.LIFE_CYCLE_STATE.getValue() == LifeCycleState_Issue.CLOSED) {
            return;
        }

        List<DmMappedElement<DmRq1Irm, DmRq1Release>> irms = dmElement.HAS_MAPPED_RELEASES.getElementList();

        // rule affects IFDs for which all IRMs are qualified or canceled
        for (DmMappedElement<DmRq1Irm, DmRq1Release> irm : irms) {
            EcvEnumeration irmLcs = irm.getMap().LIFE_CYCLE_STATE.getValue();

            if (irmLcs != LifeCycleState_IRM.QUALIFIED && irmLcs != LifeCycleState_IRM.CANCELED) {
                return;
            }
        }

        List<DmMappedElement<DmRq1Irm_Bc_IssueFd, DmRq1Bc>> bcs = dmElement.MAPPED_BC.getElementList();

        // rule affects IFDs which are already linked to BCRs
        if (bcs.isEmpty()) {
            return;
        }

        // rule affects IFDs for which all BCRs are closed or canceled
        for (DmMappedElement<DmRq1Irm_Bc_IssueFd, DmRq1Bc> bc : bcs) {
            EcvEnumeration bcLcs = bc.getTarget().LIFE_CYCLE_STATE.getValue();

            if (bcLcs != LifeCycleState_Release.CLOSED && bcLcs != LifeCycleState_Release.CANCELED) {
                return;
            }
        }

        Warning warning = new Warning(this, warningTitle, "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + ", even though all the BC mapped to it are closed or cancelled.");
        warning.addAffectedElement(dmElement);
        addMarker(dmElement, warning);
    }
}
