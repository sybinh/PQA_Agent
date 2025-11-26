/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmExcitedCpCRule;
import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1BcRelease;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1PstReleaseI;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
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
 * @author sas82wi, gug2wi
 */
public class Rule_Bc_CheckPstDates extends DmRule<DmRq1BcRelease> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "BC__PLANNED_DATE",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.BC_PLANNING),
            "BC, check dates of mapped PVERs", "Creates a warning if planned date of BC later than requested delivery date of any mapped PVER or PVAR");

    public Rule_Bc_CheckPstDates(DmRq1BcRelease myRelease) {
        super(description, myRelease);
    }

    @Override
    public void executeRule() {
        //
        // Do not check for life cycle state CANCELED, DEVELOPED, CONFLICTED or CLOSED
        //
        if (dmElement.isInLifeCycleState(LifeCycleState_Release.CANCELED, LifeCycleState_Release.DEVELOPED, LifeCycleState_Release.CONFLICTED, LifeCycleState_Release.CLOSED) == true) {
            return;
        }

        //
        // Run through all PST mappings
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Pst> mappedPst : dmElement.MAPPED_PST.getElementList()) {

            //Don't check collections
            if (mappedPst.getMap() instanceof DmRq1Rrm_Pst_Bc || mappedPst.getTarget() instanceof DmRq1PstReleaseI) {
                DmRq1Rrm rrm = mappedPst.getMap();
                DmRq1Pst pst = mappedPst.getTarget();

                addDependency(rrm);

                //
                // Do not check for RRM in state CANCELED or CONFLICTED
                //
                if (rrm.isInLifeCycleState(LifeCycleState_RRM.CANCELED, LifeCycleState_RRM.CONFLICTED)) {
                    continue;
                }

                EcvDate rrmReqDate = rrm.REQUESTED_DELIVERY_DATE.getValue();

                //
                // Check if date from RRM is set, if yes, then check if planned date of BC is later than requested delivery date  
                //
                if ((rrmReqDate != null) && (rrmReqDate.isEmpty() == false)) {
                    if (rrmReqDate.isEarlierThen(dmElement.PLANNED_DATE.getValue()) == true) {
                        addMarker(dmElement, new Warning(this, "BC too late for PST",
                                "Planned date of BC later than requested delivery date of " + pst.getElementType() + "-" + pst.getRq1Id()));
                    }
                }
            }
        }
    }
}
