/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import RestClient.Exceptions.ResponseException;

/**
 * Supports the creation of objects for records received via the OSLC interface.
 * <p>
 * The created records of type {@link OslcAccess.OslcRecordI} are used to store
 * the values of records read from the OSCL interface.
 *
 * @author gug2wi
 * @param <T_RECORD> Type of records supported by the factory.
 */
public interface OslcRecordFactoryI<T_RECORD extends OslcRecordI> {

    /**
     * Determine the type for a record received over the OSLC interface.
     *
     * @param recordTypeFromOslcInterface
     * @return
     */
    public OslcRecordTypeI getRecordType(String recordTypeFromOslcInterface);

    /**
     * Return a record that can be used as pattern for the given record type.
     * The pattern will be used to determine the fields requested from the OSLC
     * server on a get operation. Calls of this method for the same record type
     * may return the same object.
     *
     * @param recordType The record type for which the pattern is needed.
     * @return The object that shall be used as pattern.
     */
    public OslcRecordPatternI getPattern(OslcRecordTypeI recordType);

    /**
     * Return a record that fits to the fields provided by the given response.
     * <p>
     * The object shall only be created. The setting of the field values is done
     * later in the caller method.
     * <p>
     * The method might return a object that was already previously created for
     * the same OSLC record.
     *
     * @param response Response from the OSLC server.
     * @return The record that can be used to store the received field values.
     * @throws RestClient.Exceptions.ResponseException
     */
    public T_RECORD getRecord(OslcResponseRecordI response) throws ResponseException;

    /**
     * Adds a record after it was loaded from or created in the database. This
     * method might by called repeatedly for the same record, each time when the
     * record was reloaded from the database.
     *
     * @param newRecord
     */
    public void addRecord(T_RECORD newRecord);
}
