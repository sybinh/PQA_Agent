/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.DmToDsField_Text;
import DataStore.ALM.DsAlmRecordI;
import DataStore.DsFieldI_Text;

/**
 *
 * @author GUG2WI
 */
public class DmAlmField_Text extends DmToDsField_Text<DsAlmRecordI> {

    public DmAlmField_Text(DsFieldI_Text<DsAlmRecordI> dsTextField, String nameForUserInterface) {
        super(dsTextField, nameForUserInterface);
    }

}
