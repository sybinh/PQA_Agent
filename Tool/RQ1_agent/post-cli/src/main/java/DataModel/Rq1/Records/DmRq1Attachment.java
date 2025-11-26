/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import RestClient.Exceptions.WriteToDatabaseRejected;
import Rq1Cache.Records.Rq1Attachment;
import Rq1Cache.Records.Rq1Subject;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvXmlContainerElement;

/**
 *
 * @author gug2wi
 */
public class DmRq1Attachment extends DmRq1Element {

    final static private Logger LOGGER = Logger.getLogger(DmRq1Attachment.class.getCanonicalName());

    final public DmRq1Field_Text DESCRIPTION;
    final public DmRq1Field_Text FILENAME;
    final public DmRq1Field_Text FILESIZE;
    final private Rq1Attachment rq1Attachment;

    /**
     *
     * @param rq1Attachment the Rq1 base Element of the Data Model Element
     */
    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Attachment(Rq1Attachment rq1Attachment) {
        super("RQ1-ATTACHMENT", rq1Attachment);
        this.rq1Attachment = rq1Attachment;

        //
        // Create and add fields
        //
        addField(DESCRIPTION = new DmRq1Field_Text(this, rq1Attachment.DESCRIPTION, "Description"));
        addField(FILENAME = new DmRq1Field_Text(this, rq1Attachment.FILENAME, "Filename"));
        addField(FILESIZE = new DmRq1Field_Text(this, rq1Attachment.FILESIZE, "Size"));
    }

    @Override
    public boolean isCanceled() {
        return (false);
    }

    @Override
    public String getTitle() {
        return (FILENAME.getValue());
    }

    public EcvXmlContainerElement getFileContent() {
        return rq1Attachment.getFileContentNeverNull();
    }

    // check if filename contains invalid characters and rename it
    static List<String> specialChars = Arrays.asList("\\", "/", ":", "*", "?", "\"", "<", ">", "|");

    static String replaceInvalidCharacters(String fileName, String replaceWith) {

        if (fileName == null) {
            LOGGER.log(Level.WARNING, "Parameter fileName is null");
            return null;
        } else {
            for (String c : specialChars) {
                fileName = fileName.replace(c, replaceWith);
            }
            return fileName;
        }
    }

    public String downloadAndSave(String targetDirectory) {

        if (targetDirectory == null) {
            LOGGER.log(Level.WARNING, "target directory not defined. No download started for {0}", toString());
            return null;
        } else {
            File file = new File(targetDirectory);
            boolean exists = file.exists();      // Check if the file exists
            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            if (!exists || !isDirectory) {
                LOGGER.log(Level.WARNING, "target directory not exists. No download started for {0}", toString());
                return null;
            }
        }

        List<byte[]> content = rq1Attachment.download();
        if (content == null) {
            LOGGER.log(Level.WARNING, "Download failed for {0}", toString());
            return null;
        }

        String cleanFileName = replaceInvalidCharacters(FILENAME.getValueAsText(), "");

        OutputStream downLoadFile = createFile(targetDirectory, cleanFileName);
        if (downLoadFile == null) {
            LOGGER.log(Level.WARNING, "Cannot create download file for {0}", toString());
            return null;
        }
        try {
            for (byte[] part : content) {
                downLoadFile.write(part);
            }
            downLoadFile.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1Attachment.class.getCanonicalName(), ex);
            LOGGER.log(Level.WARNING, "Cannot save data to download file for {0}", toString());
        }

