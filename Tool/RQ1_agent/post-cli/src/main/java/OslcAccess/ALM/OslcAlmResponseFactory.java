/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import OslcAccess.OslcResponse;
import OslcAccess.OslcResponseFactoryI;
import java.util.List;
import java.util.Map;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class OslcAlmResponseFactory implements OslcResponseFactoryI {

    @Override
    public OslcResponse build(Map<String, String> responseHeader, EcvXmlElement responseBodyXml, String responseBodyString) {
        return (new OslcResponse_AlmSpecific(responseHeader, responseBodyXml, responseBodyString));
    }

    @Override
    public OslcResponse build(Map<String, String> responseHeader, List<byte[]> responseInBinaryFormat) {
        return (new OslcResponse_AlmSpecific(responseHeader, responseInBinaryFormat));
    }

}
