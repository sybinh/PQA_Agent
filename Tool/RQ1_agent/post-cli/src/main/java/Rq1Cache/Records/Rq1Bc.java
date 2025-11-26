/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_BELONGS_TO_PROJECT;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_LIFE_CYCLE_STATE;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.ProcessTailoringBc;
import java.util.ArrayList;
import java.util.List;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import Rq1Cache.Fields.Rq1DatabaseField_ContactReference;
import Rq1Cache.Types.Rq1XmlTable_FmeaDocument;
import Rq1Data.Enumerations.Sync;

/**
 *
 * @author GUG2WI
 */
public class Rq1Bc extends Rq1SoftwareRelease {

    /**
     * Fields for Sync RQ1 and ALM
     */
    final public Rq1DatabaseField_Enumeration SYNC;
    final public Rq1DatabaseField_Text LINKS;
    /**
     *
     */

    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Enumeration FLOW_KIT_STATUS;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Text FLOW_RANK;
    final public Rq1XmlSubField_Text FLOW_R_DATE;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Text FLOW_CLUSTERNAME;
    final public Rq1XmlSubField_Text FLOW_CLUSTERID;
    final public Rq1XmlSubField_Text FLOW_GROUP;
    final public Rq1XmlSubField_Text FLOW_IRM_GROUP;
    final public Rq1XmlSubField_Text FLOW_R_EFFORT;
    final public Rq1XmlSubField_Text FLOW_NO_OF_DEVELOPERS;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Text FLOW_EXC_BOARD;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;

    final public Rq1DatabaseField_Text DERIVATIVES;
    final public Rq1DatabaseField_Enumeration PROCESS_TAILORING;

    final public Rq1DatabaseField_ContactReference EXTERNAL_SUBMITTER;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_NAME;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_ORGANIZATION;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_DEPARTMENT;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_EMAIL;
    final public Rq1DatabaseField_Text EXTERNAL_SUBMITTER_PHONE;

    final public Rq1DatabaseField_ContactReference EXTERNAL_ASSIGNEE;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_NAME;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public Rq1DatabaseField_Text EXTERNAL_ASSIGNEE_PHONE;

    final public Rq1XmlSubField_Table<Rq1XmlTable_FmeaDocument> FMEA_DOCUMENT_TABLE;
    final public Rq1XmlSubField_Text FMEA_VARIANT;

