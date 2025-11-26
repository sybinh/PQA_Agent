/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implements a identity hash map that contains a map .
 *
 * The set for a key can be empty.
 *
 * @author gug2wi
 */
public class EcvIdMapMap<K1, K2 extends Comparable<K2>, V> extends IdentityHashMap<K1, TreeMap<K2, V>> {

    static public class Entry<K1, K2, V> {

        final private K1 key1;
        final private K2 key2;
        final private V value;

        private Entry(K1 key1, K2 key2, V value) {
            assert (key1 != null);
            assert (key2 != null);
            assert (value != null);

            this.key1 = key1;
            this.key2 = key2;
            this.value = value;
        }

        public K1 getKey1() {
            return key1;
        }

        public K2 getKey2() {
            return key2;
        }

        public V getValue() {
            return value;
        }
    }

    public EcvIdMapMap() {

    }

    public void add(Entry<K1, K2, V> entry) {
        add(entry.getKey1(), entry.getKey2(), entry.getValue());
    }

    public void add(K1 key1, K2 key2, V value) {
        assert (key1 != null);
        assert (key2 != null);
        assert (value != null);

        TreeMap<K2, V> m = this.get(key1);
        if (m == null) {
            m = new TreeMap<>();
            put(key1, m);
        }
        m.put(key2, value);
    }

    public void remove(Entry<K1, K2, V> entry) {
        remove(entry.key1, entry.key2);
    }

    public void remove(K1 key1, K2 key2) {
        assert (key1 != null);
        assert (key2 != null);

        TreeMap<K2, V> m = this.get(key1);
        if (m != null) {
            m.remove(key2);
            if (m.isEmpty()) {
                this.remove(key1);
            }
        }
    }

    /**
     * Returns all values from the map map.
     *
     * @return
     */
    public List<V> getValues() {
        List<V> result = new ArrayList<>();
        for (TreeMap<K2, V> m : values()) {
            result.addAll(m.values());
        }
        return (result);
    }

    public List<V> getValues(K1 key1) {
        List<V> result = new ArrayList<>();

        TreeMap<K2, V> m = this.get(key1);
        if (m != null) {
            result.addAll(m.values());
        }
        return (result);
    }

    public V getValue(K1 key1, K2 key2) {
        TreeMap<K2, V> m = this.get(key1);
        if (m != null) {
            return (m.get(key2));
        }
        return (null);
    }

    public Entry<K1, K2, V> getFirstEntry(K1 key1) {
        TreeMap<K2, V> m = this.get(key1);
        if (m != null) {
            for (Map.Entry<K2, V> entry : m.entrySet()) {
                return (new Entry<>(key1, entry.getKey(), entry.getValue()));
            }
        }
        return (null);
    }

}
