/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Rq1Cache.Records.Rq1HardwareRelease;

/**
 *
 * @author gug2wi
 */
public class DmRq1HardwareRelease extends DmRq1Release {

    public DmRq1HardwareRelease(String subjectType, Rq1HardwareRelease release) {
        super(subjectType, release);
    }

}
