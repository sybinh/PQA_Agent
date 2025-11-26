/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

import ConfigurableRules.ConfigRule.Query.Criteria;
import static ConfigurableRules.ConfigRule.Query.Criteria.ATTRIBUTE_ID;
import static ConfigurableRules.ConfigRule.Query.Criteria.ATTRIBUTE_FIELD_NAME;
import static ConfigurableRules.ConfigRule.Query.Criteria.ATTRIBUTE_OPERATOR;
import static ConfigurableRules.ConfigRule.Query.Criteria.ATTRIBUTE_TYPE;
import static ConfigurableRules.ConfigRule.Query.Criteria.ATTRIBUTE_VALUE;
import static ConfigurableRules.ConfigRule.Query.Criteria.CRITERIA;
import ConfigurableRules.ConfigRule.Query.CriteriaValueI;
import ConfigurableRules.ConfigRule.Query.DateCriteriaValue;
import static ConfigurableRules.ConfigRule.Query.DateCriteriaValue.ATTRIBUTE_FROM_DATE;
import static ConfigurableRules.ConfigRule.Query.DateCriteriaValue.ATTRIBUTE_TO_DATE;
import ConfigurableRules.ConfigRule.Query.TextCriteriaValue;
import ConfigurableRules.ConfigRule.Query.EnumCriteriaValue;
import ConfigurableRules.ConfigRule.Records.AbstractConfigurableRuleRecord;
import java.util.logging.Level;
import java.util.logging.Logger;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;
import util.EcvXmlEmptyElement;

/**
 *
 * @author RHO2HC
 * Type of field in Configurable Rule Criteria Object
 */

public enum CriteriaFieldType {

    AsList("List", CriteriaOperatorForList.convertToArrayOfStrings()) {
        @Override
        public EcvXmlEmptyElement encodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj, CriteriaValueI valueToCompare, String ruleId) {
            return null;
        }

