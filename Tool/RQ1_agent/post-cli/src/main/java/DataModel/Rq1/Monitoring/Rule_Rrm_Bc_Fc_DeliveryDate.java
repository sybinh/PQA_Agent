/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Fc;
import DataModel.Rq1.Records.DmRq1Rrm_Bc_Fc;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_RRM;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Rrm_Bc_Fc_DeliveryDate extends DmRule<DmRq1Rrm_Bc_Fc> {

//    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rule_Rrm_Bc_Fc_DeliveryDate.class.getCanonicalName());
    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.BC_PLANNING, RuleExecutionGroup.FC_PLANNING),
            "FC in time for BC",
            "Checks that a FC comes in time for a BC.\n"
            + "\n"
            + "The following warnings are set on the RRM:\n"
            + "a) 'No planned date set for FC.'\n"
            + "b) 'No planned date set for BC.'\n"
            + "c) 'FC planned date too late for BC requested delivery date.'\n"
            + "d) 'FC planned date too late for BC planned date.'\n"
            //            + "e) 'FC implementation freeze too late for BC requested implementation date.'\n"
            //            + "f) 'FC planned date too late for BC requested implementation date and no ImplementationFreeze set.'\n"
            + "\n"
            + "About d: Check is only done, if requested delivery date is not set in RRM.\n"
            //            + "About f: Check is only done, if BC implementation freeze is not set.\n"
            + "\n"
            + "This rule is only applied if the LifeCycleStates of the RRM is REQUESTED or PLANNED.");

    public Rule_Rrm_Bc_Fc_DeliveryDate(DmRq1Rrm_Bc_Fc myMap) {
        super(description, myMap);
    }

    @Override
    public void executeRule() {
        //
        // Do not check if element is conflicted.
        //
        if (dmElement.isInLifeCycleState(LifeCycleState_RRM.NEW, LifeCycleState_RRM.IMPLEMENTED, LifeCycleState_RRM.CANCELED, LifeCycleState_RRM.CONFLICTED)) {
            return;
        }

        DmRq1Bc bc = dmElement.MAPPED_PARENT.getElement();
        DmRq1Fc fc = dmElement.MAPPED_CHILDREN.getElement();

        addDependency(bc);
        addDependency(fc);

        if (fc.isInLifeCycleState(LifeCycleState_Release.CLOSED)) {
            return;
        }

        if (bc.isConflicted() || fc.isConflicted()) {
            return;
        }

        // a)
        if (fc.PLANNED_DATE.getValue().isEmpty() == true) {
            addMarker(dmElement, new Warning(this, "No planned date set for FC.", "No planned date set for FC."));
            return;
        }

        // b)
        if (bc.PLANNED_DATE.getValue().isEmpty() == true) {
            addMarker(dmElement, new Warning(this, "No planned date set for BC.", "No planned date set for BC."));
            return;
        }

        if (dmElement.REQUESTED_DELIVERY_DATE.getValue().isEmpty() == false) {
            // c)
            if (fc.PLANNED_DATE.getValue().isLaterThen(dmElement.REQUESTED_DELIVERY_DATE.getValue())) {
                addMarker(dmElement, new Warning(this, "FC planned date too late for BC requested delivery date.",
                        "PlannedDate of FC: " + fc.PLANNED_DATE.getValue().toString() + "\n"
                        + "RequestedDeliveryDate in RRM: " + dmElement.REQUESTED_DELIVERY_DATE.getValue().toString()));
            }
        } else {
            // d)
            if (fc.PLANNED_DATE.getValue().isLaterThen(bc.PLANNED_DATE.getValue())) {
                addMarker(dmElement, new Warning(this, "FC planned date too late for BC planned date.",
                        "PlannedDate of FC: " + fc.PLANNED_DATE.getValue().toString() + "\n"
                        + "PlannedDate of BC: " + bc.PLANNED_DATE.getValue().toString()));
            }
        }

//        if (rrm_Bc_Fc.REQUESTED_IMPLEMENTATION_DATE.getValue().isEmpty() == false) {
//            if (fc.IMPLEMENTATION_FREEZE.getValue().isEmpty() == false) {
//                if (fc.IMPLEMENTATION_FREEZE.getValue().isLaterThen(rrm_Bc_Fc.REQUESTED_IMPLEMENTATION_DATE.getValue())) {
//                    addMarker(rrm_Bc_Fc, new Warning(this, "FC implementation freeze too late for BC requested implementation date.",
//                            "Implementation Freeze of FC: " + fc.IMPLEMENTATION_FREEZE.getValue().toString() + "\n"
//                            + "Requested implementation date of BC: " + rrm_Bc_Fc.REQUESTED_IMPLEMENTATION_DATE.getValue().toString()));
//                }
//            } else {
//                if (fc.PLANNED_DATE.getValue().isLaterThen(rrm_Bc_Fc.REQUESTED_IMPLEMENTATION_DATE.getValue())) {
//                    addMarker(rrm_Bc_Fc, new Warning(this, "FC planned date too late for BC requested implementation date and no ImplementationFreeze set.",
//                            "Planned date of FC: " + fc.PLANNED_DATE.getValue().toString() + "\n"
//                            + "Requested implementation date of BC: " + rrm_Bc_Fc.REQUESTED_IMPLEMENTATION_DATE.getValue().toString()));
//                }
//            }
//        }
    }
}
