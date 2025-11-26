/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.SystemConstants.Records;

import DataModel.Xml.DmXmlTable;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvSysConstSchieneTable extends DmXmlTable<DmEcvSysConstSchiene> {

    public void loadFromEcvXmlContainerElement(EcvXmlContainerElement xmlListe) throws Exception {
        try {
            EcvXmlContainerElement lieferant = (EcvXmlContainerElement) xmlListe.getElement("Lieferant");
            super.loadFromString(lieferant.getXmlString());
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (ex);
        }
    }

    @Override
    protected DmEcvSysConstSchiene createElement(EcvXmlContainerElement elem) {
        return new DmEcvSysConstSchiene("Dm Ecv SysConst Schiene", elem);
    }

}
