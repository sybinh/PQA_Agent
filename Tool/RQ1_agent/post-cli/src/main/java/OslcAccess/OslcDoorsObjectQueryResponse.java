/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.Exceptions.FieldNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.EcvXmlCombinedElement;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlElement.EncodeFormat;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author gug2wi
 */
public class OslcDoorsObjectQueryResponse extends OslcDoorsResponse {

    final static public String NAMESPACE_RM_PROPERTY = "rm_property";

    public class ObjectResponseRecord {

        final private String dctermsTitle;
        final private String shortTitle;
        final private String doorsAttributeAbsoluteNumber;
        final private String doorsAttributeObjectHeading;
        final private String doorsAttributeObjectShortText;
        final private String doorsAttributeModifiedOn;
        final private String oslcServiceProvider;
        final private String oslcInstanceShape;
        final private String rmPrimaryText;
        final private Map<String, String> rmProperties;
        final private List<String> defaultLinksReferences;

        private ObjectResponseRecord(EcvXmlContainerElement oslcRequirement) throws FieldNotFoundException {
            assert (oslcRequirement != null);

            //----------------------------------
            //
            // Extract fields defined by DOORS
            //
            //----------------------------------
            dctermsTitle = getText(oslcRequirement, "dcterms:title");
            shortTitle = getText(oslcRequirement, "oslc:shortTitle");
            doorsAttributeAbsoluteNumber = getText(oslcRequirement, "doorsAttribute:absoluteNumber");
            doorsAttributeObjectHeading = getHtmlText(oslcRequirement, "doorsAttribute:objectHeading");
            doorsAttributeObjectShortText = getHtmlText(oslcRequirement, "doorsAttribute:objectShortText");
            doorsAttributeModifiedOn = getHtmlText(oslcRequirement, "doorsAttribute:modifiedOn");
            oslcServiceProvider = getElement(oslcRequirement, "oslc:serviceProvider").getAttribute("rdf:resource");
            oslcInstanceShape = getElement(oslcRequirement, "oslc:instanceShape").getAttribute("rdf:resource");
            rmPrimaryText = getHtmlText(oslcRequirement, "rm:primaryText");

            //-------------------------------------
            //
            // Extract references defined by DOORS
            //
            //-------------------------------------
            defaultLinksReferences = new ArrayList<>();
            for (EcvXmlElement linkElement : oslcRequirement.getElementList("defaultLinks:references")) {
                String linkUrl = linkElement.getAttribute("rdf:resource");
                defaultLinksReferences.add(linkUrl);
            }

            //------------------------------------------
            //
            // Extract fields configured for the module
            //
            //------------------------------------------
            rmProperties = new TreeMap<>();
            for (EcvXmlElement element : oslcRequirement.getElementList()) {
                if (element.getName().startsWith(NAMESPACE_RM_PROPERTY + ":")) {
                    String attributeName = element.getName().substring(NAMESPACE_RM_PROPERTY.length() + 1);
                    String attributeValue = "";
                    if (element instanceof EcvXmlTextElement) {
                        attributeValue = ((EcvXmlTextElement) element).getText();
                    } else if (element instanceof EcvXmlEmptyElement) {
                        attributeValue = "";
                    } else if (element instanceof EcvXmlContainerElement) {
                        attributeValue = ((EcvXmlContainerElement) element).getXmlString_WithoutContainer(EncodeFormat.SHORT_EMPTY_VALUE);
                    } else if (element instanceof EcvXmlCombinedElement) {
                        attributeValue = ((EcvXmlCombinedElement) element).getXmlString_WithoutContainer(EncodeFormat.SHORT_EMPTY_VALUE);
                    } else if (element.hasAttribute("rdf:resource") == true) {
                        attributeValue = element.getAttribute("rdf:resource");
                    }
                    rmProperties.put(attributeName, attributeValue);
                }
            }

        }

        public String getDctermsTitle() {
            return dctermsTitle;
        }

        public String getShortTitle() {
            return shortTitle;
        }

        public String getDoorsAttributeAbsoluteNumber() {
            return doorsAttributeAbsoluteNumber;
        }

        public String getDoorsAttributeObjectHeading() {
            return doorsAttributeObjectHeading;
        }

        public String getDoorsAttributeObjectShortText() {
            return doorsAttributeObjectShortText;
        }

        public String getDoorsAttributeModifiedOn() {
            return doorsAttributeModifiedOn;
        }

        public String getOslcServiceProvider() {
            return oslcServiceProvider;
        }

        public String getOslcInstanceShape() {
            return oslcInstanceShape;
        }

        public String getRmPrimaryText() {
            return rmPrimaryText;
        }

        Map<String, String> getRmProperties() {
            return rmProperties;
        }

        public String getRmProperty(String propertyName) {
            assert (propertyName != null);
            assert (propertyName.isEmpty() == false);

            return (rmProperties.get(propertyName));
        }

        public List<String> getDefaultLinksReferences() {
            return defaultLinksReferences;
        }

    }

    final private List<ObjectResponseRecord> records = new ArrayList<>();

    OslcDoorsObjectQueryResponse(OslcResponse oslcReponse) throws FieldNotFoundException {
        assert (oslcReponse != null);

        EcvXmlContainerElement body = getBody(oslcReponse);

        //-----------------------------------------------------
        // Extract the record directly in container
        //-----------------------------------------------------
        EcvXmlContainerElement oslcRequirement = getOptionalContainer(body, "oslc_rm:Requirement");
        if (oslcRequirement != null) {
            ObjectResponseRecord record = new ObjectResponseRecord(oslcRequirement);
            records.add(record);
        }

        //-----------------------------------------------------
        // Extract the container elements from rdf:Description
        //-----------------------------------------------------
        EcvXmlContainerElement rdfDescription = getOptionalContainer(body, "rdf:Description");
        if (rdfDescription != null) {
            for (EcvXmlContainerElement rdfsMember : rdfDescription.getContainerElementList("rdfs:member")) {
                oslcRequirement = getContainer(rdfsMember, "oslc_rm:Requirement");
                ObjectResponseRecord record = new ObjectResponseRecord(oslcRequirement);
                records.add(record);
            }
        }

        //-----------------------------------------------------
        // Extract the container elements from rdf:Statement
        //-----------------------------------------------------
        EcvXmlContainerElement rdfStatement = getOptionalContainer(body, "rdf:Statement");
        if (rdfStatement != null) {
            for (EcvXmlContainerElement rdfsSubject : rdfStatement.getContainerElementList("rdf:subject")) {
                oslcRequirement = getContainer(rdfsSubject, "oslc_rm:Requirement");
                ObjectResponseRecord record = new ObjectResponseRecord(oslcRequirement);
                records.add(record);
            }
        }

    }

    public List<ObjectResponseRecord> getRecords() {
        return records;
    }

}
