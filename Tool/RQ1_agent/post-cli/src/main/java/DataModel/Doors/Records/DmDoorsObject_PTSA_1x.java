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
public class DmDoorsObject_PTSA_1x extends DmDoorsObject {

    final public DmConstantField_Text ASIL;
    final public DmConstantField_Text COMMENT;

    protected DmDoorsObject_PTSA_1x(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);

        ASIL = extractFieldForSafetyClassification();

        DmConstantField_Text tmpComment = extractOptionalUserDefinedField("Comment");
        if (tmpComment == null) {
            tmpComment = extractOptionalUserDefinedField("Internal Comment");
            if (tmpComment == null) {
                tmpComment = createMissingField("Comment", "Comment", "Internal Comment");
            }
        }
        COMMENT = tmpComment;
    }

}
