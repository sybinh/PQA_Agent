/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

/**
 * Defines the base functionality of a rule. Basically the storage of the
 * description.
 *
 * @author gug2wi
 */
public interface RuleI {

    /**
     * Get the description for the rule.
     *
     * @return
     */
    RuleDescription getRuleDescription();

    /**
     * Shortcut for getRuleDescription().getTitle().
     *
     * @return Title of the rule.
     */
    default public String getRuleTitle() {
        return (getRuleDescription().getTitle());
    }

    /**
     * Shortcut for getRuleDescription().getRuleDescription().
     *
     * @return Description of the rule.
     */
    default public String getRuleDescriptionText() {
        return (getRuleDescription().getDescription());
    }

}
