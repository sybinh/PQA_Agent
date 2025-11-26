/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

/**
 * Manages the description for a rule.
 *
 * @author gug2wi
 */
public class Rule implements RuleI {

    final private RuleDescription description;

    public Rule(RuleDescription description) {
        assert (description != null);
        this.description = description;
    }

    @Override
    final public RuleDescription getRuleDescription() {
        return (description);
    }

}
