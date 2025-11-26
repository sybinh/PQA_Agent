/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

import ConfigurableRules.ConfigRule.Query.TextCriteriaValue;
import ConfigurableRules.ConfigRule.util.NumberObjectComparator;

/**
 *
 * @author RHO2HC
 */
public enum CriteriaOperatorForText implements CriteriaOperatorI<Object, TextCriteriaValue> {

    EQUALS("Equal") {
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return comparedValueToString.equalsIgnoreCase(thisStringValue);
        }
    },
    NOT_EQUALS("Not Equal"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return !comparedValueToString.equalsIgnoreCase(thisStringValue);
        }
    },
    CONTAINS("Contain"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return comparedValueToString.toLowerCase().contains(thisStringValue.toLowerCase());
        }
    },
    NOT_CONTAINS("Not Contain"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return !comparedValueToString.toLowerCase().contains(thisStringValue.toLowerCase());
        }
    },
    STARTS_WITH("Start With"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return comparedValueToString.toLowerCase().startsWith(thisStringValue.toLowerCase());
        }
    },
    ENDS_WITH("End With"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return comparedValueToString.toLowerCase().endsWith(thisStringValue.toLowerCase());
        }
    },
    REGULAR_EXPRESSION("Match With Regular Expression"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return comparedValueToString.toLowerCase().matches(thisStringValue.toLowerCase());
        }
    },
    NOT_REGULAR_EXPRESSION("Not Match With Regular Expression"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String thisStringValue = String.valueOf(criteriaValue.getValueToCompare());
            String comparedValueToString = String.valueOf(comparedValue);
            return !comparedValueToString.toLowerCase().matches(thisStringValue.toLowerCase());
        }
    },
    EMPTY("Empty"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String comparedValueToString = String.valueOf(comparedValue);
            return comparedValueToString.isEmpty();
        }
    },
    NOT_EMPTY("Not Empty"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            String comparedValueToString = String.valueOf(comparedValue);
            return !comparedValueToString.isEmpty();
        }
    },
    GREATER_THAN("Is Greater Than"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            int comparationResult = numberObjectComparator.compare(comparedValue, criteriaValue.getValueToCompare());
            return comparationResult != NumberObjectComparator.DEFAULT_RESULT ? comparationResult == 1 : false;
        }
    },
    GREATER_THAN_EQUAL("Is Greater Than Or Equal To") {
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            int comparationResult = numberObjectComparator.compare(comparedValue, criteriaValue.getValueToCompare());
            return comparationResult != NumberObjectComparator.DEFAULT_RESULT ? (comparationResult == 1 || comparationResult == 0) : false;
        }
    },
    LESS_THAN("Is Less Than"){
        @Override
        public boolean checkCriteriaValue(TextCriteriaValue criteriaValue, Object comparedValue) {
            int comparationResult = numberObjectComparator.compare(comparedValue, criteriaValue.getValueToCompare());
            return comparationResult != NumberObjectComparator.DEFAULT_RESULT ? comparationResult == -1 : false;
        }
    };

    private static final NumberObjectComparator numberObjectComparator =  new NumberObjectComparator();
    private final String name;
    
    CriteriaOperatorForText(String name) {
        assert (name != null);
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public static String [] convertToArrayOfStrings() {
        String [] result = new String [CriteriaOperatorForText.values().length];
        for(int i = 0; i < result.length; i++) {
            result[i] = CriteriaOperatorForText.values()[i].getName();
        }
        return result;
    } 
    
    @Override
    public String toString() {
        return name;
    }
    
    public static CriteriaOperatorForText enumOf(String name) {
        for(CriteriaOperatorForText enumeration: CriteriaOperatorForText.values()) {
            if (enumeration.getName().equals(name)) {
                return enumeration;
            }
        };
        return null;
    }
}
