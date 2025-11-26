/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.PPT.Records.DmPPTInfoSysCo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Line_InfoSyCo extends DmPPTField_ReferenceList<DmPPTInfoSysCo> {

    private final DmPPTLine LINE;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_Line_InfoSyCo(DmPPTLine parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTInfoSysCo>(), nameForUserInterface);
        this.LINE = parent;
        alreadyLoaded = false;
    }

    @Override
    public List<DmPPTInfoSysCo> getElementList() {
        if (!alreadyLoaded) {
            //for (DmPPTRelease release : LINE.PPT_CUPF_RELEASES.getElementList()) {
            for (DmPPTRelease release : LINE.PPT_RELEASES_PLANDAT_PST_SYSCONST.getElementList()) {
                this.addElements(release.SYSTEMKONSTANTEN_TIDIED.getElementList());

            }
            alreadyLoaded = true;
        }
        return element;
    }
}
