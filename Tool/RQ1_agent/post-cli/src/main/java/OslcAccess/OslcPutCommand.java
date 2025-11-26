/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 *
 * @author gug2wi
 */
public abstract class OslcPutCommand extends OslcCommand {

    public OslcPutCommand() {
    }

    public OslcPutCommand(OslcProtocolVersion protocolVersion) {
        super(protocolVersion);
    }

    /**
     * Overwrite this method, if the PUT command has a body.
     *
     * @return null, if no body is available for the PUT command. Otherwise the
     * body.
     */
    public String buildBodyString() {
        return (null);
    }

}
