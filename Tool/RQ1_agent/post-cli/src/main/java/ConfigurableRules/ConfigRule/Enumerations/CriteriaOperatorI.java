/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

/**
 *
 * @author RHO2HC
 */
public interface CriteriaOperatorI<T_VALUE_TO_COMPARE, T_CRITERIA_VALUE> {
    public String getName();
    public abstract boolean checkCriteriaValue(T_CRITERIA_VALUE criteriaValue, T_VALUE_TO_COMPARE comparedValue);
}
