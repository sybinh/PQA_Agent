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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements a map that contains a list of values for each key.
 *
 * The list for a key can be empty.
 *
 * @author gug2wi
 * @param <KEY> Type of the key for the map.
 * @param <VALUE> Type of the elements in the list for a key.
 */
public class EcvLinkedHashMapList<KEY extends Comparable<KEY>, VALUE> extends LinkedHashMap<KEY, List<VALUE>> {

    static public class MapListEntry<KEY, VALUE> {

        final public KEY key;
        final public VALUE value;

        MapListEntry(KEY key, VALUE value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return (key.toString() + " -> " + value.toString());
        }
    }

    /**
     * Create an empty map list.
     */
    public EcvLinkedHashMapList() {
    }

    /**
     * Create a map list initialized with a key and a value.
     *
     * @param key
     * @param value
     */
    public EcvLinkedHashMapList(KEY key, VALUE value) {
        add(key, value);
    }

    /**
     * Create a map list initialized with the list of keys.
     *
     * @param keys
     *
     */
    public EcvLinkedHashMapList(Collection<KEY> keys) {
        assert (keys != null);
        for (KEY key : keys) {
            add(key);
        }
    }

    /**
     * Add a new value to the list of the key. If the key is new, then a new
     * entry is added. The value is added to the end of the list for existing
     * keys.
     *
     * @param key Key for the value.
     * @param value Value object.
     */
    final public EcvLinkedHashMapList<KEY, VALUE> add(KEY key, VALUE value) {
        assert (key != null);
        assert (value != null);
        List<VALUE> l = get(key);
        if (l == null) {
            l = new ArrayList<>();
            put(key, l);
        }
        l.add(value);
        return (this);
    }

    final public EcvLinkedHashMapList<KEY, VALUE> addAll(KEY key, List<? extends VALUE> value) {
        assert (key != null);
        assert (value != null);
        List<VALUE> l = get(key);
        if (l == null) {
            l = new ArrayList<>();
            put(key, l);
        }
        l.addAll(value);
        return (this);
    }

    /**
     * Ensures that the key exists in the map. If the key is new, then a new
     * entry with an empty values list is created.
     *
     * @param key Key to be added..
     */
    final public EcvLinkedHashMapList<KEY, VALUE> add(KEY key) {
        assert (key != null);
        List<VALUE> l = get(key);
        if (l == null) {
            l = new ArrayList<>();
            put(key, l);
        }
        return (this);
    }

    /**
     * Removes the first occurrence of the value for the key.
     *
     * @param key
     * @param value
     * @return
     */
    final public boolean removeValue(KEY key, VALUE value) {
        assert (key != null);
        assert (value != null);
        List<VALUE> l = get(key);
        if (l != null) {
            return (l.remove(value));
        }
        return (false);
    }

    /**
     * Checks if a value for a key exists in the map.
     *
     * @param key Key for which the values shall be checked.
     * @param value Value to find.
     * @return true if found; false otherwise
     */
    final public boolean containsValue(KEY key, VALUE value) {
        assert (key != null);
        assert (value != null);
        List<VALUE> l = get(key);
        if (l != null) {
            return (l.contains(value));
        }
        return (false);
    }

    /**
     * Finds the first key for the value.
     *
     * @param value Value to find.
     * @return The key, if found; null otherwise;
     */
    final public KEY getKey(VALUE value) {
        assert (value != null);

        for (Map.Entry<KEY, List<VALUE>> entry : entrySet()) {
            if (entry.getValue().contains(value)) {
                return (entry.getKey());
            }
        }
        return (null);
    }

    /**
     * Adds the value to the key, if the value is not yet in the list of the
     * key.
     *
     * @param key Key for which the values shall be added.
     * @param value Value that shall be added.
     * @return true, if the values was added (meaning that the values was not
     * yet in the list of the key).
     */
    final public boolean addValueUnique(KEY key, VALUE value) {
        assert (key != null);
        assert (value != null);
        List<VALUE> l = get(key);
        if (l == null) {
            l = new ArrayList<>();
            l.add(value);
            put(key, l);
            return (true);
        } else {
            if (l.contains(value) == false) {
                l.add(value);
                return (true);
            }
        }
        return (false);
    }

    /**
     * Returns the first value for the given key and removes the value from the
     * list for the key. The key is removed, if the last value was taken from
     * the list.
     *
     * @param key Key for which the first values shall be fetched.
     * @return The value or null if no value exists for the key.
     */
    final public VALUE removeFirst(KEY key) {
        assert (key != null);
        List<VALUE> l = get(key);
        if ((l != null) && (l.isEmpty() == false)) {
            VALUE v = l.remove(0);
            if (l.isEmpty() == true) {
                remove(key);
            }
            return (v);
        }
        return (null);
    }

    final public List<MapListEntry<KEY, VALUE>> getContentAsList() {
        List<MapListEntry<KEY, VALUE>> result = new ArrayList<>();
        for (Map.Entry<KEY, List<VALUE>> entry : super.entrySet()) {
            if (entry.getValue() != null) {
                for (VALUE value : entry.getValue()) {
                    result.add(new MapListEntry<>(entry.getKey(), value));
                }
            }
        }
        return (result);
    }

}
