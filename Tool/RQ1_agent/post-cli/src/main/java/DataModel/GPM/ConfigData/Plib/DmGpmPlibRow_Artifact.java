/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import TablePlus.Csv.CsvLoadException;
import TestFramework.TestFramework;

/**
 *
 * @author GUG2WI
 */
public class DmGpmPlibRow_Artifact extends DmGpmPlibRow<Integer> {

    //--------------------------------------------------------------------------
    //
    // Position of fields in the CSV file
    //
    //--------------------------------------------------------------------------
    static final int ARTIFACT_ID = 0;
    static final int ARTIFACT_NAME = 1;
    static final int LINK_TO_ARTIFACT = 2;

    DmGpmPlibRow_Artifact(String[] rowValues, int rowNumber) throws CsvLoadException {
        super(rowValues, rowNumber, 3);
    }

    @Override
    public Integer getId() {
        return (getIntValueForColumn(ARTIFACT_ID));
    }

    public String getName() {
        return (getStringValueForColumn(ARTIFACT_NAME));
    }

    public String getLink() {
        return (getLinkValueForColumn(LINK_TO_ARTIFACT));
    }

    @Override
    public String toString() {
        return ("Artifact:" + getId() + "," + getName() + "," + TestFramework.shorten(getLink()));
    }

}
