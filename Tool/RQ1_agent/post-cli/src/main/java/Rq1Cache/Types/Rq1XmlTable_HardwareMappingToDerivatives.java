/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_HardwareMappingToDerivatives extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String INTERN;
    final public Rq1XmlTableColumn_String EXTERN;

    public Rq1XmlTable_HardwareMappingToDerivatives() {
        addXmlColumn(INTERN = new Rq1XmlTableColumn_String("Internal", 10, "SI", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(EXTERN = new Rq1XmlTableColumn_String("External", 10, "-", ColumnEncodingMethod.CONTENT));
    }

}
