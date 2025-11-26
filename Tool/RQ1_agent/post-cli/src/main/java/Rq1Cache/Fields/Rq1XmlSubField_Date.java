/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField_Date;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Date;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 * Represents a field which is stored as an part of an Rq1DatabaseField_Xml and
 * holds a date value.
 *
 * @author GUG2WI
 */
public class Rq1XmlSubField_Date extends DsField_XmlSubField_Date<Rq1RecordInterface> implements Rq1FieldI_Date {

    public Rq1XmlSubField_Date(Rq1RecordInterface parent, DsField_XmlSourceI source, String elementName) {
        super(parent, source, elementName);
    }

}
