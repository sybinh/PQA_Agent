/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1HistoryLog;
import DataModel.Rq1.Records.DmRq1IssueFD;
import Ipe.Annotations.EcvElementList;
import Monitoring.SwitchableRuleI;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;

/**
 *
 * @author frt83wi
 */
public class Rule_IssueFD_Evaluation extends DmRule<DmRq1IssueFD> {

    // TODO: Warning class to transmit warningId
    static public class ExcitedCpcWarning extends Warning {

        public ExcitedCpcWarning(SwitchableRuleI rule, String title, String description) {
            super(rule, title, description);
        }

        public ExcitedCpcWarning(SwitchableRuleI rule, String title) {
            super(rule, title);
        }
    }

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_FD_01__EVALUATION",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.I_FD_PLANNING),
            "Check Evaluation",
            "Creates a warning if I-FD not evaluated in time");

    public Rule_IssueFD_Evaluation(DmRq1IssueFD issueFD) {
        super(description, issueFD);
    }

    @Override
    public void executeRule() {

        // rule is NOT for cancelled or conflicted IFDs
        // rule affects only new IFDs
        if (dmElement.LIFE_CYCLE_STATE.getValue() != LifeCycleState_Issue.NEW) {
            return;
        }

        // retrieve evaluation date and forward date
        EcvDate evaluationDate = dmElement.EVALUATION_REQUIRED_BY.getValue();
        EcvDate forwardDate = getForwardDate(dmElement);
        EcvDate submitDate = dmElement.SUBMIT_DATE.getValue();

        // if evaluation date is provided
        if ((evaluationDate != null) && (!evaluationDate.isEmpty())) {

            // threshold for evaluation is after evaluation date
            if (evaluationDate.isInThePast()) {
                Warning warning = new Warning(this, "Not evaluated after evaluation date.", "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " after evaluation date (" + evaluationDate.toString() + ").");
                warning.addAffectedElement(dmElement);
                addMarker(dmElement, warning);
            }
        } // if evaluation date is NOT provided
        else {

            // if forward date is available
            if (forwardDate != null) {

                // threshold for evaluation is 5 business days (equal 7 days in any situation) after forward date
                if (forwardDate.addDays(7).isInThePast()) {
                    Warning warning = new Warning(this, "Not evaluated after forward date plus 5 working business days.", "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " after forward date plus 5 working business days (" + forwardDate.addDays(7).toString() + ").");
                    warning.addAffectedElement(dmElement);
                    addMarker(dmElement, warning);
                }
            } // if forward date is NOT available
            else {

                // threshold for evaluation is 5 business days (equal 7 days in any situation) after submit date
                if (submitDate.addDays(7).isInThePast()) {
                    Warning warning = new Warning(this, "Not evaluated after submit date plus 5 working business days.", "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + " after submit date plus 5 working business days (" + submitDate.addDays(7).toString() + ").");
                    warning.addAffectedElement(dmElement);
                    addMarker(dmElement, warning);
                }
            }
        }
    }

    private EcvDate getForwardDate(DmRq1IssueFD issueFD) {
        List<DmRq1HistoryLog> historyLogs = issueFD.HAS_HISTORY_LOGS.getElementList();

        // if history logs are NOT empty
        if (!historyLogs.isEmpty()) {

            EcvDate forwardDate = null;

            // loop through history logs
            for (DmRq1HistoryLog historyLog : historyLogs) {

                // if logged action is a forward action
                if (historyLog.ACTION_NAME.getValue().equals("Forward")) {

                    // set last modified date as forward date
                    try {
                        forwardDate = EcvDate.parseOslcValue(historyLog.LAST_MODIFIED_DATE.getValue().getRq1Value());
                    } catch (EcvDate.DateParseException ex) {
                        Logger.getLogger(Rule_IssueFD_Evaluation.class.getName()).log(Level.SEVERE, "Forward date could not be set to history log last modified date.", ex);
                        ToolUsageLogger.logError(Rule_IssueFD_Evaluation.class.getName(), ex);
                    }
                }
            }

            return forwardDate;
        }

        // if history logs are empty, return null
        return null;
    }
}
