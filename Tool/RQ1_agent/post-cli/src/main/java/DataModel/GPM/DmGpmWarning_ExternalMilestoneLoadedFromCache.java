/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import Monitoring.Rule;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmGpmWarning_ExternalMilestoneLoadedFromCache extends Warning implements DmGpmWarningI {

    static final private String warningTitle = "Milestone loaded from cache.";

    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            warningTitle,
            "This rule marks external milestones with a warning, if the database containing the external milestone cannot be accessed.\n"
            + "\n"
            + "If such a database cannot be reached, then the data cached in RQ1 is used to show at least some information for that milestone.\n"
            + "The warning is used to inform the user that the data shown might be outdated and incomplete."
    );

    final private static Rule rule = new Rule(description);

    public DmGpmWarning_ExternalMilestoneLoadedFromCache(DmGpmMilestone milestone, String database) {
        super(rule, warningTitle);
        assert (milestone != null);

        setDescription("Database " + database + " cannot be reached.\n"
                + "\n"
                + "All milestones from this database are loaded from the data cached in RQ1.\n"
                + "Therefore, the data of the milestones might be outdated and incomplete.");
    }

}
