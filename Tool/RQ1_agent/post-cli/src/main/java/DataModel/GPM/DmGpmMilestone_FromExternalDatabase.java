/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmConstantField_Enumeration;
import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.Rq1.Records.DmRq1WorkItem_Project;
import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public abstract class DmGpmMilestone_FromExternalDatabase extends DmGpmMilestone {

    final private GpmXmlMilestoneSource milestoneSource;

    public DmGpmMilestone_FromExternalDatabase(Type type, String elementType, GpmXmlMilestoneSource milestoneSource, DmRq1Project dmRq1Project) {
        super(type, elementType, dmRq1Project);
        assert (milestoneSource != null);

        this.milestoneSource = milestoneSource;

        addField(EXPORT_CATEGORY = new DmConstantField_Enumeration("Export_Category", ExportCategory.EMPTY));
        addField(EXPORT_SCOPE = new DmConstantField_Enumeration("Export_Scope", ExportScope.EMPTY));

        addRule(new DmGpmRule_CheckDateOfExternalMilestone(this));
    }

    final public GpmXmlMilestoneSource getMilestoneSource() {
        return milestoneSource;
    }

    //--------------------------------------------------------------------------
    //
    // Support confirmation of changed milestone date
    //
    //--------------------------------------------------------------------------
    final public EcvDate getConfirmedDate() {
        EcvDate confirmedDate = getProject().GPM_DATA_OF_EXTERNAL_MILESTONES.getLastConfirmedDate(milestoneSource, getId(), getOldId());
        return (confirmedDate);
    }

    final public void confirmMilestoneDate() {
        getProject().GPM_DATA_OF_EXTERNAL_MILESTONES.confirmDate(milestoneSource, getId(), getOldId(), getElementType(), DATE.getValue());

        if (milestoneSource == GpmXmlMilestoneSource.HIS) {
            // Change old entries for mapped workitems from name to id
            List<DmRq1WorkItem> workitemsByName = project.WORKITEMS_ON_MILESTONES.getWorkitemsForMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, NAME.getValueAsText());
            if (workitemsByName.isEmpty() == false) {
                project.WORKITEMS_ON_MILESTONES.renameMilestone(GpmXmlMilestoneSource.RQ1_PROJECT, GpmXmlMilestoneSource.HIS, NAME.getValueAsText(), getId());
            }
        }

        fireChange();
    }

    protected String getOldId() {
        return (getId());
    }

    //--------------------------------------------------------------------------
    //
    // Support for tasks (work items) on the milestone
    //
    //--------------------------------------------------------------------------
    @Override
    final protected String getIdForWorkitem() {
        return (milestoneSource.getValueInDatabase() + "/" + getId());
    }

    final public DmRq1WorkItem_Project createAndSaveWorkitem(DmGpmConfig_Task configTableRow, String title, EcvDate plannedDate, String effort, DmRq1User assignee) {

        DmRq1WorkItem_Project newWorkitem = DmRq1WorkItem_Project.createBasedOnProject(project, null);

        setMilestoneDataAndSaveNewWorkitem(newWorkitem, configTableRow, title, plannedDate, effort, assignee);

        return (newWorkitem);
    }

}
