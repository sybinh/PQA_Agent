/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.SystemConstants.Records;

import DataModel.DmElement;
import DataModel.Ecv.SystemConstants.Fields.DmEcvField_Reference_Programmstand_SysConst;
import DataModel.Xml.DmXmlStringField;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvProgrammstand_SysConst extends DmElement {

    public final DmXmlStringField PROGRAMMSTAND_NAME;
    public final DmEcvField_Reference_Programmstand_SysConst PROGRAMMSTAND_SYSTEMKONSTANTEN;

    public DmEcvProgrammstand_SysConst(String elementType, EcvXmlContainerElement container) {
        super(elementType);
        addField(PROGRAMMSTAND_NAME = new DmXmlStringField(this, "Name", "ProPlaTo <Name> "));
        PROGRAMMSTAND_SYSTEMKONSTANTEN = new DmEcvField_Reference_Programmstand_SysConst(this, container, "Programmstand SystemConstants");
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        return this.PROGRAMMSTAND_NAME.getValueAsText();
    }

    @Override
    public String getId() {
        return this.getTitle();
    }

    public DmEcvProgrammstand_SysConst getCopy() {
        DmEcvProgrammstand_SysConst programmstand = new DmEcvProgrammstand_SysConst("Dm Ecv Programmstand SysConst", null);
        programmstand.PROGRAMMSTAND_NAME.setValue(this.PROGRAMMSTAND_NAME.getValueAsText());
        for (DmEcvSysConst sys : this.PROGRAMMSTAND_SYSTEMKONSTANTEN.getElementList()) {
            programmstand.PROGRAMMSTAND_SYSTEMKONSTANTEN.addElement(new DmEcvSysConst(sys));
        }
        return programmstand;
    }
}
