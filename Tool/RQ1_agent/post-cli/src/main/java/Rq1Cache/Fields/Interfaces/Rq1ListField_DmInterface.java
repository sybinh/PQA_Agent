/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields.Interfaces;

import Rq1Cache.Fields.Rq1FieldI;

/**
 *
 * @author gug2wi
 */
public interface Rq1ListField_DmInterface<T> extends Rq1FieldI<T[]>, Rq1ListI {

    /**
     * Add the element to the list.
     *
     * @param newElement Element that shall be added.
     */
    void addElement(T newElement);

    /**
     * Removes the first occurrence of an element from the list.
     *
     * @param elementToRemove Element that shall be removed.
     * @return <ul>
     * <li> true, if the element was found and removed
     * <li> false, if the element was not found.
     * </ul>
     */
    boolean removeElement(T elementToRemove);

}
