/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
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
public class Rule_Rrm_Pst_Bc_DeliveryDate extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.BC_PLANNING),
            "BC in time for PST",
            "Checks that a BC comes in time for a PST (PVER or PVAR).\n"
            + "\n"
            + "The following warnings are set on the RRM:\n"
            + "a) 'No planned date set for BC.'\n"
            + "b) 'No planned date set for PST.'\n"
            + "c) 'BC planned date too late for PST requested delivery date.'\n"
            + "d) 'BC planned date too late for PST planned date.'\n"
            + "e) 'BC implementation freeze too late for PST requested implementation date.'\n"
            + "f) 'BC planned date too late for PST requested implementation date and no ImplementationFreeze set.'\n"
            + "\n"
            + "About d: Check is only done, if requested delivery date is not set in RRM.\n"
            + "About f: Check is only done, if BC implementation freeze is not set.\n"
            + "\n"
            + "This rule is only applied if the LifeCycleStates of the RRM is REQUESTED or PLANNED.");

    final private DmRq1Rrm_Pst_Bc myRrm;

    /*
     Wie soll ich reagieren, wenn RRM.RequestedDeliveryDate nach PVER.PlannedDate. Diese Situation könnte ungewollt beim verschieben von PlannedDate passieren.
     */
    public Rule_Rrm_Pst_Bc_DeliveryDate(DmRq1Rrm_Pst_Bc myMap) {
        super(description, myMap);
        this.myRrm = myMap;
    }

    @Override
    public void executeRule() {

        if (myRrm.isInLifeCycleState(LifeCycleState_RRM.NEW, LifeCycleState_RRM.IMPLEMENTED, LifeCycleState_RRM.CANCELED, LifeCycleState_RRM.CONFLICTED)) {
            return;
        }

        DmRq1Bc bc = myRrm.getMappedBc();
        DmRq1Pst prg = myRrm.getMappedPst();

        addDependency(bc);
        addDependency(prg);
        
        if ( bc.isInLifeCycleState(LifeCycleState_Release.CLOSED)){
            return;
        }

        if (bc.PLANNED_DATE.getValue().isEmpty() == true) {
            addMarker(myRrm, new Warning(this, "No planned date set for BC.", "No planned date set for BC."));
            return;
        }

        if (prg.PLANNED_DATE.getValue().isEmpty() == true) {
            addMarker(myRrm, new Warning(this, "No planned date set for PST.", "No planned date set for PST."));
            return;
        }

        if (myRrm.REQUESTED_DELIVERY_DATE.getValue().isEmpty() == false) {
            if (bc.PLANNED_DATE.getValue().isLaterThen(myRrm.REQUESTED_DELIVERY_DATE.getValue())) {
                addMarker(myRrm, new Warning(this, "BC planned date too late for PST requested delivery date.",
                                             "PlannedDate of BC: " + bc.PLANNED_DATE.getValue().toString() + "\n"
                                             + "RequestedDeliveryDate in RRM: " + myRrm.REQUESTED_DELIVERY_DATE.getValue().toString()));
            }
        } else {
            if (bc.PLANNED_DATE.getValue().isLaterThen(prg.PLANNED_DATE.getValue())) {
                addMarker(myRrm, new Warning(this, "BC planned date too late for PST planned date.",
                                             "PlannedDate of BC: " + bc.PLANNED_DATE.getValue().toString() + "\n"
                                             + "PlannedDate of PST: " + prg.PLANNED_DATE.getValue().toString()));
            }
        }

        if (myRrm.REQUESTED_IMPLEMENTATION_DATE.getValue().isEmpty() == false) {
            if (bc.IMPLEMENTATION_FREEZE.getValue().isEmpty() == false) {
                if (bc.IMPLEMENTATION_FREEZE.getValue().isLaterThen(myRrm.REQUESTED_IMPLEMENTATION_DATE.getValue())) {
                    addMarker(myRrm, new Warning(this, "BC implementation freeze too late for PST requested implementation date.",
                                                 "Implementation Freeze of BC: " + bc.IMPLEMENTATION_FREEZE.getValue().toString() + "\n"
                                                 + "Requested implementation date of PST: " + myRrm.REQUESTED_IMPLEMENTATION_DATE.getValue().toString()));
                }
            } else {
                if (bc.PLANNED_DATE.getValue().isLaterThen(myRrm.REQUESTED_IMPLEMENTATION_DATE.getValue())) {
                    addMarker(myRrm, new Warning(this, "BC planned date too late for PST requested implementation date and no ImplementationFreeze set.",
                                                 "Planned date of BC: " + bc.PLANNED_DATE.getValue().toString() + "\n"
                                                 + "Requested implementation date of PST: " + myRrm.REQUESTED_IMPLEMENTATION_DATE.getValue().toString()));
                }
            }
        }
    }
}
