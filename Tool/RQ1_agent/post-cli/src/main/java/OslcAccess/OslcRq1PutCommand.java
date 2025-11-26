/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author GUG2WI
 */
class OslcRq1PutCommand extends OslcPutCommand {

    static public enum Action {

        MODIFY("modify"),
        FORWARD("forward");

        final private String commandString;

        Action(String commandString) {
            assert (commandString != null);
            assert (commandString.isEmpty() == false);
            this.commandString = commandString;
        }

        String getCommandString() {
            return (commandString);
        }

    }

    final private OslcPropertySet writeProperties;
    private OslcSelection selection;
    private Action action;
    private List<OslcPropertyName> writeOrder = null;

    public OslcRq1PutCommand() {
        writeProperties = new OslcPropertySet();
        selection = null;
        action = Action.MODIFY;
    }

    public void setAction(Action action) {
        assert (action != null);
        this.action = action;
    }

    public void setWriteOrder(List<OslcPropertyName> writeOrder) {
        assert (writeOrder != null);

        this.writeOrder = new ArrayList<>(writeOrder);
    }

    public OslcRq1PutCommand setSelection(OslcSelection selection) {
        assert (selection != null);
        assert (selection.getRdfAbout() != null);
        assert (this.selection == null);
        this.selection = selection;
        return (this);
    }

    public OslcRq1PutCommand addPropertyField(String propertyPath, String value) {
        assert (propertyPath != null);
        assert (propertyPath.isEmpty() == false);
        assert (value != null);
        writeProperties.addProperty(new OslcPropertyField(propertyPath, value));
        return (this);
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);
        assert (selection != null);
        assert (selection.getSelectionType() == OslcSelection.SelectionType.RDF_ABOUT);
        assert (writeProperties.getProperties().isEmpty() == false);

        StringBuilder builder = new StringBuilder(100);

        //
        // Add URL
        //
        builder.append(oslcUrl).append("record/");

        //
        // Append record id from rdf:about
        //
        String[] rdfAboutParts = selection.getRdfAbout().split("/");
        builder.append(rdfAboutParts[rdfAboutParts.length - 1]);

        //
        // Add properties to write
        //
        builder.append("?oslc.properties=");
        appendProperties(builder, writeProperties.getProperties());
        builder.append("&rcm.action=").append(action.getCommandString());
        return builder.toString();
    }

    @Override
    public String buildBodyString() {

        assert (selection != null);
        assert (writeProperties.getProperties().isEmpty() == false);

        StringBuilder builder = new StringBuilder(100);

        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n");

        EcvXmlContainerElement rdf_RDF = new EcvXmlContainerElement("rdf:RDF");
        rdf_RDF.addAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        rdf_RDF.addAttribute("xmlns:cq", "http://www.ibm.com/xmlns/prod/rational/clearquest/1.0/");
        rdf_RDF.addAttribute("xmlns:dcterms", "http://purl.org/dc/terms/");
        rdf_RDF.addAttribute("xmlns:oslc", "http://open-services.net/ns/core#");
        rdf_RDF.addAttribute("xmlns:oslc_cm", "http://open-services.net/ns/cm#");
        rdf_RDF.addAttribute("xmlns:rdfs", "http://www.w3.org/2000/01/rdf-schema#");

        EcvXmlContainerElement oslc_cm_ChangeRequest = new EcvXmlContainerElement("oslc_cm:ChangeRequest");
        oslc_cm_ChangeRequest.addAttribute("rdf:about", selection.getRdfAbout());
        rdf_RDF.addElement(oslc_cm_ChangeRequest);

        for (OslcProperty property : writeProperties.getProperties()) {

            assert (property instanceof OslcPropertyField) : property.getName();

            EcvXmlElement cq_fieldName;
            if ((((OslcPropertyField) property).getValue() != null) && (((OslcPropertyField) property).getValue().isEmpty() == false)) {
                cq_fieldName = new EcvXmlTextElement("cq:" + property.getName(), ((OslcPropertyField) property).getValue());
            } else {
                cq_fieldName = new EcvXmlEmptyElement("cq:" + property.getName());
            }
            cq_fieldName.addAttribute("rdf:ID", property.getName());
            oslc_cm_ChangeRequest.addElement(cq_fieldName);

            // Add property at the end of the write order, if it is not yet in the write order.
            if (writeOrder != null) {
                OslcPropertyName oslcPropertyName = new OslcPropertyName(property.getName());
                if (writeOrder.contains(oslcPropertyName) == false) {
                    writeOrder.add(oslcPropertyName);
                }
            }
        }

        //
        // Add field order
        //
        if ((writeOrder != null) && (writeOrder.isEmpty() == false)) {
            int fieldOrder = 0;
            for (OslcPropertyName propertyName : writeOrder) {
                fieldOrder++;
                EcvXmlContainerElement rdfStatement = new EcvXmlContainerElement("rdf:Statement");
                rdfStatement.addAttribute("rdf:about", "#" + propertyName.getName());
                rdfStatement.addElement(new EcvXmlTextElement("cq:fieldOrder", Integer.toString(fieldOrder)));
                rdf_RDF.addElement(rdfStatement);
            }
        }

        builder.append(rdf_RDF.getXmlString());

        return (builder.toString());
    }

    private void appendProperties(StringBuilder builder, Collection<OslcProperty> properties) {
        boolean firstField = true;
        for (OslcProperty property : properties) {
            if (firstField == false) {
                builder.append(',');
            }
            firstField = false;
            builder.append("cq:").append(property.getName());
            if (property instanceof OslcPropertyRecord) {
                builder.append("{");
                appendProperties(builder, ((OslcPropertyRecord) property).getProperties());
                builder.append("}");
            }
        }
    }

    @Override
    public String getAddressForUi() {
        return (selection.getRdfAbout());
    }

}
