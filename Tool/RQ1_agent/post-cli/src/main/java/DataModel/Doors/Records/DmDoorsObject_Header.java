/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import Doors.DoorsObject;

public class DmDoorsObject_Header extends DmDoorsObject_Requirement {

    final public DmConstantField_Text AFFECTED_COMPONENT;
    final public DmConstantField_Text ALLOCATION;
    final public DmConstantField_Text EFFECT_CHAIN;
    final public DmConstantField_Text TAGS;
    final public DmConstantField_Text UNIQUE_DOORS_ID;

    public DmDoorsObject_Header(DoorsObject doorsObject) {
        super(ElementType.HEADER, doorsObject);
        assert (doorsObject != null);

        AFFECTED_COMPONENT = extractUserDefinedField("Affected Component");
        ALLOCATION = extractUserDefinedField("Allocation");
        EFFECT_CHAIN = extractUserDefinedField("Effect Chain");
        TAGS = extractUserDefinedField("Tags");
        UNIQUE_DOORS_ID = extractUserDefinedField("Unique DOORS ID");

        finishUserDefinedFields();
    }

    @Override
    public String getDoorsAllocation() {
        return (ALLOCATION.getValueAsText());
    }

}
