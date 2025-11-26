/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTAenderung;
import DataModel.PPT.Records.DmPPTAenderung_Bc;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_BCRelease_Issues_wihout_FC_relation extends DmPPTField_ReferenceList<DmPPTAenderung_Bc> {

    private final DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> ISSUES;
    private final DmPPTBCRelease BC;

    public DmPPTField_Reference_BCRelease_Issues_wihout_FC_relation(DmPPTBCRelease parent,
            DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> elements, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTAenderung_Bc>(), nameForUserInterface);
        this.ISSUES = elements;
        this.BC = parent;
    }

    @Override
    public List<DmPPTAenderung_Bc> getElementList() {
        if (element.isEmpty()) {
            for ( DmPPTAenderung_Bc pptIssueSW : this.BC.AENDERUNG_FOR_BC.getElementList()) {
                if (!BC.PPT_FCS.isIssueUnderFc(pptIssueSW)) {
                    addElement(pptIssueSW);
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
