/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Records;

/**
 *
 * @author RHO2HC
 */
public class AbstractConfigurableRuleRecord {
    public static final String ATTRIBUTE_RULE_ID = "ruleId";
    public static final String ATTRIBUTE_NAME = "name";
    
    protected String ruleId = "";
    protected String name = "";

    public AbstractConfigurableRuleRecord(String ruleId, String name) {
        this.ruleId = ruleId;
        this.name = name;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
