/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1RecordType;
import OslcAccess.OslcRecordIdentifier;
import OslcAccess.OslcSelection;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Rq1RecordDescription.FixedRecordValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.EcvDate;
import util.EcvEnumeration;

/**
 * Describes the criteria and the expected result (record type) for a query in
 * the RQ1 database via OSLC.
 *
 * @author GUG2WI
 */
public class Rq1QueryDescription {

    static public class DateCriteria {

        public enum Criteria {

            LATER_OR_EQUAL
        }

        final private EcvDate date;
        final private Criteria criteria;

        public DateCriteria(EcvDate date, Criteria criteria) {
            this.date = date;
            this.criteria = criteria;
        }

        final public EcvDate getDate() {
            return date;
        }

        final public Criteria getCriteria() {
            return criteria;
        }

    }

    final private Map<String, Rq1RecordInterface> referenceFieldCriteria;
    final private OslcSelection oslcSelection;
    private Map<String, DateCriteria> dateFieldCriteria = null;

    public Rq1QueryDescription() {
        referenceFieldCriteria = new TreeMap<>();
        oslcSelection = new OslcSelection();
    }

    public Rq1QueryDescription(Rq1RecordType resultRecordType) {
        assert (resultRecordType != null);

        referenceFieldCriteria = new TreeMap<>();
        oslcSelection = new OslcSelection();
        oslcSelection.setRecordType(resultRecordType);
    }

    final public void addCriteria(String fieldname, Collection<String> allowedValues) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (allowedValues != null);
        assert (allowedValues.size() > 0);

        oslcSelection.addValueList(fieldname, allowedValues);
    }

    final public void addCriteria(String fieldname, String[] allowedValues) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);

        oslcSelection.addValueList(fieldname, Arrays.asList(allowedValues));
    }

    final public void addCriteria(String fieldname, EcvEnumeration[] allowedValues) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);

        List<String> valuesList = new ArrayList<>();
        for (EcvEnumeration s : allowedValues) {
            valuesList.add(s.getText());
        }
        oslcSelection.addValueList(fieldname, valuesList);

    }

    final public void addCriteria(Rq1AttributeName attribute, EcvEnumeration[] allowedValues) {
        assert (attribute != null);
        assert (allowedValues != null);
        assert (allowedValues.length > 0);

        List<String> valuesList = new ArrayList<>();
        for (EcvEnumeration s : allowedValues) {
            valuesList.add(s.getText());
        }
        oslcSelection.addValueList(attribute.getName(), valuesList);

    }

    final public void addCriteria(String fieldname, String searchedValue) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (searchedValue != null);
        assert (searchedValue.isEmpty() == false);

        oslcSelection.addValue(fieldname, searchedValue);
    }

    void addCriteria(FixedRecordValue[] fixedRecordValues) {
        assert (fixedRecordValues != null);
        assert (fixedRecordValues.length > 0);

        for (FixedRecordValue value : fixedRecordValues) {
            if (value.isOnlyOneValueAllowed() == true) {
                oslcSelection.addValue(value.getFieldName(), value.getValue());
            } else {
                addCriteria(value.getFieldName(), value.getValues());
            }
        }
    }

    final public void addCriteria(String fieldname, Rq1RecordInterface referencedRecord) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (referencedRecord != null);

        //
        // Referenced record might not exist in the database at this time. 
        // So we store the record in a map and add it to OslcSelection as
        // soon as the selection is used.
        //
        referenceFieldCriteria.put(fieldname, referencedRecord);
    }

    void addCriteria(Rq1AttributeName attribute1, Rq1RecordInterface referencedRecord) {
        assert (attribute1 != null);
        assert (referencedRecord != null);
        referenceFieldCriteria.put(attribute1.getName(), referencedRecord);
    }

    void addCriteria(Rq1AttributeName fieldname1, Rq1AttributeName fieldname2, Rq1RecordInterface referencedRecord) {
        assert (fieldname1 != null);
        assert (fieldname2 != null);
        assert (referencedRecord != null);
        referenceFieldCriteria.put(fieldname1 + "." + fieldname2, referencedRecord);
    }

    final public void addCriteria_isLaterOrEqualThen(String fieldname, EcvDate testDate) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (testDate != null);
        assert (testDate.isEmpty() == false);

        if (dateFieldCriteria == null) {
            dateFieldCriteria = new TreeMap<>();
        }
        dateFieldCriteria.put(fieldname, new DateCriteria(testDate, DateCriteria.Criteria.LATER_OR_EQUAL));
    }

    final public OslcSelection getOslcSelection() {

        //
        // Set criterias for reference to other records
        //
        for (Map.Entry<String, Rq1RecordInterface> e : referenceFieldCriteria.entrySet()) {
            OslcRecordIdentifier r = e.getValue().getOslcRecordIdentifier();
            assert (r != null);
            oslcSelection.addReference(e.getKey(), r.getRdfAbout());
        }
        //
        // Set criterias for reference to odate field.
        //
        if (dateFieldCriteria != null) {
            for (Map.Entry<String, DateCriteria> e : dateFieldCriteria.entrySet()) {
                oslcSelection.addCriteria_isLaterOrEqualThen(e.getKey(), e.getValue().getDate());
            }
        }

        return (oslcSelection);
    }

    final public Rq1RecordType getReferencedRecordType() {
        return ((Rq1RecordType) oslcSelection.getRecordType());
    }

}
