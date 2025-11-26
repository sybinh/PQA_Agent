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
public class OslcStoredQuerySearch extends OslcGetCommand {

    final private String queryId;

    public OslcStoredQuerySearch(String queryId) {
        super(OslcProtocolVersion.OSLC_10);

        assert (queryId != null);
        assert (queryId.length() >= 2);

        this.queryId = queryId;
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);

        StringBuilder builder = new StringBuilder(100);

        builder.append(oslcUrl).append("query/").append(queryId);

        return (builder.toString());
    }

    @Override
    public String getAddressForUi() {
        return ("Records matching query " + queryId);
    }

    @Override
    public ResponseFormat getResponseFormat() {
        return (ResponseFormat.TEXT_OR_XML);
    }

}
