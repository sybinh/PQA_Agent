/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.Font;
import java.util.Objects;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 * @param <T_VALUE> Type of value hold in the column.
 */
public abstract class EcvTableColumn<T_VALUE> implements EcvTableColumnI<T_VALUE> {

    private final String uiName;
    private Visibility visibility = Visibility.ALWAYS_VISIBLE;
    private final int columnWidth;
    private int columnIndexData = -1;
    private boolean isReadOnly = false;
    private boolean optional = false;
    private boolean allowEmptyField = true;

    public EcvTableColumn(String uiName) {
        assert (uiName != null);
        assert (uiName.isEmpty() == false);
        this.uiName = uiName;
        this.columnWidth = uiName.length();
    }

    public EcvTableColumn(String uiName, int columnWidth) {
        assert (uiName != null);
        assert (uiName.isEmpty() == false);
        assert (columnWidth > 0);
        this.uiName = uiName;
        this.columnWidth = columnWidth;
    }

    @Override
    public final void setVisibility(Visibility visibility) {
        assert (visibility != null);
        this.visibility = visibility;
    }

    @Override
    public final Visibility getVisibility() {
        return (visibility);
    }

    @Override
    public boolean setValue(EcvTableRow row, T_VALUE newValue) {
        assert (row != null);
        if (Objects.equals(row.getValueAt(columnIndexData), newValue) == false) {
            row.setValueAt(columnIndexData, newValue);
            return (true);
        } else {
            return (false);
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public T_VALUE getValue(EcvTableRow row) {
        assert (row != null);
        return (T_VALUE) row.getValueAt(this);
    }

    @Override
    public final void setColumnIndexData(int index) {
        assert (index >= 0);
        assert (columnIndexData == -1);
        columnIndexData = index;
    }

    @Override
    public final void setReadOnly() {
        isReadOnly = true;
    }

    @Override
    public final boolean isReadOnly() {
        return isReadOnly;
    }

    @Override
    public final EcvTableColumnI setOptional() {
        optional = true;
        return (this);
    }

    @Override
    public final boolean isOptional() {
        return optional;
    }

    public final void setAllowEmptyField(boolean allowEmptyField) {
        this.allowEmptyField = allowEmptyField;
    }

    public final boolean getAllowEmptyValue() {
        return this.allowEmptyField;
    }

    @Override
    public final String getUiName() {
        return uiName;
    }

    @Override
    public final int getColumnWidth() {
        return columnWidth;
    }

    @Override
    public final int getColumnIndexData() {
        return columnIndexData;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        return null;
    }

    @Override
    public TableCellRenderer getTableCellRenderer(Font font) {
        return null;
    }

    @Override
    public boolean handleDoubleClick(Object cellValue) {
        return (false);
    }

    @Override
    @SuppressWarnings(value = "EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return o == (Object) this;
    }

}
