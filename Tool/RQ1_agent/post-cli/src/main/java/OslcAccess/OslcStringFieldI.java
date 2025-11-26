/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 * Interface for a field in {@link OslcAccess.OslcRecordI} that maps to a
 * property of that record on the OSLC interface and holds an string value.
 *
 * @author gug2wi
 */
public interface OslcStringFieldI extends OslcForwardableFieldI {

    /**
     * Sets the value read from the OSLC interface.
     *
     * @param fieldValue Value read from OSLC database.
     * @return true, if the new value changed the last field value.
     */
    public boolean setOslcValue(String fieldValue);

    /**
     * Get the current value for the OSLC interface.
     *
     * @return The value that shall be sent over the OSLC interface.
     */

    /**
     * Copies the field value from the provided field.
     *
     * @param field
     * @return true, if the new value changed the last field value.
     */
    public boolean copyOslcValue(OslcStringFieldI field);

    public boolean equalDbValues(String value1, String value2);
    
    /**
     * Returns the last value from the OSLC database.
     * 
     * @return 
     */
    public String provideLastValueFromDbAsStringForDb();
}
