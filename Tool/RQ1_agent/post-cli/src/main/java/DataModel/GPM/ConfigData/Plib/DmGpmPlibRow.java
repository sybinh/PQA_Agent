/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import TablePlus.Csv.CsvLoadException;
import TablePlus.Csv.CsvRow;
import ToolUsageLogger.ToolUsageLogger;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public abstract class DmGpmPlibRow<T_ID> extends CsvRow {

    final private static Logger LOGGER = Logger.getLogger(DmGpmPlibRow.class.getCanonicalName());

    DmGpmPlibRow(String[] rowValues, int rowNumber, int expectedValueLength) throws CsvLoadException {
        super(rowValues);

        if (rowValues.length != expectedValueLength) {
            String errorText = this.getClass().getSimpleName() + ": Wrong number of columns in row " + rowNumber + ". Column Number is " + rowValues.length + ". Expected are " + expectedValueLength + " columns.";
            throw new CsvLoadException(errorText);
        }

        for (int i = 0; i < expectedValueLength; i++) {
            trimColumn(i);
        }
    }

    public abstract T_ID getId();

    private void checkColumnNumber(int columnNumber) {
        if (columnNumber >= rowValues.length || columnNumber < 0) {
            throw new Error("Invalid column number " + columnNumber);
        }
    }

    final protected String getStringValueForColumn(int columnNumber) {
        checkColumnNumber(columnNumber);

        return (rowValues[columnNumber]);
    }

    protected int getIntValueForColumn(int columnNumber) {
        checkColumnNumber(columnNumber);

        int value = Integer.parseInt(rowValues[columnNumber]);
        return (value);
    }

    protected String getLinkValueForColumn(int columnNumber) {
        checkColumnNumber(columnNumber);

        String value = rowValues[columnNumber];
        if ((value.isEmpty() == false) && (value.charAt(0) == '#')) {
            value = value.substring(1);
        }
        if ((value.isEmpty() == false) && (value.charAt(value.length() - 1) == '#')) {
            value = value.substring(0, value.length() - 1);
        }
        return (value);
    }

    protected boolean getBooleanValueForColumn(int columnNumber) {
        checkColumnNumber(columnNumber);

        String value = rowValues[columnNumber];
        switch (value) {
            case "FALSCH":
                return (false);
            case "WAHR":
                return (true);
            default:
                throw new Error("Unexpected value '" + value + "' for column " + columnNumber);
        }
    }

    protected Set<String> getStringValuesForColumn(int columnNumber) {
        checkColumnNumber(columnNumber);

        Set<String> result = new TreeSet<>();
        String rowValue = rowValues[columnNumber];
        String[] splittedValue = rowValue.split(";");
        for (String v : splittedValue) {
            result.add(v);
        }
        return (result);
    }

    protected Set<Integer> getIntValuesForColumn(int columnNumber) {
        checkColumnNumber(columnNumber);

        Set<Integer> result = new TreeSet<>();
        String rowValue = rowValues[columnNumber];
        if (rowValue.isBlank() == false) {
            String[] splittedValue = rowValue.split(";");
            for (String v : splittedValue) {
                try {
                    result.add(Integer.parseInt(v));
                } catch (NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid integer string: '" + v + "' from '" + rowValue + "'.", ex);
                    ToolUsageLogger.logError(DmGpmPlibRow.class.getCanonicalName(), ex);
                }
            }
        }
        return (result);
    }

}
