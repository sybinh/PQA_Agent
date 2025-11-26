/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Date;
import DataModel.ALM.Fields.DmAlmField_Resource;
import DataModel.ALM.Fields.DmAlmField_ResourceAsEnumeration;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataModel.DmElementListField_ReadOnly_PreDNG_CRQ;
import DataStore.ALM.DsAlmRecord_Requirement;

/**
 *
 * @author GUG2WI
 */
public class DmAlmRequirement extends DmAlmElement {

    final public DmAlmField_Text TITLE;
    final public DmAlmField_Resource CONTRIBUTOR;
    final public DmAlmField_Date CREATED;
    final public DmAlmField_Resource CREATOR;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Text IDENTIFIER;
    final public DmAlmField_Text INTERNAL_COMMENT;
    final public DmAlmField_Date MODIFIED_DATE;
    final public DmAlmField_ResourceAsEnumeration ASIL_CLASSIFICATION;
    final public DmAlmField_Text PRE_DNG_CRQ;
    final public DmAlmField_ResourceAsEnumeration REVIEW_STATE;
    final public DmAlmField_ResourceAsEnumeration STATUS;
    final public DmAlmField_ResourceAsEnumeration TEST_LEVEL;
    final public DmAlmField_Text VERIFICATION_CRITERIA;
    final public DmAlmField_Text PRIMARY_TEXT;
    final public DmAlmField_Resource COMPONENT;
    final public DmAlmField_Resource PROJECT_AREA;
    final public DmAlmField_Resource TEAM_AREA;
    final public DmAlmField_Text VAR_FUNC_SYS;
    final public DmAlmField_Text VAR_FUNC_SW;
    final public DmAlmField_Boolean QUALIFIED_FOR_REGRESSION;
    final public DmAlmField_Text AFFECTED_COMPONENT;
    final public DmAlmField_Text ALLOCATION;
    final public DmElementListField_ReadOnly_PreDNG_CRQ PRE_DNG_ELEMENTS;

    protected DmAlmRequirement(String elementType, DsAlmRecord_Requirement dsAlmRecord) {
        super(elementType, dsAlmRecord);

        TITLE = addTextField("dcterms:title", "Title");
        CONTRIBUTOR = addResourceField("dcterms:contributor", "Contributor");
        CREATED = addDateField("dcterms:created", "Created");
        CREATOR = addResourceField("dcterms:creator", "Creator");
        DESCRIPTION = addTextField("dcterms:description", "Description", true);
        IDENTIFIER = addTextField("dcterms:identifier", "Identifier");
        INTERNAL_COMMENT = addTextField(create_j_names("InternalComment"), "Internal Comment", true);
        MODIFIED_DATE = addDateField("dcterms:modified", "Modified date");
        ASIL_CLASSIFICATION = addResourceAsEnumerationField(create_j_names("ASILClassification"), "ASIL Classification", true);
        PRE_DNG_CRQ = addTextField(create_j_names("PreDNGCRQ"), "Pre-DNG CRQ", true);
        REVIEW_STATE = addResourceAsEnumerationField(create_j_names("ReviewState"), "Review State", true);
        STATUS = addResourceAsEnumerationField(create_j_names("Status"), "Status", true);
        TEST_LEVEL = addResourceAsEnumerationField(create_j_names("TestLevel"), "Test Level", true);
        VERIFICATION_CRITERIA = addTextField(create_j_names("VerificationCriteria"), "Verification Criteria", true);
        PRIMARY_TEXT = addTextField("jazz_rm:primaryText", "Primary Text", true);
        COMPONENT = addResourceField("oslc_config:component", "Component");
        PROJECT_AREA = addResourceField("process:projectArea", "Project Area");
        TEAM_AREA = addResourceField("process:teamArea", "Team Area", true);
        addResourceListField("oslc_rm:satisfiedBy", "Satisfied By", true);
        addResourceListField(create_j_names("Satisfaction"), "Satisfaction", true);
        addResourceListField(create_j_names("Link"), "Link", true);
        VAR_FUNC_SYS = addTextField(create_j_names("VAR_FUNC_SYS"), "VAR_FUNC_SYS", true);
        VAR_FUNC_SW = addTextField(create_j_names("VAR_FUNC_SW"), "VAR_FUNC_SW", true);
        QUALIFIED_FOR_REGRESSION = addBooleanField(create_j_names("QualifiedforRegression"), "Qualified for Regression", true);
        AFFECTED_COMPONENT = addTextField(create_j_names("AffectedComponent"), "Affected Component", true);
        ALLOCATION = addTextField(create_j_names("Allocation"), "Allocation", true);
        PRE_DNG_ELEMENTS = addField(new DmElementListField_ReadOnly_PreDNG_CRQ(PRE_DNG_CRQ, "Pre-DNG CRQ_In_Tree"));

        ignoreField("acp:accessControl");

        ignoreField("oslc:instanceShape");
        ignoreField("oslc:serviceProvider");
    }

    @Override
    public String getTitle() {
        return (TITLE.getValueAsText());
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

    @Override
    public String getId() {
        return (IDENTIFIER.getValueAsText());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + IDENTIFIER.getValueAsText() + " - " + TITLE.getValueAsText());
    }
}
