/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvDerivateField;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 */
public class EcvDerivativeColumnEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private final EcvTableColumn_Derivate column;
    private final EcvDerivateField field;

    protected EcvDerivativeColumnEditor(EcvTableColumn_Derivate column) {
        assert (column != null);
        this.column = column;
        this.field = new EcvDerivateField(column.getAllowedValues(), column.getColumnWidth());
    }

    @Override
    public Object getCellEditorValue() {
        return field.getValue();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        field.setValue((String) value, column, row);
        return field;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        field.setValue((String) value, column, row);
        field.setSelected(isSelected);
        return field;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return !column.isReadOnly();
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

    public void setAllowedValues(String[] newAllowedValues) {
        assert (newAllowedValues != null);
        if (SwingUtilities.isEventDispatchThread() == true) {
            setAllowedValues_OnDispatchThread(newAllowedValues);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                String[] myNewAllowedValues = newAllowedValues;
                @Override
                public void run() {
                    setAllowedValues_OnDispatchThread(myNewAllowedValues);
                }
            });
        }
    }

    private void setAllowedValues_OnDispatchThread(String[] newAllowedValues) {
        field.setAllowedValues(newAllowedValues);
    }

}
