/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1PverRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;
import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public class Rule_CheckDatesForPver extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Check dates for PVER.",
            "The dates for PVER are checked against the following rules:\n"
            + "\n"
            + "For life cycle state REQUESTED:\n"
            + "- If a Planning Freeze is set, then it has to be in the future or today.\n"
            + "- If a Implementation Freeze is set, then it has to be in the future or today.\n"
            + "- Planned Date must be set and in the future or today.\n"
            + "\n"
            + "For life cycle state PLANNED:\n"
            + "- If a Implementation Freeze is set, then it has to be in the future or today.\n"
            + "- Planned Date must be set and in the future or today.\n"
            + "\n"
            + "For life cycle state DEVELOPED:\n"
            + "- Planned Date must be set and in the future or today.\n"
            + "\n"
            + "For life cycle states NEW, CLOSED, CANCELLED and CONFLICTED:\n"
            + "- No dates are checked.\n"
    );

    final private DmRq1PverRelease myRelease;

    public Rule_CheckDatesForPver(DmRq1PverRelease pverRelease) {
        super(description, pverRelease);
        this.myRelease = pverRelease;
    }

    @Override
    public void executeRule() {

        LifeCycleState_Release lifeCycleState = (LifeCycleState_Release) myRelease.LIFE_CYCLE_STATE.getValue();

        switch (lifeCycleState) {
            case NEW:
                break;
            case REQUESTED:
                checkPlanningFreeze();
                checkImplementationFreeze();
                checkPlannedDate();
                break;
            case PLANNED:
                checkImplementationFreeze();
                checkPlannedDate();
                break;
            case DEVELOPED:
                checkPlannedDate();
                break;
            case CLOSED:
                break;
            case CANCELED:
                break;
            case CONFLICTED:
                break;
        }
    }

    private void checkPlanningFreeze() {

        EcvDate planningFreeze = myRelease.PLANNING_FREEZE.getValue();

        if (planningFreeze.isEmpty()) {
            return;
        }

        if (planningFreeze.isInThePast()) {
            addMarker(myRelease, new Warning(this, "Release not in state Planned after Planning Freeze.",
                    myRelease.getTypeIdTitle() + "\n"
                    + "Planning Freeze: " + planningFreeze.toString() + "\n"
                    + "LifeCycleState: " + myRelease.getLifeCycleState().getText())
                    .addAffectedElement(myRelease));
        }
    }

    private void checkImplementationFreeze() {

        EcvDate implementationFreeze = myRelease.IMPLEMENTATION_FREEZE.getValue();

        if (implementationFreeze.isEmpty()) {
            return;
        }

        if (implementationFreeze.isInThePast()) {
            addMarker(myRelease, new Warning(this, "Release not in state Developed after Implementation Freeze.",
                    myRelease.getTypeIdTitle() + "\n"
                    + "Implementation Freeze: " + implementationFreeze.toString() + "\n"
                    + "LifeCycleState: " + myRelease.getLifeCycleState().getText())
                    .addAffectedElement(myRelease));
        }
    }

    private void checkPlannedDate() {

        EcvDate plannedDate = myRelease.PLANNED_DATE.getValue();

        if (plannedDate.isEmpty()) {
            addMarker(myRelease, new Warning(this, "Planned Date not set.",
                    myRelease.getTypeIdTitle() + "\n"
                    + "LifeCycleState: " + myRelease.getLifeCycleState().getText())
                    .addAffectedElement(myRelease));
            return;
        }

        if (plannedDate.isInThePast()) {
            addMarker(myRelease, new Warning(this, "Release not in state Closed after Planned Date.",
                    myRelease.getTypeIdTitle() + "\n"
                    + "Planned Date: " + plannedDate.toString() + "\n"
                    + "LifeCycleState: " + myRelease.getLifeCycleState().getText())
                    .addAffectedElement(myRelease));
        }
    }
}
