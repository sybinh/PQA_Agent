/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_ContactReference;
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_MappedReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_UserReference;
import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import static Rq1Cache.Records.Rq1SoftwareIssue.ATTRIBUTE_CATEGORY;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.Enumerations.DevelopmentMethod;
import Rq1Data.Enumerations.DrbfmState;
import Rq1Data.Enumerations.ExternalNextState;
import Rq1Data.Enumerations.ExternalReview;
import Rq1Data.Enumerations.FmeaState;
import Rq1Data.Enumerations.LeanLessonsLearned;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.Occurrence;
import Rq1Data.Enumerations.ProductionRelevance;
import Rq1Data.Enumerations.ReviewState;
import Rq1Data.Enumerations.SafetyRelevance;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.Severity;

/**
 *
 * @author GUG2WI
 */
public class Rq1Issue extends Rq1AssignedRecord {

    final static public Rq1AttributeName ATTRIBUTE_ASIL_CLASSIFICATION = new Rq1AttributeName("ASILClassification");
    final static public Rq1AttributeName ATTRIBUTE_BELONGS_TO_PROJECT = new Rq1AttributeName("belongsToProject");
    final static public Rq1AttributeName ATTRIBUTE_COLLABORATION = new Rq1AttributeName("Collaboration");
    final static public Rq1AttributeName ATTRIBUTE_DEVELOPMENT_METHOD = new Rq1AttributeName("DevelopmentMethod");
    final static public Rq1AttributeName ATTRIBUTE_HAS_PARENT = new Rq1AttributeName("hasParent");
    final static public Rq1AttributeName ATTRIBUTE_HAS_MAPPED_RELEASES = new Rq1AttributeName("hasMappedReleases");
    final static public Rq1AttributeName ATTRIBUTE_HAS_WORKITEMS = new Rq1AttributeName("hasWorkitems");
    final static public Rq1AttributeName ATTRIBUTE_HAS_ATTACHMENT_MAPPINGS = new Rq1AttributeName("hasAttachmentMappings");
    final static public Rq1AttributeName ATTRIBUTE_OEM_COLLABORATION_MODEL = new Rq1AttributeName("OEMCollaborationModel");
    final static public Rq1AttributeName ATTRIBUTE_RELATED_SYSTEM = new Rq1AttributeName("RelatedSystem");
    final static public Rq1AttributeName ATTRIBUTE_RELATED_DEFECT_CLASSIFICATION = new Rq1AttributeName("RelatedDefectClass");
    final public static Rq1AttributeName ATTRIBUTE_DEFECT_CLASSIFICATION = new Rq1AttributeName("DefectClassification");
    final static public Rq1AttributeName ATTRIBUTE_DEFECT_DETECTION_LOCATION = new Rq1AttributeName("DefectDetectionLocation");
    final static public Rq1AttributeName ATTRIBUTE_DEFECT_DETECTION_ORGANISATION = new Rq1AttributeName("DefectDetectionOrga");
    final static public Rq1AttributeName ATTRIBUTE_DEFECT_DETECTION_PROCESS = new Rq1AttributeName("DefectDetectionProcess");
    final static public Rq1AttributeName ATTRIBUTE_DEFECT_INJECTION_ORGANISATION = new Rq1AttributeName("DefectInjectionOrga");
    final static public Rq1AttributeName ATTRIBUTE_DEFECT_WORKPRODUCT_TYPE = new Rq1AttributeName("DefectiveWorkproductType");
    final static public Rq1AttributeName ATTRIBUTE_LLL_INJECTION_ROOT_CAUSE = new Rq1AttributeName("LLLInjectionRootCause");
    final static public Rq1AttributeName ATTRIBUTE_LLL_NON_DETECTION_ROOT_CAUSE = new Rq1AttributeName("LLLNonDetectionRootCause");

    final public static String FIELDNAME_EXTERNAL_ID = "External_ID";

