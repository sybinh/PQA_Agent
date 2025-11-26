/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_Xml;
import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField_Combined;
import Rq1Cache.Records.Rq1RecordInterface;
import util.EcvXmlCombinedElement;

/**
 *
 *
 * @author GUG2WI
 */
public class Rq1XmlSubField_Combined extends DsField_XmlSubField_Combined<Rq1RecordInterface> implements Rq1FieldI<EcvXmlCombinedElement> {

    public Rq1XmlSubField_Combined(Rq1RecordInterface parent, DsField_XmlSourceI source, String elementName) {
        super(parent, source, elementName);
    }

    public Rq1XmlSubField_Combined(Rq1RecordInterface parent, DsField_XmlSourceI source, DsField_Xml.ContentMode unknownElementsAllowed, String elementName) {
        super(parent, source, unknownElementsAllowed, elementName);

    }

}
