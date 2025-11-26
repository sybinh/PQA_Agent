/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.DmValueFieldI_Text;
import DataModel.Flow.DmFlowIssue;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FlowCopy;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.InternalRank;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataModel.Flow.Util.ClearFlowTags;
import DataModel.Monitoring.Rule_IssueFD_Close;
import DataModel.Monitoring.Rule_IssueFD_Commit;
import DataModel.Monitoring.Rule_IssueFD_Conflicted;
import DataModel.Monitoring.Rule_IssueFD_DefectFlowModel;
import DataModel.Monitoring.Rule_IssueFD_Evaluation;
import DataModel.Monitoring.Rule_IssueFD_Implementation;
import DataModel.Monitoring.Rule_IssueFD_NotCommited;
import DataModel.Rq1.Requirements.DmRq1Field_DoorsRequirementsOnIssueFromTables;
import DataModel.Rq1.Requirements.DmRq1LinkToRequirement_Type;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_IssueFD_Pilot;
import DataModel.Rq1.Monitoring.Rule_IssueFD_WithoutLinkToBc;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnIssue;
import Ipe.Annotations.IpeFactoryConstructor;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1Project;
import Rq1Cache.Records.Rq1SoftwareIssue;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIssue;
import static Rq1Data.Enumerations.DevelopmentMethod.CHANGE_BASED_DEVELOPMENT;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Templates.Rq1TemplateI;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmRq1IssueFD extends DmRq1SoftwareIssue implements DmFlowIssue, FlowCopy, Comparable<DmRq1IssueFD> {

    final public DmRq1Field_Enumeration SYNC;
    final public DmRq1Field_Text LINKS;

    final public DmRq1Field_Enumeration APPROVAL;
    final public DmRq1Field_Date COMMERCIAL_GO_REQUIRED_BY;
    final public DmRq1Field_Text DEFECT_CLASSIFICATION;
    final public DmRq1Field_Text REQUIREMENTS_REVIEW_CHANGE_COMMENT;
    final public DmRq1Field_Text DEVELOPMENT_METHOD_COMMENT;

    final public DmRq1Field_Text CSTCOMMENT;

    final public DmRq1Field_Reference<DmRq1Issue> PARENT;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_Bc_IssueFd, DmRq1Bc> MAPPED_BC;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_Fc_IssueFd, DmRq1Fc> MAPPED_FC;

    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L3_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L3_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L3_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L4_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L4_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L4_REMOVED_REQUIREMENT_TABLE;

    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> SC_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> SC_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> SC_AFFECTED_ARTEFACT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> SC_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_AFFECTED_ARTEFACT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_REMOVED_REQUIREMENT_TABLE;

    final public DmRq1Field_DoorsRequirementsOnIssueFromTables MAPPED_DOORS_REQUIREMENTS;
    final public DmRq1Field_AllRequirementsOnIssue MAPPED_REQUIREMENTS;

    final public DmRq1Field_Enumeration FLOW_KIT_STATUS;
    final public DmRq1Field_Text FLOW_RANK;
    final public DmRq1Field_Text FLOW_VERSION;
    final public DmRq1Field_Text FLOW_R_DATE;
    final public DmRq1Field_Text FLOW_GROUP;
    final public DmRq1Field_Text FLOW_INTERNAL_RANK;
    final public DmRq1Field_Text FLOW_CLUSTERNAME;
    final public DmRq1Field_Text FLOW_CLUSTERID;
    final public DmRq1Field_Text FLOW_R_EFFORT;
    final public DmRq1Field_Text FLOW_NO_OF_DEVELOPERS;
    final public DmRq1Field_Date FLOW_TO_RED_DATE;
    final public DmRq1Field_Enumeration FLOW_SIZE;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    public InternalRank internalRank = null;
    final public DmRq1Field_Enumeration FLOW_STATUS;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_Text FLOW_EXC_BOARD;
    final public DmRq1Field_Text FLOW_INC_AS_TASK;
    final public DmRq1Field_Text FLOW_EST_SHEET_NAME;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;

    //
    // Fields for ESH
    //
    final public DmValueFieldI_Text ESH_SHORT_DESCRIPTION;

    //
    // Fields for ECB
    //
    final public DmRq1Field_Text ECB_DIDINFO;
    final public DmRq1Field_Enumeration ECB_STANDARDJOB_STATE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1IssueFD(Rq1IssueFD rq1IssueFD) {
        super("I-FD", rq1IssueFD);

        //
        // Create and add fields
        //
        addField(SYNC = new DmRq1Field_Enumeration(this, rq1IssueFD.SYNC, "Sync"));
        addField(LINKS = new DmRq1Field_Text(this, rq1IssueFD.LINKS, "Links"));

        addField(CSTCOMMENT = new DmRq1Field_Text(this, rq1IssueFD.CSTCOMMENT, "CST Comment"));

        addField(REQUIREMENTS_REVIEW_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1IssueFD.REQUIREMENTS_REVIEW_CHANGE_COMMENT, "Requirements Review Change Comment"));
        REQUIREMENTS_REVIEW_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        REQUIREMENTS_REVIEW_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.SINGLELINE_TEXT);

        addField(DEVELOPMENT_METHOD_COMMENT = new DmRq1Field_Text(this, rq1IssueFD.DEVELOPMENT_METHOD_COMMENT, "Development Method Comment"));
        DEVELOPMENT_METHOD_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        DEVELOPMENT_METHOD_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.SINGLELINE_TEXT);

        addField(APPROVAL = new DmRq1Field_Enumeration(this, rq1IssueFD.APPROVAL, "Approval"));
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        addField(DEFECT_CLASSIFICATION = new DmRq1Field_Text(this, rq1IssueFD.DEFECT_CLASSIFICATION, "Defect Classification"));
        addField(COMMERCIAL_GO_REQUIRED_BY = new DmRq1Field_Date(this, rq1IssueFD.COMMERCIAL_GO_REQUIRED_BY, "Commercial GO Required By"));
        COMMERCIAL_GO_REQUIRED_BY.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        COMMERCIAL_GO_REQUIRED_BY.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(PARENT = new DmRq1Field_Reference<>(this, rq1IssueFD.HAS_PARENT, "Parent"));
        addField(MAPPED_BC = new DmRq1Field_MappedReferenceList<>(this, rq1IssueFD.HAS_MAPPED_BC, "Mapped BC"));
        addField(MAPPED_FC = new DmRq1Field_MappedReferenceList<>(this, rq1IssueFD.HAS_MAPPED_FC, "Mapped FC"));

        addField(L3_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.L3_PLANNED_REQUIREMENTS, "L3 Planned Requirements "));  // Blank at the end of the name to differ it from the list below.
        addField(L3_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.L3_IMPACTED_REQUIREMENTS, "L3 Impacted Requirements "));
        addField(L3_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.L3_REMOVED_REQUIREMENTS, "L3 Removed Requirements "));
        addField(L4_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.L4_PLANNED_REQUIREMENTS, "L4 Planned Requirements "));
        addField(L4_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.L4_IMPACTED_REQUIREMENTS, "L4 Impacted Requirements "));
        addField(L4_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.L4_REMOVED_REQUIREMENTS, "L4 Removed Requirements "));

        addField(MAPPED_DOORS_REQUIREMENTS = new DmRq1Field_DoorsRequirementsOnIssueFromTables(this, "Mapped Doors Requirements"));
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L3_PLANNED, L3_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L3_IMPACTED, L3_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L3_REMOVED, L3_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L4_PLANNED, L4_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L4_IMPACTED, L4_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L4_REMOVED, L4_REMOVED_REQUIREMENT_TABLE);
        addField(MAPPED_REQUIREMENTS = new DmRq1Field_AllRequirementsOnIssue(this, MAPPED_DOORS_REQUIREMENTS, MAPPED_DNG_REQUIREMENTS, "Mapped Requirements"));

        addField(SC_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.SC_IMPACTED_REQUIREMENTS, "SC Impacted Requirements "));
        addField(SC_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.SC_PLANNED_REQUIREMENTS, "SC Planned Requirements "));
        addField(SC_AFFECTED_ARTEFACT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.SC_AFFECTED_ARTEFACT, "SC Affected Artefact "));
        addField(SC_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.SC_REMOVED_REQUIREMENTS, "SC Removed Requirements "));
        addField(BC_FC_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.BC_FC_PLANNED_REQUIREMENTS, "BC-FC Planned Requirements "));
        addField(BC_FC_AFFECTED_ARTEFACT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.BC_FC_AFFECTED_ARTEFACT, "BC-FC Affected Artefact "));
        addField(BC_FC_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.BC_FC_IMPACTED_REQUIREMENTS, "BC-FC Impacted Requirements "));
        addField(BC_FC_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1IssueFD.BC_FC_REMOVED_REQUIREMENTS, "BC-FC Removed Requirements "));

        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.SC_PLANNED, SC_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.SC_AFFECTED, SC_AFFECTED_ARTEFACT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.SC_IMPACTED, SC_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.SC_REMOVED, SC_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.BC_FC_PLANNED, BC_FC_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.BC_FC_AFFECTED, BC_FC_AFFECTED_ARTEFACT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.BC_FC_IMPACTED, BC_FC_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.BC_FC_REMOVED, BC_FC_REMOVED_REQUIREMENT_TABLE);

        //
        // Fields for flow framework
        //
        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, rq1IssueFD.FLOW_KIT_STATUS, "Full-Kit-Status"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, rq1IssueFD.FLOW_STATUS, "Flow Task Status"));
        addField(FLOW_RANK = new DmRq1Field_Text(this, rq1IssueFD.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, rq1IssueFD.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, rq1IssueFD.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, rq1IssueFD.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, rq1IssueFD.FLOW_CLUSTERNAME, "Flow Cluster Name"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, rq1IssueFD.FLOW_GROUP, "Groups"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, rq1IssueFD.FLOW_CLUSTERID, "Flow Cluster ID"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, rq1IssueFD.FLOW_SIZE, "Size"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, rq1IssueFD.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, rq1IssueFD.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, rq1IssueFD.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, rq1IssueFD.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, rq1IssueFD.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_EXC_BOARD = new DmRq1Field_Text(this, rq1IssueFD.FLOW_EXC_BOARD, "Exclude from Board"));
        addField(FLOW_INC_AS_TASK = new DmRq1Field_Text(this, rq1IssueFD.FLOW_INC_AS_TASK, "Flow include as task in list"));
        addField(FLOW_EST_SHEET_NAME = new DmRq1Field_Text(this, rq1IssueFD.FLOW_EST_SHEET_NAME, "Flow estimation sheet"));

        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, rq1IssueFD.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, rq1IssueFD.TO_RED_DATE, "To RED Date"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, rq1IssueFD.TARGET_DATE, "Planned-Date"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, rq1IssueFD.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, rq1IssueFD.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, rq1IssueFD.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(rq1IssueFD.FLOW_BLOCKER_TABLE, rq1IssueFD.FLOW_SUBTASK_TABLE, "Blocker with Subtask Title"));

        ALGORITHM_TO_REVIEW.addAlternativeField(rq1IssueFD.ALG2RV);
        ALGORITHM_TO_REVIEW_COMMENT.addAlternativeField(rq1IssueFD.ALG2RV_COMMENT);

        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.I_FD_PLANNING));
        addRule(new Rule_IssueFD_Evaluation(this));
        addRule(new Rule_IssueFD_Close(this));
        addRule(new Rule_IssueFD_Implementation(this));
        addRule(new Rule_IssueFD_Commit(this));
        addRule(new Rule_IssueFD_DefectFlowModel(this));
        addRule(new Rule_IssueFD_Conflicted(this));
        addRule(new Rule_IssueFD_NotCommited(this));
        addRule(new Rule_IssueFD_WithoutLinkToBc(this));

        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.I_FD_PILOT));
        addRule(new Rule_IssueFD_Pilot(this));

        //
        // Fields for ECB
        //
        addField(ECB_DIDINFO = new DmRq1Field_Text(this, rq1IssueFD.ECB_DIDINFO, "ECB: DIDinfo"));
        ECB_DIDINFO.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        ECB_DIDINFO.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_DIDINFO.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        ECB_DIDINFO.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        
        addField(ECB_STANDARDJOB_STATE = new DmRq1Field_Enumeration(this, rq1IssueFD.ECB_STANDARDJOB_STATE, "ECB: Standardjobs"));
        ECB_STANDARDJOB_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_STANDARDJOB_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        //
        // Fields for ESH
        //
        addField(ESH_SHORT_DESCRIPTION = new DmRq1Field_Text(this, rq1IssueFD.ESH_SHORT_DESCRIPTION, "ESH: Short Description (sDesc)"));
        ESH_SHORT_DESCRIPTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
    }

    /**
     * Creates an I-FD. The I-FD is created in the chosen target project. The
     * new created I-FD is not stored in the database.
     *
     * @param project Project in which the new I-FD shall be created.
     * @param template
     * @return The new created I-FD.
     */
    public static DmRq1IssueFD createBasedOnProject(DmRq1SoftwareProject project, Rq1TemplateI template) {
        assert (project != null);

        //
        // Create the new I-FD
        //
        DmRq1IssueFD issueFD = DmRq1ElementCache.createIssueFD();

        issueFD.SCOPE.setValue(Scope.INTERNAL);

        //
        // Connect IssueFD with Project
        //
        issueFD.PROJECT.setElement(project);
        if (template != null) {
            template.execute(issueFD);
        }
        return (issueFD);
    }

    /**
     * Creates an new I-FD as child of the given I-SW. The new I-FD is created
     * in the given project and connected to the I-SW in a parent/child
     * relation. The new I-FD is not stored in the database.
     *
     * @param issueSW I-SW which shall be the parent of the I-FD
     * @param project Project in which the new I-SW shall be created.
     * @param template
     * @return The new created I-FD.
     */
    static public DmRq1IssueFD createBasedOnParent(DmRq1IssueSW issueSW, DmRq1SoftwareProject project, Rq1TemplateI template) {

        assert (issueSW != null);
        assert (project != null);

        DmRq1IssueFD issueFD = DmRq1ElementCache.createIssueFD();

        //
        // Take over content from parent
        //
        issueFD.ACCOUNT_NUMBERS.setValue(issueSW.ACCOUNT_NUMBERS.getValue());
        issueFD.DESCRIPTION.setValue(issueSW.DESCRIPTION.getValue());
        issueFD.INTERNAL_COMMENT.setValue(issueSW.INTERNAL_COMMENT.getValue());
        issueFD.SCOPE.setValue(Scope.INTERNAL);
        issueFD.TITLE.setValue(issueSW.TITLE.getValue());
        issueFD.ASIL_CLASSIFICATION.setValue(issueSW.ASIL_CLASSIFICATION.getValue());
        issueFD.FMEA_STATE.setValue(issueSW.FMEA_STATE.getValue());
        issueFD.REQUIREMENTS_REVIEW.setValue(issueSW.REQUIREMENTS_REVIEW.getValue());
        issueFD.SPECIFICATION_REVIEW.setValue(issueSW.SPECIFICATION_REVIEW.getValue());
        issueFD.DEVELOPMENT_METHOD.setValue(issueSW.DEVELOPMENT_METHOD.getValue());
        issueFD.CATEGORY.setValue(issueSW.CATEGORY.getValue());

//        issueFD.DEFECT_DETECTION_DATE.setValue(issueSW.DEFECT_DETECTION_DATE.getValue());
//        issueFD.DEFECT_DETECTION_LOCATION.setValue(issueSW.DEFECT_DETECTION_LOCATION.getValue());
        issueFD.DEFECT_DETECTION_ORGANISATION.setValue(issueSW.DEFECT_DETECTION_ORGANISATION.getValue());
//        issueFD.DEFECT_DETECTION_PROCESS.setValue(issueSW.DEFECT_DETECTION_PROCESS.getValue());
//        issueFD.DEFECT_DETECTION_PROCESS_COMMENT.setValue(issueSW.DEFECT_DETECTION_PROCESS_COMMENT.getValue());

        //
        // Connect parent-child
        //
        issueSW.CHILDREN.addElement(issueFD);
        issueFD.PARENT.setElement(issueSW);

        //
        // Connect project-issue
        //
        issueFD.PROJECT.setElement(project);
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which e.g. for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
//        project.ISSUES.addElement(issueFD);
        if (template != null) {
            template.execute(issueFD);
        }
        return (issueFD);
    }

    /**
     * Creates an new I-FD in the given project. The new I-FD is not stored in
     * the database.
     *
     * @param project Project in which the new I-SW shall be created.
     * @return The new created I-FD.
     */
    static public DmRq1IssueFD createBasedOnDevelopmentProject(DmRq1DevelopmentProject project, Rq1TemplateI template) {

        assert (project != null);

        DmRq1IssueFD issueFD = DmRq1ElementCache.createIssueFD();

        //
        // Take over content from project
        //
        issueFD.ACCOUNT_NUMBERS.setValue(project.ACCOUNT_NUMBERS.getValue());
        issueFD.SCOPE.setValue(Scope.INTERNAL);

        //
        // Connect project-issue
        //
        issueFD.PROJECT.setElement(project);
        project.OPEN_ISSUE_FD.addElement(issueFD);
        if (template != null) {
            template.execute(issueFD);
        }
        return (issueFD);
    }

    /**
     * Creates an I-FD as successor an given I-FD The I-FD is created in the
     * given target project. The new created I-FD is not stored in the database.
     *
     *
     * @param predecessor I-FD which shall be used as predecessor for the new
     * I-FD.
     * @param targetProject Project in which the new I-SW shall be created.
     * @return The new created I-FD.
     */
    public static DmRq1IssueFD createBasedOnPredecessor(DmRq1IssueFD predecessor, DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        //
        // Create the new i_FD
        //
        DmRq1IssueFD issueFD = DmRq1ElementCache.createIssueFD();

        //
        // Take over content from predecessor
        //
        issueFD.ACCOUNT_NUMBERS.setValue(predecessor.ACCOUNT_NUMBERS.getValue());
        issueFD.DESCRIPTION.setValue(predecessor.DESCRIPTION.getValue());
        issueFD.SCOPE.setValue(predecessor.SCOPE.getValue());
        issueFD.TITLE.setValue(predecessor.TITLE.getValue() + " Successor");
//        issueFD.PARENT.setElement(predecessor.PARENT.getElement());       ACHTUNG: Das gibt Probleme wenn der I-SW nicht mehr im Status NEW ist !!!

        //
        // Connect with predecessor
        //
        issueFD.PREDECESSOR.setElement(predecessor);
        predecessor.SUCCESSORS.addElement(issueFD);

        //
        // Connect IssueSW with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
        issueFD.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueFD);
        }
        return (issueFD);
    }

    @Override
    public boolean save() {

        List<Rq1AttributeName> attributeList = new ArrayList<>();

        if (DEVELOPMENT_METHOD.getValue() == CHANGE_BASED_DEVELOPMENT) {
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_DEVELOPMENT_METHOD);
        }

        if ("Yes".equals(CATEGORY_EXHAUST.getValueAsText()) == true) {
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CATEGORY_EXHAUST);
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CODEX_SUB_CATEGORY);
        } else {
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CODEX_SUB_CATEGORY);
            attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_CATEGORY_EXHAUST);
        }

        attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_ALGORITHM_TO_REVIEW);
        attributeList.add(Rq1SoftwareIssue.ATTRIBUTE_ALGORITHM_TO_REVIEW_COMMENT_FIELD);

        return (save(attributeList));
    }

    public void addSuccessor(DmRq1IssueFD issue) throws ExistsAlreadyException {
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

    public void addPredecessor(DmRq1IssueFD issue) throws ExistsAlreadyException {
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

    @Override
    public String getRank() {
        return (FLOW_RANK.getValueAsText());
    }

    @Override
    public String getFlowVersion() {
        return (FLOW_VERSION.getValueAsText());
    }

    @Override
    public String getRemainingEffort() {
        return (FLOW_R_EFFORT.getValueAsText());
    }

    @Override
    public String getRequestedDate() {
        return (FLOW_R_DATE.getValueAsText());
    }

    @Override
    public FullKitStatus getStatus() {
        return ((FullKitStatus) FLOW_KIT_STATUS.getValue());
    }

    @Override
    public TaskStatus getTaskStatus() {
        return ((TaskStatus) FLOW_STATUS.getValue());
    }

    @Override
    public String getClusterName() {
        return FLOW_CLUSTERNAME.getValueAsText();
    }

    public String getFlowEstimationSheetName() {
        return FLOW_EST_SHEET_NAME.getValueAsText();
    }

    @Override
    public FullKitSize getSize() {
        return ((FullKitSize) FLOW_SIZE.getValue());
    }

    @Override
    public InternalRank getInternalRank() throws InternalRank.BuildException {
        if (internalRank == null) {
            internalRank = InternalRank.buildForRecord(getRq1Id(), FLOW_RANK.getValue(), FLOW_INTERNAL_RANK.getValue());
            FLOW_INTERNAL_RANK.setValue(internalRank.toString());
        }
        return (internalRank);
    }

    @Override
    public InternalRank rankFirst(InternalRank currentFirst) throws InternalRank.BuildException {
        assert (currentFirst != null);

        internalRank = InternalRank.buildFirst(currentFirst);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    @Override
    public InternalRank rankBetween(InternalRank before, InternalRank after) throws InternalRank.BuildException {
        assert (before != null);
        assert (after != null);

        internalRank = InternalRank.buildBetween(before, after);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    @Override
    public InternalRank rankLast(InternalRank currentLast) throws InternalRank.BuildException {
        assert (currentLast != null);

        internalRank = InternalRank.buildLast(currentLast);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    public void setExistingIFdRank(InternalRank ifdRank) {
        internalRank = ifdRank;
        FLOW_INTERNAL_RANK.setValue(ifdRank.toString());
    }

    @Override
    public String getClusterID() {
        return FLOW_CLUSTERID.getValueAsText();
    }

    @Override
    public String getGroupId() {
        return FLOW_GROUP.getValueAsText();
    }

    public String getSwimlaneHeading() {
        return FLOW_BOARD_SWIMLANE_HEADING.getValueAsText();
    }

    public KingState getKingState() {
        return ((KingState) KING_STATE.getValue());
    }

    public ExpertState getExpertState() {
        return ((ExpertState) EXPERT_STATE.getValue());
    }

    @Override
    public void reload() {
        internalRank = null;
        super.reload();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DmRq1IssueFD cloned = (DmRq1IssueFD) super.clone();
        DmRq1Field_MappedReferenceList list = (DmRq1Field_MappedReferenceList) cloned.HAS_MAPPED_RELEASES.clone();
        try {
            final Field[] fields = cloned.getClass().getFields();
            for (Field field : fields) {
                if (field.getName().equals("HAS_MAPPED_RELEASES")) {
                    field.setAccessible(true);

                    field.set(cloned, list);

                    break;
                }
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DmRq1IssueFD.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueFD.class.getName(), ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DmRq1IssueFD.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueFD.class.getName(), ex);
        }

        return cloned;
    }

    @Override
    public EcvDate getToRedDate() {
        return FLOW_TO_RED_DATE.getValue();
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }

    public String getExpertAvailEffort() {
        return FLOW_EXP_AVAL_EFFORT.getValueAsText();
    }

    @Override
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    public String getExcFromBoard() {
        return FLOW_EXC_BOARD.getValueAsText();
    }

    public String getIncAsTask() {
        return FLOW_INC_AS_TASK.getValueAsText();
    }

    /**
     * Returns all I-FD which belong to a I-SW which belongs to a project with
     * the given customer.
     *
     * @param customerGroup Group of the customer.
     * @return
     */
    static public List<DmRq1IssueFD> get_IFD_on_ISW_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);
        assert (project != null);

        List<DmRq1IssueFD> result = new ArrayList<>();
        for (Rq1IssueFD rq1IssueFD : Rq1IssueFD.get_IFD_from_Project_on_ISW_on_Project_for_Customer((Rq1Project) project.getRq1Record(), customerGroup)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1IssueFD);
            if (dmElement instanceof DmRq1IssueFD) {
                result.add((DmRq1IssueFD) dmElement);
            }
        }

        return (result);
    }

    @Override
    public int compareTo(DmRq1IssueFD o) {
        assert (o != null);

        return ((this.getId()).compareTo(o.getId()));
    }

    @Override
    public void forward(DmRq1Project project, DmRq1User newAssignee) {
        ClearFlowTags.clearTags(project, this);
        super.forward(project, newAssignee);
    }

    /**
     * Calculates the total estimated effort. This contains:
     *
     * - Estimated Effort of I-FD.
     *
     * - Estimated Effort of all workitems on the I-FD.
     *
     * @return The Total Estimated Effort as integer.
     */
    public int calculateTotalEstimatedEffort() {
        int totalEffort = 0;
        //totalEffort += parseIntNoException(this.ESTIMATED_EFFORT.getValueAsText());
        for (DmRq1WorkItem ifd_wi : this.WORKITEMS.getElementList()) {
            if (ifd_wi.isCanceled() == false) {
                totalEffort += parseIntNoException(ifd_wi.EFFORT_ESTIMATION.getValueAsText());
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
    
    /**
     * Creates Issue-FD from Problem record
     * @param dmProblem parent problem from which the I-FD is created
     * @param targetProject
     * @param template
     * @param suffixSet
     * @param suffixSeparator
     * @return 
     */
    public static DmRq1IssueFD createBasedOnProblem(DmRq1Problem dmProblem, DmRq1SoftwareProject targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {
        assert (dmProblem != null);
        assert (targetProject != null);

        //
        // Create the new i_SW
        //
        DmRq1IssueFD issueFD = create();

        //
        // Connect with problem
        //
        issueFD.PROBLEM.setElement(dmProblem);
        dmProblem.ISSUES.addElement(issueFD);

        //
        // Connect IssueFD with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is established anyway when the issue is stored in RQ1.
        //
        issueFD.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueFD);
        }
        return (issueFD);
    }
    
    private static DmRq1IssueFD create() {
        return (DmRq1ElementCache.createIssueFD());
    }
}
