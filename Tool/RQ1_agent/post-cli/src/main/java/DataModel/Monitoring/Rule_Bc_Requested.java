/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1BcRelease;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.DmMappedElement;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_RRM;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;
import util.EcvDate;

/**
 *
 * @author frt83wi
 */
public class Rule_Bc_Requested extends DmRule<DmRq1BcRelease> {

    static final private String warningTitle = "BC is not in requested state, 8 weeks before PVER planned delivery date (QAM ID-1.1.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "BC_05__REQUESTED",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.BC_PLANNING),
            warningTitle,
            "Implements the check for QAM ID-1.1.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if BC is not in requested state, 8 weeks before the planned date of the earliest mapped PVER/PVAR.\n");

    public Rule_Bc_Requested(DmRq1BcRelease bcRelease) {
        super(description, bcRelease);
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted BCRs
        // rule affects only new BCRs
        if (dmElement.LIFE_CYCLE_STATE.getValue() != LifeCycleState_Release.NEW) {
            return;
        }

        EcvDate firstPstPlannedDate = getFirstPstPlannedDate(dmElement);

        if (firstPstPlannedDate == null) {
            return;
        }

        // threshold for closure of planned or developed BCRs is 8 weeks before first PST planned date
        if (firstPstPlannedDate.addDays(-8 * 7).isInThePast()) {
            Warning warning = new Warning(this, warningTitle, "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " less than 8 weeks before planned date (" + firstPstPlannedDate.addDays(-8 * 7).toString() + ").");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }
    }

    private EcvDate getFirstPstPlannedDate(DmRq1BcRelease bcRelease) {

        EcvDate firstPstPlannedDate = null;

        for (DmMappedElement<DmRq1Rrm, DmRq1Pst> rrmPstMap : bcRelease.MAPPED_PST.getElementList()) {
            if (rrmPstMap.getMap().isInLifeCycleState(LifeCycleState_RRM.CANCELED) == false) {

                DmRq1Pst pst = rrmPstMap.getTarget();

                if (pst.isInLifeCycleState(LifeCycleState_Release.CANCELED) == false) {
                    EcvDate currentPstPlannedDate = pst.PLANNED_DATE.getValue();

                    if (firstPstPlannedDate == null) {
                        firstPstPlannedDate = currentPstPlannedDate;
                    } else if (firstPstPlannedDate.isLaterThen(currentPstPlannedDate)) {
                        firstPstPlannedDate = currentPstPlannedDate;
                    }
                }
            }
        }
        return firstPstPlannedDate;
    }
}
