/*
 *  Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 *  This program and the accompanying materials are made available under
 *  the terms of the Bosch Internal Open Source License v4
 *  which accompanies this distribution, and is available at
 *  http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

/**
 *
 * @author DUR3WI
 */
public class Rq1XmlTable_ProPlaToAblieferungsstand extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String DERIVAT;
    final public Rq1XmlTableColumn_String SET_DATE;
    final public Rq1XmlTableColumn_String SET_BY;
    final public Rq1XmlTableColumn_String STATUS;

    public Rq1XmlTable_ProPlaToAblieferungsstand() {
        addXmlColumn(DERIVAT = new Rq1XmlTableColumn_String("Derivat", 15, "Derivat", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(SET_DATE = new Rq1XmlTableColumn_String("SetDate", 15, "SetDate", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(SET_BY = new Rq1XmlTableColumn_String("SetBy", 15, "SetBy", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(STATUS = new Rq1XmlTableColumn_String("Status", 10, "Status", ColumnEncodingMethod.CONTENT));
    }

}
