/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

/**
 * Represents a RQ1 record that is identified by a RQ1-ID. E.g. Issues,
 * Releases, Workitems, ...
 *
 * @author GUG2WI
 */
public interface Rq1SubjectInterface extends Rq1NodeInterface {

    final static public String patternRq1Id = "RQONE[0-9]{8}";

    public String getRq1Id();

    /**
     * Checks whether or nor the given string has the format of a valid RQ1-ID.
     *
     * Note that the method does not check if an element with this is exists in
     * the RQ1 database. Only the format of the string is checked.
     *
     * @param rq1Id The string that shall be checked if the format fits for a
     * RQ1 ID.
     * @return true, if the string matches the format; false if not.
     */
    static boolean isRq1Id(String rq1Id) {
        return (rq1Id.matches(Rq1SubjectInterface.patternRq1Id));
    }

}
