/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 * Interface for a field in {@link OslcRecordI} that maps to a property of that
 * record on the OSLC interface.
 *
 * @author gug2wi
 */
public interface OslcFieldI {

    /**
     * Get the name of the property on the OSLC interface.
     *
     * @return Name of the property.
     */
    public String getOslcPropertyName();

    /**
     * Indicates whether or not the field is optional on the OSLC interface.
     *
     * @return true, if the field is optional.<br>
     * false, if the field is mandatory.
     */
    public boolean isOptional();

   
}
