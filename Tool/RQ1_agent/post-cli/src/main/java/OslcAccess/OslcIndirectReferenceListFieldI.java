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
 * property of that record on the OSLC interface and holds indirect references
 * to other records on the OSLC server.
 * <p>
 * The structure on the OSCL interface is the following:
 * <p>
 * Record -&gt; Referenced Record -&gt; Indirect Referenced Record
 * <p>
 * This means: The record contains a property which points to the referenced
 * record. This referenced record contains a property which points to the
 * indirect referenced Record.
 * <p>
 * The field allows a list of referenced records, each referenced record points
 * exactly to one indirect referenced Record.
 *
 * @author gug2wi
 * @param <T_RECORD>
 * @param <T_INDIRECT_RECORD>
 */
public interface OslcIndirectReferenceListFieldI<T_RECORD extends OslcRecordI, T_INDIRECT_RECORD extends OslcRecordI> extends OslcFieldI {

    static public class Entry<T_RECORD extends OslcRecordI, T_INDIRECT_RECORD extends OslcRecordI> {

        final private OslcRecordReference<T_RECORD> directReference;
        final private OslcRecordReference<T_RECORD> indirectReference;

        public Entry(OslcRecordReference<T_RECORD> directReference, OslcRecordReference<T_RECORD> indirectReference) {
            assert (directReference != null);
            assert (indirectReference != null);

            this.directReference = directReference;
            this.indirectReference = indirectReference;
        }

        public OslcRecordReference<T_RECORD> getDirectReference() {
            return directReference;
        }

        public OslcRecordReference<T_RECORD> getIndirectReference() {
            return indirectReference;
        }

    }

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
    public boolean setOslcValue(List<Entry<T_RECORD, T_INDIRECT_RECORD>> recordReferenceList);

    /**
     * Copies the field value from the provided field.
     *
     * @param field
     * @return true, if the new value changed the last field value.
     */
    public boolean copyOslcValue(OslcIndirectReferenceListFieldI<T_RECORD, T_INDIRECT_RECORD> field);

    /**
     * Get the name of the OSLC property in the referenced record that refers to
     * the indirect referenced Record.
     *
     * @return
     */
    public String getIndirectOslcPropertyName();

    /**
     * Returns the record type of the indirect referenced records.
     *
     * @return
     */
    public OslcRecordTypeI getIndirectReferencedRecordType();

}
