/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
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
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Pver_Derivatives extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Derivatives for PVER",
            "No derivatives are allowed for PVER-Releases.\n"
            + "\n"
            + "The warning 'No derivatives allowed.' is set on the PVER, if derivatives are defined.\n"
            + "\n"
            + "Note: The check is only done for releases that are not canceled and not closed.");

    final private DmRq1PverRelease myRelease;

    public Rule_Pver_Derivatives(DmRq1PverRelease myRelease) {
        super(description, myRelease);
        this.myRelease = myRelease;
    }

    @Override
    public void executeRule() {

        //
        // CLOSED, CANCELED: no check for derivatives.
        //
        // Note: Do the check also for state NEW. Otherwise the Lumpensammler will never be checked.
        //
        if (myRelease.isCanceledOrConflicted() || myRelease.isClosed()) {
            return;
        }

        if (myRelease.DERIVATIVES.getValue().isEmpty() == false) {
            addMarker(myRelease, new Warning(this, "No derivatives allowed.",
                    "No derivatives are allowed for PVER-Releases."));
        }
    }
}
