/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvTableColumn_Link;
import util.EcvTableRow;

/**
 *
 * @author gug2wi
 */
public class Rq1XmlTableColumn_Link extends EcvTableColumn_Link implements Rq1XmlTableColumn<String> {

    final String sourceName;
    private List<String> alternativSourceNames = null;
    private boolean deleteSpacesByLoadingValueFromDatabase = true;
    final Rq1XmlTable.ColumnEncodingMethod encodingMethod;
    
    public Rq1XmlTableColumn_Link(String uiName, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_Link(String uiName, int columnWidth, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
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
    
    public void setDeleteSpacesByLoadingValueFromDatabase(boolean deleteSpacesByLoadingValueFromDatabase) {
        this.deleteSpacesByLoadingValueFromDatabase = deleteSpacesByLoadingValueFromDatabase;
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
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public Rq1XmlTable.ColumnEncodingMethod getEncodingMethod() {
        return encodingMethod;
    }

    @Override
    public Object loadValueFromDatabase(String stringValue) {
        if (stringValue == null) {
            return (null);
        }
        String trimmedValue = stringValue.trim();
        if (deleteSpacesByLoadingValueFromDatabase) {
            String valueWithoutBlanks = trimmedValue.replaceAll(" ", "");
            return (valueWithoutBlanks);
        } else {
            return (trimmedValue);
        }
    }

    @Override
    public String provideValueForDatabase(EcvTableRow row) {
        assert (row != null);

        Object objectValue = row.getValueAt(this);
        if (objectValue == null) {
            return ("");
        } else {
            assert (objectValue instanceof String) : this.getClass().getCanonicalName() + " / " + objectValue.getClass().getCanonicalName();
            String s = (String) objectValue;
            if (s.endsWith(" ") == true) {
                return (s);
            } else {
                return (s + " ");
            }

        }
    }

    @Override
    public String toString() {
        return (this.getClass().getCanonicalName() + " - " + sourceName + " - " + encodingMethod.name());
    }
    
}
