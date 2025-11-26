/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Monitoring.Rq1ParseFieldException;
import java.util.logging.Logger;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_FmeaDocument extends Rq1XmlTable {

    static private final Logger LOGGER = Logger.getLogger(Rq1XmlTable_FmeaDocument.class.getCanonicalName());

    final public Rq1XmlTableColumn_String COMMENT;
    final public Rq1XmlTableColumn_Link LINK;
    final public Rq1XmlTableColumn_String VERSION;

    public Rq1XmlTable_FmeaDocument() {

        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String("Comment", 35, "comment", ColumnEncodingMethod.ATTRIBUTE));
        COMMENT.setOptional();

        addXmlColumn(LINK = new Rq1XmlTableColumn_Link("Link (double click to open)", 200, "link", ColumnEncodingMethod.ATTRIBUTE));
        LINK.setOptional();

        addXmlColumn(VERSION = new Rq1XmlTableColumn_String("Version", 25, "version", ColumnEncodingMethod.ATTRIBUTE));
        VERSION.setOptional();
    }

    @Override
    public void loadRowFromDb(EcvTableData data, EcvXmlElement rowElement) throws Rq1ParseFieldException {

        if (rowElement instanceof EcvXmlEmptyElement) {
            // Seems to be the default format.
            super.loadRowFromDb(data, rowElement);
        } else if (rowElement instanceof EcvXmlTextElement) {
            EcvTableRow row = parseOldFormat(((EcvXmlTextElement) rowElement).getText());
            if (row != null) {
                data.addRow(row);
            }
        } else {
            LOGGER.warning("Unexpected content in row for FMEA document.\n" + rowElement.toString());
            StringBuilder b = new StringBuilder(100);
            b.append("Error when parsing row for FMEA document: Unexpected type ").append(rowElement.getClass().getCanonicalName());
            throw (new Rq1ParseFieldException(b.toString()));
        }

    }

    EcvTableRow parseOldFormat(String text) {
        assert (text != null);

        String file = null;
        String link = null;
        String version = null;

        String[] parts = text.split("\\;");
        switch (parts.length) {

            case 1:
                link = parts[0].trim();
                if (link.isEmpty()) {
                    return (null);
                }
                break;
            case 2:
                link = parts[0].trim();
                version = parts[1].trim();
                break;
            case 3:
                file = parts[0].trim();
                link = parts[1].trim();
                version = parts[2].trim();
                break;
            default:
                LOGGER.warning("Unexpected content in row for FMEA document: >" + text + "<");
                return (null);
        }

        EcvTableRow row = new EcvTableRow(this);
        if (file != null) {
            COMMENT.setValue(row, file);
        }
        if (link != null) {
            LINK.setValue(row, link);
        }
        if (version != null) {
            VERSION.setValue(row, version);
        }
        return (row);
    }

}
