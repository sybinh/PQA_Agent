/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

/**
 *
 * @author frt83wi
 */
public class Rq1XmlTable_ExcItedDepartmentMap extends Rq1XmlTable {

    //
    // Example of excITED CPC Tag:
    //
    //    <excITEDCPC>
    //        <TEAM>Mon</TEAM>
    //        <GROUP>EFD4</GROUP>
    //        <DEPARTMENTMAP ASSIGNEE=”RBEI” DEPARTMENT=”EPV”></DEPARTMENTMAP>
    //        <DEPARTMENTMAP ASSIGNEE=”EFD” DEPARTMENT=”EFD”></DEPARTMENTMAP>
    //        <DEPARTMENTMAP ASSIGNEE=”OTHER” DEPARTMENT=”EFE”></DEPARTMENTMAP>
    //    </excITEDCPC>
    //
    final public Rq1XmlTableColumn_String ASSIGNEE;
    final public Rq1XmlTableColumn_String DEPARTMENT;

    public Rq1XmlTable_ExcItedDepartmentMap() {
        addXmlColumn(ASSIGNEE = new Rq1XmlTableColumn_String("Assignee", 10, "ASSIGNEE", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(DEPARTMENT = new Rq1XmlTableColumn_String("Department", 10, "DEPARTMENT", ColumnEncodingMethod.ATTRIBUTE));
    }
}
