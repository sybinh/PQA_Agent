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
 * @author ser4cob
 */
public class Rq1XmlTable_RelativeProjectConfiguration extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String ID;

    public Rq1XmlTable_RelativeProjectConfiguration() {

        addXmlColumn(ID = new Rq1XmlTableColumn_String("Id", 25, "Id", ColumnEncodingMethod.ATTRIBUTE));

    }
}
