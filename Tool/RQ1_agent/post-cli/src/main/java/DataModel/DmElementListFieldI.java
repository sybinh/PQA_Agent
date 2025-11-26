/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Defines the interface of a field that contains a list of DmElement.
 *
 * @author gug2wi
 * @param <T_ELEMENT> Type of elements in the list of the field.
 */
public interface DmElementListFieldI<T_ELEMENT extends DmElementI> extends DmElementListField_ReadOnlyI<T_ELEMENT> {

    /**
     * Adds the given element to the list.
     *
     * @param element Element that shall be added.
     */
    void addElement(T_ELEMENT element);

}
