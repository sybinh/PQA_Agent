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

public class DmDoorsObject_PTSA_1x_DesignDecision extends DmDoorsObject_PTSA_1x_Requirement {

    final public DmConstantField_Text AFFECTED_COMPONENT;
    final public DmConstantField_Text ALLOCATION;
    final public DmConstantField_Text EFFECT_CHAIN;
    final public DmConstantField_Text TAGS;
    final public DmConstantField_Text UNIQUE_DOORS_ID;

    public DmDoorsObject_PTSA_1x_DesignDecision(DoorsObject doorsObject) {
        super(ElementType.DESIGN_DECISION, doorsObject);
        assert (doorsObject != null);

        AFFECTED_COMPONENT = extractFieldForAffectedComponent();
        ALLOCATION = extractFieldForAllocation();
        EFFECT_CHAIN = extractUserDefinedField("Effect Chain");
        TAGS = extractUserDefinedField("Tags");
        UNIQUE_DOORS_ID = extractUserDefinedField("Unique DOORS ID");

        finishExtractionOfUserDefinedFields();
    }

    @Override
    public String getDoorsAllocation() {
        return (ALLOCATION.getValueAsText());
    }

}
