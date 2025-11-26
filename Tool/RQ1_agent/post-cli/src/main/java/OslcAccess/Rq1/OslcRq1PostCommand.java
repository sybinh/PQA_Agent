/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Rq1;

import OslcAccess.OslcPostCommand;
import OslcAccess.OslcProperty;
import OslcAccess.OslcPropertyField;
import OslcAccess.OslcPropertyListField;
import OslcAccess.OslcPropertyName;
import OslcAccess.OslcPropertySet;
import OslcAccess.OslcRecordTypeI;
import java.util.ArrayList;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlTextElement;

/**
 *
 * @author GUG2WI
 */
public class OslcRq1PostCommand extends OslcPostCommand {

    final private OslcRecordTypeI recordType;
    final private OslcPropertySet writeProperties;
    private List<OslcPropertyName> writeOrder = null;

    public OslcRq1PostCommand(OslcRecordTypeI recordType) {
        assert (recordType != null);

        this.recordType = recordType;
        writeProperties = new OslcPropertySet();
    }

    public void setWriteOrder(List<OslcPropertyName> writeOrder) {
        assert (writeOrder != null);

        this.writeOrder = new ArrayList<>(writeOrder);
    }

    public OslcRq1PostCommand addPropertyField(String propertyPath, String value) {
        assert (propertyPath != null);
        assert (propertyPath.isEmpty() == false);
        assert (value != null);
        writeProperties.addProperty(new OslcPropertyField(propertyPath, value));
        return (this);
    }
    
    public OslcRq1PostCommand addPropertyField(String propertyPath, List<String> values) {
        assert (propertyPath != null);
        assert (propertyPath.isEmpty() == false);
        assert (values != null);
        writeProperties.addProperty(new OslcPropertyListField(propertyPath, values));
        return (this);
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);
        assert (writeProperties.getProperties().isEmpty() == false);

        StringBuilder builder = new StringBuilder(100);

        //
        // Add URL
        //
        builder.append(oslcUrl).append("record/");

        return builder.toString();
    }

    @Override
    public String buildBodyString() {
        assert (writeProperties.getProperties().isEmpty() == false);

        //
        // Add header
        //
        EcvXmlContainerElement rdf_RDF = new EcvXmlContainerElement("rdf:RDF");
        rdf_RDF.addAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        rdf_RDF.addAttribute("xmlns:cq", "http://www.ibm.com/xmlns/prod/rational/clearquest/1.0/");
        rdf_RDF.addAttribute("xmlns:dcterms", "http://purl.org/dc/terms/");
        rdf_RDF.addAttribute("xmlns:oslc", "http://open-services.net/ns/core#");
        rdf_RDF.addAttribute("xmlns:oslc_cm", "http://open-services.net/ns/cm#");
        rdf_RDF.addAttribute("xmlns:rdfs", "http://www.w3.org/2000/01/rdf-schema#");

        EcvXmlContainerElement oslc_cm_ChangeRequest = new EcvXmlContainerElement("oslc_cm:ChangeRequest");

        oslc_cm_ChangeRequest.addAttribute("dcterms:type", recordType.getOslcType());
        rdf_RDF.addElement(oslc_cm_ChangeRequest);

        //
        // Add attributes
        //
        for (OslcProperty property : writeProperties.getProperties()) {
            assert (property instanceof OslcPropertyField || property instanceof OslcPropertyListField) : property.getName();
            
            if (property instanceof OslcPropertyField) {
                EcvXmlElement cq_fieldName = generateXmlElement((OslcPropertyField)property);
                if (cq_fieldName != null) {
                    oslc_cm_ChangeRequest.addElement(cq_fieldName);
                }
            }
            else if (property instanceof OslcPropertyListField) {
                for (String value : ((OslcPropertyListField)property).getValues()) {
                    EcvXmlElement cq_fieldName = generateXmlElement(new OslcPropertyField(property.getName(), value));
                    if (cq_fieldName != null) {
                        oslc_cm_ChangeRequest.addElement(cq_fieldName);
                    }
                }
            }
            
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

        //
        // Create body string
        //
        StringBuilder builder = new StringBuilder(100);
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n");
        builder.append(rdf_RDF.getXmlString());

        return (builder.toString());
    }
    
    @Override
    public String getAddressForUi() {
        return (recordType.getText());
    }
}
