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

public class DmDoorsObject_PTSA_1x_FunctionalRequirement extends DmDoorsObject_PTSA_1x_Requirement {

    final public DmConstantField_Text AFFECTED_COMPONENT;
    final public DmConstantField_Text ALLOCATION;
    final public DmConstantField_Text EFFECT_CHAIN;
    final public DmConstantField_Text TAGS;
    final public DmConstantField_Text UNIQUE_DOORS_ID;

    public DmDoorsObject_PTSA_1x_FunctionalRequirement(DoorsObject doorsObject) {
        super(ElementType.FUNC_REQ, doorsObject);
        assert (doorsObject != null);

        AFFECTED_COMPONENT = extractFieldForAffectedComponent();
        ALLOCATION = extractFieldForAllocation();
        EFFECT_CHAIN = extractUserDefinedField("Effect Chain");
        TAGS = extractUserDefinedField("Tags");

        DmConstantField_Text tmpUniqueDoorsId = extractOptionalUserDefinedField("Unique DOORS ID");
        if (tmpUniqueDoorsId == null) {
            tmpUniqueDoorsId = extractOptionalUserDefinedField("UNIQUE_DOORS_ID", "UNIQUE DOORS ID");
            if (tmpUniqueDoorsId == null) {
                tmpUniqueDoorsId = createMissingField("Unique DOORS ID", "Unique DOORS ID", "UNIQUE_DOORS_ID");
            }
        }
        UNIQUE_DOORS_ID = tmpUniqueDoorsId;

        finishExtractionOfUserDefinedFields();
    }

    @Override
    public String getDoorsAllocation() {
        return (ALLOCATION.getValueAsText());
    }

}
