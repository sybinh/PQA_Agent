/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author RHO2HC
 */
public class EnumCriteriaValue implements CriteriaValueI {
    
    public static final String SEPARATOR_SYMBOL = ", ";
    private List<Object> range = new ArrayList<>();

    public EnumCriteriaValue(String value) {
        assert (value != null);
        if (value.contains(SEPARATOR_SYMBOL)) {
            range = Arrays.asList((Object[]) value.split(SEPARATOR_SYMBOL));
        } else {
            range.add(value);
        }
    }

    public List<Object> getRange() {
        return range;
    }

    public void setRange(List<Object> range) {
        this.range = range;
    }

    @Override
    public String toString() {
        String result = "";
        if (range.size() > 0) {
            result += range.get(0);
            for (int i = 1; i < range.size(); i++) {
                result += SEPARATOR_SYMBOL + String.valueOf(range.get(i));
            }
        }
        return result;
    }
}
