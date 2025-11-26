/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import DataModel.Doors.Records.DmDoorsElement.ElementType;
import Doors.DoorsObject;

/**
 *
 * @author GUG2WI
 */
public class DmDoorsObject_PTSA_20_ATYPE_INFO extends DmDoorsObject_PTSA_20_ATYPE {

    final public DmConstantField_Text DESCRIPTION;
    final public DmConstantField_Text VAR_FUNC_SW;

    public DmDoorsObject_PTSA_20_ATYPE_INFO(DoorsObject doorsObject) {
        super(ElementType.PTSA_20_ATYPE_INFO, doorsObject);

        DESCRIPTION = extractUserDefinedField("Description (alternative language)");
        VAR_FUNC_SW = extractUserDefinedField("VAR_FUNC_SW");

        finishExtractionOfUserDefinedFields();
    }

}
