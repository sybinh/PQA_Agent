/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
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
public class Rule_CheckDatesForPver extends DmRule<DmRq1PverRelease> {

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

    public Rule_CheckDatesForPver(DmRq1PverRelease pverRelease) {
        super(description, pverRelease);
    }

    @Override
    public void executeRule() {

        LifeCycleState_Release lifeCycleState = (LifeCycleState_Release) dmElement.LIFE_CYCLE_STATE.getValue();

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

        EcvDate planningFreeze = dmElement.PLANNING_FREEZE.getValue();

        if (planningFreeze.isEmpty()) {
            return;
        }

        if (planningFreeze.isInThePast()) {
            addMarker(dmElement, new Warning(this, "Release not in state Planned after Planning Freeze.",
                    dmElement.getTypeIdTitle() + "\n"
                    + "Planning Freeze: " + planningFreeze.toString() + "\n"
                    + "LifeCycleState: " + dmElement.getLifeCycleState().getText())
                    .addAffectedElement(dmElement));
        }
    }

    private void checkImplementationFreeze() {

        EcvDate implementationFreeze = dmElement.IMPLEMENTATION_FREEZE.getValue();

        if (implementationFreeze.isEmpty()) {
            return;
        }

        if (implementationFreeze.isInThePast()) {
            addMarker(dmElement, new Warning(this, "Release not in state Developed after Implementation Freeze.",
                    dmElement.getTypeIdTitle() + "\n"
                    + "Implementation Freeze: " + implementationFreeze.toString() + "\n"
                    + "LifeCycleState: " + dmElement.getLifeCycleState().getText())
                    .addAffectedElement(dmElement));
        }
    }

    private void checkPlannedDate() {

        EcvDate plannedDate = dmElement.PLANNED_DATE.getValue();

        if (plannedDate.isEmpty()) {
            addMarker(dmElement, new Warning(this, "Planned Date not set.",
                    dmElement.getTypeIdTitle() + "\n"
                    + "LifeCycleState: " + dmElement.getLifeCycleState().getText())
                    .addAffectedElement(dmElement));
            return;
        }

        if (plannedDate.isInThePast()) {
            addMarker(dmElement, new Warning(this, "Release not in state Closed after Planned Date.",
                    dmElement.getTypeIdTitle() + "\n"
                    + "Planned Date: " + plannedDate.toString() + "\n"
                    + "LifeCycleState: " + dmElement.getLifeCycleState().getText())
                    .addAffectedElement(dmElement));
        }
    }
}
