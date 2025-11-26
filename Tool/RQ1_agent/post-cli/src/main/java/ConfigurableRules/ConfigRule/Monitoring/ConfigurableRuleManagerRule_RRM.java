/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Monitoring;

import DataModel.DmElementFieldI;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;

/**
 *
 * @author RHO2HC
 */
public class ConfigurableRuleManagerRule_RRM extends ConfigurableRuleManagerRule<DmRq1Rrm> {

    private final DmElementFieldI<? extends DmRq1Release> parentField;
    private final DmElementFieldI<? extends DmRq1Release> childField;

    public ConfigurableRuleManagerRule_RRM(DmRq1Rrm assignedDmElement, DmElementFieldI<? extends DmRq1Release> parentField, DmElementFieldI<? extends DmRq1Release> childField) {
        super(assignedDmElement);
        assert (parentField != null);
        assert (childField != null);

        this.parentField = parentField;
        this.childField = childField;
    }

    @Override
    protected boolean checkAssignedElementIsInTheGivenProject(DmRq1Project project) {
        if (project != null) {
            DmRq1Release parent = parentField.getElement();
            if (parent != null) {
                if (project == parent.getProject()) {
                    return (true);
                }
            }
            DmRq1Release child = childField.getElement();
            if (child != null) {
                if (project == child.getProject()) {
                    return (true);
                }
            }
        }
        return (false);
    }

}
