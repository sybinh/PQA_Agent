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
import DataModel.DmValueFieldI_Xml;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Xml;
import Rq1Cache.Records.Rq1RecordInterface;
import util.EcvXmlContainerElement;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_Xml extends DmToDsValueField<Rq1RecordInterface, EcvXmlContainerElement> implements DmValueFieldI_Xml {

    public DmRq1Field_Xml(DmElementI parent, Rq1FieldI_Xml rq1XmlField, String nameForUserInterface) {
        this(rq1XmlField, nameForUserInterface);
    }

    public DmRq1Field_Xml(Rq1FieldI_Xml rq1XmlField, String nameForUserInterface) {
        super(rq1XmlField, nameForUserInterface);
    }

}
