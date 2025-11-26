/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1PvarRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Types.Rq1DerivativeMapping.Mode;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public class Rule_Pvar_Derivatives extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Derivatives for PVAR",
            "Checks the following conditions on the derivative settings for a PVAR.\n"
            + "\n"
            + "1) Exactly one derivative has to be marked as pilot.\n"
            + "2) The planned date of the pilot derivative has to match the planned date of the PVAR.\n"
            + "3) The planned date of all other derivatives have to be later then the planned date of the pilot.\n"
            + "\n"
            + "Warnings are created, if on or more of this conditions are not fullfilled.\n"
            + "\n"
            + "Note:\n"
            + "- The check is only done for PVAR that are not canceled, not conflicted and not closed.\n"
            + "- The value of the planned date is checked in an own rule.");

    //
    // The derivatives of the PVAR are not checked against the derivatives defined in the project, because the derivatives in the
    // project might change.
    //
    final private DmRq1PvarRelease myRelease;

    public Rule_Pvar_Derivatives(DmRq1PvarRelease myRelease) {
        super(description, myRelease);
        this.myRelease = myRelease;
    }

    @Override
    public void executeRule() {

        //
        // CLOSED, CANCELED, CONFLICTED: no check for derivatives.
        //
        // Note: Do the check also for state NEW. Otherwise the Lumpensammler will never be checked.
        //
        if (myRelease.isCanceledOrConflicted() || myRelease.isClosed()) {
            return;
        }

        Set<String> pilots = myRelease.DERIVATIVES.getValue().getMatchingDerivativeNames(EnumSet.of(Mode.PILOT));
        Set<String> mandatoryDerivatives = myRelease.DERIVATIVES.getValue().getMatchingDerivativeNames(EnumSet.of(Mode.MANDATORY));
        Map<String, EcvDate> plannedDates = myRelease.PLANNED_DATE_DERIVATIVES.getValue().getDates();

        //
        // Warn, if no pilot exists
        //
        if (pilots.isEmpty() == true) {
            addMarker(myRelease, new Warning(this, "No pilot derivative set.", "No derivative is set as pilot PVAR-Release."));
        }

        //
        // Warn, if more then one pilot exists
        //
        if (pilots.size() > 1) {
            StringBuilder b = new StringBuilder(100);
            b.append("The following derivatives are set as pilot: ");
            boolean first = true;
            for (String pilot : pilots) {
                if (first == false) {
                    b.append(", ");
                }
                first = false;
                b.append(pilot);
            }
            addMarker(myRelease, new Warning(this, "Too many pilots set.", b.toString()));
        }

        //
        // Check date for pilot
        //
        if (pilots.size() == 1) {
            for (String pilot : pilots) {
                EcvDate pilotDate = plannedDates.get(pilot);
                if (pilotDate == null) {
                    addMarker(myRelease, new Warning(this, "Planned date missing for pilot.", "No planned date set for pilot derivative " + pilot + "."));
                } else if (pilotDate.equals(myRelease.PLANNED_DATE.getValue()) == false) {
                    addMarker(myRelease, new Warning(this, "Planned date for pilot differs to planned date of release.",
                            "Planned date for pilot (" + pilotDate.toString() + ") differs to planned date of release (" + myRelease.PLANNED_DATE.getValue().toString() + ")."));
                }
            }
        }

        //
        // Check dates for mandatory derivatives
        // 
        for (String mandatory : mandatoryDerivatives) {
            EcvDate mandatoryDate = plannedDates.get(mandatory);
            if (mandatoryDate == null) {
                addMarker(myRelease, new Warning(this, "Planned date missing for mandatory derivative.", "No planned date set for mandatory derivative " + mandatory + "."));
            } else if (mandatoryDate.isEarlierThen(myRelease.PLANNED_DATE.getValue()) == true) {
                addMarker(myRelease, new Warning(this, "Planned date for mandatory derivatives is earlier then planned date of release.",
                        "Planned date for derivative " + mandatory + " (" + mandatoryDate.toString() + ") is earlier then planned date of release (" + myRelease.PLANNED_DATE.getValue().toString() + ")."));
            }
        }

        /* Note: Deactivated according to https://rb-wam.bosch.com/tracker01/browse/ECVTOOL-281
         //
         // Ensure that release is assigned to an customer project.
         //
         if ((myRelease.PROJECT.getElement() instanceof DmRq1CustomerProject) == false) {
         addMarker(myRelease, new Warning(this, "Not assigned to customer project.",
         "PVAR is not assigned to a customer project."));
         return;
         }
         List<PstLineEcuProject> pstLines = ((DmRq1CustomerProject) myRelease.PROJECT.getElement()).getPstLines();
         Set<String> derivateNames = myRelease.getDerivatives().getDerivativeNames();
         //
         // Check that PVAR has derivatives.
         //
         //        if (derivateNames.isEmpty()) {
         //            addMarker(myRelease, new Warning(this, "Missing derivatives", "No derivatives defined for the PVAR."));
         //            return;
         //        }
         //
         // Check that all derivatives defined for a PVAR match to one of the PST lines in the project
         //
         for (String derivateName : derivateNames) {
         boolean found = false;
         for (PstLineEcuProject pstLine : pstLines) {
         if (derivateName.equals(pstLine.getInternalName())) {
         found = true;
         }
         }
         if (found == false) {
         addMarker(myRelease, new Warning(this, "Unknown derivative.", "No PST line in project for derivate " + derivateName));
         }
         }
         //
         // Check that for all PST lines in the project a derivative is set.
         //
         for (PstLineEcuProject pstLine : pstLines) {
         boolean found = false;
         for (String derivateName : derivateNames) {
         if (derivateName.equals(pstLine.getInternalName())) {
         found = true;
         }
         }
         if (found == false) {
         addMarker(myRelease, new Warning(this, "Missing derivative.", "No derivate set for PST line " + pstLine.getInternalName()));
         }
         }
         */
    }
}
