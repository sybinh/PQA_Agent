/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1Pst;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.Scope;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public class Rule_Prg_Lumpensammler extends DmRule<DmRq1Pst> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rule_Prg_Lumpensammler.class.getCanonicalName());

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROJECT, RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Check settings for Lumpensammler",
            "The following settings are necessary for a Lumpensammler:\n"
            + "- Title has to end with \"ZZZ\".\n"
            + "- Planned date has to be 31.12.2030 or 31.12.2099.\n"
            + "- Scope has to be external.\n"
            + "\n"
            + "The warnings 'Wrong title for Lumpensammler', 'Wrong date for Lumpensammler' and 'Wrong scope for Lumpensammler' are set on the Lumpensammler if this is not the case.");

    public Rule_Prg_Lumpensammler(DmRq1Pst myRelease) {
        super(description, myRelease);
    }

    @Override
    public void executeRule() {

        //
        // CLOSED, CANCELED, CONFLICTED : no check 
        //
        // Note: Do the check also for state NEW. Otherwise the Lumpensammler will never be checked.
        //
        if (dmElement.isClosed() || dmElement.isCanceledOrConflicted()) {
            return;
        }

        //
        // Check, if it looks like a Lumpensammler
        //
        if ((dmElement.getTitle().contains("ZZZ") == true)
                || (dmElement.getPlannedDate().equals("20301231") == true)
                || (dmElement.getPlannedDate().equals("20991231") == true)) {

            //
            // Check Title
            //
            if (dmElement.getTitle().endsWith("ZZZ") == false) {
                addMarker(dmElement, new Warning(this, "Wrong title for Lumpensammler", "Title of Lumpensammler has to end with \"ZZZ\""));
            }

            //
            // Check date
            //
            if ((dmElement.getPlannedDate().equals("20301231") == false)
                    && (dmElement.getPlannedDate().equals("20991231")) == false) {
                addMarker(dmElement, new Warning(this, "Wrong date for Lumpensammler", "Planned date of Lumpensammler has to be 31.12.2030 or 31.12.2099."));
            }

            //
            // Check scope
            //
            if (dmElement.SCOPE.getValue() != Scope.EXTERNAL) {
                addMarker(dmElement, new Warning(this, "Wrong scope for Lumpensammler", "Scope of Lumpensammler has to be external."));
            }
        }
    }
}
