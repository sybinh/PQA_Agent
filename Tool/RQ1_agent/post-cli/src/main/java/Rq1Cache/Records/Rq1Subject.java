/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1NodeDescription;
import OslcAccess.OslcProtocolVersion;
import Rq1Cache.Fields.Rq1DatabaseField_Text;

public class Rq1Subject extends Rq1BaseRecord implements Rq1SubjectInterface {

    final static public Rq1AttributeName ATTRIBUTE_TITLE = new Rq1AttributeName("Title");

    final public Rq1DatabaseField_Text TITLE;
    final public Rq1DatabaseField_ReferenceList ATTACHMENTS;

    protected Rq1Subject(Rq1NodeDescription subjectDescription) {
        this(subjectDescription, null);
    }

    protected Rq1Subject(Rq1NodeDescription subjectDescription, Rq1AttributeName attributeNameForDescription) {
        super(subjectDescription, attributeNameForDescription);

        addField(TITLE = new Rq1DatabaseField_Text(this, ATTRIBUTE_TITLE));
        addField(ATTACHMENTS = new Rq1DatabaseField_ReferenceList(this, "Attachments", Rq1NodeDescription.ATTACHMENT.getRecordType(), OslcProtocolVersion.OSLC_10));
    }

    @Override
    final public String getRq1Id() {
        return (getOslcShortTitle());
    }

    @Override
    public synchronized void reload() {
        if (existsInDatabase() == true) {
            Rq1Client.client.loadRecordByIdentifier(this.getOslcRecordIdentifier());
        }
    }

    @Override
    public String toString() {
        return (getOslcShortTitle());
    }

}
