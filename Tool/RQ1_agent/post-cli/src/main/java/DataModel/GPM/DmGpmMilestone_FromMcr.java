/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmConstantField_Date;
import DataModel.DmConstantField_Text;
import DataModel.DmValueFieldI_Date;
import DataModel.Rq1.Records.DmRq1Project;
import DataStore.MCR.MCR_Milestone;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import util.EcvDate;

/**
 *
 * @author CIL83WI
 */
public class DmGpmMilestone_FromMcr extends DmGpmMilestone_FromExternalDatabase {

    final public DmConstantField_Text PTA_ID;
    final public DmConstantField_Text PRJ_ID;
    final public DmConstantField_Text PROJECT_KEY_DISPLAY;
    final public DmConstantField_Text PROJECT;
    final public DmConstantField_Text PLAN_ID;
    final public DmConstantField_Text PLAN_NAME;
    final public DmConstantField_Text PLAN_DESCRIPTION;
    final public DmConstantField_Text PARENT_PTA_ID;
    final public DmConstantField_Text MSP_WPO_ID;
    final public DmConstantField_Text MSP_GROUP_WPO_ID;
    final public DmConstantField_Text WPO_WPI_NUMBER;
    final public DmConstantField_Text WPO_NAME;
    final public DmConstantField_Text ACTIVE_FLAG;
    final public DmConstantField_Text MSP_TASK_INDEX;
    final public DmConstantField_Text MSP_UNIQUE_ID;
    final public DmConstantField_Text OLP_UNIQUE_ID;
    final public DmConstantField_Text TASK_NAME;
    final public DmConstantField_Text TASK_TYPE;
    final public DmValueFieldI_Date PLANNED_START;
    final public DmValueFieldI_Date PLANNED_FINISH;
    final public DmValueFieldI_Date MSP_ACTUAL_START;
    final public DmValueFieldI_Date MSP_ACTUAL_FINISH;
    final public DmConstantField_Text MSP_COMPLETE_PERCENT;
    final public DmConstantField_Text STRUCTURE_KEY;
    final public DmConstantField_Text RESOURCE_GROUP_ID;

    public DmGpmMilestone_FromMcr(MCR_Milestone mcrmilestone, DmRq1Project dmRq1Project) {
        super(Type.MCR_MILESTONE, Type.MCR_MILESTONE.getText(), GpmXmlMilestoneSource.MCR, dmRq1Project);

        addField(this.PTA_ID = new DmConstantField_Text("PTA ID", mcrmilestone.getPTA_ID()));
        addField(this.PRJ_ID = new DmConstantField_Text("PRJ ID", mcrmilestone.getPRJ_ID()));
        addField(this.PROJECT_KEY_DISPLAY = new DmConstantField_Text("Project Key Display", mcrmilestone.getPROJECT_KEY_DISPLAY()));
        addField(this.PROJECT = new DmConstantField_Text("Project", mcrmilestone.getPROJECT()));
        addField(this.PLAN_ID = new DmConstantField_Text("plan ID", mcrmilestone.getPLAN_ID()));
        addField(this.PLAN_NAME = new DmConstantField_Text("Plan Name", mcrmilestone.getPLAN_NAME()));
        addField(this.PLAN_DESCRIPTION = new DmConstantField_Text("Plan Description", mcrmilestone.getPLAN_DESCRIPTION()));
        addField(this.PARENT_PTA_ID = new DmConstantField_Text("Parent PTA ID", mcrmilestone.getPARENT_PTA_ID()));
        addField(this.MSP_WPO_ID = new DmConstantField_Text("MSP WPO ID", mcrmilestone.getMSP_WPO_ID()));
        addField(this.MSP_GROUP_WPO_ID = new DmConstantField_Text("MSP Group WPO ID", mcrmilestone.getMSP_GROUP_WPO_ID()));
        addField(this.WPO_WPI_NUMBER = new DmConstantField_Text("WPO WPI Number", mcrmilestone.getWPO_WPI_NUMBER()));
        addField(this.WPO_NAME = new DmConstantField_Text("WPO Name", mcrmilestone.getWPO_NAME()));
        addField(this.ACTIVE_FLAG = new DmConstantField_Text("Active Flag", mcrmilestone.getACTIVE_FLAG()));
        addField(this.MSP_TASK_INDEX = new DmConstantField_Text("MSP Task Index", mcrmilestone.getMSP_TASK_INDEX()));
        addField(this.MSP_UNIQUE_ID = new DmConstantField_Text("MSP Unique ID", mcrmilestone.getMSP_UNIQUE_ID()));
        addField(this.OLP_UNIQUE_ID = new DmConstantField_Text("OLP Unique ID", mcrmilestone.getOLP_UNIQUE_ID()));
        addField(this.TASK_NAME = new DmConstantField_Text("Task Name", mcrmilestone.getTASK_NAME()));
        addField(NAME = new DmConstantField_Text("Name", mcrmilestone.getTASK_NAME() + " - " + mcrmilestone.getPLAN_NAME() + " (" + mcrmilestone.getPLAN_ID() + ")"));
        addField(this.TASK_TYPE = new DmConstantField_Text("Task Type", mcrmilestone.getTASK_TYPE()));

        addField(PLANNED_START = new DmConstantField_Date("Planned Start", mcrmilestone.getPLANNED_START() != null ? mcrmilestone.getPLANNED_START() : EcvDate.getEmpty()));
        addField(PLANNED_FINISH = new DmConstantField_Date("Planned Finish", mcrmilestone.getPLANNED_FINISH() != null ? mcrmilestone.getPLANNED_FINISH() : EcvDate.getEmpty()));
        addField(DATE = new DmConstantField_Date("Date", mcrmilestone.getPLANNED_FINISH() != null ? mcrmilestone.getPLANNED_FINISH() : EcvDate.getEmpty()));
        addField(MSP_ACTUAL_START = new DmConstantField_Date("MSP Actual Start", mcrmilestone.getMSP_ACTUAL_START() != null ? mcrmilestone.getMSP_ACTUAL_START() : EcvDate.getEmpty()));
        addField(MSP_ACTUAL_FINISH = new DmConstantField_Date("MSP Actual Finish", mcrmilestone.getMSP_ACTUAL_FINISH() != null ? mcrmilestone.getMSP_ACTUAL_FINISH() : EcvDate.getEmpty()));

        addField(this.MSP_COMPLETE_PERCENT = new DmConstantField_Text("MSP Complete Percent", mcrmilestone.getMSP_COMPLETE_PERCENT()));
        addField(this.STRUCTURE_KEY = new DmConstantField_Text("Structure Key", mcrmilestone.getSTRUCTURE_KEY()));
        addField(this.RESOURCE_GROUP_ID = new DmConstantField_Text("Resource Groupe ID", mcrmilestone.getRESOURCE_GROUP_ID()));

        addField(ALL_WORKITEMS = new DmGpmFieldOnMilestone_Workitems("All Workitems", project, GpmXmlMilestoneSource.MCR, PLAN_ID.getValue() + "/" + PTA_ID.getValue(), NAME, project.ALL_WORKITEMS));
    }

