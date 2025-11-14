/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
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
public class Rule_Release_Predecessor extends DmRule<DmRq1Release> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROJECT,
                    RuleExecutionGroup.BC_PLANNING,
                    RuleExecutionGroup.FC_PLANNING),
            // The rules is not used for PVER and PVAR. For those releases a view rule exists.
            "Valid Predecessor",
            "Checks that the predecessor of a release is valid.\n"
            + "\n"
            + "The following warnings are set on the release:\n"
            + "a) 'Planned date of predecessor too late.' ... If the planned date of the predessor is later then the planned date of the current release.\n"
            + "b) 'Predecessor in state Canceled or Conflicted.' ... If the predecessor is Cancelled or Conflicted\n"
            + "\n"
            + "The check is not done for releases in state New, Closed and Cancelled.\n"
            + "No warning is set, if no predecessor exists.");

    public Rule_Release_Predecessor(DmRq1Release myRelease) {
        super(description, myRelease);
    }

    public void executeRule() {
        //
        // NEW, CLOSED, CANCELED, CONFLICTED: no check
        //
        if (dmElement.isInLifeCycleState(LifeCycleState_Release.NEW) || dmElement.isCanceledOrConflicted() || dmElement.isClosed()) {
            return;
        }

        EcvEnumeration lifeCycleState = dmElement.getLifeCycleState();
        EcvDate plannedDate = dmElement.getPlannedDate();

        //
        // Check only, if predecessor is set
        //
        DmRq1Release predecessor = dmElement.PREDECESSOR.getElement();
        if (predecessor == null) {
            return;
        }
        super.addDependency(predecessor);

        //
        // Check state of predecessor
        //
        EcvEnumeration lcsOfPredecessor = predecessor.getLifeCycleState();
        if ((lcsOfPredecessor == LifeCycleState_Release.CANCELED) || (lcsOfPredecessor == LifeCycleState_Release.CONFLICTED)) {
            addMarker(dmElement, new Warning(this,
                    "Predecessor in state Canceled or Conflicted.",
                    dmElement.getTypeIdTitle() + "\n"
                    + "LifeCycleState of predecessor: " + lcsOfPredecessor.getText())
                    .addAffectedElement(dmElement));
        }

        //
        // Check planned date
        //
        EcvDate plannedDateOfPredecessor = predecessor.getPlannedDate();
        if ((plannedDateOfPredecessor.isEmpty() == false)
                && (plannedDate.isEmpty() == false)
                && (plannedDate.isEarlierThen(plannedDateOfPredecessor))) {
            addMarker(dmElement, new Warning(this,
                    "Planned date of predecessor too late.",
                    "Planned Date: " + plannedDate.toString() + "\n"
                    + "Planned date of predecessor: " + plannedDateOfPredecessor.toString())
                    .addAffectedElement(dmElement));
        }

    }
}
