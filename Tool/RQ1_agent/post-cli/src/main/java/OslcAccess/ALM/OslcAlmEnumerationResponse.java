/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import OslcAccess.OslcResponse;
import RestClient.Exceptions.ResponseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class OslcAlmEnumerationResponse {

    private final static Logger LOGGER = Logger.getLogger(OslcAlmEnumerationResponse.class.getCanonicalName());

    public static class Value {

        final private String resource;
        final private String identifier;
        final private String title;

        Value(String resource, String identifier, String title) {
            this.resource = resource;
            this.identifier = identifier;
            this.title = title;
        }

        public String getResource() {
            return resource;
        }

        public String getIdentifier() {
            return identifier;
        }

        public String getTitle() {
            return title;
        }

    }

    private final List<Value> valueList = new ArrayList<>();

    //--------------------------------------------------------------------------
    //
    // Members and object creation
    //
    //--------------------------------------------------------------------------
    private OslcAlmEnumerationResponse() {

    }

    private void addValueForXmlElement(EcvXmlContainerElement xmlResponse, EcvXmlContainerElement element) throws ResponseException {
        assert (xmlResponse != null);
        assert (element != null);

        EcvXmlContainerElement xml_rtc_xm_Literal;
        try {
            xml_rtc_xm_Literal = element.getContainerElement("rtc_cm:Literal");
        } catch (EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.SEVERE, "Missing content in tag: " + element.getName(), ex);
            ToolUsageLogger.logError(OslcAlmEnumerationResponse.class.getCanonicalName(), ex);
            throw (new ResponseException("Missing content in tag: " + element.getName(), xmlResponse));
        }

        try {
            String resource = xml_rtc_xm_Literal.getAttribute("rdf:about");
            String text = xml_rtc_xm_Literal.getText("dcterms:title");
            String identifier = xml_rtc_xm_Literal.getText("dcterms:identifier");
            valueList.add(new Value(resource, identifier, text));
        } catch (EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.SEVERE, "Missing content in tag: " + element.getName(), ex);
            ToolUsageLogger.logError(OslcAlmEnumerationResponse.class.getCanonicalName(), ex);
            throw (new ResponseException("Missing content in tag: " + element.getName(), xmlResponse));
        }

    }

    public List<Value> getValues() {
        return valueList;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Value field : valueList) {
            b.append(field.identifier).append(" - ").append(field.title).append("\n");
        }
        return (b.toString());
    }

    //--------------------------------------------------------------------------
    //
    // Static Builder Method
    //
    //--------------------------------------------------------------------------
    public static OslcAlmEnumerationResponse build(OslcResponse response) throws ResponseException {
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
        EcvXmlContainerElement xml_rdf_RDF = reponseBodyList.get(0);
        if ("rdf:RDF".equals(xml_rdf_RDF.getName()) == false) {
            LOGGER.log(Level.SEVERE, "Unexpected name of body in response: {0}", xml_rdf_RDF.getName());
            throw (new ResponseException("Unexpected name of body in response.", xml_rdf_RDF));
        }

        //
        // Go into oslc:ResponseInfo
        //
        EcvXmlContainerElement xml_ResponseInfo;
        try {
            xml_ResponseInfo = xml_rdf_RDF.getContainerElement("oslc:ResponseInfo");
        } catch (EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.SEVERE, "Missing oslc:ResponseInfo.");
            ToolUsageLogger.logError(OslcAlmEnumerationResponse.class.getCanonicalName(), ex);
            throw (new ResponseException("Missing oslc:ResponseInfo", xml_rdf_RDF));
        }

        OslcAlmEnumerationResponse almResponse = new OslcAlmEnumerationResponse();

        //
        // Select element that contains the values.
        //
        List<EcvXmlContainerElement> xnl_rdfs_member = xml_ResponseInfo.getContainerElementList("rdfs:member");

        //
        // Loop for all enumerations
        //
        for (EcvXmlContainerElement xmlValue : xnl_rdfs_member) {
            almResponse.addValueForXmlElement(xml_ResponseInfo, xmlValue);
        }

        LOGGER.finer("\n----- Result of build ----\n" + almResponse.toString() + "----------");

        return (almResponse);
    }
}
