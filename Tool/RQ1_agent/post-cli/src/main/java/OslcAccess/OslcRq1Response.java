/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.Exceptions.ResponseException;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author gug2wi
 */
class OslcRq1Response {

    final private OslcResponse oslcResponse;

    protected OslcRq1Response(OslcResponse oslcResponse) {
        assert (oslcResponse != null);
        this.oslcResponse = oslcResponse;
    }

    final String getTitle() {
        String title = "";
        if (oslcResponse.getResponseBodyList().isEmpty() == false) {
            try {
                title = oslcResponse.getResponseBodyList().get(0).getContainerElement("oslc:ResponseInfo").getTextElement("dcterms:title").getText();
            } catch (EcvXmlElement.NotfoundException ex) {
                try {
                    title = oslcResponse.getResponseBodyList().get(0).getTextElement("title").getText();
                } catch (EcvXmlElement.NotfoundException ex2) {
                }
            }
        }
        return (title);
    }

    final OslcResponseRecordI getSolelyRecord() throws ResponseException {
        return (oslcResponse.getSolelyRecord());
    }

    final OslcRecordIdentifier getOslcRecordReference() throws ResponseException {
        return getSolelyRecord().getOslcRecordIdentifier();
    }

    final List<OslcResponseRecordI> getRecords() throws ResponseException {
        return (oslcResponse.getRecords());
    }

    final EcvXmlContainerElement getXmlContent() {
        if (oslcResponse.getResponseBodyList().isEmpty() || oslcResponse.getResponseBodyList().get(0) == null) {
            return new EcvXmlContainerElement("Empty_Response");
        } else {
            return oslcResponse.getResponseBodyList().get(0);
        }
    }
}
