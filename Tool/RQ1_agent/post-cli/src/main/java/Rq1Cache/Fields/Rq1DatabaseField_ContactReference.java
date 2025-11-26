/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Rq1Cache.Records.Rq1Contact;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Reference;

/**
 * Special field to support the setting of a RQ1 contact via template.
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_ContactReference extends Rq1DatabaseField_Reference {

    private Rq1DatabaseField_Text fieldForName = null;
    private Rq1DatabaseField_Text fieldForOrganization = null;
    private Rq1DatabaseField_Text fieldForDepartment = null;
    private Rq1DatabaseField_Text fieldForEmail = null;
    private Rq1DatabaseField_Text fieldForPhone = null;

    public Rq1DatabaseField_ContactReference(Rq1RecordInterface parent, String dbFieldName) {
        super(parent, dbFieldName, Rq1RecordType.CONTACT);
    }

    public void setFieldForName(Rq1DatabaseField_Text fieldForName) {
        this.fieldForName = fieldForName;
    }

    public void setFieldForOrganization(Rq1DatabaseField_Text fieldForOrganization) {
        this.fieldForOrganization = fieldForOrganization;
    }

    public void setFieldForDepartment(Rq1DatabaseField_Text fieldForDepartment) {
        this.fieldForDepartment = fieldForDepartment;
    }

    public void setFieldForEmail(Rq1DatabaseField_Text fieldForEmail) {
        this.fieldForEmail = fieldForEmail;
    }

    public void setFieldForPhone(Rq1DatabaseField_Text fieldForPhone) {
        this.fieldForPhone = fieldForPhone;
    }

    @Override
    public void setValueFromTemplate(String value) {
        super.setValueFromTemplate(value);

        Rq1Reference contactReference = getDataModelValue();
        if (contactReference != null) {
            Rq1RecordInterface contact = contactReference.getRecord();
            if (contact instanceof Rq1Contact) {
                if (fieldForName != null) {
                    fieldForName.setValueFromTemplate(((Rq1Contact) contact).NAME.getDataModelValue());
                }
                if (fieldForOrganization != null) {
                    fieldForOrganization.setValueFromTemplate(((Rq1Contact) contact).ORGANIZATION.getDataModelValue());
                }
                if (fieldForDepartment != null) {
                    fieldForDepartment.setValueFromTemplate(((Rq1Contact) contact).DEPARTMENT.getDataModelValue());
                }
                if (fieldForEmail != null) {
                    fieldForEmail.setValueFromTemplate(((Rq1Contact) contact).EMAIL.getDataModelValue());
                }
                if (fieldForPhone != null) {
                    fieldForPhone.setValueFromTemplate(((Rq1Contact) contact).PHONE_NUMBERS.getDataModelValue());
                }
            }
        }
    }

}
