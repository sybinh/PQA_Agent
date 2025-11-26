/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTAenderung;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Rq1Data.Enumerations.DefectOrganisation;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.SoftwareIssueCategory;
import util.EcvEnumeration;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Issue_Vorgangstyp extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Issue_Vorgangstyp(DmPPTAenderung parent, String ctc, String nameForUserInterface) {
        super(parent, "", nameForUserInterface);

        if (parent.RQ1_ISSUE_SW.getElement() == null) {
            //Dummy Isse so Platformübernahme
            this.setValue(calcVorgangsstyp(null, null, null, true, false, null, false));
        } else {
            DmRq1IssueSW rq1Issue = parent.RQ1_ISSUE_SW.getElement();
            this.setValue(calcVorgangsstyp(parent.getTitle(),
                    rq1Issue.CATEGORY.getValue(),
                    rq1Issue.DEFECT_INJECTION_ORGANISATION.getValue(),
                    false,
                    !rq1Issue.CHILDREN.getElementList().isEmpty(),
                    rq1Issue.LIFE_CYCLE_STATE.getValue(), ctc.contains("SY")));
        }
    }

    public static String calcVorgangsstyp(String title, EcvEnumeration category, String defectInjectionOrganisation,
            boolean isDummy, boolean hasIssueFD, EcvEnumeration lcs, boolean hasSystemConstValue) {
        //category is a IssueCategory
        //defectInjectionOrganisation is a  DefectOrganisation
        //lcs is a LifeCycleState_Issue
        if (isDummy) {
            return "50";
        }
        if (category.equals(SoftwareIssueCategory.DEFECT) && !defectInjectionOrganisation.equals(DefectOrganisation.CUSTOMER.getText())) {
            return "60";
        }
        if ((lcs.equals(LifeCycleState_Issue.COMMITTED) || lcs.equals(LifeCycleState_Issue.IMPLEMENTED) || lcs.equals(LifeCycleState_Issue.CLOSED))
                && hasSystemConstValue && !hasIssueFD) {
            return "30";
        }
        if (title.startsWith("FID")) {
            return "20";
        }
        if( category.equals(SoftwareIssueCategory.NON_FUNCTIONAL)){
            return "5";
        }
        return "10";
    }
}
