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
import util.EcvTableRow;
import util.EcvTableColumnI;

/**
 *
 * @author gug2wi
 * @param <T_VALUE>
 */
public interface Rq1XmlTableColumn<T_VALUE> extends EcvTableColumnI<T_VALUE> {

    String getSourceName();

    default Collection<String> getAlternativeSourceNames() {
        return (new ArrayList<>(0));
    }

    /**
     * Decodes the column value that was read from an attribute of an XML
     * element.
     *
     * @param stringValue The string value from the attribute.
     * @return The object that shall be stored in the table data. Might be null.
     */
    default Object loadValueFromDatabase(String stringValue) {
        return (stringValue);
    }

    /**
     * Encodes the value to be stored with the encoding method ATTRIBUTE.
     *
     * @param row Row from which the value of the column shall be taken.
     * @return The string that shall be written to the attribute.
     */
    String provideValueForDatabase(EcvTableRow row);

    Rq1XmlTable.ColumnEncodingMethod getEncodingMethod();

}
