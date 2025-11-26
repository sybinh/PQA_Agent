/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author CIL83WI
 * @param <T>
 */
public class FIFOItemQueueList<T> extends LinkedList<T> {

    final private int maxQueueSize;

    public FIFOItemQueueList(int size) {
        super();
        assert (size > 0);
        this.maxQueueSize = size;
    }

    /**
     *
     * Adds element to the list if it is not already in the list it is moved to
 the end of the list If the List is larger than the defined maxQueueSize the first
 element is removed
     *
     * @param element
     * @return
     */
    @Override
    public boolean add(T element) {
        if (element != null) {
            if (!super.contains(element)) {
                super.addLast(element);
            } else {
                super.remove(element);
                super.addLast(element);
            }

            while (super.size() > maxQueueSize) {
                super.removeFirst();
            }
            return true;
        }
        return false;
    }

    /**
     * Adds element from a given sourcelist as long as queuelist does not
 reached maxQueueSize and there are still the entries in the sourcelist

 In the queuelist contained entries are not added
     *
     * @param sourceList
     */
    public void completeQueueListWithElements(Set<T> sourceList) {
        if (super.size() < maxQueueSize) {
            for (T object : sourceList) {
                if (super.size() < maxQueueSize && !super.contains(object)) {
                    super.add(object);
                }
            }
        }

    }
}
