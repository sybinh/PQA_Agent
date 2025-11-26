/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import Monitoring.RuleExecutionGroup;
import java.util.Set;

/**
 *
 * @author gug2wi
 */
public class DmExcitedCpCRule extends DmRq1RuleDescription {

    // Set link to rule set on a central place to enable easy change.
    final static public String seeQamRuleSet = "See http://fe-ecupkit.de.bosch.com/PDP/Procedure/toSW_QAMRuleSet.xlsm for the QAM rule set.";

    final private String cpcIdentifier;

    public DmExcitedCpCRule(String cpcIdentifier, Set<RuleExecutionGroup> executionGroups, String title, String description) {
        super(executionGroups, title, description);
        assert (cpcIdentifier != null);
        assert (cpcIdentifier.isEmpty() == false);

        this.cpcIdentifier = cpcIdentifier;
    }

    public String getCpcIdentifier() {
        return cpcIdentifier;
    }

}
