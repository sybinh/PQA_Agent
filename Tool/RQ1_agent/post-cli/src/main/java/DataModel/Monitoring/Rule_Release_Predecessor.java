/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Release;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;
import util.EcvDate;
import util.EcvEnumeration;

/**
 *
 * @author gug2wi
 */
public class Rule_Release_Predecessor extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROJECT,
                       RuleExecutionGroup.BC_PLANNING,
                       RuleExecutionGroup.FC_PLANNING),
            "Valid Predecessor",
            "Checks that the predecessor of a release is valid.\n"
            + "\n"
            + "The following warnings are set on the release:\n"
            + "a) 'Planned date of predecessor too late.' ... If the planned date of the predessor is later then the planned date of the current release.\n"
            + "b) 'Predecessor in state Canceled or Conflicted.' ... If the predecessor is Cancelled or Conflicted\n"
            + "\n"
            + "The check is not done for releases in state New, Closed and Cancelled.\n"
            + "No warning is set, if no predecessor exists.");

    final private DmRq1Release myRelease;

    public Rule_Release_Predecessor(DmRq1Release myRelease) {
        super(description, myRelease);
        this.myRelease = myRelease;
    }

    public void executeRule() {
        //
        // NEW, CLOSED, CANCELED, CONFLICTED: no check
        //
        if (myRelease.isInLifeCycleState(LifeCycleState_Release.NEW) || myRelease.isCanceledOrConflicted() || myRelease.isClosed()) {
            return;
        }

        EcvEnumeration lifeCycleState = myRelease.getLifeCycleState();
        EcvDate plannedDate = myRelease.getPlannedDate();

        //
        // Check only, if predecessor is set
        //
        DmRq1Release predecessor = myRelease.PREDECESSOR.getElement();
        if (predecessor == null) {
            return;
        }
        super.addDependency(predecessor);

        //
        // Check state of predecessor
        //
        EcvEnumeration lcsOfPredecessor = predecessor.getLifeCycleState();
        if ((lcsOfPredecessor == LifeCycleState_Release.CANCELED) || (lcsOfPredecessor == LifeCycleState_Release.CONFLICTED)) {
            addMarker(myRelease, new Warning(this,
                                             "Predecessor in state Canceled or Conflicted.",
                                             myRelease.getTypeIdTitle() + "\n"
                                             + "LifeCycleState of predecessor: " + lcsOfPredecessor.getText())
                      .addAffectedElement(myRelease));
        }

        //
        // Check planned date
        //
        EcvDate plannedDateOfPredecessor = predecessor.getPlannedDate();
        if ((plannedDateOfPredecessor.isEmpty() == false)
            && (plannedDate.isEmpty() == false)
            && (plannedDate.isEarlierThen(plannedDateOfPredecessor))) {
            addMarker(myRelease, new Warning(this,
                                             "Planned date of predecessor too late.",
                                             "Planned Date: " + plannedDate.toString() + "\n"
                                             + "Planned date of predecessor: " + plannedDateOfPredecessor.toString())
                      .addAffectedElement(myRelease));
        }

    }
}
