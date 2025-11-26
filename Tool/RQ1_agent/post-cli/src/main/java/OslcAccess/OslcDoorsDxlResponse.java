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
import util.EcvXmlParser;

/**
 *
 * @author gug2wi
 */
public class OslcDoorsDxlResponse extends OslcDoorsResponse {

    final private EcvXmlContainerElement doorsResult;

    OslcDoorsDxlResponse(OslcResponse oslcResponse) throws FieldNotFoundException {
        assert (oslcResponse != null);

        EcvXmlContainerElement body = getBody(oslcResponse);

        doorsResult = getContainer(body, "doors:DxlServiceResult", "doors:result");
    }

    /**
     * Create a response object for a string created during a test.
     *
     * @param testResponse Response of an XML-command from a test run.
     * @throws util.EcvXmlElement.ParseException
     */
    public OslcDoorsDxlResponse(String testResponse) throws EcvXmlParser.ParseException {
        EcvXmlParser parser = new EcvXmlParser("<doors:result>" + testResponse + "</doors:result>");
        doorsResult = (EcvXmlContainerElement) parser.parse();
    }

    public EcvXmlContainerElement getResult() {
        return (doorsResult);
    }

}
