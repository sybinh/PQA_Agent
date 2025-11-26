/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.Set;

/**
 * Holds the description for a rule.
 * @author GUG2WI
 */
public class RuleDescription {

    final private Set<RuleExecutionGroup> executionGroups;
    final private String title;
    final private String description;

    public RuleDescription(Set<RuleExecutionGroup> executionGroups, String title, String description) {
        assert (executionGroups != null);
        assert (executionGroups.isEmpty() == false);
        assert (title != null);
        assert (title.isEmpty() == false);
        assert (description != null);
        assert (description.isEmpty() == false);

        this.executionGroups = executionGroups;
        this.title = title;
        this.description = description;
    }

    final public Set<RuleExecutionGroup> getExecutionGroups() {
        return (executionGroups);
    }

    final public String getTitle() {
        return (title);
    }

    final public String getDescription() {
        return (description);
    }

}
