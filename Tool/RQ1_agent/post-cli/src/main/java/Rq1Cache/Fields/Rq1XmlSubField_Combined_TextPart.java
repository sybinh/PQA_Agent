/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_XmlSubField_Combined;
import DataStore.DsField_XmlSubField_Combined_TextPart;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 *
 * @author gug2wi
 */
public class Rq1XmlSubField_Combined_TextPart extends DsField_XmlSubField_Combined_TextPart<Rq1RecordInterface> implements Rq1FieldI_Text {

    public Rq1XmlSubField_Combined_TextPart(Rq1RecordInterface parent, DsField_XmlSubField_Combined<Rq1RecordInterface> source) {
        super(parent, source);
    }

}
