/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 *
 * @author gug2wi
 */
public class OslcRecordReference<T_RECORD extends OslcRecordI> {

    final private OslcRecordIdentifier recordIdentifier;
    final private T_RECORD record;

    public OslcRecordReference(OslcRecordIdentifier oslcRecordReference) {
        assert (oslcRecordReference != null);

        this.recordIdentifier = oslcRecordReference;
        this.record = null;
    }

    public OslcRecordReference(OslcRecordIdentifier oslcRecordReference, T_RECORD record) {
        assert (oslcRecordReference != null);
        assert (record != null) : oslcRecordReference.toString();

        this.recordIdentifier = oslcRecordReference;
        this.record = record;
    }

    final public T_RECORD getRecord() {
        return (record);
    }

    final public OslcRecordIdentifier getRecordIdentifier() {
        return recordIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OslcRecordReference) {
            return (recordIdentifier.equals(((OslcRecordReference) o).recordIdentifier));
        } else {
            return (false);
        }
    }

}
