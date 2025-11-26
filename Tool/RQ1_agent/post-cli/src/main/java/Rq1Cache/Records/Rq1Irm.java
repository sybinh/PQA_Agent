/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
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
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1LinkDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1XmlTable_AnalysisDone;
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_FmeaDocument;
import Rq1Cache.Types.Rq1XmlTable_PverFStart;
import Rq1Data.Enumerations.Category_Exhaust_Comments;
import Rq1Data.Enumerations.Category_OBD_Comments;
import Rq1Data.Enumerations.Issue_WorkflowStatus;
import Rq1Data.Enumerations.LifeCycleState_IRM;
import Rq1Data.Enumerations.PlanningAction;
import Rq1Data.Enumerations.Priority;
import Rq1Data.Enumerations.QualityMeasure;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.YesNoEmpty;

/**
 *
 * @author gug2wi
 */
public class Rq1Irm extends Rq1Map {

    final public static Rq1AttributeName ATTRIBUTE_HAS_MAPPED_ISSUE = new Rq1AttributeName("hasMappedIssue");
    final public static Rq1AttributeName ATTRIBUTE_HAS_MAPPED_RELEASE = new Rq1AttributeName("hasMappedRelease");
    final public static Rq1AttributeName ATTRIBUTE_QUALIFICATION_STATUS = new Rq1AttributeName("QualificationStatus");

    final public Rq1DatabaseField_Text INTEGRATION_STEP;
    final public Rq1DatabaseField_Enumeration IS_PILOT;
    final public Rq1DatabaseField_Enumeration QUALIFICATION_MEASURE;
    final public Rq1DatabaseField_Enumeration QUALIFICATION_STATUS;
    final public Rq1DatabaseField_Text REQUESTED_CONFIGURATION;
    //
    final public Rq1DatabaseField_Enumeration LIFE_CYCLE_STATE;
    final public Rq1DatabaseField_Enumeration PLANNING_ACTION;
    final public Rq1DatabaseField_Enumeration PRIORITY;
    final public Rq1DatabaseField_Text PRIORITY_COMMENT;
    final public Rq1DatabaseField_Enumeration SCOPE;
    //
    final private Rq1DatabaseField_Xml CHANGES_TO_CONFIGURATION_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ChangesToConfiguration> CHANGES_TO_CONFIGURATION;
    final public Rq1DatabaseField_Xml CHANGES_TO_PARTLIST_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ChangesToPartlist> CHANGES_TO_PARTLIST;
    final public Rq1DatabaseField_Text CHANGES_TO_ISSUES;
    //
    final public Rq1DatabaseField_Reference HAS_MAPPED_ISSUE;
    final public Rq1DatabaseField_Reference HAS_MAPPED_RELEASE;

    final public Rq1DatabaseField_Enumeration WORKFLOW_STATUS;

    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Enumeration FLOW_KIT_STATUS;
    final public Rq1XmlSubField_Text FLOW_RANK;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Text FLOW_R_DATE;
    final public Rq1XmlSubField_Text FLOW_GROUP;
    final public Rq1XmlSubField_Text FLOW_IRM_GROUP;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Text FLOW_CLUSTERNAME;
    final public Rq1XmlSubField_Text FLOW_CLUSTERID;
    final public Rq1XmlSubField_Text FLOW_R_EFFORT;
    final public Rq1XmlSubField_Text FLOW_NO_OF_DEVELOPERS;
    final public Rq1XmlSubField_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_AnalysisDone> FLOW_ANALYSIS_DONE_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_PverFStart> FLOW_PVER_F_STARTED;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE_ANALYSIS;
    final public Rq1XmlSubField_Date TO_RED_DATE_ANALYSIS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_ANALYSIS_TABLE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_ANALYSIS_TABLE;
    final public Rq1XmlSubField_Text FLOW_ISW_IRM_TASK;
    final public Rq1XmlSubField_Text FLOW_EXC_BOARD;

