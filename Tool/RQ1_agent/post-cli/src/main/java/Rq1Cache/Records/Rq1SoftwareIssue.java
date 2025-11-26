/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsField_Xml;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_EnumerationSet;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.Allocation;
import Rq1Data.Enumerations.Category_YesNo;
import Rq1Data.Enumerations.SoftwareIssueCategory;
import Rq1Data.Enumerations.SoftwareIssueClassification;

/**
 *
 * @author gug2wi
 */
public class Rq1SoftwareIssue extends Rq1Issue {

    final static public Rq1AttributeName ATTRIBUTE_ALGORITHM_TO_REVIEW_COMMENT_FIELD = new Rq1AttributeName("AlgorithmsToReviewComment");
    final static public Rq1AttributeName ATTRIBUTE_ALGORITHM_TO_REVIEW = new Rq1AttributeName("AlgorithmsToReview");
    final static public Rq1AttributeName ATTRIBUTE_CATEGORY = new Rq1AttributeName("Category");
    final static public Rq1AttributeName ATTRIBUTE_CATEGORY_EXHAUST = new Rq1AttributeName("CategoryExhaust");
    final static public Rq1AttributeName ATTRIBUTE_CATEGORY_EXHAUST_COMMENT = new Rq1AttributeName("CategoryExhaustComment");
    final static public Rq1AttributeName ATTRIBUTE_CODEX_SUB_CATEGORY = new Rq1AttributeName("CodexSubCategory");
    final static public Rq1AttributeName ATTRIBUTE_PROCESS_TAILORING = new Rq1AttributeName("ProcessTailoring");

    final public Rq1XmlSubField_Text DEFECT_WORKPRODUCT_TYPE_COMMENT;

    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Enumeration CLASSIFICATION;
    final public Rq1XmlSubField_Text RELATED_HW_ISSUE;
    final public Rq1DatabaseField_Enumeration ALLOCATION;

    final public Rq1DatabaseField_Enumeration ALGORITHM_TO_REVIEW;
    final public Rq1DatabaseField_Text ALGORITHM_TO_REVIEW_COMMENT_FIELD;
    final public Rq1XmlSubField_Text ALGORITHM_TO_REVIEW_COMMENT_TAG;

    final public Rq1DatabaseField_Enumeration PROCESS_TAILORING;

    final public Rq1XmlSubField_Date OFFER_EXPIRES_BY;

    final public Rq1XmlSubField_Xml CATEGORIES;
    final public Rq1XmlSubField_Enumeration CATEGORY_SAFETY;
    final public Rq1XmlSubField_Text CATEGORY_SAFETY_COMMENT;
    final public Rq1XmlSubField_Enumeration CATEGORY_OBD;
    final public Rq1XmlSubField_Text CATEGORY_OBD_COMMENT;
    final public Rq1XmlSubField_Enumeration CATEGORY_EXHAUST_TAG;
    final public Rq1XmlSubField_Text CATEGORY_EXHAUST_COMMENT_TAG;

    final public Rq1DatabaseField_Enumeration CATEGORY_EXHAUST_FIELD;
    final public Rq1DatabaseField_Text CATEGORY_EXHAUST_COMMENT_FIELD;
    final public Rq1DatabaseField_EnumerationSet CODEX_SUB_CATEGORY;

    final public Rq1XmlSubField_Enumeration ALG2RV;
    final public Rq1XmlSubField_Text ALG2RV_COMMENT;

    public Rq1SoftwareIssue(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        addField(DEFECT_WORKPRODUCT_TYPE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DefectiveWorkproductType_Comment"));

        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, "Category", SoftwareIssueCategory.values()));
        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", SoftwareIssueClassification.values()));
        CLASSIFICATION.setReadOnly();
        addField(RELATED_HW_ISSUE = new Rq1XmlSubField_Text(this, TAGS, "Related_HW_Issue"));
        addField(ALLOCATION = new Rq1DatabaseField_Enumeration(this, "Allocation", Allocation.values()));

