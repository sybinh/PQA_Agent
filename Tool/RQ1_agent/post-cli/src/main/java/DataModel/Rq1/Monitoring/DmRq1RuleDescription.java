/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import java.util.Set;

/**
 * Marks a rule as rule of the DataModel.
 * @author gug2wi
 */
public class DmRq1RuleDescription extends RuleDescription {

    public DmRq1RuleDescription(Set<RuleExecutionGroup> executionGroups, String title, String description) {
        super(executionGroups, title, description);
    }
    
}
