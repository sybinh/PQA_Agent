/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

/**
 *
 * @author GUG2WI
 */
public enum RuleExecutionGroup {

    DATA_STORE("Data store access and validation", "Checks the data received from data stores and the successful write to data stores."),
    RQ1_DATA("RQ1 Data Validation", "Checks the data received from RQ1 database. These rules are checked for all data received from RQ1 database"),
    ELEMENT_INTEGRITY("Element Integrity", "Checks that the data fields of an element contain correct data according to formal criterias (e.g. name schema)."),
    UI_VISIBLE("UI-Visible", ""),
    //    ASAM_EXCHANGE("ASAM-Exchange", "Checks that the data fields for ASAM exchange are correct set,"),
    PVER_PLANNING_VIEW("PVER Planning View", "Checks if the planning of a PVER is complete and consistent."),
    PVAR_PLANNING_VIEW("PVAR Planning View", "Checks if the planning of a PVAR is complete and consistent."),
    //    PST_INTEGRATION("PVAR/PVER Integration", "Checks if the integration of an PVER/PVAR is complete and consistent."),
    BC_PLANNING("BC Planning", "Checks if the planning of a BC is complete and consistent."),
    FC_PLANNING("FC Planning", "Checks if the planning of a FC is complete and consistent."),
    I_SW_PLANNING("I-SW Planning", "Checks if the planning of a I-SW is complete and consistent"),
    PROJECT("Project", "Checks constistency of project."),
    PROPLATO("ProPlaTo", "Checks for ProPlaTo requirements."),
    PROPLATO_BACKCHANNEL("ProPlaTo BackChannel", "Checks for ProPlaTo BackChannel requirements."),
    EXCITED_CPC("excITED CPC", "Checks the rules for excITED CPC."),
    I_FD_PLANNING("I-FD Planning", "Checks if the planning of a I-FD is complete and consistent"),
    I_FD_PILOT("I-FD Pilot", "Checks if the I-SW of the I-FD has a pilot PVER or PVAR"),
    TEST("Test", "For automatic tests");
    //
    final String name;
    final String description;

    private RuleExecutionGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getGroupName() {
        return (name);
    }

    public String getGroupDescription() {
        return (description);
    }
}
