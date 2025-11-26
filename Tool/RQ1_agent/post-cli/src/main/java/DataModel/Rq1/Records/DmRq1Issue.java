/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_Rq1AssignedRecord;
import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Number;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Fields.DmRq1Field_Text_AsilClassification;
import DataModel.Rq1.Fields.DmRq1Field_Xml;
import DataModel.Rq1.Requirements.DmRq1Field_DngRequirementsOnIssuesFromExternalLinks;
import Rq1Cache.Records.Rq1Issue;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import UiSupport.BulkOperationRq1UserI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvEnumeration;
import util.MailSendable;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Issue extends DmRq1AssignedRecord implements MailSendable, BulkOperationRq1UserI {

    final public static String UI_NAME_SPECIFICATION_REVIEW = "Specification Review (with Customer)";

    final public DmRq1Field_Text ACCEPTANCE_CRITERIA;
    final public DmRq1Field_Text AFFECTED_ISSUE_COMMENT;
    final public DmRq1Field_Text RECLASSIFICATION_COMMENT;
    final public DmRq1Field_Text AGGREGATED_ESTIMATED_EFFORT;

    final public DmRq1Field_Text_AsilClassification ASIL_CLASSIFICATION;
    final public DmRq1Field_Text ASIL_CLASSIFICATION_CHANGE_COMMENT;
    final public DmRq1Field_Reference<DmRq1User> REQUESTER;
    final public DmRq1Field_Text REQUESTER_FULLNAME;
    final public DmRq1Field_Text REQUESTER_EMAIL;
    final public DmRq1Field_Text REQUESTER_LOGIN_NAME;
    final public DmRq1Field_Text DEFECT_CLASSIFICATION_COMMENT;
    final public DmRq1Field_Date DEFECT_DETECTION_DATE;
    final public DmRq1Field_Text DEFECT_DETECTION_LOCATION;
    final public DmRq1Field_Text DEFECT_DETECTION_LOCATION_COMMENT;
    final public DmRq1Field_Text DEFECT_DETECTION_PROCESS;
    final public DmRq1Field_Text DEFECT_DETECTION_PROCESS_COMMENT;
    final public DmRq1Field_Text DEFECT_DETECTION_ORGANISATION;
    final public DmRq1Field_Date DEFECT_INJECTION_DATE;
    final public DmRq1Field_Text DEFECT_INJECTION_ORGANISATION;
    final public DmRq1Field_Enumeration DEVELOPMENT_METHOD;

    final public DmRq1Field_Text RELATED_DEFECT_TRACKING_SYSTEM;
    final public DmRq1Field_Text RELATED_DEFECT_CLASSIFICATION;
    final public DmRq1Field_Text RELATED_ID;

    final public DmRq1Field_Text EXTERNAL_ID;
    final public DmRq1Field_Text EXTERNAL_COMMENT;
    final public DmRq1Field_Text EXTERNAL_CONVERSATION;
    final public DmRq1Field_Text EXTERNAL_HISTORY;
    final public DmRq1Field_Text EXTERNAL_DESCRIPTION;
    final public DmRq1Field_Enumeration EXTERNAL_NEXT_STATE;
    final public DmRq1Field_Text EXTERNAL_MILESTONES;
    final public DmRq1Field_Text EXTERNAL_ORGANISATION;
    final public DmRq1Field_Text EXTERNAL_STATE;
    final public DmRq1Field_Text EXTERNAL_TITLE;
    final public DmRq1Field_Enumeration EXTERNAL_REVIEW;

    final public DmRq1Field_Text EXTERNAL_SUBMITTER_NAME;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_EMAIL;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_PHONE;

    final public DmRq1Field_Reference<DmRq1Contact> EXTERNAL_ASSIGNEE;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_NAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_PHONE;

    final public DmRq1Field_Enumeration LEAN_LESSONS_LEARNED;
    final public DmRq1Field_Text LEAN_LESSONS_LEARNED_COMMENT;
    final public DmRq1Field_Text LEAN_LESSONS_LEARNED_CHANGE_COMMENT;
    final public DmRq1Field_Text LLL_DETAILED_DESCRIPTION;
    final public DmRq1Field_Text LLL_FIVE_WHY;
    final public DmRq1Field_Text LLL_INJECTION_ROOT_CAUSE;
    final public DmRq1Field_Text LLL_NON_DETECTION_ROOT_CAUSE;
    final public DmRq1Field_Text LLL_MEASURES;
    final public DmRq1Field_Enumeration OCCURRENCE;
    final public DmRq1Field_Enumeration SEVERITY;
    final public DmRq1Field_Enumeration DRBFM;
    final public DmRq1Field_Text DRBFM_CHANGE_COMMENT;
    final public DmRq1Field_Number ESTIMATED_EFFORT;
    final public DmRq1Field_Text ESTIMATION_COMMENT;
    final public DmRq1Field_Date EVALUATION_REQUIRED_BY;
    final public DmRq1Field_Enumeration FMEA_STATE;
    final public DmRq1Field_Enumeration LIFE_CYCLE_STATE;
    final public DmRq1Field_Xml MILESTONES;
    final public DmRq1Field_Text COLLABORATION;
    final public DmRq1Field_Text OEM_COLLABORATION_MODEL;
    final public DmRq1Field_Text PRODUCT;
    final public DmRq1Field_Enumeration PRODUCTION_RELEVANCE;
    final public DmRq1Field_Enumeration SAFETY_RELEVANCE;
    final public DmRq1Field_Enumeration REQUIREMENTS_REVIEW;
    final public DmRq1Field_Enumeration SCOPE;
    final public DmRq1Field_Enumeration SPECIFICATION_REVIEW;
    final public DmRq1Field_Text SPECIFICATION_REVIEW_COMMENT;
    final public DmRq1Field_Text SPECIFICATION_REVIEW_CHANGE_COMMENT;
    final public DmRq1Field_Text FMEA_CHANGE_COMMENT;
    final public DmRq1Field_Text FMEA_COMMENT;
    final public DmRq1Field_Text FMEA_PROCEDURE;
    final public DmRq1Field_Text FMEA_DOCUMENT;
    final public DmRq1Field_Text PROJECT_CONFIG;
    //
    final public DmRq1Field_ReferenceList<DmRq1AttachmentMapping> ATTACHMENT_MAPPING;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Release> HAS_MAPPED_RELEASES;
    final public DmRq1Field_Reference<DmRq1Problem> PROBLEM;
    final public DmRq1Field_Reference<DmRq1Issue> PREDECESSOR;
    final public DmRq1Field_ReferenceList<DmRq1Issue> SUCCESSORS;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> WORKITEMS;

    final public DmRq1Field_Text RCMS_OPL;

    final public DmRq1Field_Text SEVERITY_COMMENT;
    final public DmRq1Field_Text SEVERITY_CHANGE_COMMENT;

//    final public DmRq1Field_Text VALIDATION_EXCEPTION_COMMENT;
//    final public DmRq1Field_Text VALIDATION_EXCEPTIONS;
    final public DmRq1Field_DngRequirementsOnIssuesFromExternalLinks MAPPED_DNG_REQUIREMENTS;

    public DmRq1Issue(String subjectType, Rq1Issue rq1Issue) {
        super(subjectType, rq1Issue);

        //
        // Create and add fields
        //
        addField(AFFECTED_ISSUE_COMMENT = new DmRq1Field_Text(this, rq1Issue.AFFECTED_ISSUE_COMMENT, "Affected Issue Comment"));
        addField(RECLASSIFICATION_COMMENT = new DmRq1Field_Text(this, rq1Issue.RECLASSIFICATION_COMMENT, "Reclassification Comment"));

        addField(ACCEPTANCE_CRITERIA = new DmRq1Field_Text(this, rq1Issue.ACCEPTANCE_CRITERIA, "Acceptance Criteria"));
        ACCEPTANCE_CRITERIA.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);

        addField(ASIL_CLASSIFICATION = new DmRq1Field_Text_AsilClassification(this, rq1Issue.ASIL_CLASSIFICATION, "ASIL Classification"));
        ASIL_CLASSIFICATION.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        ASIL_CLASSIFICATION.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ASIL_CLASSIFICATION.setAttribute(DmFieldI.Attribute.ASIL_CLASSIFICATION);

        addField(ASIL_CLASSIFICATION_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Issue.ASIL_CLASSIFICATION_CHANGE_COMMENT, "ASIL Classification Change Comment"));

        ASIL_CLASSIFICATION_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);

        addField(AGGREGATED_ESTIMATED_EFFORT = new DmRq1Field_Text(this, rq1Issue.AGGREGATED_ESTIMATED_EFFORT, "Aggregated Est. Eff."));

        addField(COLLABORATION = new DmRq1Field_Text(this, rq1Issue.COLLABORATION, "Collaboration"));
        addField(OEM_COLLABORATION_MODEL = new DmRq1Field_Text(this, rq1Issue.OEM_COLLABORATION_MODEL, "OEM Collaboration Model"));

        addField(REQUESTER = new DmRq1Field_Reference<>(this, rq1Issue.REQUESTER, "Requester"));
        addField(REQUESTER_FULLNAME = new DmRq1Field_Text(this, rq1Issue.REQUESTER_FULLNAME, "Fullname Requester"));
        addField(REQUESTER_EMAIL = new DmRq1Field_Text(this, rq1Issue.REQUESTER_EMAIL, "E-Mail Requester"));
        addField(REQUESTER_LOGIN_NAME = new DmRq1Field_Text(this, rq1Issue.REQUESTER_LOGIN_NAME, "Shortcut Requester"));

        addField(DEFECT_CLASSIFICATION_COMMENT = new DmRq1Field_Text(this, rq1Issue.DEFECT_CLASSIFICATION_COMMENT, "Defect Classification Comment"));
        addField(DEFECT_DETECTION_DATE = new DmRq1Field_Date(this, rq1Issue.DEFECT_DETECTION_DATE, "Defect Detection Date"));
        addField(DEFECT_DETECTION_LOCATION = new DmRq1Field_Text(this, rq1Issue.DEFECT_DETECTION_LOCATION, "Defect Detection Location"));
        addField(DEFECT_DETECTION_LOCATION_COMMENT = new DmRq1Field_Text(this, rq1Issue.DEFECT_DETECTION_LOCATION_COMMENT, "Defect Detection Location Comment"));
        addField(DEFECT_DETECTION_PROCESS = new DmRq1Field_Text(this, rq1Issue.DEFECT_DETECTION_PROCESS, "Defect Detection Process"));
        addField(DEFECT_DETECTION_PROCESS_COMMENT = new DmRq1Field_Text(this, rq1Issue.DEFECT_DETECTION_PROCESS_COMMENT, "Defect Detection Process Comment"));
        addField(DEFECT_DETECTION_ORGANISATION = new DmRq1Field_Text(this, rq1Issue.DEFECT_DETECTION_ORGANISATION, "Defect Detection Organisation"));
        addField(DEFECT_INJECTION_DATE = new DmRq1Field_Date(this, rq1Issue.DEFECT_INJECTION_DATE, "Defect Injection Date"));
        addField(DEFECT_INJECTION_ORGANISATION = new DmRq1Field_Text(this, rq1Issue.DEFECT_INJECTION_ORGANISATION, "Defect Injection Organisation"));
        addField(RELATED_DEFECT_TRACKING_SYSTEM = new DmRq1Field_Text(this, rq1Issue.RELATED_SYSTEM, "Related Defect Tracking System"));
        addField(RELATED_DEFECT_CLASSIFICATION = new DmRq1Field_Text(this, rq1Issue.RELATED_DEFECT_CLASSIFICATION, "Related Defect Classification"));
        addField(RELATED_ID = new DmRq1Field_Text(this, rq1Issue.RELATED_ID, "Related ID"));

        addField(EXTERNAL_ID = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_ID, "External ID"));
        EXTERNAL_ID.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(EXTERNAL_COMMENT = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_COMMENT, "New External Comment"));
        EXTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(EXTERNAL_CONVERSATION = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_CONVERSATION, "External Conversation"));
        EXTERNAL_CONVERSATION.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(EXTERNAL_HISTORY = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_HISTORY, "External History"));
        EXTERNAL_HISTORY.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(EXTERNAL_DESCRIPTION = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_DESCRIPTION, "External Description"));
        EXTERNAL_DESCRIPTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        addField(EXTERNAL_NEXT_STATE = new DmRq1Field_Enumeration(this, rq1Issue.EXTERNAL_NEXT_STATE, "External Next State"));
        addField(EXTERNAL_MILESTONES = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_MILESTONES, "External Milestones"));
        EXTERNAL_MILESTONES.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(EXTERNAL_ORGANISATION = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_ORGANISATION, "External Organisation"));
        addField(EXTERNAL_STATE = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_STATE, "External State"));
        addField(EXTERNAL_TITLE = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_TITLE, "External Title"));
        EXTERNAL_TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        addField(EXTERNAL_REVIEW = new DmRq1Field_Enumeration(this, rq1Issue.EXTERNAL_REVIEW, "External Review"));

        addField(EXTERNAL_SUBMITTER_NAME = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_SUBMITTER_NAME, "External Submitter Name"));
        addField(EXTERNAL_SUBMITTER_ORGANIZATION = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_SUBMITTER_ORGANIZATION, "External Submitter Organization"));
        addField(EXTERNAL_SUBMITTER_DEPARTMENT = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_SUBMITTER_DEPARTMENT, "External Submitter Department"));
        addField(EXTERNAL_SUBMITTER_EMAIL = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_SUBMITTER_EMAIL, "External Submitter E-Mail"));
        addField(EXTERNAL_SUBMITTER_PHONE = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_SUBMITTER_PHONE, "External Submitter Phone Number"));

        addField(EXTERNAL_ASSIGNEE = new DmRq1Field_Reference<>(this, rq1Issue.EXTERNAL_ASSIGNEE, "External Assignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_ASSIGNEE_NAME, "External Assignee Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_ASSIGNEE_ORGANIZATION, "External Assignee Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_ASSIGNEE_DEPARTMENT, "External Assignee Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_ASSIGNEE_EMAIL, "External Assignee E-Mail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new DmRq1Field_Text(this, rq1Issue.EXTERNAL_ASSIGNEE_PHONE, "External Assignee Phone Number"));

        addField(DEVELOPMENT_METHOD = new DmRq1Field_Enumeration(this, rq1Issue.DEVELOPMENT_METHOD, "Development Method"));
        DEVELOPMENT_METHOD.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        DEVELOPMENT_METHOD.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(LEAN_LESSONS_LEARNED = new DmRq1Field_Enumeration(this, rq1Issue.LEAN_LESSONS_LEARNED, "Lean Lessons Learned"));
        addField(LEAN_LESSONS_LEARNED_COMMENT = new DmRq1Field_Text(this, rq1Issue.LEAN_LESSONS_LEARNED_COMMENT, "Lean Lessons Learned Comment"));
        addField(LEAN_LESSONS_LEARNED_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Issue.LEAN_LESSONS_LEARNED_CHANGE_COMMENT, "Lean Lessons Learned Change Comment"));

        addField(LLL_DETAILED_DESCRIPTION = new DmRq1Field_Text(this, rq1Issue.LLL_DETAILED_DESCRIPTION, "LLL Detailed Description"));
        addField(LLL_FIVE_WHY = new DmRq1Field_Text(this, rq1Issue.LLL_FIVE_WHY, "LLL 5x Why"));
        addField(LLL_INJECTION_ROOT_CAUSE = new DmRq1Field_Text(this, rq1Issue.LLL_INJECTION_ROOT_CAUSE, "LLL Injection Root Cause"));
        addField(LLL_NON_DETECTION_ROOT_CAUSE = new DmRq1Field_Text(this, rq1Issue.LLL_NON_DETECTION_ROOT_CAUSE, "LLL Non Detection Root Cause"));
        addField(LLL_MEASURES = new DmRq1Field_Text(this, rq1Issue.LLL_MEASURES, "LLL Measures"));
        addField(OCCURRENCE = new DmRq1Field_Enumeration(this, rq1Issue.OCCURRENCE, "Occurence"));
        addField(SEVERITY = new DmRq1Field_Enumeration(this, rq1Issue.SEVERITY, "Severity"));

        addField(DRBFM = new DmRq1Field_Enumeration(this, rq1Issue.DRBFM, "DRBFM"));
        DRBFM.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        DRBFM.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(DRBFM_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Issue.DRBFM_CHANGE_COMMENT, "DRBFM Change Comment"));

        addField(ESTIMATED_EFFORT = new DmRq1Field_Number(rq1Issue.ESTIMATED_EFFORT, "Estimated Effort"));
        addField(ESTIMATION_COMMENT = new DmRq1Field_Text(this, rq1Issue.ESTIMATION_COMMENT, "Estimation Comment"));
        ESTIMATION_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(EVALUATION_REQUIRED_BY = new DmRq1Field_Date(this, rq1Issue.EVALUATION_REQUIRED_BY, "Evaluation Required By"));
        EVALUATION_REQUIRED_BY.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        EVALUATION_REQUIRED_BY.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(FMEA_STATE = new DmRq1Field_Enumeration(this, rq1Issue.FMEA_STATE, "FMEA State"));
        FMEA_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        FMEA_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(FMEA_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Issue.FMEA_CHANGE_COMMENT, "FMEA Change Comment"));
        FMEA_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        FMEA_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        FMEA_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        FMEA_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(FMEA_COMMENT = new DmRq1Field_Text(this, rq1Issue.FMEA_COMMENT, "FMEA Comment"));
        FMEA_COMMENT.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        FMEA_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        FMEA_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        FMEA_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(FMEA_PROCEDURE = new DmRq1Field_Text(this, rq1Issue.FMEA_PROCEDURE, "FMEA Procedure"));
        addField(FMEA_DOCUMENT = new DmRq1Field_Text(this, rq1Issue.FMEA_DOCUMENT, "FMEA Document"));

        addField(PROJECT_CONFIG = new DmRq1Field_Text(this, rq1Issue.PROJECT_CONFIG, "Project Config"));
        PROJECT_CONFIG.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(LIFE_CYCLE_STATE = new DmRq1Field_Enumeration(this, rq1Issue.LIFE_CYCLE_STATE, "Life Cycle State"));
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);

        addField(MILESTONES = new DmRq1Field_Xml(this, rq1Issue.MILESTONES, "Milestones"));
        addField(PRODUCT = new DmRq1Field_Text(this, rq1Issue.PRODUCT, "Product"));

        addField(PRODUCTION_RELEVANCE = new DmRq1Field_Enumeration(this, rq1Issue.PRODUCTION_RELEVANCE, "Production Relevance"));
        PRODUCTION_RELEVANCE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PRODUCTION_RELEVANCE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SAFETY_RELEVANCE = new DmRq1Field_Enumeration(this, rq1Issue.SAFETY_RELEVANCE, "Safety Relevance"));

        addField(REQUIREMENTS_REVIEW = new DmRq1Field_Enumeration(this, rq1Issue.REQUIREMENTS_REVIEW, "Requirements Review (with Customer)"));
        REQUIREMENTS_REVIEW.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        REQUIREMENTS_REVIEW.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SCOPE = new DmRq1Field_Enumeration(this, rq1Issue.SCOPE, "Scope"));
        SCOPE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SPECIFICATION_REVIEW = new DmRq1Field_Enumeration(this, rq1Issue.SPECIFICATION_REVIEW, UI_NAME_SPECIFICATION_REVIEW));
        SPECIFICATION_REVIEW.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        SPECIFICATION_REVIEW.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SPECIFICATION_REVIEW_COMMENT = new DmRq1Field_Text(this, rq1Issue.SPECIFICATION_REVIEW_COMMENT, "Specification Review Comment"));
        addField(SPECIFICATION_REVIEW_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Issue.SPECIFICATION_REVIEW_CHANGE_COMMENT, "Specification Review Change Comment"));

        addField(PREDECESSOR = new DmRq1Field_Reference<>(this, rq1Issue.HAS_PREDECESSOR, "Predecessor"));
        addField(HAS_MAPPED_RELEASES = new DmRq1Field_MappedReferenceList<>(this, rq1Issue.HAS_MAPPED_RELEASES, "Releases"));
        addField(PROBLEM = new DmRq1Field_Reference<>(this, rq1Issue.BELONGS_TO_PROBLEM, "Problem"));
        addField(SUCCESSORS = new DmRq1Field_ReferenceList<>(this, rq1Issue.HAS_SUCCESSOR, "Successor"));
        addField(WORKITEMS = new DmRq1Field_ReferenceList<>(this, rq1Issue.HAS_WORKITEMS, "Workitems"));

        addField(RCMS_OPL = new DmRq1Field_Text(this, rq1Issue.RCMS_OPL, "RCMS OPL"));

        addField(SEVERITY_COMMENT = new DmRq1Field_Text(this, rq1Issue.SEVERITY_COMMENT, "Severity Comment"));
        addField(SEVERITY_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Issue.SEVERITY_CHANGE_COMMENT, "Severity Change Comment"));

        addField(ATTACHMENT_MAPPING = new DmRq1Field_ReferenceList<>(this, rq1Issue.HAS_ATTACHMENT_MAPPINGS, "Attachment Mappings (drop attachment from upper table to create mapping)"));

        //        addField(VALIDATION_EXCEPTION_COMMENT = new DmRq1Field_Text(rq1Issue.VALIDATION_EXCEPTION_COMMENT, "Validation Exception Comment"));
        //        addField(VALIDATION_EXCEPTIONS = new DmRq1Field_Text(rq1Issue.VALIDATION_EXCEPTIONS, "Validation Exceptions"));
        addField(MAPPED_DNG_REQUIREMENTS = new DmRq1Field_DngRequirementsOnIssuesFromExternalLinks(this, "DNG Requirements"));

        addRule(new ConfigurableRuleManagerRule_Rq1AssignedRecord(this));
    }

    private Rq1Issue getRq1Issue() {
        return ((Rq1Issue) super.rq1Record);
    }

    @Override
    final public String getElementClass() {
        return ("Issue");
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    @Override
    final public boolean isCanceled() {
        return ((LifeCycleState_Issue) LIFE_CYCLE_STATE.getValue() == LifeCycleState_Issue.CANCELED);
    }

    final public boolean isConflicted() {
        return (isInLifeCycleState(LifeCycleState_Issue.CONFLICTED));
    }

    final public boolean isCanceledOrConflicted() {
        return (isCanceled() || ((LifeCycleState_Issue) LIFE_CYCLE_STATE.getValue() == LifeCycleState_Issue.CONFLICTED));
    }

    final public boolean isClosed() {
        return ((LifeCycleState_Issue) LIFE_CYCLE_STATE.getValue() == LifeCycleState_Issue.CLOSED);
    }

    @Override
    public String getResponsible() {
        return (PROJECT.getElement().RESPONSIBLE_AT_BOSCH.getValue());
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LIFE_CYCLE_STATE.getValidInputValues());
    }

    @Override
    public String getAssigneeMail() {
        return ASSIGNEE_EMAIL.getValueAsText();
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTypeIdTitle();
    }

    @Override
    public String getIdForSubject() {
        return getId();
    }

    @Override
    public List<MailActionType> getActionName() {
        List<MailActionType> mailActionTypes = new ArrayList<>();
        mailActionTypes.add(MailActionType.ASSIGNEE);
        mailActionTypes.add(MailActionType.REQUESTER);
        return mailActionTypes;
    }

    @Override
    public String getProjectLeaderMail() {
        return null;
    }

    @Override
    public String getRequesterMail() {
        return REQUESTER_EMAIL.getValueAsText();
    }
    
    @Override
    public String getContactMail() {
        return null;
    }

    @Override
    public Collection<DmFieldI> getEditableFields() {
        ArrayList<DmFieldI> fields = new ArrayList<>();
//        fields.add(ASIL_CLASSIFICATION);
        return fields;
    }

    @Override
    public DmRq1Field_Reference<DmRq1User> getRequesterField() {
        return (REQUESTER);
    }

    //--------------------------------------------------------------------------
    //
    // Support for connecting with problem
    //
    //-------------------------------------------------------------------------
    public void setProblem(DmRq1Problem dmProblem) throws DmRq1MapExistsException {
        assert (dmProblem != null);
        for (DmRq1Issue mappedIssue : dmProblem.ISSUES.getElementList()) {
            if (mappedIssue == this) {
                throw (new DmRq1MapExistsException());
            }
        }
        if (PROBLEM.isElementSet() == true) {
            PROBLEM.getElement().ISSUES.removeElement(this);
        }
        PROBLEM.setElement(dmProblem);
        dmProblem.ISSUES.addElement(this);
    }

}
