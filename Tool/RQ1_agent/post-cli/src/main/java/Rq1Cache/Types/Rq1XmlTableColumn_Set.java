/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvTableColumn_Set;
import util.EcvTableRow;
import util.EcvXmlElement;
import util.EcvXmlTextElement;

/**
 *
 * @author gug2wi
 */
public class Rq1XmlTableColumn_Set extends EcvTableColumn_Set implements Rq1XmlTableColumn<SortedSet<String>>, Rq1XmlTable.ElementListSupported {

    final String sourceName;
    final Rq1XmlTable.ColumnEncodingMethod encodingMethod;

    public Rq1XmlTableColumn_Set(String uiName, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName);
        assert (encodingMethod != null);
        assert (sourceName != null);
        assert (sourceName.isEmpty() == false);
        this.sourceName = sourceName;
        this.encodingMethod = encodingMethod;
    }

    public Rq1XmlTableColumn_Set(String uiName, int columnWidth, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
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
    public Object loadFromElementList(Iterable<EcvXmlElement> elements) {
        TreeSet<String> set = new TreeSet<>();
        for (EcvXmlElement e : elements) {
            if (e instanceof EcvXmlTextElement) {
                set.add(((EcvXmlTextElement) e).getText());
            }
        }
        return set;
    }

    @Override
    public List<String> provideElementForDb(Object o) {
        List<String> l = new ArrayList<>();
        if (o instanceof Set) {
            for (Object e : (Set) o) {
                if (e instanceof String) {
                    l.add((String) e);
                }
            }
        }
        return l;
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
