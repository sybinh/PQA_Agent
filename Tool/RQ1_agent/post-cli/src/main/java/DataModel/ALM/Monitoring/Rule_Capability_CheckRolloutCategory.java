/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Monitoring;

import DataModel.ALM.Records.DmAlmWorkitem_Capability;
import DataModel.Monitoring.DmRule;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author CNI83WI
 */
public class Rule_Capability_CheckRolloutCategory extends DmRule<DmAlmWorkitem_Capability> {

    @EcvElementList("DataModel.ALM.Monitoring.DmAlmRuleDescription")
    static final public DmAlmRuleDescription description = new DmAlmRuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE), 
            "Capability, check, if Rollout Category is set", 
            "Creates a warning, if no Rollout Category is set in Tags, such as 'pmt-rollout-cat-<*>");

    public Rule_Capability_CheckRolloutCategory(DmAlmWorkitem_Capability capability) {
        super(description, capability);
    }

    @Override
    protected void executeRule() {
        if (dmElement.TAGS.getValueAsText().contains("pmt-rollout-cat") == false && dmElement.SAFE_WORKTYPE.getValueAsText().equals("Business")) {
            addMarker(dmElement, new Warning(this, "No Rollout-Category set in Tags.", "No Rollout-Category set in Tags."));
        }

    }

}
