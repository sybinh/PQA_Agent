/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import java.util.List;

/**
 * Defines the interface of a field that contains a list of DmElement.
 *
 * <P>
 * An reference field is a field that contains the reference to one or more
 * elements.
 *
 * @see DmValueField
 * @author gug2wi
 * @param <T_ELEMENT> Type of elements in the list of the field.
 */
public interface DmElementListField_ReadOnlyI<T_ELEMENT extends DmElementI> extends DmFieldI {

    /**
     * Returns the elements of the list.
     *
     * @return The elements of the list.
     */
    List<T_ELEMENT> getElementList();

    /**
     * Triggers the reload of the list from the source.
     */
    void reload();

    @Override
    public default boolean isReadOnly() {
        return (true);
    }

}
