/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.SystemConstants.Fields;

import DataModel.DmElementI;
import DataModel.Ecv.Fields.DmEcvField_ReferenceList;
import DataModel.Ecv.SystemConstants.Records.DmEcvProgrammstandTable_SysConst;
import DataModel.Ecv.SystemConstants.Records.DmEcvProgrammstand_SysConst;
import java.util.LinkedList;
import java.util.List;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvField_Reference_Schiene_Programmstand_SysConst extends DmEcvField_ReferenceList<DmEcvProgrammstand_SysConst> {

    private EcvXmlContainerElement CONTAINER;

    public DmEcvField_Reference_Schiene_Programmstand_SysConst(DmElementI parent, EcvXmlContainerElement container, String nameForUserInterface) {
        super(parent, new LinkedList<DmEcvProgrammstand_SysConst>(), nameForUserInterface);
        this.CONTAINER = container;
    }

    @Override
    public List<DmEcvProgrammstand_SysConst> getElementList() {
        if (elements.isEmpty() && CONTAINER != null) {
            DmEcvProgrammstandTable_SysConst programmstandTable = new DmEcvProgrammstandTable_SysConst();
            programmstandTable.loadFromString(CONTAINER.getXmlString());
            for (DmEcvProgrammstand_SysConst programmstand : programmstandTable.getElements()) {
                this.addElement(programmstand);
            }
        }
        this.CONTAINER = null;
        return elements;
    }

}
