/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Monitoring;

import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Warning_LinkToRq1Missing extends Warning {

    static final public RuleDescription ruleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Highlight missing links from DOORS to RQ1.",
            "The rule checks, if a DOORS object that is referenced by a RQ1 object has also a reference back to the RQ1 object.");

    final public static class Rule extends Monitoring.Rule {

        public Rule() {
            super(ruleDescription);
        }

    }

    public Warning_LinkToRq1Missing(Rule rule, String rq1Id) {
        super(rule, "Link to " + rq1Id + " missing.");
        setDescription(rq1Id + " has a reference to this DOORS object, but the reference from the DOORS object back to " + rq1Id + " is missing.");
    }

}
