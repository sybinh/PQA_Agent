/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.OslcCommandExecutor.ResponseFormat;

/**
 *
 * @author GUG2WI
 */
public class OslcFileDownload extends OslcGetCommand {

    final private String rdfAbout;

//    private OslcSelection selection;
    public OslcFileDownload(String rdfAbout) {
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);

        this.rdfAbout = rdfAbout;
    }

    @Override
    public ResponseFormat getResponseFormat() {
        return (ResponseFormat.BINARY);
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);

        OslcSelection selection = new OslcSelection().setRdfAbout(rdfAbout);

        StringBuilder builder = new StringBuilder(100);

        builder.append(rdfAbout).append("?");

        assert (builder.toString().isEmpty() == false) : "Type: " + selection.getSelectionType() + " "
                + "rdfAbout: " + selection.getRdfAbout();

        builder.append("oslc.pageSize=1000");

        return builder.toString();
    }

    @Override
    public String getAddressForUi() {
        return (rdfAbout);
    }

}
