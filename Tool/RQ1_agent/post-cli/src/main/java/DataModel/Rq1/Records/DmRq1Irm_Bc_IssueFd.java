/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import static DataModel.DmFieldI.Attribute.MULTILINE_TEXT;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_MappingToDerivatives_String;
import Rq1Cache.Records.Rq1Irm_Bc_IssueFd;
import DataModel.DmMappedElement;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Irm_Bc_IssueFd extends DmRq1Irm {

    final public DmRq1Field_MappingToDerivatives_String MAPPING_TO_DERIVATIVES;
    
    final public DmRq1Field_Text EXTERNAL_ID;
    final public DmRq1Field_Text EXCHANGE_WORKFLOW;
    final public DmRq1Field_Date LAST_IMPORT_DATE;
    final public DmRq1Field_Date LAST_EXPORTED_DATE;
    final public DmRq1Field_Text EXTERNAL_REVIEW;
    final public DmRq1Field_Enumeration EXTERNAL_NEXT_STATE;
    final public DmRq1Field_Text EXTERNAL_COMMENT;
    final public DmRq1Field_Text EXTERNAL_CONVERSATION;
    final public DmRq1Field_Text EXTERNAL_STATES;
    final public DmRq1Field_Text EXTERNAL_TAGS;
    final public DmRq1Field_Reference<DmRq1Contact> EXTERNAL_ASSIGNEE;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_NAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_LASTNAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_FIRSTNAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_PHONE;
    final public DmRq1Field_Text SUPPLIER_ID;
    final public DmRq1Field_Text SUPPLIER_STATE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Irm_Bc_IssueFd(Rq1Irm_Bc_IssueFd rq1Irm_Bc_IssueFd) {
        super("IRM-BC-ISSUE_FD", rq1Irm_Bc_IssueFd);

        addField(MAPPING_TO_DERIVATIVES = new DmRq1Field_MappingToDerivatives_String(this, rq1Irm_Bc_IssueFd.MAPPING_TO_DERIVATIVES, "Mapping To Derivatives"));

        addField(EXTERNAL_ID = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ID, "External ID"));
        addField(EXCHANGE_WORKFLOW = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXCHANGE_WORKFLOW, "Exchange Workflow"));
        addField(LAST_IMPORT_DATE = new DmRq1Field_Date(this, rq1Irm_Bc_IssueFd.LAST_IMPORT_DATE, "Last Imported Date"));
        addField(LAST_EXPORTED_DATE = new DmRq1Field_Date(this, rq1Irm_Bc_IssueFd.LAST_EXPORTED_DATE, "Last Exported Date"));
        addField(EXTERNAL_REVIEW = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_REVIEW, "External Review"));
        addField(EXTERNAL_NEXT_STATE = new DmRq1Field_Enumeration(this, rq1Irm_Bc_IssueFd.EXTERNAL_NEXT_STATE, "External Next State"));
        addField(EXTERNAL_COMMENT = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_COMMENT, "New External Comment"));
        EXTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        EXTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.MULTILINE_TEXT);
        addField(EXTERNAL_CONVERSATION = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_CONVERSATION, "External Conversation"));
        EXTERNAL_CONVERSATION.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        EXTERNAL_CONVERSATION.setAttribute(FIELD_FOR_BULK_OPERATION, MULTILINE_TEXT);
        addField(EXTERNAL_STATES = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_STATES, "External State"));
        addField(EXTERNAL_TAGS = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_TAGS, "External Tags"));
        EXTERNAL_TAGS.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(EXTERNAL_ASSIGNEE = new DmRq1Field_Reference<>(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE, "External Assignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE_NAME, "External Assignee Name"));
        addField(EXTERNAL_ASSIGNEE_LASTNAME = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE_LASTNAME, "External Assignee Last Name"));
        addField(EXTERNAL_ASSIGNEE_FIRSTNAME = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE_FIRSTNAME, "External Assignee First Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE_ORGANIZATION, "External Assignee Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE_DEPARTMENT, "External Assignee Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE_EMAIL, "External Assignee E-Mail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.EXTERNAL_ASSIGNEE_PHONE, "External Assignee Phone Number"));
        addField(SUPPLIER_ID = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.SUPPLIER_ID, "Supplier ID"));
        addField(SUPPLIER_STATE = new DmRq1Field_Text(this, rq1Irm_Bc_IssueFd.SUPPLIER_STATE, "Supplier State"));
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Bc_IssueFd.HAS_MAPPED_ISSUE, "Issue SW"));
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Bc_IssueFd.HAS_MAPPED_RELEASE, "BC Release"));
    }

    public static DmRq1Irm_Bc_IssueFd create(DmRq1Bc bcRelease, DmRq1IssueFD dmIssueFD) throws DmRq1MapExistsException {
        assert (bcRelease != null);
        assert (dmIssueFD != null);

        //
        // Ensure that IRM does not yet exist
        //
        if (dmIssueFD.MAPPED_BC.isLoaded() == true) {
            for (DmMappedElement<DmRq1Irm_Bc_IssueFd, DmRq1Bc> m : dmIssueFD.MAPPED_BC.getElementList()) {
                if (m.getTarget() == bcRelease) {
                    throw (new DmRq1MapExistsException());
                }
            }
        } else {
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : bcRelease.MAPPED_ISSUES.getElementList()) {
                if (m.getTarget() == dmIssueFD) {
                    throw (new DmRq1MapExistsException());
                }
            }
        }

        DmRq1Irm_Bc_IssueFd irm = DmRq1ElementCache.createIrm_Bc_IssueFd();

        //
        // Take over content from BC
        //
        irm.ACCOUNT_NUMBERS.setValue(bcRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Connect PVER - Map - Issue
        //
        irm.HAS_MAPPED_RELEASE.setElement(bcRelease);
        irm.HAS_MAPPED_ISSUE.setElement(dmIssueFD);
        bcRelease.MAPPED_ISSUES.addElement(irm, dmIssueFD);
        dmIssueFD.MAPPED_BC.addElement(irm, bcRelease);

        return (irm);
    }

    public static DmRq1Irm_Bc_IssueFd create(DmRq1Bc bcRelease, DmRq1Irm_Bc_IssueFd dmIrm_Bc_IssueFd, DmRq1IssueFD dmIssueFD) throws DmRq1MapExistsException {
        assert (bcRelease != null);
        assert (dmIrm_Bc_IssueFd != null);
        assert (dmIssueFD != null);

        //
        // Create new RRM
        //
        DmRq1Irm_Bc_IssueFd newIrm = create(bcRelease, dmIssueFD);

        //
        // Take over values from RRM
        //
        return (newIrm);
    }

    public static DmRq1Irm_Bc_IssueFd moveFromBcToBc(DmRq1Bc newBcRelease, DmRq1Irm_Bc_IssueFd dropedIrm, DmRq1IssueFD dmIssueFD) throws DmRq1MapExistsException {
        assert (newBcRelease != null);
        assert (dropedIrm != null);
        assert (dmIssueFD != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : newBcRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == dmIssueFD) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1Bc oldBcRelease = (DmRq1Bc) dropedIrm.HAS_MAPPED_RELEASE.getElement();

        //
        // Change mapping on BC
        //
        oldBcRelease.MAPPED_ISSUES.removeElement(dmIssueFD);
        newBcRelease.MAPPED_ISSUES.addElement(dropedIrm, dmIssueFD);

        //
        // Change mapping on issue
        //
        dmIssueFD.MAPPED_BC.removeElement(oldBcRelease);
        dmIssueFD.MAPPED_BC.addElement(dropedIrm, newBcRelease);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedIrm.HAS_MAPPED_RELEASE.setElement(newBcRelease);

        return (dropedIrm);
    }

    public static DmRq1Irm_Bc_IssueFd moveFromIssueToIssue(DmRq1Bc bcRelease, DmRq1Irm_Bc_IssueFd dropedIrm, DmRq1IssueFD newIssueFD) throws DmRq1MapExistsException {
        assert (bcRelease != null);
        assert (dropedIrm != null);
        assert (newIssueFD != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : bcRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == newIssueFD) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1IssueFD oldIssueFD = (DmRq1IssueFD) dropedIrm.HAS_MAPPED_ISSUE.getElement();

        //
        // Change mapping on BC
        //
        bcRelease.MAPPED_ISSUES.removeElement(oldIssueFD);
        bcRelease.MAPPED_ISSUES.addElement(dropedIrm, newIssueFD);

        //
        // Change mapping on issue
        //
        oldIssueFD.MAPPED_BC.removeElement(bcRelease);
        newIssueFD.MAPPED_BC.addElement(dropedIrm, bcRelease);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedIrm.HAS_MAPPED_ISSUE.setElement(newIssueFD);
        return (dropedIrm);
    }

}
