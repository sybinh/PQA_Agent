/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

/**
 *
 * @author hvb1kor
 */
public class Rq1XmlTable_CrpCombinedProjects extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String COMBINED_PRJ;
    final public Rq1XmlTableColumn_String PRIMARY;

    public Rq1XmlTable_CrpCombinedProjects() {

        addXmlColumn(COMBINED_PRJ = new Rq1XmlTableColumn_String("Combined Projects", "PROJECTS", ColumnEncodingMethod.ATTRIBUTE));

        addXmlColumn(PRIMARY = new Rq1XmlTableColumn_String("Primary", "PRIMARY", ColumnEncodingMethod.ATTRIBUTE));

    }
}