    final public Rq1DatabaseField_Text ACCEPTANCE_CRITERIA;
    final public Rq1XmlSubField_Text AFFECTED_ISSUE_COMMENT;
    final public Rq1XmlSubField_Text RECLASSIFICATION_COMMENT;
    final public Rq1DatabaseField_Text AGGREGATED_ESTIMATED_EFFORT;
    //  final public Rq1DatabaseField_Enumeration ALLOCATION;
    final public Rq1DatabaseField_Text ASIL_CLASSIFICATION;
    final public Rq1XmlSubField_Text ASIL_CLASSIFICATION_CHANGE_COMMENT;
    final public Rq1DatabaseField_Enumeration DRBFM;
    final public Rq1DatabaseField_Text COLLABORATION;
    final public Rq1XmlSubField_Text DRBFM_CHANGE_COMMENT;
    final public Rq1DatabaseField_Text DEFECT_CLASSIFICATION;
    final public Rq1XmlSubField_Text DEFECT_CLASSIFICATION_COMMENT;
    final public Rq1DatabaseField_Date DEFECT_DETECTION_DATE;
    final public Rq1DatabaseField_Text DEFECT_DETECTION_LOCATION;
    final public Rq1XmlSubField_Text DEFECT_DETECTION_LOCATION_COMMENT;
    final public Rq1DatabaseField_Text DEFECT_DETECTION_PROCESS;
    final public Rq1XmlSubField_Text DEFECT_DETECTION_PROCESS_COMMENT;
    final public Rq1DatabaseField_Text DEFECT_DETECTION_ORGANISATION;
    final public Rq1DatabaseField_Date DEFECT_INJECTION_DATE;
    final public Rq1DatabaseField_Text DEFECT_INJECTION_ORGANISATION;
    final public Rq1DatabaseField_Text DEFECT_WORKPRODUCT_TYPE;
    final public Rq1DatabaseField_Enumeration DEVELOPMENT_METHOD;
    final public Rq1DatabaseField_Text ESTIMATED_EFFORT;
    final public Rq1DatabaseField_Text ESTIMATION_COMMENT;
    final public Rq1XmlSubField_Date EVALUATION_REQUIRED_BY;
    final public Rq1XmlSubField_Text FMEA_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text FMEA_COMMENT;
    final public Rq1XmlSubField_Text FMEA_DOCUMENT;
    final public Rq1XmlSubField_Text FMEA_PROCEDURE;
    final public Rq1DatabaseField_Enumeration FMEA_STATE;

    final public Rq1DatabaseField_Text EXTERNAL_ID;
    final public Rq1DatabaseField_Text EXTERNAL_COMMENT;
    final public Rq1DatabaseField_Text EXTERNAL_CONVERSATION;
    final public Rq1DatabaseField_Text EXTERNAL_HISTORY;
    final public Rq1DatabaseField_Text EXTERNAL_DESCRIPTION;
    final public Rq1DatabaseField_Enumeration EXTERNAL_NEXT_STATE;
    final public Rq1DatabaseField_Text EXTERNAL_MILESTONES;
    final public Rq1DatabaseField_Text EXTERNAL_ORGANISATION;
    final public Rq1DatabaseField_Enumeration EXTERNAL_REVIEW;
    final public Rq1DatabaseField_Text EXTERNAL_STATE;
    final public Rq1DatabaseField_Text EXTERNAL_TAGS;
    final public Rq1DatabaseField_Text EXTERNAL_TITLE;
    final public Rq1DatabaseField_ContactReference EXTERNAL_SUBMITTER;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_NAME;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_ORGANIZATION;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_DEPARTMENT;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_EMAIL;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_PHONE;
    final public Rq1DatabaseField_ContactReference EXTERNAL_ASSIGNEE;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_NAME;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_PHONE;

    final public Rq1DatabaseField_MappedReferenceList HAS_MAPPED_RELEASES;
    final public Rq1DatabaseField_ReferenceList HAS_ATTACHMENT_MAPPINGS;
    final public Rq1DatabaseField_Reference HAS_PARENT;
    final public Rq1DatabaseField_Reference HAS_PREDECESSOR;
    final public Rq1DatabaseField_ReferenceList HAS_SUCCESSOR;
    final public Rq1DatabaseField_ReferenceList HAS_WORKITEMS;
    final public Rq1DatabaseField_Reference BELONGS_TO_PROBLEM;
    final public Rq1DatabaseField_Enumeration LIFE_CYCLE_STATE;

    final public Rq1DatabaseField_Enumeration LEAN_LESSONS_LEARNED;
    final public Rq1XmlSubField_Text LEAN_LESSONS_LEARNED_COMMENT;
    final public Rq1XmlSubField_Text LEAN_LESSONS_LEARNED_CHANGE_COMMENT;

