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
public class EcvTableColumn_CheckBox extends EcvTableColumn<Boolean> {

    public EcvTableColumn_CheckBox(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_CheckBox(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    @Override
    public final Class<?> getColumnClass() {
        return Boolean.class;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        return (new EcvCheckBoxColumnEditor(this));
    }

    @Override
    public TableCellRenderer getTableCellRenderer(Font font) {
        return (new EcvCheckBoxColumnEditor(this));
    }

    @Override
    public Object copy(Object o) {
        if (o != null) {
            assert (o instanceof Boolean);
            boolean b = (Boolean) o;
            return b;
        } else {
            return null;
        }
    }

    @Override
    public String getToolTipText(Object o) {
        if (o instanceof Boolean) {
            return (Boolean) o ? "true" : "false";
        } else {
            return null;
        }
    }

    @Override
    public Object parse(String s) {
        if (s != null) {
            switch (s.toLowerCase()) {
                case "true":
                case "yes":
                case "ja":
                case "1":
                    return (true);
            }
        }
        return (false);
    }

}
