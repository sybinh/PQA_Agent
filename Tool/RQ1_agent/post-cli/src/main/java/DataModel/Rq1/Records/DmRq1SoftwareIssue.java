/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_ECVTOOL_5517_CategoryExhaust;
import DataModel.Rq1.Fields.DmRq1Field_ECVTOOL_5517_CategoryExhaustComment;
import DataModel.DmFieldI;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import DataModel.DmValueFieldI_EnumerationSet;
import DataModel.DmValueFieldI_Enumeration_EagerLoad;
import DataModel.DmValueFieldI_Text;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_EnumerationSet;
import DataModel.Rq1.Fields.DmRq1Field_ExternalAlgorithmToReview;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Fields.Rq1DatabaseField_EnumerationSet;
import Rq1Cache.Records.Rq1SoftwareIssue;

/**
 *
 * @author gug2wi
 */
public class DmRq1SoftwareIssue extends DmRq1Issue {

    final public DmRq1Field_Text DEFECT_WORKPRODUCT_TYPE;
    final public DmRq1Field_Text DEFECT_WORKPRODUCT_TYPE_COMMENT;

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;
    final public DmRq1Field_Text RELATED_HW_ISSUE;
    final public DmRq1Field_Enumeration ALLOCATION;

    final public DmRq1Field_Enumeration ALGORITHM_TO_REVIEW;
    final public DmRq1Field_Text ALGORITHM_TO_REVIEW_COMMENT;
    final public DmValueFieldI_Enumeration_EagerLoad EXTERNAL_ALGORITHM_TO_REVIEW;
    final public DmValueFieldI_Text EXTERNAL_ALGORITHM_TO_REVIEW_COMMENT;
    final public DmValueFieldI_Text EXTERNAL_TAGS;

    final public DmRq1Field_Enumeration PROCESS_TAILORING;

    final public DmRq1Field_Date OFFER_EXPIRES_BY;

    final public DmValueFieldI_Enumeration_EagerLoad CATEGORY_SAFETY;
    final public DmValueFieldI_Text CATEGORY_SAFETY_COMMENT;
    final public DmValueFieldI_Enumeration_EagerLoad CATEGORY_OBD;
    final public DmValueFieldI_Text CATEGORY_OBD_COMMENT;
    /* Removed for ECVTOOL-5517
    final public DmRq1Field_Enumeration CATEGORY_EXHAUST;
    final public DmRq1Field_Text CATEGORY_EXHAUST_COMMENT;
     */
    final public DmRq1Field_ECVTOOL_5517_CategoryExhaust CATEGORY_EXHAUST;
    final public DmRq1Field_ECVTOOL_5517_CategoryExhaustComment CATEGORY_EXHAUST_COMMENT;
    final public DmValueFieldI_EnumerationSet CODEX_SUB_CATEGORY;

    final public static String BMW_IMPACT_ANALYSE = "https://wi0psec01.emea.bosch.com/ImpactAnalysis/?id=";
    final public static String IMPACT_ANALYSE = "http://si0vmc4091.de.bosch.com/";

    public DmRq1SoftwareIssue(String subjectType, Rq1SoftwareIssue rq1SoftwareIssue) {
        super(subjectType, rq1SoftwareIssue);

        addField(DEFECT_WORKPRODUCT_TYPE = new DmRq1Field_Text(this, rq1SoftwareIssue.DEFECT_WORKPRODUCT_TYPE, "Defective Work Product Type"));
        addField(DEFECT_WORKPRODUCT_TYPE_COMMENT = new DmRq1Field_Text(this, rq1SoftwareIssue.DEFECT_WORKPRODUCT_TYPE_COMMENT, "Defective Work Product Type Comment"));

        addField(CATEGORY = new DmRq1Field_Enumeration(this, rq1SoftwareIssue.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1SoftwareIssue.CLASSIFICATION, "Classification"));
        addField(RELATED_HW_ISSUE = new DmRq1Field_Text(this, rq1SoftwareIssue.RELATED_HW_ISSUE, "Related HW Issue"));
        addField(ALLOCATION = new DmRq1Field_Enumeration(this, rq1SoftwareIssue.ALLOCATION, "Allocation"));

        addField(ALGORITHM_TO_REVIEW = new DmRq1Field_Enumeration(this, rq1SoftwareIssue.ALGORITHM_TO_REVIEW, "Algorithms to Review"));
        addField(ALGORITHM_TO_REVIEW_COMMENT = new DmRq1Field_Text(this, rq1SoftwareIssue.ALGORITHM_TO_REVIEW_COMMENT_FIELD, "Algorithms to Review Comment"));
        ALGORITHM_TO_REVIEW_COMMENT.addAlternativeField(rq1SoftwareIssue.ALGORITHM_TO_REVIEW_COMMENT_TAG);
        ALGORITHM_TO_REVIEW_COMMENT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);

