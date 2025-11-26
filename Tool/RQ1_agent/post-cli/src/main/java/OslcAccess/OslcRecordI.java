/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 * Interface for a record on the OSCL interface.
 *
 * @author gug2wi
 */
public interface OslcRecordI extends OslcRecordPatternI {

    /**
     * Sets the reference that identifies the record on the OSLC interface.
     *
     * @param olscIdentifier Unique identifier for the record on the OSLC
     * interface.
     */
    public void setOslcRecordIdentifier(OslcRecordIdentifier olscIdentifier);

    /**
     * Returns the references that identifies the record on the OSLC interface.
     *
     * @return Unique identifier for the record on the OSLC interface or null,
     * if the record does not (yet) exist in the database.
     */
    public OslcRecordIdentifier getOslcRecordIdentifier();

    /**
     * Called before the setting of OSLC field values starts.
     */
    public void startOslcValueSetting();

    public Iterable<? extends OslcWriteableFieldI> getWriteableOslcFields();

    /**
     * Called after the setting of OSLC field values was finished.
     *
     * @param fieldContentChanged true, if at least one field reported that the
     * field values was changed due to the new OSLC value.
     */
    public void endOslcValueSetting(boolean fieldContentChanged);
}
