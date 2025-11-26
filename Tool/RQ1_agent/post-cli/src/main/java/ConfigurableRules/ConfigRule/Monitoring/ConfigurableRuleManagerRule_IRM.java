/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Monitoring;

import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1Release;

/**
 *
 * @author RHO2HC
 */
public class ConfigurableRuleManagerRule_IRM extends ConfigurableRuleManagerRule<DmRq1Irm> {

    public ConfigurableRuleManagerRule_IRM(DmRq1Irm assignedDmElement) {
        super(assignedDmElement);
    }

    @Override
    protected boolean checkAssignedElementIsInTheGivenProject(DmRq1Project project) {
        if (project != null) {
            DmRq1Release release = dmElement.HAS_MAPPED_RELEASE.getElement();
            if (release != null) {
                if (project == release.getProject()) {
                    return (true);
                }
            }
            DmRq1Issue issue = dmElement.HAS_MAPPED_ISSUE.getElement();
            if (issue != null) {
                if (project == issue.getProject()) {
                    return (true);
                }
            }
        }
        return (false);
    }

}
