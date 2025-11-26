/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1Pst;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.YesNoEmpty;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Irm_Prg_IssueSw_PilotSet extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Pilot set for IRM",
            "The field pilot has to be set to yes or no in the IRM between an PVAR/PVER and an I-SW.\n"
            + "\n"
            + "The warning \"Pilot not set.\" is set on the IRM if this is not the case.\n"
            + "\n"
            + "The rule is only checked for IRM that are not canceled."
    );
    
    final private DmRq1Irm_Pst_IssueSw myIrm;

    public Rule_Irm_Prg_IssueSw_PilotSet(DmRq1Irm_Pst_IssueSw irm_Pver_IssueSW) {
        super(description, irm_Pver_IssueSW);
        this.myIrm = irm_Pver_IssueSW;
    }

    @Override
    public void executeRule() {

        //
        // Check only for IRM not canceled.
        //
        if (myIrm.isCanceledOrConflicted() == true) {
            return;
        }

        //
        // Check only if connected with PVAR or PVER
        //
        if (myIrm.HAS_MAPPED_RELEASE.getElement() instanceof DmRq1Pst) {

            if (EnumSet.of(YesNoEmpty.NO, YesNoEmpty.YES).contains(myIrm.IS_PILOT.getValue()) == false) {
                addMarker(myIrm, new Warning(this, "Pilot not set.", myIrm.getTypeIdTitle())
                        .addAffectedElement(myIrm));
            }
        }
    }
}
