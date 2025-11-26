/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

/**
 *
 * @author RHO2HC
 */
public class TextCriteriaValue implements CriteriaValueI {
    
    private Object valueToCompare;
    
    public TextCriteriaValue(Object valueToCompare) {
        this.valueToCompare = valueToCompare;
    }
    
    public Object getValueToCompare() {
        return valueToCompare;
    }

    public void setValueToCompare(Object valueToCompare) {
        this.valueToCompare = valueToCompare;
    }
}