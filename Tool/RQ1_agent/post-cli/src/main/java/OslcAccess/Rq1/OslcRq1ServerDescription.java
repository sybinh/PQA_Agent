/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Rq1;

import OslcAccess.OslcServerDescriptionI;
import java.util.logging.Logger;

/**
 * Contains the information needed to connect to a specific OSLC server to
 * access the RQ1 database.
 *
 * @author GUG2WI
 */
public enum OslcRq1ServerDescription implements OslcServerDescriptionI {

    PRODUCTIVE("RQ1_PRODUCTIVE", "RQ1_PRODUCTIVE",
            "https://rb-dgsrq1-oslc-p.de.bosch.com/cqweb/oslc/repo/RQ1_PRODUCTIVE/db/RQONE/",
            "https://rb-dgsrq1.de.bosch.com/cqweb/restapi/RQ1_PRODUCTIVE/RQONE/RECORD/",
            "https://rb-dgsrq1.de.bosch.com/rq1plus/index.html#/RQ1_PRODUCTIVE/RQONE/RECORD/",
            "https://rb-dgsrq1-oslc-p.de.bosch.com/cqweb/oslc/session/",
            "RQONE01044125"),
    APPROVAL("RQ1_APPROVAL", "RQ1_APPROVAL",
            "https://rb-dgsrq1-approval.de.bosch.com/cqweb/oslc/repo/RQ1_APPROVAL/db/RQONE/",
            "https://rb-dgsrq1-approval.de.bosch.com/cqweb/restapi/RQ1_APPROVAL/RQONE/RECORD/",
            "https://rb-dgsrq1-approval.de.bosch.com/rq1plus/index.html#/RQ1_APPROVAL/RQONE/RECORD/",
            "https://rb-dgsrq1-approval.de.bosch.com/cqweb/oslc/session/",
            "RQONE00039415"),
    ACCEPTANCE("RQ1_ACCEPTANCE", "RQ1_ACCEPTANCE",
            "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/",
            "https://rb-dgsrq1-acceptance.de.bosch.com/cqweb/restapi/RQ1_ACCEPTANCE/RQONE/RECORD/",
            "https://rb-dgsrq1-acceptance.de.bosch.com/rq1plus/index.html#/RQ1_ACCEPTANCE/RQONE/RECORD/",
            "https://rb-dgsrq1-acceptance.de.bosch.com/cqweb/oslc/session/",
            "RQONE01044125");

    public enum LinkType {
        RESTAPI,
        RQ1PLUS;
    }

    private final String serverName;
    private final String serverNameForConfigFile;
    private final String oslcUrl;
    private final String linkRestapiUrl;
    private final String linkRq1PlusUrl;
    private final String deleteUrl;
    private final String toolProjectId;

    OslcRq1ServerDescription(
            String serverName,
            String serverNameForConfigFile,
            String oslcUrl,
            String linkRestapiUrl,
            String linkRq1PlusUrl,
            String deleteUrl,
            String toolProjectId) {

        this.serverName = serverName;
        this.serverNameForConfigFile = serverNameForConfigFile;
        this.oslcUrl = oslcUrl;
        this.linkRestapiUrl = linkRestapiUrl;
        this.linkRq1PlusUrl = linkRq1PlusUrl;
        this.deleteUrl = deleteUrl;
        this.toolProjectId = toolProjectId;
    }

    /**
     * Returns the name of the server.
     *
     * @return Name of the server.
     */
    public final String getServerName() {
        return serverName;
    }

    public String getServerNameForConfigFile() {
        return serverNameForConfigFile;
    }

    @Override
    public final String getOslcUrl() {
        return oslcUrl;
    }

    public final String buildUrl(LinkType linkType, String rq1Id, String recordType) {
        assert (linkType != null);
        assert (rq1Id != null);
        assert (rq1Id.isEmpty() == false);
        assert (recordType != null);
        assert (recordType.isEmpty() == false);

        switch (linkType) {
            case RESTAPI:
                return (linkRestapiUrl + rq1Id + "?format=HTML&recordType=" + recordType);
            case RQ1PLUS:
                return (linkRq1PlusUrl + rq1Id + "&format=HTML&recordType=" + recordType);
            default:
                Logger.getLogger(this.getClass().getCanonicalName()).severe("Unexpected link type " + linkType.toString());
                return (linkRestapiUrl + rq1Id + "?format=HTML&recordType=" + recordType);
        }

    }

    @Override
    public final String getDeleteUrl() {
        return (deleteUrl);
    }

    public String getToolProjectId() {
        return toolProjectId;
    }

    /**
     * Returns the server name as string.
     *
     * @return Server name.
     */
    @Override
    public final String toString() {
        return serverName;
    }

    /**
     *
     * @param name, the name of the server
     * @return the found server Description, or null if not found
     */
    public static OslcRq1ServerDescription getDescriptionByName(String name) {
        for (OslcRq1ServerDescription desc : OslcRq1ServerDescription.values()) {
            if (desc.getServerName().equals(name)) {
                return desc;
            }
        }
        return null;
    }

    @Override
    public int getMaxRequestNumberPerSession() {
        return (250);
    }
}
