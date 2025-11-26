/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_Xml;
import Rq1Cache.Records.Rq1RecordInterface;
import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField_Xml;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Xml;
import util.EcvXmlContainerElement;

/**
 * Represents a field which is stored as an part of an Rq1DatabaseField_Xml and
 * holds a text value of any content.
 *
 * @author GUG2WI
 */
public class Rq1XmlSubField_Xml extends DsField_XmlSubField_Xml<Rq1RecordInterface> implements Rq1FieldI<EcvXmlContainerElement>, Rq1FieldI_Xml {

    public Rq1XmlSubField_Xml(Rq1RecordInterface parent, DsField_XmlSourceI source, String elementName) {
        super(parent, source, elementName);
    }

    public Rq1XmlSubField_Xml(Rq1RecordInterface parent, DsField_XmlSourceI source, DsField_Xml.ContentMode unknownElementsAllowed, String elementName) {
        super(parent, source, unknownElementsAllowed, elementName);
    }

}
