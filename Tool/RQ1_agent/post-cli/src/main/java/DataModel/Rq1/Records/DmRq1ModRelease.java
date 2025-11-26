/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Dgs.DmDgsBcReleaseI;
import DataModel.Flow.DmFlowIssue;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.InternalRank;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1ModRelease;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Templates.Rq1TemplateI;
import util.EcvDate;
import util.UiWorker;

/**
 *
 * @author gug2wi
 */
public class DmRq1ModRelease extends DmRq1HardwareRelease implements DmDgsBcReleaseI, DmFlowIssue {

    final public DmRq1Field_Enumeration CLASSIFICATION;

    final public DmRq1Field_MappedReferenceList<DmRq1Rrm_Ecu_Mod, DmRq1EcuRelease> MAPPED_PARENT;

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
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;
    final public DmRq1Field_Text FLOW_EXC_BOARD;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1ModRelease(Rq1ModRelease release) {
        super("HW-MOD", release);

        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, release.CLASSIFICATION, "Classification"));

        addField(MAPPED_PARENT = new DmRq1Field_MappedReferenceList<>(this, release.HAS_MAPPED_PARENTS, "Parents"));

        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, release.FLOW_KIT_STATUS, "Full-Kit-Status"));
        addField(FLOW_RANK = new DmRq1Field_Text(this, release.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, release.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, release.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, release.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, release.FLOW_CLUSTERNAME, "Cluster Name"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, release.FLOW_CLUSTERID, "Cluster ID"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, release.FLOW_SIZE, "Size"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, release.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, release.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, release.FLOW_STATUS, "Task Status"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, release.FLOW_GROUP, "Groups"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, release.TO_RED_DATE, "To RED Date"));
        addField(FLOW_IRM_GROUP = new DmRq1Field_Text(this, release.FLOW_IRM_GROUP, "IRM Group"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, release.TARGET_DATE, "Planned-Date"));
        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, release.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, release.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, release.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, release.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, release.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, release.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, release.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_EXC_BOARD = new DmRq1Field_Text(this, release.FLOW_EXC_BOARD, "Exclude from Board"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(release.FLOW_BLOCKER_TABLE, release.FLOW_SUBTASK_TABLE, "Blocker with Subtask Title"));
    }

    private static DmRq1ModRelease create() {
        Rq1ModRelease rq1Release = new Rq1ModRelease();
        DmRq1ModRelease dmRelease = new DmRq1ModRelease(rq1Release);
        DmRq1ElementCache.addElement(rq1Release, dmRelease);
        return (dmRelease);
    }

    public static DmRq1ModRelease createBasedOnPredecessor(DmRq1ModRelease predecessor, DmRq1Project targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        final DmRq1ModRelease newRelease = create();

        //
        // Take over content from predecessor
        //
        newRelease.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");
        newRelease.CLASSIFICATION.setValue(predecessor.CLASSIFICATION.getValue());

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newRelease);
        newRelease.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newRelease.PROJECT.setElement(targetProject);
        UiWorker.execute(new UiWorker<Void>("Add HW-MOD to project") {

            @Override
            protected Void backgroundTask() {
                targetProject.OPEN_HW_MOD_RELEASES.addElement(newRelease);
                return (null);
            }
        });
        if (template != null) {
            template.execute(newRelease);
        }
        return (newRelease);
    }

    public final void addSuccessor(DmRq1ModRelease newSuccessor) throws ExistsAlready {
        super.addSuccessor(newSuccessor);
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
    
    
    public String getSwimlaneHeading() {
        return (FLOW_CLUSTERID.getValueAsText());
    }

    public KingState getKingState() {
        return ((KingState) KING_STATE.getValue());
    }

    public ExpertState getExpertState() {
        return ((ExpertState) EXPERT_STATE.getValue());
    }

    public String getExpertAvailEffort() {
        return FLOW_EXP_AVAL_EFFORT.getValueAsText();
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

    @Override
    public int compareTo(DmDgsBcReleaseI t) {
        return (BcReleaseComparator.compare(this, t));
    }

    @Override
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }

}
