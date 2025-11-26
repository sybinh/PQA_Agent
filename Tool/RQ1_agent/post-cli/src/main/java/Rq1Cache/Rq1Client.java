/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.NotFoundException;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.WriteToDatabaseRejected;
import OslcAccess.OslcForwardableFieldI;
import OslcAccess.OslcFullTextSearchResultRecord;
import OslcAccess.OslcLoadHint;
import OslcAccess.OslcPropertyName;
import OslcAccess.OslcProtocolVersion;
import OslcAccess.OslcQueryResult;
import OslcAccess.OslcQueryResultRecord;
import OslcAccess.OslcRecordIdentifier;
import OslcAccess.OslcRecordReference;
import OslcAccess.Rq1.OslcRq1Client;
import OslcAccess.Rq1.OslcRq1ServerDescription.LinkType;
import OslcAccess.OslcSelection;
import OslcAccess.OslcUploadFileResult;
import Rq1Cache.Fields.Rq1DatabaseField_MappedReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList_Writeable;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1ForwardableRecord;
import Rq1Cache.Records.Rq1Project;
import Rq1Cache.Records.Rq1QueryHit;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Records.Rq1Subject;
import Rq1Cache.Types.Rq1Reference;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import static ToolUsageLogger.ToolUsageLogger.LIST_RQ1_ELEMENT_TIMER;
import static ToolUsageLogger.ToolUsageLogger.SINGLE_RQ1_ELEMENT_TIMER;
import ToolUsageLogger.TulTimer;
import util.EcvApplication;

/**
 *
 * @author gug2wi
 */
