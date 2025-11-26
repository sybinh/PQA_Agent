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
import Rq1Data.Enumerations.CategoryEcuRelease;
import Rq1Data.Enumerations.HardwareEcuReleaseClassification;

/**
 *
 * @author gug2wi
 */
public class Rq1EcuRelease extends Rq1HardwareRelease {

    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Enumeration CLASSIFICATION;

    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Enumeration FLOW_KIT_STATUS;
    final public Rq1XmlSubField_Text FLOW_RANK;
    final public Rq1XmlSubField_Text FLOW_GROUP;
    final public Rq1XmlSubField_Text FLOW_IRM_GROUP;
    final public Rq1XmlSubField_Text FLOW_R_DATE;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Text FLOW_CLUSTERNAME;
    final public Rq1XmlSubField_Text FLOW_CLUSTERID;
    final public Rq1XmlSubField_Text FLOW_R_EFFORT;
    final public Rq1XmlSubField_Text FLOW_NO_OF_DEVELOPERS;
    final public Rq1XmlSubField_Text FLOW_ISW_IRM_TASK;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public Rq1XmlSubField_Text PARENT_SWIMLANE;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public Rq1XmlSubField_Text FLOW_INCLUDE_TO_LIST;

    public Rq1EcuRelease() {
        super(Rq1NodeDescription.HW_ECU_RELEASE);

        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CATEGORY, CategoryEcuRelease.values()));

        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", HardwareEcuReleaseClassification.values()));
        CLASSIFICATION.acceptInvalidValuesInDatabase();

        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        addField(FLOW_VERSION = new Rq1XmlSubField_Text(this, FLOW, "V"));
        addField(FLOW_KIT_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "FKS", FullKitStatus.values(), null));
        addField(FLOW_RANK = new Rq1XmlSubField_Text(this, FLOW, "RANK"));
        addField(FLOW_GROUP = new Rq1XmlSubField_Text(this, FLOW, "GROUP"));
        addField(FLOW_IRM_GROUP = new Rq1XmlSubField_Text(this, FLOW, "IRM_GROUP"));
        addField(FLOW_R_DATE = new Rq1XmlSubField_Text(this, FLOW, "R_DATE"));
        addField(FLOW_INTERNAL_RANK = new Rq1XmlSubField_Text(this, FLOW, "INTERNAL_RANK"));
        addField(FLOW_SIZE = new Rq1XmlSubField_Enumeration(this, FLOW, "SIZE", FullKitSize.values(), null));
        addField(FLOW_CLUSTERNAME = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER"));
        addField(FLOW_CLUSTERID = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER_ID"));
        addField(FLOW_R_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "R_EFFORT"));
        addField(FLOW_NO_OF_DEVELOPERS = new Rq1XmlSubField_Text(this, FLOW, "NB_D"));
        addField(FLOW_ISW_IRM_TASK = new Rq1XmlSubField_Text(this, FLOW, "TASK"));
        addField(FLOW_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "STATUS", TaskStatus.values(), null));
        addField(TO_RED_DATE = new Rq1XmlSubField_Date(this, FLOW, "TO_RED"));
        addField(TARGET_DATE = new Rq1XmlSubField_Date(this, FLOW, "T_DATE"));
        addField(FLOW_SUBTASK_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowSubTask(), FLOW, "SUBTASK"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new Rq1XmlSubField_Text(this, FLOW, "SL_H"));
        addField(PARENT_SWIMLANE = new Rq1XmlSubField_Text(this, FLOW, "PARENT_SWIMLANE"));
        addField(KING_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "KING", KingState.values(), null));
        addField(FLOW_BLOCKER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowBlocker(), FLOW, "BLOCKER"));
        addField(EXPERT_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "EXP", ExpertState.values(), null));
        addField(FLOW_EXP_AVAl_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "EXP_EFFORT"));
        addField(CRITICAL_RESOURCE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CriticalResource(), FLOW, "C_RES"));
        addField(FLOW_INCLUDE_TO_LIST = new Rq1XmlSubField_Text(this, FLOW, "INC_TO_LIST"));
        CRITICAL_RESOURCE.setOptional();
        FLOW_EXP_AVAl_EFFORT.setOptional();
        FLOW_INCLUDE_TO_LIST.setOptional();
        FLOW.setOptional();
        FLOW_IRM_GROUP.setOptional();
    }

}
