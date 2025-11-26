/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvCheckBoxField;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 */
public class EcvCheckBoxColumnEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private final EcvTableColumn_CheckBox checkBoxColumn;
    private final EcvCheckBoxField checkBoxField;

    protected EcvCheckBoxColumnEditor(EcvTableColumn_CheckBox column) {
        assert (column != null);
        this.checkBoxColumn = column;
        this.checkBoxField = new EcvCheckBoxField(this);
    }

    @Override
    public Object getCellEditorValue() {
        return checkBoxField.getValue();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Boolean) {
            checkBoxField.setValue((boolean) value);
        } else {
            checkBoxField.setValue(false);
        }
        return checkBoxField;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Boolean) {
            checkBoxField.setValue((boolean) value);
        } else {
            checkBoxField.setValue(false);
        }
        checkBoxField.setSelected(isSelected);
        return checkBoxField;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return !checkBoxColumn.isReadOnly();
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

}
