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
 * @author gug2wi
 */
public class EcvTableColumn_DateTime extends EcvTableColumn<EcvDateTime> {

    public EcvTableColumn_DateTime(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_DateTime(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    @Override
    public final Class<?> getColumnClass() {
        return EcvDateTime.class;
    }

    @Override
    public Object copy(Object o) {
        if (o != null) {
            assert (o instanceof EcvDateTime);
            return ((EcvDateTime) o).copy();
        } else {
            return null;
        }
    }

    @Override
    public Object parse(String s) {
        if (s != null) {
            EcvDateTime result = new EcvDateTime();
            try {
                result.setRq1Value(s);
            } catch (EcvDateTime.DateTimeParseException ex) {
            }
            return (result);
        }
        return (null);
    }

    @Override
    public String getToolTipText(Object o) {
        if (o instanceof EcvDateTime) {
            return ((EcvDateTime) o).getUiValue();
        } else {
            return null;
        }
    }

}
