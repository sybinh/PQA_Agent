/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import util.EcvTableData;
import util.EcvTableRow;

import java.util.*;

/**
 * A table to store the values for qmm filter criteria.
 * * @author trb83wi
 */
public class Rq1XmlTable_QmmFilterCriteria extends Rq1XmlTableAsList<String> {

    final private Rq1XmlTableColumn_String PROFIT_CENTER;

    public Rq1XmlTable_QmmFilterCriteria() {
        addXmlColumn(PROFIT_CENTER = new Rq1XmlTableColumn_String("QmmFilterCriteria", 10, "QmmFilterCriteria", ColumnEncodingMethod.CONTENT));
        PROFIT_CENTER.setOptional();
    }

    @Override
    final public Collection<String> getList(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_QmmFilterCriteria);

        SortedSet<String> result = new TreeSet<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(PROFIT_CENTER.getValue(row));
        }
        return (result);
    }
}
