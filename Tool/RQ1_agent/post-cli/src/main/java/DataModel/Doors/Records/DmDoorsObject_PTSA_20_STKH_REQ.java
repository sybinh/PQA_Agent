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

/**
 *
 * @author GUG2WI
 */
class DmDoorsObject_PTSA_20_STKH_REQ extends DmDoorsObject_PTSA_20_STKH {

    final public DmConstantField_Text AFFECTED_COMPONENT;
    final public DmConstantField_Text SAFETY_CLASSIFICATION;
    final public DmConstantField_Text ACCEPTANCE_CRITERIA;
    final public DmConstantField_Text CUSTOMER_REQ_ID;
    final public DmConstantField_Text DISCUSSION_OEM;
    final public DmConstantField_Text ELICITATION_COMMENT;
    final public DmConstantField_Text ELICITATION_FEATURES;
    final public DmConstantField_Text FOREIGN_ID;
    final public DmConstantField_Text INTERNAL_COMMENT;
    final public DmConstantField_Text OEM_CATEGORY;
    final public DmConstantField_Text OEM_RESPONSIBLE;
    final public DmConstantField_Text STAKEHOLDER_REVIEW_COMMENT;
    final public DmConstantField_Text SUPPLIER_DISCUSSION;
    final public DmConstantField_Text SUPPLIER_RESPONSIBLE;
    final public DmConstantField_Text SUPPLIER_STATUS;
    final public DmConstantField_Text TAGS;
    final public DmConstantField_Text UNIQUE_DOORS_ID;

    protected DmDoorsObject_PTSA_20_STKH_REQ(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);

        AFFECTED_COMPONENT = extractFieldForAffectedComponent();
        SAFETY_CLASSIFICATION = extractFieldForSafetyClassification();
        ACCEPTANCE_CRITERIA = extractUserDefinedField("Acceptance Criteria");
        CUSTOMER_REQ_ID = extractUserDefinedField("Customer Req.ID");
        DISCUSSION_OEM = extractUserDefinedField("Discussion OEM");
        ELICITATION_COMMENT = extractUserDefinedField("Elicitation Comment");
        ELICITATION_FEATURES = extractUserDefinedField("Elicitation Features");
        FOREIGN_ID = extractUserDefinedField("Foreign ID");
        INTERNAL_COMMENT = extractUserDefinedField("Internal Comment");
        OEM_CATEGORY = extractUserDefinedField("OEM Category");
        OEM_RESPONSIBLE = extractUserDefinedField("OEM Responsible");
        STAKEHOLDER_REVIEW_COMMENT = extractUserDefinedField("Stakeholder Review Comment");
        SUPPLIER_DISCUSSION = extractUserDefinedField("Supplier Discussion");
        SUPPLIER_RESPONSIBLE = extractUserDefinedField("Supplier responsible");
        SUPPLIER_STATUS = extractUserDefinedField("Supplier Status");
        TAGS = extractUserDefinedField("Tags");
        UNIQUE_DOORS_ID = extractUserDefinedField("UNIQUE_DOORS_ID");
    }

    @Override
    public String getDoorsObjectIdentifier() {
        return (UNIQUE_DOORS_ID.getValueAsText());
    }

}