    final public Rq1DatabaseField_Text LLL_DETAILED_DESCRIPTION;
    final public Rq1DatabaseField_Text LLL_FIVE_WHY;
    final public Rq1DatabaseField_Text LLL_INJECTION_ROOT_CAUSE;
    final public Rq1DatabaseField_Text LLL_NON_DETECTION_ROOT_CAUSE;
    final public Rq1DatabaseField_Text LLL_MEASURES;
    final public Rq1DatabaseField_Enumeration OCCURRENCE;
    final public Rq1DatabaseField_Enumeration SEVERITY;
    final public Rq1DatabaseField_Xml MILESTONES;
    final public Rq1DatabaseField_Text OEM_COLLABORATION_MODEL;
    final public Rq1DatabaseField_Text PRODUCT;
    final public Rq1DatabaseField_Enumeration PRODUCTION_RELEVANCE;
    final public Rq1XmlSubField_Text PROJECT_CONFIG;
    final public Rq1DatabaseField_Text RELATED_SYSTEM;
    final public Rq1DatabaseField_Text RELATED_DEFECT_CLASSIFICATION;
    final public Rq1DatabaseField_Text RELATED_ID;
    final public Rq1DatabaseField_UserReference REQUESTER;
    final public Rq1DatabaseField_Text REQUESTER_FULLNAME;
    final public Rq1DatabaseField_Text REQUESTER_EMAIL;
    final public Rq1DatabaseField_Text REQUESTER_LOGIN_NAME;
    final public Rq1DatabaseField_Enumeration REQUIREMENTS_REVIEW;
    final public Rq1DatabaseField_Enumeration SAFETY_RELEVANCE;
    final public Rq1DatabaseField_Enumeration SCOPE;
    final public Rq1DatabaseField_Enumeration SPECIFICATION_REVIEW;
    final public Rq1XmlSubField_Text SPECIFICATION_REVIEW_COMMENT;
    final public Rq1XmlSubField_Text SPECIFICATION_REVIEW_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text RCMS_OPL;
    final public Rq1XmlSubField_Text SEVERITY_COMMENT;
    final public Rq1XmlSubField_Text SEVERITY_CHANGE_COMMENT;
//    final public Rq1DatabaseField_Text VALIDATION_EXCEPTION_COMMENT;
//    final public Rq1DatabaseField_Text VALIDATION_EXCEPTIONS;

    public Rq1Issue(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        addField(ACCEPTANCE_CRITERIA = new Rq1DatabaseField_Text(this, "AcceptanceCriteria"));
        addField(AFFECTED_ISSUE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "AffectedIssue_Comment"));
        addField(RECLASSIFICATION_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "Reclassification_Comment"));
        addField(ASIL_CLASSIFICATION = new Rq1DatabaseField_Text(this, ATTRIBUTE_ASIL_CLASSIFICATION));
        addField(ASIL_CLASSIFICATION_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "ASILClassification_ChangeComment"));
        addField(OEM_COLLABORATION_MODEL = new Rq1DatabaseField_Text(this, ATTRIBUTE_OEM_COLLABORATION_MODEL));
        addField(COLLABORATION = new Rq1DatabaseField_Text(this, ATTRIBUTE_COLLABORATION));

        addField(AGGREGATED_ESTIMATED_EFFORT = new Rq1DatabaseField_Text(this, "hasBackground.AggregatedEstimatedEffort"));
        AGGREGATED_ESTIMATED_EFFORT.setOptional();

        addField(REQUESTER = new Rq1DatabaseField_UserReference(this, "Requester"));
        addField(REQUESTER_FULLNAME = new Rq1DatabaseField_Text(this, "Requester.fullname"));
        addField(REQUESTER_EMAIL = new Rq1DatabaseField_Text(this, "Requester.email"));
        addField(REQUESTER_LOGIN_NAME = new Rq1DatabaseField_Text(this, "Requester.login_name"));

        REQUESTER.setFieldForFullName(REQUESTER_FULLNAME);
        REQUESTER.setFieldForEmail(REQUESTER_EMAIL);
        REQUESTER.setFieldForLoginName(REQUESTER_LOGIN_NAME);

        REQUESTER.setOptional();
        REQUESTER_FULLNAME.setNoWriteBack().setOptional();
        REQUESTER_EMAIL.setNoWriteBack().setOptional();
        REQUESTER_LOGIN_NAME.setNoWriteBack().setOptional();

        addField(RELATED_SYSTEM = new Rq1DatabaseField_Text(this, ATTRIBUTE_RELATED_SYSTEM));
        addField(RELATED_DEFECT_CLASSIFICATION = new Rq1DatabaseField_Text(this, "RelatedDefectClass"));
        addField(RELATED_ID = new Rq1DatabaseField_Text(this, "RelatedID"));

