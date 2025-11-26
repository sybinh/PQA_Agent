/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1MappedReferenceListField_FilterByClass;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIssue;
import Rq1Data.Enumerations.Approval;
import Rq1Data.Enumerations.CommercialClassification;
import Rq1Data.Enumerations.CommercialQuotationReq;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.OfferType;
import Rq1Data.Enumerations.SalesInfo;
import Rq1Data.Enumerations.Sync;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Enumerations.ZlkReview;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the cache for an Issue SW record of the RQ1 database.
 *
 * @author gug2wi
 */
public class Rq1IssueSW extends Rq1SoftwareIssue {

    /**
     * Fields for Sync RQ1 and ALM
     */
    final public Rq1DatabaseField_Enumeration SYNC;
    final public Rq1DatabaseField_Text LINKS;
    /**
     *
     */

    final public static String FIELDNAME_HAS_CHILDREN = "hasChildren";
    final public static String FIELDNAME_AFFECTED_BY_DEFECT = "isAffectedByDefectIssue";
    final public static String FIELDNAME_AFFECTED_ISSUE = "AffectedIssue";
    final public static Rq1AttributeName ATTRIBUTE_EXCHANGE_WORKFLOW = new Rq1AttributeName("ExternalExchangeWorkflow");
    final public static Rq1AttributeName ATTRIBUTE_EXCHANGED_ATTACHMENTS = new Rq1AttributeName("ExternalExchangedAttach");

    final public static String TAGNAME_COMMERCIAL_REQUESTED = "CommercialRequested";
    final public static String TAGNAME_COMMERCIAL_QUOTED = "CommercialQuoted";
    final public static String TAGNAME_COMMERCIAL_AGGREED = "CommercialAgreed";

    final public Rq1DatabaseField_Enumeration APPROVAL;
    final public Rq1XmlSubField_Text ALLOCATION_COMMENT;
    final public Rq1DatabaseField_Reference AFFECTED_ISSUE;
    final public Rq1DatabaseField_ReferenceList AFFECTED_RELEASES;
    final public Rq1XmlSubField_Text ASAM_SALES_COMMENT;
    final public Rq1MappedReferenceListField_FilterByClass HAS_MAPPED_PST;
    final public Rq1MappedReferenceListField_FilterByClass HAS_MAPPED_PST_COLLECTIONS;
    final public Rq1DatabaseField_Reference COMMERCIAL_ASSIGNEE;
    final public Rq1DatabaseField_Text COMMERCIAL_ASSIGNEE_FULLNAME;
    final public Rq1DatabaseField_Text COMMERCIAL_ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text COMMERCIAL_ASSIGNEE_LOGIN_NAME;
    final public Rq1DatabaseField_Enumeration COMMERCIAL_CLASSIFICATION;
    final public Rq1DatabaseField_Text COMMERCIAL_QUOTATION_ID;
    final public Rq1DatabaseField_Enumeration COMMERCIAL_QUOTATION_REQUIRED;
    final public Rq1XmlSubField_Date COMMERCIAL_REQUESTED;
    final public Rq1XmlSubField_Date COMMERCIAL_QUOTED;
    final public Rq1XmlSubField_Date COMMERCIAL_AGREED;
    final public Rq1XmlSubField_Text CUSTOMER_SEVERITY;
    final public Rq1XmlSubField_Text REQUIREMENTS_REVIEW_CHANGE_COMMENT;

    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Text FLOW_EST_SHEET_NAME;
    
    final public Rq1XmlSubField_Xml CRP;
    final public Rq1XmlSubField_Text CRP_EST_SHEET_NAME;

    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_FIRSTNAME;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_LASTNAME;

    final public Rq1XmlSubField_Text CSTCOMMENT;
    final public Rq1DatabaseField_Text EXCHANGE_WORKFLOW;
    final public Rq1DatabaseField_Text EXCHANGED_ATTACHMENTS;
    final public Rq1XmlSubField_Text HAUPTENTWICKLUNGSPAKET;
    final public Rq1DatabaseField_ReferenceList HAS_CHILDREN;
    final public Rq1XmlSubField_Text ISSUE_SW_SUB_CATEGORY;
    final public Rq1XmlSubField_Text ISSUE_SW_SUB_CATEGORY_DETAIL;
    final public Rq1DatabaseField_ReferenceList IS_AFFECTED_BY_DEFECT_ISSUE;
    final public Rq1DatabaseField_Enumeration OEM_PROPOSAL_ADOPTED;
    final public Rq1XmlSubField_Enumeration OFFER_TYPE;
    final public Rq1XmlSubField_Enumeration SALES_INFO;
    final public Rq1XmlSubField_Enumeration ZLK_REVIEW;

