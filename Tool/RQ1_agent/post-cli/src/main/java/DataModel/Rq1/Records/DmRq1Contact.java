/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Contact;
import UiSupport.UiTreeViewRootElementI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmRq1Contact extends DmRq1Element implements UiTreeViewRootElementI {

    final public DmRq1Field_Enumeration ACTIVE_CONTACT;
    final public DmRq1Field_Text DEPARTMENT;
    final public DmRq1Field_Text DESCRIPTION;
    final public DmRq1Field_Text EMAIL;
    final public DmRq1Field_Text FIRST_NAME;
    final public DmRq1Field_Text LAST_NAME;
    final public DmRq1Field_Text NAME;
    final public DmRq1Field_Text ORGANIZATION;
    final public DmRq1Field_Text PHONE_NUMBERS;
    final public DmRq1Field_Text SUBMIT_DATE;
    final public DmRq1Field_Text SUBMITTER;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Contact(Rq1Contact rq1Contact) {
        super("Contact", rq1Contact);

        //
        // Create and add fields
        //
        addField(ACTIVE_CONTACT = new DmRq1Field_Enumeration(this, rq1Contact.ACTIVE_CONTACT, "Active Contact"));
        addField(DEPARTMENT = new DmRq1Field_Text(this, rq1Contact.DEPARTMENT, "Department"));
        addField(DESCRIPTION = new DmRq1Field_Text(this, rq1Contact.DESCRIPTION, "Description"));
        addField(EMAIL = new DmRq1Field_Text(this, rq1Contact.EMAIL, "E-Mail"));
        addField(FIRST_NAME = new DmRq1Field_Text(this, rq1Contact.FIRST_NAME, "Firstname"));
        addField(LAST_NAME = new DmRq1Field_Text(this, rq1Contact.LAST_NAME, "Lastname"));
        addField(NAME = new DmRq1Field_Text(this, rq1Contact.NAME, "Name"));
        addField(ORGANIZATION = new DmRq1Field_Text(this, rq1Contact.ORGANIZATION, "Organization"));
        addField(PHONE_NUMBERS = new DmRq1Field_Text(this, rq1Contact.PHONE_NUMBERS, "Phone Numbers"));
        addField(SUBMIT_DATE = new DmRq1Field_Text(this, rq1Contact.SUBMIT_DATE, "Submit Date"));
        addField(SUBMITTER = new DmRq1Field_Text(this, rq1Contact.SUBMITTER, "Submitter"));
    }

    @Override
    public boolean isCanceled() {
        return (false);
    }

    @Override
    public String getTitle() {
        return (NAME.getValue());
    }

    @Override
    final public String toString() {
        return (NAME.getValue() + " - " + ORGANIZATION.getValue());
    }

    static public List<DmRq1Contact> getActiveContactsForOrganization(String[] organizations) {
        assert (organizations != null);

        List<DmRq1Contact> result = new ArrayList<>();
        for (Rq1Contact rq1Contact : Rq1Contact.getActiveContactsForOrganization(organizations)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Contact);
            if (dmElement instanceof DmRq1Contact) {
                result.add((DmRq1Contact) dmElement);
            }
        }

        return (result);
    }

    static public List<DmRq1Contact> getActiveContactsForProject(DmRq1Project project) {
        assert (project != null);

        String[] organizations;

        switch (project.getCustomer()) {
            case VW_GROUP:
                organizations = new String[]{"Audi", "IAV", "VW", "Volkswagen", "VAG", "Porsche"};
                break;
            case TATA_GROUP:
                organizations = new String[]{"JLR"};
                break;
            default:
                return (new ArrayList<>());
        }

        return (getActiveContactsForOrganization(organizations));
    }

}
