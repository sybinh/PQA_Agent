/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
import Ipe.Annotations.EcvElementList;
import Monitoring.Hint;
import Monitoring.RuleExecutionGroup;
import Rq1Data.Enumerations.IntegrationAction;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Rrm_Pst_Bc_HintForExclude extends DmRule<DmRq1Rrm_Pst_Bc> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Hint for Integration Action Remove",
            "Checks if the Integration Action is Remove.\n"
            + "\n"
            + " The Hint \"Integration Action = REMOVE\" is set on the RRM, if this is the case.\n"
            + "\n"
            + "This is done to highlight removed BC."
            + "\n"
            + "The rule is only checked for RRM that are not canceled or conflicted."
    );

    public Rule_Rrm_Pst_Bc_HintForExclude(DmRq1Rrm_Pst_Bc rrm) {
        super(description, rrm);
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
        if (dmElement.INTEGRATION_ACTION.getValue() == IntegrationAction.REMOVE) {
            addMarker(dmElement, new Hint(this, "Integration Action = REMOVE", dmElement.getTypeIdTitle())
                    .addAffectedElement(dmElement));
        }
    }
}
