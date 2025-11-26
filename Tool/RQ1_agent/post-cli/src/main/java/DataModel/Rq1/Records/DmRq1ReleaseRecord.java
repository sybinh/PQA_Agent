/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import static DataModel.Rq1.Records.DmRq1Release.compareVersion;
import Rq1Cache.Records.Rq1ReleaseRecord;
import UiSupport.BulkOperationRq1UserI;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import util.EcvDate;
import util.MailSendable;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1ReleaseRecord extends DmRq1AssignedRecord implements MailSendable, BulkOperationRq1UserI {

    final public DmRq1Field_Enumeration BASED_ON_PREDECESSOR;
    final public DmRq1Field_Enumeration LIFE_CYCLE_STATE;
    final public DmRq1Field_Enumeration SCOPE;
    final public DmRq1Field_Date PLANNED_DATE;
    final public DmRq1Field_Date PLANNING_FREEZE;
    final public DmRq1Field_Date SPECIFICATION_FREEZE;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> WORKITEMS;

    final public DmRq1Field_Reference<DmRq1User> REQUESTER;
    final public DmRq1Field_Text REQUESTER_FULLNAME;
    final public DmRq1Field_Text REQUESTER_EMAIL;
    final public DmRq1Field_Text REQUESTER_LOGIN_NAME;

    public DmRq1ReleaseRecord(String subjectType, Rq1ReleaseRecord rq1ReleaseRecord) {
        super(subjectType, rq1ReleaseRecord);

        addField(BASED_ON_PREDECESSOR = new DmRq1Field_Enumeration(this, rq1ReleaseRecord.BASED_ON_PREDECESSOR, "Based On Predecessor"));

        addField(LIFE_CYCLE_STATE = new DmRq1Field_Enumeration(this, rq1ReleaseRecord.LIFE_CYCLE_STATE, "Life Cycle State"));
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);

        addField(SCOPE = new DmRq1Field_Enumeration(this, rq1ReleaseRecord.SCOPE, "Scope"));
        SCOPE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);  // Read-only field

        addField((PLANNED_DATE = new DmRq1Field_Date(this, rq1ReleaseRecord.PLANNED_DATE, "Planned Date")));
        PLANNED_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PLANNED_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(PLANNING_FREEZE = new DmRq1Field_Date(this, rq1ReleaseRecord.PLANNING_FREEZE, "Planning Freeze"));
        PLANNING_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PLANNING_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(SPECIFICATION_FREEZE = new DmRq1Field_Date(this, rq1ReleaseRecord.SPECIFICATION_FREEZE, "Specification Freeze"));
        SPECIFICATION_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        SPECIFICATION_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(WORKITEMS = new DmRq1Field_ReferenceList<>(this, rq1ReleaseRecord.HAS_WORKITEMS, "Workitems"));

        addField(REQUESTER = new DmRq1Field_Reference<>(this, rq1ReleaseRecord.REQUESTER, "Requester"));
        addField(REQUESTER_FULLNAME = new DmRq1Field_Text(this, rq1ReleaseRecord.REQUESTER_FULLNAME, "Fullname Requester"));
        addField(REQUESTER_EMAIL = new DmRq1Field_Text(this, rq1ReleaseRecord.REQUESTER_EMAIL, "E-Mail Requester"));
        addField(REQUESTER_LOGIN_NAME = new DmRq1Field_Text(this, rq1ReleaseRecord.REQUESTER_LOGIN_NAME, "Shortcut Requester"));
    }

    public final EcvDate getPlannedDate() {
        return (PLANNED_DATE.getValue());
    }

    public int compareTypeNameVersion(DmRq1ReleaseRecord other) {
        assert (other != null);

        int typeCompare = getType().compareToIgnoreCase(other.getType());
        if (typeCompare != 0) {
            return (typeCompare);
        }

        int nameCompare = getName().compareToIgnoreCase(other.getName());
        if (nameCompare != 0) {
            return (nameCompare);
        }

        return (compareVersion(getVersion(), other.getVersion()));
    }

    public abstract String getType();

    public abstract String getName();

    public abstract String getVersion();

    public TreeSet<EcvDate> getAllPlannedDates() {
        TreeSet<EcvDate> dates = new TreeSet<>();
        EcvDate date = getPlannedDate();
        if (date == null || date.isEmpty()) {
            dates.add(EcvDate.getEmpty());
        } else {
            dates.add(date);
        }
        return dates;
    }

    @Override
    public String getAssigneeMail() {
        return ASSIGNEE_EMAIL.getValueAsText();
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTypeIdTitle();
    }

    @Override
    public String getIdForSubject() {
        return getId();
    }

    @Override
    public List<MailActionType> getActionName() {
        List<MailActionType> mailActionTypes = new ArrayList<>();
        mailActionTypes.add(MailActionType.ASSIGNEE);
        mailActionTypes.add(MailActionType.REQUESTER);
        return mailActionTypes;
    }

    @Override
    public String getProjectLeaderMail() {
        return null;
    }

    @Override
    public String getRequesterMail() {
        return REQUESTER_EMAIL.getValueAsText();
    }

    @Override
    public String getContactMail() {
        return null;
    }

    @Override
    public DmRq1Field_Reference<DmRq1User> getRequesterField() {
        return (REQUESTER);
    }

}
