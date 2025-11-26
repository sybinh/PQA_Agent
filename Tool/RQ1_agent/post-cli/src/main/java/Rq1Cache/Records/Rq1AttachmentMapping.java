/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.Enumerations.ExportAttribute;
import Rq1Data.Enumerations.ExportState;

/**
 *
 * @author gug2wi
 */
public class Rq1AttachmentMapping extends Rq1Record implements Rq1NodeInterface {

    final static public Rq1AttributeName ATTRIBUTE_EXPORT_ATTRIBUTE = new Rq1AttributeName("Attribute");
    final static public Rq1AttributeName ATTRIBUTE_ENG_ATTACHMENT = new Rq1AttributeName("Eng_Attachment");
    final static public Rq1AttributeName ATTRIBUTE_EXPORT_STATE = new Rq1AttributeName("ExportState");
    final static public Rq1AttributeName ATTRIBUTE_HAS_ISSUE = new Rq1AttributeName("hasIssue");
    final static public Rq1AttributeName ATTRIBUTE_HAS_IRM = new Rq1AttributeName("hasIssuereleasemap");
    final static public Rq1AttributeName ATTRIBUTE_LINKED_ID = new Rq1AttributeName("linkedId");
    final static public Rq1AttributeName ATTRIBUTE_LINKED_RECORD_TYPE = new Rq1AttributeName("linkedRecordType");
    final static public Rq1AttributeName ATTRIBUTE_SALES_ATTACHMENT = new Rq1AttributeName("Sales_Attachment");

    final public Rq1DatabaseField_Enumeration EXPORT_ATTRIBUTE;
    final public Rq1DatabaseField_Text ENG_ATTACHMENT;
    final public Rq1DatabaseField_Enumeration EXPORT_STATE;
    final public Rq1DatabaseField_Reference HAS_ISSUE;
    final public Rq1DatabaseField_Reference HAS_IRM;
    final public Rq1DatabaseField_Text LINKED_ID;
    final public Rq1DatabaseField_Text LINKED_RECORD_TYPE;
    final public Rq1DatabaseField_Text SALES_ATTACHMENT;

    public Rq1AttachmentMapping() {
        super(Rq1NodeDescription.ATTACHMENT_MAPPING);

        addField(EXPORT_ATTRIBUTE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_EXPORT_ATTRIBUTE, ExportAttribute.values()));
        EXPORT_ATTRIBUTE.acceptInvalidValuesInDatabase();
        addField(ENG_ATTACHMENT = new Rq1DatabaseField_Text(this, ATTRIBUTE_ENG_ATTACHMENT));
        addField(EXPORT_STATE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_EXPORT_STATE, ExportState.values()));
        EXPORT_STATE.acceptInvalidValuesInDatabase();
        addField(HAS_ISSUE = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_ISSUE, Rq1RecordType.ISSUE));
        addField(HAS_IRM = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_IRM, Rq1RecordType.IRM));
        addField(LINKED_ID = new Rq1DatabaseField_Text(this, ATTRIBUTE_LINKED_ID));
        addField(LINKED_RECORD_TYPE = new Rq1DatabaseField_Text(this, ATTRIBUTE_LINKED_RECORD_TYPE));
        addField(SALES_ATTACHMENT = new Rq1DatabaseField_Text(this, ATTRIBUTE_SALES_ATTACHMENT));
    }

    public String getTitle() {
        String engAttachment = ENG_ATTACHMENT.getDataModelValue();
        if (engAttachment != null) {
            return (engAttachment);
        }
        String salesAttachment = SALES_ATTACHMENT.getDataModelValue();
        if (salesAttachment != null) {
            return (salesAttachment);
        }
        return ("");
    }

    @Override
    public void reload() {
        // ignored
    }

}
