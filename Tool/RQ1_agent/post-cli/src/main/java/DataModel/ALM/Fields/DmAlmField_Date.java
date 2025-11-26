/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.DmToDsField_Date;
import DataStore.ALM.DsAlmRecordI;
import DataStore.DsFieldI_Date;

/**
 *
 * @author GUG2WI
 */
public class DmAlmField_Date extends DmToDsField_Date<DsAlmRecordI> {

    public DmAlmField_Date(DsFieldI_Date<DsAlmRecordI> dsDateField, String nameForUserInterface) {
        super(dsDateField, nameForUserInterface);
    }

}
