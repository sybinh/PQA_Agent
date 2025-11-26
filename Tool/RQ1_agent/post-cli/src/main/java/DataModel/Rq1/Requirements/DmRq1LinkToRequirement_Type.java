/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import java.util.EnumSet;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public enum DmRq1LinkToRequirement_Type implements EcvEnumeration {
    //
    L1_PLANNED("L1 Planned", true), L1_REMOVED("L1 Removed", false), //
    L2_PLANNED("L2 Planned", true), L2_IMPACTED("L2 Impacted", false), L2_REMOVED("L2 Removed", false),
    L3_PLANNED("L3 Planned", true), L3_IMPACTED("L3 Impacted", false), L3_REMOVED("L3 Removed", false),
    L4_PLANNED("L4 Planned", true), L4_IMPACTED("L4 Impacted", false), L4_REMOVED("L4 Removed", false),
    //
    STAKEHOLDER_PLANNED("Stakeholder Planned", true), STAKEHOLDER_AFFECTED("Stakeholder Affected", false), STAKEHOLDER_REMOVED("Stakeholder Removed", false),
    MO_PLANNED("MO Planned", true), MO_AFFECTED("Mo Affected", false), MO_IMPACTED("MO Impacted", false), MO_REMOVED("MO Removed", false),
    SC_PLANNED("SC Planned", true), SC_AFFECTED("SC Affected", false), SC_IMPACTED("SC Impacted", false), SC_REMOVED("SC Removed", false),
    BC_FC_PLANNED("BC-FC Planned", true), BC_FC_AFFECTED("BC-FC Affected", false), BC_FC_IMPACTED("BC-FC Impacted", false), BC_FC_REMOVED("BC-FC Removed", false),
    //
    NONE("DNG", false);
    //
    private final String linkTypeName;
    private final boolean backReferenceRequired;

    private DmRq1LinkToRequirement_Type(String linkTypeName, boolean backReferenceRequired) {
        this.linkTypeName = linkTypeName;
        this.backReferenceRequired = backReferenceRequired;
    }

    public String getLinkTypeName() {
        return linkTypeName;
    }

    public boolean isBackReferenceRequired() {
        return backReferenceRequired;
    }

    @Override
    public String toString() {
        return (linkTypeName.replace(' ', '_'));
    }

    @Override
    public String getText() {
        return (linkTypeName);
    }

    static public DmRq1LinkToRequirement_Type[] getDoorsTypes() {
        EnumSet<DmRq1LinkToRequirement_Type> enumSetForDoors = EnumSet.complementOf(EnumSet.of(DmRq1LinkToRequirement_Type.NONE));
        DmRq1LinkToRequirement_Type[] result = enumSetForDoors.toArray(new DmRq1LinkToRequirement_Type[0]);
        return (result);
    }

    static public DmRq1LinkToRequirement_Type getType(String textForType) {
        for (DmRq1LinkToRequirement_Type value : values()) {
            if (value.getText().equals(textForType) == true) {
                return (value);
            }
        }
        return (null);
    }

}
