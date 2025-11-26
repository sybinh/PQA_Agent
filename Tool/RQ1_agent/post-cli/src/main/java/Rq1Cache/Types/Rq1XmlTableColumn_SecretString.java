/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Types.Rq1XmlTable.ColumnEncodingMethod;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvTableColumn_SecretString;
import util.EcvTableRow;

/**
 *
 * @author hfi5wi
 */
public class Rq1XmlTableColumn_SecretString extends EcvTableColumn_SecretString implements Rq1XmlTableColumn<String> {

    final String sourceName;
    private List<String> alternativSourceNames = null;
    final Rq1XmlTable.ColumnEncodingMethod encodingMethod;

    public Rq1XmlTableColumn_SecretString(String uiName, String sourceName, ColumnEncodingMethod encodingMethod) {
        super(uiName);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_SecretString(String uiName, int columnWidth, ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth);
        assert (encodingMethod == ColumnEncodingMethod.CONTENT || encodingMethod == ColumnEncodingMethod.TAG_NAME);
        this.sourceName = null;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_SecretString(String uiName, int columnWidth, String sourceName, ColumnEncodingMethod encodingMethod) {
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

    public void addAlternativSourceName(String alternativeSourceName) {
        assert (alternativeSourceName != null);
        assert (alternativeSourceName.isEmpty() == false);
        assert ((alternativSourceNames) == null || (alternativSourceNames.contains(alternativeSourceName) == false)) : "Already set: " + alternativeSourceName;

        if (alternativSourceNames == null) {
            alternativSourceNames = new ArrayList<>(1);
        }
        alternativSourceNames.add(sourceName);
    }

    @Override
    public Collection<String> getAlternativeSourceNames() {
        if (alternativSourceNames != null) {
            return (alternativSourceNames);
        } else {
            return (new ArrayList<>(0));
        }
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
