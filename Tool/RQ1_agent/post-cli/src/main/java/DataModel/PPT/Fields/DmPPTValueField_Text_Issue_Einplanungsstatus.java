/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTAenderung;
import Rq1Data.Enumerations.LifeCycleState_IRM;
import Rq1Data.Enumerations.LifeCycleState_RRM;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Issue_Einplanungsstatus extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Issue_Einplanungsstatus(DmPPTAenderung parent, String nameForUserInterface) {
        super(parent, calcValue(parent), nameForUserInterface);
    }

    public static String calcValue(DmPPTAenderung ISSUE) {
        String einplanungsStatus = "";
        if (ISSUE.PPT_BC.getElement() != null || ISSUE.PPT_FC.getElement() != null) {
            LifeCycleState_RRM lcs_RRM = null;
            if (ISSUE.PPT_FC.getElement() != null) {
                //There is a FC for the Issue, so check the RRM between BC and PVER
                lcs_RRM = ISSUE.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().LCS_RRM;
            } else if (ISSUE.PPT_BC.getElement() != null) {
                //There is a FC for the Issue, so check the RRM between BC and PVER
                lcs_RRM = ISSUE.PPT_BC.getElement().LCS_RRM;
            }
            assert (lcs_RRM != null);
            switch (lcs_RRM) {
                case IMPLEMENTED:
                    einplanungsStatus = "100";
                    break;
                case PLANNED:
                    einplanungsStatus = "75";
                    break;
                case NEW:
                case REQUESTED:
                case CONFLICTED:
                    einplanungsStatus = "25";
                    break;
                default:
                    einplanungsStatus = "0";
            }
        }
        else if (ISSUE.PPT_RELEASE.getElement() != null) {
            //There is a IRM
            LifeCycleState_IRM lcs_IRM = ISSUE.LCS_IRM;
            assert (lcs_IRM != null);
            switch (lcs_IRM) {
                case IMPLEMENTED:
                case QUALIFIED:
                    einplanungsStatus = "100";
                    break;
                case PLANNED:
                    einplanungsStatus = "75";
                    break;
                case NEW:
                case REQUESTED:
                case CONFLICTED:
                    einplanungsStatus = "25";
                    break;
                default:
                    einplanungsStatus = "0";
            }
        }
        return einplanungsStatus;
    }
}
