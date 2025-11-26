/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1BcRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Bc_WithoutLinkToPst extends DmRule<DmRq1BcRelease> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.BC_PLANNING),
            "BC without mapping to PVER or PFAM.",
            "Checks that BC is mapped to a PVER or PFAM.\n"
            + "The warning 'BC not linked to any PVER/PFAM.' is set on the BC, if no mapping exists.\n"
            + "\n"
            + "Note:\n"
            + "This check is only performed for BCs in life cycle state 'Requested' and 'Planned'.");

    public Rule_Bc_WithoutLinkToPst(DmRq1BcRelease myRelease) {
        super(description, myRelease);
    }

    @Override
    public void executeRule() {
        //
        // Do not check if element is conflicted.
        //
        if (dmElement.isConflicted() == true) {
            return;
        }

        if (dmElement.MAPPED_PST.getElementList().isEmpty() == true) {
            addMarker(dmElement, new Warning(this, "BC not linked to any PVER/PFAM.", "BC not linked to any PVER/PFAM."));
        }
    }

}
