/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Monitoring;

import Monitoring.Rule;
import Rq1Data.Monitoring.Rq1RuleDescription;

/**
 * Base class for all rule for the RQ1 OSLC interface. These rules provide
 * markers when the read/write access to the database fails.
 *
 * @author gug2wi
 */
public class Rq1DataRule extends Rule {

    public Rq1DataRule(Rq1RuleDescription description) {
        super(description);
    }

}
