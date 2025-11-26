/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import Rq1Data.Enumerations.ImpactCategory;
import TablePlus.Csv.CsvLoadException;

/**
 *
 * @author GUG2WI
 */
public class DmGpmPlibRow_ImpactCategory extends DmGpmPlibRow<Integer> {

    //--------------------------------------------------------------------------
    //
    // Position of fields in the CSV file
    //
    //--------------------------------------------------------------------------
    static final int IMPACT_CATEGORY_ID = 0;
    static final int IMPACT_CATEGORY = 1;

    final private ImpactCategory impactCategory;

    DmGpmPlibRow_ImpactCategory(String[] rowValues, int rowNumber) throws CsvLoadException {
        super(rowValues, rowNumber, 2);

        String icValue = getStringValueForColumn(IMPACT_CATEGORY);
        impactCategory = ImpactCategory.getByValue(icValue);
        if (impactCategory == null) {
            throw new Error("Unknown impact category: " + icValue);
        }
    }

    @Override
    public Integer getId() {
        return (getIntValueForColumn(IMPACT_CATEGORY_ID));
    }

    public ImpactCategory getCategory() {
        return (impactCategory);
    }

    @Override
    public String toString() {
        return ("ImpactCategory:" + getId() + "," + getCategory());
    }

}
