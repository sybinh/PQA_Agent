/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1QueryHit;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1FreeQuery;

/**
 *
 * @author miw83wi
 */
public class DmRq1FreeQuery extends DmRq1TopReferenceList<DmRq1QueryHit> {

    final private Rq1FreeQuery query;

    public DmRq1FreeQuery(String nameForUserInterface, Rq1RecordType recordType) {
        super(new Rq1FreeQuery(recordType), nameForUserInterface);
        query = (Rq1FreeQuery) super.rq1ReferenceList;
    }

    public void addReferenceCriteria(Rq1AttributeName attribute1, Rq1AttributeName attribute2, DmRq1Element referencedElement) {
        assert (attribute1 != null);
        assert (attribute2 != null);
        assert (referencedElement != null);

        query.addReferenceCriteria(attribute1, attribute2, referencedElement.getRq1Record());
    }

    void addRequestedAttribute(Rq1AttributeName attribute) {
        assert (attribute != null);
        query.addRequestedAttribute(attribute);
    }

}
