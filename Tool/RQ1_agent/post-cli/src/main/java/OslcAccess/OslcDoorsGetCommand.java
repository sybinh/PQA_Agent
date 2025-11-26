/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.OslcCommandExecutor.ResponseFormat;

/**
 *
 * @author gug2wi
 */
public class OslcDoorsGetCommand extends OslcGetCommand {

    final private String relativeUrl;

    OslcDoorsGetCommand(String relativeUrl) {
        super(OslcProtocolVersion.OSLC_20);
        this.relativeUrl = relativeUrl;
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        return (oslcUrl + "/" + relativeUrl);
    }

    @Override
    public String getAddressForUi() {
        return (relativeUrl);
    }

    @Override
    public ResponseFormat getResponseFormat() {
        return (ResponseFormat.TEXT_OR_XML);
    }

}
