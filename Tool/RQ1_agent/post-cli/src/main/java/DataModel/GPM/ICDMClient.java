/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import RestClient.Exceptions.RestException;
import RestClient.iCDM.RestClient_iCDM;
import RestClient.iCDM.RestClient_iCDM_Milestone;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.EcvUserMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import util.EcvApplication;

/**
 *
 * @author GUG2WI
 */
public class ICDMClient {

    private static final Logger LOGGER = Logger.getLogger(ICDMClient.class.getCanonicalName());

    static private boolean testIcdmNotReachable = false;

    public static void setTestIcdmNotReachable(boolean test) {
        testIcdmNotReachable = test;
    }

    public static List<RestClient_iCDM_Milestone> getMilestones(Collection<String> pidcVerIds) {
        assert (pidcVerIds != null);

        if (testIcdmNotReachable == true) {
            return (null);
        }

        List<RestClient_iCDM_Milestone> result = new ArrayList<>();
        Set<String> notFoundPidcVerIds = new TreeSet<>();
        try {

            for (String pidcVerId : pidcVerIds) {
                try {
                    result.addAll(RestClient_iCDM.client.getMilestones(pidcVerId));
                } catch (RestClient.Exceptions.NotFoundException notFound) {
                    LOGGER.log(Level.FINER, "PIDC Version: " + pidcVerId, notFound);
                    notFoundPidcVerIds.add(pidcVerId);
                }
            }

        } catch (RestException ex) {
            StringBuilder b = new StringBuilder();
            for (String pidcVerId : pidcVerIds) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(pidcVerId);
            }
            Logger.getLogger(ICDMClient.class.getName()).log(Level.SEVERE, "Loading milestones from iCDM failed for " + b.toString() + ".", ex);
            ToolUsageLogger.logError(ICDMClient.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (null);
        }

        if (notFoundPidcVerIds.isEmpty() == false) {
            String message;
            RestException ex;
            if (notFoundPidcVerIds.size() == 1) {
                message = "No milestones received for Project ID Card Version " + notFoundPidcVerIds.iterator().next() + ".";
            } else {
                message = "No milestones received for Project ID Card Versions "
                        + notFoundPidcVerIds.stream().collect(Collectors.joining(", "));
            }
            LOGGER.log(Level.WARNING, message);
            EcvUserMessage.showMessageDialog(message, "No milestones received from iCDM", EcvUserMessage.MessageType.WARNING_MESSAGE);
        }

        return (result);

    }

}
