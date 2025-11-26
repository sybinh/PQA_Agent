/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author gug2wi
 */
public class EcvTableData {

    //
    private final EcvTableDescription description;
    private final List<EcvTableRow> rows;

    //
    // Protected to prevent creation outside of EcvTableDescription
    //
    protected EcvTableData(EcvTableDescription description) {
        assert (description != null);
        this.description = description;
        rows = new ArrayList<>(1);
    }

    public final EcvTableDescription getDescription() {
        return (description);
    }

    /**
     * Creates a new row that can be used for this table data.
     *
     * @return A new row. The row is not connected to an table data.
     */
    public final EcvTableRow createRow() {
        return new EcvTableRow(description);
    }

    /**
     * Creates a new row and adds if to the end of the table data.
     *
     * @return A new row connected to the table data.
     */
    public final EcvTableRow createAndAddRow() {
        EcvTableRow newRow = new EcvTableRow(description);
        rows.add(newRow);
        return (newRow);
    }

    public final void clearRows() {
        rows.clear();
    }

    /**
     * Removes the given row from the rows in the data.
     *
     * @param row Row that shall be removed.
     */
    public void removeRow(EcvTableRow row) {
        for (Iterator<EcvTableRow> it = rows.iterator(); it.hasNext();) {
            EcvTableRow r = it.next();
            if (r == row) {
                it.remove();
            }
        }
    }

    /**
     * Removes the given rows from the rows in the data.
     *
     * @param rows Rows that shall be removed.
     */
    public void removeRows(Collection<EcvTableRow> rows) {
        if (rows != null) {
            for (EcvTableRow row : rows) {
                removeRow(row);
            }
        }
    }

    /**
     * Adds the given row at the end of the table data.
     *
     * @param newRow Row that shall be added.
     */
    public final void addRow(EcvTableRow newRow) {
        assert (newRow != null);
        rows.add(newRow);
    }

    public final List<EcvTableRow> getRows() {
        return (rows);
    }

    public final int getRowCount() {
        return (rows.size());
    }

    public final int getColumnCount() {
        return (description.getColumnCount());
    }

    /**
     * Method for testing.
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public final Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getValues()[columnIndex];
    }

    public final Object getValueAt(int rowIndex, EcvTableColumn column) {
        return rows.get(rowIndex).getValues()[column.getColumnIndexData()];
    }

    @Override
    public final boolean equals(Object o) {
        if ((o != null) && (o instanceof EcvTableData)) {
            EcvTableData otherData = (EcvTableData) o;
            if ((getRowCount() != otherData.getRowCount()) || (getColumnCount() != otherData.getColumnCount())) {
                return false;
            }
            for (int r = 0; r < rows.size(); r++) {
                for (int c = 0; c < description.getColumnCount(); c++) {
                    Object myObj = rows.get(r).getValueAt(c);
                    Object otherObj = otherData.rows.get(r).getValueAt(c);
                    if (myObj != null) {
                        if (myObj.equals(otherObj) == false) {
                            return false;
                        }
                    } else if (otherObj != null) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public final EcvTableData copy() {
        EcvTableData newData = new EcvTableData(description);
        for (EcvTableRow r : rows) {
            newData.addRow(r.copy());
        }
        return (newData);
    }

    /**
     * Returns the first row that matches the given value on the given column.
     * This method was introduced to simplify automatic tests.
     *
     * @param columnIndex Index of the column to check.
     * @param value
     * @return
     */
    public EcvTableRow findFirst(int columnIndex, Object value) {
        assert (columnIndex >= 0) : "Invalid columnIndex " + columnIndex;
        assert (columnIndex < getColumnCount()) : "Invalid columnIndex " + columnIndex;

        for (EcvTableRow row : rows) {
            if (Objects.equals(row.getValueAt(columnIndex), value) == true) {
                return (row);
            }
        }
        return (null);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(rows);
        return (hash);
    }

    public final boolean isEmpty() {
        return (rows.isEmpty());
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (EcvTableRow row : rows) {
            if (first == true) {

                first = false;
            } else {
                b.append("\n"); 
            }
            b.append(row.toString());
        }
        return (b.toString());

    }

}