    final public Rq1XmlSubField_Text TREXILM;

    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L1_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L2_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L2_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L1_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L2_REMOVED_REQUIREMENTS;

    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_AFFECTED_ARTEFACT;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> MO_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> MO_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> MO_AFFECTED_ARTEFACT;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> MO_REMOVED_REQUIREMENTS;

    public Rq1IssueSW() {
        super(Rq1NodeDescription.ISSUE_SW);

        addField(SYNC = new Rq1DatabaseField_Enumeration(this, "Sync", Sync.values()));
        SYNC.acceptInvalidValuesInDatabase();
        addField(LINKS = new Rq1DatabaseField_Text(this, "Links"));
        LINKS.setReadOnly();

        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        FLOW.setOptional();
        addField(FLOW_EST_SHEET_NAME = new Rq1XmlSubField_Text(this, FLOW, "EST_SHEET_NAME"));
        FLOW_EST_SHEET_NAME.setOptional();
        
        addField(CRP = new Rq1XmlSubField_Xml(this, TAGS, "CRP"));
        CRP.setOptional();
        addField(CRP_EST_SHEET_NAME = new Rq1XmlSubField_Text(this, CRP, "EST_SHEET_NAME"));
        CRP_EST_SHEET_NAME.setOptional();
        
        addField(CSTCOMMENT = new Rq1XmlSubField_Text(this, TAGS, "CST_Comment"));

        addField(APPROVAL = new Rq1DatabaseField_Enumeration(this, "Approval", Approval.values()));

        addField(ALLOCATION_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "Allocation_Comment"));
        addField(ASAM_SALES_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "ASAM_Sales_Comment"));

        EXTERNAL_REVIEW.acceptInvalidValuesInDatabase();

        addField(EXTERNAL_ASSIGNEE_LASTNAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.LastName"));
        addField(EXTERNAL_ASSIGNEE_FIRSTNAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.FirstName"));
        EXTERNAL_ASSIGNEE_LASTNAME.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_FIRSTNAME.setOptional().setNoWriteBack();

        addField(EXCHANGE_WORKFLOW = new Rq1DatabaseField_Text(this, ATTRIBUTE_EXCHANGE_WORKFLOW));
        addField(EXCHANGED_ATTACHMENTS = new Rq1DatabaseField_Text(this, ATTRIBUTE_EXCHANGED_ATTACHMENTS));
        EXCHANGED_ATTACHMENTS.setReadOnly();

        addField(COMMERCIAL_ASSIGNEE = new Rq1DatabaseField_Reference(this, "CommercialAssignee", Rq1RecordType.USER));
        addField(COMMERCIAL_ASSIGNEE_FULLNAME = new Rq1DatabaseField_Text(this, "CommercialAssignee.fullname"));
        addField(COMMERCIAL_ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "CommercialAssignee.email"));
        addField(COMMERCIAL_ASSIGNEE_LOGIN_NAME = new Rq1DatabaseField_Text(this, "CommercialAssignee.login_name"));
        COMMERCIAL_ASSIGNEE_FULLNAME.setNoWriteBack().setOptional();
        COMMERCIAL_ASSIGNEE_EMAIL.setNoWriteBack().setOptional();
        COMMERCIAL_ASSIGNEE_LOGIN_NAME.setNoWriteBack().setOptional();
        addField(COMMERCIAL_CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "CommercialClassification", CommercialClassification.values()));
        addField(COMMERCIAL_QUOTATION_ID = new Rq1DatabaseField_Text(this, "CommercialQuotationId"));
        addField(COMMERCIAL_QUOTATION_REQUIRED = new Rq1DatabaseField_Enumeration(this, "CommercialQuotationReq", CommercialQuotationReq.values()));

        addField(COMMERCIAL_REQUESTED = new Rq1XmlSubField_Date(this, MILESTONES, TAGNAME_COMMERCIAL_REQUESTED));
        addField(COMMERCIAL_QUOTED = new Rq1XmlSubField_Date(this, MILESTONES, TAGNAME_COMMERCIAL_QUOTED));
        addField(COMMERCIAL_AGREED = new Rq1XmlSubField_Date(this, MILESTONES, TAGNAME_COMMERCIAL_AGGREED));

        addField(CUSTOMER_SEVERITY = new Rq1XmlSubField_Text(this, TAGS, "CustomerSeverity"));

        addField(REQUIREMENTS_REVIEW_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "RequirementsReview_ChangeComment"));

        addField(ISSUE_SW_SUB_CATEGORY = new Rq1XmlSubField_Text(this, TAGS, "IssueSWC"));
        addField(ISSUE_SW_SUB_CATEGORY_DETAIL = new Rq1XmlSubField_Text(this, TAGS, "IssueSWC_DETAIL"));

        addField(HAUPTENTWICKLUNGSPAKET = new Rq1XmlSubField_Text(this, TAGS, "HauptEntwicklungsPaket"));

        addField(HAS_CHILDREN = new Rq1DatabaseField_ReferenceList(this, FIELDNAME_HAS_CHILDREN, Rq1RecordType.ISSUE));
