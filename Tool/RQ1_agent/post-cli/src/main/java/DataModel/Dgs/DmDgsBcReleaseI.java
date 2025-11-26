/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Dgs;

/**
 * Interface representing a BC release in the DGS.
 *
 * @author gug2wi
 */
public interface DmDgsBcReleaseI extends Comparable<DmDgsBcReleaseI> {

    /**
     * Returns the type of the BC (BC, BCC, CC).
     *
     * @return Name of the BC.
     */
    String getType();

    /**
     * Returns the name of the BC.
     *
     * @return Name of the BC.
     */
    String getName();

    /**
     * Returns the version of the BC.
     *
     * @return Version of the BC.
     */
    String getVersion();

    /**
     * Returns the title of the BC in the format "BC : Name / Version"
     *
     * @return Version of the BC.
     */
    String getTitle();

    /**
     * Support class to implement the Comparable interface in subclasses.
     */
    static class BcReleaseComparator {

        static public boolean equals(DmDgsBcReleaseI me, Object other) {
            if (other instanceof DmDgsBcReleaseI) {
                if (me.getName().equals(((DmDgsBcReleaseI)other).getName())) {
                    return (me.getVersion().equals(((DmDgsBcReleaseI)other).getVersion()));
                } else {
                    return (false);
                }
            } else {
                return (false);
            }
        }

        static public int compare(DmDgsBcReleaseI me, DmDgsBcReleaseI other) {
            if (me.getName().equals(other.getName())) {
                return (me.getVersion().compareTo(other.getVersion()));
            } else {
                return (me.getName().compareTo(other.getName()));
            }
        }
    }

}
