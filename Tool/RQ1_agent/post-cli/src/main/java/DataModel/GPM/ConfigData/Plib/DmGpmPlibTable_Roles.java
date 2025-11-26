/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import TablePlus.Csv.CsvLoadException;
import ToolUsageLogger.ToolUsageLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class DmGpmPlibTable_Roles extends DmGpmPlibTable<Integer, DmGpmPlibRow_Role> {

    final private static Logger LOGGER = Logger.getLogger(DmGpmPlibTable_Roles.class.getCanonicalName());

    private static DmGpmPlibTable_Roles table = null;

    private DmGpmPlibTable_Roles() {
    }

    static public DmGpmPlibTable_Roles table() {
        if (table == null) {
            var newTable = new DmGpmPlibTable_Roles();
            InputStream input = DmGpmPlibTable_Roles.class.getResourceAsStream("Roles.csv");
            try {
                newTable.load(input, Encoding.UTF_8, FieldSeparator.SEMICOLON, true);
            } catch (IOException | CsvLoadException ex) {
                LOGGER.log(Level.SEVERE, "Loading failed.", ex);
                ToolUsageLogger.logError(DmGpmPlibTable_Roles.class.getCanonicalName(), ex);
            }
            table = newTable;
        }
        return (table);
    }

    @Override
    protected DmGpmPlibRow_Role createRow(String[] rowValues, int rowNumber) throws CsvLoadException {
        return (new DmGpmPlibRow_Role(rowValues, rowNumber));
    }

}
