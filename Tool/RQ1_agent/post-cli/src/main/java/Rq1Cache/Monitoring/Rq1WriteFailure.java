/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Monitoring;

import Monitoring.RuleI;
import Monitoring.WriteToDatabaseFailure;

/**
 * Created if a RQ1 record could not be written because if errors.
 *
 * @author GUG2WI
 */
public class Rq1WriteFailure extends WriteToDatabaseFailure {

    public Rq1WriteFailure(RuleI rule, String title, String description) {
        super(rule, title, description);
    }

}