    final public Rq1XmlSubField_Table<Rq1XmlTable_FmeaDocument> FMEA_DOCUMENT_TABLE;
    final public Rq1XmlSubField_Text FMEA_VARIANT;
    
    final public Rq1XmlSubField_Xml DELIB;
    final public Rq1XmlSubField_Xml DELIB_FCCHANGEDESCRIPTION;
    final public Rq1XmlSubField_Xml DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL;
    final public Rq1XmlSubField_Enumeration DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_EVALUATION;
    final public Rq1XmlSubField_Text DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_CONDITION;
    final public Rq1XmlSubField_Text DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_REASON;
    final public Rq1XmlSubField_Xml DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL;
    final public Rq1XmlSubField_Enumeration DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_EVALUATION;
    final public Rq1XmlSubField_Text DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_CONDITION;
    final public Rq1XmlSubField_Text DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_REASON;
    final public Rq1XmlSubField_Xml DELIB_FNCCAT;
    final public Rq1XmlSubField_Xml DELIB_FNCCAT_EXHAUST;
    final public Rq1XmlSubField_Enumeration DELIB_FNCCAT_EXHAUST_EVALUATION;
    final public Rq1XmlSubField_Enumeration DELIB_FNCCAT_EXHAUST_EVALUATIONCOMMENT;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_EXHAUST_ENABLECONDITION;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_EXHAUST_ASSESSMENTCOMMENT;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_EXHAUST_REVIEWDONE;
    final public Rq1XmlSubField_Xml DELIB_FNCCAT_OBD;
    final public Rq1XmlSubField_Enumeration DELIB_FNCCAT_OBD_EVALUATION;
    final public Rq1XmlSubField_Enumeration DELIB_FNCCAT_OBD_EVALUATIONCOMMENT;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_OBD_ENABLECONDITION;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_OBD_ASSESSMENTCOMMENT;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_OBD_REVIEWDONE;
    final public Rq1XmlSubField_Xml DELIB_FNCCAT_SAFETY;
    final public Rq1XmlSubField_Enumeration DELIB_FNCCAT_SAFETY_EVALUATION;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_SAFETY_ENABLECONDITION;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_SAFETY_ASSESSMENTCOMMENT;
    final public Rq1XmlSubField_Text DELIB_FNCCAT_SAFETY_REVIEWDONE;
    

