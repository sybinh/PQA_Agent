/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.Rq1.OslcRq1Client;
import RestClient.Exceptions.FieldNotFoundException;
import RestClient.Exceptions.ResponseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author gug2wi
 */
public class OslcResponseRecord_Oslc20 implements OslcResponseRecordI {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(OslcResponseRecord_Oslc20.class.getCanonicalName());
    final private EcvXmlContainerElement xmlResponse;

    public OslcResponseRecord_Oslc20(EcvXmlContainerElement oslc_cm_ChangeRequest) {
        assert (oslc_cm_ChangeRequest != null);
        this.xmlResponse = oslc_cm_ChangeRequest;
    }

    @Override
    public OslcRecordIdentifier getOslcRecordIdentifier() throws ResponseException {
        String oslcShortTitle;
        try {
            oslcShortTitle = getShortTitle();
            return (new OslcRecordIdentifier(getRdfAbout(), getRecordType(), oslcShortTitle));
        } catch (ResponseException e) {
            // ignore
        }
        return (new OslcRecordIdentifier(getRdfAbout(), getRecordType()));
    }

    @Override
    public String getRdfAbout() {
        return (xmlResponse.getAttribute("rdf:about"));
    }
    
    private OslcRecordTypeI getRecordType() throws ResponseException {
        String recordType;
        try {
            recordType = xmlResponse.getTextElement("dcterms:type").getText();
        } catch (EcvXmlElement.NotfoundException ex) {
            logger.warning(ex.getMessage());
            throw (new ResponseException("No text for dcterms:type which should contain the record type.", xmlResponse));
        }
        return (OslcRq1Client.getRecordType(recordType));
    }

    @Override
    public EcvXmlContainerElement getRecordContent() {
        return xmlResponse;
    }

    private String getShortTitle() throws ResponseException {
        String shortTitle;
        try {
            shortTitle = xmlResponse.getTextElement("oslc:shortTitle").getText();
        } catch (EcvXmlElement.NotfoundException ex) {
            logger.warning(ex.getMessage());
            throw new ResponseException("No text for oslc:shortTitle which should contain the short title.", xmlResponse);
        }
        return shortTitle;
    }

    @Override
    public String getFieldRdfAbout(String fieldName) throws ResponseException {
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);

