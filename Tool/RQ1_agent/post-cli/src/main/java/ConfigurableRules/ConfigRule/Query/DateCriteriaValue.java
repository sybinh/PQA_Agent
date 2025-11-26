/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

import ConfigurableRules.ConfigRule.Enumerations.CriteriaOperatorForDate.LeftDateCriteriaOperator;
import ConfigurableRules.ConfigRule.Enumerations.CriteriaOperatorForDate.RightDateCriteriaOperator;
import util.EcvDate;

/**
 *
 * @author RHO2HC
 */
public class DateCriteriaValue implements CriteriaValueI {
    
    public static final String ATTRIBUTE_FROM_DATE = "fromDate";
    public static final String ATTRIBUTE_TO_DATE = "toDate";
    public static final String ATTRIBUTE_FROM_DATE_OPERATOR = "fromDateOperator";
    public static final String ATTRIBUTE_TO_DATE_OPERATOR = "toDateOperator";
    public static final String ATTRIBUTE_DAYS_AGO = "daysAgo";
    public static final String ATTRIBUTE_COMPARE_DATE_OPERATOR = "compareDateOperator";
    public static final String ATTRIBUTE_COMPARED_FIELD = "comparedField";
    
    private EcvDate fromDate = null;
    private LeftDateCriteriaOperator fromDateOperator = LeftDateCriteriaOperator.GREATER_THAN;
    private EcvDate toDate = null;
    private RightDateCriteriaOperator toDateOperator = RightDateCriteriaOperator.LESS_THAN;

    private int daysAgo = 0;
    
    private String comparedFieldName = null;
    private LeftDateCriteriaOperator compareDateOperator = LeftDateCriteriaOperator.GREATER_THAN;
    
    public DateCriteriaValue() {
    }

    public DateCriteriaValue(EcvDate fromDate, LeftDateCriteriaOperator fromDateOperator, EcvDate toDate, RightDateCriteriaOperator toDateOperator) {
        this.fromDate = fromDate;
        this.fromDateOperator = fromDateOperator;
        this.toDate = toDate;
        this.toDateOperator = toDateOperator;
    }
    
    public DateCriteriaValue(int daysAgo) {
        this.daysAgo = daysAgo;
    }
    
    public DateCriteriaValue(String comparedFieldName, LeftDateCriteriaOperator compareDateOperator) {
        this.comparedFieldName = comparedFieldName;
        this.compareDateOperator = compareDateOperator;
    }

    public EcvDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(EcvDate fromDate) {
        this.fromDate = fromDate;
    }

    public LeftDateCriteriaOperator getFromDateOperator() {
        return fromDateOperator;
    }

    public void setFromDateOperator(LeftDateCriteriaOperator fromDateOperator) {
        this.fromDateOperator = fromDateOperator;
    }

    public EcvDate getToDate() {
        return toDate;
    }

    public void setToDate(EcvDate toDate) {
        this.toDate = toDate;
    }

    public RightDateCriteriaOperator getToDateOperator() {
        return toDateOperator;
    }

    public void setToDateOperator(RightDateCriteriaOperator toDateOperator) {
        this.toDateOperator = toDateOperator;
    }

    public int getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(int daysAgo) {
        this.daysAgo = daysAgo;
    }
    
    public String getComparedFieldName() {
        return comparedFieldName;
    }
    
    public void setComparedFieldName(String comparedFieldName) {
        this.comparedFieldName = comparedFieldName;
    }
    
    public LeftDateCriteriaOperator getCompareDateOperator() {
        return compareDateOperator;
    }
    
    public void setCompareDateOperator(LeftDateCriteriaOperator compareDateOperator) {
        this.compareDateOperator = compareDateOperator;
    }
}
