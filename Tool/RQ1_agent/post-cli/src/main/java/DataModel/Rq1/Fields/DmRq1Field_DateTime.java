/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmToDsValueField;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1RecordInterface;
import util.EcvDateTime;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_DateTime extends DmToDsValueField<Rq1RecordInterface, EcvDateTime> {

    public DmRq1Field_DateTime(Rq1FieldI<EcvDateTime> rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
    }

    public DmRq1Field_DateTime(DmElementI parent, Rq1FieldI<EcvDateTime> rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
    }
}
