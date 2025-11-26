/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvEnumeration;
import util.EcvEnumerationValue;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_Enumeration extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String ORDINAL;
    final public Rq1XmlTableColumn_String ID;

    public Rq1XmlTable_Enumeration() {
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 15, "name", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(ORDINAL = new Rq1XmlTableColumn_String("Ordinal", 15, "ordinal", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(ID = new Rq1XmlTableColumn_String("Id", 15, "id", ColumnEncodingMethod.ATTRIBUTE));
        ID.setOptional();
        ORDINAL.setOptional();
    }

    /**
     * Returns a sorted set containing all values from the table as EcvEnumerations.
     * The ordinal number starts with 1.
     * @param data
     * @return 
     */
    final public SortedSet<EcvEnumeration> createEnumerationSet(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_Enumeration);

        int lastOrdinal = 0;

        SortedSet<EcvEnumeration> result = new TreeSet<>();
        for (EcvTableRow row : data.getRows()) {
            int ordinal = lastOrdinal + 1;
            Object ordinalObject = row.getValueAt(ORDINAL);
            if (ordinalObject instanceof String) {
                try {
                    ordinal = Integer.parseInt((String) ordinalObject);
                } catch (NumberFormatException e) {
                    // Do nothing. We use the default value
                }
            }
            result.add(new EcvEnumerationValue(row.getValueAt(NAME).toString(), ordinal));

            lastOrdinal = ordinal;
        }
        return (result);
    }

    /**
     * Creates a list of strings from the given data.
     *
     * @param data Data from the table containing the enumeration values.
     * @return List of strings with the names of the enumerations. Each call of
     * this method creates a new list. This list is not connected in any way to
     * the provided data.
     */
    final public List<String> createStringList(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_Enumeration);

        SortedSet<EcvEnumeration> s = createEnumerationSet(data);
        List<String> result = new ArrayList<>(s.size());
        for (EcvEnumeration e : s) {
            result.add(e.getText());
        }
        return (result);
    }

}
