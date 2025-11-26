/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.Font;
import java.text.ParseException;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author gug2wi
 */
public class EcvTableColumn_Date extends EcvTableColumn<EcvDate> {

    public EcvTableColumn_Date(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_Date(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    @Override
    public final Class<?> getColumnClass() {
        return EcvDate.class;
    }

    @Override
    public TableCellEditor getTableCellEditor(Font font) {
        return new EcvDateColumnEditor(this, font);
    }

    @Override
    public Object copy(Object o) {
        if (o != null) {
            assert (o instanceof EcvDate) : o;
            return (o);
        } else {
            return null;
        }
    }

    @Override
    public Object parse(String s) {
        try {
            return (EcvDate.parseUiValue(s));
        } catch (ParseException ex) {
            try {
                return (EcvDate.parseXmlValue(s));
            } catch (EcvDate.DateParseException ex1) {
                try {
                    return (EcvDate.parseOslcValue(s));
                } catch (EcvDate.DateParseException ex2) {
                    return (null);
                }
            }
        }
    }

    @Override
    public String getToolTipText(Object o) {
        if (o instanceof EcvDate) {
            return ((EcvDate) o).toString();
        } else {
            return null;
        }
    }

}
