/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import TablePlus.Csv.CsvLoadException;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class DmGpmPlibRow_TaskStatus extends DmGpmPlibRow<Integer> {

    //--------------------------------------------------------------------------
    //
    // Position of fields in the CSV file
    //
    //--------------------------------------------------------------------------
    static final int TASK_STATUS_ID = 0;
    static final int TASK_STATUS_NAME = 1;

    final private boolean valid;

    DmGpmPlibRow_TaskStatus(String[] rowValues, int rowNumber) throws CsvLoadException {
        super(rowValues, rowNumber, 2);

        String statusName = getStringValueForColumn(TASK_STATUS_NAME);
        switch (statusName) {
            case "Valid":
                valid = true;
                break;
            case "Invalid":
                valid = false;
                break;
            default:
                Logger.getLogger(DmGpmPlibRow_TaskStatus.class.getCanonicalName()).warning("Unexpected value for status name: >" + statusName + "<");
                valid = false;
                break;
        }
    }

    @Override
    public Integer getId() {
        return (getIntValueForColumn(TASK_STATUS_ID));
    }

    public boolean isValid() {
        return (valid);
    }

    @Override
    public String toString() {
        return ("TaskStatus:" + getId() + "," + getStringValueForColumn(TASK_STATUS_NAME));
    }

}
