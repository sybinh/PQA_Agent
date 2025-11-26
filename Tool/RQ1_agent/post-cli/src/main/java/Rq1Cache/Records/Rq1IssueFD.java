/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1MappedReferenceListField_FilterByClass;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIssue;
import Rq1Data.Enumerations.Approval;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.Sync;
import Rq1Data.Enumerations.EcbStandardjob;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class Rq1IssueFD extends Rq1SoftwareIssue {

    /**
     * Fields for Sync RQ1 ALM
     */
    final public Rq1DatabaseField_Enumeration SYNC;
    final public Rq1DatabaseField_Text LINKS;

    final public Rq1DatabaseField_Enumeration APPROVAL;
    final public Rq1XmlSubField_Date COMMERCIAL_GO_REQUIRED_BY;

    final public Rq1MappedReferenceListField_FilterByClass HAS_MAPPED_BC;
    final public Rq1MappedReferenceListField_FilterByClass HAS_MAPPED_FC;

    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L3_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L3_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L4_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L3_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L4_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L4_IMPACTED_REQUIREMENTS;

    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> SC_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> SC_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> SC_AFFECTED_ARTEFACT;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> SC_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_AFFECTED_ARTEFACT;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_REMOVED_REQUIREMENTS;

    final public Rq1XmlSubField_Text REQUIREMENTS_REVIEW_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text DEVELOPMENT_METHOD_COMMENT;
    final public Rq1XmlSubField_Text CSTCOMMENT;

    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Enumeration FLOW_KIT_STATUS;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Text FLOW_RANK;
    final public Rq1XmlSubField_Text FLOW_R_DATE;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Text FLOW_CLUSTERNAME;
    final public Rq1XmlSubField_Text FLOW_CLUSTERID;
    final public Rq1XmlSubField_Text FLOW_GROUP;
    final public Rq1XmlSubField_Text FLOW_R_EFFORT;
    final public Rq1XmlSubField_Text FLOW_NO_OF_DEVELOPERS;
    final public Rq1XmlSubField_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Text FLOW_EXC_BOARD;
    final public Rq1XmlSubField_Text FLOW_INC_AS_TASK;
    final public Rq1XmlSubField_Text FLOW_EST_SHEET_NAME;

    //
    // Fields for ESH
    //
    final public Rq1XmlSubField_Text ESH_SHORT_DESCRIPTION;

    //
    // Fields for ECB
    //
    final public Rq1XmlSubField_Text ECB_DIDINFO;
    final public Rq1XmlSubField_Enumeration ECB_STANDARDJOB_STATE;

    public Rq1IssueFD() {
        super(Rq1NodeDescription.ISSUE_FD);

        DEFECT_DETECTION_DATE.setReadOnlyForNewRecord();
        DEFECT_DETECTION_LOCATION.setReadOnlyForNewRecord();
        DEFECT_DETECTION_PROCESS.setReadOnlyForNewRecord();

        addField(SYNC = new Rq1DatabaseField_Enumeration(this, "Sync", Sync.values()));
        SYNC.acceptInvalidValuesInDatabase();
        addField(LINKS = new Rq1DatabaseField_Text(this, "Links"));
        LINKS.setReadOnly();

        addField(CSTCOMMENT = new Rq1XmlSubField_Text(this, TAGS, "CST_Comment"));

        addField(APPROVAL = new Rq1DatabaseField_Enumeration(this, "Approval", Approval.values()));
        addField(COMMERCIAL_GO_REQUIRED_BY = new Rq1XmlSubField_Date(this, MILESTONES, "CommercialGoRequiredBy"));

        addField(HAS_MAPPED_BC = new Rq1MappedReferenceListField_FilterByClass(this, HAS_MAPPED_RELEASES, Rq1Bc.class));
        addField(HAS_MAPPED_FC = new Rq1MappedReferenceListField_FilterByClass(this, HAS_MAPPED_RELEASES, Rq1Fc.class));

        addField(L3_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L3_impacted_requirements"));
        L3_IMPACTED_REQUIREMENTS.addAlternativName("L3_Impacted_requirements");
        L3_IMPACTED_REQUIREMENTS.addAlternativName("L3_Impacted_Requirements");
        L3_IMPACTED_REQUIREMENTS.addAlternativName("L3_impacted_requirement");
        addField(L3_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L3_planned_requirements"));
        L3_PLANNED_REQUIREMENTS.addAlternativName("L3_Planned_requirements");
        L3_PLANNED_REQUIREMENTS.addAlternativName("L3_Planned_Requirements");
        L3_PLANNED_REQUIREMENTS.addAlternativName("L3_planned_requirement");
        addField(L4_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L4_planned_requirements"));
        L4_PLANNED_REQUIREMENTS.addAlternativName("L4_Planned_requirements");
        L4_PLANNED_REQUIREMENTS.addAlternativName("L4_Planned_Requirements");
        L4_PLANNED_REQUIREMENTS.addAlternativName("L4_planned_requirement");
        addField(L3_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L3_removed_requirements"));
        L3_REMOVED_REQUIREMENTS.addAlternativName("L3_Removed_requirements");
        L3_REMOVED_REQUIREMENTS.addAlternativName("L3_Removed_Requirements");
        L3_REMOVED_REQUIREMENTS.addAlternativName("L3_removed_requirement");
        addField(L4_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L4_removed_requirements"));
        L4_REMOVED_REQUIREMENTS.addAlternativName("L4_Removed_requirements");
        L4_REMOVED_REQUIREMENTS.addAlternativName("L4_Removed_Requirements");
        L4_REMOVED_REQUIREMENTS.addAlternativName("L4_removed_requirement");
        addField(L4_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L4_impacted_requirements"));
        L4_IMPACTED_REQUIREMENTS.addAlternativName("L4_Impacted_requirements");
        L4_IMPACTED_REQUIREMENTS.addAlternativName("L4_Impacted_Requirements");
        L4_IMPACTED_REQUIREMENTS.addAlternativName("L4_impacted_requirement");

        addField(SC_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "SC_impacted_requirements"));
        addField(SC_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "SC_planned_requirements"));
        addField(SC_AFFECTED_ARTEFACT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "SC_affected_artefact"));
        SC_AFFECTED_ARTEFACT.addAlternativName("SC_affected_requirements");
        addField(SC_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "SC_removed_requirements"));
        addField(BC_FC_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "BC-FC_impacted_requirements"));
        addField(BC_FC_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "BC-FC_planned_requirements"));
        addField(BC_FC_AFFECTED_ARTEFACT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "BC-FC_affected_artefact"));
        BC_FC_AFFECTED_ARTEFACT.addAlternativName("BC-FC_affected_requirements");
        addField(BC_FC_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "BC-FC_removed_requirements"));

        addField(REQUIREMENTS_REVIEW_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "RequirementsReview_ChangeComment"));
        addField(DEVELOPMENT_METHOD_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DevelopmentMethod_Comment"));
        //
        // Fields for flow framework
        //
        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));

        FLOW.setOptional();
        addField(FLOW_KIT_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "FKS", FullKitStatus.values(), null));
        addField(FLOW_RANK = new Rq1XmlSubField_Text(this, FLOW, "RANK"));
        addField(FLOW_VERSION = new Rq1XmlSubField_Text(this, FLOW, "V"));
        addField(FLOW_R_DATE = new Rq1XmlSubField_Text(this, FLOW, "R_DATE"));
        addField(FLOW_INTERNAL_RANK = new Rq1XmlSubField_Text(this, FLOW, "INTERNAL_RANK"));
        addField(FLOW_CLUSTERNAME = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER"));
        addField(FLOW_GROUP = new Rq1XmlSubField_Text(this, FLOW, "GROUP"));
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
        addField(TARGET_DATE = new Rq1XmlSubField_Date(this, FLOW, "T_DATE"));
        addField(CRITICAL_RESOURCE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CriticalResource(), FLOW, "C_RES"));
        addField(FLOW_EXC_BOARD = new Rq1XmlSubField_Text(this, FLOW, "EXC_FROM_BOARD"));
        addField(FLOW_INC_AS_TASK = new Rq1XmlSubField_Text(this, FLOW, "INC_AS_TASK"));
        addField(FLOW_EST_SHEET_NAME = new Rq1XmlSubField_Text(this, FLOW, "EST_SHEET_NAME"));
        CRITICAL_RESOURCE.setOptional();
        TARGET_DATE.setOptional();
        FLOW_EXP_AVAl_EFFORT.setOptional();
        FLOW_EXC_BOARD.setOptional();
        FLOW_INC_AS_TASK.setOptional();

        //
        // Fields for ESH
        //
        addField(ESH_SHORT_DESCRIPTION = new Rq1XmlSubField_Text(this, TAGS, "sDesc"));

        //
        // Fields for ECB
        //
        addField(ECB_DIDINFO = new Rq1XmlSubField_Text(this, TAGS, "DIDinfo"));
        addField(ECB_STANDARDJOB_STATE = new Rq1XmlSubField_Enumeration(this, TAGS, "standardjobs", EcbStandardjob.values(), null));
    }

    public static Iterable<Rq1IssueFD> get_IFD_on_ISW_on_Project_for_Customer(String customerGroup) {
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.ISSUE_FD.getRecordType());
        query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_Issue.getAllOpenState());
        query.addCriteria_FixedValues(Rq1NodeDescription.ISSUE_FD.getFixedRecordValues());
        query.addCriteria_Value(Rq1IssueFD.ATTRIBUTE_HAS_PARENT, Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT, Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1IssueFD> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1IssueFD) {
                result.add((Rq1IssueFD) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

    public static Iterable<Rq1IssueFD> get_IFD_from_Project_on_ISW_on_Project_for_Customer(Rq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.ISSUE_FD.getRecordType());

        query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, LifeCycleState_Issue.getAllOpenState());
        query.addCriteria_FixedValues(Rq1NodeDescription.ISSUE_FD.getFixedRecordValues());
        query.addCriteria_Reference(Rq1IssueFD.ATTRIBUTE_BELONGS_TO_PROJECT.getName(), project);
        query.addCriteria_Value(
                Rq1IssueFD.ATTRIBUTE_HAS_PARENT,
                Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT,
                Rq1Project.ATTRIBUTE_CUSTOMER, customerGroup);

        //
        // Handle result
        //
        List<Rq1IssueFD> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1IssueFD) {
                result.add((Rq1IssueFD) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }
        return (result);
    }

}
