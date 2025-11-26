/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Data.Enumerations.LifeCycleState_Project;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Ecu_LumpensammlerExists extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "Lumpensammler has to exist.",
            "In each customer project, at least one Lumpensammler has to exist.\n"
            + "\n"
            + "A Lumpensammler is characterized by a title that ends with 'ZZZ'. Example: 'DMG1001A01C1395 / MY15ZZZ'\n"
            + "\n"
            + "The warning 'Lumpensammler missing' is set on the project, if no Lumpensammler exists.");

    final private DmRq1SwCustomerProject_Leaf myProject;

    public Rule_Ecu_LumpensammlerExists(DmRq1SwCustomerProject_Leaf myProject) {
        super(description, myProject);
        this.myProject = myProject;
    }

    @Override
    public void executeRule() {
        //
        // Execute only for open projects
        //
        if (myProject.isInLifeCycleState(LifeCycleState_Project.CANCELED, LifeCycleState_Project.CLOSED) == true) {
            return;
        }

        for (DmRq1Pst release : myProject.ALL_PST.getElementList()) {
            if ((release.getTitle().endsWith("ZZZ") == true) || (release.getPlannedDate().equals("20301231") == true)) {
                //
                // At least one Lumpensammler exists
                //
                return;
            }
        }

        addMarker(myProject, new Warning_LumpensammlerMissing(this, myProject));
    }
}
