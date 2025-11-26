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
public class DmDoorsRequirementWithoutUrlWarning extends Warning {

    static final private RuleDescription requirementWithoutUrlDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Missing URL for DOORS reference.",
            "A requirement that points to an element in the DOORS database has no URL.");

    static final private Rule requirementWithoutUrlRule = new Rule(requirementWithoutUrlDescription);

    public DmDoorsRequirementWithoutUrlWarning(String requirement) {
        super(requirementWithoutUrlRule, "Missing URL for " + requirement, "No URL is provided for requirement " + requirement);

    }

}
