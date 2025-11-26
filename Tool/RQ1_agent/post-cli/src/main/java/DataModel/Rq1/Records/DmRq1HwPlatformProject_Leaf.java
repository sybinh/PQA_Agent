/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1HwPlatformProject_Leaf;

/**
 *
 * @author GUG2WI
 */
public class DmRq1HwPlatformProject_Leaf extends DmRq1HwCustomerProject implements DmRq1LeafProjectI {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1HwPlatformProject_Leaf(Rq1HwPlatformProject_Leaf rq1Project) {
        super("Hardware Platform Project", rq1Project);
    }

}
