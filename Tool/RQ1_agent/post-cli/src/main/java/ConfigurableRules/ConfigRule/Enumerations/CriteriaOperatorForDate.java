/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

import ConfigurableRules.ConfigRule.Query.DateCriteriaValue;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvDate;

/**
 *
 * @author RHO2HC
 */
public enum CriteriaOperatorForDate implements CriteriaOperatorI<Object, DateCriteriaValue> {

    EMPTY("Empty") {
        @Override
        public boolean checkCriteriaValue(DateCriteriaValue criteriaValue, Object comparedValue) {
            return comparedValue == null || ((EcvDate)comparedValue).isEmpty();
        }
    },
    NOT_EMPTY("Not Empty") {
        @Override
        public boolean checkCriteriaValue(DateCriteriaValue criteriaValue, Object comparedValue) {
            return comparedValue != null && !((EcvDate)comparedValue).isEmpty();
        }
    },
    WITHIN("Within") {
        @Override
        public boolean checkCriteriaValue(DateCriteriaValue criteriaValue, Object comparedValue) {
            if(comparedValue == null || ((EcvDate)comparedValue).isEmpty()) {
                return false;
            }
            
            // get today
            EcvDate today = EcvDate.getToday();
            EcvDate finalDate = today.addDays((-1) * criteriaValue.getDaysAgo());
            int checkFinalDate = ((EcvDate)comparedValue).compareTo(finalDate);
            return today.compareTo(((EcvDate)comparedValue)) == 1 && (checkFinalDate == 1 || checkFinalDate == 0);
        }
    },
    DATE_CRITERIA("Date Criteria") {
        @Override
        public boolean checkCriteriaValue(DateCriteriaValue criteriaValue, Object comparedValue) {
            if(comparedValue == null || ((EcvDate)comparedValue).isEmpty()) {
                return false;
            }
            
            boolean checkFromDate = false;
            if (criteriaValue.getFromDate() != null) {
                checkFromDate = criteriaValue.getFromDateOperator().checkDate(criteriaValue.getFromDate(), ((EcvDate)comparedValue));
            } else {
                checkFromDate = true;
            }
            
            boolean checkToDate = false;
            if (criteriaValue.getToDate() != null) {
                checkToDate = criteriaValue.getToDateOperator().checkDate(criteriaValue.getToDate(), ((EcvDate)comparedValue));
            } else {
                checkToDate = true;
            }
            return checkFromDate && checkToDate;
        }
    },
    COMPARE_OTHER("Compare Other") {
        @Override
        public boolean checkCriteriaValue(DateCriteriaValue criteriaValue, Object comparedValue) {
            @SuppressWarnings("unchecked")
            List<EcvDate> comparedDates = ((List)comparedValue);
            
            if(comparedDates.get(0) == null || comparedDates.get(0).isEmpty() || comparedDates.get(1) == null || comparedDates.get(1).isEmpty()) {
                return false;
            }
            
            return criteriaValue.getCompareDateOperator().checkDate(comparedDates.get(0), comparedDates.get(1));
        }
    };

    private final String name;
    
    CriteriaOperatorForDate(String name) {
        assert (name != null);
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public static String [] convertToArrayOfStrings() {
        String [] result = new String [CriteriaOperatorForDate.values().length];
        for(int i = 0; i < result.length; i++) {
            result[i] = CriteriaOperatorForDate.values()[i].getName();
        }
        return result;
    } 
    
    @Override
    public String toString() {
        return name;
    }
    
    public static CriteriaOperatorForDate enumOf(String name) {
        for(CriteriaOperatorForDate enumeration: CriteriaOperatorForDate.values()) {
            if (enumeration.getName().equals(name)) {
                return enumeration;
            }
        };
        return null;
    }
    
    public interface DateCriteriaOperatorI {
        public String getName();
        public abstract boolean checkDate(EcvDate date1, EcvDate date2);
    }
    
    public enum LeftDateCriteriaOperator implements DateCriteriaOperatorI {
        EQUAL("==") {

            @Override
            public boolean checkDate(EcvDate date1, EcvDate date2) {
                return date1.compareTo(date2) == 0;
            }

        },
        GREATER_THAN("<"){

            @Override
            public boolean checkDate(EcvDate date1, EcvDate date2) {
                return date1.compareTo(date2) < 0;
            }

        },
        LESS_THAN(">"){

            @Override
            public boolean checkDate(EcvDate date1, EcvDate date2) {
                return date1.compareTo(date2) > 0;
            }

        };

        private final String name;
        
        LeftDateCriteriaOperator(String name) {
            assert (name != null);
            this.name = name;
        }
        @Override
        public String getName() {
            return name;
        }
        
        public static LeftDateCriteriaOperator enumOf(String name) {
            for(LeftDateCriteriaOperator enumeration: LeftDateCriteriaOperator.values()) {
                if (enumeration.getName().equals(name)) {
                    return enumeration;
                }
            };
            return null;
        }
    }
    
    public enum RightDateCriteriaOperator implements DateCriteriaOperatorI {
        NONE_CHOOSE("No"){

            @Override
            public boolean checkDate(EcvDate date1, EcvDate date2) {
                return false;
            }

        },
        LESS_THAN("<"){

            @Override
            public boolean checkDate(EcvDate date1, EcvDate date2) {
                return date1.compareTo(date2) > 0;
            }

        };

        private final String name;
        
        RightDateCriteriaOperator(String name) {
            assert (name != null);
            this.name = name;
        }
        @Override
        public String getName() {
            return name;
        }
        
        public static RightDateCriteriaOperator enumOf(String name) {
            for(RightDateCriteriaOperator enumeration: RightDateCriteriaOperator.values()) {
                if (enumeration.getName().equals(name)) {
                    return enumeration;
                }
            };
            return null;
        }
    }
}
