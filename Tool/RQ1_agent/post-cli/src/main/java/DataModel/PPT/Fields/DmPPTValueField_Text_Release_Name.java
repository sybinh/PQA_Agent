/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1PverRelease;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Release_Name extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Release_Name(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, getNameFromProgrammstand(parent), nameForUserInterface);
    }

    public static String getNameFromProgrammstand(DmPPTRelease programmstand) {
        DmRq1Pst release = programmstand.getRq1Release();
        if (release instanceof DmRq1PverRelease) {
            return (release.EXTERNAL_ID.getValue()
                    + release.EXTERNAL_TITLE.getValue());
        } else {
            return (programmstand.BELONG_TO_SCHIENE.getElement().NAME.getValueAsText()
                    + release.EXTERNAL_TITLE.getValue());
        }
    }

}
