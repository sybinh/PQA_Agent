/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTAenderung;
import DataModel.PPT.Records.DmPPTAenderung_Bc;
import DataModel.PPT.Records.DmPPTAenderung_Bc_WithoutVisibleIssue;
import DataModel.PPT.Records.DmPPTAenderung_Bc_WithVisibleIssue_NotMappedToPst;
import DataModel.PPT.Records.DmPPTAenderung_Bc_WithVisibleIssue_MappedToPst;
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
public class DmPPTField_AenderungForBc extends DmPPTField_ReferenceList<DmPPTAenderung_Bc> {

    private final DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> MAPPED_ISSUES;
    private final DmPPTBCRelease bc;

    public DmPPTField_AenderungForBc(DmPPTBCRelease bc, String nameForUserInterface) {
        super(bc, new LinkedList<DmPPTAenderung_Bc>(), nameForUserInterface);

        this.MAPPED_ISSUES = bc.RQ1_BC_RELEASE.getElement().MAPPED_ISSUES;
        this.bc = bc;
    }

    @Override
    public List<DmPPTAenderung_Bc> getElementList() {

        if (element.isEmpty()) {
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> elem : MAPPED_ISSUES.getElementList()) {
                if (!elem.getMap().isCanceled() && !elem.getTarget().isCanceled()) {
                    DmRq1IssueFD issueFD = (DmRq1IssueFD) elem.getTarget();

                    if (issueFD.PARENT.getElement() != null && issueFD.PARENT.getElement().SCOPE.getValue().equals(Scope.EXTERNAL)) {
                        DmRq1IssueSW issueSW = (DmRq1IssueSW) issueFD.PARENT.getElement();

                        DmPPTPoolProject pool = bc.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement();
                        //Check if the issue is an AUDI Issue, if not, discard 
                        if (DmPPTAenderung.belongsToPool(issueSW, pool)) {

                            //Check if the issue is not in the fcs
                            DmPPTAenderung_IssueMappedOnPst issueMappedOnPst = bc.HAS_PARENT_RELEASE.getElement().MAPPED_ISSUES.getIssueById(issueSW.getRq1Id());

                            if (issueMappedOnPst != null) {
                                addElement(new DmPPTAenderung_Bc_WithVisibleIssue_MappedToPst(issueSW, bc, bc.HAS_PARENT_RELEASE.getElement(), issueMappedOnPst.IRM, issueFD, issueMappedOnPst.PPT_IRM_TO_RELEASE_RB_COMMENT.getValueAsText(), issueMappedOnPst.PPT_IRM_TO_RELEASE_CTC.getValueAsText()));
                            } else {
                                addElement(new DmPPTAenderung_Bc_WithVisibleIssue_NotMappedToPst(issueSW, bc, issueFD));
                            }

                        }
                    }
                }
            }

            if (element.isEmpty()) {
                //There are no issues at the bc
                //check if there is an fc under the bc
                if (bc.PPT_FCS.getElementList().isEmpty()) {
                    //There are no FCs under the bc
                    //We need a dummy Issue under the bc
                    DmPPTAenderung_Bc_WithoutVisibleIssue aenderung = new DmPPTAenderung_Bc_WithoutVisibleIssue(bc);
                    addElement(aenderung);
                }
            }
        }
        return element;
    }

    public boolean containsIssue(DmPPTAenderung givenIssue) {
        getElementList();
        return this.element.contains(givenIssue);
    }
}
