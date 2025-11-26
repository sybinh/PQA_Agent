/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

import java.util.EnumSet;

/**
 *
 * @author RHO2HC
 */
public enum SorterOrder {
    ASC("ASC Order"),
    DESC("DESC Order");
    
    private final String dbText;

    private SorterOrder(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }
    
    public static String [] convertToArray() {
        String [] result = new String [SorterOrder.values().length];
        for(int i = 0; i < result.length; i++) {
            result[i] = SorterOrder.values()[i].getText();
        }
        return result;
    } 

    final public String getText() {
        return dbText;
    }

    @Override
    public String toString() {
        return dbText;
    }
    
    public static SorterOrder constantOf(String dbText) {
        EnumSet<SorterOrder> setOfEnums = EnumSet.of(ASC, DESC);
        for(SorterOrder enumeration: setOfEnums) {
            if (enumeration.getText().equals(dbText)) {
                return enumeration;
            }
        };
        return null;
    }
}
