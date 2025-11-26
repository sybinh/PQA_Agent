/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_IRM;
import DataModel.DmFieldI;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import DataModel.Flow.DmFlowIssue;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.InternalRank;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle;
import DataModel.Rq1.Fields.DmRq1Field_IntegrationStepMap;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1Irm;
import Rq1Cache.Types.Rq1XmlTable_AnalysisDone;
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_FmeaDocument;
import Rq1Cache.Types.Rq1XmlTable_PverFStart;
import Rq1Data.Enumerations.LifeCycleState_IRM;
import Rq1Data.Enumerations.YesNoEmpty;
import UiSupport.AssigneeFilter;
import util.EcvDate;
import util.EcvEnumeration;
import util.EcvMapSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Irm extends DmRq1MapElement implements DmFlowIssue {

    static public class MappedBc {

        final private String name;
        final private String version;

        public MappedBc(String name, String version) {
            assert (name != null);
            assert (version != null);
            this.name = name;
            this.version = version;
        }

        public String getName() {
            return (name);
        }

        public String getVersion() {
            return (version);
        }
    }

    public final DmRq1Field_IntegrationStepMap INTEGRATION_STEP;
    final public DmRq1Field_Enumeration LIFE_CYCLE_STATE;
    final public DmRq1Field_Enumeration PLANNING_ACTION;
    final public DmRq1Field_Enumeration PRIORITY;
    final public DmRq1Field_Enumeration SCOPE;
    final public DmRq1Field_Enumeration IS_PILOT;
    final public DmRq1Field_Enumeration QUALIFICATION_STATUS;
    final public DmRq1Field_Enumeration QUALIFICATION_MEASURE;
    //
    final public DmRq1Field_Table<Rq1XmlTable_ChangesToConfiguration> CHANGES_TO_CONFIGURATION;
    final public DmRq1Field_Table<Rq1XmlTable_ChangesToPartlist> CHANGES_TO_PARTLIST;
    final public DmRq1Field_Text CHANGES_TO_ISSUES;
    final public DmRq1Field_Text PRIORITY_COMMENT;
    final public DmRq1Field_Text REQUESTED_CONFIGURATION;
    //

    final public DmRq1Field_Reference<DmRq1Issue> HAS_MAPPED_ISSUE;
    final public DmRq1Field_Reference<DmRq1Release> HAS_MAPPED_RELEASE;

    final public DmRq1Field_Enumeration WORKFLOW_STATUS;

    //
    // members for flow model
    //
    final public DmRq1Field_Text FLOW_GROUP;
    final public DmRq1Field_Text FLOW_IRM_GROUP;
    final public DmRq1Field_Enumeration FLOW_KIT_STATUS;
    final public DmRq1Field_Text FLOW_RANK;
    final public DmRq1Field_Text FLOW_R_DATE;
    final public DmRq1Field_Text FLOW_VERSION;
    final public DmRq1Field_Text FLOW_INTERNAL_RANK;
    final public DmRq1Field_Text FLOW_CLUSTERNAME;
    final public DmRq1Field_Text FLOW_CLUSTERID;
    final public DmRq1Field_Date FLOW_TO_RED_DATE;
    final public DmRq1Field_Text FLOW_R_EFFORT;
    final public DmRq1Field_Text FLOW_NO_OF_DEVELOPERS;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Enumeration FLOW_SIZE;
    public InternalRank internalRank = null;
    final public DmRq1Field_Enumeration FLOW_STATUS;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_ANALYSIS;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_ANALYSIS;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public DmRq1Field_Table<Rq1XmlTable_AnalysisDone> FLOW_ANALYSIS_DONE_STATE;
    final public DmRq1Field_Table<Rq1XmlTable_PverFStart> FLOW_PVER_F_STARTED;
    final public DmRq1Field_Enumeration FLOW_SIZE_ANALYSIS;
    final public DmRq1Field_Date FLOW_TO_RED_DATE_ANALYSIS;
    final public DmRq1Field_Text FLOW_ISW_IRM_TASK;
    final public DmRq1Field_Text FLOW_EXC_BOARD;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;

    final public DmRq1Field_Table<Rq1XmlTable_FmeaDocument> FMEA_DOCUMENT_TABLE;
    final public DmRq1Field_Text FMEA_VARIANT;

    final public DmRq1Field_Enumeration DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_EVALUATION;
    final public DmRq1Field_Text DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_CONDITION;
    final public DmRq1Field_Text DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_REASON;
    final public DmRq1Field_Enumeration DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_EVALUATION;
    final public DmRq1Field_Text DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_CONDITION;
    final public DmRq1Field_Text DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_REASON;
    final public DmRq1Field_Enumeration DELIB_FNCCAT_EXHAUST_EVALUATION;
    final public DmRq1Field_Enumeration DELIB_FNCCAT_EXHAUST_EVALUATIONCOMMENT;
    final public DmRq1Field_Text DELIB_FNCCAT_EXHAUST_ENABLECONDITION;
    final public DmRq1Field_Text DELIB_FNCCAT_EXHAUST_ASSESSMENTCOMMENT;
    final public DmRq1Field_Text DELIB_FNCCAT_EXHAUST_REVIEWDONE;
    final public DmRq1Field_Enumeration DELIB_FNCCAT_OBD_EVALUATION;
    final public DmRq1Field_Enumeration DELIB_FNCCAT_OBD_EVALUATIONCOMMENT;
    final public DmRq1Field_Text DELIB_FNCCAT_OBD_ENABLECONDITION;
    final public DmRq1Field_Text DELIB_FNCCAT_OBD_ASSESSMENTCOMMENT;
    final public DmRq1Field_Text DELIB_FNCCAT_OBD_REVIEWDONE;
    final public DmRq1Field_Enumeration DELIB_FNCCAT_SAFETY_EVALUATION;
    final public DmRq1Field_Text DELIB_FNCCAT_SAFETY_ENABLECONDITION;
    final public DmRq1Field_Text DELIB_FNCCAT_SAFETY_ASSESSMENTCOMMENT;
    final public DmRq1Field_Text DELIB_FNCCAT_SAFETY_REVIEWDONE;

    public DmRq1Irm(String elementType, Rq1Irm rq1Irm) {
        super(elementType, rq1Irm);
        //
        // Create and add fields
        //
        addField(CHANGES_TO_CONFIGURATION = new DmRq1Field_Table<>(this, rq1Irm.CHANGES_TO_CONFIGURATION, "Changes To Configuration"));
        addField(CHANGES_TO_PARTLIST = new DmRq1Field_Table<>(this, rq1Irm.CHANGES_TO_PARTLIST, "Changes To Partlist"));
        addField(CHANGES_TO_ISSUES = new DmRq1Field_Text(this, rq1Irm.CHANGES_TO_ISSUES, "Changes To Issues"));
        addField(HAS_MAPPED_ISSUE = new DmRq1Field_Reference<>(this, rq1Irm.HAS_MAPPED_ISSUE, "Mapped Issus"));
        addField(HAS_MAPPED_RELEASE = new DmRq1Field_Reference<>(this, rq1Irm.HAS_MAPPED_RELEASE, "Mapped Release"));
        addField(INTEGRATION_STEP = new DmRq1Field_IntegrationStepMap(this, rq1Irm.INTEGRATION_STEP, "Integration Step"));
        INTEGRATION_STEP.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        INTEGRATION_STEP.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        INTEGRATION_STEP.setAttribute(DmFieldI.Attribute.INTEGRATION_STEP);

        addField(IS_PILOT = new DmRq1Field_Enumeration(this, rq1Irm.IS_PILOT, "Is Pilot"));
        IS_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        IS_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(LIFE_CYCLE_STATE = new DmRq1Field_Enumeration(this, rq1Irm.LIFE_CYCLE_STATE, "Life Cycle State"));
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);

        addField(PLANNING_ACTION = new DmRq1Field_Enumeration(this, rq1Irm.PLANNING_ACTION, "Planning Action"));
        PLANNING_ACTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PLANNING_ACTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(PRIORITY = new DmRq1Field_Enumeration(this, rq1Irm.PRIORITY, "Priority"));
        PRIORITY.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PRIORITY.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(PRIORITY_COMMENT = new DmRq1Field_Text(this, rq1Irm.PRIORITY_COMMENT, "Priority Comment"));
        PRIORITY_COMMENT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        PRIORITY_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PRIORITY_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(QUALIFICATION_MEASURE = new DmRq1Field_Enumeration(this, rq1Irm.QUALIFICATION_MEASURE, "Qualification Measure"));
        QUALIFICATION_MEASURE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        QUALIFICATION_MEASURE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(QUALIFICATION_STATUS = new DmRq1Field_Enumeration(this, rq1Irm.QUALIFICATION_STATUS, "Qualification Status"));
        QUALIFICATION_STATUS.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        QUALIFICATION_STATUS.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(REQUESTED_CONFIGURATION = new DmRq1Field_Text(this, rq1Irm.REQUESTED_CONFIGURATION, "Requested Configuration"));
        REQUESTED_CONFIGURATION.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        REQUESTED_CONFIGURATION.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        REQUESTED_CONFIGURATION.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SCOPE = new DmRq1Field_Enumeration(this, rq1Irm.SCOPE, "Scope"));
        SCOPE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        SCOPE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(WORKFLOW_STATUS = new DmRq1Field_Enumeration(this, rq1Irm.WORKFLOW_STATUS, "Workflow Status"));
        WORKFLOW_STATUS.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        WORKFLOW_STATUS.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        //
        // Fields for flow framework
        //
        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, rq1Irm.FLOW_KIT_STATUS, "Full-Kit-Status"));
        addField(FLOW_RANK = new DmRq1Field_Text(this, rq1Irm.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, rq1Irm.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, rq1Irm.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, rq1Irm.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, rq1Irm.FLOW_CLUSTERNAME, "Flow Cluster Name"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, rq1Irm.FLOW_CLUSTERID, "Flow Cluster ID"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, rq1Irm.FLOW_SIZE, "Size"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, rq1Irm.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, rq1Irm.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, rq1Irm.FLOW_STATUS, "Task Status"));
        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, rq1Irm.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, rq1Irm.FLOW_GROUP, "Groups"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, rq1Irm.TO_RED_DATE, "To RED Date"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, rq1Irm.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, rq1Irm.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, rq1Irm.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, rq1Irm.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, rq1Irm.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, rq1Irm.TARGET_DATE, "Planned-Date"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, rq1Irm.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_ANALYSIS_DONE_STATE = new DmRq1Field_Table<>(this, rq1Irm.FLOW_ANALYSIS_DONE_STATE, "Analysis Done State"));
        addField(FLOW_PVER_F_STARTED = new DmRq1Field_Table<>(this, rq1Irm.FLOW_PVER_F_STARTED, "Pver F-started"));
        addField(FLOW_SIZE_ANALYSIS = new DmRq1Field_Enumeration(this, rq1Irm.FLOW_SIZE_ANALYSIS, "Flow Size Analysis"));
        addField(FLOW_TO_RED_DATE_ANALYSIS = new DmRq1Field_Date(this, rq1Irm.TO_RED_DATE_ANALYSIS, "Flow To PverF RED Date"));
        addField(FLOW_SUBTASK_ANALYSIS = new DmRq1Field_Table<>(this, rq1Irm.FLOW_SUBTASK_ANALYSIS_TABLE, "Flow Analysis Subtask"));
        addField(FLOW_BLOCKER_ANALYSIS = new DmRq1Field_Table<>(this, rq1Irm.FLOW_BLOCKER_ANALYSIS_TABLE, "Flow Analysis Blocker"));
        addField(FLOW_ISW_IRM_TASK = new DmRq1Field_Text(this, rq1Irm.FLOW_ISW_IRM_TASK, "Flow I-SW/IRM Task"));
        addField(FLOW_IRM_GROUP = new DmRq1Field_Text(this, rq1Irm.FLOW_IRM_GROUP, "IRM Group"));
        addField(FLOW_EXC_BOARD = new DmRq1Field_Text(this, rq1Irm.FLOW_EXC_BOARD, "Exclude from Board"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(rq1Irm.FLOW_BLOCKER_TABLE, rq1Irm.FLOW_SUBTASK_TABLE, "Blocker with Subtask Title"));

        addField(FMEA_DOCUMENT_TABLE = new DmRq1Field_Table<>(rq1Irm.FMEA_DOCUMENT_TABLE, "FMEA Documents"));
        addField(FMEA_VARIANT = new DmRq1Field_Text(rq1Irm.FMEA_VARIANT, "FMEA Variant"));

        addField(DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_EVALUATION = new DmRq1Field_Enumeration(this, rq1Irm.DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_EVALUATION, "Hex neutral"));
        addField(DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_CONDITION = new DmRq1Field_Text(this, rq1Irm.DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_CONDITION, "Condition for hex neutrality"));
        addField(DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_REASON = new DmRq1Field_Text(this, rq1Irm.DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_REASON, "Reason for hex neutrality"));
        addField(DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_EVALUATION = new DmRq1Field_Enumeration(this, rq1Irm.DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_EVALUATION, "Functional neutral"));
        addField(DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_CONDITION = new DmRq1Field_Text(this, rq1Irm.DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_CONDITION, "Condition for functional neutrality"));
        addField(DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_REASON = new DmRq1Field_Text(this, rq1Irm.DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_REASON, "Reason for functional neutrality"));
        addField(DELIB_FNCCAT_EXHAUST_EVALUATION = new DmRq1Field_Enumeration(this, rq1Irm.DELIB_FNCCAT_EXHAUST_EVALUATION, "Exhaust"));
        addField(DELIB_FNCCAT_EXHAUST_EVALUATIONCOMMENT = new DmRq1Field_Enumeration(this, rq1Irm.DELIB_FNCCAT_EXHAUST_EVALUATIONCOMMENT, "Category Exhaust Comment"));
        addField(DELIB_FNCCAT_EXHAUST_ENABLECONDITION = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_EXHAUST_ENABLECONDITION, "Condition Exhaust"));
        addField(DELIB_FNCCAT_EXHAUST_ASSESSMENTCOMMENT = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_EXHAUST_ASSESSMENTCOMMENT, "Reason for Exhaust assessment"));
        addField(DELIB_FNCCAT_EXHAUST_REVIEWDONE = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_EXHAUST_REVIEWDONE, "Review Exhaust done by"));
        addField(DELIB_FNCCAT_OBD_EVALUATION = new DmRq1Field_Enumeration(this, rq1Irm.DELIB_FNCCAT_OBD_EVALUATION, "OBD"));
        addField(DELIB_FNCCAT_OBD_EVALUATIONCOMMENT = new DmRq1Field_Enumeration(this, rq1Irm.DELIB_FNCCAT_OBD_EVALUATIONCOMMENT, "Category OBD Comment"));
        addField(DELIB_FNCCAT_OBD_ENABLECONDITION = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_OBD_ENABLECONDITION, "Condition OBD"));
        addField(DELIB_FNCCAT_OBD_ASSESSMENTCOMMENT = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_OBD_ASSESSMENTCOMMENT, "Reason for OBD assessment"));
        addField(DELIB_FNCCAT_OBD_REVIEWDONE = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_OBD_REVIEWDONE, "Review OBD done by"));
        addField(DELIB_FNCCAT_SAFETY_EVALUATION = new DmRq1Field_Enumeration(this, rq1Irm.DELIB_FNCCAT_SAFETY_EVALUATION, "Safety"));
        addField(DELIB_FNCCAT_SAFETY_ENABLECONDITION = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_SAFETY_ENABLECONDITION, "Condition Safety"));
        addField(DELIB_FNCCAT_SAFETY_ASSESSMENTCOMMENT = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_SAFETY_ASSESSMENTCOMMENT, "Reason for Safety assessment"));
        addField(DELIB_FNCCAT_SAFETY_REVIEWDONE = new DmRq1Field_Text(this, rq1Irm.DELIB_FNCCAT_SAFETY_REVIEWDONE, "Review Safety done by"));

        DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_EVALUATION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_CONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FCCHANGEDESCRIPTION_HEXNEUTRAL_REASON.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_EVALUATION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_CONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FCCHANGEDESCRIPTION_FUNCTIONALNEUTRAL_REASON.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_EXHAUST_EVALUATION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_EXHAUST_EVALUATIONCOMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_EXHAUST_ENABLECONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_EXHAUST_ASSESSMENTCOMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_EXHAUST_REVIEWDONE.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_OBD_EVALUATION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_OBD_EVALUATIONCOMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_OBD_ENABLECONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_OBD_ASSESSMENTCOMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_OBD_REVIEWDONE.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_SAFETY_EVALUATION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_SAFETY_ENABLECONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_SAFETY_ASSESSMENTCOMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        DELIB_FNCCAT_SAFETY_REVIEWDONE.setAttribute(FIELD_FOR_BULK_OPERATION);

        // User Assistant
        addRule(new ConfigurableRuleManagerRule_IRM(this));
    }

    @Override
    final public String getElementClass() {
        return ("IRM");
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    final public boolean isOpen() {
        return (LifeCycleState_IRM.getAllOpenState().contains((LifeCycleState_IRM) LIFE_CYCLE_STATE.getValue()));
    }

    @Override
    final public boolean isCanceled() {
        return ((LifeCycleState_IRM) LIFE_CYCLE_STATE.getValue() == LifeCycleState_IRM.CANCELED);
    }

    final public boolean isConflicted() {
        return ((LifeCycleState_IRM) LIFE_CYCLE_STATE.getValue() == LifeCycleState_IRM.CONFLICTED);
    }

    final public boolean isCanceledOrConflicted() {
        return (isCanceled() || isConflicted());
    }

    @Override
    public boolean save() {
        checkIsPilotOnSave();
        return super.save();
    }

    @Override
    public boolean save(Rq1AttributeName... fieldOrder) {
        checkIsPilotOnSave();
        return super.save(fieldOrder);
    }

    private void checkIsPilotOnSave() {
        if ((isCanceled() == true) && (IS_PILOT.getValue() == YesNoEmpty.YES)) {
            IS_PILOT.setValue(YesNoEmpty.NO);
        }
    }

    final public EcvEnumeration getScope() {
        return (SCOPE.getValue());
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LIFE_CYCLE_STATE.getValidInputValues());
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTitle() + " " + HAS_MAPPED_RELEASE.getElement().getTypeIdTitle() + " " + HAS_MAPPED_ISSUE.getElement().getTypeIdTitle();
    }

    @Override
    public String getIdForSubject() {
        return "IRM " + getId();
    }

    @Override
    public EcvMapSet<AssigneeFilter, DmRq1Project> getProjects() {
        EcvMapSet<AssigneeFilter, DmRq1Project> set = new EcvMapSet<>();
        if (HAS_MAPPED_ISSUE.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.IRMISSUEPROJECT, HAS_MAPPED_ISSUE.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.IRM_PROJECT, HAS_MAPPED_ISSUE.getElement().PROJECT.getElement());
        }
        if (HAS_MAPPED_RELEASE.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.IRMRELEASEPROJECT, HAS_MAPPED_RELEASE.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.IRM_PROJECT, HAS_MAPPED_RELEASE.getElement().PROJECT.getElement());
        }
        return set;
    }

    public String getRank() {
        return (FLOW_RANK.getValueAsText());
    }

    @Override
    public String getFlowVersion() {
        return (FLOW_VERSION.getValueAsText());
    }

    @Override
    public String getRequestedDate() {
        return (FLOW_R_DATE.getValueAsText());
    }

    @Override
    public String getRemainingEffort() {
        return (FLOW_R_EFFORT.getValueAsText());
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

    @Override
    public FullKitSize getSize() {
        return ((FullKitSize) FLOW_SIZE.getValue());
    }

    public FullKitSize getAnalysisSize() {
        return ((FullKitSize) FLOW_SIZE_ANALYSIS.getValue());
    }

    @Override
    public InternalRank getInternalRank() throws InternalRank.BuildException {
        if (FLOW_INTERNAL_RANK != null && FLOW_INTERNAL_RANK.getValue() != null && !FLOW_INTERNAL_RANK.getValue().isEmpty()) {
            internalRank = new InternalRank(Long.valueOf(FLOW_INTERNAL_RANK.getValue()));
            return internalRank;

        } else {
            return null;
        }

    }

    public InternalRank getExistingIrmRank() {
        return (internalRank);

    }

    public String getExpertAvailEffort() {
        return FLOW_EXP_AVAL_EFFORT.getValueAsText();
    }

    public String getISwIrmTaskStatus() {
        return FLOW_ISW_IRM_TASK.getValueAsText();
    }

    @Override
    public String getGroupId() {
        return FLOW_GROUP.getValueAsText();
    }

    public String getIRMGroupStatus() {
        return FLOW_IRM_GROUP.getValueAsText();
    }

    public void setExistingIFdRank(InternalRank ifdRank) {
        internalRank = ifdRank;
        FLOW_INTERNAL_RANK.setValue(ifdRank.toString());
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

    @Override
    public String getClusterID() {
        return FLOW_CLUSTERID.getValueAsText();
    }

    public String getSwimlaneHeading() {
        return FLOW_BOARD_SWIMLANE_HEADING.getValueAsText();
    }

    @Override
    public EcvDate getToRedDate() {
        return FLOW_TO_RED_DATE.getValue();
    }

    public EcvDate getToRedAnalysisDate() {
        return FLOW_TO_RED_DATE_ANALYSIS.getValue();
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
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }

    final public String getIntegrationStep() {
        return (INTEGRATION_STEP.getValue());
    }

    public String getExcFromBoard() {
        return FLOW_EXC_BOARD.getValueAsText();
    }
}
