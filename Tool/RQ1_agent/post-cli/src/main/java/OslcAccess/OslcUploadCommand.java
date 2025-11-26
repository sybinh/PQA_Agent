/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.io.File;
import java.util.Map;

/**
 *
 * @author GUG2WI
 */
public class OslcUploadCommand extends OslcCommand {

    final private OslcRecordIdentifier parentRecord;
    final private File file;
    final private Map<String, String> parameter;

    public OslcUploadCommand(OslcRecordIdentifier parentRecord, File file, Map<String, String> parameter) {
        super(OslcProtocolVersion.OSLC_10);
        assert (parentRecord != null);
        assert (file != null);

        this.parentRecord = parentRecord;
        this.file = file;
        this.parameter = parameter;
    }

    final public Map<String, String> getParameter() {
        return (parameter);
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);

        StringBuilder builder = new StringBuilder(100);
        builder.append(parentRecord.getRdfAbout()).append("/field/Attachments/attachment?rcm.action=Modify");
        return (builder.toString());
    }

    public File getFile() {
        return (file);
    }

    @Override
    public String getAddressForUi() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
