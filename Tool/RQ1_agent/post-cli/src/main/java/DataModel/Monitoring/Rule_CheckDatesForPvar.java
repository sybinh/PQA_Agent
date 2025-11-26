/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1PvarRelease;
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
public class Rule_CheckDatesForPvar extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Check dates for PVAR.",
            "The dates for PVAR are checked against the following rules:\n"
            + "\n"
            + "For life cycle state REQUESTED:\n"
            + "- If a Planning Freeze is set, then it has to be in the future or today.\n"
            + "- Planned Dates must be set and at least one planned date has to be in the future or today.\n"
            + "\n"
            + "For life cycle state PLANNED:\n"
            + "- Planned Dates must be set and at least one planned date has to be in the future or today.\n"
            + "\n"
            + "For life cycle state DEVELOPED:\n"
            + "- Planned Dates must be set and at least one planned date has to be in the future or today.\n"
            + "\n"
            + "For life cycle states NEW, CLOSED, CANCELLED and CONFLICTED:\n"
            + "- No dates are checked."
    );

    final private DmRq1PvarRelease myRelease;

    public Rule_CheckDatesForPvar(DmRq1PvarRelease myRelease) {
        super(description, myRelease);
        this.myRelease = myRelease;
    }

    @Override
    public void executeRule() {

        LifeCycleState_Release lifeCycleState = (LifeCycleState_Release) myRelease.LIFE_CYCLE_STATE.getValue();

        switch (lifeCycleState) {
            case NEW:
                break;
            case REQUESTED:
                checkPlanningFreeze();
                checkPlannedDate(myRelease);
                break;
            case PLANNED:
                checkPlannedDate(myRelease);
                break;
            case DEVELOPED:
                checkPlannedDate(myRelease);
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

    void checkPlannedDate(DmRq1PvarRelease pvar) {

        EcvDate latestPlannedDate = pvar.PLANNED_DATE.getValue();

        if (latestPlannedDate.isEmpty()) {
            addMarker(pvar, new Warning(this, "Planned Date not set.",
                    pvar.getTypeIdTitle() + "\n"
                    + "LifeCycleState: " + pvar.getLifeCycleState().getText())
                    .addAffectedElement(pvar));
            return;
        }

        for (EcvDate plannedDateDerivative : pvar.PLANNED_DATE_DERIVATIVES.getValue().getDates().values()) {
            latestPlannedDate = latestPlannedDate.getLatest(plannedDateDerivative);
        }

        if (latestPlannedDate.isInThePast()) {
            addMarker(pvar, new Warning(this, "Release not in state Closed after Latest Planned Date.",
                    pvar.getTypeIdTitle() + "\n"
                    + "Latest Planned Date: " + latestPlannedDate.toString() + "\n"
                    + "LifeCycleState: " + pvar.getLifeCycleState().getText())
                    .addAffectedElement(pvar));
        }
    }
}
