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
import Ipe.Annotations.EcvElementList;
import Monitoring.Hint;
import Monitoring.RuleExecutionGroup;
import Rq1Data.Enumerations.PlanningAction;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Irm_Prg_HintForExclude extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Hint for Planning Action Exclude",
            "Checks if the Planning Action is Exclude.\n"
            + "\n"
            + " The Hint \"Planning Action = EXCLUDE\" is set on the IRM, if this is the case.\n"
            + "\n"
            + "This is done to highlight excluded I-SW."
            + "\n"
            + "The rule is only checked for IRM that are not canceled."
    );

    final private DmRq1Irm_Pst_IssueSw myIrm;

    public Rule_Irm_Prg_HintForExclude(DmRq1Irm_Pst_IssueSw irm_Pver_IssueSW) {
        super(description, irm_Pver_IssueSW);
        this.myIrm = irm_Pver_IssueSW;
    }

    @Override
    public void executeRule() {

        //
        // Check only for IRM not canceled and not conflicted.
        //
        if (myIrm.isCanceledOrConflicted() == true) {
            return;
        }

        //
        // Check only if connected with PVAR or PVER
        //
        if (myIrm.PLANNING_ACTION.getValue() == PlanningAction.EXCLUDE) {
            addMarker(myIrm, new Hint(this, "Planning Action = EXCLUDE", myIrm.getTypeIdTitle())
                    .addAffectedElement(myIrm));
        }
    }
}
