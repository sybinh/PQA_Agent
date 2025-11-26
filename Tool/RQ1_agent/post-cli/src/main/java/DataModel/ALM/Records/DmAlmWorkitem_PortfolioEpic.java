/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataStore.ALM.DsAlmRecord;


/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem_PortfolioEpic extends DmAlmWorkitem_HighLevelEpic {

    public static final String ELEMENT_TYPE = "Portfolio Epic";

    public DmAlmWorkitem_PortfolioEpic(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);

        checkForUnusedFields();
    }

}