    public Rq1Irm(Rq1LinkDescription mapDescription) {
        super(mapDescription);

        Rq1XmlTable_ChangesToConfiguration ctc = new Rq1XmlTable_ChangesToConfiguration();
        addField(CHANGES_TO_CONFIGURATION_XML = new Rq1DatabaseField_Xml(this, "ChangesToConfiguration").addConverter(ctc));
        addField(CHANGES_TO_CONFIGURATION = new Rq1XmlSubField_Table<>(this, ctc, CHANGES_TO_CONFIGURATION_XML, "ChangeConfig"));

        Rq1XmlTable_ChangesToPartlist ctp = new Rq1XmlTable_ChangesToPartlist();
        addField(CHANGES_TO_PARTLIST_XML = new Rq1DatabaseField_Xml(this, "ChangesToPartlist").addConverter(ctp));
        addField(CHANGES_TO_PARTLIST = new Rq1XmlSubField_Table<>(this, ctp, CHANGES_TO_PARTLIST_XML, "ChangePart"));

        addField(CHANGES_TO_ISSUES = new Rq1DatabaseField_Text(this, "ChangesToIssues"));
        addField(HAS_MAPPED_ISSUE = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_MAPPED_ISSUE, Rq1RecordType.ISSUE));
        addField(HAS_MAPPED_RELEASE = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_MAPPED_RELEASE, Rq1RecordType.RELEASE));
        addField(INTEGRATION_STEP = new Rq1DatabaseField_Text(this, "IntegrationStep"));
        addField(IS_PILOT = new Rq1DatabaseField_Enumeration(this, "isPilot", YesNoEmpty.values()));
        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_IRM.values(), LifeCycleState_IRM.NEW));
        addField(QUALIFICATION_MEASURE = new Rq1DatabaseField_Enumeration(this, "QualificationMeasure", QualityMeasure.values()));
        addField(QUALIFICATION_STATUS = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_QUALIFICATION_STATUS, "", ""));
        QUALIFICATION_STATUS.acceptInvalidValuesInDatabase();
        addField(PLANNING_ACTION = new Rq1DatabaseField_Enumeration(this, "PlanningAction", PlanningAction.values()));
        addField(PRIORITY = new Rq1DatabaseField_Enumeration(this, "Priority", Priority.values()));
        addField(PRIORITY_COMMENT = new Rq1DatabaseField_Text(this, "PriorityComment"));
        addField(REQUESTED_CONFIGURATION = new Rq1DatabaseField_Text(this, "RequestedConfiguration"));
        addField(SCOPE = new Rq1DatabaseField_Enumeration(this, "Scope", Scope.values()));
        SCOPE.setReadOnlyForNewRecord();

        addField(WORKFLOW_STATUS = new Rq1DatabaseField_Enumeration(this, "WorkflowStatus", Issue_WorkflowStatus.values()));

        //
        // Fields for flow framework
        //
        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        FLOW.setOptional();
        addField(FLOW_KIT_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "FKS", FullKitStatus.values(), null));
        addField(FLOW_RANK = new Rq1XmlSubField_Text(this, FLOW, "RANK"));
        addField(FLOW_VERSION = new Rq1XmlSubField_Text(this, FLOW, "V"));
        addField(FLOW_R_DATE = new Rq1XmlSubField_Text(this, FLOW, "R_DATE"));
        addField(FLOW_INTERNAL_RANK = new Rq1XmlSubField_Text(this, FLOW, "INTERNAL_RANK"));
        addField(FLOW_CLUSTERNAME = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER"));
        addField(FLOW_GROUP = new Rq1XmlSubField_Text(this, FLOW, "GROUP"));
        addField(FLOW_IRM_GROUP = new Rq1XmlSubField_Text(this, FLOW, "IRM_GROUP"));
        addField(FLOW_R_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "R_EFFORT"));
        addField(FLOW_NO_OF_DEVELOPERS = new Rq1XmlSubField_Text(this, FLOW, "NB_D"));
        addField(FLOW_SIZE = new Rq1XmlSubField_Enumeration(this, FLOW, "SIZE", FullKitSize.values(), null));
        addField(FLOW_CLUSTERID = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER_ID"));
        addField(FLOW_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "STATUS", TaskStatus.values(), null));
        addField(FLOW_SUBTASK_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowSubTask(), FLOW, "SUBTASK"));
        addField(TO_RED_DATE = new Rq1XmlSubField_Date(this, FLOW, "TO_RED"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new Rq1XmlSubField_Text(this, FLOW, "SL_H"));
        addField(KING_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "KING", KingState.values(), null));
        addField(FLOW_BLOCKER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowBlocker(), FLOW, "BLOCKER"));
        addField(EXPERT_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "EXP", ExpertState.values(), null));
        addField(FLOW_EXP_AVAl_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "EXP_EFFORT"));
        addField(TARGET_DATE = new Rq1XmlSubField_Date(this, FLOW, "T_DATE"));
        addField(CRITICAL_RESOURCE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CriticalResource(), FLOW, "C_RES"));
        addField(FLOW_ANALYSIS_DONE_STATE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_AnalysisDone(), FLOW, "ANALYS_DONE"));
        addField(FLOW_PVER_F_STARTED = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_PverFStart(), FLOW, "PVER_F-STARTED"));
        addField(FLOW_SIZE_ANALYSIS = new Rq1XmlSubField_Enumeration(this, FLOW, "SIZE_ANALYSIS", FullKitSize.values(), null));
        addField(TO_RED_DATE_ANALYSIS = new Rq1XmlSubField_Date(this, FLOW, "TO_RED_ANALYSIS"));
        addField(FLOW_SUBTASK_ANALYSIS_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowSubTask(), FLOW, "ANALYSIS_SUBTASK"));
        addField(FLOW_BLOCKER_ANALYSIS_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowBlocker(), FLOW, "ANALYSIS_BLOCKER"));
        addField(FLOW_ISW_IRM_TASK = new Rq1XmlSubField_Text(this, FLOW, "TASK"));
        addField(FLOW_EXC_BOARD = new Rq1XmlSubField_Text(this, FLOW, "EXC_FROM_BOARD"));
        FLOW_ISW_IRM_TASK.setOptional();
        FLOW_ANALYSIS_DONE_STATE.setOptional();
        FLOW_EXP_AVAl_EFFORT.setOptional();
        FLOW_PVER_F_STARTED.setOptional();
        CRITICAL_RESOURCE.setOptional();
        TARGET_DATE.setOptional();
        FLOW_IRM_GROUP.setOptional();
        FLOW_EXC_BOARD.setOptional();

        addField(FMEA_DOCUMENT_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FmeaDocument(), TAGS, "FMEAdocument"));
        addField(FMEA_VARIANT = new Rq1XmlSubField_Text(this, TAGS, "FMEAvariant"));
        
        addField(DELIB = new Rq1XmlSubField_Xml(this, TAGS, "DELib"));
        addField(DELIB_FCCHANGEDESCRIPTION = new Rq1XmlSubField_Xml(this, DELIB, "FCChangeDescription"));
        addField(DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL = new Rq1XmlSubField_Xml(this, DELIB_FCCHANGEDESCRIPTION, "HEX_NEUTRAL"));
        addField(DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_EVALUATION = new Rq1XmlSubField_Enumeration(this, DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL, "Evaluation", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        addField(DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_CONDITION = new Rq1XmlSubField_Text(this, DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL, "Condition"));
        addField(DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_REASON = new Rq1XmlSubField_Text(this, DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL, "Reason"));
        addField(DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL = new Rq1XmlSubField_Xml(this, DELIB_FCCHANGEDESCRIPTION, "FUNCTIONAL_NEUTRAL"));
        addField(DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_EVALUATION = new Rq1XmlSubField_Enumeration(this, DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL, "Evaluation", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        addField(DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_CONDITION = new Rq1XmlSubField_Text(this, DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL, "Condition"));
        addField(DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_REASON = new Rq1XmlSubField_Text(this, DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL, "Reason"));
        addField(DELIB_FNCCAT = new Rq1XmlSubField_Xml(this, DELIB, "FnCCat"));
        addField(DELIB_FNCCAT_EXHAUST= new Rq1XmlSubField_Xml(this, DELIB_FNCCAT, "Exhaust"));
        addField(DELIB_FNCCAT_EXHAUST_EVALUATION = new Rq1XmlSubField_Enumeration(this, DELIB_FNCCAT_EXHAUST, "Evaluation", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        addField(DELIB_FNCCAT_EXHAUST_EVALUATIONCOMMENT = new Rq1XmlSubField_Enumeration(this, DELIB_FNCCAT_EXHAUST, "EvaluationComment", Category_Exhaust_Comments.values(), Category_Exhaust_Comments.EMPTY));
        addField(DELIB_FNCCAT_EXHAUST_ENABLECONDITION = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_EXHAUST, "EnableCondition"));
        addField(DELIB_FNCCAT_EXHAUST_ASSESSMENTCOMMENT = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_EXHAUST, "AssessmentComment"));
        addField(DELIB_FNCCAT_EXHAUST_REVIEWDONE = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_EXHAUST, "ReviewDone"));
        addField(DELIB_FNCCAT_OBD = new Rq1XmlSubField_Xml(this, DELIB_FNCCAT, "OBD"));
        addField(DELIB_FNCCAT_OBD_EVALUATION = new Rq1XmlSubField_Enumeration(this, DELIB_FNCCAT_OBD, "Evaluation", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        addField(DELIB_FNCCAT_OBD_EVALUATIONCOMMENT = new Rq1XmlSubField_Enumeration(this, DELIB_FNCCAT_OBD, "EvaluationComment", Category_OBD_Comments.values(), Category_OBD_Comments.EMPTY));
        addField(DELIB_FNCCAT_OBD_ENABLECONDITION = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_OBD, "EnableCondition"));
        addField(DELIB_FNCCAT_OBD_ASSESSMENTCOMMENT = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_OBD, "AssessmentComment"));
        addField(DELIB_FNCCAT_OBD_REVIEWDONE = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_OBD, "ReviewDone"));
        addField(DELIB_FNCCAT_SAFETY = new Rq1XmlSubField_Xml(this, DELIB_FNCCAT, "Safety"));
        addField(DELIB_FNCCAT_SAFETY_EVALUATION = new Rq1XmlSubField_Enumeration(this, DELIB_FNCCAT_SAFETY, "Evaluation", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        addField(DELIB_FNCCAT_SAFETY_ENABLECONDITION = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_SAFETY, "EnableCondition"));
        addField(DELIB_FNCCAT_SAFETY_ASSESSMENTCOMMENT = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_SAFETY, "AssessmentComment"));
        addField(DELIB_FNCCAT_SAFETY_REVIEWDONE = new Rq1XmlSubField_Text(this, DELIB_FNCCAT_SAFETY, "ReviewDone"));
        DELIB.setOptional();
        DELIB_FCCHANGEDESCRIPTION.setOptional();
        DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL.setOptional();
        DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_EVALUATION.setOptional();
        DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_CONDITION.setOptional();
        DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_REASON.setOptional();
        DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL.setOptional();
        DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_EVALUATION.setOptional();
        DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_CONDITION.setOptional();
        DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_REASON.setOptional();
        DELIB_FNCCAT.setOptional();
        DELIB_FNCCAT_EXHAUST.setOptional();
        DELIB_FNCCAT_EXHAUST_EVALUATION.setOptional();
        DELIB_FNCCAT_EXHAUST_EVALUATIONCOMMENT.setOptional();
        DELIB_FNCCAT_EXHAUST_ENABLECONDITION.setOptional();
        DELIB_FNCCAT_EXHAUST_ASSESSMENTCOMMENT.setOptional();
        DELIB_FNCCAT_EXHAUST_REVIEWDONE.setOptional();
        DELIB_FNCCAT_OBD.setOptional();
        DELIB_FNCCAT_OBD_EVALUATION.setOptional();
        DELIB_FNCCAT_OBD_EVALUATIONCOMMENT.setOptional();
        DELIB_FNCCAT_OBD_ENABLECONDITION.setOptional();
        DELIB_FNCCAT_OBD_ASSESSMENTCOMMENT.setOptional();
        DELIB_FNCCAT_OBD_REVIEWDONE.setOptional();
        DELIB_FNCCAT_SAFETY.setOptional();
        DELIB_FNCCAT_SAFETY_EVALUATION.setOptional();
        DELIB_FNCCAT_SAFETY_ENABLECONDITION.setOptional();
        DELIB_FNCCAT_SAFETY_ASSESSMENTCOMMENT.setOptional();
        DELIB_FNCCAT_SAFETY_REVIEWDONE.setOptional();
        
        
    }

    @Override
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        Rq1AttributeName[] order = {ATTRIBUTE_HAS_MAPPED_ISSUE, ATTRIBUTE_HAS_MAPPED_RELEASE, ATTRIBUTE_OPERATION_MODE};
        return (super.createInDatabase(order));
    }

    @Override
    public synchronized boolean save(Rq1AttributeName[] fieldOrder) {
        if ((fieldOrder != null) && (fieldOrder.length > 0)) {
            return super.save(fieldOrder);
        } else {
            Rq1AttributeName[] names = {ATTRIBUTE_LIFE_CYCLE_STATE};
            return super.save(names);
        }
    }
}
