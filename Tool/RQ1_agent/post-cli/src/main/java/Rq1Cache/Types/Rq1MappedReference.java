/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import OslcAccess.OslcRecordIdentifier;
import Rq1Cache.Records.Rq1LinkInterface;
import Rq1Cache.Records.Rq1NodeInterface;
import Rq1Cache.Rq1RecordIndex;

/**
 *
 * @author gug2wi
 */
public class Rq1MappedReference extends Rq1Reference {

    final private OslcRecordIdentifier mapOslcRecordIdentifier;
    private Rq1LinkInterface referencedMap;

    public Rq1MappedReference(OslcRecordIdentifier subject, OslcRecordIdentifier map) {
        super(subject);
        assert (map != null);

        this.mapOslcRecordIdentifier = map;
        this.referencedMap = null;
    }

    public Rq1MappedReference(Rq1NodeInterface rq1Subject, Rq1LinkInterface rq1Map) {
        super(rq1Subject);
        assert (rq1Map != null);

        this.mapOslcRecordIdentifier = rq1Map.getOslcRecordIdentifier();
        this.referencedMap = rq1Map;
    }

    public Rq1LinkInterface getLink() {
        if (referencedMap == null) {
            getRecord(); // Ensure that subject is loaded. The subject is necessary to determine the type of map when loaded from database.
            referencedMap = Rq1RecordIndex.getMap(mapOslcRecordIdentifier);
            assert (referencedMap != null);
        }
        return (referencedMap);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Rq1MappedReference) {
            Rq1MappedReference m = (Rq1MappedReference) o;
            return ((m.referencedMap == referencedMap) && (super.equals(m)));
        } else {
            return (false);
        }
    }
}
