/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.Ecv.PST.Records.DmEcvBc;
import DataModel.Ecv.PST.Records.DmEcvFc;
import DataModel.PPT.Records.DmPPTInfoFcBc;
import DataModel.PPT.Records.DmPPTRelease;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_InfoFcBc extends DmPPTField_ReferenceList<DmPPTInfoFcBc> {

    private final DmPPTRelease PROGRAMMSTAND;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_Release_InfoFcBc(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTInfoFcBc>(), nameForUserInterface);
        this.PROGRAMMSTAND = parent;
        alreadyLoaded = false;
    }

    @Override
    public List<DmPPTInfoFcBc> getElementList() {
        if (!alreadyLoaded) {
            if (this.PROGRAMMSTAND.PST_PROGRAMMSTAND.getElement() != null) {
                for (DmEcvBc bc : this.PROGRAMMSTAND.PST_PROGRAMMSTAND.getElement().PROGRAMMSTAND_PAKETE.getElementList()) {
                    if (bc.BC_FC.getElementList().size() > 0) { //if there are fc under the bc
                        for (DmEcvFc fc : bc.BC_FC.getElementList()) {
                            this.addElement(new DmPPTInfoFcBc(PROGRAMMSTAND, fc, bc));
                        }
                    } else { //if there are no fcs, the fc information should be empty
                        this.addElement(new DmPPTInfoFcBc(PROGRAMMSTAND, bc));
                    }
                }
            }
            alreadyLoaded = true;
        }
        return element;
    }

}
