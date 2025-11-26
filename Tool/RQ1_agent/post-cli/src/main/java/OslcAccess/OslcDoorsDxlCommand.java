/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import util.EcvXmlContainerElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author gug2wi
 */
public class OslcDoorsDxlCommand extends OslcPutCommand {

    private final String dxlUrl;
    private final String command;
    private final String parameter;

    OslcDoorsDxlCommand(String dxlUrl, String command, String parameter) {
        super(OslcProtocolVersion.OSLC_20_RDF_XML);
        assert (dxlUrl != null);
        assert (command != null);
        assert (parameter != null);

        this.dxlUrl = dxlUrl;
        this.command = command;
        this.parameter = parameter;
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        return (dxlUrl + "/" + command);
    }

    @Override
    public String buildBodyString() {
        StringBuilder builder = new StringBuilder(100);

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        EcvXmlContainerElement rdf_RDF = new EcvXmlContainerElement("rdf:RDF");
        rdf_RDF.addAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        rdf_RDF.addAttribute("xmlns:doors", "http://jazz.net/doors/xmlns/prod/jazz/doors/2.0/");

        EcvXmlContainerElement doors_Arguments = new EcvXmlContainerElement("doors:Arguments");
        rdf_RDF.addElement(doors_Arguments);

        if ((parameter != null) && (parameter.isEmpty() == false)) {
            doors_Arguments.addElement(new EcvXmlTextElement("doors:arguments", parameter));
        } else {
            doors_Arguments.addElement(new EcvXmlEmptyElement("doors:arguments"));
        }

        builder.append(rdf_RDF.getXmlString());

        return (builder.toString());
    }

    @Override
    public String getAddressForUi() {
        return (buildCommandString(null));
    }

}
