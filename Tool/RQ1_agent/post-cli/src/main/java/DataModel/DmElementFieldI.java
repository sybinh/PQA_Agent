/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
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
public interface DmElementFieldI<DM_ELEMENT_I extends DmElementI> extends DmElementField_ReadOnlyI<DM_ELEMENT_I> {

    /**
     * Set the element referenced by the field. Set to null to clear the
     * reference.
     *
     * @param v
     */
    void setElement(DM_ELEMENT_I v);

    /**
     * Removes the reference stored in the field. After calling this method,
     * getElement() will return null.
     */
    void removeElement();

}
