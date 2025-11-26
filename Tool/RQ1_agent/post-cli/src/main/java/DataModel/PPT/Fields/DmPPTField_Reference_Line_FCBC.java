/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.Ecv.PST.Records.DmEcvProgrammstand;
import DataModel.PPT.Records.DmPPTLine;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 * 
 */

//TODO

public class DmPPTField_Reference_Line_FCBC extends DmPPTField_ReferenceList<DmEcvProgrammstand> {

    private final DmPPTLine LINE;

    public DmPPTField_Reference_Line_FCBC(DmPPTLine parent,  String nameForUserInterface) {
        super(parent, new LinkedList<DmEcvProgrammstand>(), nameForUserInterface);
        this.LINE = parent;
    }

    @Override
    public List<DmEcvProgrammstand> getElementList() {
        if (element.isEmpty()) {
        }
        return element;
    }
}
