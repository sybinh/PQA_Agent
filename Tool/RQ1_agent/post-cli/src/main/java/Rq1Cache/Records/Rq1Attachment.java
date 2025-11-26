/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.WriteToDatabaseRejected;
import OslcAccess.Rq1.OslcRq1Client;
import OslcAccess.Rq1.OslcRq1ServerDescription.LinkType;
import OslcAccess.OslcUploadFileResult;

import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Monitoring.Rq1WriteFailure;
import static Rq1Cache.Records.Rq1Record.successfullWriteRule;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordIndex;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvApplication;
import util.EcvXmlContainerElement;

/**
 *
 * @author gug2wi
 */
public class Rq1Attachment extends Rq1Record implements Rq1NodeInterface {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Rq1Attachment.class.getCanonicalName());

    final public Rq1DatabaseField_Text FILENAME;
    final public Rq1DatabaseField_Text FILESIZE;
    final public Rq1DatabaseField_Text DESCRIPTION;
    public static Rq1Subject target;

    private int numberOfGetFileContentCalls = 0;
    private int numberOfGetXmlFileContentCalls = 0;

    public Rq1Attachment() {
        super(Rq1NodeDescription.ATTACHMENT);

        addField(FILENAME = new Rq1DatabaseField_Text(this, "filename"));
        addField(FILESIZE = new Rq1DatabaseField_Text(this, "filesize"));
        addField(DESCRIPTION = new Rq1DatabaseField_Text(this, "description"));
    }

    @Override
    public void reload() {
        // Not possible. Therefore ignored.
    }

    @Override
    public void openInRq1(LinkType linkType) {
        OslcRq1Client.getOslcClient().browse(getOslcRecordIdentifier().getRdfAbout());
    }

    /**
     *
     * @return Input Stream reader of the URL of the File. Name is
     * 'Empty_Response' if load failed.
     */
    public EcvXmlContainerElement getFileContentNeverNull() {
        numberOfGetFileContentCalls++;
        checkNumberOfContentCalls();
        try {
            return OslcRq1Client.getOslcClient().loadXmlFile(this.getOslcRecordIdentifier().getRdfAbout());
        } catch (RestException ex) {
            Logger.getLogger(Rq1Attachment.class.getName()).log(Level.SEVERE, "Download of attachment " + FILENAME.getDataModelValue() + " failed. Size = " + FILESIZE.getDataModelValue());
            ToolUsageLogger.logError(Rq1Attachment.class.getName(), ex);
            EcvApplication.handleException(ex);
            return (new EcvXmlContainerElement("Empty_Response"));
        }
    }

    /**
     *
     * @return null if load failed.
     */
    public EcvXmlContainerElement getXmlFileContent() {
        numberOfGetXmlFileContentCalls++;
        checkNumberOfContentCalls();
        try {
            return OslcRq1Client.getOslcClient().loadXmlFile(this.getOslcRecordIdentifier().getRdfAbout());
            // return null
        } catch (RestException ex) {
            LOGGER.log(Level.WARNING, "Load XML file content for attachment.", ex);
            EcvApplication.handleException(ex);
            return (null);
        }
    }

    /**
     * Returns the content of the attached file.
     *
     * @return Content of the attached file or null if read was not successful.
     */
    public List<byte[]> download() {
        try {
            return (OslcRq1Client.getOslcClient().loadFileContent(this.getOslcRecordIdentifier().getRdfAbout()));
        } catch (RestException ex) {
            LOGGER.log(Level.WARNING, "Download file content for attachment.", ex);
            EcvApplication.handleException(ex);
            return (null);
        }
    }

    private void checkNumberOfContentCalls() {
        if ((numberOfGetFileContentCalls > 1) || (numberOfGetXmlFileContentCalls > 1)) {
            LOGGER.log(Level.WARNING, "{0}/{1}: numberOfGetFileContentCalls = {2}, numberOfGetXmlFileContentCalls = {3}", new Object[]{FILENAME.getDataModelValue(), FILESIZE.getDataModelValue(), numberOfGetFileContentCalls, numberOfGetXmlFileContentCalls});
        }
    }

    static public Rq1Attachment create(Rq1Subject targetElement, File file, String description) throws WriteToDatabaseRejected {
        assert (targetElement != null);
        assert (targetElement.existsInDatabase() == true);
        assert (file != null);
        assert (description != null);

        target = targetElement;

        OslcUploadFileResult fileResult = null;
        try {
            fileResult = Rq1Client.client.addAttachment(targetElement, file, description);
        } catch (WriteToDatabaseRejected ex) {
            handleWriteErrorForAttachment(file, ex);
        }

        Rq1Attachment newAttachment = null;
        if (fileResult != null) {
            newAttachment = new Rq1Attachment();

            newAttachment.setOslcRecordIdentifier(fileResult.getOslcRecordIdentifier());
            newAttachment.FILENAME.setOslcValue(fileResult.getFileName());
            newAttachment.FILESIZE.setOslcValue(fileResult.getFileSize());
            newAttachment.DESCRIPTION.setOslcValue(fileResult.getDescription());

            Rq1RecordIndex.addRecord(newAttachment);
        }

        return (newAttachment);
    }

    static private void handleWriteErrorForAttachment(File file, Throwable t) throws WriteToDatabaseRejected{
        if (t instanceof WriteToDatabaseRejected) {
            WriteToDatabaseRejected ex = (WriteToDatabaseRejected) t;
            target.handleWriteError_ConstraintViolationReportedByServer((ConstraintViolationReportedByServer) t);
            target.setMarker(new Rq1WriteFailure(successfullWriteRule, "Write to RQ1 failed.", ex.getMessageForUi() + "\n\n" + ex.getDetailedMessageForUi()));
            throw ex;

        } else {
            LOGGER.log(Level.SEVERE, "Unknown problem.", t);
            ToolUsageLogger.logError(Rq1Attachment.class.getName(), t);
        }
    }


}

