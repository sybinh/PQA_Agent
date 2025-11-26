/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmToDsField_EnumerationSet;
import DataModel.DmValueFieldI_EnumerationSet;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_EnumerationSet;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_EnumerationSet extends DmToDsField_EnumerationSet<Rq1RecordInterface> implements DmValueFieldI_EnumerationSet {

    public DmRq1Field_EnumerationSet(Rq1FieldI_EnumerationSet rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
    }

}
