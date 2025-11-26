/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Monitoring;

import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import java.util.Set;

/**
 *
 * @author CNI83WI
 */
public class DmAlmRuleDescription extends RuleDescription {
    
    public DmAlmRuleDescription(Set<RuleExecutionGroup> executionGroups, String title, String description) {
        super(executionGroups, title, description);
    }
    
}
