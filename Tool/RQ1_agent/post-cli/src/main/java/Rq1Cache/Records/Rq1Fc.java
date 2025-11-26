/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsField_Xml;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Enumerations.Category_Exhaust_Comments;
import Rq1Data.Enumerations.Category_OBD_Comments;
import Rq1Data.Enumerations.Category_YesNo;
import Rq1Data.Enumerations.DevelopmentMethod;
import Rq1Data.Enumerations.ExternalDescriptionReviewQuality;
import Rq1Data.Enumerations.ExternalDescriptionReviewStatus;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.NeutralCalibration_FC;
import Rq1Data.Enumerations.ProcessTailoringFc;
import Rq1Data.Enumerations.Sync;
import Rq1Data.Enumerations.YesNoEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class Rq1Fc extends Rq1SoftwareRelease {

    /**
     * Fields for Sync RQ1 and ALM
     */
    final public Rq1DatabaseField_Enumeration SYNC;
    final public Rq1DatabaseField_Text LINKS;
    /**
     *
     */
    final public Rq1XmlSubField_Enumeration NEUTRAL_CALIBRATION;

    final public Rq1XmlSubField_Xml CATEGORIES;
    final public Rq1XmlSubField_Enumeration CATEGORY_SAFETY;
    final public Rq1XmlSubField_Text CATEGORY_SAFETY_COMMENT;
    final public Rq1XmlSubField_Text CATEGORY_SAFETY_CONDITION;
    final public Rq1XmlSubField_Enumeration CATEGORY_OBD;
    final public Rq1XmlSubField_Enumeration CATEGORY_OBD_COMMENT;
    final public Rq1XmlSubField_Text CATEGORY_OBD_CONDITION;
    final public Rq1XmlSubField_Enumeration CATEGORY_EXHAUST;
    final public Rq1XmlSubField_Enumeration CATEGORY_EXHAUST_COMMENT;
    final public Rq1XmlSubField_Text CATEGORY_EXHAUST_CONDITION;
    final public Rq1XmlSubField_Enumeration FNC_CAT_DOCU_UPDATED;
    final public Rq1XmlSubField_Enumeration ECV_KONZERN_BASELINE;

    final public Rq1XmlSubField_Enumeration EXTERNAL_DESCRIPTION_REVIEW_STATUS;
    final public Rq1XmlSubField_Enumeration EXTERNAL_DESCRIPTION_REVIEW_QUALITY;

    final public Rq1DatabaseField_Enumeration PROCESS_TAILORING;
    final public Rq1DatabaseField_Enumeration DEVELOPMENT_METHOD;
    final public Rq1XmlSubField_Text DEVELOPMENT_METHOD_COMMENT;

    //FLOW
    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Text FLOW_RANK;

    public Rq1Fc(Rq1NodeDescription nodeDescription) {
        super(nodeDescription);

        addField(SYNC = new Rq1DatabaseField_Enumeration(this, "Sync", Sync.values()));
        SYNC.acceptInvalidValuesInDatabase();
        addField(LINKS = new Rq1DatabaseField_Text(this, "Links"));
        LINKS.setReadOnly();

        addField(NEUTRAL_CALIBRATION = new Rq1XmlSubField_Enumeration(this, TAGS, "NeutralCalibration", NeutralCalibration_FC.values(), NeutralCalibration_FC.EMPTY));

        addField(PROCESS_TAILORING = new Rq1DatabaseField_Enumeration(this, "ProcessTailoring", ProcessTailoringFc.values()));
        PROCESS_TAILORING.acceptInvalidValuesInDatabase();

        addField(CATEGORIES = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKOWN_ELEMENTS_NOT_ALLOWED, "Category"));
        CATEGORIES.addAlternativName("BesondereMerkmale");
        CATEGORIES.setOptional();
        addField(CATEGORY_SAFETY = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "Safety", Category_YesNo.values(), Category_YesNo.EMPTY));
        CATEGORY_SAFETY.addAlternativName("BM_S");
        CATEGORY_SAFETY.setOptional();
        addField(CATEGORY_SAFETY_COMMENT = new Rq1XmlSubField_Text(this, CATEGORIES, "Safety_Comment"));
        CATEGORY_SAFETY_COMMENT.addAlternativName("BM_S_Comment");
        CATEGORY_SAFETY_COMMENT.setOptional();
        CATEGORY_SAFETY_COMMENT.setReadOnly();
        addField(CATEGORY_SAFETY_CONDITION = new Rq1XmlSubField_Text(this, CATEGORIES, "Safety_Condition"));
        CATEGORY_SAFETY_CONDITION.setOptional();
        addField(CATEGORY_OBD = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "OBD", Category_YesNo.values(), Category_YesNo.EMPTY));
        CATEGORY_OBD.addAlternativName("BM_O");
        CATEGORY_OBD.setOptional();
        addField(CATEGORY_OBD_COMMENT = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "OBD_Comment", Category_OBD_Comments.values(), Category_OBD_Comments.EMPTY));
        CATEGORY_OBD_COMMENT.addAlternativName("BM_O_Comment");
        CATEGORY_OBD_COMMENT.setOptional();
        CATEGORY_OBD_COMMENT.acceptInvalidValuesInDatabase();
        addField(CATEGORY_OBD_CONDITION = new Rq1XmlSubField_Text(this, CATEGORIES, "OBD_Condition"));
        CATEGORY_OBD_CONDITION.setOptional();
        addField(CATEGORY_EXHAUST = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "Exhaust", Category_YesNo.values(), Category_YesNo.EMPTY));
        CATEGORY_EXHAUST.addAlternativName("BM_D");
        CATEGORY_EXHAUST.setOptional();
        addField(CATEGORY_EXHAUST_COMMENT = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "Exhaust_Comment", Category_Exhaust_Comments.values(), Category_Exhaust_Comments.EMPTY));
        CATEGORY_EXHAUST_COMMENT.addAlternativName("BM_D_Comment");
        CATEGORY_EXHAUST_COMMENT.setOptional();
        CATEGORY_EXHAUST_COMMENT.acceptInvalidValuesInDatabase();
        addField(CATEGORY_EXHAUST_CONDITION = new Rq1XmlSubField_Text(this, CATEGORIES, "Exhaust_Condition"));
        CATEGORY_EXHAUST_CONDITION.setOptional();

        addField(EXTERNAL_DESCRIPTION_REVIEW_STATUS = new Rq1XmlSubField_Enumeration(this, TAGS, "ExternalDescription_ReviewStatus", ExternalDescriptionReviewStatus.values(), ExternalDescriptionReviewStatus.EMPTY));
        EXTERNAL_DESCRIPTION_REVIEW_STATUS.setOptional();
        addField(EXTERNAL_DESCRIPTION_REVIEW_QUALITY = new Rq1XmlSubField_Enumeration(this, TAGS, "ExternalDescription_ReviewQuality", ExternalDescriptionReviewQuality.values(), ExternalDescriptionReviewQuality.EMPTY));
        EXTERNAL_DESCRIPTION_REVIEW_QUALITY.setOptional();

        addField(ECV_KONZERN_BASELINE = new Rq1XmlSubField_Enumeration(this, TAGS, "ECV_KonzernBaseline", Category_YesNo.values(), Category_YesNo.EMPTY));
        ECV_KONZERN_BASELINE.acceptInvalidValuesInDatabase();
        ECV_KONZERN_BASELINE.setReadOnly();

        addField(FNC_CAT_DOCU_UPDATED = new Rq1XmlSubField_Enumeration(this, CATEGORIES, "FncCatDocuUpdated", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        FNC_CAT_DOCU_UPDATED.setOptional();

        addField(DEVELOPMENT_METHOD = new Rq1DatabaseField_Enumeration(this, "DevelopmentMethod", DevelopmentMethod.values()));
        DEVELOPMENT_METHOD.acceptInvalidValuesInDatabase();
        addField(DEVELOPMENT_METHOD_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DevelopmentMethod_Comment"));

        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        addField(FLOW_RANK = new Rq1XmlSubField_Text(this, FLOW, "RANK"));
        addField(FLOW_INTERNAL_RANK = new Rq1XmlSubField_Text(this, FLOW, "INTERNAL_RANK"));

        FLOW.setOptional();
        FLOW_RANK.setOptional();
        FLOW_INTERNAL_RANK.setOptional();
    }

    public static Iterable<Rq1Fc> get_FC_from_Project_on_ISW_on_Project_for_Customer(Rq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.FC_RELEASE.getRecordType());

        query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_Release.getAllOpenState());
        query.addCriteria_Value(Rq1NodeDescription.TYPE_FIELDNAME, Rq1NodeDescription.TYPE_FC);
        query.addCriteria_Reference(ATTRIBUTE_BELONGS_TO_PROJECT, project);
        query.addCriteria_Value(
                Rq1FcRelease.ATTRIBUTE_HAS_MAPPED_ISSUES,
                Rq1Irm_Fc_IssueFd.ATTRIBUTE_HAS_MAPPED_ISSUE,
                Rq1IssueFD.ATTRIBUTE_HAS_PARENT,
                Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT,
                Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1Fc> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1Fc) {
                result.add((Rq1Fc) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

}
