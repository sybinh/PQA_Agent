/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import Doors.DoorsObject;

public class DmDoorsObject_PTSA_1x_L1_INFO extends DmDoorsObject_PTSA_1x_L1_REQ {

    public DmDoorsObject_PTSA_1x_L1_INFO(DoorsObject doorsObject) {
        super(ElementType.INFO, doorsObject);
        assert (doorsObject != null);

        finishExtractionOfUserDefinedFields();
    }

}
