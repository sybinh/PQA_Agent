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
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Enumerations.ApprovalHardwareMod;

/**
 * Implements the cache for an Issue SW record of the RQ1 database.
 *
 * @author gug2wi
 */
public class Rq1IssueMod extends Rq1HardwareIssue {

    final public Rq1DatabaseField_Enumeration APPROVAL;

    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Enumeration FLOW_KIT_STATUS;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Text FLOW_RANK;
    final public Rq1XmlSubField_Text FLOW_R_DATE;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Text FLOW_CLUSTERNAME;
    final public Rq1XmlSubField_Text FLOW_CLUSTERID;
    final public Rq1XmlSubField_Text FLOW_GROUP;
    final public Rq1XmlSubField_Text FLOW_R_EFFORT;
    final public Rq1XmlSubField_Text FLOW_NO_OF_DEVELOPERS;
    final public Rq1XmlSubField_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Text FLOW_INC_AS_TASK;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Text FLOW_EXC_BOARD;
    final public Rq1XmlSubField_Text FLOW_EST_SHEET_NAME;

    public Rq1IssueMod() {
        super(Rq1NodeDescription.ISSUE_HW_MOD);

        ALLOCATION.setReadOnly();

        addField(APPROVAL = new Rq1DatabaseField_Enumeration(this, "Approval", ApprovalHardwareMod.values()));

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
        addField(FLOW_R_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "R_EFFORT"));
        addField(FLOW_NO_OF_DEVELOPERS = new Rq1XmlSubField_Text(this, FLOW, "NB_D"));
        addField(FLOW_SIZE = new Rq1XmlSubField_Enumeration(this, FLOW, "SIZE", FullKitSize.values(), null));
        addField(FLOW_CLUSTERID = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER_ID"));
        addField(FLOW_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "STATUS", TaskStatus.values(), null));
        addField(TO_RED_DATE = new Rq1XmlSubField_Date(this, FLOW, "TO_RED"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new Rq1XmlSubField_Text(this, FLOW, "SL_H"));
        addField(TARGET_DATE = new Rq1XmlSubField_Date(this, FLOW, "T_DATE"));
        addField(FLOW_INC_AS_TASK = new Rq1XmlSubField_Text(this, FLOW, "INC_AS_TASK"));
        addField(KING_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "KING", KingState.values(), null));
        addField(FLOW_SUBTASK_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowSubTask(), FLOW, "SUBTASK"));
        addField(FLOW_BLOCKER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowBlocker(), FLOW, "BLOCKER"));
        addField(EXPERT_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "EXP", ExpertState.values(), null));
        addField(FLOW_EXP_AVAl_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "EXP_EFFORT"));
        addField(CRITICAL_RESOURCE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CriticalResource(), FLOW, "C_RES"));
        addField(FLOW_EXC_BOARD = new Rq1XmlSubField_Text(this, FLOW, "EXC_FROM_BOARD"));
        addField(FLOW_EST_SHEET_NAME = new Rq1XmlSubField_Text(this, FLOW, "EST_SHEET_NAME"));
        CRITICAL_RESOURCE.setOptional();
        TARGET_DATE.setOptional();
        FLOW_EXP_AVAl_EFFORT.setOptional();
        FLOW_EXC_BOARD.setOptional();
        FLOW_INC_AS_TASK.setOptional();

    }
}
