/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.Font;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gug2wi
 */
public class EcvTableColumn_Set extends EcvTableColumn<SortedSet<String>> {

    private final SortedSet<String> allowedValues;
    private EcvSetColumnEditor renderer = null;
    private EcvSetColumnEditor editor = null;

    public EcvTableColumn_Set(String uiName) {
        super(uiName);
        allowedValues = new TreeSet<>();
    }

    public EcvTableColumn_Set(String uiName, int columnWidth) {
        super(uiName, columnWidth);
        allowedValues = new TreeSet<>();
    }

    @Override
    public final Class<?> getColumnClass() {
        return SortedSet.class;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        editor = new EcvSetColumnEditor(this);
        editor.setAllowedValues(allowedValues);
        return editor;
    }

    @Override
    public TableCellRenderer getTableCellRenderer(Font font) {
        renderer = new EcvSetColumnEditor(this);
        renderer.setAllowedValues(allowedValues);
        return renderer;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Object copy(Object o) {
        if (o != null) {
            assert (o instanceof SortedSet) : o;
            TreeSet<String> newSet = new TreeSet<>();
            newSet.addAll((SortedSet) o);
            return newSet;
        } else {
            return null;
        }
    }

    @Override
    public Object parse(String stringToParse) {

        if ((stringToParse == null) || (stringToParse.trim().isEmpty() == true)) {
            return (null);
        }

        SortedSet<String> result = new TreeSet<>();
        String[] values = stringToParse.replace('[', ',').replace(']', ',').split(",");
        for (String value : values) {
            String trimmedValue = value.trim();
            if (allowedValues.contains(trimmedValue)) {
                result.add(trimmedValue);
            }
        }
        return (result);
    }

    public void setAllowedValues(SortedSet<String> newAllowedValues) {
        assert (newAllowedValues != null);
        allowedValues.clear();
        allowedValues.addAll(newAllowedValues);
        if (renderer != null) {
            renderer.setAllowedValues(allowedValues);
        }
        if (editor != null) {
            editor.setAllowedValues(allowedValues);
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public String getToolTipText(Object o) {
        Set<Object> value = java.util.Collections.<Object>emptySet();
        if (o instanceof Set) {
            value = (Set) o;
        }
        boolean first = true;
        StringBuilder b = new StringBuilder();
        b.append("<html>");
        for (String a : allowedValues) {
            if (first == false) {
                b.append("<br>");
            }
            first = false;
            if (value.contains(a) == true) {
                b.append(a);
            } else {
                b.append("<font color=silver>").append(a).append("</font>");
            }
        }
        for (Object v : value) {
            if (allowedValues.contains(v) == false) {
                if (first == false) {
                    b.append("<br>");
                }
                first = false;
                b.append("<font color=red>").append(v).append("</font>");
            }
        }
        return b.toString();
    }

    public SortedSet<String> getValueNonNull(EcvTableRow row) {
        SortedSet<String> s = getValue(row);
        if (s != null) {
            return (s);
        } else {
            return (new TreeSet<>());
        }
    }

}
