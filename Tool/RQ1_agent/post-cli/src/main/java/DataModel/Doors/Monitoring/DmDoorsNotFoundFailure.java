/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Monitoring;

import Monitoring.LoadFromDatabaseFailure;
import Monitoring.Rule;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class DmDoorsNotFoundFailure extends LoadFromDatabaseFailure {

    static final private RuleDescription notFoundRuleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Loading DOORS element failed.",
            "An URL that should refer to an element in the DOORS database cannot be loaded from the database.");

    static final private Rule notFoundRule = new Rule(notFoundRuleDescription);

    public DmDoorsNotFoundFailure(String url, String problem) {
        super(notFoundRule, "Referenced DOORS element couldn't be loaded.", problem + "\n\nURL: " + url);

    }

}
