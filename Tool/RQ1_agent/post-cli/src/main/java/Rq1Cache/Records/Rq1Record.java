/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsRecord;
import DataStore.Exceptions.DsFieldContentFailure_InvalidValue;
import DataStore.Exceptions.DsFieldContentFailure_MandatoryField;
import DataStore.Exceptions.DsFieldContentFailure_ReadOnly;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.ConstraintViolationReportedByServer.FieldConstraintViolation;
import RestClient.Exceptions.ToolAccessException;
import RestClient.Exceptions.WriteToDatabaseRejected;
import OslcAccess.OslcFieldI;
import OslcAccess.OslcLoadHint;
import OslcAccess.OslcRecordIdentifier;
import OslcAccess.OslcRecordTypeI;
import OslcAccess.Rq1.OslcRq1ServerDescription.LinkType;
import OslcAccess.OslcWriteableFieldI;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Monitoring.Rq1WriteFailure;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1RecordDescription;
import Rq1Cache.Rq1RecordIndex;
import Rq1Data.Monitoring.Rq1RuleDescription;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.EcvUserMessage.MessageType;
import java.util.*;
import java.util.logging.Level;
import util.EcvApplication;

public abstract class Rq1Record extends DsRecord<Rq1FieldI> implements Rq1RecordInterface {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Rq1Record.class.getCanonicalName());

    final static public Rq1AttributeName ATTRIBUTE_OPERATION_MODE = new Rq1AttributeName("OperationMode");
    //
    final private Rq1RecordDescription recordDescription;
    private OslcRecordIdentifier oslcRecordIdentifier = null;
    //
    final private ArrayList<OslcFieldI> oslcFields = new ArrayList<>(20);
    final private ArrayList<OslcWriteableFieldI> writeableOslcFields = new ArrayList<>(20);
    private boolean fixedFieldsCreated = false;

    @SuppressWarnings("unchecked")
    protected Rq1Record(Rq1RecordDescription recordDescription) {
        assert (recordDescription != null);

        this.recordDescription = recordDescription;
    }

    @Override
    final public Rq1RecordDescription getRecordDescription() {
        return (recordDescription);
    }

    @Override
    public synchronized void setOslcRecordIdentifier(OslcRecordIdentifier oslcRecordIdentifier) {
        assert (this.oslcRecordIdentifier == null);
        assert (oslcRecordIdentifier != null);

        this.oslcRecordIdentifier = oslcRecordIdentifier;
    }

    @Override
    final public OslcRecordIdentifier getOslcRecordIdentifier() {
        return (oslcRecordIdentifier);
    }

    @Override
    public OslcRecordTypeI getOslcRecordType() {
        return (recordDescription.getRecordType());
    }

    @Override
    public boolean existsInDatabase() {
        return (oslcRecordIdentifier != null);
    }

    @Override
    public void loadCache(OslcLoadHint loadHint) {
        assert (loadHint != null);

        Rq1Client.client.loadRecordByIdentifier(oslcRecordIdentifier, loadHint);
    }

    @Override
    final public String getOslcShortTitle() {
        if (oslcRecordIdentifier != null) {
            return (oslcRecordIdentifier.getShortTitle());
        } else {
            return ("NewRecord");
        }
    }

    @Override
    final protected <T_NEWFIELD extends Rq1FieldI> T_NEWFIELD addField(T_NEWFIELD newField) {

        super.addField(newField);

        if (newField instanceof OslcWriteableFieldI) {
            writeableOslcFields.add((OslcWriteableFieldI) newField);
        }

        if (newField instanceof OslcAccess.OslcFieldI) {
            oslcFields.add((OslcAccess.OslcFieldI) newField);
        }

        return (newField);
    }

    private void createFixedValueFields() {
        if (fixedFieldsCreated == false) {
            fixedFieldsCreated = true;

            for (Rq1RecordDescription.FixedRecordValue fixedValue : recordDescription.getFixedRecordValues()) {
                if (fixedValue.isOnlyOneValueAllowed() == true) {
                    boolean fieldExistsAlready = false;
                    for (OslcFieldI oslcField : oslcFields) {
                        if (oslcField.getOslcPropertyName().equals(fixedValue.getFieldName()) == true) {
                            fieldExistsAlready = true;
                            break;
                        }
                    }
                    if (fieldExistsAlready == false) {
                        Rq1DatabaseField_Text field = new Rq1DatabaseField_Text(this, fixedValue.getFieldName());
                        field.setDataModelValue(fixedValue.getValue());
                        addField(field);
                    }
                }

            }
        }
    }

    @Override
    public String getId() {
        return (getOslcShortTitle());
    }

    @Override
    public void openInRq1(LinkType linkType) {
        assert (linkType != null);
        assert (oslcRecordIdentifier != null);
        Rq1Client.client.openInRq1(linkType, getOslcShortTitle(), recordDescription.getRecordType());
    }

    //--------------------------------------------------------------------------
    //
    // Reading from database
    //
    //--------------------------------------------------------------------------
    @Override
    final public List<OslcWriteableFieldI> getWriteableOslcFields() {
        createFixedValueFields();
        return (writeableOslcFields);
    }

    @Override
    final public Iterable<? extends OslcAccess.OslcFieldI> getOslcFields() {
        createFixedValueFields();
        return (oslcFields);
    }

    @Override
    public void startOslcValueSetting() {
        removeMarkers(successfullWriteRule);
        for (Rq1FieldI field : getFields()) {
            field.removeMarkers(successfullWriteRule);
        }
    }

    @Override
    public void endOslcValueSetting(boolean fieldContentChanged) {
        if (fieldContentChanged == true) {
            fireChange();
        }
    }

    //--------------------------------------------------------------------------
    //
    // Writing to database
    //
    //--------------------------------------------------------------------------
    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final public Rq1RuleDescription successfullWriteDescription = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Write to RQ1 database has to be successful.",
            "A failure is set on the element for which the write was not successful.\n"
            + "\n"
            + "The reason for the failure is described in the description of the failure.");

    final static public Rq1DataRule successfullWriteRule = new Rq1DataRule(successfullWriteDescription);

    @Override
    public synchronized boolean save(Rq1AttributeName[] fieldOrder) {
        createFixedValueFields();
        if (oslcRecordIdentifier == null) {
            return (createInDatabase(fieldOrder));
        } else {
            return (updateInDatabase(fieldOrder));
        }
    }

    /**
     * Protected to enable overwriting in sub classes.
     *
     * @param fieldOrder
     * @return
     */
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        assert (oslcRecordIdentifier == null);

        LOGGER.log(Level.FINER, "Create started: {0}", getId());
        try {
            Rq1Client.client.createRecord(this, fieldOrder);
            handleWriteSuccess();
            LOGGER.log(Level.FINER, "Create done: {0}", getId());
        } catch (WriteToDatabaseRejected ex) {
            handleWriteError(ex);
            return (false);
        } catch (Exception | Error ex) {
            handleWriteError(ex);
            return (false);
        }

        assert (oslcRecordIdentifier != null);
        assert (Rq1RecordIndex.getRecord_NoFetch(oslcRecordIdentifier) == this);

        // Change will be fired in OSLC interface after reading the created record.
        return (true);
    }

    private boolean updateInDatabase(Rq1AttributeName[] fieldOrder) {
        assert (oslcRecordIdentifier != null);

        boolean changeFound = false;
        synchronized (this) {
            for (OslcWriteableFieldI field : writeableOslcFields) {
                if (field.isOslcWritePending() == true) {
                    changeFound = true;
                    break;
                }

            }
        }
        if (changeFound == false) {
            return (true);
        }

        //
        // Change will be fired in OSLC interface after re-reading of the written record only, if anything was changed in the database.
        // Therefore, fire change here to ensure that the written changes are handled by all listeners.
        //
        fireChange();

        LOGGER.log(Level.FINER, "Save started: {0}", getId());
        try {
            Rq1Client.client.saveRecord(this, fieldOrder);
            handleWriteSuccess();
            LOGGER.log(Level.FINER, "Save done: {0}", getId());
            return (true);
        } catch (WriteToDatabaseRejected ex) {
            handleWriteError(ex);
            return (false);
        } catch (Exception | Error ex) {
            handleWriteError(ex);
            return (false);
        }

    }

    final protected void handleWriteSuccess() {
        removeMarkers(successfullWriteRule);
        for (Rq1FieldI field : getFields()) {
            field.removeMarkers(successfullWriteRule);
        }
    }

    final protected void handleWriteError(Throwable throwable) {

        for (Rq1FieldI field : getFields()) {
            field.removeMarkers(successfullWriteRule);
        }

        if (throwable instanceof ConstraintViolationReportedByServer) {
            handleWriteError_ConstraintViolationReportedByServer((ConstraintViolationReportedByServer) throwable);
            handleWriteError_WriteToDatabaseRejected((WriteToDatabaseRejected) throwable);
        } else if (throwable instanceof WriteToDatabaseRejected) {
            handleWriteError_WriteToDatabaseRejected((WriteToDatabaseRejected) throwable);
        } else if (throwable instanceof ToolAccessException) {
            handleWriteError_ToolAccessException((ToolAccessException) throwable);
        } else {
            handleWriteError_Unknown(throwable);
        }

    }

    private static final String VALIDATION_ERROR_KEY = "CRVSV0756E Validation error";

    protected void handleWriteError_ConstraintViolationReportedByServer(ConstraintViolationReportedByServer ex) {

        //
        // Add marker for field problems
        //
        for (FieldConstraintViolation invalidField : ex.getInvalidFields()) {
            for (Rq1FieldI field : getFields()) {
                if (field.getFieldName().equals(invalidField.getFieldName())) {
                    handleWriteError_setMarkerForInvalidField(field, invalidField.getMessage());
                }
            }
        }
        handleWriteError_ValidationErrors(ex);
    }

    protected void handleWriteError_ValidationErrors(ConstraintViolationReportedByServer ex) {

        //
        // Handle validation errors
        //
        if (ex.getDetailedMessageForUi().contains(VALIDATION_ERROR_KEY)) {
            List<String> validationErrors = new ArrayList<>();
            String lines[] = ex.getDetailedMessageForUi().split("\n");
            boolean nextLinesIsValidationError = false;
            for (String line : lines) {
                String trimedLine = line.trim();
                if (nextLinesIsValidationError == true) {
                    if (trimedLine.endsWith(",") == true) {
                        String lineWithoutComma = trimedLine.substring(0, trimedLine.length() - 1).trim();
                        validationErrors.add(lineWithoutComma);
                    } else {
                        validationErrors.add(trimedLine);
                        nextLinesIsValidationError = false;
                    }

                } else if (trimedLine.contains(VALIDATION_ERROR_KEY)) {
                    nextLinesIsValidationError = true;
                }
            }
            for (String validationError : validationErrors) {
                handleWriteValidationError(validationError);
            }

        }
    }

    protected void handleWriteError_setMarkerForInvalidField(Rq1FieldI field, String invalidFieldMessage) {
        assert (field != null);
        assert (invalidFieldMessage != null);

        if (invalidFieldMessage.contains("is mandatory; a value must be specified")) {
            field.addMarker(new DsFieldContentFailure_MandatoryField(successfullWriteRule, field.getFieldName()));

        } else if (invalidFieldMessage.contains("Cannot be modified because it is currently read-only.")) {
            field.addMarker(new DsFieldContentFailure_ReadOnly(successfullWriteRule, field.getFieldName()));

        } else if (invalidFieldMessage.contains("has values not permitted by its choice list")) {
            String title = "";
            String[] s = invalidFieldMessage.split("\n");
            if (s.length == 2) {
                title = s[1].trim();
                if (title.startsWith("\"") == true) {
                    title = title.substring(1);
                }
                if (title.endsWith("\"")) {
                    title = title.substring(0, title.length() - 1);
                }
            }
            if (title.isEmpty() == false) {
                field.addMarker(new DsFieldContentFailure_InvalidValue(title, successfullWriteRule, field.getFieldName()));
            } else {
                field.addMarker(new DsFieldContentFailure_InvalidValue(successfullWriteRule, field.getFieldName()));
            }
        }
    }

    protected void handleWriteValidationError(String validationError) {
    }

    private void handleWriteError_WriteToDatabaseRejected(WriteToDatabaseRejected ex) {

        setMarker(new Rq1WriteFailure(successfullWriteRule, "Write to RQ1 failed.", ex.getMessageForUi() + "\n\n" + ex.getDetailedMessageForUi()));

        StringBuilder b = new StringBuilder(100);
        b.append("Write to RQ1 failed.\n");
        b.append("\n");
        if (oslcRecordIdentifier != null) {
            b.append("RQ1-Reference: ");
            b.append(oslcRecordIdentifier.getRdfAbout()).append("\n");
            b.append("\n");
        }
        b.append("Error title:\n");
        b.append(ex.getMessageForUi()).append("\n");
        b.append("\n");
        b.append("Error details:\n");
        String temp = ex.getDetailedMessageForUi();
        if (temp.length() > 711) {
            b.append(temp.subSequence(0, 710));
        } else {
            b.append(temp);
        }
        EcvApplication.handleException(ex, b.toString(), "Write to RQ1 failed for record " + getId(), MessageType.WARNING_MESSAGE);
    }

    private void handleWriteError_ToolAccessException(ToolAccessException ex) {

        if (oslcRecordIdentifier != null) {
            LOGGER.log(Level.SEVERE, "Write failed for " + getId() + "-" + oslcRecordIdentifier.getRdfAbout(), ex);
            ToolUsageLogger.logError(Rq1Record.class.getCanonicalName(), ex);
        } else {
            LOGGER.log(Level.SEVERE, "Write failed for " + getId(), ex);
            ToolUsageLogger.logError(Rq1Record.class.getCanonicalName(), ex);
        }

        setMarker(new Rq1WriteFailure(successfullWriteRule, "Write to RQ1 failed.", ex.getMessage()));

        StringBuilder b = new StringBuilder(100);
        b.append("Write to RQ1 failed for record ").append(getId()).append("\n");
        b.append("\n");
        if (oslcRecordIdentifier != null) {
            b.append("RQ1-Reference:\n");
            b.append(oslcRecordIdentifier.getRdfAbout()).append("\n");
            b.append("\n");
        }
        b.append("Error title catched:\n");
        b.append(ex.getMessage());
        EcvApplication.handleException(ex, b.toString(), "Write to RQ1 failed for record " + getId(), MessageType.WARNING_MESSAGE);
    }

    private void handleWriteError_Unknown(Throwable ex) {

        if (oslcRecordIdentifier != null) {
            LOGGER.log(Level.SEVERE, "Write failed for " + getId() + "-" + oslcRecordIdentifier.getRdfAbout(), ex);
            ToolUsageLogger.logError(Rq1Record.class.getCanonicalName(), ex);
        } else {
            LOGGER.log(Level.SEVERE, "Write failed for " + getId(), ex);
            ToolUsageLogger.logError(Rq1Record.class.getCanonicalName(), ex);
        }

        setMarker(new Rq1WriteFailure(successfullWriteRule, "Write to RQ1 failed.", ex.getMessage() + "\n\nNo detailed error message available."));

        StringBuilder b = new StringBuilder(100);
        b.append("Write to RQ1 failed for record ").append(getId()).append("\n");
        b.append("\n");
        if (oslcRecordIdentifier != null) {
            b.append("RQ1-Reference:\n");
            b.append(oslcRecordIdentifier.getRdfAbout()).append("\n");
            b.append("\n");
        }
        b.append("Error title catched:\n");
        b.append(ex.getMessage()).append("\n");
        b.append("\n");
        b.append("No detailed error message available.\n");
        EcvApplication.handleException(ex, b.toString(), "Write to RQ1 failed for record " + getId(), MessageType.WARNING_MESSAGE);
    }

}
