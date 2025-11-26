/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.PST.Records;

import DataModel.Xml.DmXmlTable;
import java.util.Map;
import java.util.TreeMap;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvPSTSchieneTable extends DmXmlTable<DmEcvPSTSchiene> {

    private Map<String, DmEcvPSTSchiene> tableMap;

    public void loadFromEcvXmlContainerElement(EcvXmlContainerElement xmlListe) {
        try {
            EcvXmlContainerElement lieferant = (EcvXmlContainerElement) xmlListe.getElement("Lieferant");
            super.loadFromString(lieferant.getXmlString());
        } catch (EcvXmlElement.NotfoundException ex) {
            throw new Error("Load Failed " + ex.toString());
        }
    }

    @Override
    protected DmEcvPSTSchiene createElement(EcvXmlContainerElement elem) {
        return new DmEcvPSTSchiene("Dm Ecv Pst Schiene", elem);
    }

    @Override
    protected void initTree() {
        this.tableMap = new TreeMap<>();
        for (DmEcvPSTSchiene schiene : getElements()) {
            this.tableMap.put(schiene.getTitle(), schiene);
        }
    }

    public DmEcvPSTSchiene getByTitle(String title) {
        if (this.tableMap.containsKey(title)) {
            return tableMap.get(title);
        } else {
            return null;
        }
    }

}
