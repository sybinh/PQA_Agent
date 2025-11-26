/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import OslcAccess.OslcResponse;
import RestClient.Exceptions.ResponseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvDate;
import util.EcvXmlCombinedElement;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;
import util.SafeArrayList;
import ToolUsageLogger.ToolUsageLogger;

/**
 *
 * @author GUG2WI
 */
public class OslcAlmResponse {

    static private final Logger LOGGER = Logger.getLogger(OslcAlmResponse.class.getCanonicalName());

    public static String BOOLEAN_TRUE = "true";
    public static String BOOLEAN_FALSE = "false";

    //--------------------------------------------------------------------------
    //
    // Types and classes
    //
    //--------------------------------------------------------------------------
    public static enum ResponseType {
        PROJECT_AREA,
        TEAM_AREA,
        ITERATION,
        WORKITEM,
        REQUIREMENTS,
        CHANGE_SET,
        USER;
    }

    public static enum FieldType {
        STRING,
        BOOLEAN,
        INTEGER,
        DATE_TIME,
        RESOURCE;
    }

    public static class Field {

        final private FieldType type;
        final private String name;
        final private String value;

        public Field(FieldType type, String name, String value) {
            assert (type != null);
            assert (name != null);
            assert (name.isEmpty() == false);
            assert (value != null);

            this.type = type;
            this.name = name;
            this.value = value;
        }

        public FieldType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

    }

    //--------------------------------------------------------------------------
    //
    // Members and object creation
    //
    //--------------------------------------------------------------------------
    private final ResponseType responseType;
    private final SafeArrayList<Field> fieldList;
    private String etag;

    protected OslcAlmResponse(ResponseType responseType) {
        assert (responseType != null);

        this.responseType = responseType;
        this.fieldList = new SafeArrayList<>();
    }

    private void addField(FieldType type, String name, String value) {
        fieldList.add(new Field(type, name, value));
    }

    protected void addFieldForXmlElement(EcvXmlContainerElement xmlResponse, EcvXmlElement element) throws ResponseException {
        assert (xmlResponse != null);
        assert (element != null);

        if (element instanceof EcvXmlEmptyElement) {
            addFieldForEmptyElement(xmlResponse, (EcvXmlEmptyElement) element);
        } else if (element instanceof EcvXmlTextElement) {
            addFieldForTextElement(xmlResponse, (EcvXmlTextElement) element);
        } else if (element instanceof EcvXmlCombinedElement) {
            addFieldForCombinedElement(xmlResponse, (EcvXmlCombinedElement) element);
        } else if (element instanceof EcvXmlContainerElement) {
            addFieldForContainerElement(xmlResponse, (EcvXmlContainerElement) element);
        } else {
            LOGGER.log(Level.SEVERE, "Unexpected content:\n{0}", element.toString());
            throw (new ResponseException("Unexpected content in tag: " + element.getName(), xmlResponse));
        }
    }

    private void addFieldForEmptyElement(EcvXmlContainerElement xmlResponse, EcvXmlEmptyElement element) throws ResponseException {
        assert (element != null);

        //
        // Handle elements with given data type
        //
        String dataType = element.getAttribute("rdf:datatype");
        if (dataType != null) {
            switch (dataType) {
                case "http://www.w3.org/2001/XMLSchema#string":
                    addField(FieldType.STRING, element.getName(), "");
                    return;

                default:
                    LOGGER.log(Level.SEVERE, "Empty element with unknown/unexpected data type:\n{0}", element.toString());
                    throw (new ResponseException("Empty element with unknown/unexpected data type in tag: " + element.getName(), xmlResponse));
            }
        }

        //
        // Handle element with parseType
        //
        String parseType = element.getAttribute("rdf:parseType");
        if (parseType != null) {
            switch (parseType) {
                case "Literal":
                    addField(FieldType.STRING, element.getName(), "");
                    return;
                default:
                    LOGGER.log(Level.SEVERE, "Text element with unknown parse type:\n{0}", element.toString());
                    throw (new ResponseException("Text element with unknown parse type in tag: " + element.getName(), xmlResponse));
            }
        }

        String resource = element.getAttribute("rdf:resource");
        if (resource == null) {
            LOGGER.log(Level.SEVERE, "Empty element without resource:\n{0}", element.toString());
            throw (new ResponseException("Empty element without resource in tag: " + element.getName(), xmlResponse));
        }

        addField(FieldType.RESOURCE, element.getName(), resource);
    }

