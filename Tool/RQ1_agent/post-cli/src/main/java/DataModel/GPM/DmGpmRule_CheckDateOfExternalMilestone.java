/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Project;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import java.util.EnumSet;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmGpmRule_CheckDateOfExternalMilestone extends DmRule<DmGpmMilestone_FromExternalDatabase> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription DESCRIPTION = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE, RuleExecutionGroup.TEST),
            DmGpmWarning_DateOfExternalMilestoneChanged.warningTitle,
            "This rule helps to keep track of changes in the date of external milestones.\n"
            + "\n"
            + "The rule checks, if the date of a milestone loaded from an external database (e.g. HIS)\n"
            + "was changed since the last confirmation by the user."
    );

    final private GpmXmlMilestoneSource milestoneSource;

    public DmGpmRule_CheckDateOfExternalMilestone(DmGpmMilestone_FromExternalDatabase gpmMilestone) {
        super(DESCRIPTION, gpmMilestone);
        this.milestoneSource = GpmXmlMilestoneSource.HIS;
    }

    @Override
    protected void executeRule() {

        DmRq1Project project = dmElement.getProject();
        addDependency(project);

        EcvDate lastConfirmedDate = project.GPM_DATA_OF_EXTERNAL_MILESTONES.getLastConfirmedDate(dmElement.getMilestoneSource(), dmElement.getId(), dmElement.getOldId());
        if (lastConfirmedDate == null) {
            addMarker(dmElement, new DmGpmWarning_NewExternalMilestone(this));
            return;
        }
        EcvDate currentDate = dmElement.DATE.getDate();
        if (currentDate == null) {
            currentDate = EcvDate.getEmpty();
        }

        if (lastConfirmedDate.equals(currentDate) == false) {
            addMarker(dmElement.DATE, new DmGpmWarning_DateOfExternalMilestoneChanged(this, lastConfirmedDate, currentDate));
            addMarker(dmElement, new DmGpmWarning_DateOfExternalMilestoneChanged(this, lastConfirmedDate, currentDate));
        }
    }

}
