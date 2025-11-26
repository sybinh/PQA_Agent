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
public class OslcResponseRecord_Oslc10 implements OslcResponseRecordI {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(OslcResponseRecord_Oslc10.class.getCanonicalName());
    final private EcvXmlContainerElement xmlResponse;
    private EcvXmlElement IpeXmlElement;

    public OslcResponseRecord_Oslc10(EcvXmlContainerElement issue) {
        assert (issue != null);
        this.xmlResponse = issue;
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
        //
        // Implementation fits for fields of a record
        //
        String[] parts = getRdfAbout().split("/");
        if (parts[parts.length - 4].equals("field") == true) {
            //
            // Implementation fits for fields of a record
            //
            return (OslcRq1Client.getRecordType(parts[parts.length - 2]));
        } else {
            //
            // Fits for query result
            //
            return (OslcRq1Client.getRecordType(getFieldValue("dc:type")));
        }

    }

    @Override
    public EcvXmlContainerElement getRecordContent() {
        return xmlResponse;
    }

    private String getShortTitle() throws ResponseException {
        String shortTitle;
        try {
            shortTitle = xmlResponse.getTextElement("dc:title").getText();
        } catch (EcvXmlElement.NotfoundException ex) {
            logger.warning(ex.getMessage());
            throw new ResponseException("No text for dc:title which should contain the short title.", xmlResponse);
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

        EcvXmlElement field;
        try {
            field = xmlResponse.getElement(fieldName);
        } catch (EcvXmlElement.NotfoundException ex) {
            throw new FieldNotFoundException("No text for " + fieldName + " which should contain a field value.", xmlResponse, ex);
        }
        if (field instanceof EcvXmlTextElement) {
            return (((EcvXmlTextElement) field).getText());
        } else if (field instanceof EcvXmlEmptyElement) {
            return ("");
        }
        throw new FieldNotFoundException("No text for " + fieldName + " which should contain a field value.", xmlResponse);
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

        //
        // Get element with name of recordName which contains the sub records.
        //
        EcvXmlElement recordListContainer;
        try {
            recordListContainer = xmlResponse.getElement(recordName);
        } catch (EcvXmlElement.NotfoundException ex) {
            logger.warning(ex.getMessage());
            throw (new ResponseException("No record for " + recordName
                    + " -> Issue.", xmlResponse));
        }

        //
        // If the list is not empty ... 
        //
        if (recordListContainer instanceof EcvXmlContainerElement) {
            List<EcvXmlContainerElement> records;
            try {
                records = xmlResponse.getContainerElement(recordName).getContainerElementList();
            } catch (EcvXmlElement.NotfoundException ex) {
                logger.warning(ex.getMessage());
                throw (new ResponseException("No record for " + recordName
                        + " -> oslc_cm:ChangeRequest.", xmlResponse));
            }
            for (EcvXmlContainerElement xmlElement : records) {
                subRecords.add(new OslcResponseRecord_Oslc10(xmlElement));
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

    static public class QueryResult {

        final private String name;
        final private Map<String, String> fields;
        final private List<QueryResult> subRecords;

        public QueryResult(String name, Map<String, String> fields, List<QueryResult> subRecords) {
            this.name = name;
            this.fields = fields;
            this.subRecords = subRecords;
        }

        public String getName() {
            return name;
        }

        public Map<String, String> getFields() {
            return fields;
        }

        public List<QueryResult> getSubRecords() {
            return subRecords;
        }

    }

    /**
     * Returns all fields of a query result.
     *
     * @return A map containing all fields with name and value.
     */
    public QueryResult extractQueryResult() {
        return (extractQueryResult_Internal("", xmlResponse));
    }

    /**
     * Adds all fields from the query result recursive to the map.
     *
     * @param result Map to be filled with all fields.
     * @param path Path for which the method is called.
     * @param container Container for this path.
     */
    private QueryResult extractQueryResult_Internal(String path, EcvXmlContainerElement container) {
        assert (path != null);
        assert (container != null);

        Map<String, String> fields = new TreeMap<>();
        List<QueryResult> subRecords = new ArrayList<>();

        for (EcvXmlElement element : container.getElementList()) {
            String name = element.getName();

            //
            // Process only names without ':'. This way, we skip all fields with prefix 'dc:'.
            //
            if (name.contains(":") == false) {

                String fullName = path.isEmpty() ? name : path + "." + name;

                if (element instanceof EcvXmlEmptyElement) {

                    //
                    // Records without subfields have the attribute olsc_cm:label set.
                    //
                    String elementId = element.getAttribute("oslc_cm:label");
                    if (elementId != null) {
                        // It's a record. Take the id as value.
                        fields.put(fullName, elementId);
                    } else {
                        // It's a normal field with empty value.
                        fields.put(fullName, "");
                    }

                } else if (element instanceof EcvXmlTextElement) {
                    // Simple field: Add it to the field map.
                    fields.put(fullName, ((EcvXmlTextElement) element).getText());

                } else if (element instanceof EcvXmlContainerElement) {
                    // A container: Add fields from the container via recursive method call.
                    QueryResult subRecord = extractQueryResult_Internal(fullName, (EcvXmlContainerElement) element);
                    subRecords.add(subRecord);
                } else {
                    // Ooops. Didn't expect anything else ...
                    throw (new Error("Unexpected type: " + element.getClass().toString()));
                }
            }
        }

        return (new QueryResult(container.getName(), fields, subRecords));
    }

    @Override
    public String toString() {
        return xmlResponse.getUiString();
    }
}
