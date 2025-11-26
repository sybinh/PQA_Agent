/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_iGPM_MCR_System_Parent_Project;
import DataModel.Rq1.Records.DmRq1Project;
import DataStore.MCR.MCR_Milestone;
import DataStore.MCR.MCR_import;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
class DmGpmFieldOnProject_TimeScheduleOfProject_Mcr extends DmGpmFieldOnProject_TimeScheduleOfProject_SubList<DmGpmMilestone_FromMcr, DmRq1Field_CpcRepositoryTable_iGPM_MCR_System_Parent_Project> {

    DmGpmFieldOnProject_TimeScheduleOfProject_Mcr(DmGpmFieldOnProject_TimeScheduleOfProject timeScheduleField, DmRq1Project project) {
        super(timeScheduleField, project, project.CPC_iGPM_MCR_System_Parent_Project, DependencyType.PROJECT);
    }

    @Override
    final protected List<DmGpmMilestone_FromMcr> loadElementList() {
        Collection<String> planId = project.CPC_iGPM_MCR_System_Parent_Project.getMcrIdList();
        List<DmGpmMilestone_FromMcr> newContent = new ArrayList<>();
        List<MCR_Milestone> mcrMilestoneList = null;

        if (planId.isEmpty() == false) {
            mcrMilestoneList = MCR_import.getMcrMilestoneList(planId);

            if (mcrMilestoneList != null) {
                for (MCR_Milestone milestone : mcrMilestoneList) {
                    DmGpmMilestone_FromMcr milestoneFromMcr = new DmGpmMilestone_FromMcr(milestone, project);
                    newContent.add(milestoneFromMcr);
                }
            } else {
                List<GpmXmlTable_DataOfExternalMilestones.Record> cachedMilestones = project.GPM_DATA_OF_EXTERNAL_MILESTONES.getCachedMilestones(GpmXmlMilestoneSource.MCR);
                for (GpmXmlTable_DataOfExternalMilestones.Record cachedMilestone : cachedMilestones) {
                    DmGpmMilestone_FromMcr gpmMilestone = new DmGpmMilestone_FromMcr(project, cachedMilestone);
                    newContent.add(gpmMilestone);
                }
            }
        }
        return (newContent);
    }

}
