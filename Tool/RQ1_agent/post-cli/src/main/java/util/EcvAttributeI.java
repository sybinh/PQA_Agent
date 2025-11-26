/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 * Interface for the handling of arbitrary attributes. An object that supports
 * this attribute has to hold a list of attribute objects which can be
 * manipulated and examined with the methods defined in this interface.
 *
 * @author gug2wi
 */
public interface EcvAttributeI {

    /**
     * Adds an object as attribute to the field. Objects in the attribute list
     * which are equals to the newAttribute are removed.
     *
     * @param newAttribute Object that shall be added to the attribute list.
     */
    void setAttribute(Object newAttribute);

    /**
     * Adds two objects as attributes to the field. Objects in the attribute
     * list which are equals to one of the new attribute are removed.
     *
     * @param newAttribute1
     * @param newAttribute2
     */
    void setAttribute(Object newAttribute1, Object newAttribute2);

    /**
     * Checks, if an object that equals to the requested Attribute exists in the
     * attribute list.
     *
     * @param lockupAttribute Attribute to look for.
     * @return
     * <p>
     * true ... if an equal attribute was found
     * <p>
     * false ... Otherwise
     */
    boolean hasAttribute(Object lockupAttribute);

}