    private void addFieldForTextElement(EcvXmlContainerElement xmlResponse, EcvXmlTextElement element) throws ResponseException {
        assert (element != null);

        //
        // Handle elements with data type
        //
        String dataType = element.getAttribute("rdf:datatype");
        if (dataType != null) {
            switch (dataType) {
                case "http://www.w3.org/2001/XMLSchema#dateTime":
                    addField(FieldType.DATE_TIME, element.getName(), element.getText());
                    return;
                case "http://www.w3.org/2001/XMLSchema#string":
                case "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral":
                    addField(FieldType.STRING, element.getName(), element.getText());
                    return;
                case "http://www.w3.org/2001/XMLSchema#integer":
                case "http://www.w3.org/2001/XMLSchema#decimal":
                case "http://www.w3.org/2001/XMLSchema#long":
                    addField(FieldType.INTEGER, element.getName(), element.getText());
                    return;
                case "http://www.w3.org/2001/XMLSchema#boolean":
                    addField(FieldType.BOOLEAN, element.getName(), element.getText());
                    return;
                default:
                    LOGGER.log(Level.SEVERE, "Text element with unknown data type:\n{0}", element.toString());
                    throw (new ResponseException("Text element with unknown data type in tag: " + element.getName(), xmlResponse));
            }
        }

        //
        // Handle element with parseType
        //
        String parseType = element.getAttribute("rdf:parseType");
        if (parseType != null) {
            switch (parseType) {
                case "Literal":
                    addField(FieldType.STRING, element.getName(), element.getText());
                    return;
                default:
                    LOGGER.log(Level.SEVERE, "Text element with unknown parse type:\n{0}", element.toString());
                    throw (new ResponseException("Text element with unknown parse type in tag: " + element.getName(), xmlResponse));
            }
        }

        //
        // Handle element without any type definition
        //
        addField(FieldType.STRING, element.getName(), element.getText());
    }

    private void addFieldForCombinedElement(EcvXmlContainerElement xmlResponse, EcvXmlCombinedElement element) throws ResponseException {
        assert (element != null);

        //
        // Handle element with parseType
        //
        String parseType = element.getAttribute("rdf:parseType");
        if (parseType != null) {
            switch (parseType) {
                case "Literal":
                    String text = element.getXmlString_WithoutContainer(EcvXmlElement.EncodeFormat.LONG_EMPTY_VALUE);
                    addField(FieldType.STRING, element.getName(), text);
                    return;
                default:
                    LOGGER.log(Level.SEVERE, "Text element with unknown parse type:\n{0}", element.toString());
                    throw (new ResponseException("Text element with unknown parse type in tag: " + element.getName(), xmlResponse));
            }
        }

        LOGGER.log(Level.SEVERE, "Combined element without type:\n{0}", element.toString());
        throw (new ResponseException("Combined element without data in tag: " + element.getName(), xmlResponse));
    }

