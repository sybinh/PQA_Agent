/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvTextAreaDialog;
import UiSupport.UiThreadChecker;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author hfi5wi
 */
public class EcvTextAreaColumnEditor extends AbstractCellEditor implements TableCellEditor {

    private final EcvTableColumn_StringMultiline muiltilineColumn;
    private final JTextArea textArea;
    private String multilineText;

    public EcvTextAreaColumnEditor(EcvTableColumn_StringMultiline column, Font font) {
        assert (column != null);
        this.muiltilineColumn = column;
        this.multilineText = null;
        this.textArea = new JTextArea();
        this.textArea.setBorder(null);
        this.textArea.setFont(font);
        this.textArea.setEnabled(true);
        this.textArea.setEditable(false);
        this.textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    openDialog(column.getUiName());
                }
            }
        });
    }

    EcvTextAreaColumnEditor(EcvTableColumn_StringMultiline aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void openDialog(String editObjectName) {
        UiThreadChecker.ensureDispatchThread();

        this.textArea.setBackground(Color.yellow);
        EcvTextAreaDialog textAreaDialog = new EcvTextAreaDialog();
        textAreaDialog.setTitle("Edit " + editObjectName);
        textAreaDialog.setText(multilineText);
        textAreaDialog.setVisible(true);
        if (textAreaDialog.getResult() != null) {
            setText(textAreaDialog.getResult());
        }
        this.textArea.setBackground(Color.white);
    }

    private void setText(String text) {
        this.multilineText = (text != null) ? text : "";

        if (this.multilineText.contains("\n")) {
            this.textArea.setText(this.multilineText.split("\n")[0] + " ...");
            String toolTipText = "<html>" + EcvHtmlEncode.encodeHtml(this.multilineText) + "</html>";
            this.textArea.setToolTipText(toolTipText);
        } else {
            this.textArea.setText(this.multilineText);
            if (this.multilineText.isEmpty()) {
                this.textArea.setToolTipText(null);
            } else {
                this.textArea.setToolTipText(this.multilineText);
            }
        }
    }

    @Override
    public Object getCellEditorValue() {
        return (this.multilineText);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.multilineText = (String) value;
        setText(this.multilineText);
        return this.textArea;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return !muiltilineColumn.isReadOnly();
    }
}
