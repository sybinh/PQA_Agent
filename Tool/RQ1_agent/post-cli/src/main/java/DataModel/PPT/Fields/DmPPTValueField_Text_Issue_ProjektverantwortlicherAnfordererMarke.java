/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTAenderung;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererMarke extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererMarke(DmPPTAenderung parent, String nameForUserInterface) {
        super(parent, calcValue(parent), nameForUserInterface);
    }

    public static String calcValue(DmPPTAenderung parent) {
        if (parent.IRM instanceof DmRq1Irm_Pst_IssueSw) {
            DmRq1Irm_Pst_IssueSw specificIRM = (DmRq1Irm_Pst_IssueSw) parent.IRM;
                return specificIRM.EXTERNAL_ASSIGNEE_ORGANIZATION.getValueAsText();
            }
        return "";
    }
}
