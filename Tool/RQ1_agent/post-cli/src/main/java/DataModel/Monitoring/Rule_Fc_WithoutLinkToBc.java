/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1FcRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author sas82wi
 */
public class Rule_Fc_WithoutLinkToBc extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.FC_PLANNING),
            "FC: Check BC link",
            "Creates a warning if FC is not linked to any BC.");

    private final DmRq1FcRelease fcRelease;

    public Rule_Fc_WithoutLinkToBc(DmRq1FcRelease fcRelease) {
        super(description, fcRelease);
        assert (fcRelease != null);
        this.fcRelease = fcRelease;
    }

    @Override
    public void executeRule() {

        //
        // Do not check if element is conflicted.
        //
        if (fcRelease.isConflicted() == true) {
            return;
        }

        if (fcRelease.MAPPED_BC.getElementList().isEmpty() == true) {
            addMarker(fcRelease, new Warning(this, "FC not linked to any BC.", "FC not linked to any BC."));
        }
    }
}
