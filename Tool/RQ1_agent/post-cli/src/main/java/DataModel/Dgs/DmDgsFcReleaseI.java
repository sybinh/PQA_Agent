/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Dgs;

import java.util.Collection;

/**
 * Interface representing a FC release in the DGS.
 *
 * @author gug2wi
 */
public interface DmDgsFcReleaseI extends Comparable<DmDgsFcReleaseI> {

    /**
     * Returns the name of the FC.
     *
     * @return Name of the FC.
     */
    String getName();

    /**
     * Returns the version of the FC.
     *
     * @return Version of the FC.
     */
    String getVersion();

    /**
     * Returns the variant of the FC. The variant is the first number of the
     * version. In other words: The part of the version before the first dot.
     *
     * @return Variant of the FC.
     */
    String getVariant();

    String getTitle();

    /**
     * Support class to implement the Comparable interface in subclasses.
     */
    class FcInterface {

        static public int compare(DmDgsFcReleaseI me, DmDgsFcReleaseI other) {
            if (me.getName().equals(other.getName())) {
                return (me.getVersion().compareTo(other.getVersion()));
            } else {
                return (me.getName().compareTo(other.getName()));
            }
        }

        static public boolean equals(DmDgsFcReleaseI me, Object other) {
            if (other instanceof DmDgsFcReleaseI) {
                return (compare(me, (DmDgsFcReleaseI) other) == 0);
            } else {
                return (false);
            }

        }

        static public String getVariant(String version) {
            assert (version != null);
            assert (version.isEmpty() == false);

            String[] parts = version.split("\\.");
            return (parts[0]);
        }
    }

    /**
     * Checks if the FC equals to any of the FC in the collection.
     *
     * @param collection A collection of FCs to test against.
     * @return true, if one of the FC in the collection equals to this FC; false
     * otherwise
     */
    default boolean equalsFcInCollection(Collection<? extends DmDgsFcReleaseI> collection) {
        assert (collection != null);
        for (DmDgsFcReleaseI fc : collection) {
            if (equals(fc) == true) {
                return (true);
            }
        }
        return (false);
    }

}
