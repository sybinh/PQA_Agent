/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTRelease;
import java.util.LinkedList;
import java.util.List;

/**
 * All Releases, which are relevant for PSTPlanungsliste (Systemkonstanten) If
 * there is no, the Release with the planned date furthest in the future should
 * be taken (of the Line)
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Line_Release_CUPF_File_generation extends DmPPTField_ReferenceList<DmPPTRelease> {

    private final DmPPTCustomerProject PPT_PROJECT;
    private final DmPPTLine LINE;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_Line_Release_CUPF_File_generation(DmPPTLine parent, DmPPTCustomerProject element, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTRelease>(), nameForUserInterface);
        this.PPT_PROJECT = element;
        this.LINE = parent;
        alreadyLoaded = false;
    }

    @Override
    public List<DmPPTRelease> getElementList() {
        if (!alreadyLoaded) {
            if (LINE.PPT_RELEASES_PLANDAT_PST_SYSCONST.getElementList().size() > 0) {
                this.element.addAll(LINE.PPT_RELEASES_PLANDAT_PST_SYSCONST.getElementList());
            } else {
                DmPPTRelease releseFurthestInFuture = null;
                for (DmPPTRelease rec : LINE.PPT_RELEASES_WITHOUT_DATE_LIMITATION.getElementList()) {
                    if (releseFurthestInFuture == null) {
                        releseFurthestInFuture = rec;
                    } else if (rec.AUSLIEFERDATUM_SOLL.getValue().isLaterThen(releseFurthestInFuture.AUSLIEFERDATUM_SOLL.getValue())) {
                        releseFurthestInFuture = rec;
                    }
                }
                if (releseFurthestInFuture != null) {
                    this.addElement(releseFurthestInFuture);
                }
            }
            alreadyLoaded = true;
        }
        return element;
    }
}
