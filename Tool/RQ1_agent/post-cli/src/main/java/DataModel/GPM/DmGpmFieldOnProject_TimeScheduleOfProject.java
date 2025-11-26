/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmElementListField_ReadOnlyI;
import DataModel.DmField;
import DataModel.Rq1.Records.DmRq1Project;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provides access to the milestones of a project.
 *
 * @author GUG2WI
 */
public class DmGpmFieldOnProject_TimeScheduleOfProject extends DmField implements DmElementListField_ReadOnlyI<DmGpmMilestone> {

    final private static Logger LOGGER = Logger.getLogger(DmGpmFieldOnProject_TimeScheduleOfProject.class.getCanonicalName());
    //
    final private DmRq1Project project;
    //
    final private DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestoneField fromMilestoneFieldList;
    final private DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestones fromMilestonesList;
    final private DmGpmFieldOnProject_TimeScheduleOfProject_FromReleases fromReleaseList;
    final private DmGpmFieldOnProject_TimeScheduleOfProject_HIS hisList;
    final private DmGpmFieldOnProject_TimeScheduleOfProject_Mcr mcrList;
    final private DmGpmFieldOnProject_TimeScheduleOfProject_iCDM iCdmList;
    //
    private List<DmGpmMilestone> content = null;

    public DmGpmFieldOnProject_TimeScheduleOfProject(DmRq1Project project, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (project != null);

        this.project = project;

        this.fromMilestoneFieldList = new DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestoneField(project);
        this.fromMilestonesList = new DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestones(this, project);
        this.fromReleaseList = new DmGpmFieldOnProject_TimeScheduleOfProject_FromReleases(this, project);
        this.hisList = new DmGpmFieldOnProject_TimeScheduleOfProject_HIS(this, project);
        this.mcrList = new DmGpmFieldOnProject_TimeScheduleOfProject_Mcr(this, project);
        this.iCdmList = new DmGpmFieldOnProject_TimeScheduleOfProject_iCDM(this, project);
    }

    @Override
    public List<DmGpmMilestone> getElementList() {
        if (content == null) {
            loadElementList();
        }
        return (content);
    }

    private synchronized void loadElementList() {
        if (content == null) {
            List<DmGpmMilestone> newContent = new ArrayList<>();
            newContent.addAll(fromMilestoneFieldList.getElementList());
            newContent.addAll(fromMilestonesList.getElementList());
            newContent.addAll(fromReleaseList.getElementList());
            newContent.addAll(hisList.getElementList());
            newContent.addAll(mcrList.getElementList());
            newContent.addAll(iCdmList.getElementList());
            content = newContent;
        }
    }

    public List<DmGpmMilestone_FromHis> getMilestonesFromHis() {
        return (hisList.getElementList());
    }

    public List<DmGpmMilestone_FromMcr> getMilestonesFromMcr() {
        return (mcrList.getElementList());
    }

    /**
     * Package private to support testing.
     *
     * @return
     */
    List<DmGpmMilestone_From_iCDM> getMilestonesFromiCDM(Collection<String> pidcVerIds) {
        return (iCdmList.getMilestonesFromiCDM(pidcVerIds));
    }

    public List<DmGpmMilestone_FromMilestoneField> getMilestonesFromMilestoneField() {
        return (fromMilestoneFieldList.getElementList());
    }

    public List<DmGpmMilestone_FromMilestone> getMilestonesFromMilestone() {
        return (fromMilestonesList.getElementList());
    }

    @Override
    public void reload() {
        hisList.reload();
        fromMilestoneFieldList.reload();
        fromMilestonesList.reload();
        fromReleaseList.reload();
        mcrList.reload();
        iCdmList.reload();

        content = null;

        fireFieldChanged();
    }

    @Override
    protected void handleDependencyChange() {
        content = null;
        fireFieldChanged();
    }

    //--------------------------------------------------------------------------
    //
    // Special handling for milestone field
    //
    //--------------------------------------------------------------------------
    void addMilestone(DmGpmMilestone_FromMilestoneField newMilestone) {
        fromMilestoneFieldList.addMilestone(newMilestone);
    }

    void updateMilestone(DmGpmMilestone_FromMilestoneField changedMilestone) {
        fromMilestoneFieldList.updateMilestone(changedMilestone);
    }

    void removeMilestone(DmGpmMilestone_FromMilestoneField milestoneFromField) {
        fromMilestoneFieldList.removeMilestone(milestoneFromField);
    }

}
