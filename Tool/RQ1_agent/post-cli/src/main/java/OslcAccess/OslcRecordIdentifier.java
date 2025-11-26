/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.Objects;

/**
 * Identifier for a record on the OSLC interface.
 *
 * @author gug2wi
 */
public class OslcRecordIdentifier {

    final static public String patternRdfAbout = "https?://.*";
    final static public String patternDctermType = ".+";

    final private String rdfAbout; // Attribute rdf:about in the server response
    final private OslcRecordTypeI recordType;
    final private String oslcShortTitle; // Tag <oslc:shortTitle> in the server response

    public OslcRecordIdentifier(String rdfAbout, OslcRecordTypeI recordType) {
        assert (rdfAbout != null);
        assert (rdfAbout.matches(patternRdfAbout)) : "rdfAbout=" + rdfAbout;
        assert (recordType != null);

        this.rdfAbout = rdfAbout;
        this.recordType = recordType;
        this.oslcShortTitle = null;
    }

    protected OslcRecordIdentifier(String rdfAbout, OslcRecordTypeI recordType, String oslcShortTitle) {
        assert (rdfAbout != null);
        assert (rdfAbout.matches(patternRdfAbout)) : "rdfAbout=" + rdfAbout;
        assert (recordType != null);
        assert (oslcShortTitle != null);
        assert (oslcShortTitle.isEmpty() == false);

        this.rdfAbout = rdfAbout;
        this.recordType = recordType;
        this.oslcShortTitle = oslcShortTitle;
    }

    public String getRdfAbout() {
        return (rdfAbout);
    }

    public OslcRecordTypeI getRecordType() {
        return (recordType);
    }

    public String getShortTitle() {
        if (oslcShortTitle != null) {
            return (oslcShortTitle);
        } else {
            String rdfParts[] = rdfAbout.split("/");
            return (rdfParts[rdfParts.length - 1]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OslcRecordIdentifier) {
            return (this.rdfAbout.equals(((OslcRecordIdentifier) o).rdfAbout));
        } else {
            return (false);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.rdfAbout);
        return hash;
    }

    @Override
    public String toString() {
        return (getShortTitle() + " - " + recordType.getOslcType() + " - " + rdfAbout);
    }

}
