/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.Rq1.Records.DmRq1Pst;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Line_Release extends DmPPTField_ReferenceList<DmPPTRelease> {

    private final DmPPTCustomerProject PPT_PROJECT;
    private final DmPPTLine LINE;

    public DmPPTField_Reference_Line_Release(DmPPTLine parent, DmPPTCustomerProject element, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTRelease>(), nameForUserInterface);
        this.PPT_PROJECT = element;
        this.LINE = parent;
    }

    @Override
    public List<DmPPTRelease> getElementList() {
        if (element.isEmpty()) {
            for ( DmRq1Pst release : PPT_PROJECT.getPST_RELEASES().getElementList()) {
                if (release.isInProPlaToLine(LINE.getTitle())) {
                    DmPPTRelease rel = release.getPPT_Release(LINE);
                    if (rel == null) {
                        rel = new DmPPTRelease(release, LINE);
                        release.setPPT_Release(LINE, rel);
                    }
                    this.addElement(rel);
                }
            }
        }
        return element;
    }
}
