/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.Exceptions.FieldNotFoundException;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 * Implements facility methods for the extraction of data from OSLC-Doors response.
 * @author gug2wi
 */
public class OslcDoorsResponse {

    final protected EcvXmlContainerElement getBody(OslcResponse oslcResponse) throws FieldNotFoundException {
        assert (oslcResponse != null);

        if (oslcResponse.getResponseBodyList().isEmpty() || oslcResponse.getResponseBodyList().get(0) == null) {
            throw (new FieldNotFoundException("No body received or body is not in XML format."));
        }
        return (oslcResponse.getResponseBodyList().get(0));
    }

    final protected EcvXmlContainerElement getContainer(EcvXmlContainerElement topElement, String tag) throws FieldNotFoundException {
        assert (topElement != null);
        assert (tag != null);
        assert (tag.isEmpty() == false);

        try {
            return (topElement.getContainerElement(tag));
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No " + tag + " found in response.", topElement, ex));
        }
    }

    final protected EcvXmlContainerElement getOptionalContainer(EcvXmlContainerElement topElement, String tag) {
        assert (topElement != null);
        assert (tag != null);
        assert (tag.isEmpty() == false);

        try {
            return (topElement.getContainerElement(tag));
        } catch (EcvXmlElement.NotfoundException ex) {
            return (null);
        }
    }

    final protected EcvXmlContainerElement getContainer(EcvXmlContainerElement topElement, String tag1, String tag2) throws FieldNotFoundException {
        assert (topElement != null);
        assert (tag1 != null);
        assert (tag1.isEmpty() == false);
        assert (tag2 != null);
        assert (tag2.isEmpty() == false);

        EcvXmlContainerElement subElement = getContainer(topElement, tag1);
        try {
            return (subElement.getContainerElement(tag2));
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No " + tag1 + "/" + tag2 + " found in response.", topElement, ex));
        }
    }

    final protected EcvXmlElement getElement(EcvXmlContainerElement topElement, String tag) throws FieldNotFoundException {
        assert (topElement != null);
        assert (tag != null);
        assert (tag.isEmpty() == false);

        try {
            return (topElement.getElement(tag));
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No " + tag + " found in response.", topElement, ex));
        }
    }

    final protected String getText(EcvXmlContainerElement topElement, String tag) throws FieldNotFoundException {
        assert (topElement != null);
        assert (tag != null);
        assert (tag.isEmpty() == false);

        try {
            return (topElement.getText(tag));
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No " + tag + " found in response.", topElement, ex));
        }
    }

    final protected String getHtmlText(EcvXmlContainerElement topElement, String tag) throws FieldNotFoundException {
        assert (topElement != null);
        assert (tag != null);
        assert (tag.isEmpty() == false);

        try {
            return (topElement.getHtmlText(tag));
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No " + tag + " found in response.", topElement, ex));
        }
    }

    final protected String getOptionalText(EcvXmlContainerElement topElement, String tag) throws FieldNotFoundException {
        assert (topElement != null);
        assert (tag != null);
        assert (tag.isEmpty() == false);

        try {
            return (topElement.getText(tag));
        } catch (EcvXmlElement.NotfoundException ex) {
            return ("");
        }
    }

}
