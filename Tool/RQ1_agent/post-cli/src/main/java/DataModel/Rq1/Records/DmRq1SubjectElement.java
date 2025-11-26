/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1Subject;
import UiSupport.UiTreeViewRootElementI;

/**
 *
 * @author GUG2WI
 */
abstract public class DmRq1SubjectElement extends DmRq1BaseElement implements DmRq1SubjectInterface, UiTreeViewRootElementI {

    final String subjectType;

    final public DmRq1Field_Text TITLE;
    final public DmRq1Field_ReferenceList<DmRq1Attachment> ATTACHMENTS;

    DmRq1SubjectElement(String subjectType, Rq1Subject rq1Subject) {
        super(subjectType, rq1Subject);
        assert (subjectType != null);
        assert (subjectType.isEmpty() == false);
        this.subjectType = subjectType;

        addField(TITLE = new DmRq1Field_Text(this, rq1Subject.TITLE, "Title"));
        TITLE.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);
        TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(ATTACHMENTS = new DmRq1Field_ReferenceList<>(this, rq1Subject.ATTACHMENTS, "Attachments (double click to open - drop file for upload)"));
        
        /* Add field for User Assistant */
        ATTACHMENTS.setAttribute(DmFieldI.Attribute.FIELD_ATTACHMENT_FOR_CONFIG_RULES);
    }

    @Override
    final public String getTitle() {
        return (TITLE.getValue());
    }

    @Override
    final public String getRq1Id() {
        return (((Rq1Subject) getRq1Record()).getRq1Id());
    }

    @Override
    final public String toString() {
        return (subjectType + ": " + getRq1Id() + " - " + getTitle());
    }

}
