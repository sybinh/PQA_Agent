/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Doors;

import OslcAccess.Doors.OslcDoorsProperty;
import OslcAccess.OslcResponse;
import RestClient.Exceptions.FieldNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 * Implements the access to the elements in the doors response.
 *
 * @author gug2wi
 */
public class OslcDoorsPropertyResponse {

    final private List<OslcDoorsProperty> properties;

    OslcDoorsPropertyResponse(OslcResponse oslcResponse) throws FieldNotFoundException {
        assert (oslcResponse != null);

        if (oslcResponse.getResponseBodyList().isEmpty() || oslcResponse.getResponseBodyList().get(0) == null) {
            throw (new FieldNotFoundException("No body received or body is not in XML format."));
        }
        EcvXmlContainerElement body = oslcResponse.getResponseBodyList().get(0);

        EcvXmlContainerElement resourceShape;
        try {
            resourceShape = body.getContainerElement("oslc:ResourceShape");
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No oslc:ResourceShape found in response.", body, ex));
        }

        properties = new ArrayList<>();
        for (EcvXmlContainerElement oslc_property : resourceShape.getContainerElementList("oslc:property")) {
            EcvXmlContainerElement oslc_Property;
            try {
                oslc_Property = oslc_property.getContainerElement("oslc:Property");
            } catch (EcvXmlElement.NotfoundException ex) {
                throw (new FieldNotFoundException("No oslc:Property found in response.", body, ex));
            }

            try {
                properties.add(new OslcDoorsProperty(oslc_Property));
            } catch (FieldNotFoundException ex) {
                throw (new FieldNotFoundException("Field missing for property.", body, ex));
            }
        }
        
        Collections.sort(properties);

    }

    public List<OslcDoorsProperty> getProperties() {
        return properties;
    }

}
