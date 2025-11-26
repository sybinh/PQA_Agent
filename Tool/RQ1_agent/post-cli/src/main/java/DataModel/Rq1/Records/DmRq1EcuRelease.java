/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Flow.DmFlowIssue;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FlowCopy;
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
import static DataModel.Rq1.Records.DmRq1ElementCache.addElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1EcuRelease;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Enumerations.CategoryEcuRelease;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1TemplateI;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public class DmRq1EcuRelease extends DmRq1HardwareRelease implements DmFlowIssue, FlowCopy {

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;

    //
    // Members for the flow model
    //
    final public DmRq1Field_Text FLOW_GROUP;
    final public DmRq1Field_Text FLOW_IRM_GROUP;
    final public DmRq1Field_Enumeration FLOW_KIT_STATUS;
    final public DmRq1Field_Text FLOW_RANK;
    final public DmRq1Field_Text FLOW_R_DATE;
    final public DmRq1Field_Text FLOW_INTERNAL_RANK;
    final public DmRq1Field_Text FLOW_CLUSTERNAME;
    final public DmRq1Field_Text FLOW_CLUSTERID;
    final public DmRq1Field_Date FLOW_TO_RED_DATE;
    final public DmRq1Field_Text FLOW_R_EFFORT;
    final public DmRq1Field_Text FLOW_NO_OF_DEVELOPERS;
    final public DmRq1Field_Text FLOW_ISW_IRM_TASK;
    final public DmRq1Field_Enumeration FLOW_SIZE;
    public InternalRank internalRank = null;
    final public DmRq1Field_Enumeration FLOW_STATUS;
    final public DmRq1Field_Text FLOW_VERSION;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Text PARENT_SWIMLANE;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public DmRq1Field_Text FLOW_INCLUDE_TO_LIST;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1EcuRelease(Rq1EcuRelease release) {
        super("HW-ECU", release);

        addField(CATEGORY = new DmRq1Field_Enumeration(this, release.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, release.CLASSIFICATION, "Classification"));

        //
        //
        // Fields for flow framework
        //
        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, release.FLOW_KIT_STATUS, "Full-Kit-Status"));

        addField(FLOW_RANK = new DmRq1Field_Text(this, release.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, release.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, release.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, release.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, release.FLOW_CLUSTERNAME, "Flow ClusterName"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, release.FLOW_CLUSTERID, "Flow Cluster ID"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, release.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, release.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_ISW_IRM_TASK = new DmRq1Field_Text(this, release.FLOW_ISW_IRM_TASK, "Flow I-SW/IRM Task"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, release.FLOW_SIZE, "Size"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, release.FLOW_STATUS, "Task Status"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, release.FLOW_GROUP, "Groups"));
        addField(FLOW_IRM_GROUP = new DmRq1Field_Text(this, release.FLOW_IRM_GROUP, "IRM Group"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, release.TO_RED_DATE, "To RED Date"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, release.TARGET_DATE, "Planned-Date"));
        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, release.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, release.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(PARENT_SWIMLANE = new DmRq1Field_Text(this, release.PARENT_SWIMLANE, "Parent_Swimlane"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, release.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, release.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, release.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, release.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, release.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_INCLUDE_TO_LIST = new DmRq1Field_Text(this, release.FLOW_INCLUDE_TO_LIST, "Flow Exclude from List"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(release.FLOW_BLOCKER_TABLE, release.FLOW_SUBTASK_TABLE, "Blocker with Subtask Title"));

    }

    static DmRq1EcuRelease create() {
        Rq1EcuRelease rq1HwEcuRelease = new Rq1EcuRelease();
        DmRq1EcuRelease dmHwEcuRelease = new DmRq1EcuRelease(rq1HwEcuRelease);
        addElement(rq1HwEcuRelease, dmHwEcuRelease);
        return (dmHwEcuRelease);
    }

    /**
     * Creates a ECU-RELEASE-HW based on a hardware project
     *
     * @param project
     * @param template
     * @return ECU-RELEASE-HW
     */
    public static DmRq1EcuRelease createBasedOnProject(DmRq1Project project, Rq1TemplateI template) {

        assert (project != null);

        DmRq1EcuRelease release = create();

        //
        // Set fixed values
        //
        release.CATEGORY.setValue(CategoryEcuRelease.HW_VERSION);
        release.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.NO);

        //
        // Take over content from parent
        //
        release.ACCOUNT_NUMBERS.setValue(project.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Release - Project
        //
        release.PROJECT.setElement(project);
        project.ALL_RELEASES.addElementIfLoaded(release);
        project.OPEN_RELEASES.addElementIfLoaded(release);
        if (template != null) {
            template.execute(release);
        }
        return (release);
    }

    public static DmRq1EcuRelease createBasedOnPredecessor(DmRq1EcuRelease predecessor, DmRq1Project targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        final DmRq1EcuRelease newEcuRelease = create();

        //
        // Take over content from predecessor
        //
        newEcuRelease.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");
        newEcuRelease.CATEGORY.setValue(predecessor.CATEGORY.getValue());
        newEcuRelease.CLASSIFICATION.setValue(predecessor.CLASSIFICATION.getValue());

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newEcuRelease);
        newEcuRelease.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newEcuRelease.PROJECT.setElement(targetProject);
        targetProject.ALL_RELEASES.addElementIfLoaded(newEcuRelease);
        targetProject.OPEN_RELEASES.addElementIfLoaded(newEcuRelease);
        if (template != null) {
            template.execute(newEcuRelease);
        }
        return (newEcuRelease);
    }

    public final void addSuccessor(DmRq1EcuRelease newSuccessor) throws ExistsAlready {
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

    @Override
    public FullKitSize getSize() {
        return ((FullKitSize) FLOW_SIZE.getValue());
    }

    @Override
    public String getClusterID() {
        return FLOW_CLUSTERID.getValueAsText();
    }

    @Override
    public String getGroupId() {
        return FLOW_GROUP.getValueAsText();
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
    public Object clone() throws CloneNotSupportedException {
        DmRq1EcuRelease cloned = (DmRq1EcuRelease) super.clone();
        DmRq1Field_MappedReferenceList list = (DmRq1Field_MappedReferenceList) cloned.MAPPED_CHILDREN.clone();
        DmRq1Field_MappedReferenceList mappedISwCloneList = (DmRq1Field_MappedReferenceList) cloned.MAPPED_ISSUES.clone();
        try {
            final Field[] fields = cloned.getClass().getFields();
            for (Field field : fields) {
                if (field.getName().equals("MAPPED_CHILDREN")) {
                    field.setAccessible(true);

                    field.set(cloned, list);

                } else if (field.getName().equals("MAPPED_ISSUES")) {
                    field.setAccessible(true);
                    field.set(cloned, mappedISwCloneList);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(DmRq1EcuRelease.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1EcuRelease.class.getName(), ex);
        }

        return cloned;
    }

    @Override
    public EcvDate getToRedDate() {
        return FLOW_TO_RED_DATE.getValue();
    }

    @Override
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }
    
    
    public String getSwimlaneHeading() {
        return (FLOW_CLUSTERID.getValueAsText());
    }

    public String getParentSwimlane() {
        return PARENT_SWIMLANE.getValueAsText();
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

}
