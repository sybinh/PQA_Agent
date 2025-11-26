/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import Rq1Cache.Types.Rq1Reference;

/**
 * Interface for a field in {@link OslcAccess.OslcRecordI} that maps to a
 * property of that record on the OSLC interface and holds the reference to one
 * other records on the OSLC server.
 *
 * @author gug2wi
 * @param <T_RECORD> Type of the record referenced by the field.
 * @see OslcDirectReferenceListFieldI
 */
public interface OslcDirectReferenceFieldI<T_RECORD extends OslcRecordI> extends OslcWriteableFieldI {

    /**
     * Returns the record type of the referenced records.
     *
     * @return
     */
    public OslcRecordTypeI getReferencedRecordType();

    /**
     * Sets the value read from the OSLC database.
     *
     * @param recordReference Value read from OSLC database.
     * @return true, if the new value changed the last field value.
     */
    public boolean setOslcValue(OslcRecordReference<T_RECORD> recordReference);

    /**
     * Copies the field value from the provided field.
     *
     * @param field
     * @return true, if the new value changed the last field value.
     */
    public boolean copyOslcValue(OslcDirectReferenceFieldI<T_RECORD> field);
    
    /**
     * Returns the last value from the OSLC database.
     * 
     * @return 
     */
    public Rq1Reference provideLastValueFromDbAsReferenceForDb();
    
    /**
     * Override here the last value from OSLC database with the current value of
     * the data store.
     * 
     * @return 
     */
    public Rq1Reference provideValueAsReferenceForDb();
}
