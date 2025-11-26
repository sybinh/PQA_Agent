/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataStore.DsField_Xml.ContentMode;
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList_Writeable;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_UserReference;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_CrpGantt;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Enumerations.Category_WorkItem;
import Rq1Data.Enumerations.Domain_WorkItem;
import Rq1Data.Enumerations.LifeCycleState_WorkItem;
import Rq1Data.Enumerations.PlanningMethod;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import Rq1Data.Enumerations.Sync;
import Rq1Data.Enumerations.TaskmarketCategory;
import Rq1Data.Enumerations.TaskmarketPriority;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class Rq1WorkItem extends Rq1AssignedRecord {

    final static public Rq1AttributeName ATTRIBUTE_BELONGS_TO_ISSUE = new Rq1AttributeName("belongsToIssue");
    final static public Rq1AttributeName ATTRIBUTE_BELONGS_TO_RELEASE = new Rq1AttributeName("belongsToRelease");
    final static public Rq1AttributeName ATTRIBUTE_BELONGS_TO_PROBLEM = new Rq1AttributeName("belongsToProblem");
    final static public Rq1AttributeName ATTRIBUTE_DOMAIN = new Rq1AttributeName("Domain");
    final static public Rq1AttributeName ATTRIBUTE_CATEGORY = new Rq1AttributeName("Category");
    final static public Rq1AttributeName ATTRIBUTE_ESTIMATED_EFFORT = new Rq1AttributeName("EstimatedEffort");
    final static public Rq1AttributeName ATTRIBUTE_ESTIMATION_COMMENT = new Rq1AttributeName("EstimationComment");
    final static public Rq1AttributeName ATTRIBUTE_PLANNING_METHOD = new Rq1AttributeName("PlanningMethod");
    final static public Rq1AttributeName ATTRIBUTE_PLANNED_DATE = new Rq1AttributeName("PlannedDate");
    final static public Rq1AttributeName ATTRIBUTE_TYPE = new Rq1AttributeName("Type");
    final static public Rq1AttributeName ATTRIBUTE_PRODUCT = new Rq1AttributeName("Product");
    final static public Rq1AttributeName ATTRIBUTE_HAS_PREDECESSOR = new Rq1AttributeName("hasPredecessors");
    final static public Rq1AttributeName ATTRIBUTE_HAS_SUCCESSOR = new Rq1AttributeName("hasSuccessors");
    //
    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Text CREATION_MODE;
    final public Rq1DatabaseField_Enumeration DOMAIN;
    final public Rq1DatabaseField_Text ESTIMATED_EFFORT;
    final public Rq1DatabaseField_Text ESTIMATION_COMMENT;
    final public Rq1DatabaseField_Text IS_DUPLICATE;
    final public Rq1DatabaseField_Text LAST_LIFE_CYCLE_STATE;
    final public Rq1DatabaseField_Text NOTE_ENTRY;
    final public Rq1DatabaseField_Text NOTES_LOG;
    final public Rq1DatabaseField_Enumeration PLANNING_METHOD;
    final public Rq1DatabaseField_Text PRODUCT;
    final public Rq1DatabaseField_Text PRIORITY;
    final public Rq1DatabaseField_UserReference REQUESTER;
    final public Rq1DatabaseField_Text REQUESTER_FULLNAME;
    final public Rq1DatabaseField_Text REQUESTER_EMAIL;
    final public Rq1DatabaseField_Text REQUESTER_LOGIN_NAME;
    final public Rq1DatabaseField_Text RO_TITLE;
    final public Rq1DatabaseField_Text RO_TYPE;
    final public Rq1DatabaseField_Text STATE;
    final public Rq1DatabaseField_Enumeration TYPE;
    //
    final public Rq1DatabaseField_Date ACTUAL_DATE;
    final public Rq1DatabaseField_Date PLANNED_DATE;
    final public Rq1DatabaseField_Date START_DATE;
    final public Rq1DatabaseField_Enumeration SUBCATEGORY;
    //
    final public Rq1DatabaseField_Enumeration LIFE_CYCLE_STATE;
    //
    final public Rq1DatabaseField_Reference BELONGS_TO_ISSUE;
    final public Rq1DatabaseField_Reference BELONGS_TO_RELEASE;
    final public Rq1DatabaseField_Reference BELONGS_TO_PROBLEM;
    //
    final public Rq1DatabaseField_ReferenceList DEPENDS_ON_WORKTITEMS;
    final public Rq1DatabaseField_ReferenceList_Writeable HAS_PREDECESSORS;
    final public Rq1DatabaseField_ReferenceList_Writeable HAS_SUCCESSORS;

    //
    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Enumeration FLOW_KIT_STATUS;
    final public Rq1XmlSubField_Text FLOW_RANK;
    final public Rq1XmlSubField_Text FLOW_GROUP;
    final public Rq1XmlSubField_Text FLOW_IRM_GROUP;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Text FLOW_R_DATE;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Text FLOW_CLUSTERNAME;
    final public Rq1XmlSubField_Text FLOW_CLUSTERID;
    final public Rq1XmlSubField_Text FLOW_R_EFFORT;
    final public Rq1XmlSubField_Text FLOW_NO_OF_DEVELOPERS;
    final public Rq1XmlSubField_Text FLOW_WI_TASK;
    final public Rq1XmlSubField_Text FLOW_RELATED_WI;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public Rq1XmlSubField_Text PARENT_SWIMLANE;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Text FLOW_EXC_BOARD;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;

    // Fields for CRP 
    final public Rq1XmlSubField_Xml CRP;
    final public Rq1XmlSubField_Text CRP_RANK;
    final public Rq1XmlSubField_Text CRP_INTERNAL_RANK;
    final public Rq1XmlSubField_Text CRP_CLUSTERNAME;
    final public Rq1XmlSubField_Text CRP_CLUSTERID;
    final public Rq1XmlSubField_Text CRP_R_EFFORT;
    final public Rq1XmlSubField_Enumeration CRP_KIT_STATUS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CrpGantt> CRP_GANTT;
    final public Rq1XmlSubField_Text CRP_VERSION;
    final public Rq1XmlSubField_Text CRP_PRED_OFFSET;

    final private Rq1XmlSubField_Xml TASKMARKET;
    final public Rq1XmlSubField_Enumeration TASKMARKET_PRIORITY;
    final public Rq1XmlSubField_Text TASKMARKET_BONUS;
    final public Rq1XmlSubField_Enumeration TASKMARKET_CATEGORY;
    final public Rq1XmlSubField_Text TASKMARKET_PROCESS_AREA;
    final public Rq1XmlSubField_Text TASKMARKET_ROI;
    final public Rq1XmlSubField_Text TASKMARKET_INVEST_OF_ROI;

    final private Rq1XmlSubField_Xml GPM;
    final public Rq1XmlSubField_Text GPM_SOURCE;
    final public Rq1XmlSubField_Text GPM_PLIB_VERSION;
    final public Rq1XmlSubField_Text GPM_TASK_ID;
    final public Rq1XmlSubField_Text GPM_MILESTONE_ID;
    final public Rq1XmlSubField_Text GPM_PROJECT;

    final public Rq1DatabaseField_Enumeration SYNC;
    final public Rq1DatabaseField_Text LINKS;

    //Fields for SW Codex
    final public Rq1XmlSubField_Text MEASURETYPE1;
    final public Rq1XmlSubField_Text MEASURE1;
    final public Rq1XmlSubField_Text MEASURETYPE2;
    final public Rq1XmlSubField_Text MEASURE2;
    final public Rq1XmlSubField_Text MEASURETYPE3;
    final public Rq1XmlSubField_Text MEASURE3;
    final public Rq1XmlSubField_Text MEASURETYPE4;
    final public Rq1XmlSubField_Text MEASURE4;
    final public Rq1XmlSubField_Text MEASURETYPE5;
    final public Rq1XmlSubField_Text MEASURE5;
    final public Rq1XmlSubField_Text MEASURETYPE6;
    final public Rq1XmlSubField_Text MEASURE6;
    final public Rq1XmlSubField_Text MEASURETYPE7;
    final public Rq1XmlSubField_Text MEASURE7;
    final public Rq1XmlSubField_Text DECISION_STOP;
    final public Rq1XmlSubField_Text DECISION_REMOVE;
    final public Rq1XmlSubField_Text DECISION_CONTINUE;
    final public Rq1XmlSubField_Text EXTERNAL_SUMMARY;

    final private String criteriaStringForType;

    public Rq1WorkItem(Rq1NodeDescription description, Domain_WorkItem domain, Category_WorkItem category, SubCategory_WorkItem subCategory) {
        super(description);
        assert (domain != null);
        assert (category != null);
        assert (subCategory != null);

        addField(MEASURETYPE1 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_MeasureType1"));
        addField(MEASURE1 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Measure1"));
        addField(MEASURETYPE2 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_MeasureType2"));
        addField(MEASURE2 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Measure2"));
        addField(MEASURETYPE3 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_MeasureType3"));
        addField(MEASURE3 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Measure3"));
        addField(MEASURETYPE4 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_MeasureType4"));
        addField(MEASURE4 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Measure4"));
        addField(MEASURETYPE5 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_MeasureType5"));
        addField(MEASURE5 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Measure5"));
        addField(MEASURETYPE6 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_MeasureType6"));
        addField(MEASURE6 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Measure6"));
        addField(MEASURETYPE7 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_MeasureType7"));
        addField(MEASURE7 = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Measure7"));
        addField(DECISION_STOP = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Stop"));
        addField(DECISION_REMOVE = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Remove"));
        addField(DECISION_CONTINUE = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_Continue"));
        addField(EXTERNAL_SUMMARY = new Rq1XmlSubField_Text(this, TAGS, "CodexCaseResult_ExternalInfo"));

        addField(SYNC = new Rq1DatabaseField_Enumeration(this, "Sync", Sync.values()));
        SYNC.acceptInvalidValuesInDatabase();
        addField(LINKS = new Rq1DatabaseField_Text(this, "Links"));

        addField(ACTUAL_DATE = new Rq1DatabaseField_Date(this, "ActualDate"));
        addField(CREATION_MODE = new Rq1DatabaseField_Text(this, "CreationMode"));
        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CATEGORY, Category_WorkItem.values()));
        CATEGORY.setValue(category);
        addField(DOMAIN = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_DOMAIN, Domain_WorkItem.values()));
        DOMAIN.setDataModelValue(domain);
        addField(ESTIMATED_EFFORT = new Rq1DatabaseField_Text(this, ATTRIBUTE_ESTIMATED_EFFORT));
        addField(ESTIMATION_COMMENT = new Rq1DatabaseField_Text(this, ATTRIBUTE_ESTIMATION_COMMENT));
        addField(IS_DUPLICATE = new Rq1DatabaseField_Text(this, "is_duplicate"));
        addField(LAST_LIFE_CYCLE_STATE = new Rq1DatabaseField_Text(this, "LastLifeCycleState"));
        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_WorkItem.values(), LifeCycleState_WorkItem.NEW));
        addField(NOTE_ENTRY = new Rq1DatabaseField_Text(this, "Note_Entry"));
        addField(NOTES_LOG = new Rq1DatabaseField_Text(this, "Notes_Log"));
        addField(PLANNING_METHOD = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_PLANNING_METHOD, PlanningMethod.values(), PlanningMethod.EMPTY));
        addField(PLANNED_DATE = new Rq1DatabaseField_Date(this, ATTRIBUTE_PLANNED_DATE));
        addField(PRODUCT = new Rq1DatabaseField_Text(this, ATTRIBUTE_PRODUCT));
        addField(PRIORITY = new Rq1DatabaseField_Text(this, "Priority"));

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

        addField(RO_TITLE = new Rq1DatabaseField_Text(this, "ROTitle"));
        addField(RO_TYPE = new Rq1DatabaseField_Text(this, "ROType"));
        addField(START_DATE = new Rq1DatabaseField_Date(this, "StartDate"));
        addField(STATE = new Rq1DatabaseField_Text(this, "State"));
        addField(SUBCATEGORY = new Rq1DatabaseField_Enumeration(this, "SubCategory", SubCategory_WorkItem.values()));
        SUBCATEGORY.setValue(subCategory);

        String temp = "";
        if (domain != Domain_WorkItem.EMPTY) {
            temp += "Domain=" + domain.getText() + ";";
        }
        if (category != Category_WorkItem.EMPTY) {
            temp += "Category=" + category.getText() + ";";
        }
        if (subCategory != SubCategory_WorkItem.EMPTY) {
            temp += "SubCategory=" + subCategory.getText();
        }
        criteriaStringForType = temp;

        addField(TYPE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_TYPE, criteriaStringForType, ""));

        addField(BELONGS_TO_ISSUE = new Rq1DatabaseField_Reference(this, ATTRIBUTE_BELONGS_TO_ISSUE, Rq1RecordType.ISSUE));
        addField(BELONGS_TO_RELEASE = new Rq1DatabaseField_Reference(this, ATTRIBUTE_BELONGS_TO_RELEASE, Rq1RecordType.RELEASE));
        addField(BELONGS_TO_PROBLEM = new Rq1DatabaseField_Reference(this, ATTRIBUTE_BELONGS_TO_PROBLEM, Rq1RecordType.PROBLEM));

        addField(DEPENDS_ON_WORKTITEMS = new Rq1DatabaseField_ReferenceList(this, "dependsOnWorkitems", Rq1RecordType.WORKITEM));
        addField(HAS_PREDECESSORS = new Rq1DatabaseField_ReferenceList_Writeable(this, ATTRIBUTE_HAS_PREDECESSOR, Rq1RecordType.WORKITEM));
        addField(HAS_SUCCESSORS = new Rq1DatabaseField_ReferenceList_Writeable(this, ATTRIBUTE_HAS_SUCCESSOR, Rq1RecordType.WORKITEM));

        addField(TASKMARKET = new Rq1XmlSubField_Xml(this, TAGS, "Taskmarket"));
        TASKMARKET.setOptional();
        addField(TASKMARKET_PRIORITY = new Rq1XmlSubField_Enumeration(this, TASKMARKET, "Priority", TaskmarketPriority.values(), TaskmarketPriority.EMPTY));
        TASKMARKET_PRIORITY.acceptInvalidValuesInDatabase();
        addField(TASKMARKET_BONUS = new Rq1XmlSubField_Text(this, TASKMARKET, "Bonus"));
        addField(TASKMARKET_CATEGORY = new Rq1XmlSubField_Enumeration(this, TASKMARKET, "Category", TaskmarketCategory.values(), TaskmarketCategory.EMPTY));
        TASKMARKET_CATEGORY.acceptInvalidValuesInDatabase();
        addField(TASKMARKET_PROCESS_AREA = new Rq1XmlSubField_Text(this, TASKMARKET, "ProcessArea"));
        addField(TASKMARKET_ROI = new Rq1XmlSubField_Text(this, TASKMARKET, "ROI"));
        addField(TASKMARKET_INVEST_OF_ROI = new Rq1XmlSubField_Text(this, TASKMARKET, "investOfROI"));

        //
        // Fields for GPM - Guided project management
        //
        addField(GPM = new Rq1XmlSubField_Xml(this, TAGS, ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "GPM"));
        GPM.setOptional();
        addField(GPM_SOURCE = new Rq1XmlSubField_Text(this, GPM, "Source"));
        GPM_SOURCE.setOptional();
        addField(GPM_PLIB_VERSION = new Rq1XmlSubField_Text(this, GPM, "PlibVersion"));
        GPM_PLIB_VERSION.setOptional();
        addField(GPM_TASK_ID = new Rq1XmlSubField_Text(this, GPM, "TaskID"));
        GPM_TASK_ID.setOptional();
        addField(GPM_MILESTONE_ID = new Rq1XmlSubField_Text(this, GPM, "MilestoneID"));
        GPM_MILESTONE_ID.setOptional();
        addField(GPM_PROJECT = new Rq1XmlSubField_Text(this, GPM, "Project"));
        GPM_PROJECT.setOptional();

        //
        // Fields for flow framework
        //
        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        FLOW.setOptional();
        addField(FLOW_KIT_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "FKS", FullKitStatus.values(), null));
        addField(FLOW_RANK = new Rq1XmlSubField_Text(this, FLOW, "RANK"));
        addField(FLOW_GROUP = new Rq1XmlSubField_Text(this, FLOW, "GROUP"));
        addField(FLOW_IRM_GROUP = new Rq1XmlSubField_Text(this, FLOW, "IRM_GROUP"));
        addField(FLOW_VERSION = new Rq1XmlSubField_Text(this, FLOW, "V"));
        addField(FLOW_R_DATE = new Rq1XmlSubField_Text(this, FLOW, "R_DATE"));
        addField(FLOW_INTERNAL_RANK = new Rq1XmlSubField_Text(this, FLOW, "INTERNAL_RANK"));
        addField(FLOW_SIZE = new Rq1XmlSubField_Enumeration(this, FLOW, "SIZE", FullKitSize.values(), null));
        addField(FLOW_CLUSTERNAME = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER"));
        addField(FLOW_CLUSTERID = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER_ID"));
        addField(FLOW_R_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "R_EFFORT"));
        addField(FLOW_NO_OF_DEVELOPERS = new Rq1XmlSubField_Text(this, FLOW, "NB_D"));
        addField(FLOW_WI_TASK = new Rq1XmlSubField_Text(this, FLOW, "TASK"));
        addField(FLOW_RELATED_WI = new Rq1XmlSubField_Text(this, FLOW, "RELATED_WI"));
        addField(FLOW_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "STATUS", TaskStatus.values(), null));
        addField(FLOW_SUBTASK_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowSubTask(), FLOW, "SUBTASK"));
        addField(TO_RED_DATE = new Rq1XmlSubField_Date(this, FLOW, "TO_RED"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new Rq1XmlSubField_Text(this, FLOW, "SL_H"));
        addField(PARENT_SWIMLANE = new Rq1XmlSubField_Text(this, FLOW, "PARENT_SWIMLANE"));
        addField(KING_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "KING", KingState.values(), null));
        addField(FLOW_BLOCKER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowBlocker(), FLOW, "BLOCKER"));
        addField(FLOW_EXP_AVAl_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "EXP_EFFORT"));
        FLOW_EXP_AVAl_EFFORT.setOptional();
        addField(EXPERT_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "EXP", ExpertState.values(), null));
        addField(TARGET_DATE = new Rq1XmlSubField_Date(this, FLOW, "T_DATE"));
        addField(CRITICAL_RESOURCE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CriticalResource(), FLOW, "C_RES"));
        addField(FLOW_EXC_BOARD = new Rq1XmlSubField_Text(this, FLOW, "EXC_FROM_BOARD"));
        CRITICAL_RESOURCE.setOptional();
        TARGET_DATE.setOptional();
        FLOW_EXC_BOARD.setOptional();
        FLOW_IRM_GROUP.setOptional();

        //
        // Fields for CRP framework
        //
        addField(CRP = new Rq1XmlSubField_Xml(this, TAGS, "CRP"));
        CRP.setOptional();
        addField(CRP_RANK = new Rq1XmlSubField_Text(this, CRP, "CRP_RANK"));
        addField(CRP_INTERNAL_RANK = new Rq1XmlSubField_Text(this, CRP, "INT_RANK"));
        addField(CRP_CLUSTERID = new Rq1XmlSubField_Text(this, CRP, "CLUSTER_ID"));
        addField(CRP_CLUSTERNAME = new Rq1XmlSubField_Text(this, CRP, "CLUSTER"));
        addField(CRP_R_EFFORT = new Rq1XmlSubField_Text(this, CRP, "CRP_R_EFFORT"));
        addField(CRP_KIT_STATUS = new Rq1XmlSubField_Enumeration(this, CRP, "CRP_FKS", FullKitStatus.values(), null));
        addField(CRP_GANTT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CrpGantt(), CRP, "GANTT"));
        addField(CRP_VERSION = new Rq1XmlSubField_Text(this, CRP, "CRP_V"));
        addField(CRP_PRED_OFFSET = new Rq1XmlSubField_Text(this, CRP, "CRP_PRED_OFFSET"));
    }

    public String getCriteriaStringForType() {
        return criteriaStringForType;
    }

    @Override
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        Rq1AttributeName[] order = {ATTRIBUTE_BELONGS_TO_PROJECT, ATTRIBUTE_OPERATION_MODE};
        return (super.createInDatabase(order));
    }

    public static Iterable<Rq1WorkItem> get_WI_on_ISW_on_Project_for_Customer(Rq1Project rq1Project, String customerGroup) {
        assert (rq1Project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.WORKITEM.getRecordType());

        query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_WorkItem.getAllOpenState());
        query.addCriteria_Reference(Rq1WorkItem.ATTRIBUTE_BELONGS_TO_PROJECT.getName(), rq1Project);
        query.addCriteria_Value(Rq1WorkItem.ATTRIBUTE_BELONGS_TO_ISSUE, new Rq1AttributeName("Type"), "Issue SW");
        query.addCriteria_Value(
                Rq1WorkItem.ATTRIBUTE_BELONGS_TO_ISSUE,
                Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT,
                Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1WorkItem> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1WorkItem) {
                result.add((Rq1WorkItem) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

    public static Iterable<Rq1WorkItem> get_WI_on_IFD_on_ISW_on_Project_for_Customer(Rq1Project rq1Project, String customerGroup) {
        assert (rq1Project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.WORKITEM.getRecordType());

        query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_WorkItem.getAllOpenState());
        query.addCriteria_Reference(Rq1WorkItem.ATTRIBUTE_BELONGS_TO_PROJECT.getName(), rq1Project);
        query.addCriteria_Value(
                Rq1WorkItem.ATTRIBUTE_BELONGS_TO_ISSUE,
                Rq1IssueFD.ATTRIBUTE_HAS_PARENT,
                Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT,
                Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1WorkItem> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1WorkItem) {
                result.add((Rq1WorkItem) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

    public static Iterable<Rq1WorkItem> get_WI_on_Release_on_ISW_on_Project_for_Customer(Rq1Project rq1Project, String customerGroup) {
        assert (rq1Project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.WORKITEM.getRecordType());

        query.addCriteria_ValueList(Rq1WorkItem.ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_WorkItem.getAllOpenState());
        query.addCriteria_Reference(Rq1WorkItem.ATTRIBUTE_BELONGS_TO_PROJECT.getName(), rq1Project);
        query.addCriteria_Value(
                Rq1WorkItem.ATTRIBUTE_BELONGS_TO_RELEASE,
                Rq1Release.ATTRIBUTE_HAS_MAPPED_ISSUES,
                Rq1Irm_Bc_IssueFd.ATTRIBUTE_HAS_MAPPED_ISSUE,
                Rq1IssueFD.ATTRIBUTE_HAS_PARENT,
                Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT,
                Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1WorkItem> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1WorkItem) {
                result.add((Rq1WorkItem) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

}
