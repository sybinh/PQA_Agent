/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.Enumerations.ApprovalHardware;

/**
 * Implements the cache for an Issue SW record of the RQ1 database.
 *
 * @author gug2wi
 */
public class Rq1IssueEcu extends Rq1HardwareIssue {

    final public Rq1DatabaseField_Enumeration APPROVAL;
    final public Rq1DatabaseField_ReferenceList HAS_CHILDREN;
    final public Rq1DatabaseField_ReferenceList IS_AFFECTED_BY_DEFECT_ISSUE;
    final public Rq1DatabaseField_Reference AFFECTED_ISSUE;

    final public static String FIELDNAME_HAS_CHILDREN = "hasChildren";
    final public static String FIELDNAME_AFFECTED_BY_DEFECT = "isAffectedByDefectIssue";
    final public static String FIELDNAME_AFFECTED_ISSUE = "AffectedIssue";

    public Rq1IssueEcu() {
        super(Rq1NodeDescription.ISSUE_HW_ECU);

        addField(APPROVAL = new Rq1DatabaseField_Enumeration(this, "Approval", ApprovalHardware.values()));

        addField(HAS_CHILDREN = new Rq1DatabaseField_ReferenceList(this, FIELDNAME_HAS_CHILDREN, Rq1RecordType.ISSUE));
        addField(AFFECTED_ISSUE = new Rq1DatabaseField_Reference(this, FIELDNAME_AFFECTED_ISSUE, Rq1RecordType.ISSUE));
        addField(IS_AFFECTED_BY_DEFECT_ISSUE = new Rq1DatabaseField_ReferenceList(this, FIELDNAME_AFFECTED_BY_DEFECT, Rq1RecordType.ISSUE));
    }
}
