/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.Rq1.Records.DmRq1PverRelease;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.Rq1.Records.DmRq1WorkItem_Pst;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmGpmMilestone_FromPverRelease extends DmGpmMilestone_FromRelease<DmRq1PverRelease> {

    DmGpmMilestone_FromPverRelease(DmRq1PverRelease pver) {
        super(pver, Type.PVER, pver.getClassificationAsText() + " Delivery");
    }

    @Override
    public DmRq1WorkItem createAndSaveWorkitem(DmGpmConfig_Task configTableRow, String title, EcvDate plannedDate, String effort, DmRq1User assignee) {
        DmRq1WorkItem newWorkitem = DmRq1WorkItem_Pst.createBasedOnPst(release);
        setMilestoneDataAndSaveNewWorkitem(newWorkitem, configTableRow, title, plannedDate, effort, assignee);
        return (newWorkitem);
    }

    @Override
    public String getClassification() {
        return (release.CLASSIFICATION.getValueAsText());
    }

    @Override
    public String getElementType() {
        return (release.getClassificationAsText() + " Delivery");
    }

}
