/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_UserReference;
import static Rq1Cache.Records.Rq1BaseRecord.ATTRIBUTE_LIFE_CYCLE_STATE;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_BASED_ON_PREDECESSOR;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_HAS_WORKITEMS;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.YesNoEmpty;

/**
 *
 * @author GUG2WI
 */
public class Rq1ReleaseRecord extends Rq1AssignedRecord {

    final public Rq1DatabaseField_Enumeration BASED_ON_PREDECESSOR;
    final public Rq1DatabaseField_Enumeration LIFE_CYCLE_STATE;
    final public Rq1DatabaseField_Enumeration SCOPE;
    final public Rq1DatabaseField_Date PLANNED_DATE;
    final public Rq1DatabaseField_Date PLANNING_FREEZE;
    final public Rq1DatabaseField_Date SPECIFICATION_FREEZE;

    final public Rq1DatabaseField_ReferenceList HAS_WORKITEMS;

    final public Rq1DatabaseField_UserReference REQUESTER;
    final public Rq1DatabaseField_Text REQUESTER_FULLNAME;
    final public Rq1DatabaseField_Text REQUESTER_EMAIL;
    final public Rq1DatabaseField_Text REQUESTER_LOGIN_NAME;

    public Rq1ReleaseRecord(Rq1NodeDescription recordDescription) {
        super(recordDescription);

        addField(BASED_ON_PREDECESSOR = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_BASED_ON_PREDECESSOR, YesNoEmpty.values()));

        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_Release.values(), LifeCycleState_Release.NEW));
        addField(SCOPE = new Rq1DatabaseField_Enumeration(this, "Scope", Scope.values()));
        addField(PLANNED_DATE = new Rq1DatabaseField_Date(this, "PlannedDate"));
        addField(PLANNING_FREEZE = new Rq1DatabaseField_Date(this, "PlanningFreeze"));
        addField(SPECIFICATION_FREEZE = new Rq1DatabaseField_Date(this, "SpecificationFreeze"));

        addField(HAS_WORKITEMS = new Rq1DatabaseField_ReferenceList(this, ATTRIBUTE_HAS_WORKITEMS, Rq1RecordType.WORKITEM));

        addField(REQUESTER = new Rq1DatabaseField_UserReference(this, "Requester"));
        addField(REQUESTER_FULLNAME = new Rq1DatabaseField_Text(this, "Requester.fullname"));
        addField(REQUESTER_EMAIL = new Rq1DatabaseField_Text(this, "Requester.email"));
        addField(REQUESTER_LOGIN_NAME = new Rq1DatabaseField_Text(this, "Requester.login_name"));

        REQUESTER.setFieldForFullName(REQUESTER_FULLNAME);
        REQUESTER.setFieldForEmail(REQUESTER_EMAIL);
        REQUESTER.setFieldForLoginName(REQUESTER_LOGIN_NAME);

        REQUESTER.setOptional();
        REQUESTER_FULLNAME.setNoWriteBack().setOptional();
        REQUESTER_EMAIL.setNoWriteBack().setOptional();
        REQUESTER_LOGIN_NAME.setNoWriteBack().setOptional();
    }

}
