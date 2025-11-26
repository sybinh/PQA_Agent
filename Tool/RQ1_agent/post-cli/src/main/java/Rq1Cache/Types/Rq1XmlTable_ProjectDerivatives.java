/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * Implements the table of derivatives in the TAG field of software projects.
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_ProjectDerivatives extends Rq1XmlTableAsList<String> {

    final public Rq1XmlTableColumn_String NAME;

    public Rq1XmlTable_ProjectDerivatives() {
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 15, "name", ColumnEncodingMethod.ATTRIBUTE));
    }

    /**
     * Returns the derivative names in an sorted collection.
     *
     * @param data Data from the table.
     * @return A sorted collection of the derivative names.
     */
    @Override
    final public Collection<String> getList(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_ProjectDerivatives);

        SortedSet<String> result = new TreeSet<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(NAME.getValue(row));
        }
        return (result);
    }

}
