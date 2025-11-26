/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsField_Xml;
import DataStore.Exceptions.DsFieldContentFailure_InvalidValue;
import Rq1Cache.Fields.Rq1AlternativeField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_MappedReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.Enumerations.ApprovalHardware;
import Rq1Data.Enumerations.ExportScope;
import Rq1Data.Enumerations.PlanningGranularity;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author GUG2WI
 */
public class Rq1Release extends Rq1ReleaseRecord {

    final static public Rq1AttributeName ATTRIBUTE_BASED_ON_PREDECESSOR = new Rq1AttributeName("BasedOnPredecessor");
    final static public Rq1AttributeName ATTRIBUTE_CATEGORY = new Rq1AttributeName("Category");
    final static public Rq1AttributeName ATTRIBUTE_HAS_PREDECESSOR = new Rq1AttributeName("hasPredecessor");
    final static public Rq1AttributeName ATTRIBUTE_TYPE = new Rq1AttributeName("Type");
    final public static String FIELDNAME_EXTERNAL_ID = "External_ID";

    final static public String FIELDNAME_HAS_MAPPED_CHILDREN = "hasMappedChildren";
    final static public Rq1AttributeName ATTRIBUTE_HAS_MAPPED_ISSUES = new Rq1AttributeName("hasMappedIssues");
    final static public Rq1AttributeName ATTRIBUTE_HAS_WORKITEMS = new Rq1AttributeName("hasWorkitems");
    final static public Rq1AttributeName ATTRIBUTE_PRODUCT = new Rq1AttributeName("Product");

    final public Rq1DatabaseField_Date ACTUAL_DATE;
    final public Rq1DatabaseField_Enumeration APPROVAL;
    final public Rq1XmlSubField_Date DEFAULT_REQUESTED_DELIVERY_DATE;
    final public Rq1XmlSubField_Date DELIVERY_FREEZE;
    final public Rq1DatabaseField_Text ESTIMATED_EFFORT;
    final public Rq1DatabaseField_Text ESTIMATION_COMMENT;
    final public Rq1DatabaseField_Text EXTERNAL_COMMENT;
    final public Rq1DatabaseField_Text EXTERNAL_DESCRIPTION;
    final public Rq1DatabaseField_Text EXTERNAL_ID;
    final public Rq1DatabaseField_Text EXTERNAL_STATE;
    final public Rq1DatabaseField_Text EXTERNAL_TITLE;
    final public Rq1DatabaseField_Date IMPLEMENTATION_FREEZE_DATABASE;
    final public Rq1XmlSubField_Date IMPLEMENTATION_FREEZE_XML;
    final public Rq1AlternativeField_Date IMPLEMENTATION_FREEZE;
    final public Rq1DatabaseField_Enumeration PLANNING_GRANULARITY;
    final public Rq1DatabaseField_Text PRODUCT;
    final public Rq1DatabaseField_Text SCM_REFERENCES;
    //
    final public Rq1DatabaseField_Date START_DATE;
    //

    //
    final public Rq1DatabaseField_Xml MILESTONES;
    //
    final public Rq1DatabaseField_Reference HAS_PREDECESSOR;
    //
    final public Rq1DatabaseField_ReferenceList HAS_SUCCESSOR;

    //
    final public Rq1DatabaseField_MappedReferenceList HAS_MAPPED_PARENTS;
    final public Rq1DatabaseField_MappedReferenceList HAS_MAPPED_CHILDREN;
    final public Rq1DatabaseField_MappedReferenceList HAS_MAPPED_ISSUES;
    //
    final private Rq1XmlSubField_Xml GPM;
    final public Rq1XmlSubField_Enumeration EXPORT_SCOPE;

