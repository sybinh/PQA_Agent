/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.Font;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author hfi5wi
 */
public class EcvTableColumn_StringMultiline extends EcvTableColumn_String {

    static final private int MAX_TOOL_TIP_LINES = 40;
    static final private int MAX_TOOL_TIP_COLUMNS = 250;

    public EcvTableColumn_StringMultiline(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_StringMultiline(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        return new EcvTextAreaColumnEditor(this, font);
    }

    @Override
    public String getToolTipText(Object columnContent) {
        if (columnContent instanceof String) {
            String columnContentString = (String) columnContent;
            String truncatedColumnContent = EcvStringUtil.truncateArea(columnContentString, MAX_TOOL_TIP_COLUMNS, MAX_TOOL_TIP_LINES);
            if (truncatedColumnContent.contains("\n")) {
                return ("<html>" + EcvHtmlEncode.encodeHtml(truncatedColumnContent) + "</html>");
            } else {
                if (truncatedColumnContent.isEmpty()) {
                    return null;
                } else {
                    return truncatedColumnContent;
                }
            }
        } else {
            return null;
        }
    }
}
