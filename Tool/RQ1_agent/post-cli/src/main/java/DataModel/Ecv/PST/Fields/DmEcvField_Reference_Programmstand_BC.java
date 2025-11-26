/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.PST.Fields;

import DataModel.Ecv.Fields.DmEcvField_ReferenceList;
import DataModel.Ecv.PST.Records.DmEcvBc;
import DataModel.Ecv.PST.Records.DmEcvBcTable;
import DataModel.Ecv.PST.Records.DmEcvProgrammstand;
import java.util.LinkedList;
import java.util.List;
import util.EcvProPlaToConfigClass;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvField_Reference_Programmstand_BC extends DmEcvField_ReferenceList<DmEcvBc> {

    private EcvXmlContainerElement CONTAINER;

    public DmEcvField_Reference_Programmstand_BC(DmEcvProgrammstand parent, EcvXmlContainerElement container, String nameForUserInterface) {
        super(parent, new LinkedList<DmEcvBc>(), nameForUserInterface);
        this.CONTAINER = container;
    }

    @Override
    public List<DmEcvBc> getElementList() {
        if (elements.isEmpty() && CONTAINER != null) {
            DmEcvBcTable bcTable = new DmEcvBcTable();
            bcTable.loadFromString(CONTAINER.getXmlString());
            for (DmEcvBc bc : bcTable.getElements()) {
                //Check the bc name
                if (!EcvProPlaToConfigClass.BC_NAMES_WHICH_SHOULD_NOT_BE_SENT.contains(bc.BC_NAME.getValueAsText())) {
                    this.addElement(bc);
                }

            }
        }
        CONTAINER = null;
        return elements;
    }

    public DmEcvBc getPaketByTitle(String name) {
        this.getElementList();
        for (DmEcvBc bc : elements) {
            if (bc.BC_NAME.getValueAsText().equals(name)) {
                return bc;
            }
        }
        return null;
    }

    /**
     * is used if the wrong alternative of the BC is in the release
     *
     * @param name of the BC
     */
    public void removeElement(String name) {
        DmEcvBc bcToRemove = this.getPaketByTitle(name);
        if (bcToRemove != null) {
            this.elements.remove(bcToRemove);
        }
    }
}
