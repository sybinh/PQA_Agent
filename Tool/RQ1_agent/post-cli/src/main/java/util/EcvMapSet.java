/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Implements a map that contains a set of values for each key.
 *
 * The set for a key can be empty.
 *
 * @author gug2wi
 */
public class EcvMapSet<K extends Comparable<K>, V> extends TreeMap<K, TreeSet<V>> {

    /**
     * Create an empty map list.
     */
    public EcvMapSet() {
    }

    /**
     * Create a map list initialized with a key and a value.
     *
     * @param key
     * @param value
     */
    public EcvMapSet(K key, V value) {
        add(key, value);
    }

    /**
     * Create a map list initialized with the list of keys.
     *
     * @param keys
     *
     */
    public EcvMapSet(List<K> keys) {
        assert (keys != null);
        for (K key : keys) {
            add(key);
        }
    }

    /**
     * Add a new value to the set of the key. If the key is new, then a new
     * entry is added.
     *
     * @param key Key for the value.
     * @param value Value object.
     */
    final public EcvMapSet<K, V> add(K key, V value) {
        assert (key != null);
        assert (value != null);
        TreeSet<V> l = get(key);
        if (l == null) {
            l = new TreeSet<>();
            put(key, l);
        }
        l.add(value);
        return (this);
    }

    /**
     * Adds the key value pair, if the value is not null.
     *
     * @param key Key for the value.
     * @param value Value to add.
     * @return true, if the value is not null; false otherwise.
     */
    final public boolean addValueIfNotNull(K key, V value) {
        assert (key != null);
        if (value != null) {
            add(key, value);
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Add a collection of values to the set of the key. If the key is new, then
     * a new entry is added.
     *
     * @param key Key for the value.
     * @param value Value object.
     */
    final public EcvMapSet<K, V> addAll(K key, Collection<? extends V> value) {
        assert (key != null);
        assert (value != null);
        TreeSet<V> l = get(key);
        if (l == null) {
            l = new TreeSet<>();
            put(key, l);
        }
        l.addAll(value);
        return (this);
    }

    /**
     * Ensures that the key exists in the map. If the key is new, then a new
     * entry with an empty value set is created.
     *
     * @param key Key to be added..
     */
    final public EcvMapSet<K, V> add(K key) {
        assert (key != null);
        TreeSet<V> l = get(key);
        if (l == null) {
            l = new TreeSet<>();
            put(key, l);
        }
        return (this);
    }

    /**
     * Removes the value for the key.
     *
     * @param key
     * @param value
     * @return
     */
    final public boolean removeValue(K key, V value) {
        assert (key != null);
        assert (value != null);
        TreeSet<V> l = get(key);
        if (l != null) {
            return (l.remove(value));
        }
        return (false);
    }

    final public boolean containsValue(K key, V value) {
        assert (key != null);
        assert (value != null);
        TreeSet<V> l = get(key);
        if (l != null) {
            return (l.contains(value));
        }
        return (false);
    }

    /**
     * Adds the value to the key, if the value is not yet in the list.
     *
     * @param key
     * @param value
     * @return
     */
    final public boolean addValueUnique(K key, V value) {
        assert (key != null);
        assert (value != null);
        TreeSet<V> l = get(key);
        if (l == null) {
            l = new TreeSet<>();
            l.add(value);
            put(key, l);
            return true;
        } else {
            if (l.contains(value) == false) {
                l.add(value);
                return true;
            }
        }
        return (false);
    }

    /**
     * Adds the values of the given mapSet to the calling EcvMapSet. As
     * intended, duplicate values are skipped.
     *
     * @param mapSet mapSet, which entries are added to the calling set
     * @return true if added sucessfully otherwise false
     */
    final public boolean add(EcvMapSet<K, V> mapSet) {
        assert (mapSet != null);
        boolean checker = false;
        for (Entry<K, TreeSet<V>> entry : mapSet.entrySet()) {
            for (V set : entry.getValue()) {
                this.add(entry.getKey(), set);
                checker = true;
            }
        }
        return checker;
    }

}
