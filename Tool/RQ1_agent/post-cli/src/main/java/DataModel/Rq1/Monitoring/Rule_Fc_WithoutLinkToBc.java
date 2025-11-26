/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1FcRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author sas82wi
 */
public class Rule_Fc_WithoutLinkToBc extends DmRule<DmRq1FcRelease> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.FC_PLANNING),
            "FC: Check BC link",
            "Creates a warning if FC is not linked to any BC.");

    public Rule_Fc_WithoutLinkToBc(DmRq1FcRelease fcRelease) {
        super(description, fcRelease);
    }

    @Override
    public void executeRule() {

        //
        // Do not check if element is conflicted.
        //
        if (dmElement.isConflicted() == true) {
            return;
        }

        addDependency(dmElement.MAPPED_BC);
        if (dmElement.MAPPED_BC.getElementList().isEmpty() == true) {
            addMarker(dmElement, new Warning(this, "FC not linked to any BC.", "FC not linked to any BC."));
        }
    }
}
