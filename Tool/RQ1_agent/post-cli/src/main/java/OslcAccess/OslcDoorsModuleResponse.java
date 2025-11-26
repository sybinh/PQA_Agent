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
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 * Implements the access to the elements in the doors response.
 *
 * @author gug2wi
 */
public class OslcDoorsModuleResponse extends OslcDoorsResponse {

    final private String title;
    final private String description;
    final private String parentRdfAbout;
    final private List<String> objectRdfAbouts;

    OslcDoorsModuleResponse(OslcResponse oslcResponse) throws FieldNotFoundException {
        assert (oslcResponse != null);

        EcvXmlContainerElement body = getBody(oslcResponse);
        EcvXmlContainerElement oslcRequirementCollection = getContainer(body, "oslc_rm:RequirementCollection");
        title = getText(oslcRequirementCollection, "dcterms:title");
        description = getOptionalText(oslcRequirementCollection, "dcterms:description");
        String rdfAbout = null;
        if (oslcRequirementCollection.containsElement("dcterms:isPartOf") == true) {

            try {
                EcvXmlElement isPartOf = oslcRequirementCollection.getElement("dcterms:isPartOf");
                rdfAbout = isPartOf.getAttribute("rdf:resource");
            } catch (EcvXmlElement.NotfoundException ex) {
                Logger.getLogger(OslcDoorsModuleResponse.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        parentRdfAbout = rdfAbout;

        objectRdfAbouts = new ArrayList<>();
        for (EcvXmlElement element : oslcRequirementCollection.getElementList("oslc_rm:uses")) {
            objectRdfAbouts.add(element.getAttribute("rdf:resource"));
        }
    }

    final public String getTitle() throws FieldNotFoundException {
        return (title);
    }

    final public String getDescription() {
        return description;
    }

    final public String getParentRdfAbout() {
        return (parentRdfAbout);
    }

    final public List<String> getObjectRdfAbouts() {
        return objectRdfAbouts;
    }

}
