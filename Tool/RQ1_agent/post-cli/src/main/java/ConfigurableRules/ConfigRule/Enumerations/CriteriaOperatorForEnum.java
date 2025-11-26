/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

import ConfigurableRules.ConfigRule.Query.EnumCriteriaValue;
import util.EcvEnumeration;

/**
 *
 * @author RHO2HC
 */
public enum CriteriaOperatorForEnum implements CriteriaOperatorI<EcvEnumeration, EnumCriteriaValue> {

    EMPTY("Empty"){
        @Override
        public boolean checkCriteriaValue(EnumCriteriaValue criteriaValue, EcvEnumeration comparedValue) {
            return comparedValue == null || comparedValue.getText().isEmpty();
        }
    },
    NOT_EMPTY("Not Empty"){
        @Override
        public boolean checkCriteriaValue(EnumCriteriaValue criteriaValue, EcvEnumeration comparedValue) {
            return comparedValue != null && !comparedValue.getText().isEmpty();
        }
    },
    IN_LIST("In List"){
        @Override
        public boolean checkCriteriaValue(EnumCriteriaValue criteriaValue, EcvEnumeration comparedValue) {
            return criteriaValue.getRange().contains(comparedValue.getText());
        }
    },
    NOT_IN_LIST("Not In List"){
        @Override
        public boolean checkCriteriaValue(EnumCriteriaValue criteriaValue, EcvEnumeration comparedValue) {
            return !criteriaValue.getRange().contains(comparedValue.getText());
        }
    };

    protected final String name;
    
    CriteriaOperatorForEnum(String name) {
        assert (name != null);
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public static String [] convertToArrayOfStrings() {
        String [] result = new String [CriteriaOperatorForEnum.values().length];
        for(int i = 0; i < result.length; i++) {
            result[i] = CriteriaOperatorForEnum.values()[i].getName();
        }
        return result;
    } 
    
    @Override
    public String toString() {
        return name;
    }
    
    public static CriteriaOperatorForEnum enumOf(String name) {
        for(CriteriaOperatorForEnum enumeration: CriteriaOperatorForEnum.values()) {
            if (enumeration.getName().equals(name)) {
                return enumeration;
            }
        };
        return null;
    }
}
