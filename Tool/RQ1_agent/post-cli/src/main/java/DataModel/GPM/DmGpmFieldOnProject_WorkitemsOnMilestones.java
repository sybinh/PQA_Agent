/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmElementI;
import DataModel.DmElementI.EditState;
import DataModel.DmElementI.Editor;
import DataModel.DmField;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Records.Rq1SubjectInterface;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_TasksOnMilestones;
import Rq1Data.GPM.GpmXmlTable_TasksOnMilestones.TasksOnMilestonesRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvMapMapList;
import util.EcvMapMapList.MapMapListEntry;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * Manages the assignment of work items to the following types of milestones:
 *
 * - Milestones from the milestone field of the project.
 *
 * - Milestones loaded from HIS.
 *
 * This management is necessary, because the mapping between milestone and
 * workitems is done in the tag field of the project.
 *
 * Workitems on milestones for PVER/PVAR releases are not handled by this class,
 * because the mapping between PVER/PVAR and workitems is done via the standard
 * fields of RQ1.
 *
 * @author GUG2WI
 */
public class DmGpmFieldOnProject_WorkitemsOnMilestones extends DmField implements Editor {

    final private static Logger LOGGER = Logger.getLogger(DmGpmFieldOnProject_WorkitemsOnMilestones.class.getCanonicalName());

    final private DmRq1Project project;
    final private Rq1XmlSubField_Table<GpmXmlTable_TasksOnMilestones> tasksOnMilestonesField;

    // Type, ID/Name, Task
    private EcvMapMapList<GpmXmlMilestoneSource, String, DmRq1WorkItem> milestoneToWorkitemMap = null;
    private boolean pushToDatastorePending = false;

    public DmGpmFieldOnProject_WorkitemsOnMilestones(DmRq1Project project, Rq1XmlSubField_Table<GpmXmlTable_TasksOnMilestones> tasksOnMilestonesField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (project != null);
        assert (tasksOnMilestonesField != null);

        this.project = project;
        this.tasksOnMilestonesField = tasksOnMilestonesField;

        addDependency(project);
    }

    //--------------------------------------------------------------------------
    //
    // Manage map and field in database.
    //
    //--------------------------------------------------------------------------
    private synchronized void loadMap() {
        if (milestoneToWorkitemMap == null) {
            //
            // Load project workitems into cache
            //
            project.ALL_WORKITEMS.getElementList();

            //
            // Run throught table holding the mapping
            //
            EcvMapMapList<GpmXmlMilestoneSource, String, DmRq1WorkItem> newMap = new EcvMapMapList<>();
            EcvTableData data = tasksOnMilestonesField.getDataModelValue();
            List<TasksOnMilestonesRecord> tasksOnMilestones = GpmXmlTable_TasksOnMilestones.DESC.extract(data);
            for (TasksOnMilestonesRecord taskOnMilestone : tasksOnMilestones) {

                String rq1Id = taskOnMilestone.getTaskId();
                if (Rq1SubjectInterface.isRq1Id(rq1Id) == true) {

                    DmRq1ElementInterface element = DmRq1ElementCache.getElement(rq1Id);
                    if (element instanceof DmRq1WorkItem) {
                        newMap.add(taskOnMilestone.getMilestoneType(), taskOnMilestone.getMilestoneIdOrName(), (DmRq1WorkItem) element);
                    } else if (element != null) {
                        LOGGER.warning("Invalid element type " + element.getClass().getName() + " for task " + rq1Id + " on project " + project.toString());
                    } else {
                        LOGGER.warning("Unknown RQ1-ID for task " + rq1Id + " on project " + project.toString());
                    }

                } else {
                    LOGGER.warning("Invalid RQ1-ID for task " + rq1Id + " on project " + project.toString());
                }

            }
            milestoneToWorkitemMap = newMap;
        }
    }

    private synchronized void resetMap() {
        milestoneToWorkitemMap = null;
        pushToDatastorePending = false;
    }

    @Override
    public synchronized boolean pushChangesToDatastore() {
        if (pushToDatastorePending == true) {

            List<TasksOnMilestonesRecord> recordList = new ArrayList<>();
            for (MapMapListEntry<GpmXmlMilestoneSource, String, DmRq1WorkItem> entry : milestoneToWorkitemMap.getContentAsList()) {

                // Prevent saving of data for non existing workitems.
                DmRq1WorkItem wi = entry.value;
                if (wi.existsInDatabase() == true) {
                    recordList.add(new TasksOnMilestonesRecord(entry.key1, entry.key2, entry.value.getId()));
                } else {
                    LOGGER.log(Level.SEVERE, "Saving workitem tags for " + project.toString(), new Error("Workitem with title '" + wi.getTitle() + "' does not exist in RQ1."));
                }
            }

            EcvTableData data = GpmXmlTable_TasksOnMilestones.DESC.pack(recordList);
            tasksOnMilestonesField.setDataModelValue(data);

            pushToDatastorePending = false;
            project.editStateChanged();

            return (true);

        } else {
            return (false);
        }
    }

