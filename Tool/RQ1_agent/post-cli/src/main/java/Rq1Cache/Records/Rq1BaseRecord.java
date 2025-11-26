/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsFieldI;
import DataStore.Exceptions.DsFieldContentFailure_InvalidValue;
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import static Rq1Cache.Records.Rq1Record.successfullWriteRule;
import Rq1Cache.Rq1RecordDescription;
import Rq1Cache.Rq1RecordType;

/**
 * Defines the fields that are available on most record types in RQ1.
 *
 * @author gug2wi
 */
public abstract class Rq1BaseRecord extends Rq1Record {

    final static public Rq1AttributeName ATTRIBUTE_LAST_MODIFIED_DATE = new Rq1AttributeName("hasBackground", "LastModifiedDate");
    final public static Rq1AttributeName ATTRIBUTE_LIFE_CYCLE_STATE = new Rq1AttributeName("LifeCycleState");
    final public static Rq1AttributeName ATTRIBUTE_DESCRIPTION = new Rq1AttributeName("Description");
    final static public Rq1AttributeName ATTRIBUTE_DOMAIN = new Rq1AttributeName("Domain");
    final public static Rq1AttributeName ATTRIBUTE_INTERNAL_COMMENT = new Rq1AttributeName("InternalComment");
    final public static Rq1AttributeName ATTRIBUTE_SUBMITTER = new Rq1AttributeName("Submitter");

    final public Rq1DatabaseField_Text ACCOUNT_NUMBERS;
    final public Rq1DatabaseField_Text DESCRIPTION;
    final public Rq1DatabaseField_Text INTERNAL_COMMENT;
    final public Rq1DatabaseField_Text LIFE_CYCLE_STATE_COMMENT;
    final public Rq1DatabaseField_Date SUBMIT_DATE;
    final public Rq1DatabaseField_Text SUBMITTER;
    final public Rq1DatabaseField_Xml TAGS;
    final public Rq1DatabaseField_Date LAST_MODIFIED_DATE;
    final public Rq1DatabaseField_ReferenceList HAS_HISTORY_LOGS;
    final public Rq1DatabaseField_ReferenceList HAS_EXTERNAL_LINKS;

    public Rq1BaseRecord(Rq1RecordDescription recordDescription) {
        this(recordDescription, null);
    }

    public Rq1BaseRecord(Rq1RecordDescription recordDescription, Rq1AttributeName attributeNameForDescription) {
        super(recordDescription);

        if (attributeNameForDescription == null) {
            attributeNameForDescription = ATTRIBUTE_DESCRIPTION;
        }

        addField(ACCOUNT_NUMBERS = new Rq1DatabaseField_Text(this, "AccountNumbers"));
        addField(DESCRIPTION = new Rq1DatabaseField_Text(this, attributeNameForDescription));
        addField(INTERNAL_COMMENT = new Rq1DatabaseField_Text(this, ATTRIBUTE_INTERNAL_COMMENT));
        addField(LIFE_CYCLE_STATE_COMMENT = new Rq1DatabaseField_Text(this, "LifeCycleStateComment"));
        addField(SUBMIT_DATE = new Rq1DatabaseField_Date(this, "SubmitDate"));
        addField(SUBMITTER = new Rq1DatabaseField_Text(this, ATTRIBUTE_SUBMITTER));
        addField(TAGS = new Rq1DatabaseField_Xml(this, "Tags"));
        addField(HAS_HISTORY_LOGS = new Rq1DatabaseField_ReferenceList(this, "hasHistoryLogs", Rq1RecordType.HISTORY_LOG));
        addField(HAS_EXTERNAL_LINKS = new Rq1DatabaseField_ReferenceList(this, "hasExternalLinks", Rq1RecordType.EXTERNAL_LINK));
        addField(LAST_MODIFIED_DATE = new Rq1DatabaseField_Date(this, ATTRIBUTE_LAST_MODIFIED_DATE));
        LAST_MODIFIED_DATE.setOptional(); // There are records without last modified date !!!

        SUBMIT_DATE.setReadOnly();
        SUBMITTER.setReadOnly();
        LAST_MODIFIED_DATE.setReadOnly();
    }

    @Override
    protected void handleWriteValidationError(String validationError) {
        assert (validationError != null);

        int tagStart = validationError.indexOf("<");
        if (tagStart >= 0) {
            int tagEnd = validationError.indexOf(">", tagStart);
            if (tagEnd >= 0) {
                String tagName = validationError.substring(tagStart + 1, tagEnd);
                if (tagName.isEmpty() == false) {
                    handleWriteValidationErrorForTag(tagName, validationError.trim());
                }
            }
        }
    }

    private void handleWriteValidationErrorForTag(String tagName, String error) {

        DsFieldI field = TAGS.getSubField(tagName);

        if (field != null) {
            field.addMarker(new DsFieldContentFailure_InvalidValue(successfullWriteRule, tagName, error));
        }
    }

}
