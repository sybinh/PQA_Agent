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
 * @author gug2wi
 */
public abstract class OslcGetCommand extends OslcCommand {

    public OslcGetCommand() {
    }

    public OslcGetCommand(OslcProtocolVersion protocolVersion) {
        super(protocolVersion);
    }

    public abstract ResponseFormat getResponseFormat();

}
