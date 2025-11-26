/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1Milestone;
import DataModel.Rq1.Records.DmRq1Project;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
class DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestones extends DmGpmFieldOnProject_TimeScheduleOfProject_SubList<DmGpmMilestone_FromMilestone, DmRq1Field_ReferenceList<DmRq1Milestone>> {

    DmGpmFieldOnProject_TimeScheduleOfProject_FromMilestones(DmGpmFieldOnProject_TimeScheduleOfProject timeScheduleField, DmRq1Project project) {
        super(timeScheduleField, project, project.ALL_MILESTONES, DependencyType.FIELD);
    }

    @Override
    protected List<DmGpmMilestone_FromMilestone> loadElementList() {

        List<DmGpmMilestone_FromMilestone> newContent = new ArrayList<>();
        for (DmRq1Milestone dmMilestone : project.ALL_MILESTONES.getElementList()) {
            DmGpmMilestone_FromMilestone gpmMilestone = DmGpmMilestone_FromMilestone.get(dmMilestone);
            newContent.add(gpmMilestone);
        }
        return (newContent);
    }

    @Override
    protected void reloadSourceField() {
        sourceField.reload();
    }

}
