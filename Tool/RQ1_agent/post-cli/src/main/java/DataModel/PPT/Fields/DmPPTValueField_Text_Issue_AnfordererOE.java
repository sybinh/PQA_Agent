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
import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Issue_AnfordererOE extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Issue_AnfordererOE(DmPPTAenderung parent, DmRq1IssueSW issueRQ1, DmPPTBCRelease bc, DmPPTFCRelease fc, String nameForUserInterface) {
        super(parent, calcValue(parent, issueRQ1, bc, fc), nameForUserInterface);
    }

    public static String calcValue(DmPPTAenderung parent, DmRq1IssueSW issueRQ1, DmPPTBCRelease bc, DmPPTFCRelease fc) {
        String company = "";
        if (issueRQ1 != null) { //the issue exists in RQ1
            if (issueRQ1.EXTERNAL_ASSIGNEE_DEPARTMENT != null) {
                //External Assignee is there
                company = issueRQ1.EXTERNAL_ASSIGNEE_DEPARTMENT.getValueAsText();
            }
            if (company.trim().isEmpty() && issueRQ1.EXTERNAL_ASSIGNEE_ORGANIZATION != null) {
                company = issueRQ1.EXTERNAL_ASSIGNEE_ORGANIZATION.getValueAsText();
            }
            if (company.trim().isEmpty()) {
                //External Assignee is not there, so take assignee
                company = DmPPTAenderung.getTheAnfordererOE(issueRQ1.ASSIGNEE_FULLNAME.getValueAsText());
            }
        } else if (bc != null) { //dummy issue at bc
            company = DmPPTAenderung.getTheAnfordererOE(bc.BC_RESPONSIBLE_FULLNAME.getValueAsText());
        } else if (fc != null) {
            company = DmPPTAenderung.getTheAnfordererOE(fc.HAS_PARENT_RELEASE.getElement().BC_RESPONSIBLE_FULLNAME.getValueAsText());
        }
        if (company.isEmpty()) {
            return "unbekannt";
        } else {
            return company;
        }
    }
}
