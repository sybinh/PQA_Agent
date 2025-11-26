/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.Ecv.SystemConstants.Records.DmEcvSysConst;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.PPT.Records.DmPPTInfoSysCo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_InfoSyCo extends DmPPTField_ReferenceList<DmPPTInfoSysCo> {

    private final DmPPTRelease PROGRAMMSTAND;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_Release_InfoSyCo(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTInfoSysCo>(), nameForUserInterface);
        this.PROGRAMMSTAND = parent;
        alreadyLoaded = false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DmPPTInfoSysCo> getElementList() {
        if (!alreadyLoaded) {
            List syNames = new LinkedList<>();
            if (this.PROGRAMMSTAND.PPT_SYSTEM_CONSTANTS.getElement() != null) {
                for (DmEcvSysConst sysConst : this.PROGRAMMSTAND.PPT_SYSTEM_CONSTANTS.getElement().PROGRAMMSTAND_SYSTEMKONSTANTEN.getElementList()) {
                    if (!syNames.contains(sysConst.getNameNotNull())) {
                        syNames.add(sysConst.getNameNotNull());
                        this.addElement(new DmPPTInfoSysCo(PROGRAMMSTAND, sysConst));
                    }
                }
            }
            alreadyLoaded = true;
        }
        return element;
    }

}
