/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Monitoring;

import DataModel.Doors.Records.DmDoorsObject;
import DataModel.Monitoring.DmRule;
import Doors.Identifier.DoorsObjectIdentifier;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmDoorsRule_CheckBaselinedObject extends DmRule<DmDoorsObject> {

    @EcvElementList("DataModel.Doors.Monitoring.DmDoorsRuleDescription")
    static final public DmDoorsRuleDescription DESCRIPTION = new DmDoorsRuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Baseline check for DOORS objects.",
            "This rule checks that the link to a DOORS object points to a baselined version of the object.\n"
            + "\n"
            + "Links to baselined DOORS objects contain a 'V'.\n"
            + "Links to not baselined DOORS objects contain a 'O'.\n"
            + "\n"
            + "The warning is given, because a link to a not baselined DOORS object refers to an object that might be changed or even deleted in future.\n"
            + "Therefore, a link to a not baselined object is reliable.");

    public DmDoorsRule_CheckBaselinedObject(DmDoorsObject assignedDmElement) {
        super(DESCRIPTION, assignedDmElement);
    }

    @Override
    protected void executeRule() {

        if (((DoorsObjectIdentifier) dmElement.getIdentifier()).isBaseline() == false) {
            addMarker(dmElement, new Warning_NotBaselined(this, dmElement.getIdentifier().getUrn()));
        }
    }

}
