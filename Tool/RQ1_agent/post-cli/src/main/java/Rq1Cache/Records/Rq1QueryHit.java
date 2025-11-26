/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import OslcAccess.OslcRecordIdentifier;
import OslcAccess.OslcRecordTypeI;
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1RecordDescription;
import Rq1Cache.Rq1RecordIndex;
import Rq1Cache.Rq1RecordType;
import java.util.Map;
import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public class Rq1QueryHit extends Rq1Record {

    static public class Rq1QueryHitDescription implements Rq1RecordDescription {

        @Override
        public Rq1RecordType getRecordType() {
            return (Rq1RecordType.QUERY_HIT);
        }

        @Override
        public FixedRecordValue[] getFixedRecordValues() {
            return (new FixedRecordValue[0]);
        }

    }

    static Rq1QueryHitDescription description = new Rq1QueryHitDescription();

    final private OslcRecordIdentifier oslcRecordReference;
    final private String title;

    public Rq1QueryHit(OslcRecordIdentifier oslcRecordReference, OslcRecordTypeI referencedRecordType, String title) {
        super(description);
        assert (oslcRecordReference != null);
        assert (referencedRecordType != null);
        assert (oslcRecordReference.getRecordType() == referencedRecordType);
        assert (title != null);

        this.oslcRecordReference = oslcRecordReference;
//        this.referencedRecordType = referencedRecordType;
        this.title = title;

    }

    public Rq1QueryHit(OslcRecordIdentifier oslcRecordReference, OslcRecordTypeI referencedRecordType, String title, Map<String, String> queryFields) {
        this(oslcRecordReference, referencedRecordType, title);
        assert (queryFields != null);

        for (Map.Entry<String, String> oslcField : queryFields.entrySet()) {

            if (oslcField.getKey().equals("dbid") == false) {

                EcvDate ecvDate = EcvDate.getDateForOslcValue(oslcField.getValue());
                if (ecvDate != null) {
                    Rq1DatabaseField_Date rq1Field = new Rq1DatabaseField_Date(this, oslcField.getKey());
                    rq1Field.setDataModelValue(ecvDate);
                    addField(rq1Field);
                } else {
                    Rq1DatabaseField_Text rq1Field = new Rq1DatabaseField_Text(this, oslcField.getKey());
                    rq1Field.setDataModelValue(oslcField.getValue());
                    addField(rq1Field);
                }

            }
        }
    }

    public String getTitle() {
        return (title);
    }

    public OslcRecordTypeI getReferencedRecordType() {
        return (oslcRecordReference.getRecordType());
    }

    public Rq1RecordInterface getReferencedRecord() {
        return (Rq1RecordIndex.getRecord(oslcRecordReference));
    }

    public String getReferencedId() {
        return (oslcRecordReference.getShortTitle());
    }

    @Override
    public void reload() {
        // Not supported. Does not make sense.
    }

    @Override
    public boolean save(Rq1AttributeName[] fieldOrder) {
        // Not supported. Does not make sense.
        return (false);
    }

    @Override
    public String getId() {
        return (getClass().getCanonicalName() + "-" + oslcRecordReference.getShortTitle());
    }

}
