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
import util.EcvDate;
import util.EcvEnumeration;

/**
 *
 * @author frt83wi, sas82wi
 */
public class Rule_IssueFD_Implementation extends DmRule<DmRq1IssueFD> {

    static final private String warningTitle = "I-FD is not implemented or closed, after planned dated of BC (QAM ID-2.3.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_FD_03__IMPLEMENTATION",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.I_FD_PLANNING),
            warningTitle,
            "Implements the check for QAM ID-2.3.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if the following conditions are met.\n"
            + "\n"
            + "1) I-FD is in state Commit.\n"
            + "2) The planned date of at least on mapped BC is in the past."
    );

    public Rule_IssueFD_Implementation(DmRq1IssueFD issueFD) {
        super(description, issueFD);
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted IFDs
        if (dmElement.isCanceledOrConflicted()) {
            return;
        }

        EcvEnumeration ifdLcs = dmElement.LIFE_CYCLE_STATE.getValue();

        // rule affects only IFDs with life cycle state "COMITTED"
        if (ifdLcs != LifeCycleState_Issue.COMMITTED) {
            return;
        }

        List<DmMappedElement<DmRq1Irm, DmRq1Release>> irms = dmElement.HAS_MAPPED_RELEASES.getElementList();

        EcvDate firstPlannedDate = getFirstPlannedDate(irms);

        if (firstPlannedDate == null) {
            return;
        }

        // threshold for implementation is first planned date
        if (firstPlannedDate.isInThePast()) {
            Warning warning = new Warning(this, warningTitle, "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " after first planned date (" + firstPlannedDate.toString() + ").");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }
    }

    private EcvDate getFirstPlannedDate(List<DmMappedElement<DmRq1Irm, DmRq1Release>> irms) {
        EcvDate firstPlannedDate = null;

        // loop through IRMs
        for (DmMappedElement<DmRq1Irm, DmRq1Release> irm : irms) {

            // rule does NOT affect IRMs which are canceled
            if (irm.getMap().LIFE_CYCLE_STATE.getValue() != LifeCycleState_IRM.CANCELED) {
                DmRq1Release release = irm.getTarget();

                // rule affects only planned or developed BCRs
                if ((release instanceof DmRq1Bc) && release.isInLifeCycleState(LifeCycleState_Release.PLANNED, LifeCycleState_Release.REQUESTED, LifeCycleState_Release.DEVELOPED)) {

                    EcvDate currentPlannedDate = release.PLANNED_DATE.getValue();

                    // rule ignores planned dates before 2015-01-01
                    if (currentPlannedDate.isEarlierThen(EcvDate.getDate(2015, 01, 01))) {
                        continue;
                    }

                    if (firstPlannedDate == null) {
                        firstPlannedDate = currentPlannedDate;
                    } else {
                        if (firstPlannedDate.isLaterThen(currentPlannedDate)) {
                            firstPlannedDate = currentPlannedDate;
                        }
                    }
                }
            }
        }

        return firstPlannedDate;
    }
}
