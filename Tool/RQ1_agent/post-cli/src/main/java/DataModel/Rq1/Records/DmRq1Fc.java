/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Dgs.DmDgsFcReleaseI;
import DataModel.DmFieldI;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import DataModel.DmValueFieldI_Enumeration_EagerLoad;
import DataModel.DmValueFieldI_Text;
import DataModel.Flow.InternalRank;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnFc;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnRelease;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_Fc_NamingConvention;
import Rq1Cache.Records.Rq1Fc;
import Rq1Cache.Records.Rq1Project;
import Rq1Data.Enumerations.DevelopmentMethod;
import java.util.ArrayList;
import java.util.List;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Fc extends DmRq1SoftwareRelease implements DmDgsFcReleaseI {

    final public DmRq1Field_Enumeration SYNC;
    final public DmRq1Field_Text LINKS;

    final public DmRq1Field_MappedReferenceList<DmRq1Rrm_Bc_Fc, DmRq1Bc> MAPPED_BC;

    final public DmRq1Field_AllRequirementsOnRelease MAPPED_REQUIREMENTS;

    final public DmRq1Field_Enumeration NEUTRAL_CALIBRATION;

    final public DmValueFieldI_Enumeration_EagerLoad CATEGORY_SAFETY;
    final public DmValueFieldI_Text CATEGORY_SAFETY_COMMENT;
    final public DmValueFieldI_Text CATEGORY_SAFETY_CONDITION;
    final public DmValueFieldI_Enumeration_EagerLoad CATEGORY_OBD;
    final public DmValueFieldI_Enumeration_EagerLoad CATEGORY_OBD_COMMENT;
    final public DmValueFieldI_Text CATEGORY_OBD_CONDITION;
    final public DmValueFieldI_Enumeration_EagerLoad CATEGORY_EXHAUST;
    final public DmValueFieldI_Enumeration_EagerLoad CATEGORY_EXHAUST_COMMENT;
    final public DmValueFieldI_Text CATEGORY_EXHAUST_CONDITION;
    final public DmValueFieldI_Enumeration_EagerLoad FNC_CAT_DOCU_UPDATED;
    final public DmValueFieldI_Enumeration_EagerLoad ECV_KONZERN_BASELINE;

    final public DmValueFieldI_Enumeration_EagerLoad EXTERNAL_DESCRIPTION_REVIEW_STATUS;
    final public DmValueFieldI_Enumeration_EagerLoad EXTERNAL_DESCRIPTION_REVIEW_QUALITY;

    final public DmRq1Field_Enumeration DEVELOPMENT_METHOD;
    final public DmRq1Field_Text DEVELOPMENT_METHOD_COMMENT;

    final public DmRq1Field_Enumeration PROCESS_TAILORING;

    //FLOW
    final public DmRq1Field_Text FLOW_INTERNAL_RANK;
    public InternalRank internalRank = null;
    final public DmRq1Field_Text FLOW_RANK;

    public DmRq1Fc(String type, Rq1Fc rq1Fc) {
        super(type, rq1Fc);

        addField(SYNC = new DmRq1Field_Enumeration(this, rq1Fc.SYNC, "Sync"));
        addField(LINKS = new DmRq1Field_Text(this, rq1Fc.LINKS, "Links"));

        addField(PROCESS_TAILORING = new DmRq1Field_Enumeration(this, rq1Fc.PROCESS_TAILORING, "Process Tailoring"));
        PROCESS_TAILORING.setAttribute(FIELD_FOR_BULK_OPERATION);

        addField(MAPPED_BC = new DmRq1Field_MappedReferenceList<>(this, rq1Fc.HAS_MAPPED_PARENTS, "Parents"));

        addField(MAPPED_REQUIREMENTS = new DmRq1Field_AllRequirementsOnFc(this, "Mapped Requirements"));

        addField(NEUTRAL_CALIBRATION = new DmRq1Field_Enumeration(this, rq1Fc.NEUTRAL_CALIBRATION, "Neutral Calibration"));

        addField(CATEGORY_SAFETY = new DmRq1Field_Enumeration(rq1Fc.CATEGORY_SAFETY, "Cat Safety"));
        addField(CATEGORY_SAFETY_COMMENT = new DmRq1Field_Text(rq1Fc.CATEGORY_SAFETY_COMMENT, "Category Safety Comment"));
        addField(CATEGORY_SAFETY_CONDITION = new DmRq1Field_Text(rq1Fc.CATEGORY_SAFETY_CONDITION, "Condition Safety"));
        addField(CATEGORY_OBD = new DmRq1Field_Enumeration(rq1Fc.CATEGORY_OBD, "Cat OBD"));
        addField(CATEGORY_OBD_COMMENT = new DmRq1Field_Enumeration(rq1Fc.CATEGORY_OBD_COMMENT, "Category OBD Comment"));
        addField(CATEGORY_OBD_CONDITION = new DmRq1Field_Text(rq1Fc.CATEGORY_OBD_CONDITION, "Condition OBD"));
        addField(CATEGORY_EXHAUST = new DmRq1Field_Enumeration(rq1Fc.CATEGORY_EXHAUST, "Cat Exhaust"));
        addField(CATEGORY_EXHAUST_COMMENT = new DmRq1Field_Enumeration(rq1Fc.CATEGORY_EXHAUST_COMMENT, "Category Exhaust Comment"));
        addField(CATEGORY_EXHAUST_CONDITION = new DmRq1Field_Text(rq1Fc.CATEGORY_EXHAUST_CONDITION, "Condition Exhaust"));
        CATEGORY_SAFETY.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_SAFETY_COMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_SAFETY_CONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_OBD.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_OBD_COMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_OBD_CONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_EXHAUST.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_EXHAUST_COMMENT.setAttribute(FIELD_FOR_BULK_OPERATION);
        CATEGORY_EXHAUST_CONDITION.setAttribute(FIELD_FOR_BULK_OPERATION);
        addField(FNC_CAT_DOCU_UPDATED = new DmRq1Field_Enumeration(this, rq1Fc.FNC_CAT_DOCU_UPDATED, "FnCCat updated in FC-Docu"));
        FNC_CAT_DOCU_UPDATED.setAttribute(FIELD_FOR_BULK_OPERATION);
        addField(ECV_KONZERN_BASELINE = new DmRq1Field_Enumeration(this, rq1Fc.ECV_KONZERN_BASELINE, "Tag Ecv_KonzernBaseline"));

        addField(DEVELOPMENT_METHOD = new DmRq1Field_Enumeration(this, rq1Fc.DEVELOPMENT_METHOD, "Development Method"));
        DEVELOPMENT_METHOD.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        DEVELOPMENT_METHOD.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        addField(DEVELOPMENT_METHOD_COMMENT = new DmRq1Field_Text(this, rq1Fc.DEVELOPMENT_METHOD_COMMENT, "Development Method Comment"));
        DEVELOPMENT_METHOD_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        DEVELOPMENT_METHOD_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(EXTERNAL_DESCRIPTION_REVIEW_STATUS = new DmRq1Field_Enumeration(rq1Fc.EXTERNAL_DESCRIPTION_REVIEW_STATUS, "External Description Review Status"));
        addField(EXTERNAL_DESCRIPTION_REVIEW_QUALITY = new DmRq1Field_Enumeration(rq1Fc.EXTERNAL_DESCRIPTION_REVIEW_QUALITY, "External Description Review Quality"));

        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, rq1Fc.FLOW_INTERNAL_RANK, "Flow Internal Rank"));
        addField(FLOW_RANK = new DmRq1Field_Text(this, rq1Fc.FLOW_RANK, "Flow Rank"));
    }

    @Override
    public boolean save() {

        //
        // Save development method in second save, if record is a new record and predecessor is set.
        //
        if (existsInDatabase() == false) {
            if (PREDECESSOR.isElementSet() == true) {
                DmRq1Fc predecessor = (DmRq1Fc) PREDECESSOR.getElement();
                EcvEnumeration predecessorDevelopmentMethod = predecessor.DEVELOPMENT_METHOD.getValue();
                if (predecessorDevelopmentMethod == DevelopmentMethod.EMPTY) {
                    return super.save();
                }
                return (saveSpecialForDevelopmentMethod());
            }

        }

        return super.save();
    }

    private boolean saveSpecialForDevelopmentMethod() {

        EcvEnumeration developmentMethod = DEVELOPMENT_METHOD.getValue();

        if ((developmentMethod != null) && (developmentMethod != DevelopmentMethod.EMPTY)) {
            DEVELOPMENT_METHOD.setValue(null);
            boolean firstSaveResult = super.save();
            if (firstSaveResult == false) {
                DEVELOPMENT_METHOD.setValue(developmentMethod);
                return (false);
            }
            if (DEVELOPMENT_METHOD.getValue() == developmentMethod) {
                return (firstSaveResult);
            }
            DEVELOPMENT_METHOD.setValue(developmentMethod);
        }

        return (super.save());
    }

    @Override
    final public String getName() {
        //
        // extractFcName() is implemented in the rule to keep all methods working with a name schema within one class.
        //
        return (Rule_Fc_NamingConvention.extractFcName(getTitle()));
    }

    @Override
    final public int compareTo(DmDgsFcReleaseI other) {
        return (FcInterface.compare(this, other));
    }

    @Override
    final public boolean equals(Object other) {
        return (FcInterface.equals(this, other));
    }

    @Override
    final public String getVariant() {
        return (FcInterface.getVariant(getVersion()));
    }

    /**
     * Returns all FC which belong to a I-SW which belongs to a project with the
     * given customer.
     *
     * @param project
     * @param customerGroup Group of the customer for I-SW.
     * @return
     */
    static public List<DmRq1Fc> get_FC_from_Project_on_ISW_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        List<DmRq1Fc> result = new ArrayList<>();
        for (Rq1Fc rq1Fc : Rq1Fc.get_FC_from_Project_on_ISW_on_Project_for_Customer((Rq1Project) project.getRq1Record(), customerGroup)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Fc);
            if (dmElement instanceof DmRq1Fc) {
                result.add((DmRq1Fc) dmElement);
            }
        }
        return (result);
    }

    public InternalRank getInternalRank() throws InternalRank.BuildException {
        if (internalRank == null) {
            internalRank = InternalRank.buildForRecord(getRq1Id(), FLOW_RANK.getValue(), FLOW_INTERNAL_RANK.getValue());
            FLOW_INTERNAL_RANK.setValue(internalRank.toString());
        }
        return (internalRank);
    }

    public void setExistingIFdRank(InternalRank ifdRank) {
        internalRank = ifdRank;
        FLOW_INTERNAL_RANK.setValue(ifdRank.toString());
    }

}
