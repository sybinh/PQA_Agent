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
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Pver_Derivatives extends DmRule<DmRq1PverRelease> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Derivatives for PVER",
            "No derivatives are allowed for PVER-Releases.\n"
            + "\n"
            + "The warning 'No derivatives allowed.' is set on the PVER, if derivatives are defined.\n"
            + "\n"
            + "Note: The check is only done for releases that are not canceled and not closed.");

    public Rule_Pver_Derivatives(DmRq1PverRelease myRelease) {
        super(description, myRelease);
    }

    @Override
    public void executeRule() {

        //
        // CLOSED, CANCELED: no check for derivatives.
        //
        // Note: Do the check also for state NEW. Otherwise the Lumpensammler will never be checked.
        //
        if (dmElement.isCanceledOrConflicted() || dmElement.isClosed()) {
            return;
        }

        if (dmElement.DERIVATIVES.getValue().isEmpty() == false) {
            addMarker(dmElement, new Warning(this, "No derivatives allowed.",
                    "No derivatives are allowed for PVER-Releases."));
        }
    }
}