    public Rq1Bc(Rq1NodeDescription nodeDescription) {
        super(nodeDescription);

        //
        // Create and add fields
        //
        addField(SYNC = new Rq1DatabaseField_Enumeration(this, "Sync", Sync.values()));
        SYNC.acceptInvalidValuesInDatabase();
        addField(LINKS = new Rq1DatabaseField_Text(this, "Links"));
        LINKS.setReadOnly();

        addField(DERIVATIVES = new Rq1DatabaseField_Text(this, "Derivatives"));
        addField(PROCESS_TAILORING = new Rq1DatabaseField_Enumeration(this, "ProcessTailoring", ProcessTailoringBc.values()));
        PROCESS_TAILORING.acceptInvalidValuesInDatabase();

        addField(EXTERNAL_SUBMITTER = new Rq1DatabaseField_ContactReference(this, "ExternalSubmitter"));
        addField(EXTERNAL_SUBMITTER_NAME = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Name"));
        addField(EXTERNAL_SUBMITTER_ORGANIZATION = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Organization"));
        addField(EXTERNAL_SUBMITTER_DEPARTMENT = new Rq1DatabaseField_Text(this, "ExternalSubmitter.Department"));
        addField(EXTERNAL_SUBMITTER_EMAIL = new Rq1DatabaseField_Text(this, "ExternalSubmitter.eMail"));
        addField(EXTERNAL_SUBMITTER_PHONE = new Rq1DatabaseField_Text(this, "ExternalSubmitter.PhoneNumbers"));
        EXTERNAL_SUBMITTER.setFieldForName(EXTERNAL_SUBMITTER_NAME);
        EXTERNAL_SUBMITTER.setFieldForOrganization(EXTERNAL_SUBMITTER_ORGANIZATION);
        EXTERNAL_SUBMITTER.setFieldForDepartment(EXTERNAL_SUBMITTER_DEPARTMENT);
        EXTERNAL_SUBMITTER.setFieldForEmail(EXTERNAL_SUBMITTER_EMAIL);
        EXTERNAL_SUBMITTER.setFieldForPhone(EXTERNAL_SUBMITTER_PHONE);
        EXTERNAL_SUBMITTER_NAME.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_ORGANIZATION.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_DEPARTMENT.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_EMAIL.setOptional().setNoWriteBack();
        EXTERNAL_SUBMITTER_PHONE.setOptional().setNoWriteBack();

        addField(EXTERNAL_ASSIGNEE = new Rq1DatabaseField_ContactReference(this, "ExternalAssignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new Rq1DatabaseField_Text(this, "ExternalAssignee.Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new Rq1DatabaseField_Text(this, "ExternalAssignee.Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new Rq1DatabaseField_Text(this, "ExternalAssignee.Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new Rq1DatabaseField_Text(this, "ExternalAssignee.eMail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new Rq1DatabaseField_Text(this, "ExternalAssignee.PhoneNumbers"));
        EXTERNAL_ASSIGNEE.setFieldForName(EXTERNAL_ASSIGNEE_NAME);
        EXTERNAL_ASSIGNEE.setFieldForOrganization(EXTERNAL_ASSIGNEE_ORGANIZATION);
        EXTERNAL_ASSIGNEE.setFieldForDepartment(EXTERNAL_ASSIGNEE_DEPARTMENT);
        EXTERNAL_ASSIGNEE.setFieldForEmail(EXTERNAL_ASSIGNEE_EMAIL);
        EXTERNAL_ASSIGNEE.setFieldForPhone(EXTERNAL_ASSIGNEE_PHONE);
        EXTERNAL_ASSIGNEE_NAME.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_ORGANIZATION.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_DEPARTMENT.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_EMAIL.setOptional().setNoWriteBack();
        EXTERNAL_ASSIGNEE_PHONE.setOptional().setNoWriteBack();

        //
        // Fields for flow framework
        //
        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        FLOW.setOptional();
        addField(FLOW_KIT_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "FKS", FullKitStatus.values(), null));
        addField(FLOW_RANK = new Rq1XmlSubField_Text(this, FLOW, "RANK"));
        addField(FLOW_VERSION = new Rq1XmlSubField_Text(this, FLOW, "V"));
        addField(FLOW_R_DATE = new Rq1XmlSubField_Text(this, FLOW, "R_DATE"));
        addField(FLOW_GROUP = new Rq1XmlSubField_Text(this, FLOW, "GROUP"));
        addField(FLOW_IRM_GROUP = new Rq1XmlSubField_Text(this, FLOW, "IRM_GROUP"));
        addField(FLOW_INTERNAL_RANK = new Rq1XmlSubField_Text(this, FLOW, "INTERNAL_RANK"));
        addField(FLOW_CLUSTERNAME = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER"));
        addField(FLOW_R_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "R_EFFORT"));
        addField(FLOW_NO_OF_DEVELOPERS = new Rq1XmlSubField_Text(this, FLOW, "NB_D"));
        addField(FLOW_SIZE = new Rq1XmlSubField_Enumeration(this, FLOW, "SIZE", FullKitSize.values(), null));
        addField(FLOW_CLUSTERID = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER_ID"));
        addField(FLOW_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "STATUS", TaskStatus.values(), null));
        addField(FLOW_SUBTASK_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowSubTask(), FLOW, "SUBTASK"));
        addField(TO_RED_DATE = new Rq1XmlSubField_Date(this, FLOW, "TO_RED"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new Rq1XmlSubField_Text(this, FLOW, "SL_H"));
        addField(KING_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "KING", KingState.values(), null));
        addField(FLOW_BLOCKER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowBlocker(), FLOW, "BLOCKER"));
        addField(EXPERT_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "EXP", ExpertState.values(), null));
        addField(FLOW_EXP_AVAl_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "EXP_EFFORT"));
        FLOW_EXP_AVAl_EFFORT.setOptional();
        addField(FLOW_EXC_BOARD = new Rq1XmlSubField_Text(this, FLOW, "EXC_FROM_BOARD"));
        addField(TARGET_DATE = new Rq1XmlSubField_Date(this, FLOW, "T_DATE"));
        addField(CRITICAL_RESOURCE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CriticalResource(), FLOW, "C_RES"));
        CRITICAL_RESOURCE.setOptional();
        TARGET_DATE.setOptional();
        FLOW_IRM_GROUP.setOptional();
        FLOW_EXC_BOARD.setOptional();

        SCOPE.setReadOnly();

        addField(FMEA_DOCUMENT_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FmeaDocument(), TAGS, "FMEAdocument"));
        addField(FMEA_VARIANT = new Rq1XmlSubField_Text(this, TAGS, "FMEAvariant"));

//        IMPLEMENTATION_FREEZE.setReadOnlyForNewRecord();
    }

    public static Iterable<Rq1Bc> get_BC_from_Project_on_ISW_on_Project_for_Customer(Rq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.BC_RELEASE.getRecordType());

        query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_Release.getAllOpenState());
        query.addCriteria_Value(Rq1NodeDescription.TYPE_FIELDNAME, Rq1NodeDescription.TYPE_BC);
        query.addCriteria_Reference(ATTRIBUTE_BELONGS_TO_PROJECT, project);
        query.addCriteria_Value(
                Rq1BcRelease.ATTRIBUTE_HAS_MAPPED_ISSUES,
                Rq1Irm_Fc_IssueFd.ATTRIBUTE_HAS_MAPPED_ISSUE,
                Rq1IssueFD.ATTRIBUTE_HAS_PARENT,
                Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT,
                Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1Bc> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1Bc) {
                result.add((Rq1Bc) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

}
