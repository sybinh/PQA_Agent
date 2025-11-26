/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import Monitoring.Warning;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmGpmWarning_DateOfExternalMilestoneChanged extends Warning implements DmGpmWarningI, DmGpmWarning_ExternalMilestonesNotConfirmedI {

    static final String warningTitle = "Date of milestone changed till last confirmation.";

    public DmGpmWarning_DateOfExternalMilestoneChanged(DmGpmRule_CheckDateOfExternalMilestone rule, EcvDate lastConfirmedDate, EcvDate currentDate) {
        super(rule, createTitle(lastConfirmedDate));
        assert (currentDate != null);

        StringBuilder b = new StringBuilder();
        b.append("Last confirmed date: ");
        if (lastConfirmedDate != null) {
            b.append(lastConfirmedDate.getUiDate());
        }
        b.append("\n");
        b.append("New date: ");
        if (currentDate != null) {
            b.append(currentDate.getUiDate());
        }
        setDescription(b.toString());
    }

    private static String createTitle(EcvDate lastConfirmedDate) {
        assert (lastConfirmedDate != null);
        if (lastConfirmedDate.isEmpty() == false) {
            return (warningTitle + " Last confirmed date: " + lastConfirmedDate.getUiDate());
        } else {
            return (warningTitle + " Last confirmed date is empty.");
        }

    }

}
