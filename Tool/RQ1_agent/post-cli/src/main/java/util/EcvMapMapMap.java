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
public class EcvMapMapMap<T_KEY1 extends Comparable<T_KEY1>, T_KEY2 extends Comparable<T_KEY2>, T_KEY3 extends Comparable<T_KEY3>, T_VALUE>
        extends TreeMap<T_KEY1, TreeMap<T_KEY2, TreeMap<T_KEY3, T_VALUE>>> {

    public static class Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE> {

        final private T_KEY1 key1;
        final private T_KEY2 key2;
        final private T_KEY3 key3;
        final private T_VALUE value;

        public Entry(T_KEY1 key1, T_KEY2 key2, T_KEY3 key3, T_VALUE value) {
            assert (key1 != null);
            assert (key2 != null);
            assert (key3 != null);
            this.key1 = key1;
            this.key2 = key2;
            this.key3 = key3;
            this.value = value;
        }

        public T_KEY1 getKey1() {
            return key1;
        }

        public T_KEY2 getKey2() {
            return key2;
        }

        public T_KEY3 getKey3() {
            return key3;
        }

        public T_VALUE getValue() {
            return value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(key1.toString());
            b.append(" -> ");
            b.append(key2.toString());
            b.append(" -> ");
            b.append(key3.toString());
            b.append(" -> ");
            b.append(value.toString());
            return (b.toString());
        }

    }

    public T_VALUE put(T_KEY1 key1, T_KEY2 key2, T_KEY3 key3, T_VALUE value) {
        assert (key1 != null);
        assert (key2 != null);
        assert (key3 != null);

        TreeMap<T_KEY2, TreeMap<T_KEY3, T_VALUE>> key2Map = get(key1);
        if (key2Map == null) {
            key2Map = new TreeMap<>();
            super.put(key1, key2Map);
        }
        TreeMap<T_KEY3, T_VALUE> key3Map = key2Map.get(key2);
        if (key3Map == null) {
            key3Map = new TreeMap<>();
            key2Map.put(key2, key3Map);
        }
        return (key3Map.put(key3, value));
    }

    public T_VALUE get(T_KEY1 key1, T_KEY2 key2, T_KEY3 key3) {
        assert (key1 != null);
        assert (key2 != null);
        assert (key3 != null);

        TreeMap<T_KEY2, TreeMap<T_KEY3, T_VALUE>> key2Map = get(key1);
        if (key2Map != null) {
            TreeMap<T_KEY3, T_VALUE> key3Map = key2Map.get(key2);
            if (key3Map != null) {
                return (key3Map.get(key3));
            }
        }
        return (null);
    }

    public List<T_VALUE> getValues() {
        List<T_VALUE> result = new ArrayList<>();
        for (TreeMap<T_KEY2, TreeMap<T_KEY3, T_VALUE>> value2Map : super.values()) {
            for (TreeMap<T_KEY3, T_VALUE> value3Map : value2Map.values()) {
                for (T_VALUE value : value3Map.values()) {
                    result.add(value);
                }
            }
        }
        return (result);
    }

    public Collection<Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE>> getEntrySet() {
        List<Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE>> result = new ArrayList<>();
        for (Map.Entry<T_KEY1, TreeMap<T_KEY2, TreeMap<T_KEY3, T_VALUE>>> entry1 : super.entrySet()) {
            for (Map.Entry<T_KEY2, TreeMap<T_KEY3, T_VALUE>> entry2 : entry1.getValue().entrySet()) {
                for (Map.Entry<T_KEY3, T_VALUE> entry3 : entry2.getValue().entrySet()) {
                    result.add(new Entry<>(entry1.getKey(), entry2.getKey(), entry3.getKey(), entry3.getValue()));
                }
            }
        }
        return (result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object otherObject) {

        // Check that other object is a EcvMapMap
        //
        if (otherObject instanceof EcvMapMapMap == false) {
            return (false);
        }

        // Check that number of key1 is the same
        //
        EcvMapMapMap otherMapMap = (EcvMapMapMap) otherObject;
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
            TreeMap<T_KEY2, TreeMap<T_KEY3, T_VALUE>> myMap2 = get(myKeys1.get(index1));
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

                // Check that value in other object is a map
                //
                TreeMap<T_KEY3, T_VALUE> myMap3 = myMap2.get(myKeys2.get(index2));
                Object otherMapObject3 = otherMap2.get(otherKeys2.get(index2));
                if (otherMapObject3 instanceof Map == false) {
                    return (false);
                }
                Map otherMap3 = (Map) otherMapObject3;

                // Check that number of key3 is the same
                //
                List<T_KEY3> myKeys3 = new ArrayList<>(myMap3.keySet());
                List otherKeys3 = new ArrayList(otherMap3.keySet());
                if (myKeys3.size() != otherKeys3.size()) {
                    return (false);
                }

                // Check for each key3
                //
                for (int index3 = 0; index3 < myKeys3.size(); index3++) {
                    // Check that value for key3 are equal
                    //
                    if ((myMap3.get(myKeys3.get(index3)).equals(otherMap3.get(otherKeys3.get(index3)))) == false) {
                        return (false);
                    }
                }

            }
        }

        return (true);
    }

}
