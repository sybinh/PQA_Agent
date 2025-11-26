/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Doors;

import OslcAccess.OslcResponse;
import RestClient.Exceptions.FieldNotFoundException;
import util.EcvXmlContainerElement;

/**
 * Implements the access to the elements in the doors response.
 *
 * @author gug2wi
 */
public class OslcDoorsModuleServiceResponse extends OslcDoorsResponse {

    final private String title;
    final private String description;
    final private String queryBase;
    final private String resourceShape;

    OslcDoorsModuleServiceResponse(OslcResponse oslcResponse) throws FieldNotFoundException {
        assert (oslcResponse != null);

        EcvXmlContainerElement body = getBody(oslcResponse);

        description = getOptionalText(body, "dcterms:description");

        EcvXmlContainerElement serviceProvider = getContainer(body, "oslc:ServiceProvider");
        EcvXmlContainerElement service = getContainer(serviceProvider, "oslc:service", "oslc:Service");
        EcvXmlContainerElement queryCapability = getContainer(service, "oslc:queryCapability", "oslc:QueryCapability");

        title = getOptionalText(queryCapability, "oslc:label");

        queryBase = getElement(queryCapability, "oslc:queryBase").getAttribute("rdf:resource");
        if ((queryBase == null) || (queryBase.isEmpty() == true)) {
            throw (new FieldNotFoundException("Field oslc:queryBase is empty.", body));
        }

        resourceShape = getElement(queryCapability, "oslc:resourceShape").getAttribute("rdf:resource");
        if ((resourceShape == null) || (resourceShape.isEmpty() == true)) {
            throw (new FieldNotFoundException("Field oslc:resourceShape is empty.", body));
        }
    }

    final public String getTitle() {
        return (title);
    }

    final public String getDescription() {
        return (description);
    }

    final public String getQueryBase() {
        return queryBase;
    }

    final public String getResourceShape() {
        return resourceShape;
    }

}
