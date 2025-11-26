/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.PST.Fields;

import DataModel.DmElementI;
import DataModel.Ecv.Fields.DmEcvField_ReferenceList;
import DataModel.Ecv.PST.Records.DmEcvProgrammstand;
import DataModel.Ecv.PST.Records.DmEcvProgrammstandTable;
import java.util.LinkedList;
import java.util.List;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvField_Reference_Schiene_Programmstand extends DmEcvField_ReferenceList<DmEcvProgrammstand> {

    private EcvXmlContainerElement CONTAINER;

    public DmEcvField_Reference_Schiene_Programmstand(DmElementI parent, EcvXmlContainerElement container, String nameForUserInterface) {
        super(parent, new LinkedList<DmEcvProgrammstand>(), nameForUserInterface);
        this.CONTAINER = container;
    }

    @Override
    public List<DmEcvProgrammstand> getElementList() {
        if (elements.isEmpty() && CONTAINER != null) {
            DmEcvProgrammstandTable programmstandTable = new DmEcvProgrammstandTable();
            programmstandTable.loadFromString(CONTAINER.getXmlString());
            for (DmEcvProgrammstand programmstand : programmstandTable.getElements()) {
                this.addElement(programmstand);
            }
        }
        CONTAINER = null;
        return elements;
    }

}
