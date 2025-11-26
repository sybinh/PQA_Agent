/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1IssueFD;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.EnumSet;

/**
 *
 * @author sas82wi
 */
public class Rule_IssueFD_WithoutLinkToBc extends DmRule {
    
    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE, RuleExecutionGroup.I_FD_PLANNING),
            "I-FD: Check BC link",
            "Creates a warning if I-FD is not linked to any BC.");
    
    final private DmRq1IssueFD issueFD;
    
    public Rule_IssueFD_WithoutLinkToBc(DmRq1IssueFD issueFD) {
        super(description, issueFD);
        assert (issueFD != null);
        this.issueFD = issueFD;
    }

    @Override
    public void executeRule() {
        //
        // Check this rule only if status is evaluated or later
        //
        if (issueFD.isInLifeCycleState(LifeCycleState_Issue.EVALUATED) != true &&
                issueFD.isInLifeCycleState(LifeCycleState_Issue.COMMITTED)!= true &&
                issueFD.isInLifeCycleState(LifeCycleState_Issue.IMPLEMENTED)!= true ) {
            return;
        }

        if (issueFD.MAPPED_BC.getElementList().isEmpty() == true) {
            addMarker(issueFD, new Warning(this, "I-FD not linked to any BC.", "I-FD not linked to any BC."));
        }
    }
    
}
