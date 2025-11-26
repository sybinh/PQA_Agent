/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.DmRq1ForwardableElementI;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1AssignedRecord;
import Rq1Cache.Records.Rq1Project;
import UiSupport.AssigneeFilter;
import util.EcvMapSet;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1AssignedRecord extends DmRq1SubjectElement implements DmRq1ForwardableElementI {

    final public DmRq1Field_Reference<DmRq1Project> PROJECT;

    final public DmRq1Field_Reference<DmRq1User> ASSIGNEE;
    final public DmRq1Field_Text ASSIGNEE_FULLNAME;
    final public DmRq1Field_Text ASSIGNEE_EMAIL;
    final public DmRq1Field_Text ASSIGNEE_LOGIN_NAME;

    public DmRq1AssignedRecord(String subjectType, Rq1AssignedRecord rq1AssignedRecord) {
        super(subjectType, rq1AssignedRecord);

        addField(PROJECT = new DmRq1Field_Reference<>(this, rq1AssignedRecord.BELONGS_TO_PROJECT, "Project"));

        addField(ASSIGNEE = new DmRq1Field_Reference<>(this, rq1AssignedRecord.ASSIGNEE, "Assignee"));
        ASSIGNEE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(ASSIGNEE_FULLNAME = new DmRq1Field_Text(this, rq1AssignedRecord.ASSIGNEE_FULLNAME, "Fullname Assignee"));
        addField(ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1AssignedRecord.ASSIGNEE_EMAIL, "E-Mail Assignee"));
        addField(ASSIGNEE_LOGIN_NAME = new DmRq1Field_Text(this, rq1AssignedRecord.ASSIGNEE_LOGIN_NAME, "Shortcut Assignee"));
    }

    @Override
    public void forward(DmRq1Project project, DmRq1User newAssignee) {
        ((Rq1AssignedRecord) getRq1Record()).forward((Rq1Project) project.getRq1Record(), newAssignee == null ? null : newAssignee.getRq1Record());
    }

    @Override
    final public DmRq1Project getProject() {
        return PROJECT.getElement();
    }

    @Override
    final public EcvMapSet<AssigneeFilter, DmRq1Project> getProjects() {
        EcvMapSet<AssigneeFilter, DmRq1Project> set = new EcvMapSet<>();
        if (set.addValueIfNotNull(AssigneeFilter.PROJECT, PROJECT.getElement()) == true) {
            set.addValueIfNotNull(AssigneeFilter.POOLPROJECT, PROJECT.getElement().POOL_PROJECT.getElement());
        }
        return set;
    }

}
