/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/**
 *
 * @author GUG2WI
 */
public class EcvTableDescription {

    public EcvTableData createTableData() {
        initDone = true;
        return (new EcvTableData(this));
    }
    //
    private boolean initDone;
    final private List<EcvTableColumnI> columns;
    private int maxRowCount;
    private boolean createAutoSorter = true;
    private EcvTableColumn defaultSortColumn;
    private boolean defaultSortOrderAscending;

    protected EcvTableDescription() {
        initDone = false;
        columns = new ArrayList<>(5);
        maxRowCount = 100000;
        defaultSortColumn = null;
        defaultSortOrderAscending = true;
    }

    protected void addIpeColumn(EcvTableColumnI newColumn) {
        assert (initDone == false);
        assert (newColumn != null);
        newColumn.setColumnIndexData(columns.size());
        columns.add(newColumn);
    }

    protected void setCreateAutoSorter(boolean createAutoSorter) {
        assert (createAutoSorter == true ? true : defaultSortColumn == null);

        this.createAutoSorter = createAutoSorter;
    }

    public boolean getCreateAutoSorter() {
        return createAutoSorter;
    }

    protected void setDefaultSortColumn(EcvTableColumn sortColumn, boolean sortOrderAscending) {
        assert (initDone == false);
        assert (sortColumn != null);
        assert (columns.contains(sortColumn) == true);
        assert (createAutoSorter == true);

        this.defaultSortColumn = sortColumn;
        this.defaultSortOrderAscending = sortOrderAscending;
    }

    public boolean isDefaultSortColumnSet() {
        initDone = true;
        return (defaultSortColumn != null);
    }

    public int getDefaultSortColumnIndex() {
        initDone = true;
        if (defaultSortColumn != null) {
            return (defaultSortColumn.getColumnIndexData());
        } else {
            return (-1);
        }
    }

    public boolean isDefaultSortOrderAscending() {
        initDone = true;
        return (defaultSortOrderAscending);
    }

    final public List<EcvTableColumnI> getColumns() {
        initDone = true;
        return (columns);
    }

    final public EcvTableColumnI getColumn(int columnIndexData) {
        if ((columnIndexData < columns.size()) && (columnIndexData >= 0)) {
            return (columns.get(columnIndexData));
        } else {
            return (null);
        }
    }

    final public int getColumnCount() {
        initDone = true;
        return (columns.size());
    }

    final public int getMaxRowCount() {
        initDone = true;
        return (maxRowCount);
    }

    public void handleMousePressed(MouseEvent e, JTable table, int rowIndex, int columnIndexData) {
        EcvTableColumnI column = getColumn(columnIndexData);
        if (column != null && column instanceof EcvTableColumn_SecretString) {
            ((EcvTableColumn_SecretString) column).handleMousePressed(e, table, rowIndex);
        }
    }

    public void handleMouseReleased(MouseEvent e, JTable table, int rowIndex, int columnIndexData) {
        EcvTableColumnI column = getColumn(columnIndexData);
        if (column != null && column instanceof EcvTableColumn_SecretString) {
            ((EcvTableColumn_SecretString) column).handleMouseReleased(e, table, rowIndex);
        }
    }

    /**
     * Execute an optional action for double click on a field.
     *
     * @return true, if the double click was handled; false if no specific
     * action is defined for the double click on the table.
     */
    public boolean handleDoubleClick(EcvTableData data, int rowIndex, int columnIndexData) {
        if (data != null) {
            Object cellValue = data.getValueAt(rowIndex, columnIndexData);
            EcvTableColumnI column = getColumn(columnIndexData);
            if (column != null) {
                return (column.handleDoubleClick(cellValue));
            }
        }
        return (false);
    }

    /**
     * Checks whether or not the content of the row is valid. Invalid rows will
     * be removed when the editing of the table is done.
     *
     * @param row The row that shall be checked.
     * @return true, if the row is valid
     */
    public boolean isRowValid(EcvTableRow row) {
        return (true);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EcvTableDescription) {
            if (o.getClass().equals(this.getClass())) {
                return (true);
            }
        }
        return (false);
    }

}
