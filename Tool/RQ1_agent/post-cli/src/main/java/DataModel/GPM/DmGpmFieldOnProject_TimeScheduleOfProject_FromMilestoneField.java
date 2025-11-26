/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.Rq1.Fields.DmRq1Field_MilestoneTable;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import util.EcvMapList;

/**
 *
 * @author GUG2WI
 */
class DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestoneField {

    final private static Logger LOGGER = Logger.getLogger(DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestoneField.class.getCanonicalName());

    final private DmRq1Project project;

    /**
     * A name may exist multiple times in the milestone table. Therefore a map
     * of lists is used for caching the table content. Processing the table each
     * time from top to bottom minimizes the risk of loosing entries.
     */
    private EcvMapList<String, DmGpmMilestone_FromMilestoneField> projectMilestoneCache = new EcvMapList<>();
    private boolean projectMilestoneCacheValid = false;

    private List<DmGpmMilestone_FromMilestoneField> content = null;

    DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestoneField(DmRq1Project project) {
        assert (project != null);

        this.project = project;
    }

    List<DmGpmMilestone_FromMilestoneField> getElementList() {
        if (content == null) {
            loadElementList();
        }
        return (content);
    }

    public synchronized void loadElementList() {

        EcvMapList<String, DmGpmMilestone_FromMilestoneField> newCache = new EcvMapList<>();
        List<DmGpmMilestone_FromMilestoneField> newContent = new ArrayList<>();

        for (DmRq1Field_MilestoneTable.Record record : project.MILESTONES_TABLE.getValueAsList()) {

            DmGpmMilestone_FromMilestoneField milestone = projectMilestoneCache.removeFirst(record.getName());
            if (milestone == null) {
                milestone = new DmGpmMilestone_FromMilestoneField(project, record);
            }
            newCache.add(record.getName(), milestone);
            newContent.add(milestone);
        }

        projectMilestoneCache = newCache;
        projectMilestoneCacheValid = true;

        content = newContent;
    }

    private synchronized void makeProjectMilestoneCacheValid() {
        if (projectMilestoneCacheValid == false) {
            loadElementList();
        }
    }

    private synchronized void addToContent(DmGpmMilestone_FromMilestoneField newMilestone) {
        assert (newMilestone != null);
        if (content != null) {
            content.add(newMilestone);
        }
    }

    void addMilestone(DmGpmMilestone_FromMilestoneField newMilestone) {
        assert (newMilestone != null);
        assert (projectMilestoneCache.getKey(newMilestone) == null);
        makeProjectMilestoneCacheValid();

        projectMilestoneCache.add(newMilestone.NAME.getValueAsText(), newMilestone);
        project.MILESTONES_TABLE.addRecord(newMilestone.NAME.getValue(), newMilestone.DATE.getDate(), ((ExportCategory) newMilestone.EXPORT_CATEGORY.getValue()), ((ExportScope) newMilestone.EXPORT_SCOPE.getValue()));
        addToContent(newMilestone);
    }

    void removeMilestone(DmGpmMilestone_FromMilestoneField milestoneToRemove) {
        assert (milestoneToRemove != null);
        makeProjectMilestoneCacheValid();

        projectMilestoneCache.remove(milestoneToRemove.NAME.getValueAsText());
        project.MILESTONES_TABLE.removeRecord(milestoneToRemove.NAME.getValueAsText());
    }

    void updateMilestone(DmGpmMilestone_FromMilestoneField changedMilestone) {
        assert (changedMilestone != null);
        assert (projectMilestoneCache != null) : changedMilestone.toString();
        makeProjectMilestoneCacheValid();

        //
        // Check if milestone name was changed.
        //
        String oldMilestoneName = projectMilestoneCache.getKey(changedMilestone);
        assert (oldMilestoneName != null) : changedMilestone.toString();
        String newMilestoneName = changedMilestone.NAME.getValue();
        if (oldMilestoneName.equals(newMilestoneName) == false) {
            // Adapt cache and inform field for workitems on milestones
            projectMilestoneCache.removeValue(oldMilestoneName, changedMilestone);
            projectMilestoneCache.add(newMilestoneName, changedMilestone);
            project.WORKITEMS_ON_MILESTONES.renameMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, GpmXmlMilestoneSource.RQ1_PROJECT, oldMilestoneName, newMilestoneName);
        }

        //
        // Update Milestone field
        //
        List<DmRq1Field_MilestoneTable.Record> newRecordList = new ArrayList<>();
        for (EcvMapList.MapListEntry<String, DmGpmMilestone_FromMilestoneField> entry : projectMilestoneCache.getContentAsList()) {
            DmGpmMilestone_FromMilestoneField milestone = entry.value;
            newRecordList.add(new DmRq1Field_MilestoneTable.Record(milestone.NAME.getValue(), milestone.DATE.getDate(), ((ExportCategory) milestone.EXPORT_CATEGORY.getValue()), ((ExportScope) milestone.EXPORT_SCOPE.getValue())));
        }
        project.MILESTONES_TABLE.setValueAsList(newRecordList);
    }

    void reload() {
        content = null;
    }

}
