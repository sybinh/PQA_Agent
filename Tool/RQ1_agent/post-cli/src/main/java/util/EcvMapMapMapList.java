/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.*;

/**
 * Implements a map that contains a list of values for each key.
 *
 * The list for a key can be empty.
 *
 * @author trb83wi
 * @param <KEY1> Type of the key of the first map.
 * @param <KEY2> Type of the key of the second map.
*  @param <KEY3> Type of the key of the third map.
 * @param <VALUE> Type of the elements in the list for a key.
 */
public class EcvMapMapMapList<KEY1 extends Comparable<KEY1>, KEY2 extends Comparable<KEY2> , KEY3 extends Comparable<KEY3>, VALUE> extends TreeMap<KEY1, TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>>> {

    static public class MapMapMapListEntry<KEY1, KEY2, KEY3, VALUE> {

        final public KEY1 key1;
        final public KEY2 key2;
        final public KEY3 key3;
        final public VALUE value;

        MapMapMapListEntry(KEY1 key1, KEY2 key2, KEY3 key3,VALUE value) {
            this.key1 = key1;
            this.key2 = key2;
            this.key3 = key3;
            this.value = value;
        }
    }

    /**
     * Create an empty map list.
     */
    public EcvMapMapMapList() {
    }

    /**
     * Add a new value to the list for the keys. If the keys are new, then a new
     * entry is added. The value is added to the end of the list for existing
     * keys.
     *
     * @param key1 Key1 for which the values shall be added.
     * @param key2 Key2 for which the values shall be added.
     * @param key2 Key2 for which the values shall be added.
     * @param key3 Key3 for which the values shall be added.
     * @param value Value object.
     * @return This map map list.
     */
    final public EcvMapMapMapList<KEY1, KEY2, KEY3, VALUE> add(KEY1 key1, KEY2 key2, KEY3 key3, VALUE value) {
        assert (key1 != null);
        assert (key2 != null);
        assert (value != null);

        TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>>  map2 = get(key1);
        if (map2 == null) {
            map2 = new TreeMap<>();
            put(key1, map2);
        }

        TreeMap<KEY3, List<VALUE>>  map3 = map2.get(key2);
        if (map3 == null) {
            map3 = new TreeMap<>();
            map2.put(key2, map3);
        }

        List<VALUE> l = map3.get(key3);
        if (l == null) {
            l = new ArrayList<>();
            map3.put(key3, l);
        }

        l.add(value);
        return (this);
    }

    /**
     * Add new values to the list for the keys. If the keys are new, then a new
     * entry is added. The value is added to the end of the list for existing
     * keys.
     *
     * @param key1 Key1 for which the values shall be added.
     * @param key2 Key2 for which the values shall be added.
     * @param key3 Key3 for which the values shall be added.
     * @param value The values that shall be added to the keys.
     * @return This map map list.
     */
    final public EcvMapMapMapList<KEY1, KEY2, KEY3, VALUE> addAll(KEY1 key1, KEY2 key2, KEY3 key3, Collection<VALUE> value) {
        assert (key1 != null);
        assert (key2 != null);
        assert (value != null);


        TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>>  map2 = get(key1);
        if (map2 == null) {
            map2 = new TreeMap<>();
            put(key1, map2);
        }

        TreeMap<KEY3, List<VALUE>>  map3 = map2.get(key2);
        if (map3 == null) {
            map3 = new TreeMap<>();
            map2.put(key2, map3);
        }

        List<VALUE> l = map3.get(key3);
        if (l == null) {
            l = new ArrayList<>();
            map3.put(key3, l);
        }

        l.addAll(value);

        return (this);
    }

    /**
     * Adds the value to the keys, if the value is not yet in the list for the
     * keys.
     *
     * @param key1 Key1 for which the values shall be added.
     * @param key2 Key2 for which the values shall be added.
     * @param key3 Key2 for which the values shall be added.
     * @param value Value that shall be added.
     * @return true, if the values was added (meaning that the values was not
     * yet in the list of the key).
     */
    public boolean addValueUnique(KEY1 key1, KEY2 key2, KEY3 key3, VALUE value) {
        assert (key1 != null);
        assert (key2 != null);
        assert (value != null);


        TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>>  map2 = get(key1);
        if (map2 == null) {
            map2 = new TreeMap<>();
            put(key1, map2);
        }

        TreeMap<KEY3, List<VALUE>>  map3 = map2.get(key2);
        if (map3 == null) {
            map3 = new TreeMap<>();
            map2.put(key2, map3);
        }

        List<VALUE> l = map3.get(key3);
        if (l == null) {
            l = new ArrayList<>();
            map3.put(key3, l);
        }

        if (l.contains(value) == false) {
            l.add(value);
            return (true);
        }

        return (false);
    }

