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
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 * @param <T_VALUE> Type of value hold in the column.
 */
public interface EcvTableColumnI<T_VALUE> {

    /**
     * Defines if a field shall be visible in GUI.
     */
    public enum Visibility {

        ALWAYS_VISIBLE(false), // Column cannot be hidden. This is the default for columns.
        ALWAYS_HIDDEN(false), // Column is never visible
        DEFAULT_VISIBLE(true), // Column is visible by default but can be hidden by the user.
        DEFAULT_HIDDEN(true); // column is hidden by default but can made visible by the user.

        final private boolean isConfigurable;

        Visibility(boolean isConfigurable) {
            this.isConfigurable = isConfigurable;
        }

        public boolean isConfigurable() {
            return isConfigurable;
        }

    }

    String getUiName();

    /**
     * Id used for storing table settings. Usually, the id is the ui Name. But
     * if the name changes, then the old name has to be used as id. String
     * getId();
     */
    default String getId() {
        return (getUiName());
    }

    Visibility getVisibility();

    void setVisibility(Visibility visibility);

    void setColumnIndexData(int index);

    /**
     * Returns the column number in the table data that contains the data for
     * this columns.
     *
     * @return
     */
    int getColumnIndexData();

    int getColumnWidth();

    void setReadOnly();

    boolean isReadOnly();

    EcvTableColumnI setOptional();

    boolean isOptional();

    Class<?> getColumnClass();

    TableCellEditor getTableCellEditor(Font font);

    TableCellRenderer getTableCellRenderer(Font font);

    Object copy(Object o);

    /**
     * Set the given value in the given row.
     *
     * @param row Row in which the value shall be set.
     * @param newValue New value.
     * @return true, if the value was changed with the new value; false, if the
     * new value equals the old value.
     */
    boolean setValue(EcvTableRow row, T_VALUE newValue);

    /**
     *
     * @param row Row from which the value shall be returned.
     * @return The value of the column in the given row.
     */
    T_VALUE getValue(EcvTableRow row);

    /**
     * Parses the input string into a valid object.
     *
     * @param stringToParse
     * @return The object build for the string or null if the string does not
     * fit to the column type.
     */
    Object parse(String stringToParse);

    String getToolTipText(Object o);

    /**
     * Execute an optional action for double click on a field.
     *
     * @param cellValue
     * @return true, if the double click was handled; false if no specific
     * action is defined for the double click on the column.
     */
    public boolean handleDoubleClick(Object cellValue);

}
