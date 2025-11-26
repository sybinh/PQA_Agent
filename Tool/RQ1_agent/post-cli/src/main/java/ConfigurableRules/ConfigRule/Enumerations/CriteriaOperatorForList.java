/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

import ConfigurableRules.ConfigRule.Query.Criteria;
import java.util.List;

/**
 *
 * @author RHO2HC
 */
public enum CriteriaOperatorForList implements CriteriaOperatorI<List, Criteria> {

    ALL("All Elements To") {
        @Override
        public boolean checkCriteriaValue(Criteria criteriaValue, List comparedValue) {
            for (Object value : comparedValue) {
                if (!checkValueByTypeAndOperator(value, criteriaValue)) {
                    return false;
                }
            }
            return true;
        }
    },
    ANY("At Least An Element To"){
        @Override
        public boolean checkCriteriaValue(Criteria criteriaValue, List comparedValue) {
            for (Object value : comparedValue) {
                if (checkValueByTypeAndOperator(value, criteriaValue)) {
                    return true;
                }
            }
            return false;
        }
    },
    NONE("No Element To"){
        @Override
        public boolean checkCriteriaValue(Criteria criteriaValue, List comparedValue) {
            for (Object value : comparedValue) {
                if (checkValueByTypeAndOperator(value, criteriaValue)) {
                    return false;
                }
            }
            return true;
        }
    };

    private final String name;
    
    CriteriaOperatorForList(String name) {
        assert (name != null);
        this.name = name;
    }
    
    @SuppressWarnings("unchecked")
    private static boolean checkValueByTypeAndOperator(Object comparedValue, Criteria criteriaValue) {
        return criteriaValue.getOperator().checkCriteriaValue(criteriaValue.getValueToCompare(), comparedValue);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public static String [] convertToArrayOfStrings() {
        String [] result = new String [CriteriaOperatorForList.values().length];
        for(int i = 0; i < result.length; i++) {
            result[i] = CriteriaOperatorForList.values()[i].getName();
        }
        return result;
    } 
    
    @Override
    public String toString() {
        return name;
    }
    
    public static CriteriaOperatorForList enumOf(String name) {
        for(CriteriaOperatorForList enumeration: CriteriaOperatorForList.values()) {
            if (enumeration.getName().equals(name)) {
                return enumeration;
            }
        };
        return null;
    }
}
