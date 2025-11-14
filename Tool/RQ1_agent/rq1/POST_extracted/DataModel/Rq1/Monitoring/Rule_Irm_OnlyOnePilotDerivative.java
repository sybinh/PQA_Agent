/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_IRM;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Types.Rq1DerivativeMapping;
import Rq1Data.Types.Rq1DerivativeMapping.Mode;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author GUG2WI
 */
public class Rule_Irm_OnlyOnePilotDerivative extends DmRule<DmRq1Irm_Pst_IssueSw> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Only one pilot derivative in IRM.",
            "Checks that only one derivative is set as pilot in an IRM between PVER/PVAR and I-SW.\n"
            + "\n"
            + "A warning is created if more than one pilot derivate exists.\n"
            + "\n"
            + "Note:\n"
            + "- The check is only done for IRM that are not canceled, not implemented and not qualified.\n"
            + "- The check is only done if isPilot in the IRM is set to yes and the Exchange Workflow is not empty.");

    public Rule_Irm_OnlyOnePilotDerivative(DmRq1Irm_Pst_IssueSw irm) {
        super(description, irm);
    }

    @Override
    protected void executeRule() {

        //
        // CLOSED, CANCELED: no check
        //
        if (dmElement.isInLifeCycleState(LifeCycleState_IRM.CANCELED, LifeCycleState_IRM.QUALIFIED, LifeCycleState_IRM.IMPLEMENTED)) {
            return;
        }

        //
        // IRM is not pilot: no check
        //
        if (dmElement.IS_PILOT.getValue() != YesNoEmpty.YES) {
            return;
        }

        //
        // No exchange workflow: no check
        //
        if (dmElement.EXCHANGE_WORKFLOW.isEmpty()) {
            return;
        }

        //
        // Count pilot derivatives
        //
        Map<String, Rq1DerivativeMapping.Mode> derivativeMap = dmElement.MAPPING_TO_DERIVATIVES.getDerivativeMapping().getMapping();
        List<String> pilotDerivatives = new ArrayList<>();
        for (Map.Entry<String, Rq1DerivativeMapping.Mode> m : derivativeMap.entrySet()) {
            if (m.getValue() == Mode.PILOT) {
                pilotDerivatives.add(m.getKey());
            }
        }

        //
        // Set warning if more than one derivative is pilot.
        //
        if (pilotDerivatives.size() > 1) {
            String problem = "The following derivatives are set as pilot derivative:\n\n";
            for (String derivative : pilotDerivatives) {
                problem += derivative + "\n";
            }
            problem += "\nBut only one derivative is allowed as pilot.";

            addMarker(dmElement, new Warning(this, "More than one pilot derivatives", problem));
        }
    }

}
