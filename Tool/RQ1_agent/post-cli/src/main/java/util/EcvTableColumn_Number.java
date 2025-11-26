/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvNumberDocumentFilter;
import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 *
 * @author gug2wi
 */
public class EcvTableColumn_Number extends EcvTableColumn<String> {

    public EcvTableColumn_Number(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_Number(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    @Override
    public final Class<?> getColumnClass() {
        return String.class;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        JTextField textEditor = new JTextField();
        textEditor.setFont(font);
        Document document = textEditor.getDocument();
        if (document instanceof PlainDocument) {
            ((PlainDocument) document).setDocumentFilter(new EcvNumberDocumentFilter(getAllowEmptyValue()));
        }
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