    //--------------------------------------------------------------------------
    //
    // Access the data
    //
    //--------------------------------------------------------------------------
    public List<DmRq1WorkItem> getWorkitemsForMilestone(GpmXmlMilestoneSource milestoneSource, String milestoneIdOrName) {
        assert (milestoneSource != null);
        assert (milestoneIdOrName != null);
        assert (milestoneIdOrName.isEmpty() == false);

        loadMap();

        List<DmRq1WorkItem> result = milestoneToWorkitemMap.get(milestoneSource, milestoneIdOrName);
        if (result == null) {
            return (new ArrayList<>(0));
        }
        return (result);
    }

    /**
     * The given workitem shall be added to the given milestone.
     *
     * The workitem might already be added to the given milestone or to some
     * other milestone. If it is already added to the given milestone, then no
     * change will be done to the data model. If it is added to some other
     * milestone, then it will be removed from that other milestone.
     *
     * @param milestoneIdOrName The name of the milestone to which the work item
     * shall be added.
     * @param workitem The work item that shall be added.
     * @return True, if the adding changed something in the stored data.
     */
    public boolean addWorkitemForMilestone(GpmXmlMilestoneSource milestoneSource, String milestoneIdOrName, DmRq1WorkItem workitem) {
        assert (milestoneSource != null);
        assert (milestoneIdOrName != null);
        assert (milestoneIdOrName.isEmpty() == false);
        assert (workitem != null);

        loadMap();

        MapMapListEntry<GpmXmlMilestoneSource, String, DmRq1WorkItem> existingEntry = milestoneToWorkitemMap.getFirstEntry(workitem);
        if (existingEntry != null) {
            if ((milestoneSource == existingEntry.key1) && (milestoneIdOrName.equals(existingEntry.key2) == true)) {
                return (false); // no change
            } else {
                milestoneToWorkitemMap.removeEntry(existingEntry);
            }
        }

        milestoneToWorkitemMap.addValueUnique(milestoneSource, milestoneIdOrName, workitem);

        pushToDatastorePending = true;

        project.editStateChanged();

        fireFieldChanged();

        return (true);
    }

    /**
     * Renames a milestone in the data structure that hold the assignment
     * between workitems and milestones.
     *
     * @param oldMilestoneSource
     * @param oldName
     * @param newIdOrName
     */
    public void renameMilestone(GpmXmlMilestoneSource oldMilestoneSource, GpmXmlMilestoneSource newMilestoneSource, String oldName, String newIdOrName) {
        assert (oldMilestoneSource != null);
        assert (newMilestoneSource != null);
        assert (oldName != null);
        assert (oldName.isEmpty() == false);
        assert (newIdOrName != null);
        assert (newIdOrName.isEmpty() == false);

        loadMap();

        List<DmRq1WorkItem> workItemsOnMilestone = milestoneToWorkitemMap.removeList(oldMilestoneSource, oldName);
        if (workItemsOnMilestone != null) {
            milestoneToWorkitemMap.addAll(newMilestoneSource, newIdOrName, workItemsOnMilestone);
            pushToDatastorePending = true;
            project.addChangeListener(this);
            project.editStateChanged();
        }

    }

    public boolean removeWorkitemForMilestone(GpmXmlMilestoneSource milestoneSource, String milestoneIdOrName, DmRq1WorkItem workitem) {
        assert (milestoneSource != null);
        assert (milestoneIdOrName != null);
        assert (milestoneIdOrName.isEmpty() == false);
        assert (workitem != null);

        loadMap();

        milestoneToWorkitemMap.removeEntry(milestoneSource, milestoneIdOrName, workitem);

        pushToDatastorePending = true;

        project.editStateChanged();

        fireFieldChanged();

        return (true);
    }

    /**
     * The given workitem is checked weather its under the milestone or not.
     *
     * @param wi is the workitem for which its availability under milestone is
     * checked
     * @return a boolean value. If TRUE then given workitem is under a milestone
     * else this method will return FALSE.
     */
    public boolean isWorkitemOnMilestone(DmRq1WorkItem wi) {
        List<EcvTableRow> milestoneWorkitems = tasksOnMilestonesField.getDataModelValue().getRows();
        for (EcvTableRow currentRow : milestoneWorkitems) {
            if (currentRow.getValueAt(GpmXmlTable_TasksOnMilestones.DESC.TASK_ID).equals(wi.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

    @Override
    public EditState getEditState(DmElementI dmElement) {
        return (pushToDatastorePending ? EditState.CHANGED : EditState.UNCHANGED);
    }

    @Override
    public void editStateChanged(DmElementI dmElement) {

    }

    @Override
    public void changed(DmElementI changedElement) {
        resetMap();
    }

}
