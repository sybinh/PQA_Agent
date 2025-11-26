/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.Rq1.Fields.DmRq1TopReferenceList;
import DataModel.Rq1.Records.DmRq1QueryHit;
import Rq1Cache.Types.Rq1IdQuery;
import java.util.Set;

/**
 *
 * @author ths83wi
 */
public class DmRq1IdQuery extends DmRq1TopReferenceList<DmRq1QueryHit> {

    
    public DmRq1IdQuery(String nameForUserInterface, Set<String> rq1Ids) {
        super(new Rq1IdQuery(rq1Ids), nameForUserInterface);
    }

    @Override
    public String getNameForUserInterface() {
        return ("RQONE IDs");
    }

    @Override
    public String getViewTitle() {
        return getNameForUserInterface();
    }
}
