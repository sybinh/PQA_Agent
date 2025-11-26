/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.Rq1.Records.DmRq1Milestone;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.Rq1.Records.DmRq1WorkItem_Project;
import Rq1Cache.Records.Rq1WorkItem_Project;
import Rq1Data.Enumerations.Category_WorkItem;
import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import Rq1Data.Enumerations.LifeCycleState_WorkItem;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the migration of DmGpmMilestone_FromMilestoneField to
 * DmGpmMilestone_FromMilestone.
 *
 * @author GUG2WI
 */
public class DmGpmMigrationManager {
    
    public static class MigrationTestResult {
        
        final private boolean isMigrationPossible;
        final private String toolTipText;
        
        public MigrationTestResult(boolean isMigrationPossible, String toolTipText) {
            assert (toolTipText != null);
            assert (toolTipText.isBlank() == false);
            
            this.isMigrationPossible = isMigrationPossible;
            this.toolTipText = toolTipText;
        }
        
        public boolean isMigrationPossible() {
            return isMigrationPossible;
        }
        
        public String getToolTipText() {
            return toolTipText;
        }
        
        @Override
        public String toString() {
            return ("(" + isMigrationPossible + "," + toolTipText + ")");
        }
    }
    
    public static MigrationTestResult isMigrationPossible(DmGpmMilestone_FromMilestoneField milestone) {
        assert (milestone != null);

        //
        // Check if all workitems are in the same project as the milestone
        //
        for (DmRq1WorkItem wi : milestone.ALL_WORKITEMS.getElementList()) {
            if (wi.PROJECT.isElementEqual(milestone.getProject()) == false) {
                return (new MigrationTestResult(false, "Milestone has Workitems which do not belong to this project."));
            }
        }

        //
        // Check if all workitems are in LCS NEW.
        //
        for (DmRq1WorkItem wi : milestone.ALL_WORKITEMS.getElementList()) {
            if (wi.isInLifeCycleState(LifeCycleState_WorkItem.NEW) == false) {
                return (new MigrationTestResult(false, "Milestone has Workitems with LCS other than 'NEW'."));
            }
        }

        //
        // Check if all workitems are of type project workitem.
        //
        for (DmRq1WorkItem wi : milestone.ALL_WORKITEMS.getElementList()) {
            if ((wi instanceof DmRq1WorkItem_Project) == false) {
                return (new MigrationTestResult(false, "Milestone has Workitems of wrong internal type."));
            }
        }
        
        return (new MigrationTestResult(true,
                "<html>"
                + "Migrate the Milestone from a tag on the project to a real RQ1-Element.<br>"
                + "<br>"
                + "This helps with traceability of workitems, with their forwarding and with<br>"
                + "billing efforts because the Milestone will get a RQ1 ID."
                + "<html>"));
    }

    /**
     * Migrates the milestone to a RQ1 milestone and moves all workitems to the
     * new milestone. The changes are immediatelly saved in the RQ1 database.
     *
     * @param milestoneFromField
     * @return The new milestone or null if the migration fails.
     */
    public static DmGpmMilestone_FromMilestone doMigration(DmGpmMilestone_FromMilestoneField milestoneFromField) {
        assert (milestoneFromField != null);
        assert (isMigrationPossible(milestoneFromField).isMigrationPossible() == true) : isMigrationPossible(milestoneFromField).toolTipText;
        
        DmRq1Project project = milestoneFromField.getProject();
        
        DmGpmMilestone_FromMilestone newGpmMilestone = DmGpmMilestone_FromMilestone.create(project, milestoneFromField.NAME.getValue(), milestoneFromField.DATE.getDate(), (ExportCategory) milestoneFromField.EXPORT_CATEGORY.getValue(), (ExportScope) milestoneFromField.EXPORT_SCOPE.getValue());
        if (newGpmMilestone.save() == false) {
            return (null);
        }
        
        DmRq1Milestone newDmRq1Milestone = newGpmMilestone.MILESTONE.getElement();
        assert (newDmRq1Milestone.existsInDatabase() == true);
        
        List<DmRq1WorkItem> listOfWorkitems = new ArrayList<>(milestoneFromField.ALL_WORKITEMS.getElementList());
        for (DmRq1WorkItem wi : listOfWorkitems) {
            migrateWorkitem((DmRq1WorkItem_Project) wi, newDmRq1Milestone);
            milestoneFromField.removeWorkitem((DmRq1WorkItem_Project) wi);
        }
        
        project.TIME_SCHEDULE_OF_PROJECT.removeMilestone(milestoneFromField);
        project.save();
        
        project.OPEN_MILESTONES.reload();
        project.TIME_SCHEDULE_OF_PROJECT.reload();
        
        return (newGpmMilestone);
    }

    /**
     * Package private to enable testing.
     */
    static void migrateWorkitem(DmRq1WorkItem_Project dmWorkitem, DmRq1Milestone milestone) {
        assert (dmWorkitem != null);
        assert (milestone != null);
        
        Rq1WorkItem_Project rq1Workitem = (Rq1WorkItem_Project) dmWorkitem.getRq1Record();
        dmWorkitem.MILESTONE.setElement(milestone);
        dmWorkitem.CATEGORY.setValue(Category_WorkItem.RELEASE);
        dmWorkitem.SUBCATEGORY.setValue(SubCategory_WorkItem.PVER);
        dmWorkitem.MILESTONE.setElement(milestone);
        dmWorkitem.GPM_MILESTONE_ID.setValue(milestone.getId());
        
        dmWorkitem.save();
    }
    
}
