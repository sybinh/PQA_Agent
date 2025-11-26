/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvSecretTextField;
import java.awt.Font;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author hfi5wi
 */
public class EcvTableColumn_SecretString extends EcvTableColumn<String> {
    
    public enum SecretKeys {
        SUPER_OPL_KEY("");
        private String key;
        
        private SecretKeys(String key) {
            assert (key != null);
            this.key = key;
        }
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
    
    private final EcvSecretTextFieldColumnEditor editor;
    private boolean isSecretDecodeable = false;
    
    public EcvTableColumn_SecretString(String uiName) {
        super(uiName);
        this.editor = new EcvSecretTextFieldColumnEditor(this);
    }
    
    public EcvTableColumn_SecretString(String uiName, int columnWidth) {
        super(uiName, columnWidth);
        this.editor = new EcvSecretTextFieldColumnEditor(this);
    }
    
    public void handleMousePressed(MouseEvent e, JTable table, int rowIndex) {
        this.editor.handleMousePressed(e, table, rowIndex);
    }
    
    public void handleMouseReleased(MouseEvent e, JTable table, int rowIndex) {
        this.editor.handleMouseReleased(e, table, rowIndex);
    }
    
    public boolean isSecretDecodeable() {
        return (this.isSecretDecodeable);
    }
    
    public void isSecretDecodeable(boolean isSecretDecodeable) {
        this.isSecretDecodeable = isSecretDecodeable;
        this.editor.isSecretDecodeable(isSecretDecodeable);
    }
    
    public String getKey() {
        return "";
    }

    public String encodeString(String string) {
        return string;
    }
    
    public String decodeString(String encodedString) {
        return encodedString;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        return (this.editor);
    }

    @Override
    public TableCellRenderer getTableCellRenderer(Font font) {
        return (this.editor);
    }
    
    @Override
    public Class<?> getColumnClass() {
        return (EcvSecretTextField.class);
    }
    
    @Override
    public Object copy(Object o) {
        return null;
    }
    
    @Override
    public String getToolTipText(Object o) {
        return null;
    }
    
    @Override
    public Object parse(String s) {
        EcvSecretTextField textField = new EcvSecretTextField(this.editor, 0);
        textField.setEncodedText(s);
        return (textField);
    }

    public String getValueNotNull(EcvTableRow row) {
        assert (row != null);

        String value = (String) row.getValueAt(this);
        if (value != null) {
            return (value);
        } else {
            return ("");
        }
    }
}
