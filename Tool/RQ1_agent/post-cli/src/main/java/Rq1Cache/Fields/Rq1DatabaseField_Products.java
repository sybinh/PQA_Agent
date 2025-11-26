/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Rq1Cache.Records.Rq1RecordInterface;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_Products extends Rq1DatabaseField_StringAccess<Set<String>> implements Rq1FieldI<Set<String>> {

    public Rq1DatabaseField_Products(Rq1RecordInterface parent, String dbFieldName) {
        super(parent, dbFieldName, new TreeSet<String>());
    }

    @Override
    public boolean setOslcValue_Internal(String dbValue, Source source) {
        assert (dbValue != null);
        assert (source != null);

        if (dbValue.isEmpty()) {
            return (setDataSourceValue(new TreeSet<String>(), source));
        } else {
            return (setDataSourceValue(new TreeSet<String>(Arrays.asList(dbValue.split("\n"))), source));
        }
    }

    @Override
    protected String getOslcValue_Internal() {
        StringBuilder b = new StringBuilder(100);
        boolean first = true;
        for (String product : getDataSourceValue()) {
            if (first == false) {
                b.append("\n");
            }
            first = false;
            b.append(product);
        }
        return (b.toString());
    }
}
