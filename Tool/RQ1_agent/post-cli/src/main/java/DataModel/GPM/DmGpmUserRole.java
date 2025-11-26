/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import java.util.Set;

/**
 * Defines the user roles defined for GPM.
 *
 * @author GUG2WI
 */
public enum DmGpmUserRole {

    CM_R("CM-R", "CM Responsible ECU Project"),
    CAL_ENG("CalEng", "Calibration Engineer"),
    DH("DH", "Department Head"),
    PJM_ECU("PjM ECU", "ECU Project Manager"),
    EDR("EDR", "EEPROM Department Responsible"),
    FMEA_MOD("FMEA-Mod", "FMEA Moderator"),
    FN_D("FN-D", "Function Developer"),
    GRM("GrM", "Group Manager"),
    HW_ARCH_ECU("HW ARCH ECU", "HW Architect ECU"),
    HW_EPQ("HW-EPQ", "HW Engineering Product Quality"),
    HW_INT("HW INT", "HW Integrator"),
    IM("IM", "Innovation Manager"),
    LAM("LaM", "Launch Manager"),
    LEAD_EPQ("Lead-EPQ", "Lead-EPQ"),
    MOD("MOD", "Moderator"),
    PEC_C("PEC-C", "Production Engineering Coordinator Customer"),
    PL_A("PL-A", "Product Line Architect"),
    PSM("PSM", "Project Safety Manager"),
    P_SEC_M("PSecM", "Project Security Manager"),
    PFM("PfM", "Platform Manager"),
    PJM("PjM", "Project Manager"),
    QC_D_CAL("QC-D CAL", "Q-Champion Department CAL"),
    QC_D("QC-D", "Q-Champion Department"),
    QC_P("QC-P", ""),
    QG_MODERATOR("", "QG Moderator"),
    SCR("SCR", "Smart Card Responsible"),
    SEC("SEC", "System Engineer Customer"),
    SPJM_CAL("SPjM CAL", "Subproject Manager Calibration"),
    SPJM_HW("SPjM HW", "Subproject Manager Hardware"),
    SPJM_SW("SPjM SW", "Subproject Manager Software"),
    SUP_M("SupM", "Supplier Manager"),
    SW_PD("SW PD", "Software Project Developer"),
    SW_RESMAN("SW ResMan", "SW Resource Manager"),
    SW_SD("SW SD", "Software System Designer"),
    SW_SH_M("SWSH-M", "SW-Sharing Manager"),
    SW_D("SW-D", "Software Developer"),
    SW_SMR("SW-SMR", "SW Series Maintenance Responsible"),
    SALES("Sales", "Technical Sales"),
    SCM("SCM", "Subcontract Manager"),
    SYS_ARCH("SysArch", "System Architect"),
    SYS_PJM("Sys-PjM", "System Project Manager"),
    TECHNICAL_SALES_ECU("", "Technical Sales (ECU)"),
    TECHNICAL_SALES_SW_CALIBRATION("", "Technical Sales (SW & Calibration)"),
    TEST_AUTOMATION_ENGINEER("", "Test Automation Engineer"),
    TEST_ENGINEER("", "Test Engineer"),
    VIVA_TESTER("VIVA Tester", "VIVA Tester");

    private static Set<String> allRoleNames = null;

    final private String abbreviation;
    final private String longName;

    DmGpmUserRole(String abbreviation, String longName) {
        assert (abbreviation != null);
        assert (longName != null);

        this.abbreviation = abbreviation;
        this.longName = longName;
    }


    public String getAbbreviation() {
        return abbreviation;
    }

    public String getLongName() {
        return longName;
    }

    public String getCombinedName() {
        if (abbreviation.isEmpty() == true) {
            return longName;
        }
        if (longName.isEmpty() == true) {
            return abbreviation;
        }
        return longName + " (" + abbreviation + ")";
    }

    static public DmGpmUserRole getByAbbreviation(String abbreviation) {
        if (abbreviation != null) {
            for (DmGpmUserRole value : values()) {
                if (value.abbreviation.equals(abbreviation)) {
                    return (value);
                }
            }
        }
        return (null);
    }

    static public DmGpmUserRole getByLongNameOrAbbreviation(String longNameOrAbbreviation) {
        

        if (longNameOrAbbreviation != null) {
            for (DmGpmUserRole value : values()) {
                if (value.longName.equals(longNameOrAbbreviation)) {
                    return (value);
                }
                if (value.abbreviation.equals(longNameOrAbbreviation)) {
                    return (value);
                }
            }
        }
        return (null);
    }

}
