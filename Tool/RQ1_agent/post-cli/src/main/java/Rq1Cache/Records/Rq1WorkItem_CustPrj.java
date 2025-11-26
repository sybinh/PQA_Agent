/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.Domain_WorkItem;
import Rq1Data.Enumerations.SubCategory_WorkItem;

/**
 * Class to support the creation of the object when loading data from RQ1.
 *
 * @author GUG2WI
 */
public class Rq1WorkItem_CustPrj extends Rq1WorkItem_Project {

    public Rq1WorkItem_CustPrj() {
        super(Rq1NodeDescription.WORKITEM_CUST_PRJ, Domain_WorkItem.SOFTWARE, SubCategory_WorkItem.CUST_PRJ);
    }

}
