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
import java.util.Collection;

/**
 *
 * @author GUG2WI
 */
public class DmDoorsObject_PTSA_20_ATYPE extends DmDoorsObject_PTSA_20 {

    final public DmConstantField_Text ARTEFACT_TYPE;
    final public DmConstantField_Text CRQ;
    final public DmConstantField_Text REVIEW_COMMENT;
    final public DmConstantField_Text STATUS;
    final public DmConstantField_Text UNIQUE_DOORS_ID;
    final public DmConstantField_Text VAR_FUNC_SYS;

    protected DmDoorsObject_PTSA_20_ATYPE(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);

        ARTEFACT_TYPE = extractUserDefinedField("Artefact Type");
        CRQ = extractFieldForCRQ();
        REVIEW_COMMENT = extractFieldForReviewComment();
        STATUS = extractFieldForState();
        UNIQUE_DOORS_ID = extractUserDefinedField("UNIQUE_DOORS_ID");
        VAR_FUNC_SYS = extractUserDefinedField("VAR_FUNC_SYS");
    }

    @Override
    public Collection<String> getIdOfReferencedRq1Elements() {
        return (DmDoorsObject_PTSA_1x_Requirement.extractIdOfReferencedRq1Elements(CRQ.getValue()));
    }

    @Override
    public String getDoorsObjectIdentifier() {
        return (UNIQUE_DOORS_ID.getValueAsText());
    }

    @Override
    public String getDoorsEditState() {
        return (STATUS.getValueAsText());
    }

}
