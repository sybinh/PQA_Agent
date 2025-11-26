/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTAenderung_IssueSW;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Rq1Data.Enumerations.Scope;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_PPT_CustProj_IssueSW extends DmPPTField_ReferenceList<DmPPTAenderung_IssueSW> {

    private final DmRq1Field_ReferenceList<DmRq1IssueSW> ISSUES;
    private final DmPPTCustomerProject parent;

    public DmPPTField_Reference_PPT_CustProj_IssueSW(DmPPTCustomerProject parent, DmRq1Field_ReferenceList<DmRq1IssueSW> elements, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTAenderung_IssueSW>(), nameForUserInterface);
        this.parent = parent;
        this.ISSUES = elements;
    }

    @Override
    public List<DmPPTAenderung_IssueSW> getElementList() {
        this.parent.RQ1_CUSTOMER_PROJECT.getElement().loadCacheWithProPlaToIssues();
        if (element.isEmpty()) {
            for (DmRq1IssueSW issue : ISSUES.getElementList()) {
                if (issue.SCOPE.getValue().equals(Scope.EXTERNAL)) {
                    DmPPTAenderung_IssueSW issuePPT = new DmPPTAenderung_IssueSW(issue);
                    this.addElement(issuePPT);
                }
            }
        }
        return element;
    }
}