//        HAS_CHILDREN.setMaximumLoadDepth(OslcDirectReferenceFieldI.LoadDepth.Content);

        addField(HAS_MAPPED_PST = new Rq1MappedReferenceListField_FilterByClass(this, HAS_MAPPED_RELEASES, Rq1Pst.class));
        addField(HAS_MAPPED_PST_COLLECTIONS = new Rq1MappedReferenceListField_FilterByClass(this, HAS_MAPPED_RELEASES, Rq1PstCollectionI.class));

        addField(AFFECTED_RELEASES = new Rq1DatabaseField_ReferenceList(this, "AffectedReleases", Rq1RecordType.RELEASE));
        addField(AFFECTED_ISSUE = new Rq1DatabaseField_Reference(this, FIELDNAME_AFFECTED_ISSUE, Rq1RecordType.ISSUE));
//        AFFECTED_ISSUE.setMaximumLoadDepth(OslcDirectReferenceListFieldI.LoadDepth.Id);

        addField(IS_AFFECTED_BY_DEFECT_ISSUE = new Rq1DatabaseField_ReferenceList(this, FIELDNAME_AFFECTED_BY_DEFECT, Rq1RecordType.ISSUE));

        addField(OEM_PROPOSAL_ADOPTED = new Rq1DatabaseField_Enumeration(this, "OEMProposalAdopted", YesNoEmpty.values()));

        addField(OFFER_TYPE = new Rq1XmlSubField_Enumeration(this, TAGS, "OFFERTYPE", OfferType.values(), null));
        OFFER_TYPE.acceptInvalidValuesInDatabase().setOptional();
        addField(SALES_INFO = new Rq1XmlSubField_Enumeration(this, TAGS, "SALESINFO", SalesInfo.values(), null));
        SALES_INFO.acceptInvalidValuesInDatabase().setOptional();
        addField(ZLK_REVIEW = new Rq1XmlSubField_Enumeration(this, TAGS, "ZLKREVIEW", ZlkReview.values(), null));
        ZLK_REVIEW.acceptInvalidValuesInDatabase().setOptional();

        addField(TREXILM = new Rq1XmlSubField_Text(this, TAGS, "TREXILM"));
        ZLK_REVIEW.acceptInvalidValuesInDatabase().setOptional();

        addField(L1_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L1_planned_requirements"));
        L1_PLANNED_REQUIREMENTS.addAlternativName("L1_Planned_requirements");
        L1_PLANNED_REQUIREMENTS.addAlternativName("L1_Planned_Requirements");
        L1_PLANNED_REQUIREMENTS.addAlternativName("L1_planned_requirement");
        addField(L2_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L2_impacted_requirements"));
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_Impacted_requirements");
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_Impacted_Requirements");
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_impacted_requirement");
        addField(L2_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L2_planned_requirements"));
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_Planned_requirements");
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_Planned_Requirements");
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_planned_requirement");
        addField(L1_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L1_removed_requirements"));
        L1_REMOVED_REQUIREMENTS.addAlternativName("L1_Removed_requirements");
        L1_REMOVED_REQUIREMENTS.addAlternativName("L1_Removed_Requirements");
        L1_REMOVED_REQUIREMENTS.addAlternativName("L1_removed_requirement");
        addField(L2_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L2_removed_requirements"));
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_Removed_requirements");
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_Removed_Requirements");
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_removed_requirement");

        addField(STAKEHOLDER_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "Stakeholder_planned_requirements"));
        addField(STAKEHOLDER_AFFECTED_ARTEFACT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "Stakeholder_affected_artefact"));
        STAKEHOLDER_AFFECTED_ARTEFACT.addAlternativName("Stakeholder_affected_requirements");
        addField(STAKEHOLDER_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "Stakeholder_removed_requirements"));

        addField(MO_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "MO_impacted_requirements"));
        addField(MO_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "MO_planned_requirements"));
        addField(MO_AFFECTED_ARTEFACT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "MO_affected_artefact"));
        MO_AFFECTED_ARTEFACT.addAlternativName("MO_affected_requirements");
        addField(MO_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "MO_removed_requirements"));
    }

    public static Iterable<Rq1IssueSW> get_ISW_on_Project_for_Customer(Rq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.ISSUE_SW.getRecordType());

        query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_Issue.getAllOpenState());
        query.addCriteria_FixedValues(Rq1NodeDescription.ISSUE_SW.getFixedRecordValues());
        query.addCriteria_Reference(Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT.getName(), project);
        query.addCriteria_Value(
                Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT,
                Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1IssueSW> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1IssueSW) {
                result.add((Rq1IssueSW) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }
}
