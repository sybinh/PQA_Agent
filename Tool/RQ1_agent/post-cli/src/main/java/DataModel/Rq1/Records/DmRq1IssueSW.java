/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Dgs.DmDgsIssueSW_I;
import DataModel.DmFieldI;
import DataModel.DmFieldI.Attribute;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB;
import static DataModel.DmFieldI.Attribute.MULTILINE_TEXT;
import DataModel.DmValueFieldI_Text;
import DataModel.Flow.InternalRank;
import DataModel.Monitoring.Rule_IssueSW_Conflicted;
import DataModel.Monitoring.Rule_IssueSW_DefectFlowModel;
import DataModel.Rq1.Requirements.DmRq1Field_DoorsRequirementsOnIssueFromTables;
import DataModel.Rq1.Requirements.DmRq1LinkToRequirement_Type;
import DataModel.Rq1.Fields.DmRq1Field_BesondereMerkmale_OEM;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Hauptentwicklungspaket;
import DataModel.Rq1.Fields.DmRq1Field_IssueSwSubCategory;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_AccountNumberFormat;
import DataModel.Rq1.Monitoring.Rule_IssueSW_ASIL;
import DataModel.Rq1.Monitoring.Rule_IssueSW_FmeaCheck;
import DataModel.Rq1.Monitoring.Rule_IssueSW_MissingAffectedIssueComment;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnIssue;
import Ipe.Annotations.IpeFactoryConstructor;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1IssueSW;
import Rq1Cache.Records.Rq1Project;
import Rq1Cache.Records.Rq1SoftwareIssue;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIssue;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.SoftwareIssueCategory;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1TemplateI;
import ToolUsageLogger.ToolUsageLogger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class DmRq1IssueSW extends DmRq1SoftwareIssue implements DmDgsIssueSW_I {

    final public DmRq1Field_Enumeration SYNC;
    final public DmRq1Field_Text LINKS;

    final public DmRq1Field_Text CSTCOMMENT;

    final public DmRq1Field_Enumeration APPROVAL;
    final public DmRq1Field_Text ALLOCATION_COMMENT;
    final public DmRq1Field_Reference<DmRq1IssueSW> AFFECTED_ISSUE;
    final public DmRq1Field_ReferenceList<DmRq1Release> AFFECTED_RELEASES;
    final public DmRq1Field_Text ASAM_SALES_COMMENT;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> CHILDREN;
    final public DmRq1Field_Enumeration COMMERCIAL_CLASSIFICATION;
    final public DmRq1Field_Reference<DmRq1User> COMMERCIAL_ASSIGNEE;
    final public DmRq1Field_Text COMMERCIAL_ASSIGNEE_FULLNAME;
    final public DmRq1Field_Text COMMERCIAL_ASSIGNEE_EMAIL;
    final public DmRq1Field_Text COMMERCIAL_ASSIGNEE_LOGIN_NAME;
    final public DmRq1Field_Text COMMERCIAL_QUOTATION_ID;
    final public DmRq1Field_Enumeration COMMERCIAL_QUOTATION_REQ;
    final public DmRq1Field_Date COMMERCIAL_REQUESTED;
    final public DmRq1Field_Date COMMERCIAL_QUOTED;
    final public DmRq1Field_Date COMMERCIAL_AGREED;
    final public DmRq1Field_Text CUSTOMER_SEVERITY;

    final public DmRq1Field_Text DEFECT_CLASSIFICATION;

    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_LASTNAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_FIRSTNAME;

    final public DmRq1Field_Text EXCHANGE_WORKFLOW;
    final public DmRq1Field_Text EXCHANGED_ATTACHMENTS;
    final public DmRq1Field_IssueSwSubCategory ISSUE_SW_SUB_CATEGORY;
    final public DmRq1Field_Text ISSUE_SW_SUB_CATEGORY_DETAIL;
    final public DmRq1Field_Text REQUIREMENTS_REVIEW_CHANGE_COMMENT;

    final public DmValueFieldI_Text HAUPTENTWICKLUNGSPAKET;

    final public DmValueFieldI_Text BESONDERE_MERKMALE_OEM_S;
    final public DmValueFieldI_Text BESONDERE_MERKMALE_OEM_S_KOMMENTAR;
    final public DmValueFieldI_Text BESONDERE_MERKMALE_OEM_O;
    final public DmValueFieldI_Text BESONDERE_MERKMALE_OEM_O_KOMMENTAR;
    final public DmValueFieldI_Text BESONDERE_MERKMALE_OEM_D;
    final public DmValueFieldI_Text BESONDERE_MERKMALE_OEM_D_KOMMENTAR;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> IS_AFFECTED_BY_DEFECT_ISSUE;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_Pst_IssueSw, DmRq1Pst> HAS_MAPPED_COLLECTIONS;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_Pst_IssueSw, DmRq1Pst> HAS_MAPPED_PST;
    final public DmRq1Field_Enumeration OEM_PROPOSAL_ADOPTED;

    final public DmRq1Field_Enumeration OFFER_TYPE;
    final public DmRq1Field_Enumeration SALES_INFO;
    final public DmRq1Field_Enumeration ZLK_REVIEW;

    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L1_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L1_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L2_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L2_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L2_REMOVED_REQUIREMENT_TABLE;

    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_PLANNED_REQUIREMENTS;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_AFFECTED_ARTEFACT;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_REMOVED_REQUIREMENTS;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> MO_IMPACTED_REQUIREMENTS;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> MO_PLANNED_REQUIREMENTS;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> MO_AFFECTED_ARTEFACT;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> MO_REMOVED_REQUIREMENTS;

    final public DmRq1Field_DoorsRequirementsOnIssueFromTables MAPPED_DOORS_REQUIREMENTS;
    final public DmRq1Field_AllRequirementsOnIssue MAPPED_REQUIREMENTS;

    public InternalRank internalRank = null;
    public String FLOW_INTERNAL_RANK = "";
    public DmRq1Field_Text FLOW_EST_SHEET_NAME;
    public DmRq1Field_Text CRP_EST_SHEET_NAME;

    final public DmRq1Field_Text TREXILM;

    @SuppressWarnings("unchecked")
    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1IssueSW(Rq1IssueSW rq1IssueSW) {
        super("I-SW", rq1IssueSW);

        //
        // Create and add fields
        addField(SYNC = new DmRq1Field_Enumeration(this, rq1IssueSW.SYNC, "Sync"));
        addField(LINKS = new DmRq1Field_Text(this, rq1IssueSW.LINKS, "Links"));

        addField(FLOW_EST_SHEET_NAME = new DmRq1Field_Text(this, rq1IssueSW.FLOW_EST_SHEET_NAME, "Flow estimation sheet"));
        
        addField(CRP_EST_SHEET_NAME = new DmRq1Field_Text(this, rq1IssueSW.CRP_EST_SHEET_NAME, "Crp estimation sheet"));

        addField(CSTCOMMENT = new DmRq1Field_Text(this, rq1IssueSW.CSTCOMMENT, "CST Comment"));

        addField(APPROVAL = new DmRq1Field_Enumeration(this, rq1IssueSW.APPROVAL, "Approval"));
        APPROVAL.setAttribute(FIELD_FOR_USER_DEFINED_TAB);
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(ALLOCATION_COMMENT = new DmRq1Field_Text(this, rq1IssueSW.ALLOCATION_COMMENT, "Allocation Comment"));
        addField(ASAM_SALES_COMMENT = new DmRq1Field_Text(this, rq1IssueSW.ASAM_SALES_COMMENT, "ASAM Sales Comment"));
        addField(COMMERCIAL_ASSIGNEE = new DmRq1Field_Reference<>(this, rq1IssueSW.COMMERCIAL_ASSIGNEE, "Commercial Assignee"));
        addField(COMMERCIAL_ASSIGNEE_FULLNAME = new DmRq1Field_Text(this, rq1IssueSW.COMMERCIAL_ASSIGNEE_FULLNAME, "Fullname Commercial Assignee"));
        addField(COMMERCIAL_ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1IssueSW.COMMERCIAL_ASSIGNEE_EMAIL, "E-Mail Commercial Assignee"));
        addField(COMMERCIAL_ASSIGNEE_LOGIN_NAME = new DmRq1Field_Text(this, rq1IssueSW.COMMERCIAL_ASSIGNEE_LOGIN_NAME, "Shortcut Commercial Assignee"));
        addField(COMMERCIAL_CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1IssueSW.COMMERCIAL_CLASSIFICATION, "Commercial Classification"));
        addField(COMMERCIAL_QUOTATION_ID = new DmRq1Field_Text(this, rq1IssueSW.COMMERCIAL_QUOTATION_ID, "Commercial Quotation ID"));
        COMMERCIAL_QUOTATION_ID.setAttribute(FIELD_FOR_BULK_OPERATION);
        addField(COMMERCIAL_QUOTATION_REQ = new DmRq1Field_Enumeration(this, rq1IssueSW.COMMERCIAL_QUOTATION_REQUIRED, "Commercial Quotation Req"));
        COMMERCIAL_QUOTATION_REQ.setAttribute(FIELD_FOR_USER_DEFINED_TAB);
        COMMERCIAL_QUOTATION_REQ.setAttribute(FIELD_FOR_BULK_OPERATION);

        addField(COMMERCIAL_REQUESTED = new DmRq1Field_Date(this, rq1IssueSW.COMMERCIAL_REQUESTED, "Commercial Requested"));
        COMMERCIAL_REQUESTED.setAttribute(FIELD_FOR_USER_DEFINED_TAB, FIELD_FOR_BULK_OPERATION);
        addField(COMMERCIAL_QUOTED = new DmRq1Field_Date(this, rq1IssueSW.COMMERCIAL_QUOTED, "Commercial Quoted"));
        COMMERCIAL_QUOTED.setAttribute(FIELD_FOR_USER_DEFINED_TAB, FIELD_FOR_BULK_OPERATION);
        addField(COMMERCIAL_AGREED = new DmRq1Field_Date(this, rq1IssueSW.COMMERCIAL_AGREED, "Commercial Agreed"));
        COMMERCIAL_AGREED.setAttribute(FIELD_FOR_USER_DEFINED_TAB, FIELD_FOR_BULK_OPERATION);

        addField(CUSTOMER_SEVERITY = new DmRq1Field_Text(this, rq1IssueSW.CUSTOMER_SEVERITY, "Customer Severity"));

        addField(CHILDREN = new DmRq1Field_ReferenceList<>(this, rq1IssueSW.HAS_CHILDREN, "Children"));

        addField(DEFECT_CLASSIFICATION = new DmRq1Field_Text(this, rq1IssueSW.DEFECT_CLASSIFICATION, "Defect Classification"));

        addField(EXTERNAL_ASSIGNEE_LASTNAME = new DmRq1Field_Text(this, rq1IssueSW.EXTERNAL_ASSIGNEE_LASTNAME, "External Assignee Last Name"));
        addField(EXTERNAL_ASSIGNEE_FIRSTNAME = new DmRq1Field_Text(this, rq1IssueSW.EXTERNAL_ASSIGNEE_FIRSTNAME, "External Assignee First Name"));

        addField(EXCHANGE_WORKFLOW = new DmRq1Field_Text(this, rq1IssueSW.EXCHANGE_WORKFLOW, "Exchange Workflow"));
        addField(EXCHANGED_ATTACHMENTS = new DmRq1Field_Text(this, rq1IssueSW.EXCHANGED_ATTACHMENTS, "Exchanged Attachments"));
        EXCHANGED_ATTACHMENTS.setAttribute(FIELD_FOR_USER_DEFINED_TAB, MULTILINE_TEXT);

        addField(HAUPTENTWICKLUNGSPAKET = new DmRq1Field_Hauptentwicklungspaket(this, rq1IssueSW.HAUPTENTWICKLUNGSPAKET, rq1IssueSW.EXTERNAL_TAGS, "Konzernarbeitspaket"));

        addField(ISSUE_SW_SUB_CATEGORY = new DmRq1Field_IssueSwSubCategory(this, rq1IssueSW.ISSUE_SW_SUB_CATEGORY, PROJECT, "Sub Category"));
        addField(ISSUE_SW_SUB_CATEGORY_DETAIL = new DmRq1Field_Text(this, rq1IssueSW.ISSUE_SW_SUB_CATEGORY_DETAIL, "Sub Category Detail"));

        addField(REQUIREMENTS_REVIEW_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1IssueSW.REQUIREMENTS_REVIEW_CHANGE_COMMENT, "Requirements Review Change Comment"));

        addField(BESONDERE_MERKMALE_OEM_S = new DmRq1Field_BesondereMerkmale_OEM(this, rq1IssueSW.EXTERNAL_TAGS, "Anforderungsbewertung S \\(sicherheitsrelevant\\)", "BM S OEM"));
        addField(BESONDERE_MERKMALE_OEM_S_KOMMENTAR = new DmRq1Field_BesondereMerkmale_OEM(this, rq1IssueSW.EXTERNAL_TAGS, "Kommentar Sicherheitsrelevant", "Besondere Merkmale S OEM Kommentar"));
        addField(BESONDERE_MERKMALE_OEM_O = new DmRq1Field_BesondereMerkmale_OEM(this, rq1IssueSW.EXTERNAL_TAGS, "Anforderungsbewertung O \\(OBD relevant\\)", "BM O OEM"));
        addField(BESONDERE_MERKMALE_OEM_O_KOMMENTAR = new DmRq1Field_BesondereMerkmale_OEM(this, rq1IssueSW.EXTERNAL_TAGS, "Kommentar OBD relevant", "Besondere Merkmale O OEM Kommentar"));
        addField(BESONDERE_MERKMALE_OEM_D = new DmRq1Field_BesondereMerkmale_OEM(this, rq1IssueSW.EXTERNAL_TAGS, "Anforderungsbewertung D \\(dokumentationspflichtig\\)", "BM D OEM"));
        addField(BESONDERE_MERKMALE_OEM_D_KOMMENTAR = new DmRq1Field_BesondereMerkmale_OEM(this, rq1IssueSW.EXTERNAL_TAGS, "Kommentar Dokumentationspflichtig", "Besondere Merkmale D OEM Kommentar"));

        addField(HAS_MAPPED_PST = new DmRq1Field_MappedReferenceList<>(this, rq1IssueSW.HAS_MAPPED_PST, "Mapped PVER/PVAR"));
        addField(HAS_MAPPED_COLLECTIONS = new DmRq1Field_MappedReferenceList<>(this, rq1IssueSW.HAS_MAPPED_PST_COLLECTIONS, "Mapped PVER/PVAR Collections"));

        addField(AFFECTED_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1IssueSW.AFFECTED_RELEASES, "Affected Releases"));
        addField(AFFECTED_ISSUE = new DmRq1Field_Reference<>(this, rq1IssueSW.AFFECTED_ISSUE, "Affected Issue"));
        addField(IS_AFFECTED_BY_DEFECT_ISSUE = new DmRq1Field_ReferenceList<>(this, rq1IssueSW.IS_AFFECTED_BY_DEFECT_ISSUE, "Is Affected By Issues"));

        addField(OEM_PROPOSAL_ADOPTED = new DmRq1Field_Enumeration(this, rq1IssueSW.OEM_PROPOSAL_ADOPTED, "OEM Proposal Adopted"));

        addField(OFFER_TYPE = new DmRq1Field_Enumeration(this, rq1IssueSW.OFFER_TYPE, "Offer Type"));
        addField(SALES_INFO = new DmRq1Field_Enumeration(this, rq1IssueSW.SALES_INFO, "Sales Info"));
        addField(ZLK_REVIEW = new DmRq1Field_Enumeration(this, rq1IssueSW.ZLK_REVIEW, "ZLK Review"));

        addField(TREXILM = new DmRq1Field_Text(this, rq1IssueSW.TREXILM, "TREXILM"));

        addField(L1_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueSW.L1_PLANNED_REQUIREMENTS, "L1 Planned Requirements ")); // Blank at the end of the name to differ it from the list below.
        addField(L1_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueSW.L1_REMOVED_REQUIREMENTS, "L1 Removed Requirements "));
        addField(L2_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueSW.L2_PLANNED_REQUIREMENTS, "L2 Planned Requirements "));
        addField(L2_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueSW.L2_IMPACTED_REQUIREMENTS, "L2 Impacted Requirements "));
        addField(L2_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueSW.L2_REMOVED_REQUIREMENTS, "L2 Removed Requirements "));

        addField(MAPPED_DOORS_REQUIREMENTS = new DmRq1Field_DoorsRequirementsOnIssueFromTables(this, "Mapped Doors Requirements"));
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L1_PLANNED, L1_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L1_REMOVED, L1_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_PLANNED, L2_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_IMPACTED, L2_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_REMOVED, L2_REMOVED_REQUIREMENT_TABLE);
        addField(MAPPED_REQUIREMENTS = new DmRq1Field_AllRequirementsOnIssue(this, MAPPED_DOORS_REQUIREMENTS, MAPPED_DNG_REQUIREMENTS, "Mapped Requirements"));

        addField(STAKEHOLDER_PLANNED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1IssueSW.STAKEHOLDER_PLANNED_REQUIREMENTS, "Stakeholder Planned Requirements "));
        addField(STAKEHOLDER_AFFECTED_ARTEFACT = new DmRq1Field_Table<>(this, rq1IssueSW.STAKEHOLDER_AFFECTED_ARTEFACT, "Stakeholder Affected Artefact "));
        addField(STAKEHOLDER_REMOVED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1IssueSW.STAKEHOLDER_REMOVED_REQUIREMENTS, "Stakeholder Removed Requirements "));

        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.STAKEHOLDER_PLANNED, STAKEHOLDER_PLANNED_REQUIREMENTS);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.STAKEHOLDER_AFFECTED, STAKEHOLDER_AFFECTED_ARTEFACT);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.STAKEHOLDER_REMOVED, STAKEHOLDER_REMOVED_REQUIREMENTS);

        addField(MO_PLANNED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1IssueSW.MO_PLANNED_REQUIREMENTS, "MO Planned Requirements "));
        addField(MO_AFFECTED_ARTEFACT = new DmRq1Field_Table<>(this, rq1IssueSW.MO_AFFECTED_ARTEFACT, "MO Affected Artefact "));
        addField(MO_IMPACTED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1IssueSW.MO_IMPACTED_REQUIREMENTS, "MO Impacted Requirements "));
        addField(MO_REMOVED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1IssueSW.MO_REMOVED_REQUIREMENTS, "MO Removed Requirements "));

        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_PLANNED, MO_PLANNED_REQUIREMENTS);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_AFFECTED, MO_AFFECTED_ARTEFACT);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_IMPACTED, MO_IMPACTED_REQUIREMENTS);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_REMOVED, MO_REMOVED_REQUIREMENTS);

        //
        // Create and add rules
        //
