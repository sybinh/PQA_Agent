/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_ContactReference;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Enumerations.CategoryFcRelease;
import Rq1Data.Enumerations.ClassificationFcRelease;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class Rq1FcRelease extends Rq1Fc {

    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Enumeration CLASSIFICATION;

    final public Rq1DatabaseField_ContactReference EXTERNAL_SUBMITTER;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_NAME;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_ORGANIZATION;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_DEPARTMENT;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_EMAIL;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_PHONE;

    final public Rq1DatabaseField_ContactReference EXTERNAL_ASSIGNEE;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_NAME;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_PHONE;

    public Rq1FcRelease() {
        super(Rq1NodeDescription.FC_RELEASE);

        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CATEGORY, CategoryFcRelease.values()));
        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", ClassificationFcRelease.values()));

        SCOPE.setReadOnly();

        addField(EXTERNAL_SUBMITTER = new Rq1DatabaseField_ContactReference(this, "ExternalSubmitter"));
        addField(EXTERNAL_SUBMITTER_NAME = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Name"));
        addField(EXTERNAL_SUBMITTER_ORGANIZATION = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Organization"));
        addField(EXTERNAL_SUBMITTER_DEPARTMENT = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Department"));
        addField(EXTERNAL_SUBMITTER_EMAIL = new Rq1DatabaseField_Text(this, "ExternalSubmitter.eMail"));
        addField(EXTERNAL_SUBMITTER_PHONE = new Rq1DatabaseField_Text(this, "ExternalSubmitter.PhoneNumbers"));
        EXTERNAL_SUBMITTER.setFieldForName(EXTERNAL_SUBMITTER_NAME);
        EXTERNAL_SUBMITTER.setFieldForOrganization(EXTERNAL_SUBMITTER_ORGANIZATION);
        EXTERNAL_SUBMITTER.setFieldForDepartment(EXTERNAL_SUBMITTER_DEPARTMENT);
        EXTERNAL_SUBMITTER.setFieldForEmail(EXTERNAL_SUBMITTER_EMAIL);
        EXTERNAL_SUBMITTER.setFieldForPhone(EXTERNAL_SUBMITTER_PHONE);
        EXTERNAL_SUBMITTER_NAME.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_ORGANIZATION.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_DEPARTMENT.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_EMAIL.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_PHONE.setOptional().setNoWriteBack();

        addField(EXTERNAL_ASSIGNEE = new Rq1DatabaseField_ContactReference(this, "ExternalAssignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new Rq1DatabaseField_Text(this, "ExternalAssignee.Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new Rq1DatabaseField_Text(this, "ExternalAssignee.Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "ExternalAssignee.eMail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new Rq1DatabaseField_Text(this, "ExternalAssignee.PhoneNumbers"));
        EXTERNAL_ASSIGNEE.setFieldForName(EXTERNAL_ASSIGNEE_NAME);
        EXTERNAL_ASSIGNEE.setFieldForOrganization(EXTERNAL_ASSIGNEE_ORGANIZATION);
        EXTERNAL_ASSIGNEE.setFieldForDepartment(EXTERNAL_ASSIGNEE_DEPARTMENT);
        EXTERNAL_ASSIGNEE.setFieldForEmail(EXTERNAL_ASSIGNEE_EMAIL);
        EXTERNAL_ASSIGNEE.setFieldForPhone(EXTERNAL_ASSIGNEE_PHONE);
        EXTERNAL_ASSIGNEE_NAME.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_ORGANIZATION.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_DEPARTMENT.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_EMAIL.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_PHONE.setOptional().setNoWriteBack();
    }

    static public Iterable<Rq1FcRelease> getAllChangedLaterOrEqual(EcvDate date, EnumSet<LifeCycleState_Release> wantedLifeCycleState) {
        assert (date != null);
        assert (date.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.FC_RELEASE.getRecordType());
        Rq1NodeDescription.FC_RELEASE.setFixedRecordCriterias(query);
        query.addCriteria_isLaterOrEqualThen(ATTRIBUTE_LAST_MODIFIED_DATE, date);
        if (wantedLifeCycleState != null) {
            query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, wantedLifeCycleState);
        }

        //
        // Handle result
        //
        List<Rq1FcRelease> result = new ArrayList<>();
        List<Rq1Reference> referenzList = query.getReferenceList();
        for (Rq1Reference r : referenzList) {
            if (r.getRecord() instanceof Rq1FcRelease) {
                result.add((Rq1FcRelease) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }
        return (result);
    }
}
