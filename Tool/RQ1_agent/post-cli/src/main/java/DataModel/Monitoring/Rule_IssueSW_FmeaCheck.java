/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
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
import Rq1Data.Enumerations.FmeaState;
import java.util.EnumSet;
import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public class Rule_IssueSW_FmeaCheck extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Comment for not required FMEA on I-SW.",
            "A comment for the FMEA has to be set, if 'FMEA State' is set to 'Not Required'.\n"
            + "This comment can be written into the field 'FMEA Comment' or 'FMEA Change Comment'.\n"
            + "\n"
            + "If the both comment fields are empty and 'FMEA State' is 'Not Required', the warning 'Comment for not required FMEA missing.' is set on the I-SW.\n"
            + "\n"
            + "The test is only done, if the I-SW is not canceled and the submit date of the issue is greater or equal 1.1.2015.");
    
    final private DmRq1IssueSW myIssueSw;

    public Rule_IssueSW_FmeaCheck(DmRq1IssueSW issueSW) {
        super(description, issueSW);
        this.myIssueSw = issueSW;
    }

    @Override
    public void executeRule() {

        if ((myIssueSw.isCanceledOrConflicted() == false) && (myIssueSw.SUBMIT_DATE.getValue().isLaterOrEqualThen(EcvDate.getDate(2015, 1, 1)))) {

            if (myIssueSw.FMEA_STATE.getValue() == FmeaState.NOT_REQUIRED) {
                if ((myIssueSw.FMEA_COMMENT.isEmpty() == true) && (myIssueSw.FMEA_CHANGE_COMMENT.isEmpty() == true)) {
                    addMarker(myIssueSw, new Warning(this, "Comment for not required FMEA missing.", "A comment has to be set when FMEA state is set to 'Not Required'."));
                }
            }
        }

    }
}
