/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Monitoring;

import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import java.util.Set;

/**
 *
 * @author GUG2WI
 */
public class DmDoorsRuleDescription extends RuleDescription {

    public DmDoorsRuleDescription(Set<RuleExecutionGroup> executionGroups, String title, String description) {
        super(executionGroups, title, description);
    }

}
