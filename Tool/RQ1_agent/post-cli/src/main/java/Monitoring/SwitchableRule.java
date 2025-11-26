/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.EnumSet;

/**
 * Implements SwitchableRuleI
 *
 * @author gug2wi
 */
public abstract class SwitchableRule extends Rule implements SwitchableRuleI {

    private EnumSet<RuleExecutionGroup> activeExecutionGroups = null;

    protected SwitchableRule(RuleDescription description) {
        super(description);
    }

    @Override
    final public synchronized void setRuleExecutionGroupActive(RuleExecutionGroup group) {
        assert (group != null);
        if (getRuleDescription().getExecutionGroups().contains(group) == true) {
            if (activeExecutionGroups == null) {
                activeExecutionGroups = EnumSet.noneOf(RuleExecutionGroup.class);
            }
            if (activeExecutionGroups.isEmpty() == true) {
                activateRule();
            }
            activeExecutionGroups.add(group);
        }
    }

    @Override
    final public synchronized void setRuleExecutionGroupActive(RuleExecutionGroup group, boolean activate) {
        assert (group != null);

        if (activate == true) {
            setRuleExecutionGroupActive(group);
        } else if (activeExecutionGroups != null) {
            if (activeExecutionGroups.contains(group) == true) {
                activeExecutionGroups.remove(group);
                if (activeExecutionGroups.isEmpty() == true) {
                    deactivateRule();
                }
            }
        }

    }

    @Override
    final public RuleExecutionGroup getMainRuleExecutionGroup() {
        return (getRuleDescription().getExecutionGroups().iterator().next());
    }

    protected abstract void activateRule();

    protected abstract void deactivateRule();
}
