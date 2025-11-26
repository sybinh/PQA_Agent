/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import java.util.Set;

/**
 * Defines milestone types that shall be added or removed from the milestone
 * types loaded from the Plib configuration.
 *
 * Milestone types that shall be added: <br>
 * Predefined milestones are milestones that shall always be available in the
 * list of milestones that are shown in the dialog for creating new milestones.
 *
 * Not for all of these milestones, tasks might exist in the Plib config table.
 * This is why this enumeration was introduced.
 *
 * Milestone types that shall be removed:<br>
 * Some milestone types listed in the Plib configuration are releases in the
 * project. These milestone types have to be removed from the list of milestones
 * proposed by GuidedPjM.
 *
 * @author GUG2WI
 */
public enum DmGpmPredefinedMilestoneTypes {

    QGP0("QGP0", Type.MILESTONE),
    QGP1("QGP1", Type.MILESTONE),
    QGP2("QGP2", Type.MILESTONE),
    QGC0("QGC0", Type.MILESTONE),
    QGC1("QGC1", Type.MILESTONE),
    QGC2("QGC2", Type.MILESTONE),
    QGC3("QGC3", Type.MILESTONE),
    QGC4("QGC4", Type.MILESTONE),
    QGC5("QGC5", Type.MILESTONE),
    SW_QGC0("SW-QGC0", Type.MILESTONE),
    SW_QGB("SW-QGB", Type.MILESTONE),
    SW_QGC4("SW-QGC4", Type.MILESTONE),
    SW_QGR("SW-QGR", Type.MILESTONE),
    SW_QGF("SW-QGF", Type.MILESTONE),
    PMAP("PMAP", Type.MILESTONE),
    RB_SOP("RB-SOP", Type.MILESTONE),
    CUSTOMER_SOP("Customer-SOP", Type.MILESTONE),
    MAJOR("Major", Type.RELEASE),
    MINOR("Minor", Type.RELEASE),
    SERIES("Series", Type.RELEASE),
    SERIES_MAJOR("Series - Major", Type.RELEASE),
    FINAL("Final", Type.RELEASE),
    FINAL_MAJOR("Final - Major", Type.RELEASE),
    PRELIMINARY("Preliminary", Type.RELEASE),
    PRELIMINARY_MAJOR("Preliminary - Major", Type.RELEASE),
    PROTOTYPE("Prototype", Type.RELEASE);

    private enum Type {
        MILESTONE, // A milestone in the project.
        RELEASE; // A release. Cannot be used as milestone in the project.
    }

    final private String name;
    final private Type type;

    DmGpmPredefinedMilestoneTypes(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Removes and adds the predefined milestones from the given set.
     *
     * @param milestoneNames
     */
    static public void addAndRemovedPredefinedMilestones(Set<String> milestoneNames) {
        for (DmGpmPredefinedMilestoneTypes value : values()) {
            switch (value.type) {
                case RELEASE:
                    milestoneNames.remove(value.name);
                    break;
                case MILESTONE:
                    milestoneNames.add(value.name);
                    break;
                default:
                    throw (new Error("Unexpected type: " + value.type));

            }
        }
    }

    static public void addPredefinedMilestones(Set<String> milestoneNames) {
        for (DmGpmPredefinedMilestoneTypes value : values()) {
            switch (value.type) {
                case RELEASE:
                    break;
                case MILESTONE:
                    milestoneNames.add(value.name);
                    break;
                default:
                    throw (new Error("Unexpected type: " + value.type));

            }
        }
    }

}
