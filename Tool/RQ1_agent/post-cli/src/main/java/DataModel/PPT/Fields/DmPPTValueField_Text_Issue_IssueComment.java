/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTAenderung;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Issue_IssueComment extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Issue_IssueComment(DmPPTAenderung parent, String ctc, String rbComment, String nameForUserInterface) {
        super(parent, calcValue(parent, ctc, rbComment), nameForUserInterface);
    }

    public static String calcValue(DmPPTAenderung issue, String ctc, String rbComment) {
        String issueComment = "";

        if (issue.PPT_BC.getElement() == null && issue.PPT_RELEASE.getElement() == null
                && issue.RQ1_ISSUE_SW.getElement() == null && issue.PPT_FC.getElement() != null) {
            //dummy Issue for FC CASE 8, 9, 10, 11
            //TAKE The RB_Comment, and the CTP from the RRM between BC and Release
            if (!issue.PPT_FC.getElement().FC_CTP.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().FC_CTP.getValueAsText() + "\n";
            } else {
                issueComment += issue.PPT_RRM_RELEASE_BC_CTI.getValueAsText() + "\n";
            }
            if (!issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_CTC.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_CTC.getValueAsText() + "\n";
            }
            if (!issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_RB_COMMENT.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_RB_COMMENT.getValueAsText() + "\n";
            }
        } else if (issue.PPT_BC.getElement() != null && issue.PPT_RELEASE.getElement() == null
                && issue.RQ1_ISSUE_SW.getElement() == null && issue.PPT_FC.getElement() == null) {
            //dummy Issue for BC CASE 7
            //TAKE The RB_Comment, and the CTP from the RRM between BC and Release
            if (!issue.PPT_BC.getElement().PPT_CTC.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_BC.getElement().PPT_CTC.getValueAsText() + "\n";
            }
            if (!issue.PPT_BC.getElement().PPT_RB_COMMENT.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_BC.getElement().PPT_RB_COMMENT.getValueAsText() + "\n";
            }
        } else if (issue.PPT_FC.getElement() != null && issue.RQ1_ISSUE_SW.getElement() != null
                && issue.PPT_BC.getElement() == null && issue.PPT_RELEASE.getElement() == null) {
            //Issue bei FC
            //Case 12, 15 take the information from the RRM between Release and BC
            //Case 12, 15 => Take CTI only when there is no CTP

            if (!issue.PPT_FC.getElement().FC_CTP.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().FC_CTP.getValueAsText() + "\n";
            } else {
                issueComment += issue.PPT_RRM_RELEASE_BC_CTI.getValueAsText() + "\n";
            }

            if (!issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_CTC.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_CTC.getValueAsText() + "\n";
            }
            if (!issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_RB_COMMENT.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_RB_COMMENT.getValueAsText() + "\n";
            }
        } else if (issue.PPT_FC.getElement() != null && issue.RQ1_ISSUE_SW.getElement() != null
                && issue.PPT_BC.getElement() == null && issue.PPT_RELEASE.getElement() != null) {
            //Issue bei FC
            //Case 6 take the information from the RRM between Release and BC
            //Case 6 Take CTI

            if (!rbComment.isEmpty()) {
                issueComment += rbComment;
            }
            if (!ctc.isEmpty()) {
                issueComment += ctc;
            }

            if (!issue.PPT_FC.getElement().FC_CTP.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().FC_CTP.getValueAsText() + "\n";
            }
            if (!issue.PPT_RRM_RELEASE_BC_CTI.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_RRM_RELEASE_BC_CTI.getValueAsText() + "\n";
            }
            if (!issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_CTC.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_CTC.getValueAsText() + "\n";
            }
            if (!issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_RB_COMMENT.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().PPT_RB_COMMENT.getValueAsText() + "\n";
            }
        } else if (issue.PPT_FC.getElement() == null && issue.RQ1_ISSUE_SW.getElement() != null
                && issue.PPT_BC.getElement() != null && issue.PPT_RELEASE.getElement() != null) {
            //Issue bei BC ohne FC bezug
            //Case 4, 5 take the information from the RRM between Release and BC
            //Case 4, 5 additional take the information from the IRM between IssueSW and Release
            if (!rbComment.isEmpty()) {
                issueComment += rbComment;
            }
            if (!ctc.isEmpty()) {
                issueComment += ctc;
            }

            if (!issue.PPT_BC.getElement().PPT_CTC.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_BC.getElement().PPT_CTC.getValueAsText() + "\n";
            }
            if (!issue.PPT_BC.getElement().PPT_RB_COMMENT.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_BC.getElement().PPT_RB_COMMENT.getValueAsText() + "\n";
            }
        } else if (issue.PPT_FC.getElement() == null && issue.RQ1_ISSUE_SW.getElement() != null
                && issue.PPT_BC.getElement() != null && issue.PPT_RELEASE.getElement() == null) {
            //Issue bei BC ohne FC bezug
            //Case 13 take the information from the RRM between Release and BC
            if (!issue.PPT_BC.getElement().PPT_CTC.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_BC.getElement().PPT_CTC.getValueAsText() + "\n";
            }
            if (!issue.PPT_BC.getElement().PPT_RB_COMMENT.getValueAsText().isEmpty()) {
                issueComment += issue.PPT_BC.getElement().PPT_RB_COMMENT.getValueAsText() + "\n";
            }
        } else if (issue.PPT_FC.getElement() == null && issue.RQ1_ISSUE_SW.getElement() != null
                && issue.PPT_BC.getElement() == null && issue.PPT_RELEASE.getElement() != null) {
            //Case 1, 2, 3 take the RB comment between the issue and the Release
            if (!rbComment.isEmpty()) {
                issueComment += rbComment;
            }
            if (!ctc.isEmpty()) {
                issueComment += ctc;
            }
        }
        return issueComment;
    }
}
