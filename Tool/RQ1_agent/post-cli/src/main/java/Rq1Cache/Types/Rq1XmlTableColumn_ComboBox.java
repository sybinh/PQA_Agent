/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvEnumeration;
import util.EcvTableColumn_ComboBox;
import util.EcvTableRow;

/**
 * A column to store one value out of a set of allowed values.
 *
 * @author gug2wi
 */
public class Rq1XmlTableColumn_ComboBox extends EcvTableColumn_ComboBox implements Rq1XmlTableColumn<String> {

    final private String sourceName;
    private List<String> alternativSourceNames = null;
    final private Rq1XmlTable.ColumnEncodingMethod encodingMethod;

    public Rq1XmlTableColumn_ComboBox(String uiName, String[] allowedValues, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, allowedValues);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_ComboBox(String uiName, int columnWidth, String[] allowedValues, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth, allowedValues);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_ComboBox(String uiName, EcvEnumeration[] allowedValues, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, allowedValues);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_ComboBox(String uiName, int columnWidth, EcvEnumeration[] allowedValues, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth, allowedValues);
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
        alternativSourceNames.add(alternativeSourceName);
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
