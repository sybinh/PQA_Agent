/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Implements a map that contains lists as elements.
 *
 * @author gug2wi
 */
public class EcvIdentityMapList<K, V> extends IdentityHashMap<K, List<V>> {

    public EcvIdentityMapList() {
    }

    public EcvIdentityMapList(K key, V value) {
        add(key, value);
    }

    /**
     * Add a new value to the list of the key.
     *
     * @param key Key for the value.
     * @param value Value object.
     */
    final public EcvIdentityMapList<K, V> add(K key, V value) {
        assert (key != null);
        assert (value != null);
        List<V> l = get(key);
        if (l == null) {
            l = new ArrayList<>();
            put(key, l);
        }
        l.add(value);
        return (this);
    }

    /**
     * Adds only the key without a value for the list.
     *
     * @param key Key for the value.
     */
    final public EcvIdentityMapList<K, V> add(K key) {
        assert (key != null);
        List<V> l = get(key);
        if (l == null) {
            l = new ArrayList<>();
            put(key, l);
        }
        return (this);
    }

    /**
     * Removes the value rom the list of the key.
     *
     * @param key Key for the value.
     * @param value Value that shall be removed from the list.
     * @return true, if the values was removed from the list; false, if the key
     * or the value did not exist in the map.
     */
    final public boolean removeValue(K key, V value) {
        assert (key != null);
        assert (value != null);
        List<V> l = get(key);
        if (l == null) {
            return (false);
        }
        return (l.remove(value));
    }

}
