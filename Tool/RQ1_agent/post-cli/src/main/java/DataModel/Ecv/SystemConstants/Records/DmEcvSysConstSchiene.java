/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.SystemConstants.Records;

import DataModel.DmElement;
import DataModel.Ecv.SystemConstants.Fields.DmEcvField_Reference_Schiene_Programmstand_SysConst;
import DataModel.Xml.DmXmlStringField;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvSysConstSchiene extends DmElement {

    public final DmXmlStringField SCHIENEN_NAME;
    public final DmEcvField_Reference_Schiene_Programmstand_SysConst SCHIENEN_PROGRAMMSTAENDE;

    public DmEcvSysConstSchiene(String elementType, EcvXmlContainerElement container) {
        super(elementType);
        addField(SCHIENEN_NAME = new DmXmlStringField(this, "Name", "Schienen Name"));
        addField(SCHIENEN_PROGRAMMSTAENDE = new DmEcvField_Reference_Schiene_Programmstand_SysConst(this, container, "Programmstand SysConst"));
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        return this.SCHIENEN_NAME.getValueAsText();
    }

    @Override
    public String getId() {
        return this.getTitle();
    }

    public DmEcvSysConstSchiene getCopy() {
        DmEcvSysConstSchiene schiene = new DmEcvSysConstSchiene("Dm Ecv Pst Schiene", null);
        schiene.SCHIENEN_NAME.setValue(this.SCHIENEN_NAME.getValueAsText());
        for (DmEcvProgrammstand_SysConst programmstand : this.SCHIENEN_PROGRAMMSTAENDE.getElementList()) {
            schiene.SCHIENEN_PROGRAMMSTAENDE.addElement(programmstand.getCopy());
        }
        return schiene;
    }

}
