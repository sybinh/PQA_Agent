/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
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
public class Rule_Irm_Prg_HintForExclude extends DmRule<DmRq1Irm_Pst_IssueSw> {

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
            + "The rule is only checked for IRM that are not canceled or conflicted."
    );

    public Rule_Irm_Prg_HintForExclude(DmRq1Irm_Pst_IssueSw irm_Pver_IssueSW) {
        super(description, irm_Pver_IssueSW);
    }

    @Override
    public void executeRule() {

        //
        // Check only for IRM not canceled and not conflicted.
        //
        if (dmElement.isCanceledOrConflicted() == true) {
            return;
        }

        //
        // Check only if connected with PVAR or PVER
        //
        if (dmElement.PLANNING_ACTION.getValue() == PlanningAction.EXCLUDE) {
            addMarker(dmElement, new Hint(this, "Planning Action = EXCLUDE", dmElement.getTypeIdTitle())
                    .addAffectedElement(dmElement));
        }
    }
}
