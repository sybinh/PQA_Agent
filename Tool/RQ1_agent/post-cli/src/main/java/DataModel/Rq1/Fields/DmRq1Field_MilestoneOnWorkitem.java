/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */

package DataModel.Rq1.Fields;

import DataModel.DmElementField_FilteredByClass;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmField;
import DataModel.DmMilestoneI;
import DataModel.GPM.DmGpmMilestone_FromMilestoneField;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Milestone;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1ReleaseRecord;
import java.util.List;

/**
 * Class to gain (reading) access to a GPM Milestone of a Workitem
 * 
 * @author FUJ1WI
 */

public class DmRq1Field_MilestoneOnWorkitem extends DmField implements DmElementField_ReadOnlyI<DmMilestoneI> {

    final private DmElementField_FilteredByClass<DmRq1ReleaseRecord, DmRq1Milestone> milestoneReleaseField;
    final private DmRq1Field_Text milestoneIdField;

    public DmRq1Field_MilestoneOnWorkitem(DmElementField_FilteredByClass<DmRq1ReleaseRecord, DmRq1Milestone> milestoneReleaseField, DmRq1Field_Text milestoneIdField, String nameForUserInterface) {
        super(nameForUserInterface);

        assert (milestoneIdField != null);
        assert (milestoneReleaseField != null);

        this.milestoneIdField = milestoneIdField;
        this.milestoneReleaseField = milestoneReleaseField;
    }

    @Override
    public DmMilestoneI getElement() {

        if (milestoneReleaseField.isElementSet() == true) {
            return (milestoneReleaseField.getElement());
        }

        String[] milestoneIdParts = milestoneIdField.getValueAsText().split("/");
        if (milestoneIdParts.length == 2) {
            String projectId = milestoneIdParts[0];
            String milestoneName = milestoneIdParts[1];
            DmRq1ElementInterface project = DmRq1Element.getElementById(projectId);
            if (project instanceof DmRq1Project) {
                List<DmGpmMilestone_FromMilestoneField> milestonesFromField = ((DmRq1Project) project).TIME_SCHEDULE_OF_PROJECT.getMilestonesFromMilestoneField();
                for (DmGpmMilestone_FromMilestoneField milestone : milestonesFromField) {
                    if (milestoneName.equals(milestone.NAME.getValueAsText()) == true) {
                        return (milestone);
                    }
                }
            }
        }
        return (null);
    }
    
} 
 
