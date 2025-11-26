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
import DataModel.Rq1.Records.DmRq1PstReleaseI;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.DmMappedElement;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author sas82wi
 */
public class Rule_Bc_Requested_FourWeeks extends DmRule<DmRq1BcRelease> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.BC_PLANNING),
            "BC Check Requested state",
            "Creates a warning if BC is not in requested state, 4 weeks before PVER planned delivery date\n");

    public Rule_Bc_Requested_FourWeeks(DmRq1BcRelease bcRelease) {
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

        // threshold for closure of planned or developed BCRs is 4 weeks before first PST planned date
        if (firstPstPlannedDate.addDays(-4 * 7).isInThePast()) {
            Warning warning = new Warning(this, "BCR not requested 4 weeks before planned date.", "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " less than 4 weeks before planned date (" + firstPstPlannedDate.addDays(-4 * 7).toString() + ").");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }
    }

    private EcvDate getFirstPstPlannedDate(DmRq1BcRelease bcRelease) {
        List<DmMappedElement<DmRq1Rrm, DmRq1Pst>> rrmPstMapList = bcRelease.MAPPED_PST.getElementList();

        // if RRM-PST map list is NOT empty
        if (!rrmPstMapList.isEmpty()) {

            EcvDate firstPstPlannedDate = null;

            for (DmMappedElement<DmRq1Rrm, DmRq1Pst> rrmPstMap : rrmPstMapList) {
                DmRq1Release pst = rrmPstMap.getTarget();

                if (!(pst instanceof DmRq1PstReleaseI)) {
                    continue;
                }

                EcvDate currentPstPlannedDate = pst.PLANNED_DATE.getValue();

                if (firstPstPlannedDate == null) {
                    firstPstPlannedDate = currentPstPlannedDate;
                } else {
                    if (firstPstPlannedDate.isLaterThen(currentPstPlannedDate)) {
                        firstPstPlannedDate = currentPstPlannedDate;
                    }
                }
            }

            return firstPstPlannedDate;
        }

        // if RRM-PST map list is empty, return null
        return null;
    }
}