        @Override
        public CriteriaValueI decodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj) {
            return null;
        }

        @Override
        public CriteriaOperatorI getOperatorEnumByName(String name) {
            return CriteriaOperatorForList.enumOf(name);
        }

        @Override
        public Criteria createCriteriaObject(String id, String fieldName, String operatorName, CriteriaValueI valueToCompare) {
            return null;
        }
    } ,
    Text("Text", CriteriaOperatorForText.convertToArrayOfStrings()) {
        @Override
        public EcvXmlEmptyElement encodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj, CriteriaValueI valueToCompare, String ruleId) {
            criteriaXmlObj.addAttribute(ATTRIBUTE_VALUE, String.valueOf(((TextCriteriaValue) valueToCompare).getValueToCompare()));
            return criteriaXmlObj;
        }

        @Override
        public CriteriaValueI decodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj) {
            if (criteriaXmlObj.hasAttribute(ATTRIBUTE_VALUE)) {
                return new TextCriteriaValue(criteriaXmlObj.getAttribute(ATTRIBUTE_VALUE));
            }
            return null;
        }

        @Override
        public CriteriaOperatorI getOperatorEnumByName(String name) {
            return CriteriaOperatorForText.enumOf(name);
        }

        @Override
        public Criteria createCriteriaObject(String id, String fieldName, String operatorName, CriteriaValueI valueToCompare) {
            return new Criteria<Object, TextCriteriaValue>(id, fieldName, CriteriaFieldType.Text, (TextCriteriaValue) valueToCompare, operatorName);
        }
    } ,
    Enum("Enumeration", CriteriaOperatorForEnum.convertToArrayOfStrings()) {
        @Override
        public EcvXmlEmptyElement encodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj, CriteriaValueI valueToCompare, String ruleId) {
            criteriaXmlObj.addAttribute(ATTRIBUTE_VALUE, ((EnumCriteriaValue) valueToCompare).toString());
            return criteriaXmlObj;
        }

        @Override
        public CriteriaValueI decodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj) {
            if (criteriaXmlObj.hasAttribute(ATTRIBUTE_VALUE)) {
                return new EnumCriteriaValue(criteriaXmlObj.getAttribute(ATTRIBUTE_VALUE));
            }
            return null;
        }

        @Override
        public CriteriaOperatorI getOperatorEnumByName(String name) {
            return CriteriaOperatorForEnum.enumOf(name);
        }

        @Override
        public Criteria createCriteriaObject(String id, String fieldName, String operatorName, CriteriaValueI valueToCompare) {
            return new Criteria<Object, EnumCriteriaValue>(id, fieldName, CriteriaFieldType.Enum, (EnumCriteriaValue) valueToCompare, operatorName);
        }
    },
    Date("Date", CriteriaOperatorForDate.convertToArrayOfStrings()) {
        @Override
        public EcvXmlEmptyElement encodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj, CriteriaValueI valueToCompare, String ruleId) {
            DateCriteriaValue dateValue = ((DateCriteriaValue) valueToCompare);
            
            // Criteria is for compare Date fields
            if(dateValue.getComparedFieldName()!= null) {
                criteriaXmlObj.addAttribute(DateCriteriaValue.ATTRIBUTE_COMPARED_FIELD, dateValue.getComparedFieldName());
                criteriaXmlObj.addAttribute(DateCriteriaValue.ATTRIBUTE_COMPARE_DATE_OPERATOR, dateValue.getCompareDateOperator().getName());
                
            // Criteria is for Date period
            } else if (dateValue.getDaysAgo() == 0) {
                if (dateValue.getFromDate() != null) {
                    criteriaXmlObj.addAttribute(ATTRIBUTE_FROM_DATE, dateValue.getFromDate().getRq1TemplateValue());
                    criteriaXmlObj.addAttribute(DateCriteriaValue.ATTRIBUTE_FROM_DATE_OPERATOR, dateValue.getFromDateOperator().getName());
                }

                if (dateValue.getToDate() != null) {
                    criteriaXmlObj.addAttribute(ATTRIBUTE_TO_DATE, dateValue.getToDate().getRq1TemplateValue());
                    criteriaXmlObj.addAttribute(DateCriteriaValue.ATTRIBUTE_TO_DATE_OPERATOR, dateValue.getToDateOperator().getName());
                }
            
            // Criteria is for days ago
            } else {
                criteriaXmlObj.addAttribute(DateCriteriaValue.ATTRIBUTE_DAYS_AGO, String.valueOf(dateValue.getDaysAgo()));
            }
            return criteriaXmlObj;
        }

        @Override
        public CriteriaValueI decodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj) {
            DateCriteriaValue dateValue = new DateCriteriaValue();
            try {
                
                if(criteriaXmlObj.hasAttribute(DateCriteriaValue.ATTRIBUTE_COMPARE_DATE_OPERATOR)) {
                    dateValue.setComparedFieldName(criteriaXmlObj.getAttribute(DateCriteriaValue.ATTRIBUTE_COMPARED_FIELD));
                    dateValue.setCompareDateOperator(CriteriaOperatorForDate.LeftDateCriteriaOperator.enumOf(criteriaXmlObj.getAttribute(DateCriteriaValue.ATTRIBUTE_COMPARE_DATE_OPERATOR)));
                    
                } else if (criteriaXmlObj.hasAttribute(DateCriteriaValue.ATTRIBUTE_DAYS_AGO)) {
                    dateValue.setDaysAgo(Integer.parseInt(criteriaXmlObj.getAttribute(DateCriteriaValue.ATTRIBUTE_DAYS_AGO)));
                    
                } else {
                    if (criteriaXmlObj.hasAttribute(DateCriteriaValue.ATTRIBUTE_FROM_DATE)) {
                        dateValue.setFromDate(EcvDate.parseRq1TemplateValue(criteriaXmlObj.getAttribute(DateCriteriaValue.ATTRIBUTE_FROM_DATE)));
                        dateValue.setFromDateOperator(CriteriaOperatorForDate.LeftDateCriteriaOperator.enumOf(criteriaXmlObj.getAttribute(DateCriteriaValue.ATTRIBUTE_FROM_DATE_OPERATOR)));
                    }

                    if (criteriaXmlObj.hasAttribute(DateCriteriaValue.ATTRIBUTE_TO_DATE)) {
                        dateValue.setToDate(EcvDate.parseRq1TemplateValue(criteriaXmlObj.getAttribute(DateCriteriaValue.ATTRIBUTE_TO_DATE)));
                        dateValue.setToDateOperator(CriteriaOperatorForDate.RightDateCriteriaOperator.enumOf(criteriaXmlObj.getAttribute(DateCriteriaValue.ATTRIBUTE_TO_DATE_OPERATOR)));
                    }
                }
                
            } catch (EcvDate.DateParseException ex) {
                Logger.getLogger(CriteriaFieldType.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(CriteriaFieldType.class.getName(), ex);
            }
            return dateValue;
        }

        @Override
        public CriteriaOperatorI getOperatorEnumByName(String name) {
            return CriteriaOperatorForDate.enumOf(name);
        }

        @Override
        public Criteria createCriteriaObject(String id, String fieldName, String operatorName, CriteriaValueI valueToCompare) {
            return new Criteria<Object, DateCriteriaValue>(id, fieldName, CriteriaFieldType.Date, (DateCriteriaValue) valueToCompare, operatorName);
        }
    };

    private final String name;
    private final String [] operators;
    
    private CriteriaFieldType(String dbText, String [] operators) {
        this.name = dbText;
        this.operators = operators;
    }
        
    public String getName() {
        return name;
    }

    public String[] getOperators() {
        return operators;
    }
    
    public static CriteriaFieldType enumOf(String name) {
        for(CriteriaFieldType enumeration: CriteriaFieldType.values()) {
            if (enumeration.getName().equals(name)) {
                return enumeration;
            }
        };
        return null;
    }
    
    public EcvXmlEmptyElement encodeCriteria(Criteria criteria, String ruleId) {
        EcvXmlEmptyElement result = new EcvXmlEmptyElement(CRITERIA);
        result.addAttribute(AbstractConfigurableRuleRecord.ATTRIBUTE_RULE_ID, ruleId);
        result.addAttribute(ATTRIBUTE_ID, criteria.getId());
        result.addAttribute(ATTRIBUTE_FIELD_NAME, criteria.getField());
        result.addAttribute(ATTRIBUTE_TYPE, criteria.getType().getName());
        result.addAttribute(ATTRIBUTE_OPERATOR, criteria.getOperator() != null ? criteria.getOperator().getName() : "");
        if (criteria.getValueToCompare() != null) {
            result = encodeCriteriaValue(result, criteria.getValueToCompare(), ruleId);
        }
        criteria.setXmlObject(result);
        return result;
    }
    
    public abstract EcvXmlEmptyElement encodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj, CriteriaValueI valueToCompare, String ruleId);
    
    public abstract CriteriaValueI decodeCriteriaValue(EcvXmlEmptyElement criteriaXmlObj);
    
    public abstract CriteriaOperatorI getOperatorEnumByName(String name);
    
    public abstract Criteria createCriteriaObject(String id, String fieldName, String operatorName, CriteriaValueI valueToCompare);
}