public class Rq1Client {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1Client.class.getCanonicalName());

    final static public Rq1Client client = new Rq1Client();

    private Rq1Client() {

    }

    public List<Rq1Reference> loadRecordsAsList(OslcSelection selection, boolean loadRecords) {
        assert (selection != null);
        return (loadRecordsAsList(selection, new OslcLoadHint(loadRecords)));
    }

    public List<Rq1Reference> loadRecordsAsList(OslcSelection selection, OslcLoadHint loadHint) {
        assert (selection != null);
        assert (loadHint != null);

        //
        // Load values from database
        //
        List<OslcRecordReference<Rq1RecordInterface>> result;
        try {
            result = OslcRq1Client.getOslcClient().loadRecords(selection, loadHint);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, "loadRecords() failed.", ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (null);
        }

        //
        // Create RQ1 reference list
        //
        List<Rq1Reference> references = new ArrayList<>(result.size());
        for (OslcRecordReference<Rq1RecordInterface> reference : result) {
            if (loadHint.isLoadRecordContentActive() == true) {
                //
                // Create record and reference
                //
                references.add(new Rq1Reference(reference.getRecord()));
            } else {
                //
                // Create only reference
                //
                references.add(new Rq1Reference(reference.getRecordIdentifier()));
            }
        }
        return (references);
    }

    public void loadRecordById(String id) {
        assert (id != null);
        assert (id.isEmpty() == false);

        logger.fine("Load record " + id);

        TulTimer timer = new TulTimer();

        try {
            OslcRq1Client.getOslcClient().loadRecordById(id);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            if (ex instanceof NotFoundException) {
                logger.log(Level.WARNING, "Loading failed. Id = " + id, ex);
            } else {
                logger.log(Level.SEVERE, "Loading failed. Id = " + id, ex);
                ToolUsageLogger.logError(Rq1Client.class.getCanonicalName(), ex);
            }
            EcvApplication.handleException(ex);
        } catch (Throwable ex) {
            logger.log(Level.WARNING, "Loading failed. Id = " + id, ex);
            throw (ex);
        }

        ToolUsageLogger.addTime(SINGLE_RQ1_ELEMENT_TIMER, timer);
    }

    public void loadRecordByIdentifier(OslcRecordIdentifier reference) {
        assert (reference != null);

        logger.fine("Load record " + reference.getShortTitle() + " - " + reference.getRdfAbout());

        TulTimer timer = new TulTimer();

        try {
            OslcRq1Client.getOslcClient().loadRecordByIdentifier(reference);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
        }

        ToolUsageLogger.addTime(SINGLE_RQ1_ELEMENT_TIMER, timer);
    }

    public void loadRecordByIdentifier(OslcRecordIdentifier reference, OslcLoadHint loadHint) {
        assert (reference != null);
        assert (loadHint != null);

        logger.fine("Load record with hint " + reference.getShortTitle() + " - " + reference.getRdfAbout());

        try {
            OslcRq1Client.getOslcClient().loadRecordByIdentifier(reference, loadHint);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
        }
    }

    public void loadReferenceField(Rq1DatabaseField_ReferenceList field, boolean loadRecords, OslcProtocolVersion oslcProtocolVersion) {
        assert (field != null);
        assert (oslcProtocolVersion != null);

        logger.fine("Load field " + field.getParentRecord().getId() + "/" + field.getFieldName());

        OslcSelection selection = new OslcSelection(field.getParentRecord().getOslcRecordIdentifier());
        OslcLoadHint loadHint = new OslcLoadHint(false);
        loadHint.addFieldHint(field.getOslcPropertyName(), new OslcLoadHint(loadRecords));

        TulTimer timer = new TulTimer();

        try {
            OslcRq1Client.getOslcClient().loadRecords(selection, loadHint, oslcProtocolVersion);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            EcvApplication.handleException(ex);
        }

        ToolUsageLogger.addTime(LIST_RQ1_ELEMENT_TIMER, timer);
    }

    public void loadReferenceField(Rq1DatabaseField_ReferenceList field, OslcLoadHint loadHint, OslcProtocolVersion oslcProtocolVersion) {
        assert (field != null);
        assert (loadHint != null);
        assert (oslcProtocolVersion != null);

        logger.fine("Load field " + field.getParentRecord().getId() + "/" + field.getFieldName());

        OslcSelection selection = new OslcSelection(field.getParentRecord().getOslcRecordIdentifier());
        OslcLoadHint loadHintForRecord = new OslcLoadHint(false);
        loadHintForRecord.addFieldHint(field.getOslcPropertyName(), loadHint);

        try {
            OslcRq1Client.getOslcClient().loadRecords(selection, loadHintForRecord, oslcProtocolVersion);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
        }
    }

    public void loadReferenceField(Rq1DatabaseField_ReferenceList_Writeable field, boolean loadRecords, OslcProtocolVersion oslcProtocolVersion) {
        assert (field != null);
        assert (oslcProtocolVersion != null);

        logger.fine("Load field " + field.getParentRecord().getId() + "/" + field.getFieldName());

        OslcSelection selection = new OslcSelection(field.getParentRecord().getOslcRecordIdentifier());
        OslcLoadHint loadHint = new OslcLoadHint(false);
        loadHint.addFieldHint(field.getOslcPropertyName(), new OslcLoadHint(loadRecords));

        TulTimer timer = new TulTimer();

        try {
            OslcRq1Client.getOslcClient().loadRecords(selection, loadHint, oslcProtocolVersion);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            EcvApplication.handleException(ex);
        }

        ToolUsageLogger.addTime(LIST_RQ1_ELEMENT_TIMER, timer);
    }

    public void loadReferenceField(Rq1DatabaseField_ReferenceList_Writeable field, OslcLoadHint loadHint, OslcProtocolVersion oslcProtocolVersion) {
        assert (field != null);
        assert (loadHint != null);
        assert (oslcProtocolVersion != null);

        logger.fine("Load field " + field.getParentRecord().getId() + "/" + field.getFieldName());

        OslcSelection selection = new OslcSelection(field.getParentRecord().getOslcRecordIdentifier());
        OslcLoadHint loadHintForRecord = new OslcLoadHint(false);
        loadHintForRecord.addFieldHint(field.getOslcPropertyName(), loadHint);

        try {
            OslcRq1Client.getOslcClient().loadRecords(selection, loadHintForRecord, oslcProtocolVersion);
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
        }
    }

    public void loadMappedReferenceList(Rq1DatabaseField_MappedReferenceList field, boolean loadRecords) {
        assert (field != null);

        logger.log(Level.FINE, "Load field {0}/{1}", new Object[]{field.getParentRecord().getId(), field.getFieldName()});

        OslcSelection selection = new OslcSelection(field.getParentRecord().getOslcRecordIdentifier());
        OslcLoadHint loadHint = new OslcLoadHint(false);
        loadHint.addFieldHint(field.getOslcPropertyName(), new OslcLoadHint(loadRecords));

        TulTimer timer = new TulTimer();

        try {
            List<OslcRecordReference<Rq1RecordInterface>> result = OslcRq1Client.getOslcClient().loadRecords(selection, loadHint);
            logger.log(Level.FINER, "Load field {0}/{1}={2}", new Object[]{field.getParentRecord().getId(), field.getFieldName(), result.size()});
            assert (field.isNull() == false) : field.getParentRecord().getId() + ": " + field.toString();
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
        }

        ToolUsageLogger.addTime(LIST_RQ1_ELEMENT_TIMER, timer);
    }

    public void loadMappedReferenceList(Rq1DatabaseField_MappedReferenceList field, OslcLoadHint loadHint) {
        assert (field != null);
        assert (loadHint != null);

        logger.fine("Load field with hint" + field.getParentRecord().getId() + "/" + field.getFieldName());

        OslcSelection selection = new OslcSelection(field.getParentRecord().getOslcRecordIdentifier());
        OslcLoadHint loadHintForRecord = new OslcLoadHint(false);
        loadHintForRecord.addFieldHint(field.getOslcPropertyName(), loadHint);

        try {
            List<OslcRecordReference<Rq1RecordInterface>> result = OslcRq1Client.getOslcClient().loadRecords(selection, loadHintForRecord);
            logger.log(Level.FINER, "Load field with hint {0}/{1}={2}", new Object[]{field.getParentRecord().getId(), field.getFieldName(), result.size()});
        } catch (ConstraintViolationReportedByServer | RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
        }
    }

    public List<Rq1Reference> fullTextSearch(String searchText) {
        assert (searchText != null);
        assert (searchText.length() >= 2);

        try {
            return (buildFullTextResultList(OslcRq1Client.getOslcClient().executeFullTextSearch(searchText)));
        } catch (RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (new ArrayList<>(0));
        }

    }

    public List<Rq1Reference> fullTextSearch(String searchText, Rq1RecordType recordType) {
        assert (searchText != null);
        assert (searchText.length() >= 2);
        assert (recordType != null);

        try {
            return (buildFullTextResultList(OslcRq1Client.getOslcClient().executeFullTextSearch(searchText, recordType)));
        } catch (RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (null);
        }
    }

    private List<Rq1Reference> buildFullTextResultList(List<OslcFullTextSearchResultRecord> oslcList) {
        assert (oslcList != null);

        List<Rq1Reference> result = new ArrayList<>(oslcList.size());
        for (OslcFullTextSearchResultRecord oslcHit : oslcList) {

            //
            // Determine record type
            //
            Rq1RecordType recordType = Rq1RecordType.getRecordType(oslcHit.getDcType());

            //
            // Create hit object
            //
            Rq1QueryHit rq1Hit = new Rq1QueryHit(oslcHit.getOslcRecordReference(), recordType, oslcHit.getFullTextSearchTitle());

            result.add(new Rq1Reference(rq1Hit));
        }

        return (result);

    }

    static public class QueryResult {

        final private String title;
        final private List<Rq1Reference> records;

        public QueryResult(String title, List<Rq1Reference> records) {
            assert (title != null);
            assert (records != null);

            this.title = title;
            this.records = records;
        }

        public String getTitle() {
            return (title);
        }

        public List<Rq1Reference> getRecords() {
            return (records);
        }

    }

    public QueryResult executeStoredQuery(String queryId) {
        assert (queryId != null);

        OslcQueryResult oslcResult;
        try {
            oslcResult = OslcRq1Client.getOslcClient().executeStoredQuery(queryId);
        } catch (RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (null);
        }

        if (oslcResult == null) {
            return (null);
        }

        List<Rq1Reference> result = new ArrayList<>();
        for (OslcQueryResultRecord oslcHit : oslcResult.getRecords()) {

            //
            // Determine record type
            //
            Rq1RecordType recordType = Rq1RecordType.getRecordType(oslcHit.getRecordType());

            //
            // Create hit object
            //
            Rq1QueryHit rq1Hit = new Rq1QueryHit(oslcHit.getOslcRecordReference(), recordType, oslcHit.getTitle(), oslcHit.getFieldList());

            //
            // Add to result list
            //
            result.add(new Rq1Reference(rq1Hit));
        }

        return (new QueryResult(oslcResult.getTitle(), result));
    }

    public QueryResult executeFreeQuery(OslcSelection selection, Collection<String> requestedProperties) {
        assert (selection != null);
        assert (selection.isCriteriaSet() == true);
        assert (requestedProperties != null);

        OslcQueryResult oslcResult;
        try {
            oslcResult = OslcRq1Client.getOslcClient().executeFreeQuery(selection, requestedProperties);
        } catch (RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (null);
        }

        List<Rq1Reference> result = new ArrayList<>();
        for (OslcQueryResultRecord oslcHit : oslcResult.getRecords()) {

            //
            // Determine record type
            //
            Rq1RecordType recordType = Rq1RecordType.getRecordType(oslcHit.getRecordType());

            //
            // Create hit object
            //
            Rq1QueryHit rq1Hit = new Rq1QueryHit(oslcHit.getOslcRecordReference(), recordType, oslcHit.getTitle(), oslcHit.getFieldList());

            //
            // Add to result list
            //
            result.add(new Rq1Reference(rq1Hit));
        }

        return (new QueryResult(oslcResult.getTitle(), result));
    }

    public OslcUploadFileResult addAttachment(Rq1Subject rq1Subject, File file, String description) throws WriteToDatabaseRejected {
        assert (rq1Subject != null);
        assert (rq1Subject.existsInDatabase() == true);
        assert (file != null);
        assert (description != null);

        Map<String, String> parameter = new TreeMap<>();
        parameter.put("description", description);

        try {
            return (OslcRq1Client.getOslcClient().uploadFile(rq1Subject.getOslcRecordIdentifier(), file, parameter));
        } catch (RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (null);
        }
    }

    public void forward(Rq1ForwardableRecord record, Rq1Project targetProject, Rq1RecordInterface newAssignee) throws WriteToDatabaseRejected {
        List<OslcForwardableFieldI> additionalFields = new ArrayList<>();
        forward(record, targetProject, newAssignee, additionalFields);

    }

    public void forward(Rq1ForwardableRecord record, Rq1Project targetProject, Rq1RecordInterface newAssignee, OslcForwardableFieldI additionalField) throws WriteToDatabaseRejected {
        List<OslcForwardableFieldI> additionalFields = new ArrayList<>();
        additionalFields.add(additionalField);
        forward(record, targetProject, newAssignee, additionalFields);

    }

    public void forward(Rq1ForwardableRecord record, Rq1Project targetProject, Rq1RecordInterface newAssignee, List<? extends OslcForwardableFieldI> additionalFields) throws WriteToDatabaseRejected {
        assert (record != null);
        assert (record.existsInDatabase());
        assert (record.getOslcRecordIdentifier() != null);
        assert (targetProject != null);
        assert (targetProject.existsInDatabase());
        assert (targetProject.getOslcRecordIdentifier() != null);

        try {
            OslcRq1Client.getOslcClient().forward(record.getOslcRecordIdentifier(), record.getProjectField(), targetProject.getOslcRecordIdentifier(), newAssignee == null ? null : record.getAssigneeField(), newAssignee == null ? null : newAssignee.getOslcRecordIdentifier(), additionalFields);
        } catch (RestException ex) {
            Logger.getLogger(Rq1Client.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1Client.class.getName(), ex);
            EcvApplication.handleException(ex);
        }

        record.reload();
    }

    public void openInRq1(LinkType linkType, String rq1Id, Rq1RecordType recordType) {
        assert (linkType != null);
        assert (rq1Id != null);
        assert (rq1Id.isEmpty() == false);
        assert (recordType != null);

        OslcRq1Client.getOslcClient().openInRq1(linkType, rq1Id, recordType);
    }

    final public void createRecord(Rq1RecordInterface rq1Record, Rq1AttributeName[] fieldOrder) throws RestException, WriteToDatabaseRejected {
        assert (rq1Record != null);
        assert (rq1Record.existsInDatabase() == false);

        logger.fine("Create record " + rq1Record.getClass().getName());

        //
        // Convert write order
        //
        List<OslcPropertyName> propertyOrder = new ArrayList<>();
        if (fieldOrder != null) {
            for (Rq1AttributeName attribute : fieldOrder) {
                propertyOrder.add(attribute.getOslcName());
            }
        }

        //
        // Create record via OSLC in database
        //
        OslcRq1Client.getOslcClient().createRecord(rq1Record, propertyOrder);

        //
        // Add record to RQ1 record cache
        //
        Rq1RecordIndex.addRecord(rq1Record);
    }

    final public void saveRecord(Rq1RecordInterface rq1Record, Rq1AttributeName[] fieldOrder) throws RestException, WriteToDatabaseRejected {
        assert (rq1Record != null);
        assert (rq1Record.existsInDatabase() == true);

        logger.fine("Save record " + rq1Record.getClass().getName() + "/" + rq1Record.getId());

        //
        // Convert write order
        //
        List<OslcPropertyName> propertyOrder = new ArrayList<>();
        if (fieldOrder != null) {
            for (Rq1AttributeName attribute : fieldOrder) {
                propertyOrder.add(attribute.getOslcName());
            }
        }

        OslcRq1Client.getOslcClient().saveRq1Record(rq1Record, propertyOrder);
    }

}