        return downLoadFilePath.toString();
    }

    public void downloadAndOpen() {

        String userDir = System.getProperty("user.home");
        if ((userDir == null) || (userDir.isEmpty() == true)) {
            LOGGER.log(Level.WARNING, "User directory not defined. No download started for {0}", toString());
            return;
        }

        this.downloadAndSave(userDir + File.separator + "Downloads");

        try {
            Desktop.getDesktop().open(downLoadFilePath.toFile());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1Attachment.class.getCanonicalName(), ex);
            LOGGER.log(Level.WARNING, "Cannot open download file {0}", downLoadFilePath);
        }

    }

    public List<byte[]> downloadAsByteList() {
        return rq1Attachment.download();
    }

    // Dirty. But it works and is cheap.
    private Path downLoadFilePath = null;

    private OutputStream createFile(String path, String fileName) {
        assert (path != null);
        assert (path.isEmpty() == false);
        assert (fileName != null);
        assert (fileName.isEmpty() == false);

        String baseName = fileName;
        String suffix = "";
        int indexOfDot = fileName.lastIndexOf(".");
        if (indexOfDot >= 0) {
            baseName = fileName.substring(0, indexOfDot);
            if (fileName.length() > (indexOfDot)) {
                suffix = fileName.substring(indexOfDot, fileName.length());
            }
        }

        String number = "";
        FileAlreadyExistsException faee = null;
        for (int i = 1; i <= 100; i++) {
            StringBuffer b = new StringBuffer();
            b.append(path);
            b.append(File.separator);
            b.append(baseName);
            b.append(number);
            b.append(suffix);

            downLoadFilePath = Paths.get(b.toString());
            try {
                OutputStream downLoadFile = Files.newOutputStream(downLoadFilePath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                return (downLoadFile);
            } catch (FileAlreadyExistsException ex) {
                faee = ex;

            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, null, ex);
                return (null);
            }

            number = "(" + Integer.toString(i) + ")";
        }

        if (faee != null) {
            LOGGER.log(Level.WARNING, null, faee);
        }
        return (null);
    }

    /**
     * Create an attachment for the given file content.
     *
     * @param targetElement Element on which the attachment shall be added.
     * @param fileName Name of the file.
     * @param description Description for the file
     * @param content Content of the file
     * @return The new attachment object, if the creation was successful.
     * Otherwise null.
     * @throws java.io.IOException
     */
    static public DmRq1Attachment create(DmRq1SubjectElement targetElement, String fileName, String description, String content) throws IOException, WriteToDatabaseRejected {
        assert (targetElement != null);
        assert (fileName != null);
        assert (fileName.isEmpty() == false);
        assert (content != null);

        String userDir = System.getProperty("user.home");
        if ((userDir == null) || (userDir.isEmpty() == true)) {
            LOGGER.log(Level.WARNING, "User directory not defined. File {0} not created", fileName);
            return (null);
        }

        File file = new File(userDir + File.separator + "Downloads" + File.separator + fileName);
        OutputStream outputStream = Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.append(content);
        writer.close();

        DmRq1Attachment newAttachment = create(targetElement, file, description);

        Files.delete(file.toPath());

        return (newAttachment);
    }

    static public DmRq1Attachment create(DmRq1SubjectElement targetElement, File file, String description) throws WriteToDatabaseRejected {
        assert (targetElement != null);
        assert (targetElement.existsInDatabase() == true) : targetElement.getTitle() + " does not yet exist in the database.";
        assert (file != null);
//        assert (description != null); Removed, because the GUI sometimes returns null for empty text fields. See ECVTOOL-1551.

        Rq1Attachment rq1Attachment = Rq1Attachment.create((Rq1Subject) targetElement.getRq1Record(), file, description != null ? description : "");
        if (rq1Attachment != null) {
            DmRq1Attachment dmAttachment = (DmRq1Attachment) DmRq1ElementCache.getElement(rq1Attachment);
            return (dmAttachment);
        }

        return (null);
    }

    public static DmRq1Attachment getAttachmentForFilename(DmRq1Field_ReferenceList<DmRq1Attachment> attachmentField, String fileName) {
        for (DmRq1Attachment attachment : attachmentField.getElementList()) {
            if (attachment.FILENAME.getValue().equals(fileName)) {
                return attachment;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return ("Attachment: " + FILENAME.getValueAsText());
    }

}
