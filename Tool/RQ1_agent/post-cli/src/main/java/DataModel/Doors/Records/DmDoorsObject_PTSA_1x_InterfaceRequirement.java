/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import Doors.DoorsObject;

public class DmDoorsObject_PTSA_1x_InterfaceRequirement extends DmDoorsObject_PTSA_1x_Requirement {

    final public DmConstantField_Text AFFECTED_COMPONENT;
    final public DmConstantField_Text AFFECTED_SW_FUNCTION;
    final public DmConstantField_Text ALLOCATION;
//    final public DmConstantField_Text DIRECTION;
    final public DmConstantField_Text TAGS;

    public DmDoorsObject_PTSA_1x_InterfaceRequirement(DoorsObject doorsObject) {
        super(ElementType.INTERFACE_REQ, doorsObject);
        assert (doorsObject != null);

        AFFECTED_COMPONENT = extractFieldForAffectedComponent();
        AFFECTED_SW_FUNCTION = extractUserDefinedField("Affected-SW-Function", "Affected SW Function");
        ALLOCATION = extractFieldForAllocation();

        TAGS = extractUserDefinedField("Tags");

        finishExtractionOfUserDefinedFields();
    }

    @Override
    public String getDoorsAllocation() {
        return (ALLOCATION.getValueAsText());
    }

}
