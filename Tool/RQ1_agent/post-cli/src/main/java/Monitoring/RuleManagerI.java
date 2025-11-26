/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public interface RuleManagerI {

    EnumSet<RuleExecutionGroup> getSwitchableGroups();

    void setRuleExecutionGroupActive(RuleExecutionGroup group);

    void setRuleExecutionGroupActive(RuleExecutionGroup group, boolean activate);
    
    boolean isRuleExecutionGroupActive(RuleExecutionGroup group);

    void setSwitchableGroupActive(RuleExecutionGroup group);

    void setSwitchableGroupActive(RuleExecutionGroup group, boolean activate);

}
