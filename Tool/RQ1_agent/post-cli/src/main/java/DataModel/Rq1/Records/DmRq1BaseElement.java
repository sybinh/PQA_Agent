/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Fields.DmRq1Field_Xml;
import DataModel.Rq1.Monitoring.Rule_Info_InternalComment;
import DataModel.Rq1.Monitoring.Rule_ToDo_InternalComment;
import Rq1Cache.Records.Rq1BaseRecord;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1BaseElement extends DmRq1Element {

    final static public String TAG_TOOL_TIP_TEXT
            = "<html>IPE extracts all known tags from the Tags field and shows them as individual fields.<br>"
            + "Only tags not known by IPE are shown here.<br>"
            + "More details about handling tags in IPE at: Menu -&gt; Help -&gt; How To -&gt; IPE Tag Field Handling</html>";

    final public DmRq1Field_Text ACCOUNT_NUMBERS;

    final public DmRq1Field_Text DESCRIPTION;
    final public DmRq1Field_Text INTERNAL_COMMENT;
    final public DmRq1Field_Text LIFE_CYCLE_STATE_COMMENT;
    final public DmRq1Field_Date SUBMIT_DATE;
    final public DmRq1Field_Text SUBMITTER;
    final public DmRq1Field_Xml TAGS;
    final public DmRq1Field_Date LAST_MODIFIED_DATE;
    final public DmRq1Field_ReferenceList<DmRq1HistoryLog> HAS_HISTORY_LOGS;
    final public DmRq1Field_ReferenceList<DmRq1ExternalLink> HAS_EXTERNAL_LINKS;

    public DmRq1BaseElement(String elementType, Rq1BaseRecord rq1BaseRecord) {
        super(elementType, rq1BaseRecord);

        //
        // Create and add fields
        //
        addField(ACCOUNT_NUMBERS = new DmRq1Field_Text(this, rq1BaseRecord.ACCOUNT_NUMBERS, "Account Numbers"));
        ACCOUNT_NUMBERS.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        ACCOUNT_NUMBERS.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ACCOUNT_NUMBERS.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        ACCOUNT_NUMBERS.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(DESCRIPTION = new DmRq1Field_Text(this, rq1BaseRecord.DESCRIPTION, "Description"));
        DESCRIPTION.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        DESCRIPTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        DESCRIPTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);
        DESCRIPTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);

        addField(INTERNAL_COMMENT = new DmRq1Field_Text(this, rq1BaseRecord.INTERNAL_COMMENT, "Internal Comment"));
        INTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        INTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        INTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);
        INTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);

        addField(LIFE_CYCLE_STATE_COMMENT = new DmRq1Field_Text(this, rq1BaseRecord.LIFE_CYCLE_STATE_COMMENT, "Life Cycle State Comment"));
        LIFE_CYCLE_STATE_COMMENT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        LIFE_CYCLE_STATE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LIFE_CYCLE_STATE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        LIFE_CYCLE_STATE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(SUBMIT_DATE = new DmRq1Field_Date(this, rq1BaseRecord.SUBMIT_DATE, "Submit Date"));
        SUBMIT_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SUBMITTER = new DmRq1Field_Text(this, rq1BaseRecord.SUBMITTER, "Submitter"));
        SUBMITTER.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        SUBMITTER.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(TAGS = new DmRq1Field_Xml(this, rq1BaseRecord.TAGS, "Unknown Tags"));
        addField(LAST_MODIFIED_DATE = new DmRq1Field_Date(this, rq1BaseRecord.LAST_MODIFIED_DATE, "Last Modified Date"));
        addField(HAS_HISTORY_LOGS = new DmRq1Field_ReferenceList<>(this, rq1BaseRecord.HAS_HISTORY_LOGS, "History Logs"));
        addField(HAS_EXTERNAL_LINKS = new DmRq1Field_ReferenceList<>(this, rq1BaseRecord.HAS_EXTERNAL_LINKS, "External Links"));

        //
        // Create and add rules
        //
        addRule(new Rule_ToDo_InternalComment(this));
        addRule(new Rule_Info_InternalComment(this));
    }

    @Override
    public boolean isEditable() {
        return (true);
    }

    final public String getDescription() {
        return (DESCRIPTION.getValue());
    }
}
