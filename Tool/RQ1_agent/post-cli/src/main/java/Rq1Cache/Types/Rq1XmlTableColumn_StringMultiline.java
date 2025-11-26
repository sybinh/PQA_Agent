/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Types.Rq1XmlTable.ColumnEncodingMethod;
import util.EcvTableColumn_StringMultiline;
import util.EcvTableRow;

/**
 *
 * @author hfi5wi
 */
public class Rq1XmlTableColumn_StringMultiline extends EcvTableColumn_StringMultiline implements Rq1XmlTableColumn<String> {

    final String sourceName;
    final Rq1XmlTable.ColumnEncodingMethod encodingMethod;

    public Rq1XmlTableColumn_StringMultiline(String uiName, String sourceName, ColumnEncodingMethod encodingMethod) {
        super(uiName);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_StringMultiline(String uiName, int columnWidth, ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth);
        assert (encodingMethod == ColumnEncodingMethod.CONTENT || encodingMethod == ColumnEncodingMethod.TAG_NAME);
        this.sourceName = null;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_StringMultiline(String uiName, int columnWidth, String sourceName, ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public Rq1XmlTable.ColumnEncodingMethod getEncodingMethod() {
        return encodingMethod;
    }

    @Override
    public String provideValueForDatabase(EcvTableRow row) {
        assert (row != null);

        Object objectValue = row.getValueAt(this);
        if (objectValue == null) {
            return ("");
        } else {
            assert (objectValue instanceof String) : this.getClass().getCanonicalName() + " / " + objectValue.getClass().getCanonicalName();
            return ((String) objectValue);
        }
    }

}
