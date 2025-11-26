/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.EnumSet;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvEnumeration;
import DataModel.Rq1.Records.DmRq1HistoryLog;
import java.util.List;
import java.util.ArrayList;
import util.EcvDate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sas82wi
 */
public class Rule_IssueFD_NotCommited extends DmRule<DmRq1IssueFD> {

    static final private String warningTitle = "Issue-FD not commited 5 or more working days after attached Issue-SW was commited (QAM ID-2.8.0)";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_FD__NOTCOMMITED",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC, RuleExecutionGroup.I_FD_PLANNING),
            warningTitle,
            "Implements the check for QAM ID-2.8.0. " + DmExcitedCpCRule.seeQamRuleSet + "\n"
            + "\n"
            + "Creates a warning an Issue-FD is not commited 5 or "
            + "more working days after attached Issue-SW was commited");

    public Rule_IssueFD_NotCommited(DmRq1IssueFD issueFD) {
        super(description, issueFD);
    }

    @Override
    public void executeRule() {
        EcvDate latestModifiedDate = EcvDate.getEmpty();
        EcvDate tempDate = EcvDate.getEmpty();
        ArrayList<String> dates = new ArrayList<>();

        // rule is NOT for canceled or conflicted IFDs
        if (dmElement.isCanceledOrConflicted()) {
            return;
        }

        EcvEnumeration ifdLcs = dmElement.LIFE_CYCLE_STATE.getValue();

        // rule affects IFDs with life cycle state before "COMMITTED", "IMPLEMENTED" and "CLOSED"
        if (ifdLcs == LifeCycleState_Issue.COMMITTED || ifdLcs == LifeCycleState_Issue.IMPLEMENTED || ifdLcs == LifeCycleState_Issue.CLOSED) {
            return;
        }

        DmRq1Issue parent = dmElement.PARENT.getElement();

        if (parent instanceof DmRq1IssueSW) {
            DmRq1IssueSW issueSW = (DmRq1IssueSW) parent;
            List<DmRq1HistoryLog> historyLogs = issueSW.HAS_HISTORY_LOGS.getElementList();

            // if history logs are NOT empty
            if (!historyLogs.isEmpty()) {

                //Just check history of issueSWs in lifecycle COMMITTED to save execution time 
                if ((issueSW.LIFE_CYCLE_STATE.getValue().toString().equals("Committed"))) {

                    // loop through history logs
                    for (DmRq1HistoryLog historyLog : historyLogs) {

                        if (!historyLog.PREVIOUS_LIFE_CYCLE_STATE.getValue().equals("Committed") && historyLog.LIFE_CYCLE_STATE.getValue().equals("Committed")) {
                            //add date on which lifecycle state of I-SW was set to "COMMITTED" to a list which will be checked for the latest date after  
                            dates.add(historyLog.LAST_MODIFIED_DATE.getValue().getRq1Value());
                        }
                    }

                    /*check list of dates for latest changing date - Strings used for solution of this problem because trying to evaluate the latest modified date with 
                    EcvDate variables failed due to an unknown reason.*/
                    try {
                        tempDate = EcvDate.parseOslcValue(dates.get(0));
                    } catch (EcvDate.DateParseException ex) {
                        Logger.getLogger(Rule_IssueFD_NotCommited.class.getName()).log(Level.SEVERE, "Temp date could not be set to history.", ex);
                        ToolUsageLogger.logError(Rule_IssueFD_NotCommited.class.getName(), ex);
                    }
                    try {
                        latestModifiedDate = EcvDate.parseOslcValue(dates.get(0));
                    } catch (EcvDate.DateParseException ex) {
                        Logger.getLogger(Rule_IssueFD_NotCommited.class.getName()).log(Level.SEVERE, "Latest modified date could not be set to history.", ex);
                        ToolUsageLogger.logError(Rule_IssueFD_NotCommited.class.getName(), ex);
                    }
                    for (String date : dates) {
                        try {
                            tempDate = EcvDate.parseOslcValue(date);
                        } catch (EcvDate.DateParseException ex) {
                            Logger.getLogger(Rule_IssueFD_NotCommited.class.getName()).log(Level.SEVERE, "Temp modified date could not be set to history.", ex);
                            ToolUsageLogger.logError(Rule_IssueFD_NotCommited.class.getName(), ex);
                        }
                        if (tempDate.isLaterThen(latestModifiedDate)) {
                            try {
                                latestModifiedDate = EcvDate.parseOslcValue(date);
                            } catch (EcvDate.DateParseException ex) {
                                Logger.getLogger(Rule_IssueFD_NotCommited.class.getName()).log(Level.SEVERE, "Latest modified date could not be set to history.", ex);
                                ToolUsageLogger.logError(Rule_IssueFD_NotCommited.class.getName(), ex);
                            }
                        }
                    }

                    if ((latestModifiedDate.addDays(7).isInThePast())) {
                        addMarker(dmElement, new Warning(this, "I-FD not committed in time (QAM ID-2.8.0)",
                                "I-FD not in state COMMITTED 5 or more working days after attached I-SW was set to COMMITTED"));
                    }
                }
            }
        }
    }
}
