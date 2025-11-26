/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Rq1Cache.Records.Rq1RecordInterface;
import util.EcvEnumeration;
import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField_Enumeration;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Enumeration;

/**
 * Represents a field which is stored as an part of an Rq1DatabaseField_Xml and
 * holds a text value of any content.
 *
 * @author GUG2WI
 */
final public class Rq1XmlSubField_Enumeration extends DsField_XmlSubField_Enumeration<Rq1RecordInterface> implements Rq1FieldI_Enumeration {

    public <T extends Enum<T> & EcvEnumeration> Rq1XmlSubField_Enumeration(Rq1RecordInterface parent, DsField_XmlSourceI source, String elementName, T[] validValues, T initValue) {
        super(parent, source, elementName, validValues, initValue);
    }

}
