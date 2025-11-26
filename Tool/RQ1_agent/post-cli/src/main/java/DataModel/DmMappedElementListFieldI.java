/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Defines the interface of a field that contains a list of DmMappedElement.
 *
 * @param <T_MAP>
 * @param <T_TARGET>
 * @see DmValueField
 * @author gug2wi
 */
public interface DmMappedElementListFieldI<T_MAP extends DmElementI, T_TARGET extends DmElementI> extends DmMappedElementListField_ReadOnlyI<T_MAP, T_TARGET> {

    void addElement(DmMappedElement<T_MAP, T_TARGET> e);

    @Override
    default boolean isReadOnly() {
        return (false);
    }

}
