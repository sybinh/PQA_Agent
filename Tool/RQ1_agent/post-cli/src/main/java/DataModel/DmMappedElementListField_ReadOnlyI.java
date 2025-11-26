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
 * Defines the interface of a field that contains a list of DmMappedElement.
 *
 * @param <T_MAP>
 * @param <T_TARGET>
 * @author gug2wi
 */
public interface DmMappedElementListField_ReadOnlyI<T_MAP extends DmElementI, T_TARGET extends DmElementI> extends DmFieldI {

    /**
     * Checks, if the list is already available in the cache.
     *
     * @return
     * <ul>
     * <li>true ... List available in cache.
     * <li> false ... List not yet available in cache.
     * </ul>
     *
     */
    boolean isLoaded();

    List<DmMappedElement<T_MAP, T_TARGET>> getElementList();

    //
    // Trigger the update of the references from the database.
    //
    void reload();

    @Override
    default boolean isReadOnly() {
        return (true);
    }
}
