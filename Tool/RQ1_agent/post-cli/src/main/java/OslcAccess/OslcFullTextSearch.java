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
public class OslcFullTextSearch extends OslcGetCommand {

    final private String searchString;
    final private OslcRecordTypeI recordType;

    public OslcFullTextSearch(String searchString) {
        super(OslcProtocolVersion.OSLC_10);

        assert (searchString != null);
        assert (searchString.length() >= 2);

        this.searchString = searchString;
        this.recordType = null;
    }

    public OslcFullTextSearch(String searchString, OslcRecordTypeI recordType) {
        super(OslcProtocolVersion.OSLC_10);

        assert (searchString != null);
        assert (searchString.length() >= 2);
        assert (recordType != null);

        this.searchString = searchString;
        this.recordType = recordType;
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);

        StringBuilder builder = new StringBuilder(100);

        builder.append(oslcUrl).append("record/?");
        if (recordType != null) {
            builder.append("rcm.type=").append(recordType.getOslcType());
            builder.append("&");
        }
        builder.append("oslc_cm.query=oslc_cm:searchTerms=\"").append(encodeForHttp(searchString)).append("\"");

        return builder.toString();
    }

    @Override
    public String getAddressForUi() {
        if (recordType != null) {
            return ("Records of type " + recordType.getOslcType() + " containing " + searchString);
        } else {
            return ("Records containing " + searchString);
        }
    }

    @Override
    public ResponseFormat getResponseFormat() {
        return (ResponseFormat.TEXT_OR_XML);
    }

}
