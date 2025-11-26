/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import Monitoring.SwitchableRuleI;
import Monitoring.Warning;

/**
 *
 * @author gug2wi
 */
public class Warning_LumpensammlerMissing extends Warning {

    public Warning_LumpensammlerMissing(SwitchableRuleI rule, DmRq1SwCustomerProject_Leaf ecuProject) {
        super(rule, "Lumpensammler missing");
        assert (ecuProject != null);
        super.setDescription("Lumpensammler missing for project " + ecuProject.getTitle() + ".");
    }
}
