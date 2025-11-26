/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Implements a tree map that contains another tree map that contains a list of
 * values.
 *
 * The list for a key can be empty.
 *
 * @author gug2wi
 * @param <T_KEY1> Type of the key of the first map.
 * @param <T_KEY2> Type of the key of the second map.
 * @param <T_KEY3> Type of the key of the third map.
 * @param <T_VALUE> Type of the value hold in the set.
 */
public class EcvMapMapMapSet<T_KEY1 extends Comparable<T_KEY1>, T_KEY2 extends Comparable<T_KEY2>, T_KEY3 extends Comparable<T_KEY3>, T_VALUE> extends TreeMap<T_KEY1, TreeMap<T_KEY2, TreeMap<T_KEY3, TreeSet<T_VALUE>>>> {

    public static class Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE> {

        final private T_KEY1 key1;
        final private T_KEY2 key2;
        final private T_KEY3 key3;
        final private T_VALUE value;

        public Entry(T_KEY1 key1, T_KEY2 key2, T_KEY3 key3, T_VALUE value) {
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

    }

    /**
     * Create an empty structure.
     */
    public EcvMapMapMapSet() {
    }

    /**
     * Add a new value to the structure.
     *
     * @param key1 Value for key1.
     * @param key2 Value for key2.
     * @param value Value.
     * @return The structure itself.
     */
    final public EcvMapMapMapSet<T_KEY1, T_KEY2, T_KEY3, T_VALUE> add(T_KEY1 key1, T_KEY2 key2, T_KEY3 key3, T_VALUE value) {
        assert (key1 != null);
        assert (key2 != null);
        assert (key3 != null);
        assert (value != null);

        TreeMap<T_KEY2, TreeMap<T_KEY3, TreeSet<T_VALUE>>> map2 = super.get(key1);
        if (map2 == null) {
            map2 = new TreeMap<>();
            super.put(key1, map2);
        }

        TreeMap<T_KEY3, TreeSet<T_VALUE>> map3 = map2.get(key2);
        if (map3 == null) {
            map3 = new TreeMap<>();
            map2.put(key2, map3);
        }

        TreeSet<T_VALUE> set = map3.get(key3);
        if (set == null) {
            set = new TreeSet<>();
            map3.put(key3, set);
        }

        set.add(value);

        return (this);
    }

    /**
     * Add all values from the source.
     *
     * @param source Source for the values to add.
     */
    final public EcvMapMapMapSet<T_KEY1, T_KEY2, T_KEY3, T_VALUE> addAll(EcvMapMapMapSet<T_KEY1, T_KEY2, T_KEY3, T_VALUE> source) {
        assert (source != null);
        for (Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE> entry : source.entryList()) {
            add(entry.key1, entry.key2, entry.key3, entry.value);
        }
        return (this);
    }

    /**
     * Removes the given entry from the structure
     *
     * @param toRemove Entry that shall be removed.
     * @return true, if the structure was changed by the removal.
     */
    final public boolean remove(Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE> toRemove) {
        boolean changed = false;

        if (toRemove != null) {
            TreeMap<T_KEY2, TreeMap<T_KEY3, TreeSet<T_VALUE>>> map2 = super.get(toRemove.key1);
            if (map2 != null) {
                TreeMap<T_KEY3, TreeSet<T_VALUE>> map3 = map2.get(toRemove.key2);
                if (map3 != null) {
                    TreeSet<T_VALUE> set = map3.get(toRemove.key3);
                    if (set != null) {
                        changed = set.remove(toRemove.value);
                        if (set.isEmpty() == true) {
                            map3.remove(toRemove.key3);
                        }
                    }
                    if (map3.isEmpty() == true) {
                        map2.remove(toRemove.key2);
                    }
                }
                if (map2.isEmpty() == true) {
                    super.remove(toRemove.key1);
                }
            }
        }

        return (changed);
    }

    /**
     * Removes all values in toRemove from the structure.
     *
     * @param toRemove Elements that shall be removed.
     * @return true, if at least one element was removed. false otherwise.
     */
    final public boolean removeAll(EcvMapMapMapSet<T_KEY1, T_KEY2, T_KEY3, T_VALUE> toRemove) {
        boolean changed = false;
        for (Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE> entry : toRemove.entryList()) {
            changed |= remove(entry);
        }
        return (changed);
    }

//    @SuppressWarnings("unchecked")
    final public List<Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE>> entryList() {

        List<Entry<T_KEY1, T_KEY2, T_KEY3, T_VALUE>> result = new ArrayList<>();

        super.entrySet().forEach((entry1) -> {
            entry1.getValue().entrySet().forEach((entry2) -> {
                entry2.getValue().entrySet().forEach((entry3) -> {
                    entry3.getValue().forEach((value) -> {
                        result.add(new Entry<>(entry1.getKey(), entry2.getKey(), entry3.getKey(), value));
                    });
                });
            });
        });
        return (result);
    }

}