    /**
     * Removes the given entry from the map map list.
     *
     * @param entry Entry that shall be removed.
     * @return true, if the entry was found and removed; false if the entry was
     * not in the map map list.
     */
    final public boolean removeEntry(MapMapMapListEntry<KEY1, KEY2, KEY3, VALUE> entry) {
        assert (entry != null);

        TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>> map2 = get(entry.key1);
        if (map2 != null) {
            TreeMap<KEY3, List<VALUE>> map3 = map2.get(entry.key2);
            if (map3 != null) {
                List<VALUE> l = map3.get(entry.key3);
                if (l != null) {
                    boolean found = l.remove(entry.value);
                    return (found);
                }
            }
        }
        return (false);
    }

    /**
     * Removes the defined entry from the map map list.
     *
     *
     * @return true, if the entry was found and removed; false if the entry was
     * not in the map map list.
     */
    final public boolean removeEntry(KEY1 key1, KEY2 key2, KEY3 key3, VALUE value) {
        assert (key1 != null);
        assert (key2 != null);

        TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>> map2 = get(key1);
        if (map2 != null) {
            TreeMap<KEY3, List<VALUE>> map3 = map2.get(key2);
            if (map3 != null) {
                List<VALUE> l = map3.get(key3);
                if (l != null) {
                    boolean found = l.remove(value);
                    return (found);
                }
            }
        }
        return (false);
    }

    /**
     * Returns the list of values currently stored for the given keys.
     *
     * @param key1 Key1 for which the list shall be returned.
     * @param key2 Key2 for which the list shall be returned.
     * @param key2 Key2 for which the list shall be returned.
     * @return null, if no list exists for this keys; Otherwise the list.
     */
    final public List<VALUE> get(KEY1 key1, KEY2 key2, KEY3 key3) {
        assert (key1 != null);
        assert (key2 != null);

        TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>> map2 = get(key1);
        if (map2 != null) {
            TreeMap<KEY3, List<VALUE>> map3 = map2.get(key2);
            if (map3 != null) {
                return (map3.get(key3));
            }
        }
        return (null);
    }

    /**
     * Returns the list of values currently stored for the given keys and
     * removes it from the map map list.
     *
     * @param key1 Key1 for which the list shall be removed
     * @param key2 Key2 for which the list shall be removed.
     * @param key3 Key3 for which the list shall be removed.
     * @return null, if no list exists for this keys; Otherwise the list.
     */
    final public List<VALUE> removeList(KEY1 key1, KEY2 key2, KEY3 key3) {
        assert (key1 != null);
        assert (key2 != null);
        assert (key3 != null);

        TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>> map2 = get(key1);
        if (map2 != null) {
            TreeMap<KEY3, List<VALUE>> map3 = map2.get(key2);
            if (map3 != null) {
                return (map3.remove(key3));
            }
        }
        return (null);
    }

    final public MapMapMapListEntry<KEY1, KEY2, KEY3, VALUE> getFirstEntry(VALUE value) {
        assert (value != null);

        for (Map.Entry<KEY1, TreeMap<KEY2, TreeMap<KEY3,List<VALUE>>>> entry1 : entrySet()) {
            for (Map.Entry<KEY2, TreeMap<KEY3, List<VALUE>>> entry2 : entry1.getValue().entrySet()) {
                for (Map.Entry<KEY3, List<VALUE>> entry3 : entry2.getValue().entrySet()) {
                    if (entry3.getValue().contains(value) == true) {
                        MapMapMapListEntry<KEY1, KEY2, KEY3, VALUE> result = new MapMapMapListEntry<>(entry1.getKey(), entry2.getKey(), entry3.getKey(), value);
                        return (result);
                    }
                }
            }
        }
        return (null);

    }

    /**
     * Returns the content of the map map as a list.
     *
     * @return The content in ordered form as list of entries.
     */
    final public List<MapMapMapListEntry<KEY1, KEY2, KEY3, VALUE>> getContentAsList() {

        List<MapMapMapListEntry<KEY1, KEY2, KEY3, VALUE>> result = new ArrayList<>();

        for (Map.Entry<KEY1, TreeMap<KEY2, TreeMap<KEY3, List<VALUE>>>> entry1 : entrySet()) {
            for (Map.Entry<KEY2, TreeMap<KEY3, List<VALUE>>> entry2 : entry1.getValue().entrySet()) {
                if (entry1.getValue() != null) {
                    for (Map.Entry<KEY3, List<VALUE>> entry3 : entry2.getValue().entrySet()) {
                        if (entry3.getValue() != null) {
                            for (VALUE value : entry3.getValue()) {
                                result.add(new MapMapMapListEntry<>(entry1.getKey(), entry2.getKey(), entry3.getKey(), value));
                            }
                        }
                    }
                }
            }
        }

        return (result);
    }

}
