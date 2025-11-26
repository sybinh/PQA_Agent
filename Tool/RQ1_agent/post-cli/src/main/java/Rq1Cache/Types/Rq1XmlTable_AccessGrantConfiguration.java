/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

/**
 *
 * @author bel5cob
 */
public class Rq1XmlTable_AccessGrantConfiguration extends Rq1XmlTable {

    private Rq1XmlTableColumn_String granted_to;

    public Rq1XmlTable_AccessGrantConfiguration() {

        addXmlColumn(granted_to = new Rq1XmlTableColumn_String("GRANTED_TO", 10, "GRANTED_TO", ColumnEncodingMethod.CONTENT));

    }

}
