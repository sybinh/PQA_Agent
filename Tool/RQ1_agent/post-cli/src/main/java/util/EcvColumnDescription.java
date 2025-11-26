/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.Font;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 */
public interface EcvColumnDescription {

    String getUiName();

    void setColumnIndex(int index);

    int getColumnIndex();

    int getColumnWidth();

    void setReadOnly();

    boolean isReadOnly();

    EcvColumnDescription setOptional();

    boolean isOptional();

    Class<?> getColumnClass();

    TableCellEditor getTableCellEditor(Font font);

    TableCellRenderer getTableCellRenderer(Font font);

    Object copy(Object o);

    /**
     * Parses the input string into a valid object.
     *
     * @param stringToParse
     * @return The object build for the string or null if the string does not
     * fit to the column type.
     */
    Object parse(String stringToParse);

    String getToolTipText(Object o);

    public void handleDoubleClick(Object cellValue);

}