        DmRq1Field_ExternalAlgorithmToReview originalExternalTags = new DmRq1Field_ExternalAlgorithmToReview(rq1SoftwareIssue.EXTERNAL_TAGS);
        addField(EXTERNAL_ALGORITHM_TO_REVIEW = originalExternalTags.getAlgorithmField());
        addField(EXTERNAL_ALGORITHM_TO_REVIEW_COMMENT = originalExternalTags.getCommentField());
        addField(EXTERNAL_TAGS = originalExternalTags.getExternalTagsField());
        EXTERNAL_TAGS.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(PROCESS_TAILORING = new DmRq1Field_Enumeration(rq1SoftwareIssue.PROCESS_TAILORING, "Process Tailoring"));

        addField(OFFER_EXPIRES_BY = new DmRq1Field_Date(this, rq1SoftwareIssue.OFFER_EXPIRES_BY, "Offer Expires By"));
        OFFER_EXPIRES_BY.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        OFFER_EXPIRES_BY.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(CATEGORY_SAFETY = new DmRq1Field_Enumeration(this, rq1SoftwareIssue.CATEGORY_SAFETY, "Category Safety"));
        addField(CATEGORY_SAFETY_COMMENT = new DmRq1Field_Text(this, rq1SoftwareIssue.CATEGORY_SAFETY_COMMENT, "Category Safety Comment"));
        addField(CATEGORY_OBD = new DmRq1Field_Enumeration(this, rq1SoftwareIssue.CATEGORY_OBD, "Category OBD"));
        addField(CATEGORY_OBD_COMMENT = new DmRq1Field_Text(this, rq1SoftwareIssue.CATEGORY_OBD_COMMENT, "Category OBD Comment"));

        /* Removed for ECVTOOL-5517
        addField(CATEGORY_EXHAUST = new DmRq1Field_Enumeration(rq1SoftwareIssue.CATEGORY_EXHAUST_FIELD, "Cat Exhaust"));
        CATEGORY_EXHAUST.addAlternativeField(rq1SoftwareIssue.CATEGORY_EXHAUST_TAG);
        addField(CATEGORY_EXHAUST_COMMENT = new DmRq1Field_Text(rq1SoftwareIssue.CATEGORY_EXHAUST_COMMENT_FIELD, "Category Exhaust Comment"));
        CATEGORY_EXHAUST_COMMENT.addAlternativeField(rq1SoftwareIssue.CATEGORY_EXHAUST_COMMENT_TAG);
        CATEGORY_EXHAUST_COMMENT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
         */
        addField(CATEGORY_EXHAUST = new DmRq1Field_ECVTOOL_5517_CategoryExhaust(rq1SoftwareIssue.CATEGORY_EXHAUST_FIELD, rq1SoftwareIssue.CATEGORY_EXHAUST_TAG, "Category Exhaust"));
        addField(CATEGORY_EXHAUST_COMMENT = new DmRq1Field_ECVTOOL_5517_CategoryExhaustComment(rq1SoftwareIssue.CATEGORY_EXHAUST_COMMENT_FIELD, rq1SoftwareIssue.CATEGORY_EXHAUST_COMMENT_TAG, "Category Exhaust Comment"));
        CATEGORY_EXHAUST.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_EXHAUST_COMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);

        Rq1DatabaseField_EnumerationSet a = rq1SoftwareIssue.CODEX_SUB_CATEGORY;
        addField(CODEX_SUB_CATEGORY = new DmRq1Field_EnumerationSet(rq1SoftwareIssue.CODEX_SUB_CATEGORY, "Codex Sub-Category"));
        CODEX_SUB_CATEGORY.setAttribute(FIELD_FOR_BULK_OPERATION);

    }

    @Override
    public Class<? extends DmRq1Project> getTargetProjectClass() {
        return DmRq1SoftwareProject.class;
    }

    static public class ExistsAlreadyException extends Exception {

    }
}
