/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1FcRelease;
import DataModel.Rq1.Records.DmRq1Rrm_Bc_Fc;
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
 * @author sas82wi
 */
public class Rule_Fc_PlannedDate extends DmRule<DmRq1FcRelease> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.FC_PLANNING),
            "FC: Check dates of mapped PVERs", "Creates a warning if planned date of FC later than requested delivery date of any mapped BC");

    public Rule_Fc_PlannedDate(DmRq1FcRelease myRelease) {
        super(description, myRelease);
    }

    @Override
    public void executeRule() {

        if (dmElement.isInLifeCycleState(LifeCycleState_Release.CANCELED, LifeCycleState_Release.DEVELOPED, LifeCycleState_Release.CONFLICTED, LifeCycleState_Release.CLOSED) == true) {
            return;
        }

        for (DmMappedElement<DmRq1Rrm_Bc_Fc, DmRq1Bc> mappedBc : dmElement.MAPPED_BC.getElementList()) {
            System.out.println(dmElement.getRq1Id());

            DmRq1Rrm_Bc_Fc rrm = mappedBc.getMap();
            DmRq1Bc bc = mappedBc.getTarget();

            addDependency(rrm);

            //
            // Do not check for RRM in state CANCELED or CONFLICTED
            //
            if (rrm.isInLifeCycleState(LifeCycleState_RRM.CANCELED, LifeCycleState_RRM.CONFLICTED)) {
                continue;
            }

            //EcvDate bcPlannedDate = bc.getPlannedDate();
            EcvDate bcPlannedDate = rrm.REQUESTED_DELIVERY_DATE.getValue();

            if ((bcPlannedDate != null) && (bcPlannedDate.isEmpty() == false)) {
                if (bcPlannedDate.isEarlierThen(dmElement.PLANNED_DATE.getValue()) == true) {
                    addMarker(dmElement, new Warning(this, "FC planned date too late for BC planned date",
                            "Planned date of FC later than planned date date of " + bc.getElementType() + "-" + bc.getRq1Id()));
                    System.out.println("Warning: " + dmElement.getRq1Id());
                    break;
                }
            }
        }

    }

}
