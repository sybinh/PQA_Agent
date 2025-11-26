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
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Project;
import java.util.EnumSet;

// Beschrieben in HowTo für Projekte anlegen. Wenn PVER-> EXTERNAL_ID befüllen.
// Wenn PFAM -> EXTERNAL_DESCRIPTION befüllen. Format: <MAPINT2EXT SI="Derivatname_intern">ASAMname_extern</MAPINT2EXT>
/**
 *
 * @author gug2wi
 */
public class Rule_Ecu_PstLines extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Check the definition of the PST lines of an customer project.",
            "Only one of the fields External_ID or ExternalDescription shall define the PST line(s) of the project.\n"
            + "\n"
            + "If External_ID is given, it has to contain the PST line of the project.\n"
            + "\n"
            + "If ExternalDescription is given, it has to contain the list of PST lines of the project.\n"
            + "\n"
            + "The warning 'PST lines defined twice.' is set on the project, if both fields contain PST lines.");

    final private DmRq1SwCustomerProject_Leaf myProject;

    public Rule_Ecu_PstLines(DmRq1SwCustomerProject_Leaf myProject) {
        super(description, myProject);
        this.myProject = myProject;
    }

    @Override
    public void executeRule() {

        if (myProject.isInLifeCycleState(LifeCycleState_Project.CANCELED, LifeCycleState_Project.CLOSED) == true) {
            return;
        }

        if ((myProject.getPstLinesFromExternalDescription() == null)
                || (myProject.getPstLinesFromExternalDescription().isEmpty() == true)
                || (myProject.getExternalID().isEmpty() == true)) {
            return;
        }

        addMarker(myProject, new Warning(this, "PST lines defined twice.", "Only External_ID or ExternalDescription may contain the definition of PST lines."));
    }
}
