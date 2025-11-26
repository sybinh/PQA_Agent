/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvDateField;
import java.awt.Component;
import java.awt.Font;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 */
public class EcvDateColumnEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    
    private final EcvTableColumn_Date dateColumn;
    private final EcvDateField dateField;

    protected EcvDateColumnEditor(EcvTableColumn_Date column, Font font) {
        assert (column != null);
        this.dateColumn = column;
        this.dateField = new EcvDateField(this,font);

    }

    @Override
    public Object getCellEditorValue() {
        return dateField.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        dateField.setDate((EcvDate) value);
        return dateField;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        dateField.setDate((EcvDate) value);
        dateField.setSelected(isSelected);
        return dateField;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return !dateColumn.isReadOnly();
    }

    @Override
    public boolean stopCellEditing() {
        dateField.closePopup();
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        dateField.closePopup();
        fireEditingCanceled();
    }
    
}
