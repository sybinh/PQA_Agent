/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Monitoring.Rq1ParseFieldException;
import util.EcvTableColumn_CheckBox;
import util.EcvTableRow;

/**
 *
 * @author gug2wi
 */
public class Rq1XmlTableColumn_CheckBox extends EcvTableColumn_CheckBox implements Rq1XmlTableColumn<Boolean> {

    final String sourceName;
    final Rq1XmlTable.ColumnEncodingMethod encodingMethod;
    final String trueValue;
    final String falseValue;
    final String[] acceptedTrueValues;
    final String[] acceptedFalseValues;

    public Rq1XmlTableColumn_CheckBox(String uiName, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod, String trueValue, String falseValue, String[] acceptedTrueValues, String[] acceptedFalseValues) {
        this(uiName, 0, sourceName, encodingMethod, trueValue, falseValue, acceptedTrueValues, acceptedFalseValues);
    }

    public Rq1XmlTableColumn_CheckBox(String uiName, int columnWidth, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod, String trueValue, String falseValue, String[] acceptedTrueValues, String[] acceptedFalseValues) {
        super(uiName, columnWidth);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        assert (trueValue != null);
        assert (trueValue.isEmpty() == false);
        assert (falseValue != null);
        assert (falseValue.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
        this.acceptedTrueValues = acceptedTrueValues;
        this.acceptedFalseValues = acceptedFalseValues;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public Rq1XmlTable.ColumnEncodingMethod getEncodingMethod() {
        return encodingMethod;
    }

    public boolean convertToBoolean(String value) throws Rq1ParseFieldException {
        assert (value != null);
        //
        // Check for default true value
        //
        if (value.equals(trueValue)) {
            return true;
        }
        //
        // Check for default false value
        //
        if (value.equals(falseValue)) {
            return false;
        }
        //
        // Check for addtionally accepted true values
        //
        for (String t : acceptedTrueValues) {
            if (value.equals(t)) {
                return true;
            }
        }
        //
        // Check for additionally accepted false values
        //
        for (String f : acceptedFalseValues) {
            if (value.equals(f)) {
                return false;
            }
        }
        //
        // Handle unknown value
        //
        StringBuilder s = new StringBuilder(50);
        s.append("Unexpected value '").append(value).append("' cannot be interpreted as true or false.");
        throw new Rq1ParseFieldException(s.toString());
    }

    private String convertToString(Boolean value) {
        if (value == true) {
            return trueValue;
        } else {
            return falseValue;
        }
    }

    @Override
    public String provideValueForDatabase(EcvTableRow row) {
        assert (row != null);

        Object objectValue = row.getValueAt(this);
        if (objectValue == null) {
            return (convertToString(false));
        } else {
            assert (objectValue instanceof Boolean) : this.getClass().getCanonicalName() + " / " + objectValue.getClass().getCanonicalName();
            return (convertToString((Boolean) objectValue));
        }
    }

}
