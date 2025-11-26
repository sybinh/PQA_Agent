/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import Monitoring.SwitchableRuleI;
import Monitoring.Warning;
import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public class Warning_Derivatives extends Warning {

    public Warning_Derivatives(SwitchableRuleI rule, EcvDate plannedDate, EcvDate plannedDateMilestone) {
        super(rule, "Planned date mismatch.", "Planned date >" + plannedDate.toString() + "< unequal to planned date in Milestones >" + plannedDateMilestone.toString() + "<");
    }

    public Warning_Derivatives(SwitchableRuleI rule, EcvDate plannedDate, String derivative, EcvDate plannedDateMilestone) {
        super(rule, "Planned date mismatch.", "Planned date >" + plannedDate.toString() + "< before planned date for derivative " + derivative + ": >" + plannedDateMilestone.toString() + "<");
    }
}
