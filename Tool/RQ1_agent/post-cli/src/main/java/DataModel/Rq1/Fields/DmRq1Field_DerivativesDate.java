/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
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
import Rq1Data.Types.Rq1DerivativePlannedDates;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_DerivativesDate extends DmToDsValueField<Rq1RecordInterface, Rq1DerivativePlannedDates> {

    public DmRq1Field_DerivativesDate(Rq1FieldI<Rq1DerivativePlannedDates> rq1DateField, String nameForUserInterface) {
        super(rq1DateField, nameForUserInterface);
    }

    public DmRq1Field_DerivativesDate(DmElementI parent, Rq1FieldI<Rq1DerivativePlannedDates> rq1DateField, String nameForUserInterface) {
        super(rq1DateField, nameForUserInterface);
    }

}
