/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
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
public class Rq1WorkItem_Project extends Rq1WorkItem {

    final private String subjectType;

    public Rq1WorkItem_Project(Rq1NodeDescription nodeDescription, Domain_WorkItem domain, SubCategory_WorkItem subCategory) {
        super(nodeDescription, domain, Category_WorkItem.PROJECT, subCategory);
        this.subjectType = "Workitem-PRJ";
    }

    public String getSubjectType() {
        return subjectType;
    }

}
