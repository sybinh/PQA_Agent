/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import Doors.DoorsObject;

public class DmDoorsObject_PTSA_1x_L1_NON_FUNC_REQ extends DmDoorsObject_PTSA_1x_L1_REQ {

    final public DmConstantField_Text NF_CATEGORY;

    public DmDoorsObject_PTSA_1x_L1_NON_FUNC_REQ(DoorsObject doorsObject) {
        super(ElementType.NON_FUNC_REQ, doorsObject);
        assert (doorsObject != null);

        NF_CATEGORY = extractUserDefinedField("NF_Category");

        finishExtractionOfUserDefinedFields();
    }

}
