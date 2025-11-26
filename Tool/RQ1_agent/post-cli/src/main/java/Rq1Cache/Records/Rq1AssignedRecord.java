/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import RestClient.Exceptions.WriteToDatabaseRejected;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_UserReference;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;

/**
 *
 * @author GUG2WI
 */
public class Rq1AssignedRecord extends Rq1Subject implements Rq1ForwardableRecord {

    final static public Rq1AttributeName ATTRIBUTE_ASSIGNEE_LOGIN_NAME = new Rq1AttributeName("Assignee.login_name");
    final static public Rq1AttributeName ATTRIBUTE_BELONGS_TO_PROJECT = new Rq1AttributeName("belongsToProject");

    final public Rq1DatabaseField_UserReference ASSIGNEE;
    final public Rq1DatabaseField_Text ASSIGNEE_FULLNAME;
    final public Rq1DatabaseField_Text ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text ASSIGNEE_LOGIN_NAME;

    final public Rq1DatabaseField_Reference BELONGS_TO_PROJECT;

    public Rq1AssignedRecord(Rq1NodeDescription recordDescription) {
        this(recordDescription, null);
    }

    public Rq1AssignedRecord(Rq1NodeDescription subjectDescription, Rq1AttributeName attributeNameForDescription) {
        super(subjectDescription, attributeNameForDescription);

        addField(ASSIGNEE = new Rq1DatabaseField_UserReference(this, "Assignee"));
        addField(ASSIGNEE_FULLNAME = new Rq1DatabaseField_Text(this, "Assignee.fullname"));
        addField(ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "Assignee.email"));
        addField(ASSIGNEE_LOGIN_NAME = new Rq1DatabaseField_Text(this, ATTRIBUTE_ASSIGNEE_LOGIN_NAME));

        ASSIGNEE.setFieldForFullName(ASSIGNEE_FULLNAME);
        ASSIGNEE.setFieldForEmail(ASSIGNEE_EMAIL);
        ASSIGNEE.setFieldForLoginName(ASSIGNEE_LOGIN_NAME);

        ASSIGNEE_FULLNAME.setNoWriteBack().setOptional();
        ASSIGNEE_EMAIL.setNoWriteBack().setOptional();
        ASSIGNEE_LOGIN_NAME.setNoWriteBack().setOptional();

        addField(BELONGS_TO_PROJECT = new Rq1DatabaseField_Reference(this, ATTRIBUTE_BELONGS_TO_PROJECT, Rq1RecordType.PROJECT));
    }

    @Override
    final public Rq1DatabaseField_Reference getProjectField() {
        return (BELONGS_TO_PROJECT);
    }
    
    @Override
    final public Rq1DatabaseField_Reference getAssigneeField() {
        return (ASSIGNEE);
    }

    @Override
    public void forward(Rq1Project project, Rq1RecordInterface newAssignee) {
        try {
            
            Rq1Client.client.forward(this, project, newAssignee);
            handleWriteSuccess();
        } catch (WriteToDatabaseRejected ex) {
            handleWriteError(ex);
        } catch (Exception | Error ex) {
            handleWriteError(ex);
        }
    }

}
