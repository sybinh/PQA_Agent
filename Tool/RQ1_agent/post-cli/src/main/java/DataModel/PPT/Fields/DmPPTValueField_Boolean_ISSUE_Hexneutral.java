/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTAenderung;
import Rq1Cache.Types.Rq1XmlTable_RrmChangesToIssues;
import Rq1Data.Enumerations.CtiClassification;
import java.util.Iterator;
import java.util.TreeSet;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Boolean_ISSUE_Hexneutral extends DmPPTValueField_Boolean {

    public DmPPTValueField_Boolean_ISSUE_Hexneutral(DmPPTAenderung parent, String issueFdId, String nameForUserInterface) {
        super(parent, getHexneutralIssueSW(parent, issueFdId), nameForUserInterface);
    }

    private static Boolean getHexneutralIssueSW(DmPPTAenderung parent, String issueFdId) {

        EcvTableData dataCTI = null;
        Rq1XmlTable_RrmChangesToIssues descCTI = null;
        String line = "";
        DmPPTCustomerProject pool = null;

        //Check FC
        if (parent.PPT_FC.getElement() != null) {
            //Issue is from FC
            dataCTI = parent.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().CHANGES_TO_ISSUES.getValue();
            descCTI = parent.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().CHANGES_TO_ISSUES.getTableDescription();
            line = parent.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().NAME.getValueAsText();
            pool = parent.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement();

            switch (parent.PPT_FC.getElement().PPT_FC_HEXNEUTRAL.getValue()) {
                case "false":
                    return false;
                case "true":
                    return true;
                case "unbekannt":
                    switch (parent.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_BC_HEXNEUTRAL.getValue()) {
                        case "false":
                            return false;
                        case "true":
                            return true;
                    }
                    break;
            }
        } else if (parent.PPT_BC.getElement() != null) {
            dataCTI = parent.PPT_BC.getElement().CHANGES_TO_ISSUES.getValue();
            descCTI = parent.PPT_BC.getElement().CHANGES_TO_ISSUES.getTableDescription();
            line = parent.PPT_BC.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().NAME.getValueAsText();
            pool = parent.PPT_BC.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement();
            switch (parent.PPT_BC.getElement().PPT_BC_HEXNEUTRAL.getValue()) {
                case "false":
                    return false;
                case "true":
                    return true;
            }
        }

        String hexCTI = "unbekannt";
        if ((parent.PPT_FC.getElement() != null || parent.PPT_BC.getElement() != null) && (dataCTI != null && descCTI != null)) {
            //Check IssueFD, if there is a FC or a BC
            boolean takeTheCTI = false;

            for (EcvTableRow row : dataCTI.getRows()) {
                if (row.getValueAt(descCTI.DERIVATIVE) != null && !row.getValueAt(descCTI.DERIVATIVE).toString().isEmpty()) {
                    Iterator<String> iter = getIteratorOfObject(row.getValueAt(descCTI.DERIVATIVE));
                    while (iter.hasNext()) {
                        String derivate = iter.next();
                        if (pool != null && line.equals(pool.getProPlaToLineExternal(derivate))) {
                            takeTheCTI = true;
                        }
                    }
                } else {
                    takeTheCTI = true;
                }

                if (takeTheCTI) {
                    if (row.getValueAt(descCTI.TYPE) != null && row.getValueAt(descCTI.TYPE).equals("I-FD")
                            && row.getValueAt(descCTI.ID) != null && row.getValueAt(descCTI.ID).equals(issueFdId)) {
                        if (row.getValueAt(descCTI.CLASSIFICATION) != null && row.getValueAt(descCTI.CLASSIFICATION).equals(CtiClassification.HEXNEUTRAL.getText())) {
                            if (!hexCTI.equals("false")) {
                                hexCTI = "true";
                            }
                        } else {
                            hexCTI = "false";
                        }
                    }
                    takeTheCTI = false;
                }
            }
        }

        //Issue SW is not from FC and not from BC so the Hexneutral is false
        switch (hexCTI) {
            case "false":
                return false;
            case "true":
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static Iterator<String> getIteratorOfObject(Object d) {
        return ((TreeSet<String>) d).iterator();
    }

}
