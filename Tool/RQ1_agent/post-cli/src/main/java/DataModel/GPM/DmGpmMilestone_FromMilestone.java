/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmElementField_ReadOnlyFromSource;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmElementI;
import DataModel.DmValueField_Enumeration;
import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.Rq1.Records.DmRq1Milestone;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1WorkItem_Pst;
import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import java.util.IdentityHashMap;
import java.util.Map;
import util.EcvDate;
import util.EcvEnumeration;
import util.EcvEnumerationFieldI;

/**
 * A milestone object representing a RQ1 milestone in a project.
 *
 * @author GUG2WI
 */
public class DmGpmMilestone_FromMilestone extends DmGpmMilestone_FromReleaseRecord<DmRq1Milestone> {

    final private static Map<DmRq1Milestone, DmGpmMilestone_FromMilestone> milestoneToMilestoneCache = new IdentityHashMap<>();

    final public DmElementField_ReadOnlyI<DmRq1Milestone> MILESTONE;

    public static DmGpmMilestone_FromMilestone create(DmRq1Project project, String name, EcvDate date, ExportCategory exportCategory, ExportScope exportScope) {
        assert (project != null);
        assert (name != null);
        assert (name.isEmpty() == false);

        DmRq1Milestone dmRq1Milestone = DmRq1Milestone.createBasedOnProject(project);
        dmRq1Milestone.TITLE.setValue(name);
        dmRq1Milestone.PLANNED_DATE.setValue(date);
        dmRq1Milestone.EXPORT_CATEGORY.setValue(exportCategory);
        dmRq1Milestone.EXPORT_SCOPE.setValue(exportScope);

        DmGpmMilestone_FromMilestone gpmMilestone = new DmGpmMilestone_FromMilestone(dmRq1Milestone);

        milestoneToMilestoneCache.put(dmRq1Milestone, gpmMilestone);

        return (gpmMilestone);
    }

    public static DmGpmMilestone_FromMilestone get(DmRq1Milestone dmRq1Milestone) {
        assert (dmRq1Milestone != null);

        DmGpmMilestone_FromMilestone gpmMilestone = milestoneToMilestoneCache.get(dmRq1Milestone);
        if (gpmMilestone == null) {
            gpmMilestone = new DmGpmMilestone_FromMilestone(dmRq1Milestone);
            milestoneToMilestoneCache.put(dmRq1Milestone, gpmMilestone);
        }
        return (gpmMilestone);
    }

    private DmGpmMilestone_FromMilestone(DmRq1Milestone milestone) {
        super(milestone, Type.RQ1_MILESTONE, DmRq1Milestone.ELEMENTTYPE, milestone.PROJECT.getElement());

        addField(MILESTONE = new DmElementField_ReadOnlyFromSource<DmRq1Milestone>(type.getText()) {
            @Override
            public DmRq1Milestone getElement() {
                return (milestone);
            }
        });
        addField(EXPORT_SCOPE = new DmValueField_Enumeration("Export Scope", milestone.EXPORT_SCOPE.getValue(), ExportScope.values()) {
            @Override
            public void setValue(EcvEnumeration newValue) {
                super.setValue(newValue);
            }
        });
        addField(EXPORT_CATEGORY = new DmValueField_Enumeration("Export Category", milestone.EXPORT_CATEGORY.getValue(), ExportCategory.values()));
    }

    @Override
    public boolean save() {

        boolean isNewRelease = (release.existsInDatabase() == false);
        boolean changed = isNewRelease;

        if (release.TITLE.getValueAsText().equals(NAME.getValueAsText()) == false) {
            release.TITLE.setValue(NAME.getValueAsText());
            changed = true;
        }
        if (release.PLANNED_DATE.getDateNotNull().equals(DATE.getDateNotNull()) == false) {
            release.PLANNED_DATE.setValue(DATE.getDate());
            changed = true;
        }
        if (release.EXPORT_SCOPE.getValue().equals(EXPORT_SCOPE.getValue()) == false) {
            release.EXPORT_SCOPE.setValue(EXPORT_SCOPE.getValue());
            changed = true;
        }
        if (release.EXPORT_CATEGORY.getValue().equals(EXPORT_CATEGORY.getValue()) == false) {
            release.EXPORT_CATEGORY.setValue(EXPORT_CATEGORY.getValue());
            changed = true;
        }

        if (changed == true) {
            return (release.save());
        }
        return (false);
    }

    @Override
    public void changed(DmElementI changedRecord) {
        if (release != null) {
            if (release.TITLE.getValueAsText().equals(NAME.getValueAsText()) == false) {
                NAME.setValue(release.TITLE.getValueAsText());
            }
            if (release.PLANNED_DATE.getDateNotNull().equals(DATE.getDateNotNull()) == false) {
                DATE.setValue(release.PLANNED_DATE.getDate());
            }
            if (release.EXPORT_SCOPE.getValue().equals(EXPORT_SCOPE.getValue()) == false) {
                ((EcvEnumerationFieldI) EXPORT_SCOPE).setValue(release.EXPORT_SCOPE.getValue());

            }
            if (release.EXPORT_CATEGORY.getValue().equals(EXPORT_CATEGORY.getValue()) == false) {
                ((EcvEnumerationFieldI) EXPORT_CATEGORY).setValue(release.EXPORT_CATEGORY.getValue());
            }
            fireChange();
        }
    }

    //--------------------------------------------------------------------------
    //
    // Support for tasks (work items) on the milestone
    //
    //--------------------------------------------------------------------------
    public DmRq1WorkItem_Pst createAndSaveWorkitem(DmGpmConfig_Task configTableRow, String title, EcvDate plannedDate, String effort, DmRq1User assignee) {
        DmRq1WorkItem_Pst newWorkitem = DmRq1WorkItem_Pst.createBasedOnMilestone(release);
        setMilestoneDataAndSaveNewWorkitem(newWorkitem, configTableRow, title, plannedDate, effort, assignee);
        return (newWorkitem);
    }

}
