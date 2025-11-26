/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Rq1Cache.Records.Rq1WorkItem;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1WorkItem_Hardware extends DmRq1WorkItem {

    public DmRq1WorkItem_Hardware(String subjectType, Rq1WorkItem rq1WorkItem) {
        super(subjectType, rq1WorkItem);
    }

}
