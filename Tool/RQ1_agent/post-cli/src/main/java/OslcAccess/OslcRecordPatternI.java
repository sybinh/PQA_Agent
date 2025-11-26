/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 * Interface for a record pattern on the OSLC interface.
 *
 * @author gug2wi
 */
public interface OslcRecordPatternI {

    /**
     * Returns all fields contained by the record.
     *
     * @return
     */
    public Iterable<? extends OslcFieldI> getOslcFields();

    public OslcRecordTypeI getOslcRecordType();

}
