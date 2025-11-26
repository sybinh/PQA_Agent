/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
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

/**
 *
 * @author moe83wi
 */
public class DmEcvProgrammstandTable extends DmXmlTable<DmEcvProgrammstand> {

    private Map<String, DmEcvProgrammstand> tableMap;

    @Override
    protected void initTree() {
        this.tableMap = new TreeMap<>();
        for (DmEcvProgrammstand programmstand : getElements()) {
            this.tableMap.put(programmstand.getTitle(), programmstand);
        }
    }

    public DmEcvProgrammstand getByTitle(String title) {
        if (this.tableMap.containsKey(title)) {
            return tableMap.get(title);
        } else {
            return null;
        }
    }

    @Override
    protected DmEcvProgrammstand createElement(EcvXmlContainerElement elem) {
        return new DmEcvProgrammstand(elem);
    }
}
