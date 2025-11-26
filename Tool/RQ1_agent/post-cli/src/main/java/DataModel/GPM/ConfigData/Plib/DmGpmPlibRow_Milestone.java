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
public class DmGpmPlibRow_Milestone extends DmGpmPlibRow<Integer> {

    //--------------------------------------------------------------------------
    //
    // Position of fields in the CSV file
    //
    //--------------------------------------------------------------------------
    static final int MILESTONES_ID = 0;
    static final int MILESTONE_NAME = 1;

    DmGpmPlibRow_Milestone(String[] rowValues, int rowNumber) throws CsvLoadException {
        super(rowValues, rowNumber, 2);
    }

    @Override
    public Integer getId() {
        return (getIntValueForColumn(MILESTONES_ID));
    }

    public String getName() {
        return (getStringValueForColumn(MILESTONE_NAME));
    }

    @Override
    public String toString() {
        return ("Milestone:" + getId() + "," + getName());
    }

}
