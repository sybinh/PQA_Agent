/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.Doors.Records.DmDoorsElement.ElementType;
import Doors.DoorsObject;

/**
 *
 * @author GUG2WI
 */
public class DmDoorsObject_PTSA_20_STKH_FUNC_REQ extends DmDoorsObject_PTSA_20_STKH_REQ {

    public DmDoorsObject_PTSA_20_STKH_FUNC_REQ(DoorsObject doorsObject) {
        super(ElementType.PTSA_20_STKH_FUNC_REQ, doorsObject);

        finishExtractionOfUserDefinedFields();
    }

}
