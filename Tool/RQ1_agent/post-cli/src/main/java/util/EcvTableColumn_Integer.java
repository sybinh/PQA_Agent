/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 *
 * @author fal83wi
 */
public class EcvTableColumn_Integer extends EcvTableColumn<Integer> {

    @Override
    public Object parse(String stringToParse) {
        return Integer.parseInt(stringToParse);
    }

    @Override
    public String getToolTipText(Object o) {
        if (o instanceof Integer) {
            return Integer.toString((int) o);
        } else {
            return null;
        }
    }

    public enum FilterMode {
        NONE, NO_BLANKS
    }

    public EcvTableColumn_Integer(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_Integer(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    @Override
    public final Class<?> getColumnClass() {
        return Integer.class;
    }

    @Override
    public Object copy(Object o) {
        if (o != null) {
            assert (o instanceof Integer);
            return o;
        } else {
            return null;
        }
    }

    @Override
    public Integer getValue(EcvTableRow row) {
        assert (row != null);

        Object returnValue = row.getValueAt(this);

        if (returnValue instanceof Integer) {
            return (Integer) returnValue;
        } else {
            return null;
        }
    }

}