        addField(ALGORITHM_TO_REVIEW = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_ALGORITHM_TO_REVIEW, "Type=Issue FD", ""));
        ALGORITHM_TO_REVIEW.setOptional();
        ALGORITHM_TO_REVIEW.acceptInvalidValuesInDatabase();
        addField(ALGORITHM_TO_REVIEW_COMMENT_FIELD = new Rq1DatabaseField_Text(this, ATTRIBUTE_ALGORITHM_TO_REVIEW_COMMENT_FIELD));
        addField(ALGORITHM_TO_REVIEW_COMMENT_TAG = new Rq1XmlSubField_Text(this, TAGS, "AlgorithmsToReview_Comment"));
        ALGORITHM_TO_REVIEW_COMMENT_TAG.setOptional();

        addField(PROCESS_TAILORING = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_PROCESS_TAILORING, "Domain=Software", null));
        PROCESS_TAILORING.acceptInvalidValuesInDatabase();

        addField(OFFER_EXPIRES_BY = new Rq1XmlSubField_Date(this, MILESTONES, "OfferExpiresBy"));

        addField(CATEGORIES = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKOWN_ELEMENTS_NOT_ALLOWED, "Category"));
        CATEGORIES.addAlternativName("BesondereMerkmale");
        CATEGORIES.setOptional();
        addField(CATEGORY_SAFETY = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "Safety", Category_YesNo.values(), Category_YesNo.EMPTY));
        CATEGORY_SAFETY.addAlternativName("BM_S");
        CATEGORY_SAFETY.setOptional();
        addField(CATEGORY_SAFETY_COMMENT = new Rq1XmlSubField_Text(this, CATEGORIES, "Safety_Comment"));
        CATEGORY_SAFETY_COMMENT.addAlternativName("BM_S_Comment");
        CATEGORY_SAFETY_COMMENT.setOptional();
        addField(CATEGORY_OBD = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "OBD", Category_YesNo.values(), Category_YesNo.EMPTY));
        CATEGORY_OBD.addAlternativName("BM_O");
        CATEGORY_OBD.setOptional();
        addField(CATEGORY_OBD_COMMENT = new Rq1XmlSubField_Text(this, CATEGORIES, "OBD_Comment"));
        CATEGORY_OBD_COMMENT.addAlternativName("BM_O_Comment");
        CATEGORY_OBD_COMMENT.setOptional();
        addField(CATEGORY_EXHAUST_TAG = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "Exhaust", Category_YesNo.values(), Category_YesNo.EMPTY));
        CATEGORY_EXHAUST_TAG.addAlternativName("BM_D");
        CATEGORY_EXHAUST_TAG.setOptional();
        addField(CATEGORY_EXHAUST_COMMENT_TAG = new Rq1XmlSubField_Text(this, CATEGORIES, "Exhaust_Comment"));
        CATEGORY_EXHAUST_COMMENT_TAG.addAlternativName("BM_D_Comment");
        CATEGORY_EXHAUST_COMMENT_TAG.setOptional();

        addField(CATEGORY_EXHAUST_FIELD = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CATEGORY_EXHAUST, "Domain=Software", ""));
        CATEGORY_EXHAUST_FIELD.acceptInvalidValuesInDatabase();
        addField(CATEGORY_EXHAUST_COMMENT_FIELD = new Rq1DatabaseField_Text(this, ATTRIBUTE_CATEGORY_EXHAUST_COMMENT));
        addField(CODEX_SUB_CATEGORY = new Rq1DatabaseField_EnumerationSet(this, ATTRIBUTE_CODEX_SUB_CATEGORY, "Domain=Software"));
        CODEX_SUB_CATEGORY.acceptInvalidValuesInDatastore();

        addField(ALG2RV = new Rq1XmlSubField_Enumeration(this, TAGS, "Alg2rv", Category_YesNo.values(), Category_YesNo.EMPTY));
        ALG2RV.setOptional();
        addField(ALG2RV_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "Alg2rv_Comment"));
        ALG2RV_COMMENT.setOptional();

        DRBFM.setReadOnly();
    }

    @Override
    public synchronized boolean save(Rq1AttributeName[] fieldOrder) {
        if ((fieldOrder != null) && (fieldOrder.length > 0)) {
            return (super.save(fieldOrder));
        } else {
            Rq1AttributeName[] names = {ATTRIBUTE_CATEGORY, ATTRIBUTE_RELATED_SYSTEM, ATTRIBUTE_DEVELOPMENT_METHOD};
            return (super.save(names));
        }
    }

}
