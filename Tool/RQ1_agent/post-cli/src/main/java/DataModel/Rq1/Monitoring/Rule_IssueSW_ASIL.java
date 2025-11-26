/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmExcitedCpCRule;
import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Ipe.Annotations.EcvElementList;
import Monitoring.Failure;
import Monitoring.RuleExecutionGroup;
import Monitoring.RuleI;
import Monitoring.Warning;
import Rq1Data.Enumerations.AsilClassification;
import static Rq1Data.Enumerations.AsilClassification.ASIL_A;
import static Rq1Data.Enumerations.AsilClassification.ASIL_B;
import static Rq1Data.Enumerations.AsilClassification.ASIL_C;
import static Rq1Data.Enumerations.AsilClassification.ASIL_D;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class Rule_IssueSW_ASIL extends DmRule<DmRq1IssueSW> {

    public static class UnexpectedAsilLevel extends Failure {

        private UnexpectedAsilLevel(RuleI rule, String title, String description) {
            super(rule, title, description);
        }

    }

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_SW_10__ASIL",
            EnumSet.of(RuleExecutionGroup.I_SW_PLANNING, RuleExecutionGroup.EXCITED_CPC),
            "ASIL level of I-SW and I-FD.",
            "Implements the check for the ASIL level of an I-SW and it's child I-FD.\n"
            + "\n"
            + "The following is checked:\n"
            + "- The ASIL level of the I-SW has to be set. A warning is set on the I-SW, if this is not the case.\n"
            + "- At least one I-FD has to have a ASIL level high enought for the ASIL level of the I-SW. A warning is set on the I-SW, if this is not the case.\n"
            + "\n"
            + "The check is only done for I-SW in state Evaluated, Committed, and Implemented.");

    public Rule_IssueSW_ASIL(DmRq1IssueSW issueSW) {
        super(description, issueSW);
    }

    @Override
    public void executeRule() {

        //
        // Skip I-SW based on the state
        //
        if (dmElement.isInLifeCycleState(LifeCycleState_Issue.NEW,
                //                LifeCycleState_Issue.EVALUATED,
                LifeCycleState_Issue.CLOSED,
                LifeCycleState_Issue.CANCELED,
                LifeCycleState_Issue.CONFLICTED) == true) {
            return;
        }

        //
        // Check that a known ASIL level is set
        //
        AsilClassification asilOnI_SW = AsilClassification.get(dmElement.ASIL_CLASSIFICATION.getValueAsText());
        if (asilOnI_SW == null) {
            addMarker(dmElement, new Warning(this, "ASIL level missing for I-SW.", "The ASIL level set for the I-SW is not a valid one."));
            return;
        }

        //
        // Build list of levels needed on I-FD
        //
        List<AsilClassification> neededAsilOnI_FD = new ArrayList<>();
        switch (asilOnI_SW) {
            case QM:
                // No check necessary
                return;

            case ASIL_A:
            case ASIL_A_B:
            case ASIL_A_C:
            case ASIL_A_D:
                neededAsilOnI_FD.add(ASIL_A);
                neededAsilOnI_FD.add(ASIL_B);
                neededAsilOnI_FD.add(ASIL_C);
                neededAsilOnI_FD.add(ASIL_D);
                break;

            case ASIL_B:
            case ASIL_B_C:
            case ASIL_B_D:
                neededAsilOnI_FD.add(ASIL_B);
                neededAsilOnI_FD.add(ASIL_C);
                neededAsilOnI_FD.add(ASIL_D);
                break;

            case ASIL_C:
            case ASIL_C_D:
                neededAsilOnI_FD.add(ASIL_C);
                neededAsilOnI_FD.add(ASIL_D);
                break;

            case ASIL_D:
                neededAsilOnI_FD.add(ASIL_D);
                break;

            default:
                Failure f = new UnexpectedAsilLevel(this, "Unexpected ASIL level for I-SW planning rules.", "The ASIL level set for the I-SW is unknown.");
                addMarker(dmElement, f);
                f.logError();
                return;
        }

        //
        // No warning, if no I-FD is connected
        //
        if (dmElement.CHILDREN.getElementList().isEmpty() == true) {
            return;
        }

        //
        // Check if at least on I-FD has the necessary ASIL level
        //
        for (DmRq1IssueFD i_fd : dmElement.CHILDREN.getElementList()) {
            addDependency(i_fd);
            AsilClassification asilOnI_FD = AsilClassification.get(i_fd.ASIL_CLASSIFICATION.getValueAsText());
            if (neededAsilOnI_FD.contains(asilOnI_FD) == true) {
                // Everything o.k.
                return;
            }
        }

        //
        // No fitting ASIL level on I-FD
        //
        StringBuilder b = new StringBuilder();
        b.append("The ASIL level of the I-SW is ").append(asilOnI_SW.getText()).append("\n");
        b.append("\n");
        b.append("Therefore, at least on child I-FD has to have one of the following ASIL levels:\n");
        for (AsilClassification asil : neededAsilOnI_FD) {
            b.append(asil.getText()).append("\n");
        }
        addMarker(dmElement, new Warning(this, "ASIL level on I-FD not sufficient for I-SW", b.toString()));

    }
}
