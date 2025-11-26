/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Monitoring;

import DataModel.Rq1.Records.DmRq1AssignedRecord;
import DataModel.Rq1.Records.DmRq1Project;

/**
 *
 * @author RHO2HC
 */
public class ConfigurableRuleManagerRule_Rq1AssignedRecord extends ConfigurableRuleManagerRule<DmRq1AssignedRecord> {

    public ConfigurableRuleManagerRule_Rq1AssignedRecord(DmRq1AssignedRecord rq1AssignedRecord) {
        super(rq1AssignedRecord);

    }

    @Override
    protected boolean checkAssignedElementIsInTheGivenProject(DmRq1Project project) {
        return ((project != null) && (dmElement.PROJECT.getElement() == project));
    }
}
