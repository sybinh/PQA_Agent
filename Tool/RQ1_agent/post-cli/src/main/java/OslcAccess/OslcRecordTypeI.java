/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 * Type of a record on the OSLC interface.
 *
 * @author gug2wi
 */
public interface OslcRecordTypeI {

    /**
     * Returns the string for the User Interface.
     *
     * @return
     */
    String getText();

    /**
     * Returns the string which is used on the OSLC interface to identify the
     * record type.
     *
     * @return
     */
    String getOslcType();

}