//        addRule(new Rule_IssueSW_OnePilot(this));
        addRule(new Rule_IssueSW_ASIL(this));
        addRule(new Rule_IssueSW_FmeaCheck(this));
        addRule(new Rule_IssueSW_MissingAffectedIssueComment(this));
        addRule(new Rule_AccountNumberFormat(this));

        //
        // Add switchable rule groups
        //
        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.I_SW_PLANNING));
        addRule(new Rule_IssueSW_DefectFlowModel(this));
        addRule(new Rule_IssueSW_Conflicted(this));

        //
        // Settings for bulk operation
        //
        COMMERCIAL_CLASSIFICATION.setAttribute(Attribute.FIELD_FOR_BULK_OPERATION);
        ISSUE_SW_SUB_CATEGORY.setAttribute(Attribute.FIELD_FOR_BULK_OPERATION);
        ISSUE_SW_SUB_CATEGORY_DETAIL.setAttribute(Attribute.FIELD_FOR_BULK_OPERATION, Attribute.SINGLELINE_TEXT);

        //
        // Settings for User Defined Tab 
        //
        PROJECT_CONFIG.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);
    }

    public String getProjectConfig() {
        return PROJECT_CONFIG.getValueAsText();
    }

    public static DmRq1IssueSW create() {
        return (DmRq1ElementCache.createIssueSW());
    }

    /**
     * Creates an I-SW. The I-SW is created in the chosen target project. The
     * new created I-SW is not stored in the database.
     *
     * @param project Project in which the new I-SW shall be created.
     * @param template
     * @return The new created I-SW.
     */
    public static DmRq1IssueSW createBasedOnProject(DmRq1SoftwareProject project, Rq1TemplateI template) {
        assert (project != null);

        //
        // Create the new i_SW
        //
        DmRq1IssueSW issueSW = create();

        //
        // Connect IssueSW with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
        issueSW.PROJECT.setElement(project);
        if (template != null) {
            template.execute(issueSW);
        }
        return (issueSW);
    }

    /**
     * Creates an new I-SW in the given project. The new I-SW is not stored in
     * the database.
     *
     * @param project Project in which the new I-SW shall be created.
     * @param template
     * @return The new created I-SW.
     */
    static public DmRq1IssueSW createBasedOnReferenceProject(DmRq1SwReferenceProject project, Rq1TemplateI template) {
        assert (project != null);
        //
        // Create the new i_SW
        //
        DmRq1IssueSW issueSW = create();

        issueSW.PROJECT.setElement(project);
        project.OPEN_ISSUE_SW.addElement(issueSW);

        if (template != null) {
            template.execute(issueSW);
        }
        return (issueSW);
    }

    /**
     * Creates an I-SW and connects it via an IRM to the given PST release. The
     * I-SW is created in the given target project. The new created I-SW and IRM
     * are not stored in the database.
     *
     * @param pst PST release to which the new I-SW shall be mapped via IRM.
     * @param targetProject Project in which the new I-SW shall be created.
     * @param template
     * @return The new created I-SW.
     */
    public static DmRq1IssueSW createBasedOnPst(DmRq1Pst pst, DmRq1SoftwareProject targetProject, Rq1TemplateI template) {

        assert (pst != null);
        assert (targetProject != null);

        //
        // Create the new i_SW and IRM
        //
        DmRq1IssueSW issueSW = create();
        DmRq1Irm_Pst_IssueSw irm;
        try {
            irm = DmRq1Irm_Pst_IssueSw.create(pst, issueSW);
        } catch (DmRq1MapExistsException ex) {
            Logger.getLogger(DmRq1IssueSW.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueSW.class.getName(), ex);
            return (null);
        }

        //
        // Take over content from release
        //
        issueSW.ACCOUNT_NUMBERS.setValue(pst.ACCOUNT_NUMBERS.getValue());
        irm.ACCOUNT_NUMBERS.setValue(pst.ACCOUNT_NUMBERS.getValue());
        irm.IS_PILOT.setValue(YesNoEmpty.YES);

        //
        // Connect Issue with IRM -> Already done in DmRq1Irm_PstOrCollection_IssueSw.create()
        //
//        issueSW.HAS_MAPPED_PST_RELEASES.addElement(irm, release);
//        irm.HAS_MAPPED_ISSUE.setElement(issueSW);
        //
        // Connect Release with IRM -> Already done in DmRq1Irm_PstOrCollection_IssueSw.create()
        //
//        release.MAPPED_ISSUES.addElement(irm, issueSW);
//        irm.HAS_MAPPED_RELEASE.setElement(release);
        //
        // Connect IssueSW with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
        issueSW.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueSW);
        }
        return (issueSW);
    }

    /**
     * Creates an I-SW as successor an given I-SW The I-SW is created in the
     * given target project. The new created I-SW is not stored in the database.
     *
     *
     * @param predecessor I-SW which shall be used as predecessor for the new
     * I-SW.
     * @param targetProject Project in which the new I-SW shall be created.
     * @param template
     * @return The new created I-SW.
     */
    public static DmRq1IssueSW createBasedOnPredecessor(DmRq1IssueSW predecessor, DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        //
        // Create the new i_SW
        //
        DmRq1IssueSW issueSW = create();

        //
        // Take over content from predecessor
        //
        issueSW.ACCOUNT_NUMBERS.setValue(predecessor.ACCOUNT_NUMBERS.getValue());
        issueSW.DESCRIPTION.setValue(predecessor.DESCRIPTION.getValue());
        issueSW.SCOPE.setValue(predecessor.SCOPE.getValue());
        issueSW.TITLE.setValue(predecessor.TITLE.getValue() + " Successor");

        //
        // Connect with predecessor
        //
        issueSW.PREDECESSOR.setElement(predecessor);
        predecessor.SUCCESSORS.addElement(issueSW);

        //
        // Connect IssueSW with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
        issueSW.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueSW);
        }
        return (issueSW);
    }

    public static DmRq1IssueSW createBasedOnProblem(DmRq1Problem dmProblem, DmRq1SoftwareProject targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {
        assert (dmProblem != null);
        assert (targetProject != null);

        //
        // Create the new i_SW
        //
        DmRq1IssueSW issueSW = create();

        //
        // Connect with problem
        //
        issueSW.PROBLEM.setElement(dmProblem);
        dmProblem.ISSUES.addElement(issueSW);

        //
        // Connect IssueSW with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
        issueSW.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueSW);
        }
        return (issueSW);
    }
    
    /**
     * Creates an new I-SW in the given project. The new I-SW is not stored in
     * the database.
     *
     * @param project Project in which the new I-SW shall be created.
     * @return The new created I-SW.
     */
    static public DmRq1IssueSW createBasedOnDevelopmentProject(DmRq1DevelopmentProject project, Rq1TemplateI template) {

        assert (project != null);

        DmRq1IssueSW issueSW = DmRq1ElementCache.createIssueSW();

        //
        // Take over content from project
        //
        issueSW.ACCOUNT_NUMBERS.setValue(project.ACCOUNT_NUMBERS.getValue());
        issueSW.SCOPE.setValue(Scope.INTERNAL);

        //
        // Connect project-issue
        //
        issueSW.PROJECT.setElement(project);
        project.OPEN_ISSUE_SW.addElement(issueSW);
        if (template != null) {
            template.execute(issueSW);
        }
        return (issueSW);
    }

    public static DmRq1IssueSW createDefectIssue(DmRq1IssueSW predecessor, DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        //
        // Create the new i_SW
        //
        DmRq1IssueSW defectIssue = create();

        //
        // Take over content from predecessor and connect to Project
        //
        defectIssue.CATEGORY.setValue(SoftwareIssueCategory.DEFECT);
        defectIssue.SCOPE.setValue(predecessor.SCOPE.getValue());
        defectIssue.TITLE.setValue(predecessor.TITLE.getValue() + " Bugfix");
        defectIssue.PROJECT.setElement(targetProject);

        //
        // Connect with predecessor
        //
        defectIssue.AFFECTED_ISSUE.setElement(predecessor);
        predecessor.IS_AFFECTED_BY_DEFECT_ISSUE.addElement(defectIssue);

        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        if (template != null) {
            template.execute(defectIssue);
        }
        return (defectIssue);
    }

    @Override
    public boolean save() {

        List<Rq1AttributeName> attributeList = new ArrayList<>();

        attributeList.add(Rq1IssueSW.ATTRIBUTE_LIFE_CYCLE_STATE);

        if ("Yes".equals(CATEGORY_EXHAUST.getValueAsText()) == true) {
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CATEGORY_EXHAUST);
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CODEX_SUB_CATEGORY);
        } else {
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CODEX_SUB_CATEGORY);
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CATEGORY_EXHAUST);
        }

        return (save(attributeList));
    }

    static public class ExistsAlreadyException extends Exception {

    }

    static public class WrongRelationException extends Exception {

    }

    /**
     * *
     * Adds the issueFD to the issueSW
     *
     * @param dmRq1IssueFD The to be added issue fd
     * @throws DataModel.Rq1.Records.DmRq1IssueSW.ExistsAlreadyException
     */
    public void addIssueFD(DmRq1IssueFD dmRq1IssueFD) throws ExistsAlreadyException {
        for (DmRq1IssueFD fd : this.CHILDREN.getElementList()) {
            if (fd.equals(dmRq1IssueFD)) {
                throw new ExistsAlreadyException();
            }
        }
        if ((DmRq1IssueSW) dmRq1IssueFD.PARENT.getElement() != null) {
            ((DmRq1IssueSW) dmRq1IssueFD.PARENT.getElement()).CHILDREN.removeElement(dmRq1IssueFD);
        }
        this.CHILDREN.addElement(dmRq1IssueFD);
        dmRq1IssueFD.PARENT.setElement(this);
    }

    public void addAffectedIssue(DmRq1IssueSW issue) throws ExistsAlreadyException, WrongRelationException {
        assert (issue != null);

        if (this.AFFECTED_ISSUE.getElement() == issue) {
            throw new ExistsAlreadyException();
        }
        if (this.CATEGORY.getValue() == SoftwareIssueCategory.DEFECT) {
            this.AFFECTED_ISSUE.setElement(issue);
            issue.IS_AFFECTED_BY_DEFECT_ISSUE.addElement(this);
        } else {
            throw new WrongRelationException();
        }
    }

    public void addIsAffectedByDefectIssue(DmRq1IssueSW issue) throws ExistsAlreadyException, WrongRelationException {
        assert (issue != null);
        if (issue.CATEGORY.getValue() == SoftwareIssueCategory.DEFECT) {
            for (DmRq1IssueSW is : IS_AFFECTED_BY_DEFECT_ISSUE.getElementList()) {
                if (is == issue) {
                    throw new ExistsAlreadyException();
                }
            }
            issue.AFFECTED_ISSUE.setElement(this);
            this.IS_AFFECTED_BY_DEFECT_ISSUE.addElement(issue);
        } else {
            throw new WrongRelationException();
        }

    }

    public void addAffectedRelease(DmRq1Release release) throws ExistsAlreadyException, WrongRelationException {
        assert (release != null);
        for (DmRq1Release pst : AFFECTED_RELEASES.getElementList()) {
            if (pst.equals(release)) {
                throw new ExistsAlreadyException();
            }
        }
        if (this.CATEGORY.getValue() == SoftwareIssueCategory.DEFECT) {
            this.AFFECTED_RELEASES.addElement(release);
        } else {
            throw new WrongRelationException();
        }
    }

    public void addSuccessor(DmRq1IssueSW issue) throws ExistsAlreadyException {
        assert (issue != null);
        for (DmRq1Issue is : SUCCESSORS.getElementList()) {
            if (is.equals(issue)) {
                throw new ExistsAlreadyException();
            }
        }
        if (this.PREDECESSOR.getElement() == issue) {
            throw new ExistsAlreadyException();
        }
        this.SUCCESSORS.addElement(issue);
        issue.PREDECESSOR.setElement(this);
    }

    public void addPredecessor(DmRq1IssueSW issue) throws ExistsAlreadyException {
        assert (issue != null);
        if (this.PREDECESSOR.getElement() != null && this.PREDECESSOR.getElement().equals(issue)) {
            throw new ExistsAlreadyException();
        }
        for (DmRq1Issue is : this.SUCCESSORS.getElementList()) {
            if (is == issue) {
                throw new ExistsAlreadyException();
            }
        }
        this.PREDECESSOR.setElement(issue);
        issue.SUCCESSORS.addElement(this);
    }

    /**
     * Support for sorting by external id.
     *
     * @param i2
     * @return
     */
    public int compareByExternalId(DmRq1IssueSW i2) {
        assert (i2 != null);

        String extId1 = EXTERNAL_ID.getValue();
        String extId2 = i2.EXTERNAL_ID.getValue();

        if ((extId1 != null) && (extId1.isEmpty() == false)) {
            if ((extId2 != null) && (extId2.isEmpty() == false)) {
                return (extId1.compareTo(extId2));
            } else {
                return (-1);
            }
        } else {
            if ((extId2 != null) && (extId2.isEmpty() == false)) {
                return (1);
            } else {
                return (getTitle().compareTo(i2.getTitle()));
            }
        }
    }

    /**
     * Support for sorting by Commercial Quotation ID.
     *
     * @param i2
     * @return
     */
    public int compareByCommercialQuotationID(DmRq1IssueSW i2) {
        assert (i2 != null);

        String comQuoId1 = COMMERCIAL_QUOTATION_ID.getValue();
        String comQuoId2 = i2.COMMERCIAL_QUOTATION_ID.getValue();

        if ((comQuoId1 != null) && (comQuoId1.isEmpty() == false)) {
            if ((comQuoId2 != null) && (comQuoId2.isEmpty() == false)) {
                return (comQuoId1.compareTo(comQuoId2));
            } else {
                return (-1);
            }
        } else {
            if ((comQuoId2 != null) && (comQuoId2.isEmpty() == false)) {
                return (1);
            } else {
                return (getTitle().compareTo(i2.getTitle()));
            }
        }
    }

    // For JLR script below method added. 
    public static DmRq1IssueSW create(DmRq1SoftwareProject targetProject) {

        assert (targetProject != null);

        //
        // Create the new i_SW and IRM
        //
        DmRq1IssueSW issueSW = DmRq1ElementCache.createIssueSW();

        //
        // Connect IssueSW with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
        issueSW.PROJECT.setElement(targetProject);

        return (issueSW);
    }

    /**
     * Returns all I-SW which belong to a project with the given customer.
     *
     * @param customerGroup Group of the customer.
     * @return
     */
    static public List<DmRq1IssueSW> get_ISW_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);
        assert (project != null);

        List<DmRq1IssueSW> result = new ArrayList<>();
        for (Rq1IssueSW rq1IssueSW : Rq1IssueSW.get_ISW_on_Project_for_Customer((Rq1Project) project.getRq1Record(), customerGroup)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1IssueSW);
            if (dmElement instanceof DmRq1IssueSW) {
                result.add((DmRq1IssueSW) dmElement);
            }
        }

        return (result);
    }

    /**
     * Splits the content of field External States into a list of external
     * states.
     *
     * @return List of states contained in field External State.
     */
    public List<String> getExternalStates() {
        List<String> result = new ArrayList<>();
        String[] states = this.EXTERNAL_STATE.getValueAsText().split("\n");
        for (String state : states) {
            if (state.isEmpty() == false) {
                result.add(state);
            }
        }
        if (result.isEmpty() == true) {
            result.add("<Empty>");
        }
        return (result);
    }

    /**
     * Calculates the total estimated effort. This contains:
     *
     * - Estimated Effort of I-SW.
     *
     * - Estimated Effort of all workitems on the I-SW.
     *
     * @return The Total Estimated Effort as integer.
     */
    public int calculateTotalEstimatedEffort() {
        int totalEffort = 0;
        //totalEffort += parseIntNoException(this.ESTIMATED_EFFORT.getValueAsText());
        for (DmRq1WorkItem isw_wi : this.WORKITEMS.getElementList()) {
            if (isw_wi.isCanceled() == false) {
                totalEffort += parseIntNoException(isw_wi.EFFORT_ESTIMATION.getValueAsText());
            }
        }
        return (totalEffort);
    }

    /**
     * Parses a number stored as a string to an integer.
     *
     * @param value, the string to be parsed to an integer.
     * @return Integer
     */
    static int parseIntNoException(String value) {
        int parI = 0;
        try {
            parI = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            parI = 0;
        }
        return (parI);
    }

    public InternalRank getInternalRank() throws InternalRank.BuildException {
        if (internalRank == null) {
            internalRank = InternalRank.buildForRecord(getRq1Id(), "", "");
        }
        return (internalRank);
    }

    public void setInternalRank(InternalRank internalRank) {
        this.internalRank = internalRank;
    }

    public String getFlowEstimationSheetName() {
        return FLOW_EST_SHEET_NAME.getValueAsText();
    }
    
    public String getCrpEstimationSheetName() {
        return CRP_EST_SHEET_NAME.getValueAsText();
    }
}
