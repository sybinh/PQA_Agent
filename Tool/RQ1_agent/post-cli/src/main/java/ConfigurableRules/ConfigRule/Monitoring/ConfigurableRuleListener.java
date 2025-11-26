/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Monitoring;

import ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord;
import DataModel.Rq1.Records.DmRq1Project;

/**
 *
 * @author RHO2HC
 */
public interface ConfigurableRuleListener {
    public void addARule(ConfigurableRuleRecord record, DmRq1Project belongsToProject);
    public void editARule(ConfigurableRuleRecord record, DmRq1Project belongsToProject);
    public void removeARule(String recordType, String ruleId);
    
    public void deactive();
    public void active();
}
