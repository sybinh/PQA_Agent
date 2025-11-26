/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Dgs.DmDgsBcReleaseI;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import DataModel.Flow.DmFlowIssue;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.InternalRank;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataModel.Flow.Util.ClearFlowTags;
import DataModel.Rq1.Monitoring.Rule_Bc_NamingConvention;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnBc;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnRelease;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_CheckForMissing_BaselineLink;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Records.Rq1Bc;
import Rq1Cache.Records.Rq1Fc;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1Project;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_FmeaDocument;
import java.util.ArrayList;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Bc extends DmRq1SoftwareRelease implements DmDgsBcReleaseI, DmFlowIssue {

    final public DmRq1Field_Enumeration SYNC;
    final public DmRq1Field_Text LINKS;

    final public DmRq1Field_Text DERIVATIVES;
    final public DmRq1Field_Enumeration PROCESS_TAILORING;
    final public DmRq1Field_AllRequirementsOnRelease MAPPED_REQUIREMENTS;

    final public DmRq1Field_Text EXTERNAL_SUBMITTER_NAME;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_EMAIL;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_PHONE;

    final public DmRq1Field_Reference<DmRq1Contact> EXTERNAL_ASSIGNEE;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_NAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_PHONE;
    final public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Pst> MAPPED_PST;

    final public DmRq1Field_Text FLOW_GROUP;
    final public DmRq1Field_Text FLOW_IRM_GROUP;
    final public DmRq1Field_Enumeration FLOW_KIT_STATUS;
    final public DmRq1Field_Text FLOW_RANK;
    final public DmRq1Field_Text FLOW_VERSION;
    final public DmRq1Field_Text FLOW_R_DATE;
    final public DmRq1Field_Text FLOW_INTERNAL_RANK;
    final public DmRq1Field_Text FLOW_CLUSTERNAME;
    final public DmRq1Field_Text FLOW_CLUSTERID;
    final public DmRq1Field_Text FLOW_R_EFFORT;
    final public DmRq1Field_Text FLOW_NO_OF_DEVELOPERS;
    final public DmRq1Field_Enumeration FLOW_SIZE;
    final public DmRq1Field_Date FLOW_TO_RED_DATE;
    public InternalRank internalRank = null;
    final public DmRq1Field_Enumeration FLOW_STATUS;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    final public DmRq1Field_Text FLOW_EXC_BOARD;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;

    final public DmRq1Field_Table<Rq1XmlTable_FmeaDocument> FMEA_DOCUMENT_TABLE;
    final public DmRq1Field_Text FMEA_VARIANT;

    public DmRq1Bc(String type, Rq1Bc rq1Bc) {
        super(type, rq1Bc);

        //
        // Create and add fields
        // 
        addField(SYNC = new DmRq1Field_Enumeration(this, rq1Bc.SYNC, "Sync"));
        addField(LINKS = new DmRq1Field_Text(this, rq1Bc.LINKS, "Links"));

        addField(DERIVATIVES = new DmRq1Field_Text(this, rq1Bc.DERIVATIVES, "Derivatives"));

        addField(PROCESS_TAILORING = new DmRq1Field_Enumeration(this, rq1Bc.PROCESS_TAILORING, "Process Tailoring"));
        PROCESS_TAILORING.setAttribute(FIELD_FOR_BULK_OPERATION);

        addField(MAPPED_REQUIREMENTS = new DmRq1Field_AllRequirementsOnBc(this, "Mapped Requirements"));

        addField(EXTERNAL_SUBMITTER_NAME = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_SUBMITTER_NAME, "External Submitter Name"));
        addField(EXTERNAL_SUBMITTER_ORGANIZATION = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_SUBMITTER_ORGANIZATION, "External Submitter Organization"));
        addField(EXTERNAL_SUBMITTER_DEPARTMENT = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_SUBMITTER_DEPARTMENT, "External Submitter Department"));
        addField(EXTERNAL_SUBMITTER_EMAIL = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_SUBMITTER_EMAIL, "External Submitter E-Mail"));
        addField(EXTERNAL_SUBMITTER_PHONE = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_SUBMITTER_PHONE, "External Submitter Phone Number"));

        addField(EXTERNAL_ASSIGNEE = new DmRq1Field_Reference<>(this, rq1Bc.EXTERNAL_ASSIGNEE, "External Assignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_ASSIGNEE_NAME, "External Assignee Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_ASSIGNEE_ORGANIZATION, "External Assignee Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_ASSIGNEE_DEPARTMENT, "External Assignee Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_ASSIGNEE_EMAIL, "External Assignee E-Mail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new DmRq1Field_Text(this, rq1Bc.EXTERNAL_ASSIGNEE_PHONE, "External Assignee Phone Number"));

        addField(MAPPED_PST = new DmRq1Field_MappedReferenceList<>(this, rq1Bc.HAS_MAPPED_PARENTS, "Parents"));

        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, rq1Bc.FLOW_KIT_STATUS, "Full-Kit-Status"));
        addField(FLOW_RANK = new DmRq1Field_Text(this, rq1Bc.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, rq1Bc.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, rq1Bc.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, rq1Bc.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, rq1Bc.FLOW_CLUSTERNAME, "Cluster Name"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, rq1Bc.FLOW_CLUSTERID, "Cluster ID"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, rq1Bc.FLOW_SIZE, "Size"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, rq1Bc.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, rq1Bc.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, rq1Bc.FLOW_STATUS, "Task Status"));
        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, rq1Bc.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, rq1Bc.FLOW_GROUP, "Groups"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, rq1Bc.TO_RED_DATE, "To RED Date"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, rq1Bc.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, rq1Bc.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, rq1Bc.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, rq1Bc.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, rq1Bc.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, rq1Bc.TARGET_DATE, "Planned-Date"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, rq1Bc.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_IRM_GROUP = new DmRq1Field_Text(this, rq1Bc.FLOW_IRM_GROUP, "IRM Group"));
        addField(FLOW_EXC_BOARD = new DmRq1Field_Text(this, rq1Bc.FLOW_EXC_BOARD, "Exclude from Board"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(rq1Bc.FLOW_BLOCKER_TABLE, rq1Bc.FLOW_SUBTASK_TABLE,"Blocker with Subtask Title"));
//
        addField(FMEA_DOCUMENT_TABLE = new DmRq1Field_Table<>(rq1Bc.FMEA_DOCUMENT_TABLE, "FMEA Documents"));
        addField(FMEA_VARIANT = new DmRq1Field_Text(rq1Bc.FMEA_VARIANT, "FMEA Variant"));
        
        addRule(new Rule_CheckForMissing_BaselineLink(this));
    }

    @Override
    final public String getType() {
        //
        // extractBcType() is implemented in the rule to keep all methods working with a name schema within one class.
        //
        return (Rule_Bc_NamingConvention.extractBcType(getTitle()));
    }

    @Override
    final public String getName() {
        //
        // extractBcName() is implemented in the rule to keep all methods working with a name schema within one class.
        //
        return (Rule_Bc_NamingConvention.extractBcName(getTitle()));
    }

    @Override
    final public boolean equals(Object o) {
        return (BcReleaseComparator.equals(this, o));
    }

    @Override
    final public int compareTo(DmDgsBcReleaseI t) {
        return (BcReleaseComparator.compare(this, t));
    }

    //-------------------------------------------------------------------------------------
    //
    // Load optimization
    //
    //-------------------------------------------------------------------------------------
    private boolean isLoadCacheForEditExternalCommentDone = false;

    public synchronized void loadCacheForEditExternalComment() {
        if (isLoadCacheForEditExternalCommentDone == false) {

            // Load all mapped I-FD and their mapped FC
            OslcLoadHint hint = new OslcLoadHint(false);
            hint.followField(Rq1Bc.ATTRIBUTE_HAS_MAPPED_ISSUES, true).followField(Rq1IssueFD.ATTRIBUTE_HAS_MAPPED_RELEASES, true);
            loadIntoCache(hint);

            // Load all mapped FC and their mapped I-FD and their mapped FC
            hint = new OslcLoadHint(false);
            hint.followField(Rq1Bc.FIELDNAME_HAS_MAPPED_CHILDREN, true).followField(Rq1Fc.ATTRIBUTE_HAS_MAPPED_ISSUES.getName(), true).followField(Rq1IssueFD.ATTRIBUTE_HAS_MAPPED_RELEASES, true);
            loadIntoCache(hint);

            isLoadCacheForEditExternalCommentDone = true;
        }
    }

    private boolean isLoadCacheForFcIssuesDone = false;

    public synchronized void loadCacheForFcIssues() {
        if (isLoadCacheForFcIssuesDone == false) {

            //
            // Load FC -> I-FD
            //
            OslcLoadHint loadHintForFc = new OslcLoadHint(false);
            loadHintForFc.followField(Rq1Bc.FIELDNAME_HAS_MAPPED_CHILDREN, true) // FCs
                    .followField(Rq1Fc.ATTRIBUTE_HAS_MAPPED_ISSUES.getName(), true); // I-FDs
            loadIntoCache(loadHintForFc);

            isLoadCacheForFcIssuesDone = true;
        }
    }

    /**
     * Returns all BC which belong to a I-SW which belongs to a project with the
     * given customer.
     *
     * @param project
     * @param customerGroup Group of the customer for I-SW.
     * @return
     */
    static public List<DmRq1Bc> get_BC_from_Project_on_ISW_on_Project_for_Customer(DmRq1Project project, String customerGroup) {
        assert (project != null);
        assert (customerGroup != null);
        assert (customerGroup.isEmpty() == false);

        List<DmRq1Bc> result = new ArrayList<>();
        for (Rq1Bc rq1Bc : Rq1Bc.get_BC_from_Project_on_ISW_on_Project_for_Customer((Rq1Project) project.getRq1Record(), customerGroup)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Bc);
            if (dmElement instanceof DmRq1Bc) {
                result.add((DmRq1Bc) dmElement);
            }
        }

        return (result);
    }

    @Override
    public String getRank() {
        return (FLOW_RANK.getValueAsText());
    }

    @Override
    public String getFlowVersion() {
        return (FLOW_VERSION.getValueAsText());
    }

    @Override
    public String getRemainingEffort() {
        return (FLOW_R_EFFORT.getValueAsText());
    }

    @Override
    public String getRequestedDate() {
        return (FLOW_R_DATE.getValueAsText());
    }

    @Override
    public FullKitStatus getStatus() {
        return ((FullKitStatus) FLOW_KIT_STATUS.getValue());
    }

    @Override
    public TaskStatus getTaskStatus() {
        return ((TaskStatus) FLOW_STATUS.getValue());
    }

    @Override
    public String getClusterName() {
        return FLOW_CLUSTERNAME.getValueAsText();
    }

    public String getIRMGroupStatus() {
        return FLOW_IRM_GROUP.getValueAsText();
    }

    @Override
    public FullKitSize getSize() {
        return ((FullKitSize) FLOW_SIZE.getValue());
    }

    @Override
    public InternalRank getInternalRank() throws InternalRank.BuildException {
        if (internalRank == null) {
            internalRank = InternalRank.buildForRecord(getRq1Id(), FLOW_RANK.getValue(), FLOW_INTERNAL_RANK.getValue());
            FLOW_INTERNAL_RANK.setValue(internalRank.toString());
        }
        return (internalRank);
    }

    @Override
    public InternalRank rankFirst(InternalRank currentFirst) throws InternalRank.BuildException {
        assert (currentFirst != null);

        internalRank = InternalRank.buildFirst(currentFirst);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    @Override
    public InternalRank rankBetween(InternalRank before, InternalRank after) throws InternalRank.BuildException {
        assert (before != null);
        assert (after != null);

        internalRank = InternalRank.buildBetween(before, after);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    @Override
    public InternalRank rankLast(InternalRank currentLast) throws InternalRank.BuildException {
        assert (currentLast != null);

        internalRank = InternalRank.buildLast(currentLast);
        FLOW_INTERNAL_RANK.setValue(internalRank.toString());

        return (internalRank);
    }

    public void setExistingIFdRank(InternalRank ifdRank) {
        internalRank = ifdRank;
        FLOW_INTERNAL_RANK.setValue(ifdRank.toString());
    }

    @Override
    public String getGroupId() {
        return FLOW_GROUP.getValueAsText();
    }

    @Override
    public String getClusterID() {
        return FLOW_CLUSTERID.getValueAsText();
    }

    @Override
    public EcvDate getToRedDate() {
        return FLOW_TO_RED_DATE.getValue();
    }

    public KingState getKingState() {
        return ((KingState) KING_STATE.getValue());
    }

    public ExpertState getExpertState() {
        return ((ExpertState) EXPERT_STATE.getValue());
    }

    @Override
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    @Override
    public void reload() {
        internalRank = null;
        super.reload();
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }

    public String getExpertAvailEffort() {
        return FLOW_EXP_AVAL_EFFORT.getValueAsText();
    }

    public String getExcFromBoard() {
        return FLOW_EXC_BOARD.getValueAsText();
    }

    @Override
    public void forward(DmRq1Project project,DmRq1User newAssignee) {
        ClearFlowTags.clearTags(project, this);
        super.forward(project, newAssignee);
    }
}
