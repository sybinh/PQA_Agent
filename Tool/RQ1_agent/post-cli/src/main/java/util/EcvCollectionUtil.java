/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 *
 * @author GUG2WI
 */
public class EcvCollectionUtil {

    /**
     * Checks for the equality of both sets by using the equal method of the
     * elements in the sets.
     *
     * @param <T>
     * @param set1
     * @param set2
     * @return
     */
    static public <T> boolean equalsSortedSet(SortedSet<T> set1, SortedSet<T> set2) {
        if (set1 == null) {
            return (set2 == null);
        } else if (set2 == null) {
            return (false);
        } else if (set1.size() != set2.size()) {
            return (false);
        } else if (set1.isEmpty()) {
            return (true);
        } else {
            Iterator<T> iterator1 = set1.iterator();
            Iterator<T> iterator2 = set2.iterator();
            while (iterator1.hasNext()) {
                T element1 = iterator1.next();
                T element2 = iterator2.next();
                if (Objects.equals(element1, element2) == false) {
                    return (false);
                }
            }
            return (true);
        }
    }

    static public <K, V> boolean equalsSortedMap(SortedMap<K, V> map1, SortedMap<K, V> map2) {
        if (map1 == null) {
            return (map2 == null);
        } else if (map2 == null) {
            return (false);
        } else if (map1.size() != map2.size()) {
            return (false);
        } else if (map1.isEmpty()) {
            return (true);
        } else {
            Iterator<Map.Entry<K, V>> iterator1 = map1.entrySet().iterator();
            Iterator<Map.Entry<K, V>> iterator2 = map2.entrySet().iterator();
            while (iterator1.hasNext()) {
                Map.Entry<K, V> entry1 = iterator1.next();
                Map.Entry<K, V> entry2 = iterator2.next();
                if (Objects.equals(entry1.getKey(), entry2.getKey()) == false) {
                    return (false);
                }
                if (Objects.equals(entry1.getValue(), entry2.getValue()) == false) {
                    return (false);
                }
            }
            return (true);
        }

    }

}
