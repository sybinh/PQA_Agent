/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
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
public class DmEcvFcTable extends DmXmlTable<DmEcvFc> {

    private Map<String, DmEcvFc> tableMap;

    @Override
    protected void initTree() {
        this.tableMap = new TreeMap<>();
        for (DmEcvFc fc : getElements()) {
            this.tableMap.put(fc.getTitle(), fc);
        }
    }

    @Override
    protected DmEcvFc createElement(EcvXmlContainerElement elem) {
        return new DmEcvFc("Dm PST Fc");
    }

    public DmEcvFc getByTitle(String title) {
        if (this.tableMap.containsKey(title)) {
            return tableMap.get(title);
        } else {
            return null;
        }
    }

}
