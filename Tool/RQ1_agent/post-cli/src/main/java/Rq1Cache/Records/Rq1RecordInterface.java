/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsRecordI;
import OslcAccess.OslcLoadHint;
import OslcAccess.OslcRecordI;
import OslcAccess.Rq1.OslcRq1ServerDescription.LinkType;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Rq1RecordDescription;

/**
 *
 * @author GUG2WI
 */
public interface Rq1RecordInterface extends DsRecordI<Rq1FieldI>, OslcRecordI {

    Rq1RecordDescription getRecordDescription();

    String getOslcShortTitle();

    void openInRq1(LinkType linkType);

    void reload();

    void loadCache(OslcLoadHint loadHint);

    /**
     * Save all changes made in the fields of the record to the database. If the
     * record does not yet exists in the database, then the record will be
     * created.
     *
     * @param fieldOrder
     * @return true ... If the writing to the database was successful for all
     * fields. Or if no field was changed and no writing was necessary.
     * <p>
     * false ... Otherwise.
     */
    boolean save(Rq1AttributeName[] fieldOrder);
}
