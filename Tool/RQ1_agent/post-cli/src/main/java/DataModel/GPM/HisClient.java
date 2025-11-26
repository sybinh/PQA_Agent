/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import RestClient.Exceptions.RestException;
import RestClient.HIS.RestClient_HIS;
import RestClient.HIS.RestClient_HisMilestone;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvApplication;

/**
 *
 * @author GUG2WI
 */
public class HisClient {

    static private boolean testHisNotReachable = false;

    public static void setTestHisNotReachable(boolean test) {
        testHisNotReachable = test;
    }

    public static List<RestClient_HisMilestone> getMilestones(Collection<String> ecuHardwareIds) {
        assert (ecuHardwareIds != null);

        if (testHisNotReachable == true) {
            return (null);
        }

        try {
            return (RestClient_HIS.client.getMilestones(ecuHardwareIds));
        } catch (RestException ex) {
            StringBuilder b = new StringBuilder();
            for (String ecuHwId : ecuHardwareIds) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(ecuHwId);
            }
            Logger.getLogger(HisClient.class.getName()).log(Level.SEVERE, "Loading milestones from HIS failed for " + b.toString() + ".", ex);
            ToolUsageLogger.logError(HisClient.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (null);
        }
    }

}
