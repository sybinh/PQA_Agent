/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author gug2wi
 */
public class EcvTableColumn_ComboBox extends EcvTableColumn<String> {

    String[] allowedValues;

    public EcvTableColumn_ComboBox(String uiName, String[] allowedValues) {
        super(uiName);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);
        this.allowedValues = allowedValues;
    }

    public EcvTableColumn_ComboBox(String uiName, int columnWidth, String[] allowedValues) {
        super(uiName, columnWidth);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);
        this.allowedValues = allowedValues;
    }

    public EcvTableColumn_ComboBox(String uiName, EcvEnumeration[] allowedValues) {
        super(uiName);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);
        this.allowedValues = new String[allowedValues.length];
        for (int i = 0; i < allowedValues.length; i++) {
            this.allowedValues[i] = allowedValues[i].getText();
        }
    }

    public EcvTableColumn_ComboBox(String uiName, int columnWidth, EcvEnumeration[] allowedValues) {
        super(uiName, columnWidth);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);
        this.allowedValues = new String[allowedValues.length];
        for (int i = 0; i < allowedValues.length; i++) {
            this.allowedValues[i] = allowedValues[i].getText();
        }
    }

    public boolean setValue(EcvTableRow row, EcvEnumeration newValue) {
        assert (row != null);
        String newValueString = null;
        if (newValue != null) {
            newValueString = newValue.getText();
        }
        return (super.setValue(row, newValueString));
    }

    @Override
    public final Class<?> getColumnClass() {
        return String.class;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(font);
        for (String value : allowedValues) {
            comboBox.addItem(value);
        }
        return new DefaultCellEditor(comboBox);
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
        if (s != null) {
            for (String allowedValue : allowedValues) {
                if (s.equals(allowedValue)) {
                    return (s);
                }
            }
        }
        return (null);
    }

    @Override
    public String getToolTipText(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else {
            return null;
        }
    }

}
