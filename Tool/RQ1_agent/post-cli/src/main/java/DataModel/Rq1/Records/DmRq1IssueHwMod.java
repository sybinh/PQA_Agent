/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
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
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import static DataModel.Rq1.Records.DmRq1ElementCache.addElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1IssueMod;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1TemplateI;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmRq1IssueHwMod extends DmRq1HardwareIssue implements DmFlowIssue, FlowCopy, Comparable<DmRq1IssueHwMod> {

    public static class ResultMap {

        final private DmRq1Irm_ModRelease_IssueHw irm;
        final private DmRq1IssueHwMod issue;

        public ResultMap(DmRq1Irm_ModRelease_IssueHw irm, DmRq1IssueHwMod issue) {
            assert (irm != null);
            assert (issue != null);
            this.irm = irm;
            this.issue = issue;
        }

        public DmRq1Irm_ModRelease_IssueHw getIrm() {
            return irm;
        }

        public DmRq1IssueHwMod getIssue() {
            return issue;
        }

    }

    final public DmRq1Field_Enumeration APPROVAL;
    final public DmRq1Field_Reference<DmRq1IssueHwEcu> PARENT;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_ModRelease_IssueHw, DmRq1ModRelease> MAPPED_RELEASES;

    final public DmRq1Field_Enumeration FLOW_KIT_STATUS;
    final public DmRq1Field_Text FLOW_RANK;
    final public DmRq1Field_Text FLOW_VERSION;
    final public DmRq1Field_Text FLOW_R_DATE;
    final public DmRq1Field_Text FLOW_GROUP;
    final public DmRq1Field_Text FLOW_INTERNAL_RANK;
    final public DmRq1Field_Text FLOW_CLUSTERNAME;
    final public DmRq1Field_Text FLOW_CLUSTERID;
    final public DmRq1Field_Text FLOW_R_EFFORT;
    final public DmRq1Field_Text FLOW_NO_OF_DEVELOPERS;
    final public DmRq1Field_Date FLOW_TO_RED_DATE;
    final public DmRq1Field_Enumeration FLOW_SIZE;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    public InternalRank internalRank = null;
    final public DmRq1Field_Enumeration FLOW_STATUS;
    final public DmRq1Field_Text FLOW_INC_AS_TASK;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_Text FLOW_EXC_BOARD;
    final public DmRq1Field_Text FLOW_EST_SHEET_NAME;
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1IssueHwMod(Rq1IssueMod issue) {
        super("I-HW-MOD", issue);

        addField(APPROVAL = new DmRq1Field_Enumeration(this, issue.APPROVAL, "Approval"));
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        addField(PARENT = new DmRq1Field_Reference<>(this, issue.HAS_PARENT, "Parent"));
        addField(MAPPED_RELEASES = new DmRq1Field_MappedReferenceList<>(this, issue.HAS_MAPPED_RELEASES, "HW-MOD"));

        //
        // Fields for flow framework
        //
        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, issue.FLOW_KIT_STATUS, "Full-Kit-Status"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, issue.FLOW_STATUS, "Flow Task Status"));
        addField(FLOW_RANK = new DmRq1Field_Text(this, issue.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, issue.FLOW_VERSION, "Version"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, issue.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, issue.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, issue.FLOW_CLUSTERNAME, "Flow Cluster Name"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, issue.FLOW_GROUP, "Groups"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, issue.FLOW_CLUSTERID, "Flow Cluster ID"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, issue.FLOW_SIZE, "Size"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, issue.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, issue.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, issue.TO_RED_DATE, "To RED Date"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, issue.TARGET_DATE, "Planned-Date"));
        addField(FLOW_INC_AS_TASK = new DmRq1Field_Text(this, issue.FLOW_INC_AS_TASK, "Flow include as task in list"));

        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, issue.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, issue.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, issue.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(issue.FLOW_BLOCKER_TABLE, issue.FLOW_SUBTASK_TABLE, "Blocker with Subtask Title"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, issue.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, issue.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, issue.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_EXC_BOARD = new DmRq1Field_Text(this, issue.FLOW_EXC_BOARD, "Exclude from Board"));
        addField(FLOW_EST_SHEET_NAME = new DmRq1Field_Text(this, issue.FLOW_EST_SHEET_NAME, "Flow estimation sheet"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, issue.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
    }

    static public DmRq1IssueHwMod create() {
        Rq1IssueMod rq1Issue = new Rq1IssueMod();
        DmRq1IssueHwMod dmIssue = new DmRq1IssueHwMod(rq1Issue);
        addElement(rq1Issue, dmIssue);
        return (dmIssue);
    }

    public static ResultMap createBasedOnModRelease(DmRq1ModRelease release, DmRq1Project targetProject, Rq1TemplateI template) {

        assert (targetProject != null);
        assert (release != null);

        DmRq1IssueHwMod issueHwMod = create();
        DmRq1Irm_ModRelease_IssueHw irm;

        try {
            irm = DmRq1Irm_ModRelease_IssueHw.create(release, issueHwMod);
        } catch (DmRq1MapExistsException ex) {
            Logger.getLogger(DmRq1IssueHwEcu.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueHwEcu.class.getName(),ex);
            return (null);
        }

        //
        // Take over content from release
        //
        issueHwMod.ACCOUNT_NUMBERS.setValue(targetProject.ACCOUNT_NUMBERS.getValue());
        irm.ACCOUNT_NUMBERS.setValue(release.ACCOUNT_NUMBERS.getValue());
        irm.IS_PILOT.setValue(YesNoEmpty.YES);

        //
        // Connect Issue - Project
        //
        issueHwMod.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueHwMod);
        }
        return (new ResultMap(irm, issueHwMod));
    }

    static public DmRq1IssueHwMod createBasedOnParent(DmRq1IssueHwEcu issueHwEcu, DmRq1Project project, Rq1TemplateI template) {

        assert (issueHwEcu != null);
        assert (project != null);

        DmRq1IssueHwMod issueHwMod = create();

        //
        // Take over content from parent
        //
        issueHwMod.ACCOUNT_NUMBERS.setValue(issueHwEcu.ACCOUNT_NUMBERS.getValue());
        issueHwMod.DESCRIPTION.setValue(issueHwEcu.DESCRIPTION.getValue());
        issueHwMod.INTERNAL_COMMENT.setValue(issueHwEcu.INTERNAL_COMMENT.getValue());
        issueHwMod.SCOPE.setValue(Scope.INTERNAL);
        issueHwMod.TITLE.setValue(issueHwEcu.TITLE.getValue());
        issueHwMod.ASIL_CLASSIFICATION.setValue(issueHwEcu.ASIL_CLASSIFICATION.getValue());
        issueHwMod.FMEA_STATE.setValue(issueHwEcu.FMEA_STATE.getValue());
        issueHwMod.REQUIREMENTS_REVIEW.setValue(issueHwEcu.REQUIREMENTS_REVIEW.getValue());
        issueHwMod.DRBFM.setValue(issueHwEcu.DRBFM.getValue());
        issueHwMod.SPECIFICATION_REVIEW.setValue(issueHwEcu.SPECIFICATION_REVIEW.getValue());
        issueHwMod.DEVELOPMENT_METHOD.setValue(issueHwEcu.DEVELOPMENT_METHOD.getValue());

        issueHwMod.DEFECT_DETECTION_ORGANISATION.setValue(issueHwEcu.DEFECT_DETECTION_ORGANISATION.getValue());

        //
        // Connect parent-child
        //
        issueHwEcu.CHILDREN.addElement(issueHwMod);
        issueHwMod.PARENT.setElement(issueHwEcu);

        //
        // Connect project-issue
        //
        issueHwMod.PROJECT.setElement(project);
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which e.g. for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
//        project.ISSUES.addElement(issueFD);
        if (template != null) {
            template.execute(issueHwMod);
        }
        return (issueHwMod);
    }

    public static DmRq1IssueHwMod createBasedOnProject(DmRq1Project targetProject, Rq1TemplateI template) {

        assert (targetProject != null);

        DmRq1IssueHwMod issue = create();

        //
        // Take over content from project
        //
        issue.ACCOUNT_NUMBERS.setValue(targetProject.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Issue - Project
        //
        issue.PROJECT.setElement(targetProject);
        targetProject.OPEN_ISSUE_MOD.addElement(issue);
        if (template != null) {
            template.execute(issue);
        }
        return (issue);
    }
    
        /**
     * Creates an I-HW-MOD as successor for the given I-HW-MOD. The I-HW-MOD is created in the
     * given target project.
     *
     *
     * @param predecessor I-HW-MOD which shall be used as predecessor for the new
     * I-HW-MOD.
     * @param targetProject Project in which the new I-HW-MOD shall be created.
     * @return The new created I-HW-MOD.
     */
    public static DmRq1IssueHwMod createBasedOnPredecessor(DmRq1IssueHwMod predecessor, DmRq1Project targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        //
        // Create the new I-HW-MOD
        //
        DmRq1IssueHwMod issueHwMod = DmRq1IssueHwMod.create();

        //
        // Take over content from predecessor
        //
        issueHwMod.ACCOUNT_NUMBERS.setValue(predecessor.ACCOUNT_NUMBERS.getValue());
        issueHwMod.DESCRIPTION.setValue(predecessor.DESCRIPTION.getValue());
        issueHwMod.SCOPE.setValue(predecessor.SCOPE.getValue());
        issueHwMod.TITLE.setValue(predecessor.TITLE.getValue() + " Successor");
        issueHwMod.CATEGORY.setValue(predecessor.CATEGORY.getValue());
//        issueFD.PARENT.setElement(predecessor.PARENT.getElement());       ACHTUNG: Das gibt Probleme wenn der I-SW nicht mehr im Status NEW ist !!!

        //
        // Connect with predecessor
        //
        issueHwMod.PREDECESSOR.setElement(predecessor);
        predecessor.SUCCESSORS.addElement(issueHwMod);

        //
        // Connect Issue HW ECU with Project
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
        issueHwMod.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueHwMod);
        }
        return (issueHwMod);
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
    public String getClusterID() {
        return FLOW_CLUSTERID.getValueAsText();
    }

    @Override
    public String getGroupId() {
        return FLOW_GROUP.getValueAsText();
    }

    public String getIncAsTask() {
        return FLOW_INC_AS_TASK.getValueAsText();
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
    public int compareTo(DmRq1IssueHwMod o) {
        assert (o != null);

        return ((this.getId()).compareTo(o.getId()));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DmRq1IssueHwMod cloned = (DmRq1IssueHwMod) super.clone();
        DmRq1Field_MappedReferenceList list = (DmRq1Field_MappedReferenceList) cloned.HAS_MAPPED_RELEASES.clone();
        try {
            final Field[] fields = cloned.getClass().getFields();
            for (Field field : fields) {
                if (field.getName().equals("HAS_MAPPED_RELEASES")) {
                    field.setAccessible(true);

                    field.set(cloned, list);

                    break;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(DmRq1IssueHwMod.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueHwMod.class.getName(), ex);
        }

        return cloned;
    }

}
