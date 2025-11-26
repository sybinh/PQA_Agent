/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Doors;

import RestClient.Exceptions.FieldNotFoundException;
import java.util.ArrayList;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author gug2wi
 */
public class OslcDoorsProperty implements Comparable<OslcDoorsProperty> {

    static private final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(OslcDoorsProperty.class.getCanonicalName());

    public enum ValueType {
        UNKNOWN("Unknown"),
        DATE_TIME("dateTime"),
        RESOURCE("Resource"),
        XML("XMLLiteral"),
        INTEGER("integer"),
        BOOLEAN("boolean"),
        ANY_RESOURCE("AnyResource"),
        STRING("string");

        final private String typeString;

        ValueType(String typeString) {
            this.typeString = typeString;
        }

        String getTypeString() {
            return (typeString);
        }
    }

    public enum Occurence {
        UNKNOWN("Unknown"),
        EXACTLY_ONE("Exactly-one"),
        ZERO_OR_ONE("Zero-or-one"),
        ZERO_OR_MANY("Zero-or-many");

        final private String occurenceString;

        Occurence(String occurenceString) {
            this.occurenceString = occurenceString;
        }

        String getOccurenceString() {
            return (occurenceString);
        }
    }

    public enum Namespace {
        DOORS_ATTRIBUTE("doorsAttribute"),
        RM_PROPERTY("rm_property"),
        DCTERMS("dcterms"),
        OSLC("oslc"),
        RM("rm"),
        NONE("");

        final private String namespaceString;

        private Namespace(String namespaceString) {
            this.namespaceString = namespaceString;
        }

        public String getNamespaceString() {
            return namespaceString;
        }

    }

    private final String name;
    private final String title;
    private final String description;
    private final ValueType valueType;
    private final Occurence occurence;
    private final boolean readOnly;
    private final Namespace nameSpace;
    private final String propertyDefinition;
    private final List<String> allowedValuesResource;

    public OslcDoorsProperty(EcvXmlContainerElement oslcProperty) throws FieldNotFoundException {
        assert (oslcProperty != null);

        try {
            name = oslcProperty.getText("oslc:name");
            if (oslcProperty.containsElement("dcterms:description") == true) {
                description = oslcProperty.getText("dcterms:description");
            } else {
                description = "";
            }
            title = oslcProperty.getText("dcterms:title");
            valueType = determineValueType(oslcProperty.getElement("oslc:valueType").getAttribute("rdf:resource"));
            occurence = determineOccurenceType(oslcProperty.getElement("oslc:occurs").getAttribute("rdf:resource"));
            readOnly = oslcProperty.getText("oslc:readOnly").equals("true");
            propertyDefinition = oslcProperty.getElement("oslc:propertyDefinition").getAttribute("rdf:resource");
            nameSpace = determineNameSpace(propertyDefinition);
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("Field missing for property.", oslcProperty, ex));
        }

        allowedValuesResource = new ArrayList<>();
        for (EcvXmlElement allowedValue : oslcProperty.getElementList("oslc:allowedValue")) {
            if (allowedValue.hasAttribute("rdf:resource") == true) {
                allowedValuesResource.add(allowedValue.getAttribute("rdf:resource"));
            }
        }

    }

    private ValueType determineValueType(String valueTypePath) {

        if (valueTypePath == null) {
            LOGGER.warning("No value type provided for " + name);
            return (ValueType.UNKNOWN);
        }

        String[] split = valueTypePath.split("\\#");
        if (split.length == 2) {
            String typeString = split[1];
            for (ValueType type : ValueType.values()) {
                if (typeString.equals(type.getTypeString()) == true) {
                    return (type);
                }
            }
        }
        LOGGER.warning("No value type found for " + name + ", type path: " + valueTypePath);
        return (ValueType.UNKNOWN);
    }

    private Occurence determineOccurenceType(String occurencePath) {

        if (occurencePath == null) {
            LOGGER.warning("No occurence provided for " + name);
            return (Occurence.UNKNOWN);
        }

        String[] split = occurencePath.split("\\#");
        if (split.length == 2) {
            String occurenceString = split[1];
            for (Occurence occurence : Occurence.values()) {
                if (occurenceString.equals(occurence.getOccurenceString()) == true) {
                    return (occurence);
                }
            }
        }
        LOGGER.warning("No occurence found for " + name + ", occurence path: " + occurencePath);
        return (Occurence.UNKNOWN);
    }

    private Namespace determineNameSpace(String propertyDefinition) {
        assert (propertyDefinition != null);
        assert (propertyDefinition.isEmpty() == false);

        if (propertyDefinition.contains("doors/attribute#")) {
            return (Namespace.DOORS_ATTRIBUTE);

        } else if ((propertyDefinition.contains("urn:rational:")) && (propertyDefinition.contains("/types/"))) {
            return (Namespace.RM_PROPERTY);

        } else if (propertyDefinition.contains("/dc/terms/")) {
            return (Namespace.DCTERMS);

        } else if (propertyDefinition.contains("open-services.net/ns/core")) {
            return (Namespace.OSLC);

        } else if (propertyDefinition.contains("jazz.net/ns/rm")) {
            return (Namespace.RM);

        } else {
            return (Namespace.NONE);
        }
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public Occurence getOccurence() {
        return occurence;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Namespace getNameSpace() {
        return nameSpace;
    }

    public String getPropertyDefinition() {
        return propertyDefinition;
    }

    public List<String> getAllowedValuesResource() {
        return allowedValuesResource;
    }

    @Override
    public String toString() {
        return ("["
                + nameSpace + ","
                + name + ","
                + title + ","
                + valueType.name() + ","
                + (readOnly ? "RO" : "RW") + ","
                + occurence.name() + ","
                + description
                + "]");
    }

    @Override
    public int compareTo(OslcDoorsProperty other) {

        int result = nameSpace.compareTo(other.nameSpace);
        if (result != 0) {
            return (result);
        }

        result = name.compareTo(other.name);
        if (result != 0) {
            return (result);
        } else {
            return (title.compareTo(other.title));
        }
    }

}
