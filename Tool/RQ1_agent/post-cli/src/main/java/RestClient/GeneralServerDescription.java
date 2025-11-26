/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

import DataStore.MCR.MCR_ServerDescription;
import OslcAccess.ALM.OslcAlmServerDescription;
import OslcAccess.Doors.OslcDoorsServerDescription;
import OslcAccess.Rq1.OslcRq1ServerDescription;
import RestClient.HIS.RestServerDescription_HIS;
import RestClient.Rq1Poller.RestServerDescription_Rq1Poller;
import RestClient.iCDM.RestServerDescription_iCDM;

/**
 * Container for all server connections cumulated by a name.
 *
 * @author GUG2WI
 */
public enum GeneralServerDescription {

    PRODUCTIVE("RQ1_PRODUCTIVE",
            OslcRq1ServerDescription.PRODUCTIVE,
            RestServerDescription_Rq1Poller.PRODUCTIVE,
            OslcDoorsServerDescription.PRODUCTIVE,
            RestServerDescription_HIS.PRODUCTIVE,
            OslcAlmServerDescription.PRODUCTION,
            MCR_ServerDescription.PRODUCTIVE,
            RestServerDescription_iCDM.PRODUCTIVE),
    APPROVAL("RQ1_APPROVAL",
            OslcRq1ServerDescription.APPROVAL,
            RestServerDescription_Rq1Poller.PRODUCTIVE,
            OslcDoorsServerDescription.TEST,
            RestServerDescription_HIS.DEVELOPMENT,
            OslcAlmServerDescription.TEST,
            MCR_ServerDescription.QUA2_TEST,
            RestServerDescription_iCDM.TEST),
    ACCEPTANCE("RQ1_ACCEPTANCE",
            OslcRq1ServerDescription.ACCEPTANCE,
            RestServerDescription_Rq1Poller.PRODUCTIVE,
            OslcDoorsServerDescription.PRODUCTIVE,
            RestServerDescription_HIS.PRODUCTIVE,
            OslcAlmServerDescription.ACCEPTANCE,
            MCR_ServerDescription.QUA2_TEST,
            RestServerDescription_iCDM.PRODUCTIVE),
    ACCEPTANCE_TEST("RQ1_ACCEPTANCE + DOORS/HIS-TEST", // Used for testing
            OslcRq1ServerDescription.ACCEPTANCE,
            RestServerDescription_Rq1Poller.PRODUCTIVE,
            OslcDoorsServerDescription.TEST,
            RestServerDescription_HIS.REGRESSION_TEST,
            OslcAlmServerDescription.ACCEPTANCE,
            MCR_ServerDescription.QUA2_TEST,
            RestServerDescription_iCDM.PRODUCTIVE),
    DEVELOPMENT("DNG_DEVELOPMENT", // Used for testing
            OslcRq1ServerDescription.APPROVAL,
            RestServerDescription_Rq1Poller.PRODUCTIVE,
            OslcDoorsServerDescription.TEST,
            RestServerDescription_HIS.DEVELOPMENT,
            OslcAlmServerDescription.DEVELOPMENT,
            MCR_ServerDescription.QUA2_TEST,
            RestServerDescription_iCDM.TEST);

    final private String name;
    final private OslcRq1ServerDescription rq1Server;
    final private RestServerDescription_Rq1Poller rq1PollerServer;
    final private OslcDoorsServerDescription doorsServer;
    final private RestServerDescription_HIS hisServer;
    final private OslcAlmServerDescription almServer;
    final private MCR_ServerDescription mcrServer;
    final private RestServerDescription_iCDM iCDMServer;

    GeneralServerDescription(String name,
            OslcRq1ServerDescription rq1Server,
            RestServerDescription_Rq1Poller rq1PollerServer,
            OslcDoorsServerDescription doorsServer,
            RestServerDescription_HIS hisServer,
            OslcAlmServerDescription almServer,
            MCR_ServerDescription mcrServer,
            RestServerDescription_iCDM iCDMServer) {

        assert (name != null);
        assert (name.isEmpty() == false);
        assert (rq1Server != null);
        assert (rq1PollerServer != null);
        assert (doorsServer != null);
        assert (hisServer != null);
        assert (almServer != null);
        assert (mcrServer != null);
        assert (iCDMServer != null);

        this.name = name;
        this.rq1Server = rq1Server;
        this.rq1PollerServer = rq1PollerServer;
        this.doorsServer = doorsServer;
        this.hisServer = hisServer;
        this.almServer = almServer;
        this.mcrServer = mcrServer;
        this.iCDMServer = iCDMServer;
    }

    @Override
    public final String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public OslcRq1ServerDescription getRq1Server() {
        return rq1Server;
    }
    
    public RestServerDescription_Rq1Poller getRq1PollerServer() {
        return rq1PollerServer;
    }

    public OslcDoorsServerDescription getDoorsServer() {
        return doorsServer;
    }

    public RestServerDescription_HIS getHisServer() {
        return hisServer;
    }

    public OslcAlmServerDescription getAlmServer() {
        return almServer;
    }

    public MCR_ServerDescription getMcrServer() {
        return mcrServer;
    }

    public RestServerDescription_iCDM getiCDMServer() {
        return (iCDMServer);
    }

    /**
     *
     * @param name, the name of the server
     * @return the found server Description, or null if not found
     */
    public static GeneralServerDescription getDescriptionByName(String name) {
        for (GeneralServerDescription desc : GeneralServerDescription.values()) {
            if (desc.getName().equals(name)) {
                return desc;
            }
        }
        return null;
    }

}
