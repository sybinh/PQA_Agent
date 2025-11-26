/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.DmToDsField_Number;
import DataStore.ALM.DsAlmField_Number;
import DataStore.ALM.DsAlmRecordI;

/**
 *
 * @author GUG2WI
 */
public class DmAlmField_Number extends DmToDsField_Number<DsAlmRecordI> {

    public DmAlmField_Number(DsAlmField_Number dsTextField, String nameForUserInterface) {
        super(dsTextField, nameForUserInterface);
    }

}
