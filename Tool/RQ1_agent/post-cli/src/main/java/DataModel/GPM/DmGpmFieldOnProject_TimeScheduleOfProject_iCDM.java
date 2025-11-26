/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_iCDM;
import DataModel.Rq1.Records.DmRq1Project;
import RestClient.iCDM.RestClient_iCDM_Milestone;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DmGpmFieldOnProject_TimeScheduleOfProject_iCDM extends DmGpmFieldOnProject_TimeScheduleOfProject_SubList<DmGpmMilestone_From_iCDM, DmRq1Field_CpcRepositoryTable_iCDM> {

    final private static Map<String, DmGpmMilestone_From_iCDM> cache = new TreeMap<>();

    DmGpmFieldOnProject_TimeScheduleOfProject_iCDM(DmGpmFieldOnProject_TimeScheduleOfProject timeScheduleField, DmRq1Project project) {
        super(timeScheduleField, project, project.CPC_iCDM, DependencyType.PROJECT);
    }

    @Override
    final protected List<DmGpmMilestone_From_iCDM> loadElementList() {
        Collection<String> pidcVerIds = sourceField.getPidcVersIdList();
        return (getMilestonesFromiCDM(pidcVerIds));
    }

    /**
     * Package private to support testing.
     *
     * @param pidcVerIds
     * @return
     */
    List<DmGpmMilestone_From_iCDM> getMilestonesFromiCDM(Collection<String> pidcVerIds) {

        List<DmGpmMilestone_From_iCDM> result = new ArrayList<>();

        if ((pidcVerIds != null) && (pidcVerIds.isEmpty() == false)) {
            // extract pidcVerId from "icdm:pidvid,pidcVerId"
            ArrayList<String> updatedPidcVerIds = new ArrayList<>();
            Pattern pattern = Pattern.compile("icdm:pidvid,([0-9]+)");

            pidcVerIds.forEach((pidcVerId) -> {
                Matcher matcher = pattern.matcher(pidcVerId);
                if (matcher.find()) {
                    updatedPidcVerIds.add(matcher.group(1));
                } else {
                    updatedPidcVerIds.add(pidcVerId);
                }
            });

            List<RestClient_iCDM_Milestone> restMilestones = ICDMClient.getMilestones(updatedPidcVerIds);
            if (restMilestones != null) {
                for (RestClient_iCDM_Milestone restMilestone : restMilestones) {
                    DmGpmMilestone_From_iCDM gpmMilestone = new DmGpmMilestone_From_iCDM(project, restMilestone);
                    addToResultAndCache(result, gpmMilestone);
                }
            } else {
                List<GpmXmlTable_DataOfExternalMilestones.Record> cachedMilestones = project.GPM_DATA_OF_EXTERNAL_MILESTONES.getCachedMilestones(GpmXmlMilestoneSource.I_CDM);
                for (GpmXmlTable_DataOfExternalMilestones.Record cachedMilestone : cachedMilestones) {
                    DmGpmMilestone_From_iCDM gpmMilestone = new DmGpmMilestone_From_iCDM(project, cachedMilestone);
                    addToResultAndCache(result, gpmMilestone);
                }
            }
        }

        return (result);
    }

    private void addToResultAndCache(List<DmGpmMilestone_From_iCDM> result, DmGpmMilestone_From_iCDM newMilestone) {
        DmGpmMilestone_From_iCDM oldMilestone = cache.get(newMilestone.getId());
        if (oldMilestone != null) {
            result.add(oldMilestone);
        } else {
            cache.put(newMilestone.getId(), newMilestone);
            result.add(newMilestone);
        }
    }

}
