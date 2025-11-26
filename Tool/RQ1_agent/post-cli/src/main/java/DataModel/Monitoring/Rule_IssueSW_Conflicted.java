/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author frt83wi
 */
public class Rule_IssueSW_Conflicted extends DmRule<DmRq1IssueSW> {

    static final private String warningTitle = "Issue/Release/Workitem is still in \"Conflicted\" state (QAM ID-1.3.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_SW_06__CONFLICTED",
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE, RuleExecutionGroup.EXCITED_CPC),
            warningTitle,
            "Implements the check for QAM ID-1.3.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning if the Issue is in \"Conflicted\" state and the date in "
            + "LifeCycleComment is either missing or in the past");

    public Rule_IssueSW_Conflicted(DmRq1IssueSW issueSW) {
        super(description, issueSW);
    }

    @Override
    public void executeRule() {

        // rule affects only conflicted I-SWs
        if (dmElement.LIFE_CYCLE_STATE.getValue() != LifeCycleState_Issue.CONFLICTED) {
            return;
        }

        // retrieve last due date from life cycle state comment field
        EcvDate lastDueDate = getLastDueDate(dmElement.LIFE_CYCLE_STATE_COMMENT.getValue());

        if (lastDueDate == null) {
            Warning warning = new Warning(this, "No due date found in life cycle state comment field. (QAM ID-1.3.0)", "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " I-SW does not contain a due date in life cycle state comment field.");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
            return;
        }

        if (lastDueDate.isInThePast()) {
            Warning warning = new Warning(this, "Due date found in life cycle state comment field is in the past. (QAM ID-1.3.0) ", "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " I-SW due date in life cycle state comment field is in the past.");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }
    }

    private EcvDate getLastDueDate(String lifeCycleStateComment) {
        List<EcvDate> ecvDateList = EcvDate.findDatesInString(lifeCycleStateComment);

        if (ecvDateList.isEmpty()) {
            return null;
        }

        return ecvDateList.get(ecvDateList.size() - 1);
    }
}
