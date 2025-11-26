/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.List;

/**
 * Interface for a field in {@link OslcAccess.OslcRecordI} that maps to a
 * property of that record on the OSLC interface and holds references to other
 * records on the OSLC server.
 *
 * @param <T_RECORD> Type of record hold by the field.
 * @see OslcDirectReferenceFieldI
 */
public interface OslcDirectReferenceListFieldI<T_RECORD extends OslcRecordI> extends OslcFieldI {

    /**
     * Returns the record type of the referenced records.
     *
     * @return The type of record in the list.
     */
    public OslcRecordTypeI getReferencedRecordType();

    /**
     * Returns the version of the OSLC protocol that shall be used to read the
     * field list.
     *
     * @return OslcProtocolVersion that shall be used.
     */
    public OslcProtocolVersion getOslcProtocolVersion();

    /**
     * Sets the value read from the OSLC database.
     *
     * @param recordReferenceList Value read from OSLC database.
     * @return true, if the new value changed the last field value.
     */
    public boolean setOslcValue(List<OslcRecordReference<T_RECORD>> recordReferenceList);

    /**
     * Copies the field value from the provided field.
     *
     * @param field
     * @return true, if the new value changed the last field value.
     */
    public boolean copyOslcValue(OslcDirectReferenceListFieldI<T_RECORD> field);

}
