/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Doors;

/**
 *
 * @author GUG2WI
 */
public enum DmRq1ToDoorsLinkType {
    //
    L1_PLANNED("L1 Planned", true), L1_REMOVED("L1 Removed", false), //
    L2_PLANNED("L2 Planned", true), L2_IMPACTED("L2 Impacted", false), L2_REMOVED("L2 Removed", false), //
    L3_PLANNED("L3 Planned", true), L3_IMPACTED("L3 Impacted", false), L3_REMOVED("L3 Removed", false), //
    L4_PLANNED("L4 Planned", true), L4_IMPACTED("L4 Impacted", false), L4_REMOVED("L4 Removed", false);
    //
    private final String linkTypeName;
    private final boolean backReferenceRequired;

    private DmRq1ToDoorsLinkType(String linkTypeName, boolean backReferenceRequired) {
        this.linkTypeName = linkTypeName;
        this.backReferenceRequired = backReferenceRequired;
    }

    public String getLinkTypeName() {
        return linkTypeName;
    }

    public boolean isBackReferenceRequired() {
        return backReferenceRequired;
    }

}
