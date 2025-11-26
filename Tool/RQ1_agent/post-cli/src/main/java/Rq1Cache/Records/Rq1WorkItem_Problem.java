/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.Category_WorkItem;
import Rq1Data.Enumerations.Domain_WorkItem;
import Rq1Data.Enumerations.SubCategory_WorkItem;

/**
 *
 * @author GUG2WI
 */
public class Rq1WorkItem_Problem extends Rq1WorkItem {

    public Rq1WorkItem_Problem() {
        super(Rq1NodeDescription.WORKITEM_PROBLEM, Domain_WorkItem.SOFTWARE, Category_WorkItem.PROBLEM, SubCategory_WorkItem.SOFTWARE);
    }

}
