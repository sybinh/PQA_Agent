/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

/**
 *
 * @author gug2wi
 */
public class InternalRank implements Comparable<InternalRank> {

    static public class BuildException extends Exception {

        public BuildException(String message) {
            super(message);
        }

        public BuildException(String message, Throwable ex) {
            super(message, ex);
        }

    }

    final private long internalRank;

    public InternalRank(long internalRank) {
        this.internalRank = internalRank;
    }

    @Override
    public String toString() {
        return (Long.toString(internalRank));
    }

    @Override
    public int compareTo(InternalRank o) {
        return (Long.compare(internalRank, o.internalRank));
    }

    /**
     * Builds the internal rank for a RQ1 record based on the RQ1-ID, the
     * (external) rank and the internal rank.
     * <p>
     * Use this method to create the rank for a record read from the RQ1
     * database.
     *
     * @param rq1Id RQ1-Id of the record. Must not be empty or null.
     * @param rank Rank from the tag <RANK>, if available in the record.
     * @param internalRank Internal rank from the tag <INTERNAL_RANK>, if
     * available in the record.
     * @return The internal rank for the record.
     */
    static public InternalRank buildForRecord(String rq1Id, String rank, String internalRank) throws BuildException {
        assert (rq1Id != null);
        //assert (rq1Id.matches("RQONE[0-9]{8}")) : rq1Id;
        if ((internalRank != null) && (internalRank.isEmpty() == false)) {
            try {
                internalRank = internalRank.replace(" ", "");
                internalRank = internalRank.replace("_", "");
                internalRank = internalRank.replace("-", "");
                internalRank = internalRank.trim();
                return (new InternalRank(Long.valueOf(internalRank)));
            } catch (NumberFormatException ex) {

                throw (new BuildException("Invalid format for internal rank: >" + internalRank + "<", ex));
            }
        } else if ((rank != null) && (rank.isEmpty() == false)) {
            try {
                return (new InternalRank(Long.valueOf(rank) * 10000000000L));
            } catch (NumberFormatException ex) {
                throw (new BuildException("Invalid format for  rank: >" + rank + "<", ex));
            }
        } else {
            try {
                return (new InternalRank(Long.valueOf(rq1Id.substring(5, 13)) * 10000000000L));
            } catch (NumberFormatException ex) {
                throw (new BuildException("Invalid format for  RQ1-Id: >" + rq1Id + "<", ex));
            }
        }

    }

    /**
     * Calculates the internal rank that fits between two given ranks.
     *
     * @param before The internal rank that shall be directly before the new
     * internal rank.
     * @param after The internal rank that shall be directly after the new
     * internal rank.
     * @return
     */
    static public InternalRank buildBetween(InternalRank before, InternalRank after) throws BuildException {
        assert (before != null);
        assert (after != null);
        assert (before.compareTo(after) < 0) : "before=" + Double.toString(before.internalRank) + " after=" + Double.toString(after.internalRank);

        //
        // Calculate new value
        //
        Long betweenRank = (before.internalRank + after.internalRank) / 2;
        if ((before.internalRank == betweenRank) || (after.internalRank == betweenRank)) {
            throw (new BuildException("No space between " + before.internalRank + " and " + after.internalRank + "."));
        }

        return (new InternalRank(betweenRank));
    }

    static public InternalRank buildFirst(InternalRank currentFirst) throws BuildException {
        assert (currentFirst != null);
        return (buildBetween(new InternalRank(0), currentFirst));
    }

    static public InternalRank buildLast(InternalRank currentLast) throws BuildException {
        assert (currentLast != null);
        return (buildBetween(currentLast, new InternalRank(currentLast.internalRank + 50000000000L)));
    }

}
