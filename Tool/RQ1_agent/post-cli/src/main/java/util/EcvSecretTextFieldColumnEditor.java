/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvSecretTextField;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author hfi5wi
 */
public class EcvSecretTextFieldColumnEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    
    private final EcvTableColumn_SecretString column;
    private final List<EcvSecretTextField> textFields;
    
    public EcvSecretTextFieldColumnEditor(EcvTableColumn_SecretString column) {
        assert (column != null);
        this.column = column;
        this.textFields = new ArrayList<>();
    }
    
    public void handleMousePressed(MouseEvent e, JTable table, int rowIndex) {
        JButton imageButton = this.textFields.get(rowIndex).getShowKeyButton();
        Point leftTopPoint = imageButton.getLocation();
        int mousePointX = e.getX();

        if(leftTopPoint.getX() <= mousePointX && mousePointX <= (leftTopPoint.getX() + imageButton.getWidth())) {
            showDecodedText(rowIndex);
        }
    }
    
    public void showDecodedText(int row) {
        EcvSecretTextField textField = this.textFields.get(row);
        String encodedText = textField.getEncodedText();
        String decodedText = this.column.decodeString(encodedText);
        if (decodedText != null) {
            textField.setTemporaryText(decodedText);
        }
    }
    
    public void handleMouseReleased(MouseEvent e, JTable table, int rowIndex) {
        EcvSecretTextField textField = this.textFields.get(rowIndex);
        if (textField.getText() != textField.getEncodedText()) {
            textField.showEncodedText();
            table.clearSelection(); // Force table to call getTableCellRendererComponent to update changed text
        }
    }
    
    private JPanel getComponent(Object value,int row) {
        if(textFields.size() < row+1) {
            EcvSecretTextField textField = new EcvSecretTextField(this, row);
            textField.setEncodedText((String) value);
            textFields.add(row, textField);
            return (textField);
        } else {
            return (textFields.get(row));
        }
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return (getComponent(value, row));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return (getComponent(value, row));
    }
    
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return !column.isReadOnly();
    }
    
    public boolean isSecretDecodeable() {
        return (this.column.isSecretDecodeable());
    }
    
    public void isSecretDecodeable(boolean isSecretDecodeable) {
        for(EcvSecretTextField textField: textFields) {
            textField.isShowKeyButtonVisible(isSecretDecodeable);
        }
    }
    
    @Override
    public Object getCellEditorValue() {
        return ("");
    }
    
}
