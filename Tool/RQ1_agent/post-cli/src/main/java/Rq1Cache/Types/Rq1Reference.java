/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import OslcAccess.OslcRecordIdentifier;
import OslcAccess.OslcRecordReference;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1RecordIndex;
import Rq1Cache.Rq1RecordType;

/**
 * Represents a reference to a RQ1 record, which can be the object in the memory
 * or the record in the RQ1 database.
 *
 * @author gug2wi
 */
public class Rq1Reference {

    final private OslcRecordIdentifier oslcRecordIdentifier; // URL to record in RQ1 database
    private Rq1RecordInterface referencedRecord;

    public Rq1Reference(OslcRecordIdentifier oslcRecordReference) {
        assert (oslcRecordReference != null);

        this.oslcRecordIdentifier = oslcRecordReference;
        this.referencedRecord = null;
    }

    public Rq1Reference(Rq1RecordInterface referencedNode) {
        assert (referencedNode != null);

        this.oslcRecordIdentifier = referencedNode.getOslcRecordIdentifier();
        this.referencedRecord = referencedNode;
    }

    public Rq1Reference(OslcRecordReference<Rq1RecordInterface> reference) {
        assert (reference != null);
        this.oslcRecordIdentifier = reference.getRecordIdentifier();
        this.referencedRecord = reference.getRecord();
    }

    final public OslcRecordIdentifier getOslcRecordReference() {
        if (oslcRecordIdentifier == null) {
            return (referencedRecord.getOslcRecordIdentifier());
        }
        return (oslcRecordIdentifier);
    }

    final public String getId() {
        if (referencedRecord != null) {
            return (referencedRecord.getId());
        } else {
            return ("");
        }
    }

    final public Rq1RecordInterface getRecord() {
        if (referencedRecord == null) {
            referencedRecord = Rq1RecordIndex.getRecord(oslcRecordIdentifier);
        }
        return (referencedRecord);
    }

    @Override
    public boolean equals(Object other) {
        Rq1RecordInterface otherReferencedRecord;
        OslcRecordIdentifier otherOslcRecordReference;

        if (other instanceof Rq1Reference) {
            otherReferencedRecord = ((Rq1Reference) other).referencedRecord;
            otherOslcRecordReference = ((Rq1Reference) other).oslcRecordIdentifier;
        } else if (other instanceof Rq1RecordInterface) {
            otherReferencedRecord = (Rq1RecordInterface) other;
            otherOslcRecordReference = otherReferencedRecord.getOslcRecordIdentifier();
        } else if (other instanceof String) {
            String s = (String) other;
            if (oslcRecordIdentifier != null) {
                return (oslcRecordIdentifier.getRdfAbout().equals(s));
            } else {
                return (false);
            }
        } else {
            return (false);
        }

        if ((referencedRecord != null) && (otherReferencedRecord != null)) {
            return (referencedRecord == otherReferencedRecord);
        } else if ((oslcRecordIdentifier != null) && (otherOslcRecordReference != null)) {
            return (oslcRecordIdentifier.equals(otherOslcRecordReference));
        }

        return (false);
    }

    @Override
    public String toString() {
        if (referencedRecord != null) {
            return (getClass().getSimpleName() + ": " + referencedRecord.getId());
        } else if (oslcRecordIdentifier != null) {
            return (this.getClass().getSimpleName() + ": " + oslcRecordIdentifier.getShortTitle());
        } else {
            return (this.getClass().getSimpleName() + ": Empty");
        }
    }

    public String getStringRepresentation() {
        assert (oslcRecordIdentifier.getRecordType() instanceof Rq1RecordType) : oslcRecordIdentifier.getRecordType().toString();
        return (oslcRecordIdentifier.getRecordType().getText() + ":" + oslcRecordIdentifier.getRdfAbout());
    }

    public static Rq1Reference buildFromStringRepresentation(String stringRepresentation) {

        if ((stringRepresentation != null) && (stringRepresentation.isEmpty() == false)) {
            int seperatorIndex = stringRepresentation.indexOf((int) ':');
            if (seperatorIndex > 0) {
                Rq1RecordType recordType = Rq1RecordType.getRecordType(stringRepresentation.substring(0, seperatorIndex));
                if (recordType != null) {
                    String rdfAbout = stringRepresentation.substring(seperatorIndex + 1);
                    OslcRecordIdentifier oslcReference = new OslcRecordIdentifier(rdfAbout, recordType);
                    return (new Rq1Reference(oslcReference));
                }
            }
        }

        return (null);
    }

}