    public DmGpmMilestone_FromMcr(DmRq1Project dmRq1Project, GpmXmlTable_DataOfExternalMilestones.Record cachedMilestone) {
        super(Type.MCR_UNKOWN, Type.MCR_UNKOWN.getText(), GpmXmlMilestoneSource.MCR, dmRq1Project);

        String[] id = cachedMilestone.getId().split("/");
        String planId;
        String ptaId;
        if (id.length == 2) {
            planId = id[0];
            ptaId = id[1];
        } else {
            planId = id[0];
            ptaId = id[0];
        }

        addField(this.PTA_ID = new DmConstantField_Text("PTA ID", ptaId));
        addField(this.PRJ_ID = new DmConstantField_Text("PRJ ID", "?"));
        addField(this.PROJECT_KEY_DISPLAY = new DmConstantField_Text("Project Key Display", "?"));
        addField(this.PROJECT = new DmConstantField_Text("Project", "?"));
        addField(this.PLAN_ID = new DmConstantField_Text("plan ID", planId));
        addField(this.PLAN_NAME = new DmConstantField_Text("Plan Name", "?"));
        addField(this.PLAN_DESCRIPTION = new DmConstantField_Text("Plan Description", "?"));
        addField(this.PARENT_PTA_ID = new DmConstantField_Text("Parent PTA ID", "?"));
        addField(this.MSP_WPO_ID = new DmConstantField_Text("MSP WPO ID", "?"));
        addField(this.MSP_GROUP_WPO_ID = new DmConstantField_Text("MSP Group WPO ID", "?"));
        addField(this.WPO_WPI_NUMBER = new DmConstantField_Text("WPO WPI Number", "?"));
        addField(this.WPO_NAME = new DmConstantField_Text("WPO Name", "?"));
        addField(this.ACTIVE_FLAG = new DmConstantField_Text("Active Flag", "?"));
        addField(this.MSP_TASK_INDEX = new DmConstantField_Text("MSP Task Index", "?"));
        addField(this.MSP_UNIQUE_ID = new DmConstantField_Text("MSP Unique ID", "?"));
        addField(this.OLP_UNIQUE_ID = new DmConstantField_Text("OLP Unique ID", "?"));
        addField(this.TASK_NAME = new DmConstantField_Text("Task Name", "?"));
        addField(NAME = new DmConstantField_Text("Name", cachedMilestone.getId() + " - ?"));
        addField(this.TASK_TYPE = new DmConstantField_Text("Task Type", "?"));

        addField(PLANNED_START = new DmConstantField_Date("Planned Start", EcvDate.getEmpty()));
        addField(PLANNED_FINISH = new DmConstantField_Date("Planned Finish", EcvDate.getEmpty()));
        addField(DATE = new DmConstantField_Date("Date", cachedMilestone.getLastConfirmedDate() != null ? cachedMilestone.getLastConfirmedDate() : EcvDate.getEmpty()));
        addField(MSP_ACTUAL_START = new DmConstantField_Date("MSP Actual Start", EcvDate.getEmpty()));
        addField(MSP_ACTUAL_FINISH = new DmConstantField_Date("MSP Actual Finish", EcvDate.getEmpty()));

        addField(this.MSP_COMPLETE_PERCENT = new DmConstantField_Text("MSP Complete Percent", "?"));
        addField(this.STRUCTURE_KEY = new DmConstantField_Text("Structure Key", "?"));
        addField(this.RESOURCE_GROUP_ID = new DmConstantField_Text("Resource Groupe ID", "?"));

        addField(ALL_WORKITEMS = new DmGpmFieldOnMilestone_Workitems("All Workitems", project, GpmXmlMilestoneSource.MCR, planId + "/" + ptaId, NAME, project.ALL_WORKITEMS));

        super.addMarker(new DmGpmWarning_ExternalMilestoneLoadedFromCache(this, GpmXmlMilestoneSource.MCR.getValueInDatabase()));
    }

    @Override
    public String getId() {
        return PLAN_ID.getValue() + "/" + PTA_ID.getValue();
    }

}
