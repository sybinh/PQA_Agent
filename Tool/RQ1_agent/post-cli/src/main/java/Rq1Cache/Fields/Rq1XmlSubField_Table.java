/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField_Table;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Types.Rq1XmlTable;
import util.EcvTableData;

/**
 * Represents a field which is stored as an part of an Rq1DatabaseField_Xml and
 * holds a table of values.
 * <br>
 * The data of the table is stored in XML elements of the same name. Each
 * element represents one line.
 *
 * @author GUG2WI
 * @param <T_TABLE>
 */
public class Rq1XmlSubField_Table<T_TABLE extends Rq1XmlTable> extends DsField_XmlSubField_Table<Rq1RecordInterface, T_TABLE> implements Rq1FieldI<EcvTableData> {

    public Rq1XmlSubField_Table(Rq1RecordInterface parent, T_TABLE table, DsField_XmlSourceI source) {
        super(parent, table, source);
    }

    public Rq1XmlSubField_Table(Rq1RecordInterface parent, T_TABLE table, DsField_XmlSourceI source, String elementName) {
        super(parent, table, source, elementName);
    }

}
