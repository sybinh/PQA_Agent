/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
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
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_DerivativeMapping;
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
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Enumerations.IntegrationAction;
import Rq1Data.Enumerations.LifeCycleState_RRM;
import Rq1Data.Enumerations.Priority;
import Rq1Data.Enumerations.YesNoEmpty;

/**
 *
 * @author gug2wi
 */
public class Rq1Rrm extends Rq1Map {

    final public static Rq1AttributeName ATTRIBUTE_HAS_MAPPED_CHILD_RELEASE = new Rq1AttributeName("hasMappedChildRelease");
    final public static Rq1AttributeName ATTRIBUTE_HAS_MAPPED_PARENT_RELEASE = new Rq1AttributeName("hasMappedParentRelease");

    final public Rq1DatabaseField_Text CONTRIBUTION_TO_DERIVATIVES;
    final public Rq1DatabaseField_Enumeration INTEGRATION_ACTION;
    final public Rq1DatabaseField_Text INTEGRATION_STEP;
    final public Rq1DatabaseField_Enumeration IS_PILOT;
    final public Rq1XmlSubField_Enumeration PRIORITY;
    final public Rq1XmlSubField_Text PRIORITY_COMMENT;
    final public Rq1DatabaseField_DerivativeMapping SELECTION_OF_DERIVATIVES;
    final public Rq1DatabaseField_Text SUPPRESS_VALIDATION;
    final public Rq1DatabaseField_Text SUPPRESS_VALIDATION_COMMENT;
    final public Rq1DatabaseField_Text TLD;
    final public Rq1DatabaseField_Text TOOL_VERSION;
    //
    final public Rq1DatabaseField_Date REQUESTED_DELIVERY_DATE;
    //
    final public Rq1DatabaseField_Xml CHANGES_TO_CONFIGURATION_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ChangesToConfiguration> CHANGES_TO_CONFIGURATION_TABLE;
    final public Rq1DatabaseField_Xml CHANGES_TO_PARTLIST_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ChangesToPartlist> CHANGES_TO_PARTLIST;
    //
    final public Rq1DatabaseField_Enumeration LIFE_CYCLE_STATE;
    //
    final public Rq1DatabaseField_Reference HAS_MAPPED_CHILD_RELEASE;
    final public Rq1DatabaseField_Reference HAS_MAPPED_PARENT_RELEASE;

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
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;

    public Rq1Rrm(Rq1LinkDescription mapDescription) {
        super(mapDescription);

        Rq1XmlTable_ChangesToConfiguration ctc = new Rq1XmlTable_ChangesToConfiguration();
        addField(CHANGES_TO_CONFIGURATION_XML = new Rq1DatabaseField_Xml(this, "ChangesToConfiguration").addConverter(ctc));
        addField(CHANGES_TO_CONFIGURATION_TABLE = new Rq1XmlSubField_Table<>(this, ctc, CHANGES_TO_CONFIGURATION_XML, "ChangeConfig"));
        CHANGES_TO_CONFIGURATION_TABLE.addAlternativName("RowName");

        Rq1XmlTable_ChangesToPartlist ctp = new Rq1XmlTable_ChangesToPartlist();
        addField(CHANGES_TO_PARTLIST_XML = new Rq1DatabaseField_Xml(this, "ChangesToPartlist").addConverter(ctp));
        addField(CHANGES_TO_PARTLIST = new Rq1XmlSubField_Table<>(this, ctp, CHANGES_TO_PARTLIST_XML, "ChangePart"));

        addField(CONTRIBUTION_TO_DERIVATIVES = new Rq1DatabaseField_Text(this, "ContributionToDerivatives"));
        addField(HAS_MAPPED_CHILD_RELEASE = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_MAPPED_CHILD_RELEASE, Rq1RecordType.RELEASE));
        addField(HAS_MAPPED_PARENT_RELEASE = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_MAPPED_PARENT_RELEASE, Rq1RecordType.RELEASE));
        addField(INTEGRATION_ACTION = new Rq1DatabaseField_Enumeration(this, "IntegrationAction", IntegrationAction.values()));
        addField(INTEGRATION_STEP = new Rq1DatabaseField_Text(this, "IntegrationStep"));
        addField(IS_PILOT = new Rq1DatabaseField_Enumeration(this, "isPilot", YesNoEmpty.values()));
        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Enumeration(this, "LifeCycleState", LifeCycleState_RRM.values(), LifeCycleState_RRM.NEW));
        addField(PRIORITY = new Rq1XmlSubField_Enumeration(this, TAGS, "Priority", Priority.values(), Priority.EMPTY));
        PRIORITY.setOptional();
        addField(PRIORITY_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "PriorityComment"));
        PRIORITY_COMMENT.setOptional();
        addField(REQUESTED_DELIVERY_DATE = new Rq1DatabaseField_Date(this, "RequestedDeliveryDate"));
        addField(SELECTION_OF_DERIVATIVES = new Rq1DatabaseField_DerivativeMapping(this, "SelectionOfDerivatives"));
        addField(SUPPRESS_VALIDATION = new Rq1DatabaseField_Text(this, "SuppressValidation"));
        addField(SUPPRESS_VALIDATION_COMMENT = new Rq1DatabaseField_Text(this, "SuppressValidationComment"));
        addField(TLD = new Rq1DatabaseField_Text(this, "TLD"));
        addField(TOOL_VERSION = new Rq1DatabaseField_Text(this, "ToolVersion"));

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
        FLOW_IRM_GROUP.setOptional();
        CRITICAL_RESOURCE.setOptional();
        TARGET_DATE.setOptional();
        FLOW_EXP_AVAl_EFFORT.setOptional();
    }

    @Override
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        Rq1AttributeName[] order = {ATTRIBUTE_HAS_MAPPED_PARENT_RELEASE, ATTRIBUTE_HAS_MAPPED_CHILD_RELEASE, ATTRIBUTE_OPERATION_MODE};
        return (super.createInDatabase(order));
    }
}
