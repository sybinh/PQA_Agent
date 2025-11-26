/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.PPT.Records.DmPPTAenderung;
import DataModel.PPT.Records.DmPPTAenderung_Fc_WithoutVisibleIssue;
import DataModel.PPT.Records.DmPPTAenderung_Fc;
import DataModel.PPT.Records.DmPPTAenderung_Fc_WithVisibleIssue_MappedToPst;
import DataModel.PPT.Records.DmPPTAenderung_Fc_WithVisibleIssue_NotMappedToPst;
import DataModel.PPT.Records.DmPPTAenderung_IssueMappedOnPst;
import DataModel.PPT.Records.DmPPTPoolProject;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.DmMappedElement;
import Rq1Data.Enumerations.Scope;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_FCRelease_Issues extends DmPPTField_ReferenceList<DmPPTAenderung_Fc> {

    private final DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> ISSUES;
    private final DmPPTFCRelease FC;

    public DmPPTField_Reference_FCRelease_Issues(DmPPTFCRelease parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTAenderung_Fc>(), nameForUserInterface);
        this.ISSUES = parent.RQ1_FC_RELEASE.getElement().MAPPED_ISSUES;
        this.FC = parent;
    }

    @Override
    public List<DmPPTAenderung_Fc> getElementList() {
        if (element.isEmpty()) {
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> elem : ISSUES.getElementList()) {
                if (!elem.getMap().isCanceled() && !elem.getTarget().isCanceled()) {
                    DmRq1IssueFD issueFD = (DmRq1IssueFD) elem.getTarget();
                    if (issueFD.PARENT.getElement() != null && issueFD.PARENT.getElement().SCOPE.getValue().equals(Scope.EXTERNAL)) {
                        DmRq1IssueSW issueSW = (DmRq1IssueSW) issueFD.PARENT.getElement();
                        DmPPTPoolProject pool = this.FC.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement();
                        //Check if the issue is an AUDI Issue, if not, discard 
                        if (DmPPTAenderung.belongsToPool(issueSW, pool)) {
                            DmPPTAenderung_IssueMappedOnPst issueMappesOnPst = FC.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().MAPPED_ISSUES.getIssueById(issueSW.getRq1Id());
                            if (issueMappesOnPst != null) {
                                addElement(new DmPPTAenderung_Fc_WithVisibleIssue_MappedToPst(issueSW, FC, FC.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement(), issueMappesOnPst.IRM, elem.getTarget().getId(), issueMappesOnPst.PPT_IRM_TO_RELEASE_RB_COMMENT.getValueAsText(), issueMappesOnPst.PPT_IRM_TO_RELEASE_CTC.getValueAsText()));
                            } else {
                                addElement(new DmPPTAenderung_Fc_WithVisibleIssue_NotMappedToPst(issueSW, FC,   elem.getTarget().getId()));
                            }
                        }
                    }
                }
            }
            if (element.isEmpty()) {
                addElement(new DmPPTAenderung_Fc_WithoutVisibleIssue(FC));
            }
        }
        return element;
    }

    public boolean containsIssue(DmPPTAenderung givenIssue) {
        getElementList();
        return element.contains(givenIssue);
    }
}
