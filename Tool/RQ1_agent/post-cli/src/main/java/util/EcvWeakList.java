/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements a thread safe list of WeakReference&lt;T&gt; which automatically
 * removes non existing elements.
 *
 * @author gug2wi
 * @param <T> Type of element managed by the list.
 */
public class EcvWeakList<T> {

    final private ArrayList<WeakReference<T>> list;

    public EcvWeakList() {
        list = new ArrayList<>();
    }

    public EcvWeakList(int initialSize) {
        assert (initialSize >= 0);
        list = new ArrayList<>(initialSize);
    }

    /**
     * Adds a new element, if it is not yet in the list.
     *
     * @param newElement Element to add.
     * @return true, if the element was added. false, if the element was already
     * in the list.
     */
    public synchronized boolean add(T newElement) {
        assert (newElement != null);

        for (WeakReference<T> ref : list) {
            if (ref.get() == newElement) {
                return (false);
            }
        }
        list.add(new WeakReference<>(newElement));
        return (true);
    }

    /**
     * Returns the element from the list
     *
     * @param element Element to remove.
     * @return true, if the element was removed; false, if the element was not
     * in the list.
     */
    public synchronized boolean remove(T element) {
        assert (element != null);
        Iterator<WeakReference<T>> i = list.iterator();
        while (i.hasNext()) {
            T l = i.next().get();
            if ((l == element) || (l == null)) {
                i.remove();
                return (true);
            }
        }
        return (false);
    }

    /**
     * Returns a copy of the list. The returned list is a new list, not
     * connected to the object.
     *
     * @return Snapshot of the list.
     */
    public synchronized List<T> getCopy() {
        ArrayList<T> copiedList = new ArrayList<>(list.size());

        for (Iterator<WeakReference<T>> it = list.iterator(); it.hasNext();) {
            //
            // Send only to existing listeners and remove non existing from the list.
            //
            WeakReference<T> weakReference = it.next();
            T element = weakReference.get();
            if (element == null) {
                it.remove();
            } else {
                copiedList.add(element);
            }
        }

        return (copiedList);
    }

    /**
     * Removes all elements from the list.
     */
    public synchronized void clear() {
        list.clear();
    }

    public boolean isEmpty() {
        return (list.isEmpty());
    }

}
