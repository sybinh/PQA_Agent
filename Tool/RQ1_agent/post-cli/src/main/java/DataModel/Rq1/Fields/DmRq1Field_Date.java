/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmToDsField_Date;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Date;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_Date extends DmToDsField_Date<Rq1RecordInterface> {

    public DmRq1Field_Date(DmElementI parent, Rq1FieldI_Date rq1Field, String nameForUserInterface) {
        this(rq1Field, nameForUserInterface);
    }

    public DmRq1Field_Date(Rq1FieldI_Date rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
    }

}
