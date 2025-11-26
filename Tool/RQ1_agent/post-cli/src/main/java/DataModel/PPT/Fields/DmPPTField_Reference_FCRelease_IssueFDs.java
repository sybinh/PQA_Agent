/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.DmMappedElement;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_FCRelease_IssueFDs extends DmPPTField_ReferenceList<DmRq1IssueFD> {

    private final DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> ISSUES;
    private final DmPPTFCRelease FC;

    public DmPPTField_Reference_FCRelease_IssueFDs(DmPPTFCRelease parent,
            DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> elements, String nameForUserInterface) {
        super(parent, new LinkedList<DmRq1IssueFD>(), nameForUserInterface);
        this.ISSUES = elements;
        this.FC = parent;
    }

    @Override
    public List<DmRq1IssueFD> getElementList() {
        if (element.isEmpty()) {
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> elem : this.ISSUES.getElementList()) {
                if (!elem.getMap().isCanceled() && !elem.getTarget().isCanceled()) {
                    DmRq1IssueFD issueFD = (DmRq1IssueFD) elem.getTarget();
                    this.element.add(issueFD);
                }
            }
        }
        return element;
    }
}