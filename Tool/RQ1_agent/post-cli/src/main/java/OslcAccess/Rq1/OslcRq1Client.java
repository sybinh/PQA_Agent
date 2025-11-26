/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Rq1;

// * Einstellungen -> Erweitert -> Verschlüsselung -> Zertifikate anzeigen ->
// * Server Bosch-CA-DE Bosch-CA1-DE Bosch-CA-DE Als *.crt-Datei exportieren via
// * Button 'Exportieren...'.
// *
// * Keystore des JDK: C:\work\Java\jdk1.7.0_25\jre\lib\security\cacerts Programm
// * zum Bearbeiten des keystore: C:\work\Java\jdk1.7.0_25\jre\bin\keytool.exe
// * Aufruf: keytool.exe -import -file <filename> -alias <name> -keystore
// * <cacerts>
// * Default-Passwort: changeit
import ConfigurableRules.ConfigRule.Tracking.TrackingUsage;
import OslcAccess.OslcCommand;
import OslcAccess.OslcCommandExecutor;
import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.FieldNotFoundException;
import RestClient.Exceptions.NoLoginDataException;
import RestClient.Exceptions.OverwriteError;
import RestClient.Exceptions.OverwriteError.ChangedField;
import RestClient.Exceptions.ReferencedRecordDoesNotExistInDatabase;
import RestClient.Exceptions.ResponseException;
import RestClient.Exceptions.ToolAccessException;
import RestClient.Exceptions.WriteToDatabaseRejected;
import OslcAccess.OslcCommandExecutor.ResponseFormat;
import OslcAccess.OslcCriteriaSearch;
import OslcAccess.OslcDirectReferenceFieldI;
import OslcAccess.OslcDirectReferenceListFieldI;
import OslcAccess.OslcFieldI;
import OslcAccess.OslcFileDownload;
import OslcAccess.OslcFullTextSearch;
import OslcAccess.OslcFullTextSearchResultRecord;
import OslcAccess.OslcIndirectReferenceListFieldI;
import OslcAccess.OslcLoadHint;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import OslcAccess.OslcProperty;
import OslcAccess.OslcPropertyName;
import OslcAccess.OslcPropertyRecord;
import OslcAccess.OslcPropertySet;
import OslcAccess.OslcProtocolVersion;
import OslcAccess.OslcQueryResult;
import OslcAccess.OslcQueryResultRecord;
import OslcAccess.OslcRecordFactoryI;
import OslcAccess.OslcRecordI;
import OslcAccess.OslcRecordIdentifier;
import OslcAccess.OslcRecordPatternI;
import OslcAccess.OslcRecordReference;
import OslcAccess.OslcRecordTypeI;
import OslcAccess.OslcResponse;
import OslcAccess.OslcResponseRecordI;
import OslcAccess.OslcResponseRecord_Oslc10;
import OslcAccess.OslcResponseRecord_Oslc20;
import static OslcAccess.OslcCommandExecutor.repetitionIntervalForUserInterface;
import OslcAccess.OslcForwardableFieldI;
import OslcAccess.OslcResponseFactory;
import OslcAccess.Rq1.OslcRq1ServerDescription.LinkType;
import OslcAccess.OslcSelection;
import OslcAccess.OslcStoredQuerySearch;
import OslcAccess.OslcStringFieldI;
import OslcAccess.OslcUploadCommand;
import OslcAccess.OslcUploadFileResult;
import OslcAccess.OslcWriteableFieldI;
import OslcAccess.OverwriteDialog;
import RestClient.Exceptions.AuthorizationException;
import RestClient.Exceptions.NotFoundException;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestInternalException;
import RestClient.Exceptions.TemporaryServerError;
import RestClient.Exceptions.UnknownHostException;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList_Writeable;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1RecordFactory;
import Rq1Cache.Types.Rq1Reference;
import UiSupport.EcvUserMessage;
import UiSupport.EcvUserMessage.MessageType;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import util.EcvApplication;
import util.EcvApplication.ApplicationType;
import util.EcvLoginData;
import util.EcvXmlContainerElement;
import util.UiWorkerManager;

/**
 * Implements the OSLC client to connect to a RQ1 OSLC server.
 *
 * @author GUG2WI
 * @param <T_RECORD>
 */
public class OslcRq1Client<T_RECORD extends OslcRecordI> {

    private final static String OPERATION_MODE = "OperationMode";
    private final static String OPERATION_CONTEXT = "OperationContext";
    private final static String BELONGS_TO_PROJECT = "belongsToProject";

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(OslcRq1Client.class.getCanonicalName());

    final static private OslcRq1Client<Rq1RecordInterface> singletonClient = new OslcRq1Client<>(new Rq1RecordFactory());

    /**
     * Returns the singleton client.
     *
     * @return The client object.
     */
    static public OslcRq1Client<Rq1RecordInterface> getOslcClient() {
        return (singletonClient);
    }

    private EcvLoginData loginData = null;
    private OslcCommandExecutor executor = null;
    final private OslcRecordFactoryI<T_RECORD> recordFactory;

    private OslcResponse executeCommand(OslcCommand command) throws RestException, ConstraintViolationReportedByServer {
        connect();
        return (executor.executeCommand(command));
    }

