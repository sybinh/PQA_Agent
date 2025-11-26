/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_IssueComment;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Rq1Cache.Types.Rq1XmlTable_RrmChangesToIssues;
import java.util.Iterator;
import java.util.TreeSet;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * Issue with FC Connection
 *
 * @author gug2wi
 */
public abstract class DmPPTAenderung_Fc_WithVisibleIssue extends DmPPTAenderung_Fc {

    //Issue with FC Connection
    public DmPPTAenderung_Fc_WithVisibleIssue(DmRq1IssueSW rq1IssueSW, DmPPTFCRelease fc, DmPPTRelease release, DmRq1Irm irm, String issueFD, String rbComment, String ctc) {
        super(rq1IssueSW, fc, release, irm, issueFD, rbComment, ctc);
        this.PPT_RRM_RELEASE_BC_CTC.setValue(fc.HAS_PARENT_RELEASE.getElement().PPT_CTC.getValueAsText());
        this.PPT_RRM_RELEASE_BC_RB_COMMENT.setValue(fc.HAS_PARENT_RELEASE.getElement().PPT_RB_COMMENT.getValueAsText());
        this.PPT_RRM_RELEASE_BC_CTP.setValue(fc.FC_CTP.getValueAsText());
        //Get The CTI from the RRM between BC and FC and Check for the issueFD

        String cti = "";
        EcvTableData dataCTI = fc.HAS_PARENT_RELEASE.getElement().CHANGES_TO_ISSUES.getValue();
        Rq1XmlTable_RrmChangesToIssues descCTI = fc.HAS_PARENT_RELEASE.getElement().CHANGES_TO_ISSUES.getTableDescription();
        for (EcvTableRow row : dataCTI.getRows()) {
            if (row.getValueAt(descCTI.ID).equals(issueFD)) {
                //Belongs to the IssueFD between the IssueSW and the FC
                if (row.getValueAt(descCTI.DERIVATIVE) != null) {
                    Iterator<String> iter = this.getIteratorOfObject(row.getValueAt(descCTI.DERIVATIVE));
                    while (iter.hasNext()) {
                        String derivate = iter.next();
                        derivate = fc.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(derivate);
                        if (derivate.equals(fc.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().NAME.getValueAsText())) {
                            if (row.getValueAt(descCTI.EXTERNAL_COMMENT) != null && !row.getValueAt(descCTI.EXTERNAL_COMMENT).toString().isEmpty()) {
                                if (!cti.contains(row.getValueAt(descCTI.EXTERNAL_COMMENT).toString())) { //check if the text is already added
                                    cti += row.getValueAt(descCTI.EXTERNAL_COMMENT).toString() + "\n";
                                }
                            }
                        }
                    }
                } else if (row.getValueAt(descCTI.EXTERNAL_COMMENT) != null && !row.getValueAt(descCTI.EXTERNAL_COMMENT).toString().isEmpty()) {
                    if (!cti.contains(row.getValueAt(descCTI.EXTERNAL_COMMENT).toString())) { //check if the text is already added
                        cti += row.getValueAt(descCTI.EXTERNAL_COMMENT).toString() + "\n";
                    }
                }
            }
        }
        PPT_RRM_RELEASE_BC_CTI.setValue(cti);
        //We have to update the issuecomment
        this.PPT_ISSUE_COMMENT.setValue(DmPPTValueField_Text_Issue_IssueComment.calcValue(this, PPT_IRM_TO_RELEASE_CTC.getValueAsText(), PPT_IRM_TO_RELEASE_RB_COMMENT.getValueAsText()));
    }

    @SuppressWarnings("unchecked")
    private Iterator<String> getIteratorOfObject(Object d) {
        return ((TreeSet<String>) d).iterator();
    }

}
