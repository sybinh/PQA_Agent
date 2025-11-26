/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.Records;

import DataModel.DmElement;
import DataModel.Xml.DmXmlStringField;

/**
 *
 * @author mos83wi
 */
public class DmEcvFcLongName extends DmElement {

    public final DmXmlStringField BC_NAME;
    public final DmXmlStringField FC_NAME;
    public final DmXmlStringField FC_LONGNAME_GERMAN;
    public final DmXmlStringField FC_LONGNAME_ENGLISH;

    public DmEcvFcLongName(String elementType) {
        super(elementType);
        addField(BC_NAME = new DmXmlStringField(this, "BC", "BC Name"));
        addField(FC_NAME = new DmXmlStringField(this, "FC", "FC Name"));
        addField(FC_LONGNAME_GERMAN = new DmXmlStringField(this, "Longname_german", "FC Longname German"));
        addField(FC_LONGNAME_ENGLISH = new DmXmlStringField(this, "Longname_english", "FC Longname Eenglish"));
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
