/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.YesNoEmpty;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Rrm_Pver_Bc_PilotSet extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Pilot set for RRM",
            "The field pilot has to be set to yes or no in the RRM between an PVAR/PVER and a BC.\n"
            + "\n"
            + "The warning \"Pilot not set.\" is set on the RRM if this is not the case.\n"
            + "\n"
            + "The rule is only checked for RRM that are not canceled."
    );

    final private DmRq1Rrm_Pst_Bc myRrm;

    public Rule_Rrm_Pver_Bc_PilotSet(DmRq1Rrm_Pst_Bc rrm_Pst_Bc) {
        super(description, rrm_Pst_Bc);
        this.myRrm = rrm_Pst_Bc;
    }

    @Override
    public void executeRule() {

        //
        // Check only for IRM not canceled and not conflicted.
        //
        if (myRrm.isCanceledOrConflicted() == true) {
            return;
        }

        //
        // Check only if connected with PVAR or PVER
        //
        if (myRrm.MAPPED_PARENT.getElement() instanceof DmRq1Pst) {

            if (EnumSet.of(YesNoEmpty.NO, YesNoEmpty.YES).contains(myRrm.IS_PILOT.getValue()) == false) {
                addMarker(myRrm, new Warning(this, "Pilot not set.", myRrm.getTypeIdTitle())
                        .addAffectedElement(myRrm));
            }
        }
    }
}
