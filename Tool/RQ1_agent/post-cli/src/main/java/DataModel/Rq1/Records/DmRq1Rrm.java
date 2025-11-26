/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
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
import Rq1Cache.Records.Rq1Rrm;
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Enumerations.LifeCycleState_RRM;
import util.EcvDate;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Rrm extends DmRq1MapElement implements DmFlowIssue {

    final public DmRq1Field_Table<Rq1XmlTable_ChangesToConfiguration> CHANGES_TO_CONFIGURATION;
    final public DmRq1Field_Table<Rq1XmlTable_ChangesToPartlist> CHANGES_TO_PARTLIST;
    final public DmRq1Field_Text CONTRIBUTION_TO_DERIVATIVES;
    final public DmRq1Field_Enumeration INTEGRATION_ACTION;
    final public DmRq1Field_IntegrationStepMap INTEGRATION_STEP;
    final public DmRq1Field_Enumeration LIFE_CYCLE_STATE;
    final public DmRq1Field_Enumeration PRIORITY;
    final public DmRq1Field_Text PRIORITY_COMMENT;
    final public DmRq1Field_Enumeration IS_PILOT;
    final public DmRq1Field_Date REQUESTED_DELIVERY_DATE;

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
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;

    public DmRq1Rrm(String mapType, Rq1Rrm rq1Rrm) {
        super(mapType, rq1Rrm);
        //
        // Create and add fields
        //
        addField(CHANGES_TO_CONFIGURATION = new DmRq1Field_Table<>(this, rq1Rrm.CHANGES_TO_CONFIGURATION_TABLE, "Changes To Configuration"));
        addField(CHANGES_TO_PARTLIST = new DmRq1Field_Table<>(this, rq1Rrm.CHANGES_TO_PARTLIST, "Changes To Partlist"));
        addField(CONTRIBUTION_TO_DERIVATIVES = new DmRq1Field_Text(this, rq1Rrm.CONTRIBUTION_TO_DERIVATIVES, "Contribution To Derivatives"));
        CONTRIBUTION_TO_DERIVATIVES.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(INTEGRATION_STEP = new DmRq1Field_IntegrationStepMap(this, rq1Rrm.INTEGRATION_STEP, "Integration Step"));
        INTEGRATION_STEP.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        INTEGRATION_STEP.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        INTEGRATION_STEP.setAttribute(DmFieldI.Attribute.INTEGRATION_STEP);

        addField(INTEGRATION_ACTION = new DmRq1Field_Enumeration(this, rq1Rrm.INTEGRATION_ACTION, "Integration Action"));
        INTEGRATION_ACTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        INTEGRATION_ACTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(IS_PILOT = new DmRq1Field_Enumeration(this, rq1Rrm.IS_PILOT, "Is Pilot"));
        IS_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        IS_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(LIFE_CYCLE_STATE = new DmRq1Field_Enumeration(this, rq1Rrm.LIFE_CYCLE_STATE, "Life Cycle State"));
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);

        addField(PRIORITY = new DmRq1Field_Enumeration(this, rq1Rrm.PRIORITY, "Priority"));
        PRIORITY.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PRIORITY.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(PRIORITY_COMMENT = new DmRq1Field_Text(this, rq1Rrm.PRIORITY_COMMENT, "Priority Comment"));
        PRIORITY_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.MULTILINE_TEXT);
        PRIORITY_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        PRIORITY_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(REQUESTED_DELIVERY_DATE = new DmRq1Field_Date(this, rq1Rrm.REQUESTED_DELIVERY_DATE, "Requested Delivery Date"));
        REQUESTED_DELIVERY_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        REQUESTED_DELIVERY_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(new DmRq1Field_Text(this, rq1Rrm.SUPPRESS_VALIDATION, "Suppress Validation"));
        addField(new DmRq1Field_Text(this, rq1Rrm.SUPPRESS_VALIDATION_COMMENT, "Suppress Validation Comment"));
        addField(new DmRq1Field_Text(this, rq1Rrm.TLD, "TLD"));
        addField(new DmRq1Field_Text(this, rq1Rrm.TOOL_VERSION, "Tool Version"));

        //
        // Fields for flow framework
        //
        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, rq1Rrm.FLOW_KIT_STATUS, "Full-Kit-Status"));
        addField(FLOW_RANK = new DmRq1Field_Text(this, rq1Rrm.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, rq1Rrm.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, rq1Rrm.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, rq1Rrm.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, rq1Rrm.FLOW_CLUSTERNAME, "Flow Cluster Name"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, rq1Rrm.FLOW_CLUSTERID, "Flow Cluster ID"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, rq1Rrm.FLOW_SIZE, "Size"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, rq1Rrm.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, rq1Rrm.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, rq1Rrm.FLOW_STATUS, "Task Status"));
        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, rq1Rrm.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, rq1Rrm.FLOW_GROUP, "Groups"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, rq1Rrm.TO_RED_DATE, "To RED Date"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, rq1Rrm.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, rq1Rrm.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, rq1Rrm.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, rq1Rrm.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, rq1Rrm.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, rq1Rrm.TARGET_DATE, "Planned-Date"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, rq1Rrm.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_IRM_GROUP = new DmRq1Field_Text(this, rq1Rrm.FLOW_IRM_GROUP, "IRM Group"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(rq1Rrm.FLOW_BLOCKER_TABLE, rq1Rrm.FLOW_SUBTASK_TABLE,"Blocker with Subtask Title"));
    }

    @Override
    final public String getElementClass() {
        return ("RRM");
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    @Override
    final public boolean isCanceled() {
        return (isInLifeCycleState(LifeCycleState_RRM.CANCELED));
    }

    final public boolean isConflicted() {
        return (isInLifeCycleState(LifeCycleState_RRM.CONFLICTED));
    }

    final public boolean isCanceledOrConflicted() {
        return (isCanceled() || isConflicted());
    }

    final public boolean isOpen() {
        return (LifeCycleState_RRM.getAllOpenState().contains((LifeCycleState_RRM) LIFE_CYCLE_STATE.getValue()));
    }

    final public EcvDate getRequestedDeliveryDate() {
        return (REQUESTED_DELIVERY_DATE.getValue());
    }

    final public String getIntegrationStep() {
        return (INTEGRATION_STEP.getValueAsText());
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LIFE_CYCLE_STATE.getValidInputValues());
    }

    @Override
    public String getIdForSubject() {
        return "RRM " + getId();
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTypeIdTitle();
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

    @Override
    public String getGroupId() {
        return FLOW_GROUP.getValueAsText();
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

    public KingState getKingState() {
        return ((KingState) KING_STATE.getValue());
    }

    public ExpertState getExpertState() {
        return ((ExpertState) EXPERT_STATE.getValue());
    }

    public String getExpertAvailEffort() {
        return FLOW_EXP_AVAL_EFFORT.getValueAsText();
    }

    @Override
    public void reload() {
        internalRank = null;
        super.reload();
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }

    @Override
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    public String getIRMGroupStatus() {
        return FLOW_IRM_GROUP.getValueAsText();
    }

    public abstract DmRq1Field_Reference<? extends DmRq1Release> getParentField();
}
