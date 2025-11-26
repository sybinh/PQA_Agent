/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import RestClient.Exceptions.ResponseException;
import OslcAccess.OslcFieldI;
import OslcAccess.OslcRecordFactoryI;
import OslcAccess.OslcRecordIdentifier;
import OslcAccess.OslcRecordPatternI;
import OslcAccess.OslcRecordTypeI;
import OslcAccess.OslcResponseRecordI;
import OslcAccess.OslcStringField;
import Rq1Cache.Records.Rq1LinkInterface;
import Rq1Cache.Records.Rq1NodeInterface;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class Rq1RecordFactory implements OslcRecordFactoryI<Rq1RecordInterface> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1RecordFactory.class.getCanonicalName());

    static private class Rq1RecordPattern implements OslcRecordPatternI {

        final private Rq1RecordType recordType;
        final private List<OslcFieldI> fields = new ArrayList<>();

        public Rq1RecordPattern(Rq1RecordType recordType) {
            assert (recordType != null);
            this.recordType = recordType;
        }

        @Override
        public OslcRecordTypeI getOslcRecordType() {
            return (recordType);
        }

        @Override
        public Iterable<? extends OslcFieldI> getOslcFields() {
            return (fields);
        }

        public void addField(OslcFieldI newField) {
            assert (newField != null);
            for (OslcFieldI field : fields) {
                if (field.getOslcPropertyName().equals(newField.getOslcPropertyName())) {
                    return;
                }
            }
            fields.add(newField);
        }

    }

    final private IdentityHashMap<Rq1RecordType, Rq1RecordPattern> patternMap = new IdentityHashMap<>();

    @Override
    public OslcRecordTypeI getRecordType(String recordTypeFromOslcInterface) {
        return (Rq1RecordType.getRecordType(recordTypeFromOslcInterface));
    }

    @Override
    public OslcRecordPatternI getPattern(OslcRecordTypeI recordType) {
        assert (recordType != null);
        assert (recordType instanceof Rq1RecordType) : "Unknown record type: " + recordType.getClass().getCanonicalName();

        Rq1RecordPattern pattern = patternMap.get((Rq1RecordType) recordType);
        if (pattern == null) {
            pattern = createPattern((Rq1RecordType) recordType);
        }
        return (pattern);
    }

    private Rq1RecordPattern createPattern(Rq1RecordType rq1RecordType) {
        assert (rq1RecordType != null);

        Rq1RecordPattern pattern = new Rq1RecordPattern(rq1RecordType);

        //
        // Add fields for Nodes
        //
        for (Rq1NodeDescription description : Rq1NodeDescription.values()) {
            //
            // Check if the description fits to the searched record type
            //
            if (description.getRecordType() == rq1RecordType) {
                //
                // Add fields of fixed values.
                //
                for (Rq1RecordDescription.FixedRecordValue fix : description.getFixedRecordValues()) {
                    pattern.addField(new OslcStringField(fix.getFieldName()));
                }

                //
                // Add database fields from record.
                //
                Rq1RecordInterface dummyRecord = description.createSubject();
                for (OslcFieldI field : dummyRecord.getOslcFields()) {
                    pattern.addField(field);
                }
            }
        }

        //
        // Add fields for Links
        //
        for (Rq1LinkDescription description : Rq1LinkDescription.values()) {
            //
            // Check if the description fits to the searched record type
            //
            if (description.getRecordType() == rq1RecordType) {
                //
                // Add database fields from record.
                //
                Rq1RecordInterface dummyRecord = description.createMap();
                for (OslcFieldI field : dummyRecord.getOslcFields()) {
                    pattern.addField(field);
                }
            }
        }

        patternMap.put(rq1RecordType, pattern);

        return (pattern);
    }

    @Override
    public Rq1RecordInterface getRecord(OslcResponseRecordI oslcResponseRecord) throws ResponseException {
        assert (oslcResponseRecord != null);

        //----------------------------------------------------------------------
        //
        // Check if object for the record exists already in cache.
        //
        //----------------------------------------------------------------------
        Rq1RecordInterface rq1Record = Rq1RecordIndex.getRecord_NoFetch(oslcResponseRecord.getOslcRecordIdentifier());
        if (rq1Record != null) {
            return (rq1Record);
        }

        OslcRecordTypeI recordType = oslcResponseRecord.getOslcRecordIdentifier().getRecordType();

        //----------------------------------------------------------------------
        //
        // Check if record fits to a RQ1 node.
        //
        //----------------------------------------------------------------------
        for (Rq1NodeDescription nodeDescription : Rq1NodeDescription.values()) {
            if (nodeDescription.getRecordType() == recordType) {
                //
                // Check if fixed values fit
                //
                boolean fixedValuesFit = true;
                for (Rq1NodeDescription.FixedRecordValue fixedValue : nodeDescription.getFixedRecordValues()) {
                    String fieldValue = oslcResponseRecord.getFieldValue(fixedValue.getFieldName());
                    if (fixedValue.matchWithValue(fieldValue) == false) {
                        fixedValuesFit = false;
                        break;
                    }
                }

                if (fixedValuesFit == true) {
                    //------------------------------------------------------------------------
                    //
                    // Record type and fixed values match -> Create new object for the record
                    //
                    //------------------------------------------------------------------------
                    Rq1NodeInterface rq1Node = nodeDescription.createSubject();
                    rq1Node.setOslcRecordIdentifier(oslcResponseRecord.getOslcRecordIdentifier());
                    return (rq1Node);
                }
            }
        }

        //----------------------------------------------------------------------
        //
        // Check if record fits to a RQ1 map
        //
        //----------------------------------------------------------------------
        Rq1RecordInterface subjectA = null;
        Rq1RecordInterface subjectB = null;
        for (Rq1LinkDescription linkDescription : Rq1LinkDescription.values()) {

            if (linkDescription.getRecordType() == recordType) {

                //--------------------------------------------------------------------------------------------
                //
                // At least the record type matches -> So we can load the referenced Records, if not done yet
                //
                //--------------------------------------------------------------------------------------------
                if (subjectA == null) {

                    OslcRecordIdentifier referenceSubjectA = oslcResponseRecord.getSubRecords(linkDescription.getFieldNameSubjectA()).get(0).getOslcRecordIdentifier();
                    subjectA = Rq1RecordIndex.getRecord(referenceSubjectA);
                    assert (subjectA != null);

                    OslcRecordIdentifier referenceSubjectB = oslcResponseRecord.getSubRecords(linkDescription.getFieldNameSubjectB()).get(0).getOslcRecordIdentifier();
                    subjectB = Rq1RecordIndex.getRecord(referenceSubjectB);
                    assert (subjectB != null);
                }

                //
                // Check if subject types fit
                //
                Class classNodeA = linkDescription.getSubjectA().getNodeClass();
                if (classNodeA.isInstance(subjectA) == false) {
                    continue;
                }
                Class classNodeB = linkDescription.getSubjectB().getNodeClass();
                if (classNodeB.isInstance(subjectB) == false) {
                    continue;
                }

                //
                // Check if fixed values fit
                //
                boolean fixedValuesFit = true;
                for (Rq1NodeDescription.FixedRecordValue fixedValue : linkDescription.getFixedRecordValues()) {
                    String fieldValue = oslcResponseRecord.getFieldValue(fixedValue.getFieldName());
                    if (fixedValue.matchWithValue(fieldValue) == false) {
                        fixedValuesFit = false;
                        break;
                    }
                }

                if (fixedValuesFit == true) {
                    //
                    // Create new object for the map
                    //
                    Rq1LinkInterface rq1Map = linkDescription.createMap();
                    rq1Map.setOslcRecordIdentifier(oslcResponseRecord.getOslcRecordIdentifier());
                    return (rq1Map);
                }
            }
        }

        //
        // No matching record class found
        //
        logger.severe("No record class found for record " + oslcResponseRecord.getRdfAbout());
        if (subjectA != null) {
            logger.severe("Subject A found: " + subjectA.getId() + "/" + subjectA.getClass().getCanonicalName());
        }
        if (subjectB != null) {
            logger.severe("Subject B found: " + subjectB.getId() + "/" + subjectB.getClass().getCanonicalName());
        }
        logger.warning(oslcResponseRecord.getRecordContent().toString());

        return (null);
    }

    @Override
    public void addRecord(Rq1RecordInterface newRecord) {
        Rq1RecordIndex.addRecord(newRecord);
    }

}
