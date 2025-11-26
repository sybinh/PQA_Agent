/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.UiInsertTimeStampAction;
import UiSupport.UiInsertToDoAction;
import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author gug2wi
 */
public class EcvTableColumn_String extends EcvTableColumn<String> {

    public enum FilterMode {
        NONE, NO_BLANKS
    }
    private FilterMode filterMode = FilterMode.NONE;

    public EcvTableColumn_String(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_String(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    public EcvTableColumn_String(String uiName, FilterMode filter) {
        super(uiName);
        filterMode = filter;
    }

    @Override
    public final Class<?> getColumnClass() {
        return String.class;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        JTextField textEditor = new JTextField();
        textEditor.setBorder(null);
        switch (filterMode) {
            case NO_BLANKS:
                textEditor.setDocument(new PlainDocument() {
                    @Override
                    public void insertString(int offs, String str, AttributeSet attr) throws BadLocationException {
                        String newstr = str.replaceAll(" ", "");
                        super.insertString(offs, newstr, attr);
                    }

                    @Override
                    public void replace(int offs, int len, String str, AttributeSet attr) throws BadLocationException {
                        String newstr = str.replaceAll(" ", "");
                        super.replace(offs, len, newstr, attr);
                    }
                });
                break;
            default:
                break;
        }
        new UiInsertTimeStampAction().connect(textEditor);
        new UiInsertToDoAction().connect(textEditor);
        return new DefaultCellEditor(textEditor);
    }

    @Override
    public Object copy(Object o) {
        if (o != null) {
            assert (o instanceof String);
            return o;
        } else {
            return null;
        }
    }

    @Override
    public Object parse(String s) {
        return (s);
    }

    @Override
    public String getToolTipText(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else {
            return null;
        }
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
