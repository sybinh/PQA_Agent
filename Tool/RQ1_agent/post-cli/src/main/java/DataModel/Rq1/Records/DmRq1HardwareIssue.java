/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1HardwareIssue;

/**
 *
 * @author gug2wi
 */
public class DmRq1HardwareIssue extends DmRq1Issue {

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;
    final public DmRq1Field_Text DEFECT_CLASSIFICATION;
    final public DmRq1Field_Text DEFECT_WORKPRODUCT_TYPE;
    final public DmRq1Field_Text RELATED_SW_ISSUE;
    final public DmRq1Field_Enumeration ALLOCATION;

    final public DmRq1Field_Text EXTERNAL_TAGS;

    final public DmRq1Field_Enumeration COMMERCIAL_CLASSIFICATION;
    final public DmRq1Field_Enumeration OEM_PROPOSAL_ADOPTED;
    final public DmRq1Field_Enumeration COMMERCIAL_QUOTATION_REQ;
    final public DmRq1Field_Enumeration OFFER_TYPE;
    final public DmRq1Field_Enumeration SALES_INFO;
    final public DmRq1Field_Enumeration ZLK_REVIEW;
    final public DmRq1Field_Reference<DmRq1User> COMMERCIAL_ASSIGNEE;
    final public DmRq1Field_Text COMMERCIAL_ASSIGNEE_FULLNAME;
    final public DmRq1Field_Text COMMERCIAL_ASSIGNEE_EMAIL;
    final public DmRq1Field_Text COMMERCIAL_ASSIGNEE_LOGIN_NAME;
    final public DmRq1Field_Text COMMERCIAL_QUOTATION_ID;
    final public DmRq1Field_Text ASAM_SALES_COMMENT;

    public DmRq1HardwareIssue(String subjectType, Rq1HardwareIssue rq1HardwareIssue) {
        super(subjectType, rq1HardwareIssue);

        addField(CATEGORY = new DmRq1Field_Enumeration(this, rq1HardwareIssue.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1HardwareIssue.CLASSIFICATION, "Classification"));
        addField(DEFECT_CLASSIFICATION = new DmRq1Field_Text(this, rq1HardwareIssue.DEFECT_CLASSIFICATION, "Defect Classification"));
        addField(DEFECT_WORKPRODUCT_TYPE = new DmRq1Field_Text(this, rq1HardwareIssue.DEFECT_WORKPRODUCT_TYPE, "Defective Work Product Type"));
        addField(RELATED_SW_ISSUE = new DmRq1Field_Text(this, rq1HardwareIssue.RELATED_SW_ISSUE, "Related SW Issue"));
        addField(ALLOCATION = new DmRq1Field_Enumeration(this, rq1HardwareIssue.ALLOCATION, "Allocation"));

        addField(EXTERNAL_TAGS = new DmRq1Field_Text(this, rq1HardwareIssue.EXTERNAL_TAGS, "External Tags"));
        EXTERNAL_TAGS.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(ASAM_SALES_COMMENT = new DmRq1Field_Text(this, rq1HardwareIssue.ASAM_SALES_COMMENT, "ASAM Sales Comment"));
        addField(COMMERCIAL_ASSIGNEE = new DmRq1Field_Reference<>(this, rq1HardwareIssue.COMMERCIAL_ASSIGNEE, "Commercial Assignee"));
        addField(COMMERCIAL_ASSIGNEE_FULLNAME = new DmRq1Field_Text(this, rq1HardwareIssue.COMMERCIAL_ASSIGNEE_FULLNAME, "Fullname Commercial Assignee"));
        addField(COMMERCIAL_ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1HardwareIssue.COMMERCIAL_ASSIGNEE_EMAIL, "E-Mail Commercial Assignee"));
        addField(COMMERCIAL_ASSIGNEE_LOGIN_NAME = new DmRq1Field_Text(this, rq1HardwareIssue.COMMERCIAL_ASSIGNEE_LOGIN_NAME, "Shortcut Commercial Assignee"));
        addField(COMMERCIAL_CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1HardwareIssue.COMMERCIAL_CLASSIFICATION, "Commercial Classification"));
        addField(COMMERCIAL_QUOTATION_ID = new DmRq1Field_Text(this, rq1HardwareIssue.COMMERCIAL_QUOTATION_ID, "Commercial Quotation ID"));
        addField(COMMERCIAL_QUOTATION_REQ = new DmRq1Field_Enumeration(this, rq1HardwareIssue.COMMERCIAL_QUOTATION_REQUIRED, "Commercial Quotation Req"));
        addField(OEM_PROPOSAL_ADOPTED = new DmRq1Field_Enumeration(this, rq1HardwareIssue.OEM_PROPOSAL_ADOPTED, "OEM Proposal Adopted"));

        addField(OFFER_TYPE = new DmRq1Field_Enumeration(this, rq1HardwareIssue.OFFER_TYPE, "Offer Type"));
        addField(SALES_INFO = new DmRq1Field_Enumeration(this, rq1HardwareIssue.SALES_INFO, "Sales Info"));
        addField(ZLK_REVIEW = new DmRq1Field_Enumeration(this, rq1HardwareIssue.ZLK_REVIEW, "ZLK Review"));

        PROJECT_CONFIG.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);
    }

}
