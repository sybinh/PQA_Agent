/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.util;

import ConfigurableRules.ConfigRule.Enumerations.SorterOrder;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author RHO2HC
 */
public class KeyComparator<K extends Comparable<? super K>, V> implements Comparator<Map.Entry<K, V>>{

    private SorterOrder sorter = SorterOrder.ASC;
    
    public KeyComparator(SorterOrder sorter) {
        if (sorter != null) {
            this.sorter = sorter;
        }
    }

    @Override
    public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
        return (sorter == SorterOrder.DESC ? (-1) : 1) * o1.getKey().compareTo(o2.getKey());
    }
}
