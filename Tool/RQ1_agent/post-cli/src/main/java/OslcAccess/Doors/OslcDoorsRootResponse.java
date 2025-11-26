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
import util.EcvXmlElement;

/**
 * Implements the access to the elements in the doors response.
 *
 * @author gug2wi
 */
public class OslcDoorsRootResponse {

    final private EcvXmlContainerElement xmlResponse;

    OslcDoorsRootResponse(OslcResponse oslcResponse) throws FieldNotFoundException {
        assert (oslcResponse != null);

        if (oslcResponse.getResponseBodyList().isEmpty() || oslcResponse.getResponseBodyList().get(0) == null) {
            throw (new FieldNotFoundException("No body received or body is not in XML format."));
        } else {
            xmlResponse = oslcResponse.getResponseBodyList().get(0);
        }
    }

    final public String getTitle() throws FieldNotFoundException {
        try {
            return (xmlResponse.getText("dc:title"));
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No dcterms:title found in response.", xmlResponse, ex));
        }
    }

}
