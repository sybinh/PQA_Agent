/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author gug2wi
 */
public final class EcvTableRow {

    private final EcvTableDescription description;
    private final Object[] values;

    public EcvTableRow(EcvTableDescription description) {
        assert (description != null);
        this.description = description;
        values = new Object[description.getColumnCount()];
        Arrays.fill(values, null);
    }

    public final Object[] getValues() {
        return values;
    }

    /**
     * Set the value in the specified column.
     *
     * @param columnIndex Index of the column
     * @param o New value.
     * @return True, if the new value is equal() to the old value; false
     * otherwise.
     */
    public final boolean setValueAt(int columnIndex, Object o) {
        assert (columnIndex >= 0) : columnIndex;
        assert (columnIndex < values.length) : columnIndex;
        boolean equal = Objects.equals(values[columnIndex], o);
        values[columnIndex] = o;
        return !equal;
    }

    public final Object getValueAt(int columnIndex) {
        assert (columnIndex >= 0) : columnIndex;
        assert (columnIndex < values.length) : columnIndex;
        return values[columnIndex];
    }

    public final Object getValueAt(EcvTableColumnI ipeColumn) {
        assert (ipeColumn.getColumnIndexData() >= 0) : ipeColumn.getUiName();
        assert (ipeColumn.getColumnIndexData() < values.length) : ipeColumn.getUiName();
        return getValueAt(ipeColumn.getColumnIndexData());
    }

    public final EcvTableRow copy() {
        EcvTableRow newRow = new EcvTableRow(description);
        for (EcvTableColumnI c : description.getColumns()) {
            newRow.values[c.getColumnIndexData()] = c.copy(values[c.getColumnIndexData()]);
        }
        return newRow;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        b.append("(");
        for (Object v : values) {
            if (first == true) {
                first = false;
            } else {
                b.append(",");
            }
            b.append(v != null ? v.toString() : "null");
        }
        b.append(")");
        return b.toString();
    }

}
