/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_Any;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_Any extends DmRq1WorkItem {

    public DmRq1WorkItem_Any() {
        this(new Rq1WorkItem_Any());
    }

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_Any(Rq1WorkItem_Any rq1WorkItem) {
        super("Workitem", rq1WorkItem);
    }

    @Override
    public DmRq1WorkItem cloneWorkItem() {
        return null;
    }

}