        EcvXmlElement field;
        try {
            field = xmlResponse.getElement("cq:" + fieldName);
        } catch (EcvXmlElement.NotfoundException ex) {
            return (null);
        }
        if (field.hasAttribute("rdf:resource") == false) {
            return (null);
        }
        return (field.getAttribute("rdf:resource"));
    }

    @Override
    public String getFieldValue(String fieldName) throws ResponseException {
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);

        String fieldNameWithPrefix;
        if (fieldName.indexOf(':') == -1) {
            fieldNameWithPrefix = "cq:" + fieldName;
        } else {
            fieldNameWithPrefix = fieldName;
        }

        EcvXmlElement field;
        try {
            field = xmlResponse.getElement(fieldNameWithPrefix);
        } catch (EcvXmlElement.NotfoundException ex) {
            throw new FieldNotFoundException("No text for " + fieldNameWithPrefix + " which should contain a field value.", xmlResponse, ex);
        }
        if (field instanceof EcvXmlTextElement) {
            return (((EcvXmlTextElement) field).getText());
        } else if (field instanceof EcvXmlEmptyElement) {
            return ("");
        }
        throw new FieldNotFoundException("No text for " + fieldNameWithPrefix + " which should contain a field value.", xmlResponse);
    }

    @Override
    public String getFieldValue(String fieldName, String subFieldName) throws ResponseException {
        assert (fieldName != null);
        assert (!fieldName.isEmpty());
        assert (subFieldName != null);
        assert (!subFieldName.isEmpty());

        EcvXmlElement field;
        try {
            field = xmlResponse.getContainerElement("cq:" + fieldName).getContainerElement("oslc_cm:ChangeRequest").getElement("cq:" + subFieldName);
        } catch (EcvXmlElement.NotfoundException ex) {
            throw new FieldNotFoundException("No text for cq:" + fieldName
                    + " -> oslc_cm:ChangeRequest -> cq:" + subFieldName
                    + " which should contain a field value.", xmlResponse, ex);
        }
        if (field instanceof EcvXmlTextElement) {
            return (((EcvXmlTextElement) field).getText());
        } else if (field instanceof EcvXmlEmptyElement) {
            return ("");
        }
        throw new FieldNotFoundException("No text for cq:" + fieldName
                + " -> oslc_cm:ChangeRequest -> cq:" + subFieldName
                + " which should contain a field value.", xmlResponse);
    }

    public String getFieldValue(String fieldName, String subFieldName, String subSubFieldName) throws ResponseException {
        assert (fieldName != null);
        assert (!fieldName.isEmpty());
        assert (subFieldName != null);
        assert (!subFieldName.isEmpty());
        assert (subSubFieldName != null);
        assert (!subSubFieldName.isEmpty());

        EcvXmlElement field;
        try {
            field = xmlResponse.getContainerElement("cq:" + fieldName).getContainerElement("oslc_cm:ChangeRequest").getContainerElement("cq:" + subFieldName).getContainerElement("oslc_cm:ChangeRequest").getElement("cq:" + subSubFieldName);
        } catch (EcvXmlElement.NotfoundException ex) {
            throw new FieldNotFoundException("No text for cq:" + fieldName
                    + " -> oslc_cm:ChangeRequest -> cq:" + subFieldName
                    + " -> oslc_cm:ChangeRequest -> cq:" + subSubFieldName
                    + " which should contain a field value.", xmlResponse, ex);
        }
        if (field instanceof EcvXmlTextElement) {
            return (((EcvXmlTextElement) field).getText());
        } else if (field instanceof EcvXmlEmptyElement) {
            return ("");
        }
        throw new FieldNotFoundException("No text for cq:" + fieldName
                + " -> oslc_cm:ChangeRequest -> cq:" + subFieldName
                + " which should contain a field value.", xmlResponse);
    }

    @Override
    public String getFieldValueByPath(String fieldPath) throws ResponseException {
        assert (fieldPath != null);
        assert (fieldPath.isEmpty() == false);

        String path[] = fieldPath.split("\\.");
        switch (path.length) {
            case 1:
                return (getFieldValue(path[0]));
            case 2:
                return (getFieldValue(path[0], path[1]));
            case 3:
                return (getFieldValue(path[0], path[1], path[2]));
            default:
                throw (new Error("Field " + fieldPath + " not supported here."));
        }
    }

    @Override
    public List<OslcResponseRecordI> getSubRecords(String recordName) throws ResponseException {
        return getSubRecords(recordName, false);
    }

    @Override
    public List<OslcResponseRecordI> getSubRecords(String recordName, boolean faultTolerant) throws ResponseException {
        assert (recordName != null);
        assert (!recordName.isEmpty());
        List<OslcResponseRecordI> subRecords = new ArrayList<>();
        List<EcvXmlContainerElement> records = xmlResponse.getContainerElementList("cq:" + recordName);
        for (EcvXmlContainerElement xmlElement : records) {
            EcvXmlContainerElement record;
            try {
                record = xmlElement.getContainerElement("oslc_cm:ChangeRequest");
                subRecords.add(new OslcResponseRecord_Oslc20(record));
            } catch (EcvXmlElement.NotfoundException ex) {
                if (faultTolerant == false) {
                    logger.warning(ex.getMessage());
                    throw (new ResponseException("No record for cq:" + recordName
                            + " -> oslc_cm:ChangeRequest.", xmlResponse));
                }
            }
        }
        return (subRecords);
    }

    @Override
    public List<OslcRecordIdentifier> getReferenceList(String fieldName) throws ResponseException {
        assert (fieldName != null);
        assert (!fieldName.isEmpty());

        List<OslcRecordIdentifier> references = new ArrayList<>();
        for (OslcResponseRecordI record : getSubRecords(fieldName, true)) {
            references.add(record.getOslcRecordIdentifier());
        }
        return references;
    }
    
    public Map<String, String> getFieldsForQueryResult() {
        TreeMap<String, String> result = new TreeMap<>();
        addFieldsForQueryResult(result, "", xmlResponse);
        return (result);
    }

    private void addFieldsForQueryResult(Map<String, String> result, String path, EcvXmlContainerElement container) {
        assert (result != null);
        assert (path != null);
        assert (container != null);

        for (EcvXmlElement element : container.getElementList()) {

            String nameWithSuffix = element.getName();

            if (nameWithSuffix.startsWith("cq:") == true) {

                String name = nameWithSuffix.substring(3);
                String fullName = path.isEmpty() ? name : path + "." + name;

                if (element instanceof EcvXmlEmptyElement) {
                    result.put(fullName, "");
                } else if (element instanceof EcvXmlTextElement) {
                    result.put(fullName, ((EcvXmlTextElement) element).getText());
                } else {
                    throw (new Error("Unexpected type: " + element.getClass().toString()));
                }

            }
        }
    }

    @Override
    public String toString() {
        return xmlResponse.getUiString();
    }
}
