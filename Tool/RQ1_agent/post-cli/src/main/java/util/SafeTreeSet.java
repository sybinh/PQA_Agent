/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implements a set with two safety features:<br>
 * - Accepts null values in constructor and for addAll(). <br>
 * - Provides the content of the set as immutable set.
 *
 * @author gug2wi
 * @param <T> Type of the managed collection.
 */
public class SafeTreeSet<T> implements Iterable<T> {

    final private Set<T> set;
    private Set<T> immutable = null;

    public SafeTreeSet() {
        set = new TreeSet<>();
    }

    final public boolean isEmpty() {
        return (getImmutableSet().isEmpty());
    }

    public synchronized void clear() {
        set.clear();
    }

    public synchronized void add(T element) {
        set.add(element);
    }

    public synchronized void remove(T element) {
        set.remove(element);
    }

    final public boolean contains(T element) {
        return (getImmutableSet().contains(element));
    }

    final public Set<T> getImmutableSet() {
        Set<T> i = immutable;
        if (i != null) {
            return (i);
        }
        return (createImmutableSet());
    }

    private synchronized Set<T> createImmutableSet() {
        if (immutable == null) {
            immutable = Collections.unmodifiableSet(set);
        }
        return (immutable);
    }

    @Override
    public Iterator<T> iterator() {
        return (getImmutableSet().iterator());
    }

}
