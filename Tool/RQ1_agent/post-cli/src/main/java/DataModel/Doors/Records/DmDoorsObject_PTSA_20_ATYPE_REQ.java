/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
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
public class DmDoorsObject_PTSA_20_ATYPE_REQ extends DmDoorsObject_PTSA_20_ATYPE {

    final public DmConstantField_Text INTERNAL_COMMENT;
    final public DmConstantField_Text SAFETY_CLASSIFICATION;
    final public DmConstantField_Text STAKEHOLDER_REVIEW_COMMENT;
    final public DmConstantField_Text TAGS;
    final public DmConstantField_Text VERIFICATION_CRITERIA;

    protected DmDoorsObject_PTSA_20_ATYPE_REQ(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);

        INTERNAL_COMMENT = extractUserDefinedField("Internal Comment");
        SAFETY_CLASSIFICATION = extractFieldForSafetyClassification();
        STAKEHOLDER_REVIEW_COMMENT = extractUserDefinedField("Stakeholder Review Comment");
        TAGS = extractUserDefinedField("Tags");
        VERIFICATION_CRITERIA = extractFieldForVerificationCriteria();
    }

}
