/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
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
public class EcvTableColumn_Derivate extends EcvTableColumn<String> {

    private final String[] allowedValues;
    private EcvDerivativeColumnEditor renderer = null;
    private EcvDerivativeColumnEditor editor = null;

    public EcvTableColumn_Derivate(String uiName, String[] allowedValues) {
        super(uiName);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);
        this.allowedValues = allowedValues;
    }

    public EcvTableColumn_Derivate(String uiName, int columnWidth, String[] allowedValues) {
        super(uiName, columnWidth);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);
        this.allowedValues = allowedValues;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        editor = new EcvDerivativeColumnEditor(this);
        editor.setAllowedValues(allowedValues);
        return editor;
    }

    @Override
    public TableCellRenderer getTableCellRenderer(Font font) {
        renderer = new EcvDerivativeColumnEditor(this);
        renderer.setAllowedValues(allowedValues);
        return renderer;
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }

    @Override
    public final Class<?> getColumnClass() {
        return String.class;
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