    private void addFieldForContainerElement(EcvXmlContainerElement xmlResponse, EcvXmlContainerElement element) throws ResponseException {
        assert (element != null);

        //
        // Handle element with parseType
        //
        String parseType = element.getAttribute("rdf:parseType");
        if (parseType != null) {
            switch (parseType) {
                case "Literal":
                    String text = element.getXmlString_WithoutContainer(EcvXmlElement.EncodeFormat.LONG_EMPTY_VALUE);
                    addField(FieldType.STRING, element.getName(), text);
                    return;
                default:
                    LOGGER.log(Level.SEVERE, "Text element with unknown parse type:\n{0}", element.toString());
                    throw (new ResponseException("Text element with unknown parse type in tag: " + element.getName(), xmlResponse));
            }
        }

        LOGGER.log(Level.SEVERE, "Container element without type:\n{0}", element.toString());
        throw (new ResponseException("Container element without data in tag: " + element.getName(), xmlResponse));
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public List<Field> getFields() {
        return (fieldList.getImmutableList());
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        for (Field field : fieldList) {
            b.append(field.type.name()).append(": ").append(field.name).append(" = ").append(field.value).append("\n");
        }

        return (b.toString());
    }

    //--------------------------------------------------------------------------
    //
    // Static Builder Method
    //
    //--------------------------------------------------------------------------
    public static OslcAlmResponse build(OslcResponse response) throws ResponseException {
        assert (response != null);

        //
        // Check that only one element was returned.
        //
        List<EcvXmlContainerElement> reponseBodyList = response.getResponseBodyList();
        if (reponseBodyList.size() != 1) {
            LOGGER.log(Level.SEVERE, "reponseBodyList.size() = {0}", reponseBodyList.size());
            throw (new ResponseException("Unexpected number of bodies in response: " + reponseBodyList.size()));
        }

        //
        // Go into rdf:RDF
        //
        EcvXmlContainerElement xmlResponse = reponseBodyList.get(0);
        if ("rdf:RDF".equals(xmlResponse.getName()) == false) {
            LOGGER.log(Level.SEVERE, "Unexpected name of body in response: {0}", xmlResponse.getName());
            throw (new ResponseException("Unexpected name of body in response.", xmlResponse));
        }

        //
        // Select element that contains the data.
        //
        List<EcvXmlContainerElement> projectArea = xmlResponse.getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "process/project-areas");
        List<EcvXmlContainerElement> category = xmlResponse.getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "com.ibm.team.workitem.Category");
        List<EcvXmlContainerElement> teamArea = xmlResponse.getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "/team");
        List<EcvXmlContainerElement> iteration = xmlResponse.getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "oslc/iterations");
        List<EcvXmlContainerElement> workitem = xmlResponse.getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "com.ibm.team.workitem.WorkItem");
        List<EcvXmlContainerElement> requirements = xmlResponse.getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "/rm/resources/");
        List<EcvXmlContainerElement> changeSets_var1 = xmlResponse.getContainerElementList("oslc_config:Baseline");
        List<EcvXmlContainerElement> changeSets_var2 = xmlResponse.getContainerElementList("oslc_config:Stream");
        List<EcvXmlContainerElement> changeSets_var3 = xmlResponse.getContainerElementList("oslc_config:ChangeSet");
        List<EcvXmlContainerElement> user = xmlResponse.getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "jts/users");
        
        int numberOfElementsInResponse = projectArea.size() + category.size() + teamArea.size() + iteration.size() + workitem.size() + requirements.size() + changeSets_var1.size() + changeSets_var2.size() + changeSets_var3.size() + user.size();
        
        if (numberOfElementsInResponse != 1) {
            LOGGER.log(Level.SEVERE, "Unexpected number of elements in response: {0},{1}", new Object[]{xmlResponse.getName(), numberOfElementsInResponse});
            throw (new ResponseException("Unexpected number of elements in response.", xmlResponse));
        }

        OslcAlmResponse almResponse;
        EcvXmlContainerElement elementContainer;
        if (projectArea.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.PROJECT_AREA);
            elementContainer = projectArea.get(0);
        } else if (category.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.TEAM_AREA);
            elementContainer = category.get(0);
        } else if (teamArea.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.TEAM_AREA);
            elementContainer = teamArea.get(0);
        } else if (iteration.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.ITERATION);
            elementContainer = iteration.get(0);
        } else if (workitem.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.WORKITEM);

            //set Etag for updating
            almResponse.setEtag(response);
            elementContainer = workitem.get(0);
        } else if (changeSets_var1.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.CHANGE_SET);
            elementContainer = changeSets_var1.get(0);
        } else if (changeSets_var2.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.CHANGE_SET);
            elementContainer = changeSets_var2.get(0);
        } else if (changeSets_var3.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.CHANGE_SET);
            elementContainer = changeSets_var3.get(0);
        } else if (user.size() == 1) {
            almResponse = new OslcAlmResponse(ResponseType.USER);
            elementContainer = user.get(0);
        } else {
            almResponse = new OslcAlmResponse(ResponseType.REQUIREMENTS);
            elementContainer = requirements.get(0);
        }

        //
        // Loop for all fields
        //
        for (EcvXmlElement element : elementContainer.getElementList()) {
            almResponse.addFieldForXmlElement(xmlResponse, element);
        }
        LOGGER.finer("\n----- Result of build ----\n" + almResponse.toString() + "----------");

        return (almResponse);
    }

    //--------------------------------------------------------------------------
    //
    // Methods for type conversion
    //
    //--------------------------------------------------------------------------
    public static boolean decodeBoolean(String booleanInAlmFormat) {
        assert (booleanInAlmFormat != null);

        switch (booleanInAlmFormat) {
            case "true":
                return (true);
            case "false":
                return (false);
            default:
                LOGGER.severe("Unknown value for boolean: '" + booleanInAlmFormat + "'. Converted to false");
                return (false);
        }
    }

    public static EcvDate decodeDate(String dateInAlmFormat) {
        assert (dateInAlmFormat != null);

        try {
            return (EcvDate.parseAlmValue(dateInAlmFormat));
        } catch (EcvDate.DateParseException ex) {
            LOGGER.log(Level.SEVERE, "Failed to parse date string: '" + dateInAlmFormat + "'.", ex);
            ToolUsageLogger.logError(OslcAlmResponse.class.getCanonicalName(), ex);
            return (EcvDate.getEmpty());
        }
    }

    public static String getProjectAreaUrl(EcvXmlContainerElement container) throws EcvXmlElement.NotfoundException {
        return container.getContainerElement("oslc:ServiceProvider").getElement("oslc:details").getAttribute("rdf:resource");
    }

    public String getEtag() {
        return this.etag;
    }

    public void setEtag(OslcResponse response) {
        this.etag = response.getResponseHeader("ETag").replaceAll("\"", "");
    }
    
}
