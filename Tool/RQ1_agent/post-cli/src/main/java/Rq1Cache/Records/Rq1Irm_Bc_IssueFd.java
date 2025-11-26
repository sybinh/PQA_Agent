/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1LinkDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.Enumerations.ExternalNextState;

/**
 *
 * @author gug2wi
 */
public class Rq1Irm_Bc_IssueFd extends Rq1SoftwareIrm {
    
    final public Rq1DatabaseField_Text EXCHANGE_WORKFLOW;
    final public Rq1DatabaseField_Date LAST_IMPORT_DATE;
    final public Rq1DatabaseField_Date LAST_EXPORTED_DATE;
    final public Rq1DatabaseField_Text EXTERNAL_ID;
    final public Rq1DatabaseField_Text EXTERNAL_REVIEW;
    final public Rq1DatabaseField_Enumeration EXTERNAL_NEXT_STATE;
    final public Rq1DatabaseField_Text EXTERNAL_COMMENT;
    final public Rq1DatabaseField_Text EXTERNAL_CONVERSATION;
    final public Rq1DatabaseField_Text EXTERNAL_STATES;
    final public Rq1DatabaseField_Text EXTERNAL_TAGS;

    final public Rq1DatabaseField_Reference EXTERNAL_ASSIGNEE;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_NAME;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_FIRSTNAME;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_LASTNAME;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_PHONE;
    final public Rq1DatabaseField_Text SUPPLIER_ID;
    final public Rq1DatabaseField_Text SUPPLIER_STATE;
    
    final static public Rq1AttributeName ATTRIBUTE_SUPPLIER_ID = new Rq1AttributeName("hasBackground","SupplierID");
    final static public Rq1AttributeName ATTRIBUTE_SUPPLIER_STATE = new Rq1AttributeName("hasBackground","SupplierState");

    public Rq1Irm_Bc_IssueFd() {
        super(Rq1LinkDescription.IRM_BC_REL_ISSUE_FD);
        
        addField(EXCHANGE_WORKFLOW = new Rq1DatabaseField_Text(this, "ExternalExchangeWorkflow"));
        addField(LAST_IMPORT_DATE = new Rq1DatabaseField_Date(this, "ExternalLastImportedDate"));
        addField(LAST_EXPORTED_DATE = new Rq1DatabaseField_Date(this, "ExternalLastExportedDate"));
        addField(EXTERNAL_ID = new Rq1DatabaseField_Text(this, "External_ID"));
        addField(EXTERNAL_REVIEW = new Rq1DatabaseField_Text(this, "ExternalReview"));
        addField(EXTERNAL_NEXT_STATE = new Rq1DatabaseField_Enumeration(this, "ExternalNextState", ExternalNextState.values()));
        addField(EXTERNAL_COMMENT = new Rq1DatabaseField_Text(this, "ExternalComment"));
        addField(EXTERNAL_CONVERSATION = new Rq1DatabaseField_Text(this, "ExternalConversation"));
        addField(EXTERNAL_STATES = new Rq1DatabaseField_Text(this, "ExternalState"));
        addField(EXTERNAL_TAGS = new Rq1DatabaseField_Text(this, "ExternalTags"));

        addField(EXTERNAL_ASSIGNEE = new Rq1DatabaseField_Reference(this, "ExternalAssignee", Rq1RecordType.CONTACT));
        addField(EXTERNAL_ASSIGNEE_NAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.Name"));
        addField(EXTERNAL_ASSIGNEE_FIRSTNAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.FirstName"));
        addField(EXTERNAL_ASSIGNEE_LASTNAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.LastName"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new Rq1DatabaseField_Text(this, "ExternalAssignee.Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new Rq1DatabaseField_Text(this, "ExternalAssignee.Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "ExternalAssignee.eMail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new Rq1DatabaseField_Text(this, "ExternalAssignee.PhoneNumbers"));
        addField(SUPPLIER_ID = new Rq1DatabaseField_Text(this, ATTRIBUTE_SUPPLIER_ID));
        addField(SUPPLIER_STATE = new Rq1DatabaseField_Text(this, ATTRIBUTE_SUPPLIER_STATE));

        EXTERNAL_ASSIGNEE_NAME.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_FIRSTNAME.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_LASTNAME.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_ORGANIZATION.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_DEPARTMENT.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_EMAIL.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_PHONE.setOptional().setNoWriteBack();
    }
}
