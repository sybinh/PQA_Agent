/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_RRM;
import DataModel.DmFieldI;
import DataModel.Rq1.Monitoring.Rule_Rrm_Pst_Bc_DeliveryDate;
import DataModel.Rq1.Monitoring.Rule_Rrm_Pst_Bc_HintForExclude;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_MappingToDerivatives_ComboBox;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_RrmCustomerResponse;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.DmMappedElement;
import DataModel.DmToDsValueField;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Rrm_Pst_Bc;
import Rq1Cache.Types.Rq1XmlTable_RrmChangesToIssues;
import UiSupport.AssigneeFilter;
import util.EcvMapSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Rrm_Pst_Bc extends DmRq1Rrm {

    final public DmRq1Field_Date REQUESTED_IMPLEMENTATION_DATE;
    final public DmRq1Field_Text RB_COMMENT;
    final public DmRq1Field_Reference<DmRq1Pst> MAPPED_PARENT;
    final public DmRq1Field_Reference<DmRq1Bc> MAPPED_CHILDREN;
    final public DmRq1Field_Text CALIBRATION_COMMENT;
    final public DmRq1Field_Text CALIBRATION_STATE;
    final public DmRq1Field_Text INTEGRATOR_COMMENT;
    final public DmRq1Field_Enumeration INTEGRATOR_STATE;

    final public DmRq1Field_Table<Rq1XmlTable_RrmChangesToIssues> CHANGES_TO_ISSUES;
    final public DmRq1Field_RrmCustomerResponse CUSTOMER_RESPONSE;

    final public DmRq1Field_MappingToDerivatives_ComboBox SELECTION_OF_DERIVATIVES;

    final public DmRq1Field_Text INT_HINT_COMMENT;

    final public DmRq1Field_Enumeration IN_MA_INTEGRATION_STATUS;

    //
    // Fields for ECB
    //
    final public DmRq1Field_Text ECB_INT_HINT_COMMENT_TEXT;
    final public DmRq1Field_Text ECB_INT_HINT_REQUESTER;
    final public DmRq1Field_Enumeration ECB_INT_HINT_STATE;
    final public DmRq1Field_Enumeration ECB_COMMERCIAL_PILOT;
    final public DmRq1Field_Text ECB_INTEGRATOR;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Rrm_Pst_Bc(Rq1Rrm_Pst_Bc rq1Rrm_ecuRelease_Bc) {
        super("RRM-PST-BC", rq1Rrm_ecuRelease_Bc);

        addField(REQUESTED_IMPLEMENTATION_DATE = new DmRq1Field_Date(this, rq1Rrm_ecuRelease_Bc.REQUESTED_IMPLEMENTATION_FREEZE, "Requested Impl. Date"));
        REQUESTED_IMPLEMENTATION_DATE.addAlternativeField(rq1Rrm_ecuRelease_Bc.REQUESTED_IMPLEMENTATION_DATE);
        REQUESTED_IMPLEMENTATION_DATE.setWriteMode(DmToDsValueField.WriteMode.WRITE_ALSO_TO_ALTERNATIVE_FIELDS);

        addField(RB_COMMENT = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.RB_COMMENT, "RB Comment (visible to customer)"));
        RB_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);
        RB_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(CALIBRATION_COMMENT = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.CALIBRATION_COMMENT, "Calibration Comment"));
        CALIBRATION_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);
        CALIBRATION_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(CALIBRATION_STATE = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.CALIBRATION_STATE, "Calibration State"));
        CALIBRATION_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        CALIBRATION_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(INTEGRATOR_COMMENT = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.INTEGRATOR_COMMENT, "Integrator Comment"));
        INTEGRATOR_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);
        INTEGRATOR_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(INTEGRATOR_STATE = new DmRq1Field_Enumeration(this, rq1Rrm_ecuRelease_Bc.INTEGRATOR_STATE, "Integrator State"));
        INTEGRATOR_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        INTEGRATOR_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(MAPPED_PARENT = new DmRq1Field_Reference<>(this, rq1Rrm_ecuRelease_Bc.HAS_MAPPED_PARENT_RELEASE, "Parent"));
        addField(MAPPED_CHILDREN = new DmRq1Field_Reference<>(this, rq1Rrm_ecuRelease_Bc.HAS_MAPPED_CHILD_RELEASE, "Children"));
        addField(SELECTION_OF_DERIVATIVES = new DmRq1Field_MappingToDerivatives_ComboBox(this, rq1Rrm_ecuRelease_Bc.SELECTION_OF_DERIVATIVES, MAPPED_PARENT, "Selection Of Derivatives"));

        addField(CHANGES_TO_ISSUES = new DmRq1Field_Table<>(this, rq1Rrm_ecuRelease_Bc.CHANGES_TO_ISSUES, "Changes To Issues"));
        addField(CUSTOMER_RESPONSE = new DmRq1Field_RrmCustomerResponse(this, rq1Rrm_ecuRelease_Bc.CUSTOMER_RESPONSE, "Customer Response"));

        addField(INT_HINT_COMMENT = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.INT_HINT_COMMENT, "Integration Comment (IntHint)"));
        INT_HINT_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);

        addField(IN_MA_INTEGRATION_STATUS = new DmRq1Field_Enumeration(rq1Rrm_ecuRelease_Bc.IN_MA_INTEGRATION_STATUS, "InMa Int Status"));

        //
        // Fields for ECB
        //
        addField(ECB_INT_HINT_COMMENT_TEXT = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.ECB_INT_HINT_COMMENT_TEXT, "ECB: Integration Comment"));
        ECB_INT_HINT_COMMENT_TEXT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        ECB_INT_HINT_COMMENT_TEXT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INT_HINT_COMMENT_TEXT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_INT_HINT_REQUESTER = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.ECB_INT_HINT_REQUESTER, "ECB: Integration Requester"));
        ECB_INT_HINT_REQUESTER.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        ECB_INT_HINT_REQUESTER.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INT_HINT_REQUESTER.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_INT_HINT_STATE = new DmRq1Field_Enumeration(this, rq1Rrm_ecuRelease_Bc.ECB_INT_HINT_STATE, "ECB: Integration State"));
        ECB_INT_HINT_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INT_HINT_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_COMMERCIAL_PILOT = new DmRq1Field_Enumeration(this, rq1Rrm_ecuRelease_Bc.ECB_COMMERCIAL_PILOT, "ECB: Commercial-Pilot"));
        ECB_COMMERCIAL_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_COMMERCIAL_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_INTEGRATOR = new DmRq1Field_Text(this, rq1Rrm_ecuRelease_Bc.ECB_INTEGRATOR, "ECB: Integrator"));
        ECB_INTEGRATOR.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);
        ECB_INTEGRATOR.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        //
        // Settings for bulk operation
        //
        REQUESTED_IMPLEMENTATION_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        SELECTION_OF_DERIVATIVES.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        //
        // Create and add rules
        //
        addRule(new Rule_Rrm_Pst_Bc_DeliveryDate(this));
        addRule(new Rule_Rrm_Pst_Bc_HintForExclude(this));
        addRule(new ConfigurableRuleManagerRule_RRM(this, MAPPED_PARENT, MAPPED_CHILDREN));
    }

    public DmRq1Pst getMappedPst() {
        return (MAPPED_PARENT.getElement());
    }

    public DmRq1Bc getMappedBc() {
        return (MAPPED_CHILDREN.getElement());
    }

    public static DmRq1Rrm_Pst_Bc create(DmRq1Pst pstRelease, DmRq1Bc bcRelease) throws DmRq1MapExistsException {
        assert (pstRelease != null);
        assert (bcRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : pstRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == bcRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        DmRq1Rrm_Pst_Bc rrm = DmRq1ElementCache.createRrm_Pst_Bc();

        //
        // Take over content from PST
        //
        rrm.ACCOUNT_NUMBERS.setValue(pstRelease.ACCOUNT_NUMBERS.getValue());
        if (pstRelease.DEFAULT_REQUESTED_DELIVERY_DATE.getValue().isEmpty() == false) {
            rrm.REQUESTED_DELIVERY_DATE.setValue(pstRelease.DEFAULT_REQUESTED_DELIVERY_DATE.getValue());
        }
        rrm.SELECTION_OF_DERIVATIVES.updateForPst(pstRelease);

        //
        // Connect PST - Map - BC
        //
        rrm.MAPPED_PARENT.setElement(pstRelease);
        rrm.MAPPED_CHILDREN.setElement(bcRelease);
        pstRelease.MAPPED_CHILDREN.addElement(rrm, bcRelease);
        bcRelease.MAPPED_PST.addElement(rrm, pstRelease);

        return (rrm);
    }

    public static DmRq1Rrm_Pst_Bc createBasedOnRrmToPst(DmRq1Pst ecuRelease, DmRq1Rrm_Pst_Bc dropedRrm, DmRq1Bc bcRelease) throws DmRq1MapExistsException {
        assert (ecuRelease != null);
        assert (dropedRrm != null);
        assert (bcRelease != null);

        //
        // Create new RRM
        //
        DmRq1Rrm_Pst_Bc newRrm = create(ecuRelease, bcRelease);

        newRrm.SELECTION_OF_DERIVATIVES.updateForPst(ecuRelease);

        //
        // Take over values from RRM
        //
        return (newRrm);
    }

    public static DmRq1Rrm_Pst_Bc moveFromPstToPst(DmRq1Pst newPstRelease, DmRq1Rrm_Pst_Bc dropedRrm, DmRq1Bc bcRelease) throws DmRq1MapExistsException {
        assert (newPstRelease != null);
        assert (dropedRrm != null);
        assert (bcRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : newPstRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == bcRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1Pst oldPstRelease = dropedRrm.MAPPED_PARENT.getElement();

        //
        // Ensure that mapping lists are loaded
        //
        oldPstRelease.MAPPED_CHILDREN.getElementList();
        newPstRelease.MAPPED_CHILDREN.getElementList();
        bcRelease.MAPPED_PST.getElementList();
        bcRelease.MAPPED_PST.getElementList();

        //
        // Change on RRM *** This setting has to be done after the mapped lists are loaded. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedRrm.MAPPED_PARENT.setElement(newPstRelease);
        if (newPstRelease.DEFAULT_REQUESTED_DELIVERY_DATE.getValue().isEmpty() == false) {
            dropedRrm.REQUESTED_DELIVERY_DATE.setValue(newPstRelease.DEFAULT_REQUESTED_DELIVERY_DATE.getValue());
        }
        dropedRrm.SELECTION_OF_DERIVATIVES.updateForPst(newPstRelease);

        //
        // Change mapping on PST
        //
        oldPstRelease.MAPPED_CHILDREN.removeElement(bcRelease);
        newPstRelease.MAPPED_CHILDREN.addElement(dropedRrm, bcRelease);

        //
        // Change mapping on BC
        //
        bcRelease.MAPPED_PST.removeElement(oldPstRelease);
        bcRelease.MAPPED_PST.addElement(dropedRrm, newPstRelease);

        dropedRrm.changed(null); // Initiate GUI update.

        return (dropedRrm);
    }

    public static DmRq1Rrm_Pst_Bc moveFromBcToBc(DmRq1Pst pstRelease, DmRq1Rrm_Pst_Bc dropedRrm, DmRq1Bc newBcRelease) throws DmRq1MapExistsException {
        assert (pstRelease != null);
        assert (dropedRrm != null);
        assert (newBcRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : pstRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == newBcRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1Bc oldBcRelease = dropedRrm.MAPPED_CHILDREN.getElement();

        //
        // Change mapping on Pst
        //
        pstRelease.MAPPED_CHILDREN.removeElement(oldBcRelease);
        pstRelease.MAPPED_CHILDREN.addElement(dropedRrm, newBcRelease);

        //
        // Change mapping on BC
        //
        oldBcRelease.MAPPED_PST.removeElement(pstRelease);
        newBcRelease.MAPPED_PST.addElement(dropedRrm, pstRelease);

        //
        // Change mapping on RRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedRrm.MAPPED_CHILDREN.setElement(newBcRelease);

        return (dropedRrm);
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTitle() + " " + MAPPED_PARENT.getElement().getTypeIdTitle() + " " + MAPPED_CHILDREN.getElement().getTypeIdTitle();
    }

    @Override
    public EcvMapSet<AssigneeFilter, DmRq1Project> getProjects() {
        EcvMapSet<AssigneeFilter, DmRq1Project> set = new EcvMapSet<>();
        if (MAPPED_PARENT.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.RRMPARENTRELEASEPROJECT, MAPPED_PARENT.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.RRM_PROJECT, MAPPED_PARENT.getElement().PROJECT.getElement());
        }
        if (MAPPED_CHILDREN.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.RRMCHILDRELEASEPROJECT, MAPPED_CHILDREN.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.RRM_PROJECT, MAPPED_CHILDREN.getElement().PROJECT.getElement());
        }
        return set;
    }

    @Override
    public DmRq1Field_Reference<? extends DmRq1Release> getParentField() {
        return (MAPPED_PARENT);
    }

}
