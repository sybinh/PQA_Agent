/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Data.Enumerations.LifeCycleState_Project;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_SwCustomerProject_ProPlaTo extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Check ProPlaTo settings for customer software projects.",
            "ProPlaTo is a tool for transfering planning data from RB to VW/Audi. "
            + "ProPlaTo works on settings in RQ1 which are specific for DGS-EC/ECV. There, this rule is only valid for projects of DGS-EC/ECV\n"
            + "\n"
            + "This rule consideres a project to be a ProPlaTo-project, if the tag 'ProPlaTo' exists.\n"
            + "\n"
            + "The following has to be true, if the project is a ProPlaTo project:"
            + "1) At least one PST line has to be defined."
            + "2) For each PST line, a 'Projektcluster' has to be defined.\n"
            + "\n"
            + "The check is only done for projects in life cycle state 'New' and 'Open'.");

    final private DmRq1SwCustomerProject_Leaf myProject;

    public Rule_SwCustomerProject_ProPlaTo(DmRq1SwCustomerProject_Leaf myProject) {
        super(description, myProject);
        this.myProject = myProject;
    }

    @Override
    public void executeRule() {

        if (myProject.isInLifeCycleState(LifeCycleState_Project.CANCELED, LifeCycleState_Project.CLOSED) == true) {
            return;
        }

        if ((myProject.getPstLinesFromExternalDescription().isEmpty() == true)
                || (myProject.getExternalID().isEmpty() == true)) {
            return;
        }

//        addMarker(myProject, new Warning(this, "PST lines defined twice.", "Only External_ID or ExternalDescription may contain the definition of PST lines."));
    }
}
