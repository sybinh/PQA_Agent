/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import TablePlus.Csv.CsvLoadException;

/**
 *
 * @author GUG2WI
 */
public class DmGpmPlibRow_Role extends DmGpmPlibRow<Integer> {

    //--------------------------------------------------------------------------
    //
    // Position of fields in the CSV file
    //
    //--------------------------------------------------------------------------
    static final int ROLE_ID = 0;
    static final int ROLE_NAME = 1;
    static final int ROLE_SHORT_NAME = 2;

    DmGpmPlibRow_Role(String[] rowValues, int rowNumber) throws CsvLoadException {
        super(rowValues, rowNumber, 3);
    }

    @Override
    public Integer getId() {
        return (getIntValueForColumn(ROLE_ID));
    }

    public String getName() {
        return (getStringValueForColumn(ROLE_NAME));
    }

    public String getShortName() {
        return (getStringValueForColumn(ROLE_SHORT_NAME));
    }

    @Override
    public String toString() {
        return ("Role:" + getId() + "," + getShortName() + "," + getName());
    }

}
