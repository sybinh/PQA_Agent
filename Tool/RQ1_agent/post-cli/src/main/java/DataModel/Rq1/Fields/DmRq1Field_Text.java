/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmToDsField_Text;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_Text extends DmToDsField_Text<Rq1RecordInterface> {

    public DmRq1Field_Text(DmElementI parent, Rq1FieldI_Text rq1TextField, String nameForUserInterface) {
        this(rq1TextField, nameForUserInterface);
    }
    
    public DmRq1Field_Text(DmElementI parent, Rq1FieldI_Text rq1TextField, String nameForUserInterface, int widthInCharacter) {
        this(rq1TextField, nameForUserInterface);
        this.setWidthInCharacter(widthInCharacter);
    }

    public DmRq1Field_Text(Rq1FieldI_Text rq1TextField, String nameForUserInterface) {
        super(rq1TextField, nameForUserInterface);
    }

}
