/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1HwCustomerProject_Pool;

/**
 *
 * @author GUG2WI
 */
public class DmRq1HwCustomerProject_Pool extends DmRq1HwCustomerProject {

    final public DmRq1Field_ReferenceList<DmRq1Project> POOL_PROJECT_MEMBERS;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1HwCustomerProject_Pool(Rq1HwCustomerProject_Pool rq1Project) {
        super("Hardware Pool Project", rq1Project);

        addField(POOL_PROJECT_MEMBERS = new DmRq1Field_ReferenceList<>(this, rq1Project.HAS_POOL_PROJECT_MEMBERS, "Pool Projects"));
    }

}
