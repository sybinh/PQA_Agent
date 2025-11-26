/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Implements a list with two safety features:<br>
 * - Accepts null values in constructor and for addAll(). <br>
 * - Provides the content of the list as immutable list. This prevents
 * concurrent read and write access to the list. <br>
 * - The advantage against the CopyOnWriteList is, that not each writing
 * triggers a copy of the whole list.
 *
 * @author gug2wi
 * @param <T> Type of the managed collection.
 */
public class SafeArrayList<T> implements Iterable<T> {

    final private List<T> list;
    private List<T> immutable = null;

    public SafeArrayList() {
        list = new ArrayList<>();
    }

    public SafeArrayList(Collection<? extends T> collection) {
        list = new ArrayList<>();
        addAll(collection);
    }

    final public synchronized boolean add(T e) {
        immutable = null;
        return (list.add(e));
    }

    final public synchronized boolean addAll(Collection<? extends T> collectionToAdd) {
        if (collectionToAdd != null) {
            immutable = null;
            return (list.addAll(collectionToAdd));
        } else {
            return (false);
        }
    }

    final public synchronized boolean remove(T e) {
        immutable = null;
        return (list.remove(e));
    }

    final public synchronized void clear() {
        immutable = null;
        list.clear();
    }

    final public synchronized boolean contains(T e) {
        return (list.contains(e));
    }

    final public List<T> getImmutableList() {
        List<T> i = immutable;
        if (i != null) {
            return (i);
        }
        return (createImmutableList());
    }

    private synchronized List<T> createImmutableList() {
        if (immutable == null) {
            immutable = Collections.unmodifiableList(new ArrayList<>(list));
        }
        return (immutable);
    }

    @Override
    public Iterator<T> iterator() {
        return (getImmutableList().iterator());
    }

    final public synchronized boolean isEmpty() {
        return (list.isEmpty());
    }

    final public synchronized int size() {
        return (list.size());
    }

    final public synchronized T get(int columnIndex) {
        return (list.get(columnIndex));
    }

    /**
     * Reverse the order of elements in the list.
     */
    final public synchronized void reverse() {
        immutable = null;
        Collections.reverse(list);
    }

    /**
     * Sorts the sublist of the list with the given comparator.
     *
     * @param comparator Comparator that shall be used for Collections.sort().
     * @param fromIndex Used to create the sub list via List.subList().
     * @param toIndex Used to create the sub list via List.subList().
     */
    final public synchronized void sort(Comparator<T> comparator, int fromIndex, int toIndex) {
        assert (comparator != null);
        immutable = null;
        Collections.sort(list.subList(fromIndex, toIndex), comparator);
    }

}