//        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, "Category", SoftwareIssueCategory.values()));
//        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", SoftwareIssueClassification.values()));
        addField(DEFECT_CLASSIFICATION = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_CLASSIFICATION));
        addField(DEFECT_CLASSIFICATION_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DefectClassification_Comment"));
        addField(DEFECT_DETECTION_DATE = new Rq1DatabaseField_Date(this, "DefectDetectionDate"));
        addField(DEFECT_DETECTION_LOCATION = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_DETECTION_LOCATION));
        addField(DEFECT_DETECTION_LOCATION_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DefectDetectionLocation_Comment"));
        addField(DEFECT_DETECTION_PROCESS = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_DETECTION_PROCESS));
        addField(DEFECT_DETECTION_PROCESS_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DefectDetectionProcess_Comment"));
        addField(DEFECT_DETECTION_ORGANISATION = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_DETECTION_ORGANISATION));
        addField(DEFECT_INJECTION_DATE = new Rq1DatabaseField_Date(this, "DefectInjectionDate"));
        addField(DEFECT_INJECTION_ORGANISATION = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_INJECTION_ORGANISATION));
        addField(DEFECT_WORKPRODUCT_TYPE = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_WORKPRODUCT_TYPE));

        addField(DEVELOPMENT_METHOD = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_DEVELOPMENT_METHOD, DevelopmentMethod.values()));
        DEVELOPMENT_METHOD.acceptInvalidValuesInDatabase();

        addField(EXTERNAL_ID = new Rq1DatabaseField_Text(this, FIELDNAME_EXTERNAL_ID));
        addField(EXTERNAL_COMMENT = new Rq1DatabaseField_Text(this, "ExternalComment"));
        addField(EXTERNAL_CONVERSATION = new Rq1DatabaseField_Text(this, "ExternalConversation"));
        addField(EXTERNAL_HISTORY = new Rq1DatabaseField_Text(this, "ExternalHistory"));
        addField(EXTERNAL_DESCRIPTION = new Rq1DatabaseField_Text(this, "ExternalDescription"));
        addField(EXTERNAL_NEXT_STATE = new Rq1DatabaseField_Enumeration(this, "ExternalNextState", ExternalNextState.values()));
        addField(EXTERNAL_MILESTONES = new Rq1DatabaseField_Text(this, "ExternalMilestones"));
        addField(EXTERNAL_ORGANISATION = new Rq1DatabaseField_Text(this, "ExternalOrganisation"));
        addField(EXTERNAL_TAGS = new Rq1DatabaseField_Text(this, "ExternalTags"));
        addField(EXTERNAL_STATE = new Rq1DatabaseField_Text(this, "ExternalState"));
        addField(EXTERNAL_TITLE = new Rq1DatabaseField_Text(this, "ExternalTitle"));
        addField(EXTERNAL_REVIEW = new Rq1DatabaseField_Enumeration(this, "ExternalReview", ExternalReview.values()));

        addField(EXTERNAL_SUBMITTER = new Rq1DatabaseField_ContactReference(this, "ExternalSubmitter"));
        addField(EXTERNAL_SUBMITTER_NAME = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Name"));
        addField(EXTERNAL_SUBMITTER_ORGANIZATION = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Organization"));
        addField(EXTERNAL_SUBMITTER_DEPARTMENT = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Department"));
        addField(EXTERNAL_SUBMITTER_EMAIL = new Rq1DatabaseField_Text(this, "ExternalSubmitter.eMail"));
        addField(EXTERNAL_SUBMITTER_PHONE = new Rq1DatabaseField_Text(this, "ExternalSubmitter.PhoneNumbers"));
        EXTERNAL_SUBMITTER.setFieldForName(EXTERNAL_SUBMITTER_NAME);
        EXTERNAL_SUBMITTER.setFieldForOrganization(EXTERNAL_SUBMITTER_ORGANIZATION);
        EXTERNAL_SUBMITTER.setFieldForDepartment(EXTERNAL_SUBMITTER_DEPARTMENT);
        EXTERNAL_SUBMITTER.setFieldForEmail(EXTERNAL_SUBMITTER_EMAIL);
        EXTERNAL_SUBMITTER.setFieldForPhone(EXTERNAL_SUBMITTER_PHONE);
        EXTERNAL_SUBMITTER_NAME.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_ORGANIZATION.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_DEPARTMENT.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_EMAIL.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_PHONE.setOptional().setNoWriteBack();

        addField(EXTERNAL_ASSIGNEE = new Rq1DatabaseField_ContactReference(this, "ExternalAssignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new Rq1DatabaseField_Text(this, "ExternalAssignee.Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new Rq1DatabaseField_Text(this, "ExternalAssignee.Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "ExternalAssignee.eMail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new Rq1DatabaseField_Text(this, "ExternalAssignee.PhoneNumbers"));
        EXTERNAL_ASSIGNEE.setFieldForName(EXTERNAL_ASSIGNEE_NAME);
        EXTERNAL_ASSIGNEE.setFieldForOrganization(EXTERNAL_ASSIGNEE_ORGANIZATION);
        EXTERNAL_ASSIGNEE.setFieldForDepartment(EXTERNAL_ASSIGNEE_DEPARTMENT);
        EXTERNAL_ASSIGNEE.setFieldForEmail(EXTERNAL_ASSIGNEE_EMAIL);
        EXTERNAL_ASSIGNEE.setFieldForPhone(EXTERNAL_ASSIGNEE_PHONE);
        EXTERNAL_ASSIGNEE_NAME.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_ORGANIZATION.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_DEPARTMENT.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_EMAIL.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_PHONE.setOptional().setNoWriteBack();

        addField(LEAN_LESSONS_LEARNED = new Rq1DatabaseField_Enumeration(this, "LeanLessonsLearned", LeanLessonsLearned.values()));
        addField(LEAN_LESSONS_LEARNED_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "LeanLessonsLearned_Comment"));
        LEAN_LESSONS_LEARNED_COMMENT.addAlternativName("LeanLessonsLearned");
        addField(LEAN_LESSONS_LEARNED_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "LeanLessonsLearned_ChangeComment"));

        addField(LLL_DETAILED_DESCRIPTION = new Rq1DatabaseField_Text(this, "LLLDetailedDescription"));
        addField(LLL_FIVE_WHY = new Rq1DatabaseField_Text(this, "LLL5Why"));
        addField(LLL_MEASURES = new Rq1DatabaseField_Text(this, "LLLMeasures"));
        addField(LLL_INJECTION_ROOT_CAUSE = new Rq1DatabaseField_Text(this, ATTRIBUTE_LLL_INJECTION_ROOT_CAUSE));
        addField(LLL_NON_DETECTION_ROOT_CAUSE = new Rq1DatabaseField_Text(this, ATTRIBUTE_LLL_NON_DETECTION_ROOT_CAUSE));
        addField(OCCURRENCE = new Rq1DatabaseField_Enumeration(this, "Occurrence", Occurrence.values()));
        addField(SEVERITY = new Rq1DatabaseField_Enumeration(this, "Severity", Severity.values()));

        addField(DRBFM = new Rq1DatabaseField_Enumeration(this, "DRBFM", DrbfmState.values()));
        DRBFM.acceptInvalidValuesInDatabase();
        addField(DRBFM_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DRBFM_ChangeComment"));

        addField(ESTIMATED_EFFORT = new Rq1DatabaseField_Text(this, "EstimatedEffort"));
        addField(ESTIMATION_COMMENT = new Rq1DatabaseField_Text(this, "EstimationComment"));
        addField(FMEA_STATE = new Rq1DatabaseField_Enumeration(this, "FMEA", FmeaState.values()));

        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_Issue.values(), LifeCycleState_Issue.NEW));

        addField(MILESTONES = new Rq1DatabaseField_Xml(this, "Milestones"));
        addField(PRODUCT = new Rq1DatabaseField_Text(this, "Product"));
        addField(PRODUCTION_RELEVANCE = new Rq1DatabaseField_Enumeration(this, "ProductionRelevance", ProductionRelevance.values()));
        addField(SAFETY_RELEVANCE = new Rq1DatabaseField_Enumeration(this, "SafetyRelevance", SafetyRelevance.values()));
        addField(REQUIREMENTS_REVIEW = new Rq1DatabaseField_Enumeration(this, "RequirementsReview", ReviewState.values()));
        addField(SPECIFICATION_REVIEW = new Rq1DatabaseField_Enumeration(this, "SpecificationReview", ReviewState.values()));
        addField(SCOPE = new Rq1DatabaseField_Enumeration(this, "Scope", Scope.values()));

        addField(HAS_MAPPED_RELEASES = new Rq1DatabaseField_MappedReferenceList(this, ATTRIBUTE_HAS_MAPPED_RELEASES, "hasMappedRelease", Rq1RecordType.IRM, Rq1RecordType.RELEASE));
        addField(BELONGS_TO_PROBLEM = new Rq1DatabaseField_Reference(this, "belongsToProblem", Rq1RecordType.PROBLEM));

        addField(HAS_ATTACHMENT_MAPPINGS = new Rq1DatabaseField_ReferenceList(this, ATTRIBUTE_HAS_ATTACHMENT_MAPPINGS, Rq1RecordType.ATTACHMENT_MAPPING));
        addField(HAS_PARENT = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_PARENT, Rq1RecordType.ISSUE));
        addField(HAS_PREDECESSOR = new Rq1DatabaseField_Reference(this, "hasPredecessor", Rq1RecordType.ISSUE));
        addField(HAS_SUCCESSOR = new Rq1DatabaseField_ReferenceList(this, "hasSuccessor", Rq1RecordType.ISSUE));
        addField(HAS_WORKITEMS = new Rq1DatabaseField_ReferenceList(this, ATTRIBUTE_HAS_WORKITEMS, Rq1RecordType.WORKITEM));

        addField(FMEA_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "FMEA_ChangeComment"));
        addField(FMEA_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "FMEA_Comment"));
        FMEA_COMMENT.addAlternativName("FMEA_comment");
        FMEA_COMMENT.addAlternativName("FMEAcomment");
        addField(FMEA_PROCEDURE = new Rq1XmlSubField_Text(this, TAGS, "FMEAprocedure"));
        addField(FMEA_DOCUMENT = new Rq1XmlSubField_Text(this, TAGS, "FMEAdocument"));
        addField(PROJECT_CONFIG = new Rq1XmlSubField_Text(this, TAGS, "PROJECTCONFIG"));
        PROJECT_CONFIG.addAlternativName("ProjectConfig");
        PROJECT_CONFIG.addAlternativName("Projectconfig");
        PROJECT_CONFIG.addAlternativName("Projectkonfig");
        PROJECT_CONFIG.addAlternativName("projectconfig");
        addField(SPECIFICATION_REVIEW_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SpecificationReview_Comment"));
        SPECIFICATION_REVIEW_COMMENT.addAlternativName("SpecificationReviewComment");
        addField(SPECIFICATION_REVIEW_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SpecificationReview_ChangeComment"));

        addField(RCMS_OPL = new Rq1XmlSubField_Text(this, TAGS, "RCMS_OPL"));

        addField(SEVERITY_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "Severity_Comment"));
        addField(SEVERITY_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "Severity_ChangeComment"));

        addField(EVALUATION_REQUIRED_BY = new Rq1XmlSubField_Date(this, MILESTONES, "EvaluationRequiredBy"));

//        addField(VALIDATION_EXCEPTION_COMMENT = new Rq1DatabaseField_Text(this, "ValidationExceptComment"));
//        addField(VALIDATION_EXCEPTIONS = new Rq1DatabaseField_Text(this, "ValidationExceptions"));
    }

    @Override
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        Rq1AttributeName[] order = {ATTRIBUTE_BELONGS_TO_PROJECT, ATTRIBUTE_OPERATION_MODE, ATTRIBUTE_DOMAIN, ATTRIBUTE_CATEGORY, ATTRIBUTE_HAS_PARENT};
        return (super.createInDatabase(order));
    }

}