    private void connect() throws RestException {
        if (executor != null) {
            return;
        }

        String originalTaskAction = UiWorkerManager.getMyTaskAction();
        UiWorkerManager.setMyTaskAction("Waiting for login data");

        loginData = EcvLoginManager.getFirstLoginData();
        boolean repeatConnect = true;
        int repetitionNumberForTemporaryServerError = 0;

        do {
            logger.log(Level.INFO, "Login started for user {0}", loginData.getLoginName());
            executor = new OslcCommandExecutor(loginData.getServerDescription().getRq1Server(), new Rq1AuthenticationProvider(loginData), new OslcResponseFactory());
            Date startDate = new Date();

            //
            // Build URL string for GET command
            //
            StringBuilder getUrl = new StringBuilder(100);
            getUrl.append(loginData.getServerDescription().getRq1Server().getOslcUrl());
            getUrl.append("record/");
            getUrl.append("?rcm.name=");
            getUrl.append(loginData.getLoginName());
            getUrl.append("&rcm.type=users");
            getUrl.append("&oslc.properties=fullname,email,phone,is_active,misc_info");
            getUrl.append("&oslc.pageSize=1");

            //
            // Execute get command to check if login is o.k.
            //
            try {
                OslcResponse connectResult = executor.performGetRequest(OslcProtocolVersion.OSLC_20, getUrl.toString(), ResponseFormat.TEXT_OR_XML);
                EcvLoginManager.setCurrentUserFullName(connectResult.getSolelyRecord().getFieldValue("fullname"));
                EcvApplication.setUserName(loginData.getLoginName());
                repeatConnect = false;

                // Tracking usage of User Assistant
                TrackingUsage.trackingUsage(loginData.getLoginName());

            } catch (ToolAccessException ex) {
                loginData = null;
                executor = null;
                EcvUserMessage.showMessageDialog(
                        "The tool is not authorized to access RQ1.",
                        "Connection denied.",
                        MessageType.INFORMATION_MESSAGE);
                logger.log(Level.WARNING, "The tool is not authorized to access RQ1. Connection denied." /* , ex */);
                throw (ex);

            } catch (AuthorizationException | NotFoundException ex) {
                loginData = EcvLoginManager.getLoginDataVariant();
                if (loginData == null) {
                    // Login failed for all possible login names
                    executor = null;
                    handleLoginError(ex);
                }
            } catch (UnknownHostException ex) {
                loginData = null;
                executor = null;
                if (EcvUserMessage.showConfirmDialog(
                        "Host not reachable. Please check network connection.\n\nRepeat login?",
                        "Host not reachable",
                        JOptionPane.YES_NO_OPTION,
                        MessageType.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
                    EcvUserMessage.showMessageDialog(
                            "Host not reachable. '" + originalTaskAction + "' canceled.",
                            "Host not reachable.",
                            MessageType.INFORMATION_MESSAGE);
                    logger.log(Level.WARNING, "Host not reachable.", ex);
                    throw (ex);
                }
            } catch (ConstraintViolationReportedByServer ex) {
                logger.log(Level.SEVERE, "ConstraintViolationReportedByServer during login.", ex);
                ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
                throw (new RestInternalException("ConstraintViolationReportedByServer during login.", ex));
            } catch (TemporaryServerError ex) {
                logger.log(Level.SEVERE, "TemporaryServerError during login.", ex);
                ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
                if (repetitionNumberForTemporaryServerError < repetitionIntervalForUserInterface.length) {
                    logger.severe("Repetition number " + repetitionNumberForTemporaryServerError + ", now sleeping for " + repetitionIntervalForUserInterface[repetitionNumberForTemporaryServerError] + " s");
                    try {
                        Thread.sleep(repetitionIntervalForUserInterface[repetitionNumberForTemporaryServerError] * 1000);
                    } catch (InterruptedException ex1) {

                    }
                } else {
                    logger.severe("Repetition number " + repetitionNumberForTemporaryServerError + ". Giving up.");
                    ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
                    throw (new RestInternalException("TemporaryServerError during login.", ex));
                }
                repetitionNumberForTemporaryServerError++;

            } catch (Throwable ex) {
                logger.log(Level.SEVERE, "Throwable during login.", ex);
                ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
                shutdown();
                throw (new RestInternalException("Throwable during login.", ex));
            }

            Date endDate = new Date();
            logger.log(Level.INFO, "Login done. Duration = {0} ms.", (endDate.getTime() - startDate.getTime()));

            if (repeatConnect == true) {
                loginData = EcvLoginManager.getNextLoginData();
            }

        } while (repeatConnect == true);

        EcvLoginManager.confirmSuccessFullLogin(loginData);
        UiWorkerManager.setMyTaskAction(originalTaskAction);
    }

    private void handleLoginError(RestException ex) throws RestException {
        assert (ex != null);
        if (ex instanceof NotFoundException) {
            ex = new AuthorizationException(((NotFoundException) ex).getAddressForUi());
        }
        if (EcvApplication.getApplicationType() != ApplicationType.UserInterface) {
            logger.log(Level.WARNING, "Login failed", ex);
            throw (ex);
        }
        if (EcvUserMessage.showConfirmDialog(
                "<html>"
                + "Login failed! Please check login name and password:<br>"
                + "<br>"
                + "<i>Note:<br>"
                + "- The password must not contain special characters like ä, ö, ü, ß</i><br><br>"
                + "Repeat login?"
                + "</html>",
                "Login failed",
                JOptionPane.YES_NO_OPTION,
                MessageType.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
            logger.log(Level.WARNING, "Login failed", ex);
            throw (ex);
        }
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdownHttpConnection();
            executor = null;
        }
    }

    static public OslcRecordTypeI getRecordType(String dcterms) {
        assert (dcterms != null);
        assert (dcterms.isEmpty() == false);

        return (singletonClient.recordFactory.getRecordType(dcterms));
    }

    /**
     * Creates a OSLC client.
     *
     * @param recordFactory The factory that shall be used to create records.
     */
    protected OslcRq1Client(OslcRecordFactoryI<T_RECORD> recordFactory) {
        assert (recordFactory != null);

        this.recordFactory = recordFactory;
    }

    private String getDbValueForPlainAccessField(OslcStringFieldI dbField, OslcResponseRecordI receivedRecord) throws ResponseException {
        assert (dbField != null);
        assert (receivedRecord != null);

        String dbValue = null;
        String fieldPathArray[] = dbField.getOslcPropertyName().split("\\.");

        switch (fieldPathArray.length) {
            case 1:
                try {
                dbValue = receivedRecord.getFieldValue(fieldPathArray[0]);
            } catch (FieldNotFoundException e) {
                if (dbField.isOptional() == true) {
                    dbValue = "";
                } else {
                    throw (e);
                }
            }
            break;
            case 2:
                try {
                dbValue = receivedRecord.getFieldValue(fieldPathArray[0], fieldPathArray[1]);
            } catch (FieldNotFoundException e) {
                if (dbField.isOptional() == true) {
                    dbValue = "";
                } else {
                    throw (e);
                }
            }
            break;
            default:
                throw (new Error("Field " + dbField.getOslcPropertyName() + " not supported here."));
        }
        if (dbValue == null) {
            throw (new Error("Field " + dbField.getOslcPropertyName() + " not found in result set"));
        }
        return (dbValue);
    }

    private void saveRq1Record_Internal(Rq1RecordInterface rq1Record, List<OslcPropertyName> writeOrder) throws RestException, WriteToDatabaseRejected {
        assert (rq1Record != null);
        assert (rq1Record.existsInDatabase() == true);
        assert (rq1Record.getOslcRecordIdentifier() != null);
        assert (writeOrder != null);

        //
        // Prepare read & write commands
        //
        List<OslcWriteableFieldI> fieldsToSave = new ArrayList<>(10);
        OslcCriteriaSearch getCommand = new OslcCriteriaSearch().setSelection(new OslcSelection().setRdfAbout(rq1Record.getOslcRecordIdentifier().getRdfAbout()));
        OslcRq1PutCommand putCommand = new OslcRq1PutCommand().setSelection(new OslcSelection().setRdfAbout(rq1Record.getOslcRecordIdentifier().getRdfAbout()));
        putCommand.setWriteOrder(writeOrder);

        //
        // Add property for RQ1 Operation mode to the post command
        //
        putCommand.addPropertyField(OPERATION_MODE, EcvApplication.getToolName() + " - Version " + EcvApplication.getToolVersionForLogging());
        putCommand.addPropertyField(OPERATION_CONTEXT, "PUT");

        //
        // Find fields changed by data model and add to read & write commands
        //
        synchronized (rq1Record) {
            for (OslcWriteableFieldI field : rq1Record.getWriteableOslcFields()) {
                if (field.isOslcWritePending() == true) {
                    fieldsToSave.add(field);
                    if (field instanceof OslcStringFieldI) {
                        putCommand.addPropertyField(field.getOslcPropertyName(), ((OslcStringFieldI) field).getOslcValue());
                        getCommand.addPropertyField(field.getOslcPropertyName());
                    } else if (field instanceof Rq1DatabaseField_Reference) {
                        Rq1Reference reference = ((Rq1DatabaseField_Reference) field).provideValueAsReferenceForDb();
                        if (reference != null) {
                            if (reference.getOslcRecordReference() == null) {
                                throw (new ReferencedRecordDoesNotExistInDatabase(field.getOslcPropertyName()));
                            } else {
                                putCommand.addPropertyField(field.getOslcPropertyName(), reference.getOslcRecordReference().getRdfAbout());
                                getCommand.addPropertyField(field.getOslcPropertyName());
                            }
                        } else {
                            putCommand.addPropertyField(field.getOslcPropertyName(), "");
                            getCommand.addPropertyField(field.getOslcPropertyName());
                        }
                    } else if (field instanceof Rq1DatabaseField_ReferenceList_Writeable) {
                        getCommand.addPropertyField(field.getOslcPropertyName());
                    } else {
                        throw (new Error("Unexpected class: " + field.getClass().getCanonicalName()));
                    }
                }

            }
        }

        //
        // No field changed. -> Nothing to save, quit method.
        //
        if (fieldsToSave.isEmpty() == true) {
            return;
        }

        //
        // Read current values from DB and compare with last value read from DB
        //
        OslcResponse response = executeCommand(getCommand);
        List<ChangedField> fieldsWithChangedDbValue = new ArrayList<>(10);
        for (OslcWriteableFieldI field : fieldsToSave) {
            if (field instanceof OslcStringFieldI) {
                String currentDbValue = getDbValueForPlainAccessField((OslcStringFieldI) field, response.getSolelyRecord());
                String oldDbValue = ((OslcStringFieldI) field).provideLastValueFromDbAsStringForDb();
                String newDbValue = ((OslcStringFieldI) field).getOslcValue();
                if (((OslcStringFieldI) field).equalDbValues(currentDbValue, oldDbValue) == false) {
                    fieldsWithChangedDbValue.add(new ChangedField(field, oldDbValue, currentDbValue, newDbValue));
                }
            } else if (field instanceof Rq1DatabaseField_Reference) {
                String currentDbValue = response.getSolelyRecord().getFieldRdfAbout(field.getOslcPropertyName());
                Rq1Reference oldDbValue = ((Rq1DatabaseField_Reference) field).provideLastValueFromDbAsReferenceForDb();
                Rq1Reference newDbValue = ((Rq1DatabaseField_Reference) field).provideValueAsReferenceForDb();
                boolean changed;
                if ((currentDbValue == null) && (oldDbValue != null)) {
                    changed = true;
                } else if ((currentDbValue != null) && (oldDbValue == null)) {
                    changed = true;
                } else if ((currentDbValue != null) && (oldDbValue != null) && (oldDbValue.equals(currentDbValue) == false)) {
                    changed = true;
                } else {
                    changed = false;
                }
                if (changed == true) {
                    fieldsWithChangedDbValue.add(new ChangedField(field,
                            oldDbValue == null ? "" : oldDbValue.getOslcRecordReference().getRdfAbout(),
                            currentDbValue == null ? "" : currentDbValue,
                            newDbValue == null ? "" : newDbValue.getOslcRecordReference().getRdfAbout()));
                }
            } else if (field instanceof Rq1DatabaseField_ReferenceList_Writeable) {
                List<String> currentDbValues = new ArrayList<>(0);
                String olscPropertyName = ((Rq1DatabaseField_ReferenceList_Writeable) field).getOslcPropertyName();
                List<Rq1Reference> valuesToDelete = ((Rq1DatabaseField_ReferenceList_Writeable) field).getValuesToDeleteAtSave();
                List<Rq1Reference> valuesToAdd = ((Rq1DatabaseField_ReferenceList_Writeable) field).getValuesToAddAtSave();

                // load current reference list from RQ1
                ((Rq1DatabaseField_ReferenceList_Writeable) field).provideValueAsReferenceListForDb();
                for (OslcResponseRecordI record : response.getRecords()) {
                    try {
                        List<OslcResponseRecordI> subrecordList = record.getSubRecords(olscPropertyName, true);
                        for (OslcResponseRecordI subrecord : subrecordList) {
                            String fieldRfdAbout = subrecord.getOslcRecordIdentifier().getRdfAbout();
                            currentDbValues.add(fieldRfdAbout);
                        }
                    } catch (ResponseException ex) {
                    }
                }

                // delete all references that the user want to delete
                for (Rq1Reference reference : valuesToDelete) {
                    String fieldRdfAbout = reference.getOslcRecordReference().getRdfAbout();
                    currentDbValues.remove(fieldRdfAbout);
                }

                // add all references that the user want to add
                for (Rq1Reference reference : valuesToAdd) {
                    String fieldRdfAbout = reference.getOslcRecordReference().getRdfAbout();
                    if (currentDbValues.contains(fieldRdfAbout) == false) {
                        currentDbValues.add(fieldRdfAbout);
                    }
                }

                putCommand.addPropertyField(field.getOslcPropertyName(), currentDbValues);
            } else {
                throw (new Error("Unknown class: " + field.getClass().getCanonicalName()));
            }
        }

        //
        // Check with user, if changes should be overwritten.
        //
        if (fieldsWithChangedDbValue.isEmpty() == false) {
            OverwriteError overwriteError = new OverwriteError(rq1Record);
            for (ChangedField field : fieldsWithChangedDbValue) {
                overwriteError.addField(field);
            }
            logger.warning(overwriteError.getDetailedMessageForUi());
            OverwriteDialog dialog = new OverwriteDialog(overwriteError);
            dialog.setVisible(true);
            if (dialog.getResult() == OverwriteDialog.DialogResult.CANCEL_WRITE) {
                throw (overwriteError);
            } else if (dialog.getResult() == OverwriteDialog.DialogResult.OVERWRITE) {
                // Simply continue with the write
            } else {
                throw (overwriteError);
            }
        }

        //
        // Write to DB
        //
        executeCommand(putCommand);

        //
        // Reset change state for written fields
        //
        for (OslcWriteableFieldI field : fieldsToSave) {
            if (field instanceof Rq1DatabaseField_ReferenceList_Writeable) {
                ((Rq1DatabaseField_ReferenceList_Writeable) field).reload();
            }
            field.setOslcWriteSuccessfull();
        }

        //
        // Reload record from database to update all fields automatically filled by RQ1.
        //
        loadRecordByRdf(rq1Record.getOslcRecordIdentifier().getRdfAbout(), rq1Record.getRecordDescription().getRecordType());

    }

    public void saveRq1Record(Rq1RecordInterface rq1Record, List<OslcPropertyName> writeOrder) throws RestException, WriteToDatabaseRejected {
        assert (rq1Record != null);
        assert (rq1Record.existsInDatabase() == true);
        assert (writeOrder != null);

        try {
            saveRq1Record_Internal(rq1Record, writeOrder);
        } catch (RestException | WriteToDatabaseRejected ex) {
            logger.log(Level.SEVERE, "Save failed for " + rq1Record.getOslcShortTitle(), ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }

    }

    private void createRecord_Internal(T_RECORD newRecord, List<OslcPropertyName> writeOrder) throws RestException, WriteToDatabaseRejected {
        assert (newRecord != null);
        assert (writeOrder != null);

        //
        // Set record type and fixed field values
        //
        OslcRq1PostCommand postCommand = new OslcRq1PostCommand(newRecord.getOslcRecordType());
        postCommand.setWriteOrder(writeOrder);

        //
        // Add property for RQ1 Operation mode to the post command
        //
        postCommand.addPropertyField(OPERATION_MODE, EcvApplication.getToolName() + " - Version " + EcvApplication.getToolVersionForLogging());
        postCommand.addPropertyField(OPERATION_CONTEXT, "POST");

        //
        // Set and check field values
        //
        synchronized (newRecord) {
            //
            // Check and add field values
            //
            // Do nothing for reference lists and mapped reference lists, because they are stored in the counterpart.
            //
            for (OslcWriteableFieldI field : newRecord.getWriteableOslcFields()) {
                String oslcPropertyName = field.getOslcPropertyName();
                if (field.isOslcWritePending() == true) {
                    if (field instanceof OslcDirectReferenceFieldI) {
                        //
                        // Ensure that referenced records exists in the database and add the existing one.
                        //
                        Rq1Reference reference = ((Rq1DatabaseField_Reference) field).provideValueAsReferenceForDb();
                        if (reference != null) {
                            Rq1RecordInterface referencedRecord = reference.getRecord();
                            if (referencedRecord != null) {
                                // TODO: Handle references for records not loaded yet.
                                if (referencedRecord.existsInDatabase() == false) {
                                    throw (new ReferencedRecordDoesNotExistInDatabase(oslcPropertyName));
                                } else {
                                    postCommand.addPropertyField(oslcPropertyName, referencedRecord.getOslcRecordIdentifier().getRdfAbout());
                                }
                            }
                        }

                    } else if (field instanceof OslcStringFieldI) {
                        //
                        // Add all non empty fields
                        //
                        String value = ((OslcStringFieldI) field).getOslcValue();
                        if ((value != null) && (value.isEmpty() == false)) {
                            postCommand.addPropertyField(oslcPropertyName, value);
                        }
                    } else if (field instanceof Rq1DatabaseField_ReferenceList_Writeable) {
                        List<Rq1Reference> references = ((Rq1DatabaseField_ReferenceList_Writeable) field).provideValueAsReferenceListForDb();
                        if (references != null && references.isEmpty() == false) {
                            List<String> properties = new ArrayList<>(0);
                            for (Rq1Reference reference : references) {
                                if (reference.getOslcRecordReference() == null) {
                                    throw (new ReferencedRecordDoesNotExistInDatabase(field.getOslcPropertyName()));
                                } else {
                                    properties.add(reference.getOslcRecordReference().getRdfAbout());
                                }
                            }
                            postCommand.addPropertyField(field.getOslcPropertyName(), properties);
                        } else {
                            postCommand.addPropertyField(field.getOslcPropertyName(), "");
                        }
                    } else {
                        throw (new Error("Unknown class: " + field.getClass().getCanonicalName()));
                    }
                }
            }

        }

        //
        // Create record in DB by executing POST command.
        //
        OslcResponse postReponse = executeCommand(postCommand);

        //
        // Get reference for new record
        //
        String rdfAbout = postReponse.getResponseHeader("Location");
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);

        //
        // Get oslcShortTitle via GET command
        // ToDo: gug2wi - Prüfen, ob wie mit diesem GET command schon alle veränderten Felder lesen können.
        //
        OslcCriteriaSearch getCommand = new OslcCriteriaSearch().setSelection(new OslcSelection().setRdfAbout(rdfAbout));
        getCommand.addPropertyField(newRecord.getWriteableOslcFields().iterator().next().getOslcPropertyName()); // Use any field to get a result set.
        OslcRq1Response getResponse = new OslcRq1Response(executeCommand(getCommand));

        //
        // Set database reference
        //
        newRecord.setOslcRecordIdentifier(getResponse.getOslcRecordReference());

        //
        // Add record to cache
        //
        recordFactory.addRecord(newRecord);

        //
        // Reload record from database to update all fields automatically filled by RQ1.
        //
        loadRecordByRdf(newRecord.getOslcRecordIdentifier().getRdfAbout(), newRecord.getOslcRecordType());
    }

    /**
     * Creates a new record in the RQ1 database and adds it to the cache.
     *
     * @param newRecord OslcRecordI to create. The record must not yet exist in
     * the database.
     * @param writeOrder
     * @throws RestClient.Exceptions.RestException
     * @throws WriteToDatabaseRejected
     */
    final public void createRecord(T_RECORD newRecord, List<OslcPropertyName> writeOrder) throws RestException, WriteToDatabaseRejected {
        assert (newRecord != null);
        assert (writeOrder != null);

        try {
            createRecord_Internal(newRecord, writeOrder);
        } catch (RestException | WriteToDatabaseRejected ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }
    }

    synchronized public void openInRq1(LinkType linkType, String rq1Id, OslcRecordTypeI recordType) {
        assert (linkType != null);
        assert (rq1Id != null);
        assert (rq1Id.isEmpty() == false);
        assert (recordType != null);

        //
        // Examples for Links to IRM and Issue:
        // https://rb-dgsrq1.de.bosch.com/cqweb/restapi/RQ1_PRODUCTIVE/RQONE/RECORD/56608178?format=HTML&noframes=true&recordType=Issuereleasemap
        // https://rb-dgsrq1.de.bosch.com/cqweb/restapi/RQ1_PRODUCTIVE/RQONE/RECORD/RQONE00187379?format=HTML&noframes=true&recordType=Issue
        //
        try {
            connect();
        } catch (RestException ex) {
            logger.log(Level.SEVERE, "Open " + rq1Id + " in RQ1.", ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
        }

        String url = loginData.getServerDescription().getRq1Server().buildUrl(linkType, rq1Id, recordType.getOslcType());
        try {
            Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (URISyntaxException | IOException ex) {
            logger.log(Level.SEVERE, "Open " + rq1Id + " in RQ1.", ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
        }

    }

    synchronized public void browse(String url) {
        assert (url != null);
        assert (url.isEmpty() == false);

        try {
            Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (URISyntaxException | IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
        }
    }

    synchronized public EcvXmlContainerElement loadXmlFile(String rdfAbout) throws RestException {
        try {
            return loadXmlFile_Internal(rdfAbout);
        } catch (RestException ex) {
            logger.log(Level.SEVERE, "Unexpected RestException. rdfAbout:" + rdfAbout, ex);
            logger.severe("rdfAbout: " + rdfAbout);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        } catch (ConstraintViolationReportedByServer ex) {
            logger.log(Level.SEVERE, "Unexpected constraint error. rdfAbout:" + rdfAbout, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            RestInternalException ie = new RestInternalException("Unexpected constraint error.", ex);
            throw (ie);
        }
    }

    synchronized private EcvXmlContainerElement loadXmlFile_Internal(String rdfAbout) throws RestException, ConstraintViolationReportedByServer {
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);

        OslcCriteriaSearch getCommand = new OslcCriteriaSearch()
                .setSelection(new OslcSelection().setRdfAbout(rdfAbout));

        OslcRq1Response queryResult = new OslcRq1Response(executeCommand(getCommand));

        return queryResult.getXmlContent();
    }

    synchronized public List<byte[]> loadFileContent(String rdfAbout) throws RestException {
        try {
            return (loadFileContent_Internal(rdfAbout));
        } catch (RestException ex) {
            logger.log(Level.SEVERE, "Unexpected RestException. rdfAbout:" + rdfAbout, ex);
            logger.severe("rdfAbout: " + rdfAbout);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        } catch (ConstraintViolationReportedByServer ex) {
            logger.log(Level.SEVERE, "Unexpected constraint error. rdfAbout:" + rdfAbout, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            RestInternalException ie = new RestInternalException("Unexpected constraint error.", ex);
            throw (ie);
        }
    }

    synchronized private List<byte[]> loadFileContent_Internal(String rdfAbout) throws RestException, ConstraintViolationReportedByServer {
        assert (rdfAbout != null);
        assert (rdfAbout.isEmpty() == false);

        OslcFileDownload getCommand = new OslcFileDownload(rdfAbout);
        OslcResponse queryResult = executeCommand(getCommand);
        return (queryResult.getResponseInBinaryFormat());
    }

    //------------------------------------------------------------------------------------------------
    //
    // New interface. Will replace the old one.
    //
    //------------------------------------------------------------------------------------------------
    public void loadLoginUser() throws NoLoginDataException, RestInternalException, RestException {
        try {
            connect();
        } catch (NoLoginDataException ex) {
            throw (ex);
        } catch (RestException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }
    }

    /**
     * Loads the record for the given id (e.g. RQ1-ID for the RQ1 database).
     *
     * @param id Id of the wanted record.
     * @return The loaded record, if a matching record was found in the
     * database. Otherwise null.
     * @throws RestClient.Exceptions.RestException
     * @throws RestClient.Exceptions.ConstraintViolationReportedByServer
     */
    final public T_RECORD loadRecordById(String id) throws RestException, ConstraintViolationReportedByServer {
        assert (id != null);
        assert (id.isEmpty() == false);

        T_RECORD t_record = (loadRecordTypeAndRecord(new OslcSelection().setName(id), new OslcLoadHint(true)));
        return t_record;
    }

    /**
     * Loads the record for the given RDF-About string.
     *
     * @param rdf RDF-About string of the wanted record.
     * @param recordType
     * @return The loaded record, if a matching record was found in the
     * database. Otherwise null.
     * @throws RestClient.Exceptions.RestException
     * @throws RestClient.Exceptions.ConstraintViolationReportedByServer
     */
    final public T_RECORD loadRecordByRdf(String rdf, OslcRecordTypeI recordType) throws RestException, ConstraintViolationReportedByServer {
        assert (rdf != null);
        assert (rdf.isEmpty() == false);
        assert (recordType != null);

        OslcSelection selection = new OslcSelection();
        selection.setRdfAbout(rdf);
        selection.setRecordType(recordType);
        T_RECORD t_record = (loadRecordTypeAndRecord(selection, new OslcLoadHint(true)));
        return t_record;
    }

    /**
     * Loads one record which is defined by the given OslcSelection object.
     *
     * @param selection
     * @param loadHint Hints for loading the record.
     * @return
     */
    private T_RECORD loadRecordTypeAndRecord(OslcSelection selection, OslcLoadHint loadHint) throws RestException, ConstraintViolationReportedByServer {

        assert (selection != null);
        assert (selection.isCriteriaSet() == true);
        assert (loadHint != null);

        if (selection.getRecordType() == null) {
            //
            // Determine record type by reading dcterms:type from OSLC server
            //
            OslcRecordTypeI recordType;
            try {
                OslcCriteriaSearch getCommand = new OslcCriteriaSearch().setSelection(selection).addPropertyField("dcterms:type");
                OslcRq1Response queryResult = new OslcRq1Response(executeCommand(getCommand));
                recordType = queryResult.getOslcRecordReference().getRecordType();
            } catch (NoLoginDataException ex) {
                throw (ex);
            } catch (NotFoundException ex) {
                logger.log(Level.WARNING, "NotFoundException", ex);
                throw (ex);
            } catch (RestException ex) {
                logger.log(Level.SEVERE, "RestException", ex);
                ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
                throw (ex);
            } catch (ConstraintViolationReportedByServer ex) {
                logger.log(Level.SEVERE, "ConstraintViolationReportedByServer", ex);
                ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
                RestInternalException ie = new RestInternalException("Unexpected constraint error.", ex);
                throw (ie);
            }

            //
            // Add record type to selection
            //
            selection.setRecordType(recordType);

        }

        List<OslcRecordReference<T_RECORD>> resultList = loadRecords(selection, loadHint);
        assert (resultList.size() == 1);

        return (resultList.get(0).getRecord());
    }

    final public T_RECORD loadRecordByIdentifier(OslcRecordIdentifier identifier) throws RestException, ConstraintViolationReportedByServer {
        assert (identifier != null);
        T_RECORD t_record = (loadRecordTypeAndRecord(new OslcSelection(identifier), new OslcLoadHint(true)));
        return t_record;
    }

    final public T_RECORD loadRecordByIdentifier(OslcRecordIdentifier identifier, OslcLoadHint loadHint) throws RestException, ConstraintViolationReportedByServer {
        assert (identifier != null);
        assert (loadHint != null);

        return (loadRecordTypeAndRecord(new OslcSelection(identifier), loadHint));
    }

    final public List<OslcRecordReference<T_RECORD>> loadRecords(OslcSelection selection, OslcLoadHint loadHint) throws RestException, ConstraintViolationReportedByServer {
        assert (selection != null);
        assert (loadHint != null);
        return (loadRecords(selection, loadHint, OslcProtocolVersion.OSLC_20));
    }

    /**
     * Loads the record for the given OSLC identifier.
     *
     * @param selection
     * @param loadHint Hints for loading the record.
     * @param oslcProtocolVersion
     * @return The reference to the loaded record, if a matching record was
     * found in the database. Otherwise null.
     * @throws RestClient.Exceptions.RestException
     * @throws RestClient.Exceptions.ConstraintViolationReportedByServer
     */
    final public List<OslcRecordReference<T_RECORD>> loadRecords(OslcSelection selection, OslcLoadHint loadHint, OslcProtocolVersion oslcProtocolVersion) throws RestException, ConstraintViolationReportedByServer {
        assert (selection != null);
        assert (selection.getRecordType() != null);
        assert (loadHint != null);
        assert (oslcProtocolVersion != null);

        //
        // Create get command 
        //
        OslcCriteriaSearch getCommand = new OslcCriteriaSearch(selection);
        getCommand.setProtocolVersion(oslcProtocolVersion);

        //
        // Create Property Set for top records
        //
        OslcPropertySet propertySet = createPropertySetForRecord(selection.getRecordType(), loadHint);
        if (propertySet.isEmpty() == false) {
            getCommand.setProperties(propertySet);
        }

        //
        // Execute get command
        //
        OslcResponse queryResult;
        try {
            queryResult = executeCommand(getCommand);
        } catch (RestException | ConstraintViolationReportedByServer ex) {
            logger.log(Level.SEVERE, "RestException | ConstraintViolationReportedByServer", ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }

        //
        // 
        //
        List<OslcRecordReference<T_RECORD>> result = new ArrayList<>();
        try {
            for (OslcResponseRecordI receivedRecord : queryResult.getRecords()) {
                T_RECORD record = setDataForRecord(receivedRecord, loadHint);
                if (record == null) {
                    RestInternalException ie = new RestInternalException("loadRecords", "No matching class found for record. - " + receivedRecord.getOslcRecordIdentifier().toString());
                    EcvApplication.handleException(ie);
                    logger.log(Level.SEVERE, "setDataForRecord() failed.", ie);
                    ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ie);
                } else {
                    result.add(new OslcRecordReference<T_RECORD>(receivedRecord.getOslcRecordIdentifier(), record));
                }
            }
        } catch (ResponseException ex) {
            logger.log(Level.SEVERE, "ResponseException", ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }
        return (result);
    }

    private OslcPropertySet createPropertySetForRecord(OslcRecordTypeI recordType, OslcLoadHint loadHint) {
        assert (recordType != null);
        assert (loadHint != null);

        boolean isAtLeastOneFieldPropertySet = false;

        OslcPropertySet propertySet = new OslcPropertySet();

        OslcRecordPatternI recordPattern = recordFactory.getPattern(recordType);
        assert (recordPattern != null);

        //
        // Add properties according to fields in record pattern.
        //
        for (OslcFieldI field : recordPattern.getOslcFields()) {

            if (field instanceof OslcStringFieldI) {
                //
                // Handle string field
                //
                if (loadHint.isLoadRecordContentActive() == true) {
                    propertySet.addPropertyField(field.getOslcPropertyName());
                    isAtLeastOneFieldPropertySet = true;
                }

            } else if (field instanceof OslcDirectReferenceFieldI) {
                //
                // Handle reference field
                //
                OslcLoadHint fieldHint = loadHint.getHintForField(field.getOslcPropertyName());
                if (fieldHint != null) {
                    propertySet.addProperty(createPropertyForDirectReferenceField((OslcDirectReferenceFieldI) field, fieldHint));
                    isAtLeastOneFieldPropertySet = true;
                } else if (loadHint.isLoadRecordContentActive() == true) {
                    propertySet.addProperty(new OslcPropertyRecord(field.getOslcPropertyName()).addPropertyField("dcterms:type"));
                    isAtLeastOneFieldPropertySet = true;
                }

            } else if (field instanceof OslcDirectReferenceListFieldI) {
                //
                // Handle reference list field
                //
                OslcLoadHint fieldHint = loadHint.getHintForField(field.getOslcPropertyName());
                if (fieldHint != null) {
                    propertySet.addProperty(createPropertyForDirectReferenceListField((OslcDirectReferenceListFieldI) field, fieldHint));
                    isAtLeastOneFieldPropertySet = true;
                }

            } else if (field instanceof OslcIndirectReferenceListFieldI) {
                //
                // Handle reference list field
                //
                OslcLoadHint fieldHint = loadHint.getHintForField(field.getOslcPropertyName());
                if (fieldHint != null) {
                    propertySet.addProperty(createPropertyForIndirectReferenceListField((OslcIndirectReferenceListFieldI) field, fieldHint));
                    isAtLeastOneFieldPropertySet = true;
                }

            } else {
                throw (new Error("Unexpected field type: " + field.getClass().getCanonicalName()));
            }

        }

        //
        // Add property for type, if no other plain field is loaded.
        //
        if (isAtLeastOneFieldPropertySet == false) {
            propertySet.addPropertyField("dcterms:type");
        }

        return (propertySet);
    }

    private OslcProperty createPropertyForDirectReferenceField(OslcDirectReferenceFieldI field, OslcLoadHint loadHintForReferencedRecord) {
        assert (field != null);
        assert (loadHintForReferencedRecord != null);

        OslcPropertyRecord propertyRecord = new OslcPropertyRecord(field.getOslcPropertyName());

        propertyRecord.setPropertySet(createPropertySetForRecord(field.getReferencedRecordType(), loadHintForReferencedRecord));

        return (propertyRecord);
    }

    private OslcProperty createPropertyForDirectReferenceListField(OslcDirectReferenceListFieldI field, OslcLoadHint loadHintForReferencedRecord) {
        assert (field != null);
        assert (loadHintForReferencedRecord != null);

        OslcPropertyRecord propertyRecord = new OslcPropertyRecord(field.getOslcPropertyName());

        propertyRecord.setPropertySet(createPropertySetForRecord(field.getReferencedRecordType(), loadHintForReferencedRecord));

        return (propertyRecord);
    }

    private OslcProperty createPropertyForIndirectReferenceListField(OslcIndirectReferenceListFieldI field, OslcLoadHint loadHintForIndirectReferencedRecord) {
        assert (field != null);
        assert (loadHintForIndirectReferencedRecord != null);

        //
        // Create property for indirect referenced record
        // 
        OslcPropertyRecord propertyRecordIndirect = new OslcPropertyRecord(field.getIndirectOslcPropertyName());
        propertyRecordIndirect.setPropertySet(createPropertySetForRecord(field.getIndirectReferencedRecordType(), loadHintForIndirectReferencedRecord));

        //
        // Create property for direct referenced record
        // 
        OslcPropertyRecord propertyRecordDirect = new OslcPropertyRecord(field.getOslcPropertyName());
        propertyRecordDirect.setPropertySet(createPropertySetForRecord(field.getReferencedRecordType(), new OslcLoadHint(loadHintForIndirectReferencedRecord.isLoadRecordContentActive())));

        //
        // Add indirect to direct
        //
        propertyRecordDirect.addProperty(propertyRecordIndirect);

        return (propertyRecordDirect);
    }

    @SuppressWarnings("unchecked")
    private T_RECORD setDataForRecord(OslcResponseRecordI responseRecord, OslcLoadHint loadHint) throws ResponseException {
        assert (responseRecord != null);
        assert (loadHint != null);

        //
        // Get object for record
        //
        OslcRecordPatternI recordPattern;

        if (loadHint.isDependsOnSubRecords() == true) {
            //
            // Use record pattern for loading
            //
            recordPattern = recordFactory.getPattern(responseRecord.getOslcRecordIdentifier().getRecordType());
        } else {
            //
            // Use final record for loading
            //
            recordPattern = recordFactory.getRecord(responseRecord);
        }

        //
        // Stop here, if no record or pattern was found
        //
        if (recordPattern == null) {
            logger.severe("No record pattern found.");
            logger.log(Level.SEVERE, "Record type: {0}", responseRecord.getOslcRecordIdentifier().getRecordType());
            return (null);
        }

        //
        // Handle plain fields
        //
        if (loadHint.isLoadRecordContentActive() == true) {
            //
            // Set plain field values
            //
            if (recordPattern instanceof OslcRecordI) {
                ((OslcRecordI) recordPattern).startOslcValueSetting();
            }
            boolean fieldContentChanged = false;
            for (OslcFieldI field : recordPattern.getOslcFields()) {
                if (field instanceof OslcStringFieldI) {
                    fieldContentChanged |= setDataForStringField(responseRecord, (OslcStringFieldI) field);
                } else if (field instanceof OslcDirectReferenceFieldI) {
                    fieldContentChanged |= setDataForDirectReferenceField(responseRecord, (OslcDirectReferenceFieldI<T_RECORD>) field, null);
                } else if (field instanceof OslcDirectReferenceListFieldI) {
                    // O.k. - Nothing to do here.
                } else if (field instanceof OslcIndirectReferenceListFieldI) {
                    // O.k. - Nothing to do here.
                } else {
                    throw (new Error("Unknown field type: " + field.getClass().getCanonicalName()));
                }

            }
            if (recordPattern instanceof OslcRecordI) {
                ((OslcRecordI) recordPattern).endOslcValueSetting(fieldContentChanged);
                recordFactory.addRecord((T_RECORD) recordPattern);
            }
        }

        //
        // Handle reference list fields
        //
        for (OslcFieldI field : recordPattern.getOslcFields()) {
            if (field instanceof OslcStringFieldI) {
                // O.k. - Nothing to do here.
            } else if (field instanceof OslcDirectReferenceFieldI) {
                OslcLoadHint fieldHint = loadHint.getHintForField(field.getOslcPropertyName());
                if (fieldHint != null) {
                    setDataForDirectReferenceField(responseRecord, (OslcDirectReferenceFieldI<T_RECORD>) field, fieldHint);
                }
            } else if (field instanceof OslcDirectReferenceListFieldI) {
                //
                // Handle direct reference list field
                //
                OslcLoadHint fieldHint = loadHint.getHintForField(field.getOslcPropertyName());
                if (fieldHint != null) {
                    setDataForDirectReferenceListField(responseRecord, (OslcDirectReferenceListFieldI<T_RECORD>) field, fieldHint);
                }

            } else if (field instanceof OslcIndirectReferenceListFieldI) {
                //
                // Handle indirect reference list field
                //
                OslcLoadHint fieldHint = loadHint.getHintForField(field.getOslcPropertyName());
                if (fieldHint != null) {
                    setDataForIndirectReferenceListField(responseRecord, (OslcIndirectReferenceListFieldI<T_RECORD, T_RECORD>) field, fieldHint);
                }

            } else {
                throw (new Error("Unknown field type: " + field.getClass().getCanonicalName()));
            }

        }

        if (recordPattern instanceof OslcRecordI) {
            return ((T_RECORD) recordPattern);
        } else {
            //
            // Create record and copy field values from pattern to record
            //
            T_RECORD finalRecord = recordFactory.getRecord(responseRecord);
            if (finalRecord == null) {
                return (null);
            }
            Map<String, OslcFieldI> finalFieldMap = new TreeMap<>();
            for (OslcFieldI finalField : finalRecord.getOslcFields()) {
                finalFieldMap.put(finalField.getOslcPropertyName(), finalField);
            }

            finalRecord.startOslcValueSetting();
            boolean fieldContentChanged = false;
            for (OslcFieldI patternField : recordPattern.getOslcFields()) {
                OslcFieldI finalField = finalFieldMap.get(patternField.getOslcPropertyName());

                //
                // The final record may not have all fields from the pattern.
                //
                if (finalField != null) {
                    if (patternField instanceof OslcStringFieldI) {
                        assert (finalField instanceof OslcStringFieldI) : patternField.getOslcPropertyName();
                        fieldContentChanged |= ((OslcStringFieldI) finalField).copyOslcValue((OslcStringFieldI) patternField);

                    } else if (patternField instanceof OslcDirectReferenceFieldI) {
                        assert (finalField instanceof OslcDirectReferenceFieldI) : patternField.getOslcPropertyName();
                        fieldContentChanged |= ((OslcDirectReferenceFieldI) finalField).copyOslcValue((OslcDirectReferenceFieldI) patternField);

                    } else if (patternField instanceof OslcDirectReferenceListFieldI) {
                        assert (finalField instanceof OslcDirectReferenceListFieldI) : patternField.getOslcPropertyName();
                        fieldContentChanged |= ((OslcDirectReferenceListFieldI) finalField).copyOslcValue((OslcDirectReferenceListFieldI) patternField);

                    } else if (patternField instanceof OslcIndirectReferenceListFieldI) {
                        assert (finalField instanceof OslcIndirectReferenceListFieldI) : patternField.getOslcPropertyName();
                        fieldContentChanged |= ((OslcIndirectReferenceListFieldI) finalField).copyOslcValue((OslcIndirectReferenceListFieldI) patternField);

                    } else {
                        throw (new Error("Unknown field type: " + patternField.getClass().getCanonicalName()));
                    }
                }

            }
            finalRecord.endOslcValueSetting(fieldContentChanged);

            recordFactory.addRecord(finalRecord);

            return (finalRecord);
        }
    }

    private boolean setDataForStringField(OslcResponseRecordI responseRecord, OslcStringFieldI field) throws ResponseException {
        assert (responseRecord != null);
        assert (field != null);

        try {
            String value = responseRecord.getFieldValueByPath(field.getOslcPropertyName());
            return (field.setOslcValue(value));
        } catch (FieldNotFoundException ex) {
            boolean result = field.setOslcValue("");
            if (field.isOptional() == false) {
                throw (ex);
            }
            return (result);
        } catch (ResponseException ex) {
            logger.log(Level.WARNING, "Set value failed. Property name = " + field.getOslcPropertyName(), ex);
            throw (ex);
        }
    }

    private boolean setDataForDirectReferenceField(OslcResponseRecordI responseRecord, OslcDirectReferenceFieldI<T_RECORD> field, OslcLoadHint loadHintForReferencedRecord) throws ResponseException {
        assert (responseRecord != null);
        assert (field != null);

        List<OslcResponseRecordI> subRecordList = null;
        try {
            subRecordList = responseRecord.getSubRecords(field.getOslcPropertyName(), true);
        } catch (FieldNotFoundException ex) {
            boolean result = field.setOslcValue(null);
            if (field.isOptional() == false) {
                throw (ex);
            }
            return (result);
        }

        switch (subRecordList.size()) {
            case 0:
                return (field.setOslcValue(null));
            case 1:
                if ((loadHintForReferencedRecord != null) && (loadHintForReferencedRecord.isLoadRecordContentActive() == true)) {
                    T_RECORD record = setDataForRecord(subRecordList.get(0), loadHintForReferencedRecord);
                    return (field.setOslcValue(new OslcRecordReference<T_RECORD>(subRecordList.get(0).getOslcRecordIdentifier(), record)));
                } else {
                    return (field.setOslcValue(new OslcRecordReference<T_RECORD>(subRecordList.get(0).getOslcRecordIdentifier())));
                }
            default:
                throw (new Error("Error for field " + field.getOslcPropertyName() + ": Record list = " + subRecordList.size()));
        }
    }

    private void setDataForDirectReferenceListField(OslcResponseRecordI responseRecord, OslcDirectReferenceListFieldI<T_RECORD> field, OslcLoadHint loadHintForReferencedRecord) throws ResponseException {
        assert (responseRecord != null);
        assert (field != null);
        assert (loadHintForReferencedRecord != null);

        List<OslcResponseRecordI> subRecordList = null;
        List<OslcRecordReference<T_RECORD>> referenceList = new ArrayList<>();

        try {
            subRecordList = responseRecord.getSubRecords(field.getOslcPropertyName(), true);
        } catch (FieldNotFoundException ex) {
            if (field.isOptional() == false) {
                throw (ex);
            }
            return;
        }
        assert (subRecordList != null);

        for (OslcResponseRecordI subRecord : subRecordList) {
            T_RECORD record = setDataForRecord(subRecord, loadHintForReferencedRecord);
            referenceList.add(new OslcRecordReference<T_RECORD>(subRecord.getOslcRecordIdentifier(), record));
        }

        field.setOslcValue(referenceList);
    }

    private void setDataForIndirectReferenceListField(OslcResponseRecordI responseRecord, OslcIndirectReferenceListFieldI<T_RECORD, T_RECORD> field, OslcLoadHint loadHintForIndirectReferencedRecord) throws ResponseException {
        assert (responseRecord != null);
        assert (field != null);
        assert (loadHintForIndirectReferencedRecord != null);

        List<OslcResponseRecordI> directResponseRecordList = null;
        List<OslcIndirectReferenceListFieldI.Entry<T_RECORD, T_RECORD>> referenceList = new ArrayList<>();

        try {
            directResponseRecordList = responseRecord.getSubRecords(field.getOslcPropertyName(), true);
        } catch (FieldNotFoundException ex) {
            boolean result = field.setOslcValue(null);
            if (field.isOptional() == false) {
                throw (ex);
            }
            return;
        }
        assert (directResponseRecordList != null);

        for (OslcResponseRecordI directResponseRecord : directResponseRecordList) {
            OslcIndirectReferenceListFieldI.Entry<T_RECORD, T_RECORD> newEntry = null;

            List<OslcResponseRecordI> indirectResponseRecordList = directResponseRecord.getSubRecords(field.getIndirectOslcPropertyName());
            if (indirectResponseRecordList.size() == 1) {
                OslcResponseRecordI indirectResponseRecord = indirectResponseRecordList.get(0);

                T_RECORD indirectRecord = setDataForRecord(indirectResponseRecord, loadHintForIndirectReferencedRecord);
                if (indirectRecord != null) {
                    T_RECORD directRecord = setDataForRecord(directResponseRecord, new OslcLoadHint(loadHintForIndirectReferencedRecord.isLoadRecordContentActive()));
                    if (directRecord != null) {
                        newEntry = new OslcIndirectReferenceListFieldI.Entry<>(
                                new OslcRecordReference<T_RECORD>(directResponseRecord.getOslcRecordIdentifier(), directRecord),
                                new OslcRecordReference<T_RECORD>(indirectResponseRecord.getOslcRecordIdentifier(), indirectRecord)
                        );
                    } else {
                        logger.severe("Failed to get direct record for field " + field.getOslcPropertyName() + ". List incomplete.");
                    }
                } else {
                    logger.severe("Failed to get indirect record for field " + field.getOslcPropertyName() + ". List incomplete.");
                }
            } else {
                logger.severe("OslcPropertyName = " + field.getOslcPropertyName());
                logger.severe("IndirectOslcPropertyName = " + field.getIndirectOslcPropertyName());
                logger.severe("size = " + indirectResponseRecordList.size());
                logger.severe("Record Content: " + directResponseRecord.getRecordContent().getUiString());
                logger.severe("Response for indirect record missing. List incomplete.");
            }
            if (newEntry != null) {
                referenceList.add(newEntry);
            }
        }

        field.setOslcValue(referenceList);
    }

    /**
     * Performs a full text search and returns the references to the matching
     * records.
     *
     * @param searchString
     * @return
     * @throws RestClient.Exceptions.RestException
     */
    final public List<OslcFullTextSearchResultRecord> executeFullTextSearch(String searchString) throws RestException {
        assert (searchString != null);
        assert (searchString.length() >= 2);

        try {
            return (fullTextSearch_Internal(new OslcFullTextSearch(searchString)));
        } catch (RestException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        } catch (ConstraintViolationReportedByServer ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            RestInternalException ie = new RestInternalException("Unexpected constraint error.", ex);
            throw (ie);
        }

    }

    final public List<OslcFullTextSearchResultRecord> executeFullTextSearch(String searchString, OslcRecordTypeI recordType) throws RestException {
        assert (searchString != null);
        assert (searchString.length() >= 2);
        assert (recordType != null);

        try {
            return (fullTextSearch_Internal(new OslcFullTextSearch(searchString, recordType)));

        } catch (RestException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        } catch (ConstraintViolationReportedByServer ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            RestInternalException ie = new RestInternalException("Unexpected constraint error.", ex);
            throw (ie);
        }

    }

    private List<OslcFullTextSearchResultRecord> fullTextSearch_Internal(OslcFullTextSearch command) throws RestException, ConstraintViolationReportedByServer {
        assert (command != null);

        //
        // Execute search command
        //
        OslcResponse queryResult = executeCommand(command);

        //
        // Build result list
        //
        List<OslcFullTextSearchResultRecord> result = new ArrayList<>();
        for (OslcResponseRecordI record : queryResult.getRecords()) {

            String dcType;
            String fullTextSearchTitle;
            try {
                dcType = record.getFieldValue("dc:type");
            } catch (FieldNotFoundException ex) {
                dcType = "";
            }
            try {
                fullTextSearchTitle = record.getFieldValue("FullTextSearchTitle");
            } catch (FieldNotFoundException ex) {
                try {
                    fullTextSearchTitle = record.getFieldValue("Title");
                } catch (FieldNotFoundException ex1) {
                    fullTextSearchTitle = "";
                }
            }
            result.add(new OslcFullTextSearchResultRecord(record.getOslcRecordIdentifier(), dcType, fullTextSearchTitle));
        }

        return (result);
    }

    /**
     * Executes a RQ1 query and returns the references to the matching records.
     *
     * @param queryId
     * @return
     * @throws RestClient.Exceptions.NotFoundException
     * @throws RestClient.Exceptions.RestException
     */
    final public OslcQueryResult executeStoredQuery(String queryId) throws NotFoundException, RestException {

        assert (queryId != null);
        assert (queryId.isEmpty() == false);

        //
        // Create and execute search command
        //
        OslcRq1Response queryResult;
        try {
            queryResult = new OslcRq1Response(executeCommand(new OslcStoredQuerySearch(queryId)));
        } catch (NotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);

            if (EcvApplication.hasMainWindow() == true) {
                EcvUserMessage.showMessageDialog(
                        "Query " + queryId + " not found.",
                        "Query not found.",
                        MessageType.INFORMATION_MESSAGE
                );
                return (null);
            }
            throw (ex);

        } catch (RestException ex) {
            if ((ex instanceof NoLoginDataException) == false) {
                logger.log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            }
            throw (ex);

        } catch (ConstraintViolationReportedByServer ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            RestInternalException ie = new RestInternalException("Unexpected constraint error.", ex);
            throw (ie);
        }

        //
        // Build result list
        //
        List<OslcQueryResultRecord> result = new ArrayList<>();
        try {
            for (OslcResponseRecordI record : queryResult.getRecords()) {
                String dcType;
                String title;
                try {
                    dcType = record.getFieldValue("dc:type");
                } catch (FieldNotFoundException ex) {
                    dcType = "";
                }
                try {
                    title = record.getFieldValue("dc:title");
                } catch (FieldNotFoundException ex) {
                    title = "";
                }
                if (record instanceof OslcResponseRecord_Oslc10) {
                    OslcResponseRecord_Oslc10.QueryResult topQueryResult = ((OslcResponseRecord_Oslc10) record).extractQueryResult();
                    result.addAll(extractQueryResultRecords(record.getOslcRecordIdentifier(), dcType, title, topQueryResult));
                } else {
                    result.add(new OslcQueryResultRecord(record.getOslcRecordIdentifier(), dcType, title));
                }

            }
        } catch (ResponseException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }

        return (new OslcQueryResult(queryResult.getTitle(), result));
    }

    private List<OslcQueryResultRecord> extractQueryResultRecords(
            OslcRecordIdentifier topRecordIdentifier,
            String topRecordType,
            String topRecordTitle,
            OslcResponseRecord_Oslc10.QueryResult topQueryResult) {

        assert (topRecordIdentifier != null);
        assert (topRecordType != null);
        assert (topRecordType != null);
        assert (topQueryResult != null);

        List<OslcQueryResultRecord> resultRecords = new ArrayList<>();
        extractQueryResultRecordFromSubRecord(resultRecords, topRecordIdentifier, topRecordType, topRecordTitle, new TreeMap<>(), topQueryResult);
        return (resultRecords);
    }

    final private String[] sameLevelNames = new String[]{
        "hasIssue",
        "hasIssuereleasemap",
        "hasParent",
        "hasMappedIssue",
        "hasMappedRelease",
        "hasMappedChildRelease",
        "hasMappedParentRelease",
        "Assignee",
        "belongsToIssue",
        "belongsToProject",
        "belongsToRelease"
    };

    private void extractQueryResultRecordFromSubRecord(
            List<OslcQueryResultRecord> resultRecords,
            OslcRecordIdentifier topRecordIdentifier,
            String topRecordType,
            String topRecordTitle,
            Map<String, String> parentRecordResultFields,
            OslcResponseRecord_Oslc10.QueryResult queryResult) {

        assert (resultRecords != null);
        assert (topRecordIdentifier != null);
        assert (topRecordType != null);
        assert (topRecordType != null);
        assert (queryResult != null);
        assert (parentRecordResultFields != null);

        //
        // Determine processing levels for sub records
        //
        List<OslcResponseRecord_Oslc10.QueryResult> unknownLevelQueryResults = queryResult.getSubRecords();
        List<OslcResponseRecord_Oslc10.QueryResult> sameLevelQueryResults = new ArrayList<>();
        List<OslcResponseRecord_Oslc10.QueryResult> childLevelQueryResults = new ArrayList<>();
        while (unknownLevelQueryResults.isEmpty() == false) {
            List<OslcResponseRecord_Oslc10.QueryResult> newUnknownLevelQueryResults = new ArrayList<>();
            for (OslcResponseRecord_Oslc10.QueryResult unknownLevelQueryResult : unknownLevelQueryResults) {
                if (Arrays.asList(sameLevelNames).indexOf(unknownLevelQueryResult.getName()) >= 0) {
                    sameLevelQueryResults.add(unknownLevelQueryResult);
                    newUnknownLevelQueryResults.addAll(unknownLevelQueryResult.getSubRecords());
                } else {
                    childLevelQueryResults.add(unknownLevelQueryResult);
                }
            }
            unknownLevelQueryResults = newUnknownLevelQueryResults;
        }

        //
        // Fill field map with fields from parent record, fields from current record and from same level records.
        //
        Map<String, String> currentRecordResultFields = new TreeMap<>(parentRecordResultFields);
        currentRecordResultFields.putAll(queryResult.getFields());
        for (OslcResponseRecord_Oslc10.QueryResult sameLevelQueryResult : sameLevelQueryResults) {
            currentRecordResultFields.putAll(sameLevelQueryResult.getFields());
        }

        //
        // Create and add record if no child records exist
        //
        if (childLevelQueryResults.isEmpty() == true) {
            resultRecords.add(new OslcQueryResultRecord(topRecordIdentifier, topRecordType, topRecordTitle, currentRecordResultFields));
            return;
        }

        for (OslcResponseRecord_Oslc10.QueryResult childLevelQueryResult : childLevelQueryResults) {
            extractQueryResultRecordFromSubRecord(resultRecords, topRecordIdentifier, topRecordType, topRecordTitle, currentRecordResultFields, childLevelQueryResult);
        }

    }

    final public OslcQueryResult executeFreeQuery(OslcSelection selection, Collection<String> requestedProperties) throws NotFoundException, RestException {
        assert (selection != null);
        assert (selection.isCriteriaSet() == true);
        assert (requestedProperties != null);
        assert (requestedProperties.isEmpty() == false);

        //
        // Create and execute search command
        //
        OslcCriteriaSearch search = new OslcCriteriaSearch(selection);
        for (String property : requestedProperties) {
            search.addPropertyField(property);
        }
        OslcRq1Response queryResult;
        try {
            queryResult = new OslcRq1Response(executeCommand(search));
        } catch (NotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            if (EcvApplication.hasMainWindow() == true) {
                EcvUserMessage.showMessageDialog(
                        "Query not found.",
                        "Query not found.",
                        MessageType.INFORMATION_MESSAGE
                );
                return (null);
            }
            throw (ex);

        } catch (RestException ex) {
            if ((ex instanceof NoLoginDataException) == false) {
                logger.log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            }
            throw (ex);

        } catch (ConstraintViolationReportedByServer ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            RestInternalException ie = new RestInternalException("Unexpected constraint error.", ex);
            throw (ie);
        }

        //
        // Build result list
        //
        List<OslcQueryResultRecord> result = new ArrayList<>();
        try {
            for (OslcResponseRecordI record : queryResult.getRecords()) {
                String dcType;
                String title;
                try {
                    dcType = record.getFieldValue("dc:type"); // OSLC 1.0
                } catch (FieldNotFoundException ex) {
                    try {
                        dcType = record.getFieldValue("dcterms:type"); // OSLC 2.0
                    } catch (FieldNotFoundException ex2) {
                        dcType = "";
                    }
                }
                try {
                    title = record.getFieldValue("dc:title"); // OSLC 1.0
                } catch (FieldNotFoundException ex) {
                    try {
                        title = record.getFieldValue("dcterms:title"); // OSLC 2.0
                    } catch (FieldNotFoundException ex2) {
                        title = "";
                    }
                }
                if (record instanceof OslcResponseRecord_Oslc10) {
                    OslcResponseRecord_Oslc10.QueryResult topResult = ((OslcResponseRecord_Oslc10) record).extractQueryResult();
                    result.addAll(extractQueryResultRecords(record.getOslcRecordIdentifier(), dcType, title, topResult));
                } else {
                    result.add(new OslcQueryResultRecord(record.getOslcRecordIdentifier(), dcType, title, ((OslcResponseRecord_Oslc20) record).getFieldsForQueryResult()));
                }

            }
        } catch (ResponseException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }

        return (new OslcQueryResult(queryResult.getTitle(), result));
    }

    final public OslcUploadFileResult uploadFile(OslcRecordIdentifier parentRecord, File file, Map<String, String> parameter) throws WriteToDatabaseRejected, RestException {
        assert (parentRecord != null);
        assert (file != null);
        assert (parameter != null);

        //
        // Execute upload command
        //
        OslcResponse result;
        try {
            result = executeCommand(new OslcUploadCommand(parentRecord, file, parameter));
        } catch (ConstraintViolationReportedByServer ex) {
            throw (ex);
        } catch (RestException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }

        //
        // Build result record
        //
        try {
            OslcResponseRecordI responseRecord = result.getSolelyRecord();

            String responsefileName = responseRecord.getFieldValue("cmapi:file_name");
            String responseFileSize = responseRecord.getFieldValue("cmapi:file_size");
            String responseDescription = responseRecord.getFieldValue("cmapi:description");
            OslcRecordIdentifier responseRecordIdentifier = responseRecord.getOslcRecordIdentifier();

            return (new OslcUploadFileResult(responseRecordIdentifier, responsefileName, responseFileSize, responseDescription));
        } catch (ResponseException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }
    }

    private void forward_Internal(OslcRecordIdentifier recordToForward, OslcDirectReferenceFieldI projectField, OslcRecordIdentifier newProject, OslcDirectReferenceFieldI assignee, OslcRecordIdentifier newAssignee, List<? extends OslcForwardableFieldI> additionalFields) throws RestException, WriteToDatabaseRejected {
        assert (recordToForward != null);
        assert (projectField != null);
        assert (newProject != null);
        assert (additionalFields != null);

        //
        // Create put command for forward
        //
        final OslcRq1PutCommand putCommand;
        putCommand = new OslcRq1PutCommand().setSelection(new OslcSelection().setRdfAbout(recordToForward.getRdfAbout()));
        List<OslcPropertyName> writeOrder = new ArrayList<>();
        writeOrder.add(new OslcPropertyName(projectField.getOslcPropertyName()));
        for (OslcForwardableFieldI additionalField : additionalFields) {
            writeOrder.add(new OslcPropertyName(additionalField.getOslcPropertyName()));
        }
        putCommand.setWriteOrder(writeOrder);
        putCommand.addPropertyField(projectField.getOslcPropertyName(), newProject.getRdfAbout());
        if (newAssignee != null) {
            writeOrder.add(new OslcPropertyName(assignee.getOslcPropertyName()));
            putCommand.addPropertyField(assignee.getOslcPropertyName(), newAssignee.getRdfAbout());
        }
        putCommand.setAction(OslcRq1PutCommand.Action.FORWARD);
        for (OslcForwardableFieldI field : additionalFields) {
            putCommand.addPropertyField(field.getOslcPropertyName(), field.getOslcValue());
        }

        putCommand.addPropertyField(OPERATION_MODE, EcvApplication.getToolName() + " - Version " + EcvApplication.getToolVersionForLogging());
        putCommand.addPropertyField(OPERATION_CONTEXT, "PUT");

        //
        // Forward in DB
        //
        executeCommand(putCommand);

    }

    /**
     * Forwards a record to a project in the RQ1 database.
     *
     * @param recordToForward Identifier of the record that shall be forwarded.
     * @param projectField Field in the forwarded record that contains that
     * project.
     * @param newProject Identifier of the project to witch the record shall be
     * forwarded.
     * @param additionalFields Additional fields that shall be provided in the
     * forward request.
     * @throws WriteToDatabaseRejected
     * @throws RestClient.Exceptions.RestException
     */
    final public void forward(OslcRecordIdentifier recordToForward, OslcDirectReferenceFieldI projectField, OslcRecordIdentifier newProject, OslcDirectReferenceFieldI assigneeField, OslcRecordIdentifier newAssignee, List<? extends OslcForwardableFieldI> additionalFields) throws WriteToDatabaseRejected, RestException {
        assert (recordToForward != null);
        assert (projectField != null);
        assert (newProject != null);

        try {
            forward_Internal(recordToForward, projectField, newProject, assigneeField, newAssignee, additionalFields);
        } catch (WriteToDatabaseRejected ex) {
            throw (ex);
        } catch (RestException ex) {
            logger.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1Client.class.getCanonicalName(), ex);
            throw (ex);
        }
    }

    /**
     * Returns the name of the database to which the program is logged in.
     * <p>
     * Note: The name. Not the path.
     *
     * @return Name of the database.
     */
    static public String getCurrentDatabaseName() {
        if (singletonClient.executor != null) {
            OslcRq1ServerDescription rq1Server = singletonClient.loginData.getServerDescription().getRq1Server();
            return (rq1Server.getServerName());
        } else {
            return (null);
        }
    }

    static public OslcRq1ServerDescription getCurrentDatabase() {
        if (singletonClient.executor != null) {
            OslcRq1ServerDescription rq1Server = singletonClient.loginData.getServerDescription().getRq1Server();
            return (rq1Server);
        } else {
            return (null);
        }
    }

    /**
     * Returns the name of the current user for the login.
     *
     * @return User name.
     */
    static public String getCurrentUserName() {
        if ((singletonClient.executor == null) || (singletonClient.loginData.getLoginName().isEmpty())) {
            return ("No user");
        }
        return (singletonClient.loginData.getLoginName());
    }

    /**
     * Export request counter for automatic tests.
     *
     * @return The number of executed requests or -1 if no connection is
     * established.
     */
    static public int getRequestCounter() {
        if (singletonClient.executor != null) {
            return (singletonClient.executor.getRequestCounter());
        } else {
            return (-1);
        }
    }

}
