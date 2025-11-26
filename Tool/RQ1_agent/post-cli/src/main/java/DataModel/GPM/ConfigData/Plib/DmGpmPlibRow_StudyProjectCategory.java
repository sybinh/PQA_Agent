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
public class DmGpmPlibRow_StudyProjectCategory extends DmGpmPlibRow<Integer> {

    //--------------------------------------------------------------------------
    //
    // Position of fields in the CSV file
    //
    //--------------------------------------------------------------------------
    static final int STUDY_PROJECT_ID = 0;
    static final int STUDY_PROJECT = 1;

    DmGpmPlibRow_StudyProjectCategory(String[] rowValues, int rowNumber) throws CsvLoadException {
        super(rowValues, rowNumber, 2);
    }

    @Override
    public Integer getId() {
        return (getIntValueForColumn(STUDY_PROJECT_ID));
    }

    public String getSwaaPID() {
        return (getStringValueForColumn(STUDY_PROJECT));
    }

    @Override
    public String toString() {
        return ("StudyProject:" + getId() + "," + getSwaaPID());
    }

}
