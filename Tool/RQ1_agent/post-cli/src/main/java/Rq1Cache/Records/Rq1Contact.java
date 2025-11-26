/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Enumerations.YesNoEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class Rq1Contact extends Rq1Record implements Rq1NodeInterface {

    final private static String FIELDNAME_ORGANIZATION = "Organization";
    final private static String FIELDNAME_ACTIVE_CONTACT = "ActiveContact";

    final public Rq1DatabaseField_Enumeration ACTIVE_CONTACT;
    final public Rq1DatabaseField_Text DEPARTMENT;
    final public Rq1DatabaseField_Text DESCRIPTION;
    final public Rq1DatabaseField_Text EMAIL;
    final public Rq1DatabaseField_Text FIRST_NAME;
    final public Rq1DatabaseField_Text LAST_NAME;
    final public Rq1DatabaseField_Text NAME;
    final public Rq1DatabaseField_Text ORGANIZATION;
    final public Rq1DatabaseField_Text PHONE_NUMBERS;
    final public Rq1DatabaseField_Text SUBMIT_DATE;
    final public Rq1DatabaseField_Text SUBMITTER;

    public Rq1Contact() {
        super(Rq1NodeDescription.CONTACT);

        addField(ACTIVE_CONTACT = new Rq1DatabaseField_Enumeration(this, FIELDNAME_ACTIVE_CONTACT, YesNoEmpty.values(), YesNoEmpty.EMPTY));
        addField(DEPARTMENT = new Rq1DatabaseField_Text(this, "Department"));
        addField(DESCRIPTION = new Rq1DatabaseField_Text(this, "Description"));
        addField(EMAIL = new Rq1DatabaseField_Text(this, "eMail"));
        addField(FIRST_NAME = new Rq1DatabaseField_Text(this, "FirstName"));
        addField(LAST_NAME = new Rq1DatabaseField_Text(this, "LastName"));
        addField(NAME = new Rq1DatabaseField_Text(this, "Name"));
        addField(ORGANIZATION = new Rq1DatabaseField_Text(this, FIELDNAME_ORGANIZATION));
        addField(PHONE_NUMBERS = new Rq1DatabaseField_Text(this, "PhoneNumbers"));
        addField(SUBMIT_DATE = new Rq1DatabaseField_Text(this, "SubmitDate"));
        addField(SUBMITTER = new Rq1DatabaseField_Text(this, "Submitter"));
    }

    @Override
    final public void reload() {

    }

    /**
     * Returns a list of all active contacts that belongs to one of the given
     * organizations.
     *
     * @param organizations List of organizations that shall be returned.
     * @return
     */
    static public List<Rq1Contact> getActiveContactsForOrganization(String[] organizations) {

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.CONTACT.getRecordType());
        query.addCriteria_ValueList(FIELDNAME_ORGANIZATION, organizations);
        query.addCriteria_Value(FIELDNAME_ACTIVE_CONTACT, YesNoEmpty.YES.getText());

        //
        // Handle result
        //
        List<Rq1Contact> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1Contact) {
                result.add((Rq1Contact) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

}
