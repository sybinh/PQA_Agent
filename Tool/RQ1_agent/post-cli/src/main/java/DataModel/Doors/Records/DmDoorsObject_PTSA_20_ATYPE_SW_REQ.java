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

/**
 *
 * @author GUG2WI
 */
public class DmDoorsObject_PTSA_20_ATYPE_SW_REQ extends DmDoorsObject_PTSA_20_ATYPE_REQ {

    final public DmConstantField_Text VAR_FUNC_SW;

    protected DmDoorsObject_PTSA_20_ATYPE_SW_REQ(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);

        VAR_FUNC_SW = extractUserDefinedField("VAR_FUNC_SW");
    }

}
