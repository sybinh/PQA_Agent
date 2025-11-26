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
import Rq1Cache.Fields.Rq1XmlSubField_Combined;
import Rq1Cache.Fields.Rq1XmlSubField_Combined_TextPart;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1LinkDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1XmlTable_CustomerResponse_IRM;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIRM;
import Rq1Data.Enumerations.EcbPverFQStatus;
import Rq1Data.Enumerations.ExternalNextState;
import Rq1Data.Enumerations.InMaIntState;
import Rq1Data.Enumerations.IntHint_State;
import Rq1Data.Enumerations.IntegratorState;
import Rq1Data.Enumerations.YesNoEmpty;

/**
 *
 * @author gug2wi
 */
public class Rq1Irm_Pst_IssueSw extends Rq1SoftwareIrm {

    final public Rq1XmlSubField_Text CUSTOMER_PRIORITY;
    final public Rq1XmlSubField_Date PVER_F_PLANNED_DATE;
    final public Rq1XmlSubField_Text RB_COMMENT;
    final public Rq1XmlSubField_Text QUALIFICATION_STATUS_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text PVER_F_COMMENT;
    final public Rq1XmlSubField_Text CALIBRATION_COMMENT;
    final public Rq1XmlSubField_Text CALIBRATION_STATE;
    final public Rq1XmlSubField_Text INTEGRATOR_COMMENT;
    final public Rq1XmlSubField_Enumeration INTEGRATOR_STATE;
    final public Rq1DatabaseField_Enumeration LAST_MINUTE_CHANGE;
    final public Rq1DatabaseField_Enumeration LATE_CHANGE;

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

    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIRM> L2_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIRM> L2_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIRM> L2_REMOVED_REQUIREMENTS;

    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIRM> MO_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIRM> MO_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIRM> MO_AFFECTED_ARTEFACT;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIRM> MO_REMOVED_REQUIREMENTS;

    final private Rq1XmlSubField_Xml CUSTOMER_RESPONSE_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CustomerResponse_IRM> CUSTOMER_RESPONSE;

    final private Rq1XmlSubField_Combined INT_HINT;
    final public Rq1XmlSubField_Combined_TextPart INT_HINT_COMMENT;

    final public Rq1XmlSubField_Enumeration IN_MA_INTEGRATION_STATUS;

    //
    // Fields for ECB
    //
    final public Rq1XmlSubField_Combined ECB_INT_HINT_COMMENT_FIELD;
    final public Rq1XmlSubField_Combined_TextPart ECB_INT_HINT_COMMENT_TEXT;
    final public Rq1XmlSubField_Text ECB_INT_HINT_DIDINFO;
    final public Rq1XmlSubField_Text ECB_INT_HINT_REQUESTER;
    final public Rq1XmlSubField_Enumeration ECB_INT_HINT_STATE;
    final public Rq1XmlSubField_Enumeration ECB_COMMERCIAL_PILOT;
    final public Rq1XmlSubField_Text ECB_INTEGRATOR;
    final public Rq1XmlSubField_Enumeration ECB_PVAR_F_QSTATUS;
    //
    final public Rq1XmlSubField_Text PROJECT_CONFIG_CONFIRM;

