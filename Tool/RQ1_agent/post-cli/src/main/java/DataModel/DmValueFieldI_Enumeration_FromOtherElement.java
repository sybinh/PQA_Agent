/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import java.util.List;

/**
 * Defines the interface of a field that contains one DmElement.
 *
 * @author gug2wi
 * @param <DM_ELEMENT_I> Type of the element stored in the field.
 */
public interface DmValueFieldI_Enumeration_FromOtherElement<DM_ELEMENT_I extends DmElementI> extends DmValueFieldI<DM_ELEMENT_I> {
    /**
     * @return List of allowed Elements for the Field.
     */
    List<DM_ELEMENT_I> getValidInputValues();
    
    /**
     * Returns the element referenced by the field or null if no element is
     * referenced.
     *
     * @return The referenced element or null.
     */
    DM_ELEMENT_I getValue();
    
    void setValue(DM_ELEMENT_I v);
    
    String getValueUrl();
    
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
        return (getValue() != null);
    }

}