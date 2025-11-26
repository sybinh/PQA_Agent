/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import OslcAccess.OslcRecordTypeI;
import util.EcvEnumeration;

/**
 *
 * @author gug2wi
 */
public enum Rq1RecordType implements EcvEnumeration, OslcRecordTypeI {

    PROJECT("Project"),
    RELEASE("Release"),
    WORKITEM("Workitem"),
    ISSUE("Issue"),
    USER("users"),
    CONTACT("Contact"),
    EXTERNAL_LINK("ExternalLink"),
    ATTACHMENT("attachment"),
    ATTACHMENT_MAPPING("AttachmentMapping"),
    HISTORY_LOG("HistoryLog"),
    METADATA("RQ1_Metadata"),
    IRM("Issuereleasemap", "IRM"),
    RRM("Releasereleasemap", "RRM"),
    USER_ROLE("Rq1_RoleUser"),
    QUERY_HIT("QueryHit"),
    PROBLEM("Problem");

    //
    private final String dbText;
    private final String uiText;

    private Rq1RecordType(String dbText) {
        assert (dbText != null);
        assert (dbText.isEmpty() == false);
        this.dbText = dbText;
        this.uiText = dbText;
    }

    private Rq1RecordType(String dbText, String uiText) {
        assert (dbText != null);
        assert (dbText.isEmpty() == false);
        assert (uiText != null);
        assert (uiText.isEmpty() == false);
        this.dbText = dbText;
        this.uiText = uiText;
    }

    @Override
    public String getText() {
        return uiText;
    }

    @Override
    public String getOslcType() {
        return (dbText);
    }

    public static Rq1RecordType getRecordType(String recordTypeString) {
        assert (recordTypeString != null);
        assert (recordTypeString.isEmpty() == false);

        for (Rq1RecordType recordType : values()) {
            if (recordType.getOslcType().toLowerCase().equals(recordTypeString.toLowerCase())) {
                return (recordType);
            }
        }
        return (null);
    }

}
