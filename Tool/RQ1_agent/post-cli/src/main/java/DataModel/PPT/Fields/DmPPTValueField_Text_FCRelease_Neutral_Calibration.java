/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.Rq1.Records.DmRq1Fc;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_FCRelease_Neutral_Calibration extends DmPPTValueField_Text {

    public DmPPTValueField_Text_FCRelease_Neutral_Calibration(DmRq1Fc parent, String nameForUserInterface) {
        super(parent, getNeutralCalibration(parent), nameForUserInterface);
    }

    private static String getNeutralCalibration(DmRq1Fc fc) {
        String neutralCalibration = "";
        switch (fc.NEUTRAL_CALIBRATION.getValueAsText().toUpperCase()) {
            case "NO":
                neutralCalibration = "nein";
                break;
            case "YES":
                neutralCalibration = "ja";
                break;
            default:
                neutralCalibration = "unbekannt";
        }
        return neutralCalibration;
    }

}
