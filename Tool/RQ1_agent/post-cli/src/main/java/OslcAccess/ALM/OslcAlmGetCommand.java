/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import OslcAccess.OslcCommandExecutor.ResponseFormat;
import OslcAccess.OslcGetCommand;
import OslcAccess.OslcProtocolVersion;

/**
 *
 * @author CNI83WI
 */
public class OslcAlmGetCommand extends OslcGetCommand {
    private final String url;

    public OslcAlmGetCommand(String url, OslcProtocolVersion protocolVersion) {
        super(protocolVersion);
        this.url = url;
    }
    
    @Override
    public ResponseFormat getResponseFormat() {
        return(ResponseFormat.TEXT_OR_XML);
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        return (url);
    }

    @Override
    public String getAddressForUi() {
        return (url);
    }
    
}
