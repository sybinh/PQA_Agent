/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataModel.DmElementI;

/**
 * Container for two elements.
 *
 * @author GUG2WI
 * @param <T_MAP>
 * @param <T_TARGET>
 */
public class DmMappedElement<T_MAP extends DmElementI, T_TARGET extends DmElementI> {

    final private T_TARGET target;
    final private T_MAP map;

    /**
     * Create the container for map and target.
     *
     * @param map The map element. Must not be null.
     * @param target The target element. Must not be null.
     */
    public DmMappedElement(T_MAP map, T_TARGET target) {
        assert (map != null);
        assert (target != null);

        this.map = map;
        this.target = target;
    }

    /**
     * Get the target element.
     *
     * @return The target element. Cannot be null.
     */
    final public T_TARGET getTarget() {
        return (target);
    }

    /**
     * Get the map element.
     *
     * @return The map element. Cannot be null.
     */
    final public T_MAP getMap() {
        return (map);
    }

    @Override
    final public boolean equals(Object o) {
        if (o instanceof DmMappedElement) {
            DmMappedElement m = (DmMappedElement) o;
            return ((m.getMap() == map) && (m.getTarget() == target));
        } else {
            return (false);
        }
    }

    /**
     * Returns a string that can be used for debugging.
     *
     * @return A string useful for debugging.
     */
    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + "(" + map.toString() + "," + target.toString() + ")");
    }

}
