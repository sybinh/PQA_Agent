/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmElementListFieldI;
import DataModel.DmElementListField_FromSource;
import DataModel.DmValueFieldI_Text;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Manages the list of work items that are assigned to a milestone of a project.
 * The mapping of work item to milestone is not stored in the data model object
 * of the milestone, but in the data model object of the project.
 *
 * @author GUG2WI
 */
public class DmGpmFieldOnMilestone_Workitems extends DmElementListField_FromSource<DmRq1WorkItem> {

    final private DmRq1Project project;
    final private GpmXmlMilestoneSource milestoneSource;
    final private DmValueFieldI_Text nameField;
    final private DmElementListFieldI<DmRq1WorkItem> workItemField;

    final private String milestoneId;
    private String currentMilestoneName;

    /**
     * Manages the list of workitems mapped to a milestone on a project. The
     * milestone can be a milestone defined in the milestone field of a project
     * or a milestone loaded from HIS for this project. The class loads the
     * mapping information from the given project and stores changes also in the
     * project.
     *
     * @param nameForUserInterface The name of the field when shown on the GUI.
     * @param project The project to which the milestone belongs to and that
     * stores the assignment of workitems for this milestone.
     * @param milestoneSource The type of milestone.
     * @param milestoneId
     * @param nameField The field that holds the name of the milestone.
     * @param workItemField The field on the project that holds the list of
     * workitems for milestones on a project.
     */
    public DmGpmFieldOnMilestone_Workitems(
            String nameForUserInterface,
            DmRq1Project project,
            GpmXmlMilestoneSource milestoneSource,
            String milestoneId,
            DmValueFieldI_Text nameField,
            DmElementListFieldI<DmRq1WorkItem> workItemField) {
        super(nameForUserInterface);

        assert (project != null);
        assert (milestoneSource != null);
        assert ((milestoneId == null) || (milestoneId.isEmpty() == false));
        assert ((milestoneId != null) || (milestoneSource == GpmXmlMilestoneSource.RQ1_PROJECT));
        assert (nameField != null);
        assert (workItemField != null);

        this.nameField = nameField;
        this.project = project;
        this.milestoneSource = milestoneSource;
        this.milestoneId = milestoneId;
        this.workItemField = workItemField;
        currentMilestoneName = nameField.getValueAsText();
    }

    @Override
    protected Collection<DmRq1WorkItem> loadElementList() {
        addDependency(nameField);
        addDependency(project.WORKITEMS_ON_MILESTONES);

        List<DmRq1WorkItem> result = new ArrayList<>();

        if (milestoneId != null) {
            List<DmRq1WorkItem> workitemsById = project.WORKITEMS_ON_MILESTONES.getWorkitemsForMilestone(milestoneSource, milestoneId);
            result.addAll(workitemsById);
        }

        if (milestoneSource == GpmXmlMilestoneSource.RQ1_PROJECT) {
            List<DmRq1WorkItem> workitems = project.WORKITEMS_ON_MILESTONES.getWorkitemsForMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, currentMilestoneName);
            result.addAll(workitems);
        }

        return (result);
    }

    @Override
    public void addElement(DmRq1WorkItem element) {
        assert (element != null);

        //
        // Prevent adding of workitems which do not yet exist in the RQ1 database.
        // This is done, because the list of workitems would contain invalid information, if the creation of the workitem fails later.
        //
        if (element.existsInDatabase() != true) {
            throw (new Error("addElement(" + element.toString() + "): Workitem does not exist in database. Adding not possible."));
        }

        if (milestoneId != null) {
            project.WORKITEMS_ON_MILESTONES.addWorkitemForMilestone(milestoneSource, milestoneId, element);

            if (milestoneSource == GpmXmlMilestoneSource.RQ1_PROJECT) {
                // Change old entries from name to id
                if (milestoneId.equals(currentMilestoneName) == false) {
                    List<DmRq1WorkItem> workitemsByName = project.WORKITEMS_ON_MILESTONES.getWorkitemsForMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, currentMilestoneName);
                    if (workitemsByName.isEmpty() == false) {
                        project.WORKITEMS_ON_MILESTONES.renameMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, milestoneSource, currentMilestoneName, milestoneId);
                    }
                }
            }
        } else {
            project.WORKITEMS_ON_MILESTONES.addWorkitemForMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, nameField.getValueAsText(), element);
        }
    }

    @Override
    protected void handleDependencyChange() {

        //
        // Check if name was changed
        //
        String newMilestoneName = nameField.getValueAsText();
        if (newMilestoneName.equals(currentMilestoneName) == false) {
            project.WORKITEMS_ON_MILESTONES.renameMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, GpmXmlMilestoneSource.RQ1_PROJECT, currentMilestoneName, newMilestoneName);
            currentMilestoneName = newMilestoneName;
        }

        super.handleDependencyChange();
    }

    public void removeElement(DmRq1WorkItem element) {
        assert (element != null);
        assert (milestoneId == null); // Only allowed for milestones from milestone field.

        //
        // Ignore removal of workitems which do not yet exist in the RQ1 database.
        //
        if (element.existsInDatabase() != true) {
            return;
        }

        project.WORKITEMS_ON_MILESTONES.removeWorkitemForMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, nameField.getValueAsText(), element);
    }

}
