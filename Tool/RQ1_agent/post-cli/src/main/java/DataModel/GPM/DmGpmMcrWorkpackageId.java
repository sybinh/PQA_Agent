package DataModel.GPM;

/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */

/**
 *
 * @author GEA83WI
 */
public enum DmGpmMcrWorkpackageId {

    B_SAMPLE_DEFINITION("B-Sample Definiton", "1008225"),
    C_SAMPLE_DEFINITION("C-Sample Definition", "1008226"),
    CUSTOMER_SOP("Customer-SOP", "1008228"),
    IG1_INNOVATION_GATE_1("IG1 - Innovation-Gate 1", "1008199"),
    IG1_INNOVATION_GATE_2("IG2 - Innovation-Gate 2", "1008200"),
    IG1_INNOVATION_GATE_3("IG3 - Innovation-Gate 3", "1008201"),
    PROJECT_KICKOFF_MEETING("Project KickoffMeeting", "1008206"),
    SW_QG0_SW_QUALITY_GATE_0("SW-QG0", "1008222"),
    SW_QG4_SW_QUALITY_GATE_4("SW-QG4", "1008224"),
    SW_QGR_SW_QUALITY_GATE_R("SW-QGR", "1008223"),
    SW_QGC0_SW_QUALITY_GATE_0("SW-QGC0", "1008222"),
    SW_QGC4_SW_QUALITY_GATE_4("SW-QGC4", "1008224"),
    QG0_QUALITY_GATE_0("QG0", "1008207"),
    QG1_QUALITY_GATE_1("QG1", "1008208"),
    QG2_QUALITY_GATE_2("QG2", "1008209"),
    QG3_QUALITY_GATE_3("QG3", "1008210"),
    QG4_QUALITY_GATE_4("QG4", "1008211"),
    QG5_QUALITY_GATE_5("QG5", "1008212"),
    QGC0_CUSTOMER_QUALITY_GATE_0("QGC0", "1008216"),
    QGC1_CUSTOMER_QUALITY_GATE_1("QGC1", "1008217"),
    QGC2_CUSTOMER_QUALITY_GATE_2("QGC2", "1008218"),
    QGC3_CUSTOMER_QUALITY_GATE_3("QGC3", "1008219"),
    QGC4_CUSTOMER_QUALITY_GATE_4("QGC4", "1008220"),
    QGC5_CUSTOMER_QUALITY_GATE_5("QGC5", "1008221"),
    QGP0_PLATFORM_QUALITY_GATE_0("QGP0", "1008213"),
    QGP1_PLATFORM_QUALITY_GATE_1("QGP1", "1008214"),
    QGP2_PLATFORM_QUALITY_GATE_2("QGP2", "1008215"),
    QGR_QUALITY_GATE_R("QGR - Quality-Gate R", "1013584"),
    SE0_SALES_EVALUATION_0("SE0 - Sales Evaluation 0", "1008202"),
    SE1_2_SALES_EVALUATION_1_2("SE1/2 - Sales Evaluation 1/2", "1008203"),
    SE3_SALES_EVALUATION_3("SE3 - Sales Evaluation 3", "1008204"),
    SE4_SALES_EVALUATION_4("SE4 - Sales Evaluation 4", "1008205"),
    SOP_COMPONENT("SOP -Component", "1008227");
    
    final private String milestoneName;
    final private String workpackageId;

    private DmGpmMcrWorkpackageId(String milestoneName, String workpackageId) {
        this.milestoneName = milestoneName;
        this.workpackageId = workpackageId;
    }

    public static String getWorkPackageId(String milestoneNameToFind) {
        assert (milestoneNameToFind != null);
        assert (milestoneNameToFind.isEmpty() == false);

        for (DmGpmMcrWorkpackageId value : values()) {

            if (milestoneNameToFind.indexOf(value.milestoneName) >= 0) {
                return (value.workpackageId);
            }
        }

        return (null);
    }

}
