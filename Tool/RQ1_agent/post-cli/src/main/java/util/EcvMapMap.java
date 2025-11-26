/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implements a tree map that contains another tree map.
 *
 * Null values are allowed.
 *
 * @author gug2wi
 * @param <T_KEY1> Type of the key of the first map.
 * @param <T_KEY2> Type of the key of the second map.
 * @param <T_VALUE> Type of the value hold in the data structure.
 */
public class EcvMapMap<T_KEY1 extends Comparable<T_KEY1>, T_KEY2 extends Comparable<T_KEY2>, T_VALUE> extends TreeMap<T_KEY1, TreeMap<T_KEY2, T_VALUE>> {

    public static class Entry<T_KEY1, T_KEY2, T_VALUE> {

        final private T_KEY1 key1;
        final private T_KEY2 key2;
        final private T_VALUE value;

        public Entry(T_KEY1 key1, T_KEY2 key2, T_VALUE value) {
            assert (key1 != null);
            assert (key2 != null);
            this.key1 = key1;
            this.key2 = key2;
            this.value = value;
        }

        public T_KEY1 getKey1() {
            return key1;
        }

        public T_KEY2 getKey2() {
            return key2;
        }

        public T_VALUE getValue() {
            return value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (key1 != null) {
                b.append(key1.toString());
            } else {
                b.append("null");
            }
            b.append(" -> ");
            if (key2 != null) {
                b.append(key2.toString());
            } else {
                b.append("null");
            }
            b.append(" -> ");
            if (value != null) {
                b.append(value.toString());
            } else {
                b.append("null");
            }

            return (b.toString());
        }

    }

    public T_VALUE put(T_KEY1 key1, T_KEY2 key2, T_VALUE value) {
        assert (key1 != null);
        assert (key2 != null);

        TreeMap<T_KEY2, T_VALUE> key2Map = get(key1);
        if (key2Map == null) {
            key2Map = new TreeMap<>();
            super.put(key1, key2Map);
        }
        return (key2Map.put(key2, value));
    }

    public T_VALUE get(T_KEY1 key1, T_KEY2 key2) {
        assert (key1 != null);
        assert (key2 != null);

        TreeMap<T_KEY2, T_VALUE> key2Map = get(key1);
        if (key2Map != null) {
            return (key2Map.get(key2));
        } else {
            return (null);
        }
    }

    public List<T_VALUE> getValues() {
        List<T_VALUE> result = new ArrayList<>();
        for (TreeMap<T_KEY2, T_VALUE> value2Map : super.values()) {
            for (T_VALUE value : value2Map.values()) {
                result.add(value);
            }
        }
        return (result);
    }

    public Collection<Entry<T_KEY1, T_KEY2, T_VALUE>> getEntrySet() {
        List<Entry<T_KEY1, T_KEY2, T_VALUE>> result = new ArrayList<>();
        for (Map.Entry<T_KEY1, TreeMap<T_KEY2, T_VALUE>> entry1 : entrySet()) {
            for (Map.Entry<T_KEY2, T_VALUE> entry2 : entry1.getValue().entrySet()) {
                result.add(new Entry<>(entry1.getKey(), entry2.getKey(), entry2.getValue()));
            }
        }
        return (result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object otherObject) {

        // Check that other object is a EcvMapMap
        //
        if (otherObject instanceof EcvMapMap == false) {
            return (false);
        }

        // Check that number of key1 is the same
        //
        EcvMapMap otherMapMap = (EcvMapMap) otherObject;
        List<T_KEY1> myKeys1 = new ArrayList<>(keySet());
        List otherKeys1 = new ArrayList(otherMapMap.keySet());
        if (myKeys1.size() != otherKeys1.size()) {
            return (false);
        }

        // Check for each key1
        //
        for (int index1 = 0; index1 < myKeys1.size(); index1++) {

            // Check that key1 are equal
            //
            if (myKeys1.get(index1).equals(otherKeys1.get(index1)) == false) {
                return (false);
            }

            // Check that value in other object is a map
            //
            Map<T_KEY2, T_VALUE> myMap2 = get(myKeys1.get(index1));
            Object otherMapObject2 = otherMapMap.get(otherKeys1.get(index1));
            if (otherMapObject2 instanceof Map == false) {
                return (false);
            }
            Map otherMap2 = (Map) otherMapObject2;

            // Check that number of key2 is the same
            //
            List<T_KEY2> myKeys2 = new ArrayList<>(myMap2.keySet());
            List otherKeys2 = new ArrayList(otherMap2.keySet());
            if (myKeys2.size() != otherKeys2.size()) {
                return (false);
            }

            // Check for each key2
            //
            for (int index2 = 0; index2 < myKeys2.size(); index2++) {

                // Check that key2 are equal
                //
                if (myKeys2.get(index2).equals(otherKeys2.get(index2)) == false) {
                    return (false);
                }

                // Check that value for key2 are equal
                //
                if ((myMap2.get(myKeys2.get(index2)).equals(otherMap2.get(otherKeys2.get(index2)))) == false) {
                    return (false);
                }
            }
        }

        return (true);
    }

}
