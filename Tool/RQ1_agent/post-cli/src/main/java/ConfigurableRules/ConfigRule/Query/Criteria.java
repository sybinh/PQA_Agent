/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

import ConfigurableRules.ConfigRule.Enumerations.CriteriaFieldType;
import ConfigurableRules.ConfigRule.Enumerations.CriteriaOperatorForList;
import ConfigurableRules.ConfigRule.Enumerations.CriteriaOperatorI;
import DataModel.Eis.DmEisWorkitemTable_WiType_Enumeration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvEnumeration;
import util.EcvEnumerationValue;
import util.EcvXmlEmptyElement;

/**
 *
 * @author RHO2HC
 */
public class Criteria<T_VALUE_TO_COMPARE, T_CRITERIA_VALUE extends CriteriaValueI> {
    
    public static final String CRITERIA = "Criteria";
    public static final String ATTRIBUTE_TYPE = "type";
    public static final String ATTRIBUTE_VALUE = "value";
    public static final String ATTRIBUTE_OPERATOR = "operator";
    public static final String ATTRIBUTE_FIELD_NAME = "field";
    public static final String ATTRIBUTE_ID = "id";
    
    public static final List<String> fieldNames = Arrays.asList(ATTRIBUTE_FIELD_NAME, ATTRIBUTE_TYPE, 
                                                                ATTRIBUTE_OPERATOR, ATTRIBUTE_VALUE);

    private String id;
    private final String field;
    private final CriteriaFieldType type;
    private CriteriaOperatorI<T_VALUE_TO_COMPARE, T_CRITERIA_VALUE> operator;
    private CriteriaOperatorForList operatorForFieldList = CriteriaOperatorForList.ALL;
    private final T_CRITERIA_VALUE valueToCompare;
    private EcvXmlEmptyElement xmlObject;
    
    public Criteria(String id, String field, CriteriaFieldType type, T_CRITERIA_VALUE valueToCompare, String operatorName) {
        this.id = id;
        this.field = field;
        this.type = type;
        this.valueToCompare = valueToCompare;
        setOperatorByName(operatorName);
    }
    
    @SuppressWarnings("unchecked")
    public Criteria(EcvXmlEmptyElement element) {
        id = element.getAttribute(ATTRIBUTE_ID);
        field = element.getAttribute(ATTRIBUTE_FIELD_NAME);
        type = CriteriaFieldType.enumOf(element.getAttribute(ATTRIBUTE_TYPE));
        
        if (element.hasAttribute(ATTRIBUTE_OPERATOR)) {
            setOperatorByName(element.getAttribute(ATTRIBUTE_OPERATOR));
        }
        valueToCompare = (T_CRITERIA_VALUE) getValueFromXml(element, type);
            
        this.xmlObject = element;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
        
    public String getField() {
        return field;
    }

    public CriteriaFieldType getType() {
        return type;
    }

    public CriteriaOperatorI<T_VALUE_TO_COMPARE, T_CRITERIA_VALUE> getOperator() {
        return operator;
    }

    public void setOperator(CriteriaOperatorI<T_VALUE_TO_COMPARE, T_CRITERIA_VALUE> operator) {
        this.operator = operator;
    }

    public T_CRITERIA_VALUE getValueToCompare() {
        return valueToCompare;
    }
    
    @SuppressWarnings("unchecked")
    public boolean checkValue(Object comparedValue) {
        try {
            
            // For case field have properties (Ex: Attachment have name, description, size)
            if (field.contains("_")) {
                if (comparedValue instanceof List) {
                    List values = (List) comparedValue;
                    setOperatorForFieldList();
                    if (operatorForFieldList != null) {
                        return operatorForFieldList.checkCriteriaValue(this, values);
                    }
                }
            
            // For case field is 1 value
            } else {
                
                // Input is list of values (may from multiple fields)
                if (comparedValue instanceof List) {
                    List<T_VALUE_TO_COMPARE> comparedValues = (List) comparedValue;
                    return this.operator.checkCriteriaValue(this.valueToCompare, (T_VALUE_TO_COMPARE) comparedValues);

                // Input is 1 value (from 1 field)
                } else {
                    return this.operator.checkCriteriaValue(this.valueToCompare, (T_VALUE_TO_COMPARE) comparedValue);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Criteria.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Criteria.class.getName(), ex);
        }
        return false;
    }
    
    public static EcvEnumeration[] getValuesArrayOfEcvEnumerationClass(Object o) {
        EcvEnumeration[] values = null;
        
        if(o == null) {
            return values;
        }
        
        try {
            // Incase 'o' is EcvEnumerationInstance -> from TYPE attribute of workItem
            if(o instanceof EcvEnumerationValue) {
                values = DmEisWorkitemTable_WiType_Enumeration.values();
                return values;
            }
            
            List<String> methodNames = Arrays.asList(o.getClass().getMethods()).stream().map(m -> {return m.getName();}).collect(Collectors.toList());
            
            if (o instanceof EcvEnumeration && methodNames.contains(VALUES)) {
                Method getValueMethod;
                getValueMethod = o.getClass().getMethod(VALUES, (Class<?>[]) null);
                values = (EcvEnumeration[]) getValueMethod.invoke(o, new Object [0]);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Criteria.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Criteria.class.getName(), ex);
        }
        return values;
    }
    
    public static final String VALUES = "values";
    
    public EcvXmlEmptyElement convertToXmlObject(String ruleId) {
        return this.type.encodeCriteria(this, ruleId);
    }

    public EcvXmlEmptyElement getXmlObject() {
        return xmlObject;
    }

    public void setXmlObject(EcvXmlEmptyElement xmlObject) {
        this.xmlObject = xmlObject;
    }

    private CriteriaValueI getValueFromXml(EcvXmlEmptyElement element, CriteriaFieldType type) {
        return type.decodeCriteriaValue(element);
    }

    @SuppressWarnings("unchecked")
    public void setOperatorByName(String name) {
        this.operator = type.getOperatorEnumByName(name);
    }

    private void setOperatorForFieldList() {
        if (operator.getName().toLowerCase().startsWith("not")) {
            operatorForFieldList = CriteriaOperatorForList.ALL;
        } else {
            operatorForFieldList = CriteriaOperatorForList.ANY;
        }
    }
}
