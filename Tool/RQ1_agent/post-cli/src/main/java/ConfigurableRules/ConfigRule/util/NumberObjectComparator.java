/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.util;

import java.util.Comparator;

/**
 *
 * @author RHO2HC
 */
public class NumberObjectComparator implements Comparator<Object>{
    public static final String NUMBER_EXPRESSION = "\\d+";
    public final static int DEFAULT_RESULT = -1000;
            
    @Override
    public int compare(Object o1, Object o2) {
        String string1 = String.valueOf(o1);
        String string2 = String.valueOf(o2);
        if (string2.matches(NUMBER_EXPRESSION) && string1.matches(NUMBER_EXPRESSION)) {
            Integer num1 = Integer.parseInt(string1);
            Integer num2 = Integer.parseInt(string2);
            return num1.compareTo(num2);
        }
        return DEFAULT_RESULT;
    }
    
}
