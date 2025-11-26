/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Monitoring;

import Monitoring.Rule;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Warning_LinkToDoorsMissing extends Warning {

    static final public RuleDescription ruleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Highlight missing links from RQ1 to DOORS.",
            "The rule checks, if a RQ1 object that is referenced by a DOORS object has also a reference back to the DOORS object.");

    static final public Rule rule = new Rule(ruleDescription);

    public Warning_LinkToDoorsMissing(String doorsId) {
        super(rule, "Link to " + doorsId + " missing.");
        setDescription(doorsId + " has a reference to this RQ1 object, but the reference from the Rq1 object back to " + doorsId + " is missing.");
    }

}