    public Rq1Irm_Pst_IssueSw() {
        super(Rq1LinkDescription.IRM_PVAR_REL_ISSUE_SW);

        PROJECT_CONFIG_CONFIRM = new Rq1XmlSubField_Text(this, TAGS, "ProjectConfigConfirmed");

        addField(CUSTOMER_PRIORITY = new Rq1XmlSubField_Text(this, TAGS, "CustomerPriority"));
        addField(PVER_F_PLANNED_DATE = new Rq1XmlSubField_Date(this, TAGS, "PVER_F_Planned_Date"));
        addField(RB_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "RB_Comment"));
        RB_COMMENT.addAlternativName("KundenKommentar");
        addField(QUALIFICATION_STATUS_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "QualificationStatus_ChangeComment"));
        addField(PVER_F_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "PVER_F_Comment"));
        addField(CALIBRATION_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "CalibrationComment"));
        addField(CALIBRATION_STATE = new Rq1XmlSubField_Text(this, TAGS, "CalibrationState"));
        addField(INTEGRATOR_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "IntegratorComment"));
        addField(INTEGRATOR_STATE = new Rq1XmlSubField_Enumeration(this, TAGS, "IntegratorState", IntegratorState.values(), IntegratorState.EMPTY));
        addField(LAST_MINUTE_CHANGE = new Rq1DatabaseField_Enumeration(this, "LastMinuteChange", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        addField(LATE_CHANGE = new Rq1DatabaseField_Enumeration(this, "LateChange", YesNoEmpty.values(), YesNoEmpty.EMPTY));

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

        addField(L2_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIRM(), TAGS, "L2_impacted_requirements"));
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_Impacted_requirements");
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_Impacted_Requirements");
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_impacted_requirement");
        addField(L2_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIRM(), TAGS, "L2_planned_requirements"));
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_Planned_requirements");
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_Planned_Requirements");
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_planned_requirement");
        addField(L2_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIRM(), TAGS, "L2_removed_requirements"));
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_Removed_requirements");
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_Removed_Requirements");
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_removed_requirement");

        addField(MO_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIRM(), TAGS, "MO_impacted_requirements"));
        addField(MO_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIRM(), TAGS, "MO_planned_requirements"));
        addField(MO_AFFECTED_ARTEFACT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIRM(), TAGS, "MO_affected_artefact"));
        addField(MO_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIRM(), TAGS, "MO_removed_requirements"));

        addField(CUSTOMER_RESPONSE_XML = new Rq1XmlSubField_Xml(this, TAGS, "CustomerResponse"));
        CUSTOMER_RESPONSE_XML.setOptional();
        addField(CUSTOMER_RESPONSE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CustomerResponse_IRM(), CUSTOMER_RESPONSE_XML, "Response"));
        CUSTOMER_RESPONSE.setOptional();

        addField(INT_HINT = new Rq1XmlSubField_Combined(this, TAGS, "IntHint"));
        INT_HINT.setOptional();
        addField(INT_HINT_COMMENT = new Rq1XmlSubField_Combined_TextPart(this, INT_HINT));
        INT_HINT_COMMENT.setReadOnly();

        addField(IN_MA_INTEGRATION_STATUS = new Rq1XmlSubField_Enumeration(this, TAGS, "InMaIntState", InMaIntState.values(), InMaIntState.EMPTY));

        //
        // Fields for ECB
        //
        addField(ECB_INT_HINT_COMMENT_FIELD = new Rq1XmlSubField_Combined(this, INT_HINT, "Comment"));
        addField(ECB_INT_HINT_COMMENT_TEXT = new Rq1XmlSubField_Combined_TextPart(this, ECB_INT_HINT_COMMENT_FIELD));
        addField(ECB_INT_HINT_DIDINFO = new Rq1XmlSubField_Text(this, TAGS, "DIDinfo"));
        addField(ECB_INT_HINT_REQUESTER = new Rq1XmlSubField_Text(this, INT_HINT, "Requester"));
        addField(ECB_INT_HINT_STATE = new Rq1XmlSubField_Enumeration(this, INT_HINT, "State", IntHint_State.values(), IntHint_State.EMPTY));
        ECB_INT_HINT_STATE.acceptInvalidValuesInDatabase();
        addField(ECB_COMMERCIAL_PILOT = new Rq1XmlSubField_Enumeration(this, TAGS, "Commercial-Pilot", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        ECB_COMMERCIAL_PILOT.acceptInvalidValuesInDatabase();
        addField(ECB_INTEGRATOR = new Rq1XmlSubField_Text(this, TAGS, "Integrator"));
        addField(ECB_PVAR_F_QSTATUS = new Rq1XmlSubField_Enumeration(this, TAGS, "PVER_F_QStatus", EcbPverFQStatus.values(), EcbPverFQStatus.EMPTY));
    }
}
