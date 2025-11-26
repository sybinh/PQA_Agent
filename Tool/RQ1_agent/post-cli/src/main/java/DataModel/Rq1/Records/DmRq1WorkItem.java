/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_Rq1AssignedRecord;
import DataModel.CRP.DmCrpIssue;
import DataModel.DmElementField_FilteredByClass;
import DataModel.DmFieldI;
import DataModel.DmFieldI.Attribute;
import DataModel.Flow.DmFlowIssue;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.InternalRank;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataModel.Flow.Util.ClearFlowTags;
import DataModel.Monitoring.Rule_Workitem_Close;
import DataModel.Monitoring.Rule_Workitem_Conflicted;
import DataModel.Monitoring.Rule_Workitem_PlannedDate;
import DataModel.Monitoring.Rule_Workitem_Started;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle;
import DataModel.Rq1.Fields.DmRq1Field_MilestoneOnWorkitem;
import DataModel.Rq1.Fields.DmRq1Field_Number;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList_Writeable;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Fields.DmRq1Field_Text_Product;
import Rq1Cache.Records.Rq1Project;
import Rq1Cache.Records.Rq1WorkItem;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_CrpGantt;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Enumerations.LifeCycleState_WorkItem;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import UiSupport.BulkOperationRq1UserI;
import UiSupport.EcvUserMessage;
import UiSupport.RoadmapRowI;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import util.EcvDate;
import util.EcvEnumeration;
import util.MailSendable;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1WorkItem extends DmRq1AssignedRecord implements MailSendable, BulkOperationRq1UserI, RoadmapRowI, DmFlowIssue, DmCrpIssue {

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration DOMAIN;
    final public DmRq1Field_Number EFFORT_ESTIMATION;
    final public DmRq1Field_Text ESTIMATION_COMMENT;
    final public DmRq1Field_Enumeration LIFE_CYCLE_STATE;
    final public DmRq1Field_Enumeration TYPE;
    final public DmRq1Field_Date ACTUAL_DATE;
    final public DmRq1Field_Date PLANNED_DATE;
    final public DmRq1Field_Enumeration PLANNING_METHOD;
    final public DmRq1Field_Text_Product PRODUCT;
    final public DmRq1Field_Text PRIORITY;
    final public DmRq1Field_Reference<DmRq1User> REQUESTER;
    final public DmRq1Field_Text REQUESTER_FULLNAME;
    final public DmRq1Field_Text REQUESTER_EMAIL;
    final public DmRq1Field_Text REQUESTER_LOGIN_NAME;
    final public DmRq1Field_Date START_DATE;
    final public DmRq1Field_Text STATE;
    final public DmRq1Field_Enumeration SUBCATEGORY;

    final public DmRq1Field_ReferenceList_Writeable<DmRq1WorkItem> PREDECESSORS;
    final public DmRq1Field_ReferenceList_Writeable<DmRq1WorkItem> SUCCESSORS;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> DEPENDS_ON_WORKITEMS;
    final public DmRq1Field_Reference<DmRq1Issue> ISSUE;
    final public DmRq1Field_Reference<DmRq1ReleaseRecord> RELEASE_RECORD;
    final public DmElementField_FilteredByClass<DmRq1ReleaseRecord, DmRq1Release> RELEASE;
    final public DmElementField_FilteredByClass<DmRq1ReleaseRecord, DmRq1Milestone> MILESTONE;
    final public DmRq1Field_MilestoneOnWorkitem MILESTONE_FROM_PROJECT;
    final public DmRq1Field_Reference<DmRq1Problem> PROBLEM;

    final public DmRq1Field_Enumeration TASKMARKET_PRIORITY;
    final public DmRq1Field_Text TASKMARKET_BONUS;
    final public DmRq1Field_Enumeration TASKMARKET_CATEGORY;
    final public DmRq1Field_Text TASKMARKET_PROCESS_AREA;
    final public DmRq1Field_Text TASKMARKET_ROI;
    final public DmRq1Field_Text TASKMARKET_INVEST_OF_ROI;

    final public DmRq1Field_Text GPM_SOURCE;
    final public DmRq1Field_Text GPM_PLIB_VERSION;
    final public DmRq1Field_Text GPM_TASK_ID;
    final public DmRq1Field_Text GPM_MILESTONE_ID;
    final public DmRq1Field_Text GPM_PROJECT_ID;

    // Members for the flow model
    //
    final public DmRq1Field_Text FLOW_GROUP;
    final public DmRq1Field_Text FLOW_IRM_GROUP;
    final public DmRq1Field_Enumeration FLOW_KIT_STATUS;
    final public DmRq1Field_Text FLOW_RANK;
    final public DmRq1Field_Text FLOW_VERSION;
    final public DmRq1Field_Text FLOW_R_DATE;
    final public DmRq1Field_Text FLOW_INTERNAL_RANK;
    final public DmRq1Field_Text FLOW_CLUSTERNAME;
    final public DmRq1Field_Text FLOW_CLUSTERID;
    final public DmRq1Field_Date FLOW_TO_RED_DATE;
    final public DmRq1Field_Text FLOW_R_EFFORT;
    final public DmRq1Field_Text FLOW_NO_OF_DEVELOPERS;
    final public DmRq1Field_Text FLOW_WI_TASK;
    final public DmRq1Field_Text FLOW_RELATED_WI;
    final public DmRq1Field_Enumeration FLOW_SIZE;
    public InternalRank internalRank = null;
    final public DmRq1Field_Enumeration FLOW_STATUS;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    final public DmRq1Field_Text FLOW_EXC_BOARD;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Text PARENT_SWIMLANE;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;

    // Members for the CRP model
    //
    final public DmRq1Field_Text CRP_RANK;
    final public DmRq1Field_Text CRP_INTERNAL_RANK;
    final public DmRq1Field_Text CRP_CLUSTERNAME;
    final public DmRq1Field_Text CRP_CLUSTERID;
    final public DmRq1Field_Text CRP_R_EFFORT;
    final public DmRq1Field_Enumeration CRP_KIT_STATUS;
    final public DmRq1Field_Table<Rq1XmlTable_CrpGantt> CRP_GANTT;
    final public DmRq1Field_Text CRP_VERSION;
    public InternalRank crpInternalRank = null;
    final public DmRq1Field_Text CRP_PRED_OFFSET;

    final public DmRq1Field_Enumeration SYNC;
    final public DmRq1Field_Text LINKS;

    final public DmRq1Field_Text Measure_Type1;
    final public DmRq1Field_Text Measure1;
    final public DmRq1Field_Text Measure_Type2;
    final public DmRq1Field_Text Measure2;
    final public DmRq1Field_Text Measure_Type3;
    final public DmRq1Field_Text Measure3;
    final public DmRq1Field_Text Measure_Type4;
    final public DmRq1Field_Text Measure4;
    final public DmRq1Field_Text Measure_Type5;
    final public DmRq1Field_Text Measure5;
    final public DmRq1Field_Text Measure_Type6;
    final public DmRq1Field_Text Measure6;
    final public DmRq1Field_Text Measure_Type7;
    final public DmRq1Field_Text Measure7;
    final public DmRq1Field_Text DECISION_STOP;
    final public DmRq1Field_Text DECISION_REMOVE;
    final public DmRq1Field_Text DECISION_CONTINUE;
    final public DmRq1Field_Text EXTERNAL_SUMMARY;

    public DmRq1WorkItem(String subjectType, Rq1WorkItem rq1WorkItem) {
        super(subjectType, rq1WorkItem);

        //
        // Create and add fields
        //
        addField(Measure_Type1 = new DmRq1Field_Text(rq1WorkItem.MEASURETYPE1, "Measure Type 1"));
        addField(Measure1 = new DmRq1Field_Text(rq1WorkItem.MEASURE1, "Measure 1"));
        addField(Measure_Type2 = new DmRq1Field_Text(rq1WorkItem.MEASURETYPE2, "Measure Type 2"));
        addField(Measure2 = new DmRq1Field_Text(rq1WorkItem.MEASURE2, "Measure 2"));
        addField(Measure_Type3 = new DmRq1Field_Text(rq1WorkItem.MEASURETYPE3, "Measure Type 3"));
        addField(Measure3 = new DmRq1Field_Text(rq1WorkItem.MEASURE3, "Measure 3"));
        addField(Measure_Type4 = new DmRq1Field_Text(rq1WorkItem.MEASURETYPE4, "Measure Type 4"));
        addField(Measure4 = new DmRq1Field_Text(rq1WorkItem.MEASURE4, "Measure 4"));
        addField(Measure_Type5 = new DmRq1Field_Text(rq1WorkItem.MEASURETYPE5, "Measure Type 5"));
        addField(Measure5 = new DmRq1Field_Text(rq1WorkItem.MEASURE5, "Measure 5"));
        addField(Measure_Type6 = new DmRq1Field_Text(rq1WorkItem.MEASURETYPE6, "Measure Type 6"));
        addField(Measure6 = new DmRq1Field_Text(rq1WorkItem.MEASURE6, "Measure 6"));
        addField(Measure_Type7 = new DmRq1Field_Text(rq1WorkItem.MEASURETYPE7, "Measure Type 7"));
        addField(Measure7 = new DmRq1Field_Text(rq1WorkItem.MEASURE7, "Measure 7"));
        addField(DECISION_STOP = new DmRq1Field_Text(rq1WorkItem.DECISION_STOP, "Decision Stop"));
        addField(DECISION_REMOVE = new DmRq1Field_Text(rq1WorkItem.DECISION_REMOVE, "Decision Remove"));
        addField(DECISION_CONTINUE = new DmRq1Field_Text(rq1WorkItem.DECISION_CONTINUE, "Decision Continue"));
        addField(EXTERNAL_SUMMARY = new DmRq1Field_Text(rq1WorkItem.EXTERNAL_SUMMARY, "External Summary"));

        addField(SYNC = new DmRq1Field_Enumeration(this, rq1WorkItem.SYNC, "Sync"));
        addField(LINKS = new DmRq1Field_Text(this, rq1WorkItem.LINKS, "Links"));

        addField(ACTUAL_DATE = new DmRq1Field_Date(this, rq1WorkItem.ACTUAL_DATE, "Actual Date"));
        ACTUAL_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(CATEGORY = new DmRq1Field_Enumeration(this, rq1WorkItem.CATEGORY, "Category"));
        CATEGORY.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.READ_ONLY_ON_UI);

        addField(DOMAIN = new DmRq1Field_Enumeration(this, rq1WorkItem.DOMAIN, "Domain"));
        DOMAIN.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.READ_ONLY_ON_UI);

        addField(EFFORT_ESTIMATION = new DmRq1Field_Number(rq1WorkItem.ESTIMATED_EFFORT, "Estimated Effort", 20)); //standard widthInCharacter is 20
        EFFORT_ESTIMATION.setAttribute(Attribute.FIELD_FOR_USER_DEFINED_TAB, Attribute.SINGLELINE_TEXT);
        EFFORT_ESTIMATION.setAttribute(Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(ESTIMATION_COMMENT = new DmRq1Field_Text(this, rq1WorkItem.ESTIMATION_COMMENT, "Estimation Comment"));
        ESTIMATION_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);
        ESTIMATION_COMMENT.setAttribute(Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(LIFE_CYCLE_STATE = new DmRq1Field_Enumeration(this, rq1WorkItem.LIFE_CYCLE_STATE, "Life Cycle State"));
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);

        addField(PLANNING_METHOD = new DmRq1Field_Enumeration(this, rq1WorkItem.PLANNING_METHOD, "Planning Method"));
        PLANNING_METHOD.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PLANNING_METHOD.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(PLANNED_DATE = new DmRq1Field_Date(this, rq1WorkItem.PLANNED_DATE, "Planned Date"));
        PLANNED_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PLANNED_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(REQUESTER = new DmRq1Field_Reference<>(this, rq1WorkItem.REQUESTER, "Requester"));
        addField(REQUESTER_FULLNAME = new DmRq1Field_Text(this, rq1WorkItem.REQUESTER_FULLNAME, "Fullname Requester"));
        addField(REQUESTER_EMAIL = new DmRq1Field_Text(this, rq1WorkItem.REQUESTER_EMAIL, "E-Mail Requester"));
        addField(REQUESTER_LOGIN_NAME = new DmRq1Field_Text(this, rq1WorkItem.REQUESTER_LOGIN_NAME, "Shortcut Requester"));

        addField(START_DATE = new DmRq1Field_Date(this, rq1WorkItem.START_DATE, "Start Date"));
        START_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        START_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SUBCATEGORY = new DmRq1Field_Enumeration(this, rq1WorkItem.SUBCATEGORY, "Sub Category"));
        SUBCATEGORY.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.READ_ONLY_ON_UI);

        addField(TYPE = new DmRq1Field_Enumeration(rq1WorkItem.TYPE, "Type"));
        TYPE.setAttribute(Attribute.FIELD_FOR_BULK_OPERATION);

        addField(ISSUE = new DmRq1Field_Reference<>(this, rq1WorkItem.BELONGS_TO_ISSUE, "Issue"));
        addField(RELEASE_RECORD = new DmRq1Field_Reference<>(this, rq1WorkItem.BELONGS_TO_RELEASE, "Release Records"));
        addField(MILESTONE = new DmElementField_FilteredByClass<DmRq1ReleaseRecord, DmRq1Milestone>(RELEASE_RECORD, DmRq1Milestone.class, "Milestone"));
        addField(RELEASE = new DmElementField_FilteredByClass<DmRq1ReleaseRecord, DmRq1Release>(RELEASE_RECORD, DmRq1Release.class, "Release"));
        addField(PROBLEM = new DmRq1Field_Reference<>(this, rq1WorkItem.BELONGS_TO_PROBLEM, "Problem"));

        addField(DEPENDS_ON_WORKITEMS = new DmRq1Field_ReferenceList<>(this, rq1WorkItem.DEPENDS_ON_WORKTITEMS, "Depends"));
        addField(PREDECESSORS = new DmRq1Field_ReferenceList_Writeable<>(rq1WorkItem.HAS_PREDECESSORS, "Predecessors"));
        addField(SUCCESSORS = new DmRq1Field_ReferenceList_Writeable<>(rq1WorkItem.HAS_SUCCESSORS, "Successors"));

        addField(PRODUCT = new DmRq1Field_Text_Product(this, rq1WorkItem.PRODUCT, "Product"));
        PRODUCT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PRODUCT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        PRODUCT.setAttribute(DmFieldI.Attribute.PRODUCT);
        addField(PRIORITY = new DmRq1Field_Text(this, rq1WorkItem.PRIORITY, "Priority"));
        addField(STATE = new DmRq1Field_Text(this, rq1WorkItem.STATE, "State"));

        addField(TASKMARKET_PRIORITY = new DmRq1Field_Enumeration(this, rq1WorkItem.TASKMARKET_PRIORITY, "Taskmarket Priority"));
        addField(TASKMARKET_BONUS = new DmRq1Field_Text(this, rq1WorkItem.TASKMARKET_BONUS, "Taskmarket Bonus"));
        addField(TASKMARKET_CATEGORY = new DmRq1Field_Enumeration(this, rq1WorkItem.TASKMARKET_CATEGORY, "Taskmarket Category"));
        addField(TASKMARKET_PROCESS_AREA = new DmRq1Field_Text(this, rq1WorkItem.TASKMARKET_PROCESS_AREA, "Taskmarket Process Area"));
        addField(TASKMARKET_ROI = new DmRq1Field_Text(this, rq1WorkItem.TASKMARKET_ROI, "Taskmarket ROI"));
        addField(TASKMARKET_INVEST_OF_ROI = new DmRq1Field_Text(this, rq1WorkItem.TASKMARKET_INVEST_OF_ROI, "Taskmarket Invest of ROI"));

        //
        // Fields for GPM - Guided project management
        //
        addField(GPM_SOURCE = new DmRq1Field_Text(rq1WorkItem.GPM_SOURCE, "GuidedPjM Source"));
        addField(GPM_PLIB_VERSION = new DmRq1Field_Text(rq1WorkItem.GPM_PLIB_VERSION, "GuidedPjM Version"));
        addField(GPM_TASK_ID = new DmRq1Field_Text(rq1WorkItem.GPM_TASK_ID, "GuidedPjM Task ID"));
        addField(GPM_MILESTONE_ID = new DmRq1Field_Text(rq1WorkItem.GPM_MILESTONE_ID, "GuidedPjM Milestone ID"));
        addField(GPM_PROJECT_ID = new DmRq1Field_Text(rq1WorkItem.GPM_PROJECT, "GuidedPjM Project ID"));

        
        //
        // GPM Milestone from Project
        //
        addField(MILESTONE_FROM_PROJECT = new DmRq1Field_MilestoneOnWorkitem(MILESTONE, GPM_MILESTONE_ID, "GPM Milestone"));
        
        
        //
        // Fields for flow framework
        //
        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, rq1WorkItem.FLOW_KIT_STATUS, "Full-Kit-Status"));

        addField(FLOW_RANK = new DmRq1Field_Text(this, rq1WorkItem.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, rq1WorkItem.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, rq1WorkItem.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, rq1WorkItem.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, rq1WorkItem.FLOW_CLUSTERNAME, "Flow ClusterName"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, rq1WorkItem.FLOW_CLUSTERID, "Flow Cluster ID"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, rq1WorkItem.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, rq1WorkItem.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_WI_TASK = new DmRq1Field_Text(this, rq1WorkItem.FLOW_WI_TASK, "Flow WorkItem Task"));
        addField(FLOW_RELATED_WI = new DmRq1Field_Text(this, rq1WorkItem.FLOW_RELATED_WI, "Flow Related WI"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, rq1WorkItem.FLOW_SIZE, "Size"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, rq1WorkItem.FLOW_STATUS, "Task Status"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, rq1WorkItem.FLOW_GROUP, "Groups"));
        addField(FLOW_IRM_GROUP = new DmRq1Field_Text(this, rq1WorkItem.FLOW_IRM_GROUP, "IRM Group"));
        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, rq1WorkItem.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, rq1WorkItem.TO_RED_DATE, "To RED Date"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, rq1WorkItem.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(PARENT_SWIMLANE = new DmRq1Field_Text(this, rq1WorkItem.PARENT_SWIMLANE, "Parent_Swimlane"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, rq1WorkItem.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, rq1WorkItem.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, rq1WorkItem.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, rq1WorkItem.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, rq1WorkItem.TARGET_DATE, "Planned-Date"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, rq1WorkItem.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_EXC_BOARD = new DmRq1Field_Text(this, rq1WorkItem.FLOW_EXC_BOARD, "Exclude from Board"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(rq1WorkItem.FLOW_BLOCKER_TABLE, rq1WorkItem.FLOW_SUBTASK_TABLE, "Blocker with Subtask Title"));

        //
        // Fields for CRP 
        //
        addField(CRP_RANK = new DmRq1Field_Text(this, rq1WorkItem.CRP_RANK, "CRP Rank"));
        addField(CRP_INTERNAL_RANK = new DmRq1Field_Text(this, rq1WorkItem.CRP_INTERNAL_RANK, "CRP Internal Rank"));
        addField(CRP_CLUSTERID = new DmRq1Field_Text(this, rq1WorkItem.CRP_CLUSTERID, "CRP Cluster ID"));
        addField(CRP_CLUSTERNAME = new DmRq1Field_Text(this, rq1WorkItem.CRP_CLUSTERNAME, "CRP ClusterName"));
        addField(CRP_R_EFFORT = new DmRq1Field_Text(this, rq1WorkItem.CRP_R_EFFORT, "CRP Remaining Effort"));
        addField(CRP_KIT_STATUS = new DmRq1Field_Enumeration(this, rq1WorkItem.CRP_KIT_STATUS, "CRP Task Status"));
        addField(CRP_GANTT = new DmRq1Field_Table<>(this, rq1WorkItem.CRP_GANTT, "CRP Gantt"));
        addField(CRP_VERSION = new DmRq1Field_Text(this, rq1WorkItem.CRP_VERSION, "CRP Version"));
        addField(CRP_PRED_OFFSET = new DmRq1Field_Text(this, rq1WorkItem.CRP_PRED_OFFSET, "CRP Predecessor Offset"));

        //
        // Create and add rules
        //
        addRule(new Rule_Workitem_PlannedDate(this));
        addRule(new Rule_Workitem_Started(this));
        addRule(new Rule_Workitem_Close(this));
        addRule(new Rule_Workitem_Conflicted(this));
        addRule(new ConfigurableRuleManagerRule_Rq1AssignedRecord(this));
    }

    public String getCriteriaStringForType() {
        return (((Rq1WorkItem) rq1Record).getCriteriaStringForType());
    }

    private boolean isWorkitemPredecessorOrSuccessor(DmRq1WorkItem workItem) {
        return (this.PREDECESSORS.getElementList().contains(workItem)
                || this.SUCCESSORS.getElementList().contains(workItem));
    }

    public void addPredecessor(DmRq1WorkItem workItem) throws ExistsAlreadyException {
        assert (workItem != null);
        if (isWorkitemPredecessorOrSuccessor(workItem) == true) {
            throw new ExistsAlreadyException();
        }
        this.PREDECESSORS.addElement(workItem);
        workItem.SUCCESSORS.addElement(this);
    }

    public void removePredecessor(DmRq1WorkItem workItem) {
        assert (workItem != null);
        this.PREDECESSORS.removeElement(workItem);
        workItem.SUCCESSORS.removeElement(this);
    }

    public void addSuccessor(DmRq1WorkItem workItem) throws ExistsAlreadyException {
        assert (workItem != null);
        if (isWorkitemPredecessorOrSuccessor(workItem) == true) {
            throw new ExistsAlreadyException();
        }
        this.SUCCESSORS.addElement(workItem);
        workItem.PREDECESSORS.addElement(this);
    }

    public void removeSuccessor(DmRq1WorkItem workItem) {
        assert (workItem != null);
        this.SUCCESSORS.removeElement(workItem);
        workItem.PREDECESSORS.removeElement(this);
    }

    public class ExistsAlreadyException extends Exception {

        public ExistsAlreadyException() {
            EcvUserMessage.showMessageDialog("Workitem " + getIdTitle() + " already exist!", "Error", EcvUserMessage.MessageType.ERROR_MESSAGE);
        }
    }

    public final EcvDate getPlannedDate() {
        return (PLANNED_DATE.getValue());
    }

    public String getGpmTaskId() {
        return GPM_TASK_ID.getValue();
    }

    final public TreeSet<EcvDate> getAllPlannedDates() {
        TreeSet<EcvDate> dates = new TreeSet<>();
        EcvDate date = getPlannedDate();
        if (date == null || date.isEmpty()) {
            dates.add(EcvDate.getEmpty());
        } else {
            dates.add(date);
        }
        return dates;
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    final public boolean isConflicted() {
        return (LIFE_CYCLE_STATE.getValue() == LifeCycleState_WorkItem.CONFLICTED);
    }

    final public boolean isClosed() {
        return ((LifeCycleState_WorkItem) LIFE_CYCLE_STATE.getValue() == LifeCycleState_WorkItem.CLOSED);
    }

    @Override
    final public boolean isCanceled() {
        return ((LifeCycleState_WorkItem) LIFE_CYCLE_STATE.getValue() == LifeCycleState_WorkItem.CANCELED);
    }

    @Override
    final public String getResponsible() {
        return (PROJECT.getElement().RESPONSIBLE_AT_BOSCH.getValue());
    }

    @Override
    public void forward(DmRq1Project project, DmRq1User newAssignee) {
        ClearFlowTags.clearTags(project, this);
        super.forward(project, newAssignee);
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LIFE_CYCLE_STATE.getValidInputValues());
    }

    @Override
    final public String getElementClass() {
        return ("Workitem");
    }

    @Override
    public String getAssigneeMail() {
        return ASSIGNEE_EMAIL.getValueAsText();
    }

    @Override
    public String getElementConfigurationType() {
        return ("Workitem");
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

    public DmRq1Field_Text getCrp_Rank() {
        return CRP_RANK;
    }

    public DmRq1Field_Text getCrp_R_Effort() {
        return CRP_R_EFFORT;
    }

    public DmRq1Field_Enumeration getCrpStatus() {
        return CRP_KIT_STATUS;
    }

    public DmRq1Field_Text getCrp_Version() {
        return CRP_VERSION;
    }

    public String getCrp_Predecessor_offset() {
        return CRP_PRED_OFFSET.getValueAsText();
    }

    /**
     * *
     * Moves one Workitem form a release (e.g. PVAR, PVER) to another release.
     * If the workitem already exists in the list of workitems of the new
     * release an exception gets thrown
     *
     * @param release the target release
     * @throws DataModel.Rq1.Records.DmRq1WorkItem.ExistsAlreadyException gets
     * thrown if the workitem already exists in the target release
     */
    public void moveFromReleaseToRelease(DmRq1Release release) throws ExistsAlreadyException {
        for (DmRq1WorkItem item : release.WORKITEMS.getElementList()) {
            if (this.equals(item)) {
                throw new ExistsAlreadyException();
            }
        }
        if (!(RELEASE.getElement().getClass().equals(release.getClass()))) {
            if (RELEASE.getElement().getClass().equals(DmRq1PvarRelease.class)) {
                this.SUBCATEGORY.setValue(SubCategory_WorkItem.PVER);
            } else {
                this.SUBCATEGORY.setValue(SubCategory_WorkItem.PVAR_PFAM);
            }
        }

        this.RELEASE.getElement().WORKITEMS.removeElement(this);
        this.RELEASE.setElement(release);
        release.WORKITEMS.addElement(this);
    }

    /**
     * *
     * Moves one Workitem form a Issue (e.g. IssueSW, IssuwFD) to another issue.
     * If the workitem already exits in the list of workitems of the new issue
     * an exception gets thrown
     *
     * @param issue the target issue
     * @throws DataModel.Rq1.Records.DmRq1WorkItem.ExistsAlreadyException gets
     * thrown if the workitem already exists in the target release
     */
    public void moveFromIssueToIssue(DmRq1Issue issue) throws ExistsAlreadyException {
        for (DmRq1WorkItem item : issue.WORKITEMS.getElementList()) {
            if (this.equals(item)) {
                throw new ExistsAlreadyException();
            }
        }
        this.ISSUE.getElement().WORKITEMS.removeElement(this);
        this.ISSUE.setElement(issue);
        issue.WORKITEMS.addElement(this);
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTypeIdTitle();
    }

    @Override
    public String getIdForSubject() {
        return getId();
    }

    public String getTaskId() {
        return GPM_TASK_ID.getValue();
    }

    /**
     *
     */
    static public List<DmRq1WorkItem> get_WI_From_All_Elements_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        ArrayList<DmRq1WorkItem> returnList = new ArrayList<>();
        returnList.addAll(get_WI_on_ISW_on_Project_for_Customer(project, customerGroup));
        returnList.addAll(get_WI_on_IFD_on_ISW_on_Project_for_Customer(project, customerGroup));
        returnList.addAll(get_WI_on_Release_on_ISW_on_Project_for_Customer(project, customerGroup));

        return returnList;
    }

    /**
     * Returns all WI which belong to a I-SW which belongs to a project with the
     * given customer.
     *
     * @param customerGroup Group of the customer.
     * @return
     */
    static public List<DmRq1WorkItem> get_WI_on_ISW_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        List<DmRq1WorkItem> result = new ArrayList<>();
        for (Rq1WorkItem rq1Workitem : Rq1WorkItem.get_WI_on_ISW_on_Project_for_Customer((Rq1Project) project.getRq1Record(), customerGroup)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Workitem);
            if (dmElement instanceof DmRq1WorkItem) {
                result.add((DmRq1WorkItem) dmElement);
            }
        }

        return (result);
    }

    /**
     * Returns all WI which belong to a I-FD which belongs to a I-SW which
     * belongs to a project with the given customer.
     *
     * @param customerGroup Group of the customer.
     * @return
     */
    static public List<DmRq1WorkItem> get_WI_on_IFD_on_ISW_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        List<DmRq1WorkItem> result = new ArrayList<>();
        for (Rq1WorkItem rq1Workitem : Rq1WorkItem.get_WI_on_IFD_on_ISW_on_Project_for_Customer((Rq1Project) project.getRq1Record(), customerGroup)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Workitem);
            if (dmElement instanceof DmRq1WorkItem) {
                result.add((DmRq1WorkItem) dmElement);
            }
        }

        return (result);
    }

    /**
     * Returns all WI which belong to a Release which belong to a I-SW which
     * belongs to a project with the given customer.
     *
     * @param customerGroup Group of the customer.
     * @return
     */
    static public List<DmRq1WorkItem> get_WI_on_Release_on_ISW_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        List<DmRq1WorkItem> result = new ArrayList<>();
        for (Rq1WorkItem rq1Workitem : Rq1WorkItem.get_WI_on_Release_on_ISW_on_Project_for_Customer((Rq1Project) project.getRq1Record(), customerGroup)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Workitem);
            if (dmElement instanceof DmRq1WorkItem) {
                result.add((DmRq1WorkItem) dmElement);
            }
        }

        return (result);
    }

    protected static DmRq1WorkItem cloneBasicContentofWorkItem(DmRq1WorkItem workItem, DmRq1WorkItem clonedWorkItem) {

        clonedWorkItem.DESCRIPTION.setValue(workItem.DESCRIPTION.getValueAsText());
        clonedWorkItem.TYPE.setValue(workItem.TYPE.getValue());
        clonedWorkItem.TITLE.setValue(workItem.TITLE.getValueAsText() + " - Clone");
        clonedWorkItem.EFFORT_ESTIMATION.setValue(workItem.EFFORT_ESTIMATION.getValueAsText());
        clonedWorkItem.ESTIMATION_COMMENT.setValue(workItem.ESTIMATION_COMMENT.getValueAsText());
        clonedWorkItem.ASSIGNEE_FULLNAME.setValue(workItem.ASSIGNEE_FULLNAME.getValueAsText());
        clonedWorkItem.ASSIGNEE_EMAIL.setValue(workItem.ASSIGNEE_EMAIL.getValueAsText());
        clonedWorkItem.ASSIGNEE_LOGIN_NAME.setValue(workItem.ASSIGNEE_LOGIN_NAME.getValueAsText());
        clonedWorkItem.ACCOUNT_NUMBERS.setValue(workItem.ACCOUNT_NUMBERS.getValueAsText());
        clonedWorkItem.LIFE_CYCLE_STATE.setValue(LifeCycleState_WorkItem.NEW);
        clonedWorkItem.REQUESTER_FULLNAME.setValue(workItem.REQUESTER_FULLNAME.getValueAsText());
        clonedWorkItem.REQUESTER_EMAIL.setValue(workItem.REQUESTER_EMAIL.getValueAsText());
        clonedWorkItem.REQUESTER_LOGIN_NAME.setValue(workItem.REQUESTER_LOGIN_NAME.getValueAsText());
        clonedWorkItem.PRIORITY.setValue(workItem.PRIORITY.getValueAsText());
        clonedWorkItem.PRODUCT.setValue(workItem.PRODUCT.getValueAsText());
        clonedWorkItem.STATE.setValue(workItem.STATE.getValueAsText());

        if (workItem.CATEGORY.getValue() != null) {
            clonedWorkItem.CATEGORY.setValue(workItem.CATEGORY.getValue());
        }
        if (workItem.SUBCATEGORY.getValue() != null) {
            clonedWorkItem.SUBCATEGORY.setValue(workItem.SUBCATEGORY.getValue());
        }
        if (workItem.START_DATE.getValue() != null) {
            clonedWorkItem.START_DATE.setValue(workItem.START_DATE.getValue());
        }
        if (workItem.PLANNED_DATE.getValue() != null) {
            clonedWorkItem.PLANNED_DATE.setValue(workItem.PLANNED_DATE.getValue());
        }
        if (workItem.ACTUAL_DATE.getValue() != null) {
            clonedWorkItem.ACTUAL_DATE.setValue(workItem.ACTUAL_DATE.getValue());
        }
        if (workItem.ASSIGNEE.getElement() != null) {
            clonedWorkItem.ASSIGNEE.setElement(workItem.ASSIGNEE.getElement());
        }
        if (workItem.REQUESTER.getElement() != null) {
            clonedWorkItem.REQUESTER.setElement(workItem.REQUESTER.getElement());
        }
        if (workItem.DOMAIN.getValue() != null) {
            clonedWorkItem.DOMAIN.setValue(workItem.DOMAIN.getValue());
        }
        if (workItem.REQUESTER.getElement() != null) {
            clonedWorkItem.REQUESTER.setElement(workItem.REQUESTER.getElement());
        }
        if (workItem.REQUESTER.getElement() != null) {
            clonedWorkItem.REQUESTER.setElement(workItem.REQUESTER.getElement());
        }
        if (workItem.TASKMARKET_PRIORITY.getValue() != null) {
            clonedWorkItem.TASKMARKET_PRIORITY.setValue(workItem.TASKMARKET_PRIORITY.getValue());
        }
        if (workItem.TASKMARKET_BONUS.getValue() != null) {
            clonedWorkItem.TASKMARKET_BONUS.setValue(workItem.TASKMARKET_BONUS.getValue());
        }
        if (workItem.TASKMARKET_CATEGORY.getValue() != null) {
            clonedWorkItem.TASKMARKET_CATEGORY.setValue(workItem.TASKMARKET_CATEGORY.getValue());
        }
        if (workItem.TASKMARKET_PROCESS_AREA.getValue() != null) {
            clonedWorkItem.TASKMARKET_PROCESS_AREA.setValue(workItem.TASKMARKET_PROCESS_AREA.getValue());
        }
        if (workItem.TASKMARKET_ROI.getValue() != null) {
            clonedWorkItem.TASKMARKET_ROI.setValue(workItem.TASKMARKET_ROI.getValue());
        }
        if (workItem.TASKMARKET_INVEST_OF_ROI.getValue() != null) {
            clonedWorkItem.TASKMARKET_INVEST_OF_ROI.setValue(workItem.TASKMARKET_INVEST_OF_ROI.getValue());
        }
        if (workItem.TAGS.getValue() != null) {
            clonedWorkItem.TAGS.setValue(workItem.TAGS.getValue());
        }
        if (workItem.PLANNING_METHOD.getValue() != null) {
            clonedWorkItem.PLANNING_METHOD.setValue(workItem.PLANNING_METHOD.getValue());
        }

        // Internal_Comment_STRING mit Attachement
        String internalComment = workItem.INTERNAL_COMMENT.getValueAsText() + "\n\n";
        internalComment += "This Workitem was cloned from Workitem " + workItem.getId() + "\n\n";

        if (workItem.ATTACHMENTS.getElementList() != null && workItem.ATTACHMENTS.getElementList().size() > 0) {
            internalComment += "The following attachments were not copied: ";
            for (DmRq1Attachment item : workItem.ATTACHMENTS.getElementList()) {
                internalComment += "\n" + item.getTitle();
            }
        }

        clonedWorkItem.INTERNAL_COMMENT.setValue(internalComment);

        return clonedWorkItem;
    }

    public abstract DmRq1WorkItem cloneWorkItem();

    @Override
    public DmRq1Field_Reference<DmRq1User> getRequesterField() {
        return (REQUESTER);
    }

    @Override
    public String getRank() {
        return (FLOW_RANK.getValueAsText());
    }

    @Override
    public String getCrpRank() {
        return (CRP_RANK.getValueAsText());
    }

    @Override
    public String getFlowVersion() {
        return (FLOW_VERSION.getValueAsText());
    }

    @Override
    public String getCrpVersion() {
        return (FLOW_VERSION.getValueAsText());
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
        return (FLOW_CLUSTERNAME.getValueAsText());
    }

    @Override
    public String getCrpClusterName() {
        return (CRP_CLUSTERNAME.getValueAsText());
    }

    @Override
    public String getClusterID() {
        return (FLOW_CLUSTERID.getValueAsText());
    }

    @Override
    public String getCrpClusterID() {
        return (CRP_CLUSTERID.getValueAsText());
    }

    @Override
    public FullKitSize getSize() {
        return ((FullKitSize) FLOW_SIZE.getValue());
    }

    @Override
    public String getRemainingEffort() {
        return (FLOW_R_EFFORT.getValueAsText());
    }

    @Override
    public String getCrpRemainingEffort() {
        return (CRP_R_EFFORT.getValueAsText());
    }

    public String getWITask() {
        return (FLOW_WI_TASK.getValueAsText());
    }

    public String getRelatedWIInfo() {
        return (FLOW_RELATED_WI.getValueAsText());
    }

    @Override
    public String getRequestedDate() {
        return (FLOW_R_DATE.getValueAsText());
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
    public InternalRank getCrpInternalRank() throws InternalRank.BuildException {
        if (crpInternalRank == null) {
            crpInternalRank = InternalRank.buildForRecord(getRq1Id(), CRP_RANK.getValue(), CRP_INTERNAL_RANK.getValue());
            CRP_INTERNAL_RANK.setValue(crpInternalRank.toString());
        }
        return (crpInternalRank);
    }

    @Override
    public InternalRank rankFirst(InternalRank currentFirst) throws InternalRank.BuildException {
        assert (currentFirst != null);

        internalRank = InternalRank.buildFirst(currentFirst);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    @Override
    public InternalRank crpRankFirst(InternalRank currentFirst) throws InternalRank.BuildException {
        assert (currentFirst != null);

        crpInternalRank = InternalRank.buildFirst(currentFirst);
        CRP_INTERNAL_RANK.setValue(crpInternalRank.toString());

        return (crpInternalRank);
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
    public InternalRank crpRankBetween(InternalRank before, InternalRank after) throws InternalRank.BuildException {
        assert (before != null);
        assert (after != null);

        crpInternalRank = InternalRank.buildBetween(before, after);
        CRP_INTERNAL_RANK.setValue(crpInternalRank.toString());

        return (crpInternalRank);
    }

    @Override
    public InternalRank rankLast(InternalRank currentLast) throws InternalRank.BuildException {
        assert (currentLast != null);

        internalRank = InternalRank.buildLast(currentLast);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    @Override
    public InternalRank crpRankLast(InternalRank currentLast) throws InternalRank.BuildException {
        assert (currentLast != null);

        crpInternalRank = InternalRank.buildLast(currentLast);
        CRP_INTERNAL_RANK.setValue(crpInternalRank.toString());

        return (crpInternalRank);
    }

    public void setExistingIFdRank(InternalRank ifdRank) {
        internalRank = ifdRank;
        FLOW_INTERNAL_RANK.setValue(ifdRank.toString());
    }

    public void setCrpExistingIFdRank(InternalRank ifdRank) {
        crpInternalRank = ifdRank;
        CRP_INTERNAL_RANK.setValue(ifdRank.toString());
    }

    @Override
    public String getGroupId() {
        return FLOW_GROUP.getValueAsText();
    }

    public String getIRMGroupStatus() {
        return FLOW_IRM_GROUP.getValueAsText();
    }

    @Override
    public EcvDate getToRedDate() {
        return FLOW_TO_RED_DATE.getValue();
    }

    public String getSwimlaneHeading() {
        return (FLOW_CLUSTERID.getValueAsText());
    }

    public String getParentSwimlane() {
        return PARENT_SWIMLANE.getValueAsText();
    }

    public KingState getKingState() {
        return ((KingState) KING_STATE.getValue());
    }

    public ExpertState getExpertState() {
        return ((ExpertState) EXPERT_STATE.getValue());
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }

    public String getExpertAvailEffort() {
        return FLOW_EXP_AVAL_EFFORT.getValueAsText();
    }

    public String getExcFromBoard() {
        return FLOW_EXC_BOARD.getValueAsText();
    }

    @Override
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    @Override
    public void reload() {
        internalRank = null;
        super.reload();
    }

    @Override
    public EcvDate getMappedPlannedDate() {
        return PLANNED_DATE.getDate();
    }

    @Override
    public int getPositionInRoadMap() {
        return -90;
    }

    /**
     *
     * Gibt das Element zurück unter welchem sich das Workitem befindet
     *
     * @return DmRq1Element or null
     */
    public DmRq1Element getBelongsToElement() {

        if (MILESTONE.isElementSet() == true) {
            return (MILESTONE.getElement());
            
        } else if (RELEASE.isElementSet() == true) {
            return (RELEASE.getElement());

        } else if (ISSUE.isElementSet() == true) {
            return (ISSUE.getElement());

        } else if (PROBLEM.isElementSet() == true) {
            return (PROBLEM.getElement());

        } else if (PROJECT.isElementSet() == true) {
            return (PROJECT.getElement());

        } else {
            return null;
        }

    }

}
