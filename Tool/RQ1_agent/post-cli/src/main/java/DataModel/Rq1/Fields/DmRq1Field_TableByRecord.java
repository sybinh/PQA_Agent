/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmToDsField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Types.Rq1XmlTable;
import DataStore.DsXmlTable_RecordAdapterI;

/**
 *
 * @author gug2wi
 * @param <T_TABLE>
 * @param <T_RECORD>
 */
public class DmRq1Field_TableByRecord<T_TABLE extends Rq1XmlTable, T_RECORD> extends DmToDsField_Table<Rq1RecordInterface, T_TABLE, T_RECORD> {

    public DmRq1Field_TableByRecord(Rq1XmlSubField_Table<T_TABLE> rq1TableField, DsXmlTable_RecordAdapterI<T_RECORD> recordAdapter, String nameForUserInterface) {
        super(rq1TableField, recordAdapter, nameForUserInterface);
    }

}
