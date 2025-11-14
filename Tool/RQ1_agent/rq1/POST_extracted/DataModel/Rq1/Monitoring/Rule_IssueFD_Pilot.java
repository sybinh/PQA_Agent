/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.DmMappedElement;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.YesNoEmpty;
import java.util.EnumSet;

/**
 *
 * @author sas82wi
 */
public class Rule_IssueFD_Pilot extends DmRule<DmRq1IssueFD> {

    final private static String PILOT_MISSING = "Pilot missing for I-SW";
    final private static String I_SW_MISSING = "I-SW missing";

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.I_FD_PILOT),
            "I-FD Pilot",
            "Checks if the I-SW of the I-FD has a pilot set.\n"
            + "\n"
            + "The warning \"" + PILOT_MISSING + "\" is set on the I-FD, if no pilot was found.\n"
            + "The warning \"" + I_SW_MISSING + "\" is set on the I-FD, if no I-SW is connected to the I-FD.");

    public Rule_IssueFD_Pilot(DmRq1IssueFD issueFD) {
        super(description, issueFD);
    }

    @Override
    public void executeRule() {

        addDependency(dmElement.PARENT);
        DmRq1Issue parent = dmElement.PARENT.getElement();

        if (parent instanceof DmRq1IssueSW) {

            DmRq1IssueSW issueSW = (DmRq1IssueSW) parent;
            boolean hasPilot = false;

            addDependency(issueSW.HAS_MAPPED_PST);

            for (DmMappedElement<DmRq1Irm_Pst_IssueSw, DmRq1Pst> map : issueSW.HAS_MAPPED_PST.getElementList()) {
                addDependency(map.getMap());
                if (map.getMap().IS_PILOT.getValue() == YesNoEmpty.YES) {
                    hasPilot = true;
                }
            }

            addDependency(issueSW.HAS_MAPPED_COLLECTIONS);

            for (DmMappedElement<DmRq1Irm_Pst_IssueSw, DmRq1Pst> map : issueSW.HAS_MAPPED_COLLECTIONS.getElementList()) {
                addDependency(map.getMap());
                if (map.getMap().IS_PILOT.getValue() == YesNoEmpty.YES) {
                    hasPilot = true;
                }
            }

            if (hasPilot == false) {
                addMarker(dmElement, new Warning(this, PILOT_MISSING, "No pilot release exists for the parent I-SW of the I-FD."));
            }

        } else {
            addMarker(dmElement, new Warning(this, I_SW_MISSING, "The I-FD has no parent I-SW."));
        }
    }

}
