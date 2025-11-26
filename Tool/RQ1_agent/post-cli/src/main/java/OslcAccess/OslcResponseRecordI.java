/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import RestClient.Exceptions.ResponseException;
import java.util.List;
import util.EcvXmlContainerElement;

/**
 * Holds the response from a OSCL command.
 *
 * @author gug2wi
 */
public interface OslcResponseRecordI {

    OslcRecordIdentifier getOslcRecordIdentifier() throws ResponseException;

    EcvXmlContainerElement getRecordContent();

    String getRdfAbout();

    String getFieldRdfAbout(String fieldName) throws ResponseException;
    
    String getFieldValue(String fieldName) throws ResponseException;

    String getFieldValue(String fieldName, String subFieldName) throws ResponseException;

    /**
     * Returns the value for the field given by fieldPath. The parts of the path
     * are separated by dots. Paths without dots are allowed.
     *
     * @param fieldPath Path to the field. Parts are separated by dots.
     * @return
     * @throws ResponseException
     */
    String getFieldValueByPath(String fieldPath) throws ResponseException;

    List<OslcResponseRecordI> getSubRecords(String recordName) throws ResponseException;

    List<OslcResponseRecordI> getSubRecords(String recordName, boolean faultTolerant) throws ResponseException;

    List<OslcRecordIdentifier> getReferenceList(String fieldName) throws ResponseException;

}
