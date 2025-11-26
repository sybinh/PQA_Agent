/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import util.EcvDate;

/**
 * Storage for selection parameters for a OSCL get request.
 *
 * @author GUG2WI
 */
public class OslcSelection {

    public enum SelectionType {

        INCOMPLETE,
        NAME,
        RDF_ABOUT,
        FILTER_CRITERIAS
    }

    static public class DateCriteriaOslc {

        public enum Criteria {

            LATER_OR_EQUAL
        }

        final private EcvDate date;
        final private Criteria criteria;

        public DateCriteriaOslc(EcvDate date, Criteria criteria) {
            assert (date != null);
            assert (date.isEmpty() == false);
            assert (criteria != null);
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

    private OslcRecordTypeI recordType;
    private String name;
    private String rdfAbout;
    private Map<String, String> references = null;
    private Map<String, String> values = null;
    private Map<String, Collection<String>> valueLists = null;
    private Map<String, DateCriteriaOslc> dateFieldCriteria = null;

    public OslcSelection() {
        recordType = null;
        name = null;
        rdfAbout = null;
    }

    public OslcSelection(OslcRecordIdentifier recordReference) {
        this();
        assert (recordReference != null);

        setRecordType(recordReference.getRecordType());
        setRdfAbout(recordReference.getRdfAbout());
    }

    final public OslcSelection setRecordType(OslcRecordTypeI recordType) {
        assert (recordType != null);
        this.recordType = recordType;
        return (this);
    }

    final public OslcSelection setName(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);
        assert (this.name == null);
        assert (this.rdfAbout == null);
        this.name = name;
        return (this);
    }

    final public OslcSelection setRdfAbout(String rdfAbout) {
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);
        assert (this.name == null);
        assert (this.rdfAbout == null);
        this.rdfAbout = rdfAbout;
        return (this);
    }

    final public SelectionType getSelectionType() {

        if (rdfAbout != null) {
            //
            // RDF-About set -> Unique identifier; no additonal info needed
            //
            return (SelectionType.RDF_ABOUT);

        } else if (name != null) {
            //
            // ID set -> Unique identifier; Record Type may be set additionally
            //
            return (SelectionType.NAME);

        } else if ((recordType != null)
                && (((references != null) && (references.isEmpty() == false))
                || ((dateFieldCriteria != null) && (dateFieldCriteria.isEmpty() == false))
                || ((values != null) && (values.isEmpty() == false))
                || ((valueLists != null) && (valueLists.isEmpty() == false)))) {
            //
            // One of the following criterias set:
            // - Reference to other record.
            // - List of allowed values for a field
            //
            // For this criteria, always the record type is necessary.
            //
            return (SelectionType.FILTER_CRITERIAS);

        } else {
            return (SelectionType.INCOMPLETE);
        }
    }

    final public boolean isCriteriaSet() {
        return (getSelectionType() != SelectionType.INCOMPLETE);
    }

    final public OslcRecordTypeI getRecordType() {
        return (recordType);
    }

    final public String getName() {
        return (name);
    }

    final public String getRdfAbout() {
        return (rdfAbout);
    }

    final public void addReference(String fieldname, String rdfAbout) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);
        if (references == null) {
            references = new TreeMap<>();
        }
        references.put(fieldname, rdfAbout);
    }

    final public void addValueList(String fieldname, Collection<String> valueList) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (valueList != null);
        assert (valueList.iterator().hasNext() == true);

        if (valueLists == null) {
            valueLists = new TreeMap<>();
        }
        valueLists.put(fieldname, valueList);
    }

    final public void addValue(String fieldname, String value) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (value != null);
        assert (value.isEmpty() == false);

        if (values == null) {
            values = new TreeMap<>();
        }
        values.put(fieldname, value);
    }

    final public void addCriteria_isLaterOrEqualThen(String fieldname, EcvDate testDate) {
        assert (fieldname != null);
        assert (fieldname.isEmpty() == false);
        assert (testDate != null);
        assert (testDate.isEmpty() == false);
        if (dateFieldCriteria == null) {
            dateFieldCriteria = new TreeMap<>();
        }
        dateFieldCriteria.put(fieldname, new DateCriteriaOslc(testDate, DateCriteriaOslc.Criteria.LATER_OR_EQUAL));
    }

    final public Map<String, String> getReferences() {
        if (references != null) {
            return (references);
        } else {
            return (new TreeMap<>());
        }
    }

    final public Map<String, Collection<String>> getValueLists() {
        if (valueLists != null) {
            return (valueLists);
        } else {
            return (new TreeMap<>());
        }
    }

    final public Map<String, String> getValues() {
        if (values != null) {
            return (values);
        } else {
            return (new TreeMap<>());
        }
    }

    final public Map<String, DateCriteriaOslc> getDateCriterias() {
        if (dateFieldCriteria != null) {
            return (dateFieldCriteria);
        } else {
            return (new TreeMap<>());
        }
    }
}
