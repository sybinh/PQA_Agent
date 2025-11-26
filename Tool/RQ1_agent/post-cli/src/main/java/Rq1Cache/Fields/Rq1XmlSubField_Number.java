/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField_Number;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Number;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 * Represents a field which is stored as an part of an Rq1DatabaseField_Xml and
 * holds a text value of any content.
 *
 * @author GUG2WI
 */
public class Rq1XmlSubField_Number extends DsField_XmlSubField_Number<Rq1RecordInterface> implements Rq1FieldI_Number {

    public Rq1XmlSubField_Number(Rq1RecordInterface parent, DsField_XmlSourceI source, String elementName) {
        super(parent, source, elementName);
    }

}
