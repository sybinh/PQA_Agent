/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import OslcAccess.OslcPropertyName;

/**
 * Contains a non null non empty name of an RQ1 attribute.
 *
 * The name may consist of sub names separated by dots.
 *
 * @author gug2wi
 */
public class Rq1AttributeName {

    final private String name;

    public Rq1AttributeName(String fieldName) {
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);

        this.name = fieldName;
    }

    protected Rq1AttributeName(String subRecordName, String fieldName) {
        assert (subRecordName != null);
        assert (subRecordName.isEmpty() == false);
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);

        this.name = subRecordName + "." + fieldName;
    }

    protected Rq1AttributeName(String subRecordName, String subSubRecordName, String fieldName) {
        assert (subRecordName != null);
        assert (subRecordName.isEmpty() == false);
        assert (subSubRecordName != null);
        assert (subSubRecordName.isEmpty() == false);
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);

        this.name = subRecordName + "." + subSubRecordName + "." + fieldName;
    }

    public String getName() {
        return (name);
    }

    public OslcPropertyName getOslcName() {
        return (new OslcPropertyName(name));
    }

    @Override
    public String toString() {
        return (name);
    }
}
