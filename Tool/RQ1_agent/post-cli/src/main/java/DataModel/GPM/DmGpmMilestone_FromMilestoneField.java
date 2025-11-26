/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmValueField_Date;
import DataModel.DmValueField_Enumeration;
import DataModel.DmValueField_Text;
import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.Rq1.Fields.DmRq1Field_MilestoneTable;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1WorkItem_Project;
import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import util.EcvDate;
import util.EcvEnumeration;

/**
 * A milestone object representing a milestone defined in the milestone field of
 * a project.
 *
 * @author GUG2WI
 */
public class DmGpmMilestone_FromMilestoneField extends DmGpmMilestone {

    /**
     * Creates a new milestone for the given project.
     *
     * @param project Project for which the milestone shall be created.
     * @param name Name of the milestone
     * @param date Date of the milestone
     * @param exportCategory
     * @param exportScope
     * @return The new object representing the milestone.
     */
    static DmGpmMilestone_FromMilestoneField create(DmRq1Project project, String name, EcvDate date, ExportCategory exportCategory, ExportScope exportScope) {
        assert (project != null);
        assert (name != null);
        assert (name.isEmpty() == false);

        DmGpmMilestone_FromMilestoneField newMilestone = new DmGpmMilestone_FromMilestoneField(project, name, date, exportCategory, exportScope);

        project.TIME_SCHEDULE_OF_PROJECT.addMilestone(newMilestone);

        return (newMilestone);
    }

    private DmGpmMilestone_FromMilestoneField(DmRq1Project project, String name, EcvDate date, ExportCategory exportCategory, ExportScope exportScope) {
        super(Type.RQ1_PROJECT, "Milestone", project);
        assert (project != null);
        assert (name != null);
        assert (name.isEmpty() == false);

        addFields(name, date, exportCategory, exportScope);
    }

    DmGpmMilestone_FromMilestoneField(DmRq1Project project, DmRq1Field_MilestoneTable.Record milestone) {
        super(Type.RQ1_PROJECT, "Milestone", project);
        assert (project != null);
        assert (milestone != null);

        addFields(milestone.getName(), milestone.getDate(), milestone.getExportCategory(), milestone.getExportScope());
    }

    private void addFields(String name, EcvDate date, ExportCategory exportCategory, ExportScope exportScope) {
        assert (project != null);

        addField(NAME = new DmValueField_Text("Name", name) {
            @Override
            public void setValue(String newValue) {
                super.setValue(newValue);
                project.TIME_SCHEDULE_OF_PROJECT.updateMilestone(DmGpmMilestone_FromMilestoneField.this);
            }
        });
        addField(DATE = new DmValueField_Date("Date", date != null ? date : EcvDate.getEmpty()) {
            @Override
            public void setValue(EcvDate newValue) {
                super.setValue(newValue);
                project.TIME_SCHEDULE_OF_PROJECT.updateMilestone(DmGpmMilestone_FromMilestoneField.this);
            }

        });

        addField(EXPORT_CATEGORY = new DmValueField_Enumeration("Export Category", exportCategory, ExportCategory.values()) {
            @Override
            public void setValue(EcvEnumeration newValue) {
                super.setValue(newValue);
                project.TIME_SCHEDULE_OF_PROJECT.updateMilestone(DmGpmMilestone_FromMilestoneField.this);
            }
        });

        addField(EXPORT_SCOPE = new DmValueField_Enumeration("Export Scope", exportScope, ExportScope.values()) {
            @Override
            public void setValue(EcvEnumeration newValue) {
                super.setValue(newValue);
                project.TIME_SCHEDULE_OF_PROJECT.updateMilestone(DmGpmMilestone_FromMilestoneField.this);
            }
        }
        );
        addField(ALL_WORKITEMS = new DmGpmFieldOnMilestone_Workitems("All Workitems", project, GpmXmlMilestoneSource.RQ1_PROJECT, null, NAME, project.ALL_WORKITEMS));
    }

    @Override
    public String getId() {
        return (project.getId() + "/" + getTitle());
    }

    @Override
    public boolean save() {
        boolean result = super.save();
        fireChange();
        return (result);
    }

    //--------------------------------------------------------------------------
    //
    // Support for tasks (work items) on the milestone
    //
    //--------------------------------------------------------------------------
    public DmRq1WorkItem_Project createAndSaveWorkitem(DmGpmConfig_Task configTableRow, String title, EcvDate plannedDate, String effort, DmRq1User assignee) {
        DmRq1WorkItem_Project newWorkitem = DmRq1WorkItem_Project.createBasedOnProject(project, null);
        setMilestoneDataAndSaveNewWorkitem(newWorkitem, configTableRow, title, plannedDate, effort, assignee);
        return (newWorkitem);
    }

    public void removeWorkitem(DmRq1WorkItem_Project workitem) {
        assert (workitem != null);
        ((DmGpmFieldOnMilestone_Workitems) ALL_WORKITEMS).removeElement(workitem);
    }
}