    public Rq1Release(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        addField(ACTUAL_DATE = new Rq1DatabaseField_Date(this, "ActualDate"));
        addField(APPROVAL = new Rq1DatabaseField_Enumeration(this, "Approval", ApprovalHardware.values()));

        addField(ESTIMATED_EFFORT = new Rq1DatabaseField_Text(this, "EstimatedEffort"));
        addField(ESTIMATION_COMMENT = new Rq1DatabaseField_Text(this, "EstimationComment"));
        addField(EXTERNAL_ID = new Rq1DatabaseField_Text(this, FIELDNAME_EXTERNAL_ID));
        addField(EXTERNAL_COMMENT = new Rq1DatabaseField_Text(this, "ExternalComment"));
        addField(EXTERNAL_DESCRIPTION = new Rq1DatabaseField_Text(this, "ExternalDescription"));
        addField(EXTERNAL_STATE = new Rq1DatabaseField_Text(this, "ExternalState"));
        addField(EXTERNAL_TITLE = new Rq1DatabaseField_Text(this, "ExternalTitle"));
        addField(PLANNING_GRANULARITY = new Rq1DatabaseField_Enumeration(this, "PlanningGranularity", PlanningGranularity.values()));
        addField(PRODUCT = new Rq1DatabaseField_Text(this, ATTRIBUTE_PRODUCT));
        addField(SCM_REFERENCES = new Rq1DatabaseField_Text(this, "hasBackground.SCMReferences"));
        SCM_REFERENCES.setOptional();
        SCM_REFERENCES.setReadOnly();
        addField(HAS_MAPPED_CHILDREN = new Rq1DatabaseField_MappedReferenceList(this, FIELDNAME_HAS_MAPPED_CHILDREN, "hasMappedChildRelease", Rq1RecordType.RRM, Rq1RecordType.RELEASE));
        addField(HAS_MAPPED_ISSUES = new Rq1DatabaseField_MappedReferenceList(this, ATTRIBUTE_HAS_MAPPED_ISSUES, "hasMappedIssue", Rq1RecordType.IRM, Rq1RecordType.ISSUE));
        addField(HAS_MAPPED_PARENTS = new Rq1DatabaseField_MappedReferenceList(this, "hasMappedParents", "hasMappedParentRelease", Rq1RecordType.RRM, Rq1RecordType.RELEASE));
        addField(HAS_PREDECESSOR = new Rq1DatabaseField_Reference(this, ATTRIBUTE_HAS_PREDECESSOR, Rq1RecordType.RELEASE));
        addField(HAS_SUCCESSOR = new Rq1DatabaseField_ReferenceList(this, "hasSuccessor", Rq1RecordType.RELEASE));

        addField(MILESTONES = new Rq1DatabaseField_Xml(this, "Milestones"));

        addField(START_DATE = new Rq1DatabaseField_Date(this, "StartDate"));

        addField(DEFAULT_REQUESTED_DELIVERY_DATE = new Rq1XmlSubField_Date(this, MILESTONES, "DefaultRequestedDeliveryDate"));
        addField(DELIVERY_FREEZE = new Rq1XmlSubField_Date(this, MILESTONES, "DeliveryFreeze"));
        addField(IMPLEMENTATION_FREEZE_DATABASE = new Rq1DatabaseField_Date(this, "ImplementationFreeze_Database", "ImplementationFreeze"));
        addField(IMPLEMENTATION_FREEZE_XML = new Rq1XmlSubField_Date(this, MILESTONES, "ImplementationFreeze"));
        addField(IMPLEMENTATION_FREEZE = new Rq1AlternativeField_Date(this, "ImplementationFreeze", IMPLEMENTATION_FREEZE_DATABASE, IMPLEMENTATION_FREEZE_XML));

        addField(GPM = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "GPM"));
        GPM.setOptional();
        addField(EXPORT_SCOPE = new Rq1XmlSubField_Enumeration(this, GPM, "Export_Scope", ExportScope.values(), ExportScope.EMPTY));
        EXPORT_SCOPE.setOptional();
    }

    @Override
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        Rq1AttributeName[] firstOrder = {ATTRIBUTE_BELONGS_TO_PROJECT, ATTRIBUTE_OPERATION_MODE};
        Rq1AttributeName[] completeOrder;
        if (fieldOrder != null) {
            completeOrder = Stream.concat(Arrays.stream(firstOrder), Arrays.stream(fieldOrder)).toArray(Rq1AttributeName[]::new);
        } else {
            completeOrder = firstOrder;
        }
        return (super.createInDatabase(completeOrder));
    }

    @Override
    protected void handleWriteValidationError(String validationError) {
        assert (validationError != null);

        if (validationError.toLowerCase().contains("wrong title") == true) {
            TITLE.addMarker(new DsFieldContentFailure_InvalidValue(successfullWriteRule, TITLE.getFieldName(), validationError));
        } else {
            super.handleWriteValidationError(validationError);
        }
    }

}
