/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Fields;

import DataModel.DmToDsField_Text;
import Doors.DoorsRecord;
import Doors.Fields.DoorsField_Text;
import Doors.Identifier.DoorsRecordIdentifier;

/**
 *
 * @author gug2wi
 */
public class DmDoorsField_Text extends DmToDsField_Text<DoorsRecord<? extends DoorsRecordIdentifier>> {

    public DmDoorsField_Text(DoorsField_Text doorsTextField, String nameForUserInterface) {
        super(doorsTextField, nameForUserInterface);
    }

  

}
