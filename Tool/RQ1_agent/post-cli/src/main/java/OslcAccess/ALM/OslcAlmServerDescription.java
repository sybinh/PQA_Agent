/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import OslcAccess.OslcServerDescriptionI;

/**
 * Provides information about the available DNG servers.
 *
 * @author GUG2WI
 */
public enum OslcAlmServerDescription implements OslcServerDescriptionI {

    /**
     * Production
     */
    PRODUCTION(
            "ALM-PRODUCTIVE",
            "https://rb-alm-05-p.de.bosch.com",
            "https://rb-alm-05-p.de.bosch.com/jts/j_security_check",
            "https://rb-alm-05-p.de.bosch.com/ccm/resource/itemName/com.ibm.team.workitem.WorkItem/"),
    /**
     * Formal Acceptance testing
     */
    ACCEPTANCE(
            //
            // Beispiel für einen GUI link zur ACCEPTANCE: 
            // https://rb-alm-05-q.de.bosch.com/ccm/web/projects/PMT%20Cluster%2C%20Product%20and%20Team%20Management%20(CCM)#action=com.ibm.team.workitem.viewWorkItem&id=135534 
            //
            "ALM-ACCEPTANCE",
            "https://rb-alm-05-q.de.bosch.com",
            "https://rb-alm-05-q.de.bosch.com/jts/j_security_check",
            "https://rb-alm-05-q.de.bosch.com/ccm/resource/itemName/com.ibm.team.workitem.WorkItem/"),
    /**
     * Trainings and tests
     */
    TEST("ALM-TEST",
            "https://rb-alm-05-t.de.bosch.com",
            "https://rb-alm-05-t.de.bosch.com/jts/j_security_check",
            "https://rb-alm-05-t.de.bosch.com/ccm/resource/itemName/com.ibm.team.workitem.WorkItem/"),
    /**
     * Development
     */
    DEVELOPMENT("ALM-DEVELOPMENT",
            "https://rb-alm-05-d.de.bosch.com",
            "https://rb-alm-05-d.de.bosch.com/jts/j_security_check",
            "https://rb-alm-05-d.de.bosch.com/ccm/resource/itemName/com.ibm.team.workitem.WorkItem/"),
    
    /*
    * New Alm 7.0.2 Test Server
    */
    TEST_7_0_2("ALM-7.0.2",
    "https://rb-alm-01-t4.de.bosch.com",
    "https://rb-alm-01-t4.de.bosch.com/jts/j_security_check",
    "https://rb-alm-01-t4.de.bosch.com/ccm/resource/itemName/com.ibm.team.workitem.WorkItem/");

    final private String name;
    final private String oslcUrl;
    final private String securityCheckUrl;
    final private String workItemUrl;

    // https://rb-alm-05-d.de.bosch.com/ccm/resource/itemName/com.ibm.team.workitem.WorkItem/255028
    private OslcAlmServerDescription(String name, String oslcUrl, String securityCheckUrl, String workItemUrl) {
        assert (name != null);
        assert (name.isEmpty() == false);
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);
        assert (securityCheckUrl != null);
        assert (securityCheckUrl.isEmpty() == false);
        assert (workItemUrl != null);
        assert (workItemUrl.isEmpty() == false);

        this.name = name;
        this.oslcUrl = oslcUrl;
        this.securityCheckUrl = securityCheckUrl;
        this.workItemUrl = workItemUrl;
    }

    public String getName() {
        return (name);
    }

    @Override
    public String getOslcUrl() {
        return (oslcUrl);
    }

    public String getSecurityCheckUrl() {
        return securityCheckUrl;
    }

    public String getWorkItemUrl() {
        return workItemUrl;
    }

    @Override
    public String getDeleteUrl() {
        return (null);
    }

    @Override
    public int getMaxRequestNumberPerSession() {
        return (-1);
    }

    private boolean checkUrl(String url) {
        assert (url != null);
        assert (url.isEmpty() == false);
        return (url.startsWith(oslcUrl));
    }

    static public OslcAlmServerDescription getServerDescriptionForUrl(String url) {
        assert (url != null);
        assert (url.isEmpty() == false);

        for (OslcAlmServerDescription value : values()) {
            if (value.checkUrl(url) == true) {
                return (value);
            }
        }

        return (null);
    }

}
