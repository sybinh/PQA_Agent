/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_UserReference;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1LinkDescription;

public class Rq1Map extends Rq1BaseRecord implements Rq1LinkInterface {

    final static public String dummyOslcShortTitle = "00000000";
    //
    // Field definitions
    //
    final public Rq1DatabaseField_Text ASSIGNEE_FULLNAME;
    final public Rq1DatabaseField_Text ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text ASSIGNEE_LOGIN_NAME;
    final public Rq1DatabaseField_UserReference ASSIGNEE;

    protected Rq1Map(Rq1LinkDescription mapDescription) {
        super(mapDescription);

        addField(ASSIGNEE_FULLNAME = new Rq1DatabaseField_Text(this, "Assignee.fullname"));
        addField(ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "Assignee.email"));
        addField(ASSIGNEE_LOGIN_NAME = new Rq1DatabaseField_Text(this, Rq1Release.ATTRIBUTE_ASSIGNEE_LOGIN_NAME));
        addField(ASSIGNEE = new Rq1DatabaseField_UserReference(this, "Assignee"));

        ASSIGNEE.setFieldForFullName(ASSIGNEE_FULLNAME);
        ASSIGNEE.setFieldForEmail(ASSIGNEE_EMAIL);
        ASSIGNEE.setFieldForLoginName(ASSIGNEE_LOGIN_NAME);

        ASSIGNEE_FULLNAME.setNoWriteBack().setOptional();
        ASSIGNEE_EMAIL.setNoWriteBack().setOptional();
        ASSIGNEE_LOGIN_NAME.setNoWriteBack().setOptional();

    }

    @Override
    final public void reload() {
        if (existsInDatabase() == true) {
            Rq1Client.client.loadRecordByIdentifier(this.getOslcRecordIdentifier());
        }
    }

    @Override
    public String toString() {
        return (getOslcShortTitle());
    }

}
