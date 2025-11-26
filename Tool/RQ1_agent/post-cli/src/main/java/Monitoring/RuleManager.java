/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Implements {@link RuleManagerI}. Intended to be used by all classes
 * implementing {@link RuleManagerI}. Either as sub class or via aggregation.
 *
 * @author gug2wi
 */
public class RuleManager extends Markable implements RuleManagerI {

    final private EnumSet<RuleExecutionGroup> activeGroups;
    final private List<SwitchableRuleI> rules;
    final private EnumSet<RuleExecutionGroup> switchableGroups;

    protected RuleManager() {
        rules = new ArrayList<>();
        switchableGroups = EnumSet.noneOf(RuleExecutionGroup.class);
        //
        // Activate default group
        //
        activeGroups = EnumSet.of(RuleExecutionGroup.RQ1_DATA);
    }

    @Override
    final public void setRuleExecutionGroupActive(RuleExecutionGroup group) {
        assert (group != null);
        setRuleExecutionGroupActive(group, true);
    }

    @Override
    final public void setRuleExecutionGroupActive(RuleExecutionGroup group, boolean activate) {
        assert (group != null);
        synchronized (activeGroups) {
            if (activate == true) {
                if (activeGroups.contains(group) == false) {
                    activeGroups.add(group);
                    for (SwitchableRuleI r : rules) {
                        r.setRuleExecutionGroupActive(group, true);
                    }
                }
            } else if (activeGroups.contains(group) == true) {
                activeGroups.remove(group);
                for (SwitchableRuleI r : rules) {
                    r.setRuleExecutionGroupActive(group, false);
                }
            }
        }
    }

    @Override
    public boolean isRuleExecutionGroupActive(RuleExecutionGroup group) {
        return (activeGroups.contains(group));
    }

    final protected void addRule(SwitchableRuleI newRule) {
        assert (newRule != null);
        rules.add(newRule);
    }

    final protected void removeRule(SwitchableRuleI oldRule) {
        rules.remove(oldRule);
    }

    final protected void addSwitchableGroup(EnumSet<RuleExecutionGroup> newGroups) {
        switchableGroups.addAll(newGroups);
    }

    @Override
    final public EnumSet<RuleExecutionGroup> getSwitchableGroups() {
        return (switchableGroups);
    }

    @Override
    final public void setSwitchableGroupActive(RuleExecutionGroup group) {
        assert (group != null);
        setSwitchableGroupActive(group, true);
    }

    @Override
    public void setSwitchableGroupActive(RuleExecutionGroup group, boolean activate) {
        assert (group != null);
        setRuleExecutionGroupActive(group, activate);
        if (switchableGroups.contains(group) == true) {
            setSwitchableGroupActiveForSubElements(group, activate);
        }
    }

    protected void setSwitchableGroupActiveForSubElements(RuleExecutionGroup group, boolean activate) {
        // Standard implementation does nothing.
    }

}
