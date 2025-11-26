/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.PPT.Records.DmPPTAenderung;
import static DataModel.PPT.Records.DmPPTAenderung.getTheAnfordererName;
import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Issue_Anforderer extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Issue_Anforderer(DmPPTAenderung parent, DmRq1IssueSW issueRQ1, DmPPTBCRelease bc, DmPPTFCRelease fc, String nameForUserInterface) {
        super(parent, calcValue(parent, issueRQ1, bc, fc), nameForUserInterface);
    }

    public static String calcValue(DmPPTAenderung parent, DmRq1IssueSW issueRQ1, DmPPTBCRelease bc, DmPPTFCRelease fc) {
        String anforderer = "";
        if (issueRQ1 != null) { //the issue exists in RQ1
            if (issueRQ1.EXTERNAL_ASSIGNEE_FIRSTNAME != null && !issueRQ1.EXTERNAL_ASSIGNEE_FIRSTNAME.getValueAsText().isEmpty() &&
                issueRQ1.EXTERNAL_ASSIGNEE_LASTNAME != null && !issueRQ1.EXTERNAL_ASSIGNEE_LASTNAME.getValueAsText().isEmpty() ) {
                //External Assignee is there
                anforderer = issueRQ1.EXTERNAL_ASSIGNEE_LASTNAME.getValueAsText() + " " + issueRQ1.EXTERNAL_ASSIGNEE_FIRSTNAME.getValueAsText();
            } else {
                //External Assignee is not there, so take assignee
                anforderer = DmPPTAenderung.getTheAnfordererName(issueRQ1.ASSIGNEE_FULLNAME.getValueAsText());
            }
        } else if (bc != null) { //dummy issue at bc
            anforderer = getTheAnfordererName(bc.BC_RESPONSIBLE_FULLNAME.getValueAsText());
        } else if (fc != null) {
            anforderer = getTheAnfordererName(fc.HAS_PARENT_RELEASE.getElement().BC_RESPONSIBLE_FULLNAME.getValueAsText());
        }
        if (anforderer.isEmpty()) {
            return "unbekannt";
        } else {
            return anforderer;
        }
    }
}
