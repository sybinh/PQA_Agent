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
 * @author moe83wi
 */
public class DmEcvBcResponsible extends DmElement {

    public final DmXmlStringField BC_NAME;
    public final DmXmlStringField BC_DESCRIPTION;
    public final DmXmlStringField DS_RESPONSIBLE;
    public final DmXmlStringField GS_RESPONSIBLE;

    public DmEcvBcResponsible(String elementType) {
        super(elementType);
        addField(BC_NAME = new DmXmlStringField(this, "BC", "BC Name"));
        addField(BC_DESCRIPTION = new DmXmlStringField(this, "Description", "Description"));
        addField(GS_RESPONSIBLE = new DmXmlStringField(this, "ECV_GS", "GS Verantwortlicher"));
        addField(DS_RESPONSIBLE = new DmXmlStringField(this, "ECV_DS", "DS Verantwortlicher"));
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
