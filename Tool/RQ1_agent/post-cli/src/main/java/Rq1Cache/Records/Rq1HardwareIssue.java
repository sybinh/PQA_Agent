/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.Enumerations.AllocationHWState;
import Rq1Data.Enumerations.CommercialClassification;
import Rq1Data.Enumerations.CommercialQuotationReq;
import Rq1Data.Enumerations.HardwareIssueCategory;
import Rq1Data.Enumerations.HardwareIssueClassification;
import Rq1Data.Enumerations.OfferType;
import Rq1Data.Enumerations.SalesInfo;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Enumerations.ZlkReview;

/**
 *
 * @author gug2wi
 */
public class Rq1HardwareIssue extends Rq1Issue {

    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Enumeration CLASSIFICATION;
    final public Rq1XmlSubField_Text RELATED_SW_ISSUE;
    final public Rq1DatabaseField_Enumeration ALLOCATION;

    final public Rq1XmlSubField_Text ASAM_SALES_COMMENT;
    final public Rq1DatabaseField_Reference COMMERCIAL_ASSIGNEE;
    final public Rq1DatabaseField_Text COMMERCIAL_ASSIGNEE_FULLNAME;
    final public Rq1DatabaseField_Text COMMERCIAL_ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text COMMERCIAL_ASSIGNEE_LOGIN_NAME;
    final public Rq1DatabaseField_Enumeration COMMERCIAL_CLASSIFICATION;
    final public Rq1DatabaseField_Text COMMERCIAL_QUOTATION_ID;
    final public Rq1DatabaseField_Enumeration COMMERCIAL_QUOTATION_REQUIRED;
    final public Rq1DatabaseField_Enumeration OEM_PROPOSAL_ADOPTED;
    final public Rq1XmlSubField_Enumeration OFFER_TYPE;
    final public Rq1XmlSubField_Enumeration SALES_INFO;
    final public Rq1XmlSubField_Enumeration ZLK_REVIEW;

    public Rq1HardwareIssue(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, "Category", HardwareIssueCategory.values()));
        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", HardwareIssueClassification.values()));

        addField(RELATED_SW_ISSUE = new Rq1XmlSubField_Text(this, TAGS, "Related_SW_Issue"));
        addField(ALLOCATION = new Rq1DatabaseField_Enumeration(this, "Allocation", AllocationHWState.values()));

        addField(ASAM_SALES_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "ASAM_Sales_Comment"));
        addField(COMMERCIAL_ASSIGNEE = new Rq1DatabaseField_Reference(this, "CommercialAssignee", Rq1RecordType.USER));
        addField(COMMERCIAL_ASSIGNEE_FULLNAME = new Rq1DatabaseField_Text(this, "CommercialAssignee.fullname"));
        addField(COMMERCIAL_ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "CommercialAssignee.email"));
        addField(COMMERCIAL_ASSIGNEE_LOGIN_NAME = new Rq1DatabaseField_Text(this, "CommercialAssignee.login_name"));
        COMMERCIAL_ASSIGNEE_FULLNAME.setNoWriteBack().setOptional();
        COMMERCIAL_ASSIGNEE_EMAIL.setNoWriteBack().setOptional();
        COMMERCIAL_ASSIGNEE_LOGIN_NAME.setNoWriteBack().setOptional();
        addField(COMMERCIAL_CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "CommercialClassification", CommercialClassification.values()));
        addField(COMMERCIAL_QUOTATION_ID = new Rq1DatabaseField_Text(this, "CommercialQuotationId"));
        addField(COMMERCIAL_QUOTATION_REQUIRED = new Rq1DatabaseField_Enumeration(this, "CommercialQuotationReq", CommercialQuotationReq.values()));
        addField(OEM_PROPOSAL_ADOPTED = new Rq1DatabaseField_Enumeration(this, "OEMProposalAdopted", YesNoEmpty.values()));
        addField(OFFER_TYPE = new Rq1XmlSubField_Enumeration(this, TAGS, "OFFERTYPE", OfferType.values(), null));
        OFFER_TYPE.acceptInvalidValuesInDatabase().setOptional();
        addField(SALES_INFO = new Rq1XmlSubField_Enumeration(this, TAGS, "SALESINFO", SalesInfo.values(), null));
        SALES_INFO.acceptInvalidValuesInDatabase().setOptional();
        addField(ZLK_REVIEW = new Rq1XmlSubField_Enumeration(this, TAGS, "ZLKREVIEW", ZlkReview.values(), null));
        ZLK_REVIEW.acceptInvalidValuesInDatabase().setOptional();
    }

}
