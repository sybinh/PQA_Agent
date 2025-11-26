/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.SystemConstants.Fields;

import DataModel.Ecv.SystemConstants.Records.DmEcvProgrammstand_SysConst;
import DataModel.Ecv.SystemConstants.Records.DmEcvSysConst;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvField_Reference_Programmstand_SysConst {

    final private List<DmEcvSysConst> elements = new ArrayList<>();

    private EcvXmlContainerElement CONTAINER;

    public DmEcvField_Reference_Programmstand_SysConst(DmEcvProgrammstand_SysConst parent, EcvXmlContainerElement container, String nameForUserInterface) {
        this.CONTAINER = container;
    }

    public List<DmEcvSysConst> getElementList() {
        if (elements.isEmpty() && CONTAINER != null) {
            for (EcvXmlContainerElement systemKonstante : CONTAINER.getContainerElementList("Systemkonstante")) {
                addElement(new DmEcvSysConst(systemKonstante));
            }
        }
        this.CONTAINER = null;
        return elements;
    }

    public List<DmEcvSysConst> getElementByName(String name) {
        List<DmEcvSysConst> result = new LinkedList<>();
        for (DmEcvSysConst sysConst : this.getElementList()) {
            if (sysConst.getNameNotNull().equals(name)) {
                result.add(sysConst);
            }
        }
        return result;
    }

    public void addElement(DmEcvSysConst sysConst) {
        elements.add(sysConst);
}
}
