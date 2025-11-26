/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTRelease;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Line_Release_PlanDatPSTListeSysConst extends DmPPTField_ReferenceList<DmPPTRelease> {

    private final DmPPTLine LINE;

    public DmPPTField_Reference_Line_Release_PlanDatPSTListeSysConst(DmPPTLine parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTRelease>(), nameForUserInterface);
        this.LINE = parent;
    }

    @Override
    public List<DmPPTRelease> getElementList() {
        if (element.isEmpty()) {
            for (DmPPTRelease release : LINE.PPT_RELEASES.getElementList()) {
                if (release.exportPlanungsdaten() == true) {
                    this.addElement(release);
                }
            }
        }
        return element;
    }

}
