/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.PST.Records;

import DataModel.DmElement;
import DataModel.Ecv.PST.Fields.DmEcvField_Reference_Programmstand_BC;
import DataModel.Ecv.Records.DmEcvBcLongNameTable;
import DataModel.Ecv.Records.DmEcvBcResponsibleTable;
import DataModel.Ecv.Records.DmEcvFcLongNameTable;
import DataModel.Xml.DmXmlStringField;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvProgrammstand extends DmElement {

    public final DmXmlStringField PROGRAMMSTAND_NAME;
    public final DmXmlStringField PROGRAMMSTAND_ABGELIEFERT;
    public final DmEcvField_Reference_Programmstand_BC PROGRAMMSTAND_PAKETE;

    public DmEcvProgrammstand(EcvXmlContainerElement container) {
        super("Dm Ecv Programmstand");
        addField(PROGRAMMSTAND_NAME = new DmXmlStringField(this, "Name", "ProPlaTo <Name> "));
        addField(PROGRAMMSTAND_ABGELIEFERT = new DmXmlStringField(this, "Abgeliefert", "ProPlaTo: <Abgeliefert> "));
        addField(PROGRAMMSTAND_PAKETE = new DmEcvField_Reference_Programmstand_BC(this, container, "Programmstand BCs"));
    }

    public final void enhanceInformation(DmEcvBcLongNameTable bcLongnames, DmEcvFcLongNameTable fcLongnames, DmEcvBcResponsibleTable bcResponsibles) {
        assert (bcLongnames != null);
        assert (fcLongnames != null);
        assert (bcResponsibles != null);
        for (DmEcvBc bc : this.PROGRAMMSTAND_PAKETE.getElementList()) {
            bc.enhanceInformation(bcLongnames, fcLongnames, bcResponsibles);
        }
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

    public DmEcvProgrammstand getCopy() {
        DmEcvProgrammstand programmstand = new DmEcvProgrammstand(null);
        programmstand.PROGRAMMSTAND_NAME.setValue(this.PROGRAMMSTAND_NAME.getValueAsText());
        programmstand.PROGRAMMSTAND_ABGELIEFERT.setValue(this.PROGRAMMSTAND_ABGELIEFERT.getValueAsText());
        for (DmEcvBc bc : this.PROGRAMMSTAND_PAKETE.getElementList()) {
            programmstand.PROGRAMMSTAND_PAKETE.addElement(bc.getCopy());
        }
        return programmstand;
    }

}
