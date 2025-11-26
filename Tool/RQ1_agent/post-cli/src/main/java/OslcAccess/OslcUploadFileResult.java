/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 *
 * @author gug2wi
 */
public class OslcUploadFileResult {

    final private OslcRecordIdentifier oslcRecordIdentifier;
    final private String fileName;
    final private String fileSize;
    final private String description;

    public OslcUploadFileResult(OslcRecordIdentifier oslcRecordIdentifier, String fileName, String fileSize, String description) {
        assert (oslcRecordIdentifier != null);
        assert (fileName != null);
        assert (fileName.isEmpty() == false);
        assert (description != null);

        this.oslcRecordIdentifier = oslcRecordIdentifier;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getDescription() {
        return description;
    }

    public OslcRecordIdentifier getOslcRecordIdentifier() {
        return oslcRecordIdentifier;
    }

}
