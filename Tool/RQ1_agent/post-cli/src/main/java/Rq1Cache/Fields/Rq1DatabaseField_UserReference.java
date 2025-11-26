/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Records.Rq1User;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Reference;

/**
 * Special field to support the setting of a RQ1 user via template.
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_UserReference extends Rq1DatabaseField_Reference {

    private Rq1DatabaseField_Text fieldFullName = null;
    private Rq1DatabaseField_Text fieldLoginName = null;
    private Rq1DatabaseField_Text fieldForEmail = null;

    public Rq1DatabaseField_UserReference(Rq1RecordInterface parent, String dbFieldName) {
        super(parent, dbFieldName, Rq1RecordType.CONTACT);
    }

    public void setFieldForFullName(Rq1DatabaseField_Text fieldFullName) {
        this.fieldFullName = fieldFullName;
    }

    public void setFieldForLoginName(Rq1DatabaseField_Text fieldLoginName) {
        this.fieldLoginName = fieldLoginName;
    }

    public void setFieldForEmail(Rq1DatabaseField_Text fieldForEmail) {
        this.fieldForEmail = fieldForEmail;
    }

    @Override
    public void setValueFromTemplate(String value) {
        super.setValueFromTemplate(value);

        Rq1Reference contactReference = getDataModelValue();
        if (contactReference != null) {
            Rq1RecordInterface contact = contactReference.getRecord();
            if (contact instanceof Rq1User) {
                if (fieldFullName != null) {
                    fieldFullName.setValueFromTemplate(((Rq1User) contact).FULLNAME.getDataModelValue());
                }
                if (fieldLoginName != null) {
                    fieldLoginName.setValueFromTemplate(((Rq1User) contact).LOGIN_NAME.getDataModelValue());
                }
                if (fieldForEmail != null) {
                    fieldForEmail.setValueFromTemplate(((Rq1User) contact).EMAIL.getDataModelValue());
                }
            }
        }
    }

}
