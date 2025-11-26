/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Defines the interface of a field that contains one DmElement.
 *
 * @author gug2wi
 * @param <DM_ELEMENT_I> Type of the element stored in the field.
 */
public interface DmElementField_ReadOnlyI<DM_ELEMENT_I extends DmElementI> extends DmFieldI {

    /**
     * Returns the element referenced by the field or null if no element is
     * referenced.
     *
     * @return The referenced element or null.
     */
    DM_ELEMENT_I getElement();

    //
    // Trigger the update of the reference from the database.
    //
    default void reload() {
    }

    @Override
    default boolean isReadOnly() {
        return (true);
    }

    /**
     * Checks whether or not an element is set on the reference field. This
     * check should be overwritten with a method that does not need database
     * access, if this is possible. It is therefore the preferred solution if
     * only the check is needed.
     *
     * @return true, if an element is set; false otherwise.
     */
    default boolean isElementSet() {
        return (getElement() != null);
    }

}
