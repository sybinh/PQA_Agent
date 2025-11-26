/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvSetField;
import java.awt.Component;
import java.util.EventObject;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 */
public class EcvSetColumnEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private final EcvTableColumn_Set setColumn;
    private final EcvSetField setField;

    protected EcvSetColumnEditor(EcvTableColumn_Set column) {
        assert (column != null);
        this.setColumn = column;
        this.setField = new EcvSetField(this);
    }

    @Override
    public Object getCellEditorValue() {
        return setField.getValue();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        setField.setValue((SortedSet<String>) value);
        return setField;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setField.setValue((SortedSet<String>) value);
        setField.setSelected(isSelected);
        return setField;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return !setColumn.isReadOnly();
    }

    @Override
    public boolean stopCellEditing() {
        setField.closePopup();
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        setField.closePopup();
        fireEditingCanceled();
    }

    public void setAllowedValues(SortedSet<String> newAllowedValues) {
        assert (newAllowedValues != null);
        if (SwingUtilities.isEventDispatchThread() == true) {
            setAllowedValues_OnDispatchThread(newAllowedValues);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                SortedSet<String> myNewAllowedValues = new TreeSet<>(newAllowedValues);
                @Override
                public void run() {
                    setAllowedValues_OnDispatchThread(myNewAllowedValues);
                }
            });
        }
    }

    private void setAllowedValues_OnDispatchThread(SortedSet<String> newAllowedValues) {
        setField.setAllowedValues(newAllowedValues);
    }

}
