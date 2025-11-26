/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTInfoFcBc;
import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTRelease;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Line_InfoFcBc extends DmPPTField_ReferenceList<DmPPTInfoFcBc> {

    private final DmPPTLine LINE;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_Line_InfoFcBc(DmPPTLine parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTInfoFcBc>(), nameForUserInterface);
        this.LINE = parent;
        alreadyLoaded = false;
    }

    @Override
    public List<DmPPTInfoFcBc> getElementList() {
        if (!alreadyLoaded) {
            //for (DmPPTRelease release : LINE.PPT_CUPF_RELEASES.getElementList()) {
            for (DmPPTRelease release : LINE.PPT_RELEASES_PLANDAT_PST_SYSCONST.getElementList()) {
                for (DmPPTInfoFcBc fcBcInfo : release.FCBC_TIDIED.getElementList()) {
                    if (!this.element.contains(fcBcInfo)) {
                        this.addElement(fcBcInfo);
                    }
                }
            }
            alreadyLoaded = true;
        }
        return element;
    }
}
