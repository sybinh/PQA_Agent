/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_HIS;
import DataModel.Rq1.Records.DmRq1Project;
import RestClient.Exceptions.RestException;
import RestClient.HIS.RestClient_HisMilestone;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class DmGpmFieldOnProject_TimeScheduleOfProject_HIS extends DmGpmFieldOnProject_TimeScheduleOfProject_SubList<DmGpmMilestone_FromHis, DmRq1Field_CpcRepositoryTable_HIS> {

    final private static Logger LOGGER = Logger.getLogger(DmGpmFieldOnProject_TimeScheduleOfProject_HIS.class.getCanonicalName());

    DmGpmFieldOnProject_TimeScheduleOfProject_HIS(DmGpmFieldOnProject_TimeScheduleOfProject timeScheduleField, DmRq1Project project) {
        super(timeScheduleField, project, project.CPC_HIS, DependencyType.PROJECT);
    }

    @Override
    final protected List<DmGpmMilestone_FromHis> loadElementList() {
        List<DmGpmMilestone_FromHis> newContent = new ArrayList<>();
        Set<String> ecuHwIds = new TreeSet<>();
        ecuHwIds.addAll(sourceField.getAllTargetEcuId());
        if (ecuHwIds.isEmpty() == false) {
            List<RestClient_HisMilestone> hisMilestones = HisClient.getMilestones(ecuHwIds);
            if (hisMilestones != null) {
                for (RestClient_HisMilestone hisMilestone : hisMilestones) {
                    ecuHwIds.remove(hisMilestone.getEcuHardwareId()); // Remove hardware IDs for which milestones were received.
                    DmGpmMilestone_FromHis gpmMilestone = new DmGpmMilestone_FromHis(project, hisMilestone);
                    newContent.add(gpmMilestone);
                }
                if (ecuHwIds.isEmpty() == false) {
                    String message;
                    RestException ex;
                    if (ecuHwIds.size() == 1) {
                        message = "No milestones received for target ECU ID " + ecuHwIds.iterator().next() + ".";
                    } else {
                        message = "No milestones received for target ECU IDs "
                                + ecuHwIds.stream().collect(Collectors.joining(", "));
                    }
                    LOGGER.log(Level.WARNING, message);
                }
            } else {
                List<GpmXmlTable_DataOfExternalMilestones.Record> cachedMilestones = project.GPM_DATA_OF_EXTERNAL_MILESTONES.getCachedMilestones(GpmXmlMilestoneSource.HIS);
                for (GpmXmlTable_DataOfExternalMilestones.Record cachedMilestone : cachedMilestones) {
                    DmGpmMilestone_FromHis gpmMilestone = new DmGpmMilestone_FromHis(project, cachedMilestone);
                    newContent.add(gpmMilestone);
                }
            }
        }

        return (newContent);
    }

}
