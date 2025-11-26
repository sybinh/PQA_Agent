/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Monitoring;

import DataModel.DmElementI;
import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1BaseElement;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;

/**
 *
 * @author RHO2HC
 */
public class ConfigurableRuleWarningExecutor extends DmRule<DmElementI> {

    private Warning warning;

    public ConfigurableRuleWarningExecutor(DmRq1BaseElement elementToCheckRule, String userSuggestedAction) {
        super(new DmRq1RuleDescription(EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
                "Configurable Rule", userSuggestedAction), elementToCheckRule);
    }

    public void setWarning(Warning warning) {
        this.warning = warning;
    }

    @Override
    public void executeRule() {
        if (warning != null) {
            addWarningMarker(this.dmElement, warning);
        }
    }

    public void deactiveRule() {
        super.deactivateRule();
    }

    public void addWarningMarker(DmElementI elementToMark, Warning newMarker) {
        super.addMarker(elementToMark, newMarker);
    }
}
