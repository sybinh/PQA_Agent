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
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 *
 * @author gug2wi
 */
public abstract class DmPPTAenderung_Bc extends DmPPTAenderung_OnPst {

    protected DmPPTAenderung_Bc(DmPPTBCRelease bc) {
        super(null, bc, null, null, null, "", "", "");
        assert (bc != null);
        initBcValues(bc);
    }

    protected DmPPTAenderung_Bc(DmRq1IssueSW rq1IssueSW, DmPPTBCRelease bc, DmPPTRelease release, DmRq1Irm irm, DmRq1IssueFD issueFD, String rbComment, String ctc) {
        super(rq1IssueSW, bc, null, release, irm, rbComment, ctc, issueFD.getId());
        assert (rq1IssueSW != null);
        assert (bc != null);
        initBcValues(bc);
    }

    private void initBcValues(DmPPTBCRelease bc) {
        PPT_RRM_RELEASE_BC_CTC.setValue(bc.PPT_CTC.getValueAsText());
        PPT_RRM_RELEASE_BC_RB_COMMENT.setValue(bc.PPT_RB_COMMENT.getValueAsText());
        PPT_ISSUE_COMMENT.setValue(DmPPTValueField_Text_Issue_IssueComment.calcValue(this, PPT_IRM_TO_RELEASE_CTC.getValueAsText(), PPT_IRM_TO_RELEASE_RB_COMMENT.getValueAsText()));
    }

}
