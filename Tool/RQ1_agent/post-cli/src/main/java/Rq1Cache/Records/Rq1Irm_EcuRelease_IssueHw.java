/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Rq1LinkDescription;

/**
 *
 * @author gug2wi
 */
public class Rq1Irm_EcuRelease_IssueHw extends Rq1HardwareIrm {

    public Rq1Irm_EcuRelease_IssueHw() {
        super(Rq1LinkDescription.IRM_HWECU_I_HW_ECU);
    }
}
