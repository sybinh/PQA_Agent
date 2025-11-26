/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Dgs.DmDgsBcReleaseI;
import DataModel.Dgs.DmDgsFcReleaseI;
import DataModel.Flow.DmFlowIssue;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FlowCopy;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.InternalRank;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnPst;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnRelease;
import DataModel.Rq1.Fields.DmRq1Codex_Case;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Derivatives;
import DataModel.Rq1.Fields.DmRq1Field_DerivativesDate;
import DataModel.Rq1.Fields.DmRq1Field_DerivativesTable;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_FcMappedToPst;
import DataModel.Rq1.Fields.DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_SoftwareMetrics;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_AccountNumberFormat;
import DataModel.Rq1.Monitoring.Rule_Prg_Lumpensammler;
import DataModel.Rq1.Monitoring.Rule_CheckForMissing_BaselineLink;
import DataModel.DmMappedElement;
import Monitoring.RuleExecutionGroup;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Records.Rq1Bc;
import Rq1Cache.Records.Rq1Irm;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1IssueSW;
import Rq1Cache.Records.Rq1Pst;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FLowCCPMConfiguraion;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_MissingPlanningForOpt;
import Rq1Data.Enumerations.IntegrationAction;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Types.Rq1LineEcuProject;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;
import util.EcvMapList;
import util.EcvTableData;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Pst extends DmRq1SoftwareRelease implements DmFlowIssue, FlowCopy {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmRq1Pst.class.getCanonicalName());

    final public DmRq1Field_Enumeration SWTEST_BFT;
    final public DmRq1Field_Enumeration SWTEST_COM;
    final public DmRq1Field_Enumeration SWTEST_CST;
    final public DmRq1Field_Enumeration SWTEST_EEPROM;
    final public DmRq1Field_Enumeration SWTEST_FT;
    final public DmRq1Field_Enumeration SWTEST_IO;
    final public DmRq1Field_Enumeration SWTEST_OPT;
    final public DmRq1Field_Enumeration SWTEST_OST;
    final public DmRq1Field_Enumeration SWTEST_PVER_CONF;
    final public DmRq1Field_Enumeration SWTEST_PVER_I;
    final public DmRq1Field_Enumeration SWTEST_ROBUSTNESS;
    final public DmRq1Field_Enumeration SWTEST_SRR;
    final public DmRq1Field_Enumeration SWTEST_VIVA;

    final public DmRq1Field_Text SWTEST_BFT_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_COM_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_CST_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_EEPROM_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_FT_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_IO_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_OPT_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_OST_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_PVER_CONF_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_PVER_I_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_SRR_CHANGE_COMMENT;
    final public DmRq1Field_Text SWTEST_VIVA_CHANGE_COMMENT;

    final public DmRq1Field_Text SWTEST_BFT_COMMENT;
    final public DmRq1Field_Text SWTEST_COM_COMMENT;
    final public DmRq1Field_Text SWTEST_CST_COMMENT;
    final public DmRq1Field_Text SWTEST_EEPROM_COMMENT;
    final public DmRq1Field_Text SWTEST_FT_COMMENT;
    final public DmRq1Field_Text SWTEST_IO_COMMENT;
    final public DmRq1Field_Text SWTEST_OPT_COMMENT;
    final public DmRq1Field_Text SWTEST_OST_COMMENT;
    final public DmRq1Field_Text SWTEST_PVER_CONF_COMMENT;
    final public DmRq1Field_Text SWTEST_PVER_I_COMMENT;
    final public DmRq1Field_Text SWTEST_SRR_COMMENT;
    final public DmRq1Field_Text SWTEST_VIVA_COMMENT;
    final public DmRq1Field_Date DELIVERY_TO_CALIBRATION;
    final public DmRq1Field_Date TEST_FREEZE;

    final public DmRq1Field_DerivativesDate PLANNED_DATE_DERIVATIVES;
    final public DmRq1Field_Derivatives DERIVATIVES;
    final public DmRq1Field_DerivativesTable DERIVATIVES_TABLE;

    final public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> MAPPED_PARENTS;

    final public DmRq1Field_Enumeration PROPLATO_PROGRAMMSTANDSKENNUNG;
    final public DmRq1Field_Enumeration PROPLATO_KAUFMAENNISCH_GEPLANT;
    final public DmRq1Field_Date PROPLATO_ABLIEFERSTAND_DATUM;

    final public DmRq1Field_AllRequirementsOnRelease MAPPED_REQUIREMENTS;

    final public DmRq1Field_SoftwareMetrics SW_METRICS;

    final public DmRq1Field_Table<Rq1XmlTable_MissingPlanningForOpt> MISSING_PLANNING_FOR_OPT;

    final private Map<DmPPTLine, DmPPTRelease> PPT_Line_Release;

    final public DmRq1Field_Table<Rq1XmlTable_FLowCCPMConfiguraion> FLOW_CCPM_CONFIG;
    final public DmRq1Field_Text FLOW_VERSION;

    final public DmRq1Field_FcMappedToPst MAPPED_FC;

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
    final public DmRq1Field_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK;
    final public DmRq1Field_Enumeration KING_STATE;
    final public DmRq1Field_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER;
    final public DmRq1Field_Enumeration EXPERT_STATE;
    final public DmRq1Field_Text FLOW_EXP_AVAL_EFFORT;
    final public DmRq1Field_Date FLOW_TARGET_DATE;
    final public DmRq1Field_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public DmRq1Field_Text PARENT_SWIMLANE;
    final public DmRq1Field_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public DmRq1Field_Text FLOW_INCLUDE_TO_LIST;
    final private Rq1Pst rq1PstRelease;
    final public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE;

    final public DmRq1Field_ReferenceList<DmRq1WorkItem> CODEXCASE;

    /**
     * Implements additional functionality for PST objects.
     *
     * Note: The separation of DmRq1Pst in DmRq1Pat_Base and DmRq1Pst_Extended
     * exists because these two classes origin from a earlier implementation
     * with a different class hierarchy. To reduce the risk during the
     * restructuring process, the two classes were not consolidated in one class
     * but only renamed.
     *
     * @param subjectType
     * @param rq1Pst
     */
    public DmRq1Pst(String subjectType, Rq1Pst rq1Pst) {
        super(subjectType, rq1Pst);
        this.rq1PstRelease = rq1Pst;

        PPT_Line_Release = new IdentityHashMap<>();

        //
        //Field for codex cases 
        //
        addField(CODEXCASE = new DmRq1Codex_Case(this, rq1Pst.HAS_WORKITEMS, "CodexCase"));

        //
        // Create and add fields
        //
        addField(SWTEST_BFT = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_BFT, "SWTest_BFT"));
        addField(SWTEST_COM = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_COM, "SWTest_COM"));
        addField(SWTEST_CST = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_CST, "SWTest_CST"));
        addField(SWTEST_EEPROM = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_EEPROM, "SWTest_EEPROM"));
        addField(SWTEST_FT = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_FT, "SWTest_FT"));
        addField(SWTEST_IO = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_IO, "SWTest_IO"));
        addField(SWTEST_OPT = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_OPT, "SWTest_OPT"));
        addField(SWTEST_OST = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_OST, "SWTest_OST"));
        addField(SWTEST_PVER_CONF = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_PVER_CONF, "SWTest_PVER-Conf"));
        addField(SWTEST_PVER_I = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_PVER_I, "SWTest_PVER-I"));
        addField(SWTEST_ROBUSTNESS = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_ROBUSTNESS, "ROBUSTNESS"));
        addField(SWTEST_SRR = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_SRR, "SWTest_SRR"));
        addField(SWTEST_VIVA = new DmRq1Field_Enumeration(this, rq1Pst.SWTEST_VIVA, "SWTest_VIVA"));

        addField(SWTEST_BFT_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_BFT_COMMENT, "BFT Comment"));
        addField(SWTEST_COM_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_COM_COMMENT, "COM Comment"));
        addField(SWTEST_CST_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_CST_COMMENT, "CST Comment"));
        addField(SWTEST_EEPROM_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_EEPROM_COMMENT, "EEPROM Comment"));
        addField(SWTEST_FT_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_FT_COMMENT, "FT Comment"));
        addField(SWTEST_IO_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_IO_COMMENT, "IO Comment"));
        addField(SWTEST_OPT_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_OPT_COMMENT, "OPT Comment"));
        addField(SWTEST_OST_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_OST_COMMENT, "OST Comment"));
        addField(SWTEST_PVER_CONF_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_PVER_CONF_COMMENT, "PVER-Conf Comment"));
        addField(SWTEST_PVER_I_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_PVER_I_COMMENT, "PVER-I Comment"));
        addField(SWTEST_SRR_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_SRR_COMMENT, "SRR Comment"));
        addField(SWTEST_VIVA_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_VIVA_COMMENT, "VIVA Comment"));

        addField(SWTEST_BFT_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_BFT_CHANGE_COMMENT, "BFT Change Comment"));
        addField(SWTEST_COM_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_COM_CHANGE_COMMENT, "COM Change Comment"));
        addField(SWTEST_CST_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_CST_CHANGE_COMMENT, "CST Change Comment"));
        addField(SWTEST_EEPROM_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_EEPROM_CHANGE_COMMENT, "EEPROM Change Comment"));
        addField(SWTEST_FT_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_FT_CHANGE_COMMENT, "FT Change Comment"));
        addField(SWTEST_IO_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_IO_CHANGE_COMMENT, "IO Change Comment"));
        addField(SWTEST_OPT_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_OPT_CHANGE_COMMENT, "OPT Change Comment"));
        addField(SWTEST_OST_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_OST_CHANGE_COMMENT, "OST Change Comment"));
        addField(SWTEST_PVER_CONF_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_PVER_CONF_CHANGE_COMMENT, "PVER-Conf Change Comment"));
        addField(SWTEST_PVER_I_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_PVER_I_CHANGE_COMMENT, "PVER-I Change Comment"));
        addField(SWTEST_SRR_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_SRR_CHANGE_COMMENT, "SRR Change Comment"));
        addField(SWTEST_VIVA_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Pst.SWTEST_VIVA_CHANGE_COMMENT, "VIVA Change Comment"));

        addField(DERIVATIVES = new DmRq1Field_Derivatives(this, rq1Pst.DERIVATIVES, "_Derivatives_")); // Name conflict with filed below.

        addField(DELIVERY_TO_CALIBRATION = new DmRq1Field_Date(this, rq1Pst.DELIVERY_TO_CALIBRATION, "Delivery to Calibration"));
        addField(new DmRq1Field_Date(this, rq1Pst.SPECIFICATION_FREEZE, "SpecificationFreeze"));
        addField(TEST_FREEZE = new DmRq1Field_Date(this, rq1Pst.TEST_FREEZE, "Test Freeze"));
        addField(PLANNED_DATE_DERIVATIVES = new DmRq1Field_DerivativesDate(this, rq1Pst.PLANNED_DATE_XML, "Planned Date XML"));

        addField(DERIVATIVES_TABLE = new DmRq1Field_DerivativesTable(this, DERIVATIVES, PROJECT, PLANNED_DATE_DERIVATIVES, "Derivatives"));
        addField(MAPPED_PARENTS = new DmRq1Field_MappedReferenceList<>(this, rq1Pst.HAS_MAPPED_PARENTS, "Parents"));

        addField(PROPLATO_PROGRAMMSTANDSKENNUNG = new DmRq1Field_Enumeration(this, rq1Pst.PROPLATO_PROGRAMMSTANDSKENNUNG, "Programmstandskennung"));
        addField(PROPLATO_KAUFMAENNISCH_GEPLANT = new DmRq1Field_Enumeration(this, rq1Pst.PROPLATO_KAUFMAENNISCH_GEPLANT, "Kaufmännisch geplant"));
        addField(PROPLATO_ABLIEFERSTAND_DATUM = new DmRq1Field_Date(this, rq1Pst.PROPLATO_ABLIEFERSTAND_DATUM, "Ablieferstand Datum"));

        addField(MAPPED_REQUIREMENTS = new DmRq1Field_AllRequirementsOnPst(this, "Mapped Requirements"));

        addField(SW_METRICS = new DmRq1Field_SoftwareMetrics(rq1Pst.SW_METRICS_VALUES, "Software Metrics"));

        addField(MISSING_PLANNING_FOR_OPT = new DmRq1Field_Table<>(this, rq1Pst.MISSING_PLANNING_FOR_OPT, "Missing Planning for OPT"));

        addField(MAPPED_FC = new DmRq1Field_FcMappedToPst(MAPPED_CHILDREN, "Mapped FC"));

        //
        //
        // Fields for flow framework
        //
        addField(FLOW_KIT_STATUS = new DmRq1Field_Enumeration(this, rq1PstRelease.FLOW_KIT_STATUS, "Full-Kit-Status"));

        addField(FLOW_RANK = new DmRq1Field_Text(this, rq1PstRelease.FLOW_RANK, "Rank"));
        addField(FLOW_VERSION = new DmRq1Field_Text(this, rq1PstRelease.FLOW_VERSION, "Version"));
        addField(FLOW_CCPM_CONFIG = new DmRq1Field_Table<>(this, rq1PstRelease.FLOW_CCPM_CONFIG, "Flow CCPM"));
        addField(FLOW_R_DATE = new DmRq1Field_Text(this, rq1PstRelease.FLOW_R_DATE, "Requested Date"));
        addField(FLOW_INTERNAL_RANK = new DmRq1Field_Text(this, rq1PstRelease.FLOW_INTERNAL_RANK, "Internal Rank"));
        addField(FLOW_CLUSTERNAME = new DmRq1Field_Text(this, rq1PstRelease.FLOW_CLUSTERNAME, "Flow ClusterName"));
        addField(FLOW_CLUSTERID = new DmRq1Field_Text(this, rq1PstRelease.FLOW_CLUSTERID, "Flow Cluster ID"));
        addField(FLOW_R_EFFORT = new DmRq1Field_Text(this, rq1PstRelease.FLOW_R_EFFORT, "Remaining Effort"));
        addField(FLOW_NO_OF_DEVELOPERS = new DmRq1Field_Text(this, rq1PstRelease.FLOW_NO_OF_DEVELOPERS, "No of Developers"));
        addField(FLOW_ISW_IRM_TASK = new DmRq1Field_Text(this, rq1PstRelease.FLOW_ISW_IRM_TASK, "Flow I-SW/IRM Task"));
        addField(FLOW_SIZE = new DmRq1Field_Enumeration(this, rq1PstRelease.FLOW_SIZE, "Size"));
        addField(FLOW_STATUS = new DmRq1Field_Enumeration(this, rq1PstRelease.FLOW_STATUS, "Task Status"));
        addField(FLOW_GROUP = new DmRq1Field_Text(this, rq1PstRelease.FLOW_GROUP, "Groups"));
        addField(FLOW_SUBTASK = new DmRq1Field_Table<>(this, rq1PstRelease.FLOW_SUBTASK_TABLE, "Flow Subtask"));
        addField(FLOW_TO_RED_DATE = new DmRq1Field_Date(this, rq1PstRelease.TO_RED_DATE, "To RED Date"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new DmRq1Field_Text(this, rq1PstRelease.FLOW_BOARD_SWIMLANE_HEADING, "Board Swimlane Heading"));
        addField(PARENT_SWIMLANE = new DmRq1Field_Text(this, rq1PstRelease.PARENT_SWIMLANE, "Parent_Swimlane"));
        addField(KING_STATE = new DmRq1Field_Enumeration(this, rq1PstRelease.KING_STATE, "King State"));
        addField(FLOW_BLOCKER = new DmRq1Field_Table<>(this, rq1PstRelease.FLOW_BLOCKER_TABLE, "Blocker"));
        addField(EXPERT_STATE = new DmRq1Field_Enumeration(this, rq1PstRelease.EXPERT_STATE, "Expert State"));
        addField(FLOW_EXP_AVAL_EFFORT = new DmRq1Field_Text(this, rq1PstRelease.FLOW_EXP_AVAl_EFFORT, "Expert Effort"));
        addField(FLOW_TARGET_DATE = new DmRq1Field_Date(this, rq1PstRelease.TARGET_DATE, "Planned-Date"));
        addField(CRITICAL_RESOURCE = new DmRq1Field_Table<>(this, rq1PstRelease.CRITICAL_RESOURCE, "Critical Resource"));
        addField(FLOW_IRM_GROUP = new DmRq1Field_Text(this, rq1PstRelease.FLOW_IRM_GROUP, "IRM Group"));
        addField(FLOW_INCLUDE_TO_LIST = new DmRq1Field_Text(this, rq1PstRelease.FLOW_INCLUDE_TO_LIST, "Flow Exclude from List"));
        addField(FLOW_BLOCKERTABLE_WITH_FLOWSUBTASKTITLE = new DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(rq1PstRelease.FLOW_BLOCKER_TABLE, rq1PstRelease.FLOW_SUBTASK_TABLE, "Blocker with Subtask Title"));
        //
        // Create and add rules
        //
        addRule(new Rule_Prg_Lumpensammler(this));
        addRule(new Rule_AccountNumberFormat(this));
        addRule(new Rule_CheckForMissing_BaselineLink(this));
    }

    @Override
    protected synchronized void setSwitchableGroupActiveForSubElements(RuleExecutionGroup group, boolean activate) {
        assert (group != null);
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> issueMap : MAPPED_ISSUES.getElementList()) {
            issueMap.getMap().setSwitchableGroupActive(group, activate);
            issueMap.getTarget().setSwitchableGroupActive(group, activate);
        }
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> childrenMap : MAPPED_CHILDREN.getElementList()) {
            childrenMap.getMap().setSwitchableGroupActive(group, activate);
        }
    }

    /**
     * Returns the internal names of all active derivatives of the PST.
     *
     * @return List of derivative names.
     */
    abstract public SortedSet<String> getActiveDerivatives();

    final public void addSuccessor(DmRq1Pst newSuccessor) throws ExistsAlready {
        super.addSuccessor(newSuccessor);
    }

    @Override
    public List<MailActionType> getActionName() {
        List<MailActionType> mailActionTypes = new ArrayList<>();
        mailActionTypes.add(MailActionType.ASSIGNEE);
        return mailActionTypes;
    }

    /**
     * Returns the field CLASSIFICATION. The field CLASSIFICATION is defined in
     * the sub classes, because the allowed values differ from type to type.
     *
     * @return Content of CLASSIFICATION field.
     */
    protected abstract DmRq1Field_Enumeration getClassificationField();

    public String getClassificationAsText() {
        return (getClassificationField().getValueAsText());
    }

    //-------------------------------------------------------------------------------------
    //
    // Load optimization
    //
    //-------------------------------------------------------------------------------------
    private boolean loadCache_ISW = false;
    private boolean loadCache_ISW_WI = false;
    private boolean loadCache_ISW_IFD_BC = false;
    private boolean isLoadCacheForPlanningRulesDone = false;
    private boolean isLoadCacheForPPT = false;
    private boolean loadCacheForFlowProjectList = false;
    private boolean loadCacheForFlowList = false;

    private void loadCache_ISW() {
        if (loadCache_ISW == false) {
            OslcLoadHint loadHint1 = new OslcLoadHint(false);
            loadHint1.followField(Rq1Pst.ATTRIBUTE_HAS_MAPPED_ISSUES, true);
            loadIntoCache(loadHint1);
            loadCache_ISW = true;
        }
    }

    private void loadCache_ISW_IFD_BC() {
        if (loadCache_ISW_IFD_BC == false) {
            loadCache_ISW();
            OslcLoadHint loadHint2 = new OslcLoadHint(false);
            loadHint2.followField(Rq1Pst.ATTRIBUTE_HAS_MAPPED_ISSUES, false).followField(Rq1IssueSW.FIELDNAME_HAS_CHILDREN, true);
            loadIntoCache(loadHint2);
            OslcLoadHint loadHint3 = new OslcLoadHint(false);
            loadHint3.followField(Rq1Pst.ATTRIBUTE_HAS_MAPPED_ISSUES, false).followField(Rq1IssueSW.FIELDNAME_HAS_CHILDREN, false).followField(Rq1IssueFD.ATTRIBUTE_HAS_MAPPED_RELEASES, true);
            loadIntoCache(loadHint3);
            loadCache_ISW_IFD_BC = true;
        }
    }

    private void loadCache_ISW_WI() {
        if (loadCache_ISW_WI == false) {
            loadCache_ISW();
            OslcLoadHint loadHint1 = new OslcLoadHint(false);
            loadHint1.followField(Rq1Pst.ATTRIBUTE_HAS_MAPPED_ISSUES, false).followField(Rq1IssueSW.ATTRIBUTE_HAS_WORKITEMS, true);
            loadIntoCache(loadHint1);
            loadCache_ISW_WI = true;
        }
    }

    public void loadCacheForPlanningRules() {

        if (isLoadCacheForPlanningRulesDone == false) {

            //
            // Load I-SW -> I-FD -> BC
            //
            loadCache_ISW_IFD_BC();

            //
            // Load BC -> I-FD
            //
            OslcLoadHint loadHintForBc = new OslcLoadHint(true);
            loadHintForBc.followField(Rq1Bc.ATTRIBUTE_HAS_MAPPED_ISSUES, true); // I-FD
            loadHintForBc.followField(Rq1Bc.ATTRIBUTE_HAS_PREDECESSOR, true); // Predecessor BC

            OslcLoadHint loadHintForPst = new OslcLoadHint(false);
            loadHintForPst.followField(Rq1Pst.FIELDNAME_HAS_MAPPED_CHILDREN, loadHintForBc); // BC

            loadIntoCache(loadHintForPst);

            isLoadCacheForPlanningRulesDone = true;
        }
    }

    public void loadCacheForPPT() {
        if (isLoadCacheForPPT == false) {

            //
            // Load BC -> predecessor BC
            //
            OslcLoadHint loadHintForPPTReleaseBC = new OslcLoadHint(false);
            loadHintForPPTReleaseBC.followField(Rq1Pst.FIELDNAME_HAS_MAPPED_CHILDREN, true)
                    .followField(Rq1Bc.ATTRIBUTE_HAS_PREDECESSOR, true);
            loadIntoCache(loadHintForPPTReleaseBC);

            //
            // Load BC -> FC
            //
            OslcLoadHint loadHintForPPTReleaseBCandFC = new OslcLoadHint(false);
            loadHintForPPTReleaseBCandFC.followField(Rq1Pst.FIELDNAME_HAS_MAPPED_CHILDREN, false)
                    .followField(Rq1Bc.FIELDNAME_HAS_MAPPED_CHILDREN, true);
            loadIntoCache(loadHintForPPTReleaseBCandFC);

            //
            // Load BC -> I-FD -> I-SW
            //
            OslcLoadHint loadHintForPPTReleaseBCandIssues = new OslcLoadHint(false);
            loadHintForPPTReleaseBCandIssues.followField(Rq1Pst.FIELDNAME_HAS_MAPPED_CHILDREN, false)
                    .followField(Rq1Bc.ATTRIBUTE_HAS_MAPPED_ISSUES, true)
                    .followField(Rq1IssueFD.ATTRIBUTE_HAS_PARENT, true);
            loadIntoCache(loadHintForPPTReleaseBCandIssues);

            //
            // Load I-SW -> I-FD
            //
            OslcLoadHint loadHintForPPTReleaseIssues = new OslcLoadHint(false);
            loadHintForPPTReleaseIssues.followField(Rq1Pst.ATTRIBUTE_HAS_MAPPED_ISSUES, true)
                    .followField(Rq1IssueSW.FIELDNAME_HAS_CHILDREN, true);
            loadIntoCache(loadHintForPPTReleaseIssues);

            isLoadCacheForPPT = true;
        }
    }

    public void loadCacheForFlowProjectList() {

        if (loadCacheForFlowProjectList == false) {

            logger.warning("loadCacheForFlowProjectList() started");

            loadCache_ISW_IFD_BC();
            loadCache_ISW_WI();

            OslcLoadHint loadHint2 = new OslcLoadHint(false);
            loadHint2.followField(Rq1Pst.FIELDNAME_HAS_MAPPED_CHILDREN, true).followField(Rq1Bc.ATTRIBUTE_HAS_MAPPED_ISSUES, true);
            loadIntoCache(loadHint2);

            loadCacheForFlowProjectList = true;

            logger.warning("loadCacheForFlowProjectList() done");
        }
    }

    public void loadCacheForFlowList(boolean doReload) {
        if (loadCacheForFlowList == false || doReload) {
            OslcLoadHint loadHintIssue = new OslcLoadHint(false);
            loadHintIssue.followField(Rq1Pst.ATTRIBUTE_HAS_MAPPED_ISSUES, true).followField(Rq1Irm.ATTRIBUTE_HAS_MAPPED_ISSUE, true);
            loadIntoCache(loadHintIssue);

            OslcLoadHint loadHintBc = new OslcLoadHint(false);
            loadHintBc.followField(Rq1Pst.FIELDNAME_HAS_MAPPED_CHILDREN, true).followField(Rq1Irm.ATTRIBUTE_HAS_MAPPED_RELEASE, true);
            loadIntoCache(loadHintBc);

            //   for (DmRq1Pst dmRq1Pst : pstList) {
            MAPPED_CHILDREN.getElementList();
            MAPPED_ISSUES.getElementList();
            // }
            // loadCache_ISW_WI();
            loadCacheForFlowList = true;
        }
    }

    //-------------------------------------------------------------------------------------
    //
    // Calculation of final BC/FC
    //
    //-------------------------------------------------------------------------------------
    /**
     * Generic result of the search for a final release.
     *
     * @param <T> Release type DmDgsBcRelease or DmDgsFcRelease.
     */
    public abstract static class FinalRelease<T> {

        final protected String derivative;

        protected FinalRelease(String derivative) {
            assert (derivative != null);

            this.derivative = derivative;
        }

        public String getDerivative() {
            return derivative;
        }

        public abstract List<? extends T> getReleases();

    }

    /**
     * Result for 'not found'. Meaning, no release was found for this
     * derivative.
     *
     * @param <T> Release type
     */
    public static class FinalReleaseNotFound<T> extends FinalRelease<T> {

        public FinalReleaseNotFound(String derivative) {
            super(derivative);
        }

        @Override
        public List<? extends T> getReleases() {
            return (new ArrayList<>());
        }

        @Override
        public String toString() {
            return ("FinalReleaseNotFound-" + derivative);
        }

    }

    /**
     * Result for successful search.
     *
     * @param <T> Release type
     */
    public static class FinalReleaseFound<T> extends FinalRelease<T> {

        final private T release;

        public FinalReleaseFound(String derivative, T release) {
            super(derivative);
            assert (release != null);

            this.release = release;
        }

        public T getRelease() {
            return release;
        }

        @Override
        public List<? extends T> getReleases() {
            ArrayList<T> result = new ArrayList<>();
            result.add(release);
            return (result);
        }

        @Override
        public String toString() {
            return ("FinalReleaseFound-" + derivative + "-" + release);
        }
    }

    /**
     * Ambiguous result. More than one release was found for the derivative.
     *
     * @param <T> Release type
     */
    public static class FinalReleaseAmbiguous<T> extends FinalRelease<T> {

        final private List<? extends T> releases;

        public FinalReleaseAmbiguous(String derivative, List<? extends T> releases) {
            super(derivative);
            assert (derivative.isEmpty() == false);
            assert (releases != null);
            assert (releases.isEmpty() == false);

            this.releases = releases;
        }

        @Override
        public List<? extends T> getReleases() {
            return releases;
        }

        @Override
        public String toString() {
            return ("FinalReleaseAmbiguous-" + derivative + "-" + releases);
        }

    }

    /**
     * Determines the final BC version for the BC name that is planned or
     * implemented in the Pst.
     * <p>
     * Scans the mapped BCs or, if available, the FC/BC-List to determine which
     * BC version is implemented in the Pst.
     * <p>
     * The scanning is restricted to the planning data of this Pst and the
     * FC/BC-Lists attached to this Pst in RQ1.
     *
     * @param bcType
     * @param bcName Name of the BC to look for.
     * @return A map where the key is the derivative name and the value is the
     * final BC for this derivative.<br>
     * null, if no BC where found for the given name.
     */
    public synchronized List<FinalRelease<DmDgsBcReleaseI>> determineFinalBc(String bcType, String bcName) {
//        assert (bcType != null);
//        assert (bcType.isEmpty() == false);
        assert (bcName != null);
        assert (bcName.isEmpty() == false);

        EcvMapList<String, ? extends DmDgsBcReleaseI> bcMap;

        if (rq1PstRelease.hasFcBcList() == true) {
            return (determineFinalBcFromList(bcName));
        } else {
            return (determineFinalBcFromMappedBc(bcType, bcName));
        }
    }

    /**
     * Determines the final FC version for the FC name that is planned or
     * implemented in the Pst.
     * <p>
     * Scans the mapped FCs or, if available, the FC/BC-List to determine which
     * FC version is implemented in the Pst.
     * <p>
     * The scanning is restricted to the planning data of this Pst and the
     * FC/BC-Lists attached to this Pst in RQ1. Additionally, the scanning for
     * the FC is restricted to the FC under the given BC name, if no FC/BC-List
     * is available.
     *
     * @param bcType
     * @param bcName
     * @param fcName
     * @return A map where the key is the derivative name and the value is the
     * final FC for this derivative.<br>
     * null, if no BC where found for the given name.
     */
    public List<FinalRelease<DmDgsFcReleaseI>> determineFinalFc(String bcType, String bcName, String fcName) {
        assert (bcType != null);
        assert (bcType.isEmpty() == false);
        assert (bcName != null);
        assert (bcName.isEmpty() == false);
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        EcvMapList<String, DmRq1Bc> bcMap;

        //
        // Find BCs
        //
        if (rq1PstRelease.hasFcBcList() == true) {
            return (determineFinalFcFromList(fcName));
        } else {
            return (determineFinalFcFromMappedBc(bcType, bcName, fcName));
        }

    }

    /**
     * Determines the final FC version for the FC name that is planned or
     * implemented in the Pst.
     * <p>
     * Scans the FC/BC-List to determine which FC version is implemented in the
     * Pst. If no FC/BC-List is available, a non found result will be returned.
     * <p>
     * The scanning is restricted to the FC/BC-Lists attached to this Pst.
     *
     * @param fcName
     * @return A map where the key is the derivative name and the value is the
     * final FC for this derivative.<br>
     * null, if no BC where found for the given name.
     */
    public List<FinalRelease<DmDgsFcReleaseI>> determineFinalFc(String fcName) {
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        EcvMapList<String, DmRq1Bc> bcMap;

        //
        // Find BCs
        //
        if (rq1PstRelease.hasFcBcList() == true) {
            return (determineFinalFcFromList(fcName));
        } else {
            List<FinalRelease<DmDgsFcReleaseI>> result = new ArrayList<>();
            for (String derivative : getActiveDerivatives()) {
                result.add(new FinalReleaseNotFound<DmDgsFcReleaseI>(derivative));
            }
            return (result);
        }

    }

    //-------------------------------------------------------------------------------------
    //
    // Calculation of final BC/FC from mapped BCs/FCs
    //
    //-------------------------------------------------------------------------------------
    private List<FinalRelease<DmDgsBcReleaseI>> determineFinalBcFromMappedBc(String bcType, String bcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);

        logger.finer("determineFinalBcFromMappedBc: bcName = " + bcName);

        Collection<String> activeDerivativesOnPst = getActiveDerivatives();
        boolean specialHandlingForPstWithoutDerivateSetting = false;
        if (activeDerivativesOnPst.isEmpty() == true) {
            specialHandlingForPstWithoutDerivateSetting = true;
            activeDerivativesOnPst.add(""); // Add empty derivative meaning "all" derivatives
        }

        //
        // Create derivate specific BC lists based on predecessor relationship.
        //
        EcvMapList<String, DmRq1Bc> lastBcMap = new EcvMapList<>(activeDerivativesOnPst);
        EcvMapList<String, DmRq1Bc> processedBcMap = new EcvMapList<>(activeDerivativesOnPst);

        //
        // Loop all BCs on the Pst
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : MAPPED_CHILDREN.getElementList()) {
            DmRq1Bc loopBc = (DmRq1Bc) m.getTarget();

            //
            // Consider only BCs with matching name and good life cycle state
            //
            if (loopBc.getName().equals(bcName) && loopBc.getType().equals(bcType) && (loopBc.isCanceledOrConflicted() == false)) {
                DmRq1Rrm_Pst_Bc loopRrm = (DmRq1Rrm_Pst_Bc) m.getMap();

                //
                // Follow only RRM with good life cycle state and good integration action
                //
                if ((loopRrm.isCanceledOrConflicted() == false) && (loopRrm.INTEGRATION_ACTION.getValue() != IntegrationAction.REMOVE)) {

                    //
                    // Determine derivatives of the RRM
                    //
                    Collection<String> activeDerivativesOnRrm;
                    if (areDerivativesRelevant() == true) {
                        activeDerivativesOnRrm = loopRrm.SELECTION_OF_DERIVATIVES.getActiveDerivatives();
                        if ((specialHandlingForPstWithoutDerivateSetting == true) && (activeDerivativesOnRrm.isEmpty() == true)) {
                            activeDerivativesOnRrm = activeDerivativesOnPst;
                        }
                    } else {
                        activeDerivativesOnRrm = activeDerivativesOnPst;
                    }

                    //
                    // Update BC list for each derivative
                    //
                    for (String derivative : activeDerivativesOnRrm) {
                        DmRq1Bc predecessorBc = (DmRq1Bc) loopBc.PREDECESSOR.getElement();
                        //
                        // Remove predecessor from lastBcMap, because the predecessor cannot be the last BC in the line.
                        //
                        if (predecessorBc != null) {
                            lastBcMap.removeValue(derivative, predecessorBc);
                        }
                        //
                        // Add BC to lastBcMap, if no other BC is a successor
                        //
                        boolean loopBcHasSuccessor = false;
                        processedBcMap.add(derivative); // Necessary for derivatives in RRM but not in PVAR.
                        for (DmRq1Bc possibleSuccessor : processedBcMap.get(derivative)) {
                            if (possibleSuccessor.PREDECESSOR.getElement() == loopBc) {
                                loopBcHasSuccessor = true;
                                break;
                            }
                        }
                        if (loopBcHasSuccessor == false) {
                            lastBcMap.add(derivative, loopBc);
                        }
                        processedBcMap.add(derivative, loopBc);
                    }

                }
            }
        }

        //
        // Create result
        //
        List<FinalRelease<DmDgsBcReleaseI>> result = new ArrayList<>();
        for (String derivative : lastBcMap.keySet()) {
            List<DmRq1Bc> foundBc = lastBcMap.get(derivative);
            if (foundBc.isEmpty()) {
                result.add(new FinalReleaseNotFound<DmDgsBcReleaseI>(derivative));
            } else if (foundBc.size() == 1) {
                result.add(new FinalReleaseFound<DmDgsBcReleaseI>(derivative, foundBc.get(0)));
            } else {
                result.add(new FinalReleaseAmbiguous<DmDgsBcReleaseI>(derivative, foundBc));
            }
        }
        return (result);
    }

    private List<FinalRelease<DmDgsFcReleaseI>> determineFinalFcFromMappedBc(String bcType, String bcName, String fcName) {
        assert (bcType != null);
        assert (bcType.isEmpty() == false);
        assert (bcName != null);
        assert (bcName.isEmpty() == false);
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        logger.finer("determineFinalFcFromMappedBc: bcName = " + bcName + ", fcName = " + fcName);

        //
        // Find possible final BCs
        //
        List<FinalRelease<DmDgsBcReleaseI>> finalBcList = determineFinalBcFromMappedBc(bcType, bcName);

        //
        // Extract FCs from BCs
        //
        List<FinalRelease<DmDgsFcReleaseI>> result = new ArrayList<>();

        for (FinalRelease<DmDgsBcReleaseI> finalBc : finalBcList) {
            if (finalBc instanceof FinalReleaseNotFound) {

                result.add(new FinalReleaseNotFound<DmDgsFcReleaseI>(finalBc.getDerivative()));

            } else if (finalBc instanceof FinalReleaseFound) {

                FinalReleaseFound<DmDgsBcReleaseI> finalBcFound = (FinalReleaseFound<DmDgsBcReleaseI>) finalBc;
                Set<DmRq1Fc> fcSet = determineFinalFcFromBc((DmRq1Bc) finalBcFound.getRelease(), fcName);
                addFc(finalBcFound.getDerivative(), fcSet, result, false);

            } else if (finalBc instanceof FinalReleaseAmbiguous) {

                FinalReleaseAmbiguous<DmDgsBcReleaseI> finalBcAmbiguous = (FinalReleaseAmbiguous<DmDgsBcReleaseI>) finalBc;
                Set<DmRq1Fc> fcSet = new TreeSet<>();
                for (DmDgsBcReleaseI bc : finalBcAmbiguous.getReleases()) {
                    fcSet.addAll(determineFinalFcFromBc((DmRq1Bc) bc, fcName));
                }
                addFc(finalBcAmbiguous.getDerivative(), fcSet, result, true);

            } else {
                throw (new Error("Unknown FinalRelease class: " + finalBc.getClass().toString()));
            }
        }

        return (result);
    }

    private Set<DmRq1Fc> determineFinalFcFromBc(DmRq1Bc bcRelease, String fcName) {
        assert (bcRelease != null);
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        Set<DmRq1Fc> lastFcSet = new TreeSet<>();
        Set<DmRq1Fc> processedFcSet = new TreeSet<>();

        for (DmMappedElement<DmRq1Rrm, DmRq1Release> map : bcRelease.MAPPED_CHILDREN.getElementList()) {
            DmRq1Rrm_Bc_Fc rrm = (DmRq1Rrm_Bc_Fc) map.getMap();
            if ((rrm.isCanceledOrConflicted() == false) && (rrm.INTEGRATION_ACTION.getValue() != IntegrationAction.REMOVE)) {

                DmRq1Fc loopFc = (DmRq1Fc) map.getTarget();
                if ((loopFc.isCanceledOrConflicted() == false) && (loopFc.getName().equals(fcName))) {
                    DmRq1Fc predecessorFc = (DmRq1Fc) loopFc.PREDECESSOR.getElement();
                    //
                    // Remove predecessor from lastFcSet, because the predecessor cannot be the last FC in the line.
                    //
                    if (predecessorFc != null) {
                        lastFcSet.remove(predecessorFc);
                    }
                    //
                    // Add FC to lastBcMap, if no other FC is a successor
                    //
                    boolean loopFcHasSuccessor = false;
                    for (DmRq1Fc possibleSuccessor : processedFcSet) {
                        if (possibleSuccessor.PREDECESSOR.getElement() == loopFc) {
                            loopFcHasSuccessor = true;
                            break;
                        }
                    }
                    if (loopFcHasSuccessor == false) {
                        lastFcSet.add(loopFc);
                    }
                    processedFcSet.add(loopFc);
                }
            }
        }

        return (lastFcSet);
    }

    private void addFc(String derivative, Set<DmRq1Fc> fcSet, List<FinalRelease<DmDgsFcReleaseI>> result, boolean isAmbiguous) {
        assert (derivative != null);
        assert (fcSet != null);
        assert (result != null);

        if (fcSet.isEmpty() == true) {

            result.add(new FinalReleaseNotFound<DmDgsFcReleaseI>(derivative));

        } else if ((fcSet.size() > 1) || isAmbiguous) {

            result.add(new FinalReleaseAmbiguous<DmDgsFcReleaseI>(derivative, new ArrayList<>(fcSet)));
        } else {

            result.add(new FinalReleaseFound<DmDgsFcReleaseI>(derivative, fcSet.iterator().next()));
        }

    }

    //-------------------------------------------------------------------------------------
    //
    // Calculation of final BC/FC from FC-BC-List
    //
    //-------------------------------------------------------------------------------------
    /**
     * Checks whether or not FC-BC-Lists are attached to this PST.
     * <br>
     * Note that for PVAR, this method returns true if at least one FC-BC-List
     * is available. Even if the FC-BC-List for some derivatives is missing.
     *
     * @return true, if at least one FC-BC-List is available.
     */
    public boolean isFcBcListAvailable() {
        return (rq1PstRelease.hasFcBcList());
    }

    private List<FinalRelease<DmDgsBcReleaseI>> determineFinalBcFromList(String bcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);

        logger.finer("determineFinalBcFromList: bcName = " + bcName);

        List<FinalRelease<DmDgsBcReleaseI>> result = new ArrayList<>();

        Set<String> derivatives = new TreeSet<>(getActiveDerivatives());

        EcvMapList<String, DmDgsBcReleaseI> bcMap = rq1PstRelease.getBcFromFcBcList(bcName);
        if (bcMap != null) {
            derivatives.addAll(bcMap.keySet());
            for (String derivative : derivatives) {
                List<DmDgsBcReleaseI> foundBc = bcMap.get(derivative);
                if ((foundBc == null) || (foundBc.isEmpty())) {
                    result.add(new FinalReleaseNotFound<DmDgsBcReleaseI>(derivative));
                } else if (foundBc.size() == 1) {
                    result.add(new FinalReleaseFound<DmDgsBcReleaseI>(derivative, foundBc.get(0)));
                } else {
                    result.add(new FinalReleaseAmbiguous<DmDgsBcReleaseI>(derivative, foundBc));
                }
            }
        } else {
            for (String derivative : derivatives) {
                result.add(new FinalReleaseNotFound<DmDgsBcReleaseI>(derivative));
            }
        }

        if (result.isEmpty() == true) {
            result.add(new FinalReleaseNotFound<DmDgsBcReleaseI>(" "));
        }

        return (result);
    }

    public List<FinalRelease<DmDgsFcReleaseI>> determineFinalFcFromList(String fcName) {
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        logger.finer("determineFinalFcFromList: fcName = " + fcName);

        List<FinalRelease<DmDgsFcReleaseI>> result = new ArrayList<>();

        Set<String> derivatives = new TreeSet<>(getActiveDerivatives());

        EcvMapList<String, DmDgsFcReleaseI> fcMap = rq1PstRelease.getFcFromFcBcList(fcName);

        if (fcMap != null) {
            derivatives.addAll(fcMap.keySet());
            for (String derivative : derivatives) {
                List<? extends DmDgsFcReleaseI> foundFc = fcMap.get(derivative);
                if ((foundFc == null) || (foundFc.isEmpty())) {
                    result.add(new FinalReleaseNotFound<DmDgsFcReleaseI>(derivative));
                } else if (foundFc.size() == 1) {
                    result.add(new FinalReleaseFound<DmDgsFcReleaseI>(derivative, foundFc.get(0)));
                } else {
                    result.add(new FinalReleaseAmbiguous<DmDgsFcReleaseI>(derivative, foundFc));
                }
            }
        } else {
            for (String derivative : derivatives) {
                result.add(new FinalReleaseNotFound<DmDgsFcReleaseI>(derivative));
            }
        }

        return (result);
    }

    //-------------------------------------------------------------------------------------
    //
    // Calculation of inactive FC
    //
    //-------------------------------------------------------------------------------------
    /**
     * Describes the activation state of a FC from the PST point of view.
     */
    public static enum FcActivationState {

        ACTIVATED_BY_CTP,
        DEACTIVATED_BY_CTP,
        ACTIVE,
        INACTIVE,
        NOT_DEFINED;

    }

    /**
     * Determines the activation state of a FC from the PST point of view.
     *
     * @param bcRelease BC release that contains the FC.
     * @param fcRelease FC release for which the state is wanted.
     * @return Activation state of the FC.
     */
    public FcActivationState isFcActive(DmRq1Bc bcRelease, DmRq1Fc fcRelease) {
        assert (bcRelease != null);
        assert (fcRelease != null);

        String bcName = bcRelease.getName();
        String fcName = fcRelease.getName();
        String fcVersion = fcRelease.getVersion();

        //
        // Check if state is set in CTP.
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> mappedBc : this.MAPPED_CHILDREN.getElementList()) {
            if (mappedBc.getTarget() == bcRelease) {
                EcvTableData ctp = mappedBc.getMap().CHANGES_TO_PARTLIST.getValue();
                Rq1XmlTable_ChangesToPartlist.FcActive state = Rq1XmlTable_ChangesToPartlist.getFcStatusForIpe(ctp, fcName, fcVersion);
                switch (state) {
                    case ACTIVE:
                        return (FcActivationState.ACTIVATED_BY_CTP);
                    case DEACTIVE:
                        return (FcActivationState.DEACTIVATED_BY_CTP);
                    case NOTDEFINED:
                        break;
                    default:
                        throw (new Error("Unexpected FC state: " + state.toString()));
                }
            }
        }

        //
        // Check if state can be determined via FC/BC-List of predecessor
        //
        Rq1Reference refPredecessor = rq1PstRelease.HAS_PREDECESSOR.getDataModelValue();
        if (refPredecessor != null) {
            if (refPredecessor.getRecord() instanceof Rq1Pst) {
                Rq1Pst rq1Predecessor = (Rq1Pst) refPredecessor.getRecord();
                if (rq1Predecessor.hasFcBcList() == true) {
                    EcvMapList<String, DmDgsFcReleaseI> fcMap = rq1Predecessor.getFcFromFcBcList(bcName, fcName);
                    if (fcMap != null) {
                        for (List<DmDgsFcReleaseI> fcList : fcMap.values()) {
                            for (DmDgsFcReleaseI fcInPredecessor : fcList) {
                                if (fcRelease.getName().equals(fcInPredecessor.getName()) && fcRelease.getVariant().equals(fcInPredecessor.getVariant())) {
                                    return (FcActivationState.ACTIVE);
                                }
                            }
                        }
                    }
                    return (FcActivationState.INACTIVE);
                }
            }
        }

        return (FcActivationState.NOT_DEFINED);
    }

    //-------------------------------------------------------------------------------------
    //
    // Determination of intermediate BCs
    //
    //-------------------------------------------------------------------------------------
    public static class IntermediateBcResult {

        public enum State {

            /**
             * PST has no predecessor. Nothing set.
             */
            NO_PST_PREDECESSOR,
            /**
             * The BC has no predecessor. Nothing set.
             */
            NO_BC_PREDECESSOR,
            /**
             * BC in predecessor PST found and no intermediate BC is missing.
             * Predecessor BC is set.
             */
            NO_BC_MISSING,
            /**
             * BC in predecessor PST found and list of missing intermediate BC
             * created.
             */
            INTERMEDIATE_BC_MISSING,
            /**
             * No predecessor BC was found in the checked predecessor PST.
             * Nothing set.
             */
            BC_NOT_FOUND;
        }

        final private State state;
        final private DmRq1Bc predecessorBc;
        final private List<DmRq1Bc> missingBc;

        /**
         * INTERMEDIATE_BC_MISSING
         *
         * @param predecessorBc
         * @param missingBc
         */
        public IntermediateBcResult(DmRq1Bc predecessorBc, List<DmRq1Bc> missingBc) {
            assert (predecessorBc != null);
            assert (missingBc != null);
            assert (missingBc.isEmpty() == false);

            this.state = State.INTERMEDIATE_BC_MISSING;
            this.predecessorBc = predecessorBc;
            this.missingBc = missingBc;
        }

        /**
         * NO_BC_MISSING
         *
         * @param predecessorBc
         */
        public IntermediateBcResult(DmRq1Bc predecessorBc) {
            assert (predecessorBc != null);

            this.state = State.NO_BC_MISSING;
            this.predecessorBc = predecessorBc;
            this.missingBc = null;
        }

        public IntermediateBcResult(State result) {
            assert (result != null);

            this.state = result;
            this.predecessorBc = null;
            this.missingBc = null;
        }

        /**
         * Returns the result of the search.
         *
         * @return
         */
        public State getResult() {
            return state;
        }

        /**
         *
         * @return Predecessor BC or null.
         */
        public DmRq1Bc getPredecessorBc() {
            return predecessorBc;
        }

        /**
         *
         * @return List of missing BCs or null.
         */
        public List<DmRq1Bc> getMissingBc() {
            return missingBc;
        }

    }

    /**
     * Determines the intermediate BCs that are missing for a complete planning
     * for the given BC.
     *
     * @param thisBc BC for which the intermediate BCs shall be found.
     * @param maxNumberOfBcToVisit maximum number of predecessor BCs.
     * @return
     *
     */
    public IntermediateBcResult checkIntermediateBc(DmRq1Bc thisBc, int maxNumberOfBcToVisit) {
        assert (thisBc != null);
        assert (maxNumberOfBcToVisit >= 0);
        //
        // Check if PST has predecessor
        //
        if (this.PREDECESSOR.getElement() instanceof DmRq1Pst == false) {
            return (new IntermediateBcResult(IntermediateBcResult.State.NO_PST_PREDECESSOR));
        }

        //
        // Check if BC has predecessor
        //
        if (thisBc.PREDECESSOR.getElement() instanceof DmRq1Bc == false) {
            return (new IntermediateBcResult(IntermediateBcResult.State.NO_BC_PREDECESSOR));
        }

        //
        // Loop over predecessor BCs
        //
        DmRq1Pst predecessorPst = (DmRq1Pst) this.PREDECESSOR.getElement();
        List<DmRq1Bc> missingBc = new ArrayList<>();
        DmRq1Bc loopBc = thisBc;
        for (int numberOfBcVisited = 0; numberOfBcVisited <= maxNumberOfBcToVisit; numberOfBcVisited++) {

            //
            // End loop, if BC is mapped to predecessor
            //
            if (predecessorPst.isBcMapped(loopBc) == true) {
                if (missingBc.isEmpty()) {
                    return (new IntermediateBcResult(loopBc));
                } else {
                    return (new IntermediateBcResult(loopBc, missingBc));
                }
            }

            //
            // Add loop BC to missing BC, if it is not mapped to the own PST
            // 
            if (this.MAPPED_CHILDREN.findElement(loopBc) == null) {
                missingBc.add(loopBc);
            }

            //
            // Get next BC or leave BC loop
            //
            if (loopBc.PREDECESSOR.getElement() instanceof DmRq1Bc == false) {
                break;
            }
            loopBc = (DmRq1Bc) loopBc.PREDECESSOR.getElement();
        }

        //
        // BC was not found in predecessor PST within the number of BC to visit.
        //
        return (new IntermediateBcResult(IntermediateBcResult.State.BC_NOT_FOUND));
    }

    private boolean isBcMapped(DmRq1Bc bc) {
        assert (bc != null);
        if (rq1PstRelease.hasFcBcList() == true) {
            EcvMapList<String, DmDgsBcReleaseI> bcMapList = rq1PstRelease.getBcFromFcBcList(bc.getName());
            if (bcMapList == null) {
                return (false);
            }
            return (bcMapList.getKey(bc) != null);
        } else {
            return (MAPPED_CHILDREN.findElement(bc) != null);
        }

    }

    //-------------------------------------------------------------------------------------
    //
    // Support for ProPlaTo
    //
    //-------------------------------------------------------------------------------------
    public List<Rq1LineEcuProject> getPstLinesOfProject() {
        if (PROJECT.getElement() instanceof DmRq1SwCustomerProject_Leaf) {
            return (((DmRq1SwCustomerProject_Leaf) PROJECT.getElement()).getPstLines());
        } else {
            return (new ArrayList<>());
        }
    }

    /**
     * Returns all cluster names from the ProPlaTo data in PST and project.
     *
     * @return
     */
    public abstract Set<String> getAll_ProPlaTo_Cluster();

    /**
     * Returns all Schienen from the ProPlaTo data in PST and project.
     *
     * @return
     */
    public abstract Set<String> getAll_ProPlaTo_Schiene();

    /**
     * Returns all hardware names set in the ProPlaTo data of this PST.
     *
     * @return
     */
    public abstract Set<String> getAll_ProPlaTo_Hardware();

    public abstract Map<String, String> getHardwareInformation();

    //Derivatives in PVAR are adaptet to Line ...AXXXX is changed to ...PXXXX
    //The A can be A,B,C,D,...
    public abstract Map<String, String> getHardwareInformationProPlaTo();

    //Get the ProPlaTo Predecessor
    //It searches the predecessor for the given line
    //that means the predecessor has to be on the line and lcs != canceled
    //It is limited by a set, which checks, if we have already been at a specific point
    //It checks if the algorithm is in a circle.
    //if there is no predecessor in RQ1 the algorithm also stops
    public DmRq1Pst getProPlaToPredecessor(String line) {
        return this.getProPlaToPredecIntern(line, new HashSet<DmRq1Release>());
    }

    private DmRq1Pst getProPlaToPredecIntern(String line, Set<DmRq1Release> releases) {
        DmRq1Release predecessor = this.PREDECESSOR.getElement();
        //If the predecessor is an PST Release and we are not too far in the hierarchy check the predecessor
        if (predecessor != null && predecessor instanceof DmRq1Pst
                && !releases.contains(predecessor)) {
            DmRq1Pst pstPredecessor = (DmRq1Pst) predecessor;
            if (pstPredecessor.SCOPE.getValue().equals(Scope.EXTERNAL) && pstPredecessor.LIFE_CYCLE_STATE.getValue() != LifeCycleState_Release.CANCELED
                    && pstPredecessor.isInProPlaToLine(line)) {
                return pstPredecessor;
            } else {
                releases.add(predecessor);
                return pstPredecessor.getProPlaToPredecIntern(line, releases);
            }
        }
        return null;
    }

    //True, if the release is a PVAR
    //False, if the release is a PVER
    public abstract boolean areDerivativesRelevant();

    //True, if the release is active in the given ProPlaTo Line
    //False, if the release is not active in the given ProPlaTo Line
    public abstract boolean isInProPlaToLine(String line);

    //True, PFAM(if the DerivativeLine is Pilot) PVER always True
    //False, PFAM(if the DerivativeLine is not Pilot
    public abstract boolean getProPlaToKennungMeta(String line);

    //get the MetaSchiene
    //PVER the External ID
    //PFAM the Derivative which is Pilot
    public abstract String getMetaSchiene();

    public abstract EcvDate getPlannedDateForPstLineByExternalName(String line);

    public DmPPTRelease getPPT_Release(DmPPTLine PPT_Line) {
        if (PPT_Line_Release.containsKey(PPT_Line)) {
            return PPT_Line_Release.get(PPT_Line);
        } else {
            return null;
        }
    }

    public void setPPT_Release(DmPPTLine PPT_Line, DmPPTRelease PPT_Release) {
        if (!PPT_Line_Release.containsKey(PPT_Line)) {
            PPT_Line_Release.put(PPT_Line, PPT_Release);
        }
    }

    //-------------------------------------------------------------------------------------
    //
    // Support for IPE-Flow
    //
    //-------------------------------------------------------------------------------------
    @Override
    public String getRank() {
        return (FLOW_RANK.getValueAsText());
    }

    @Override
    public String getFlowVersion() {
        return (FLOW_VERSION.getValueAsText());
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
        return (FLOW_CLUSTERNAME.getValueAsText());
    }

    @Override
    public String getClusterID() {
        return (FLOW_CLUSTERID.getValueAsText());
    }

    @Override
    public FullKitSize getSize() {
        return ((FullKitSize) FLOW_SIZE.getValue());
    }

    @Override
    public String getRemainingEffort() {
        return (FLOW_R_EFFORT.getValueAsText());
    }

    public String getISW_IRM_Task() {
        return (FLOW_ISW_IRM_TASK.getValueAsText());
    }

    @Override
    public String getRequestedDate() {
        return (FLOW_R_DATE.getValueAsText());
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
    public EcvDate getToRedDate() {
        return FLOW_TO_RED_DATE.getValue();
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

    @Override
    public void reload() {
        internalRank = null;
        super.reload();
    }

    @Override
    public EcvDate getTargetDate() {
        return FLOW_TARGET_DATE.getValue();
    }

    @Override
    public String getNoDevelopers() {
        return FLOW_NO_OF_DEVELOPERS.getValueAsText();
    }

    public String getIRMGroupStatus() {
        return FLOW_IRM_GROUP.getValueAsText();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DmRq1Pst cloned = (DmRq1Pst) super.clone();
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
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DmRq1IssueFD.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueFD.class.getName(), ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DmRq1IssueFD.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueFD.class.getName(), ex);
        }

        return cloned;
    }

    /**
     * Calculates the total estimated effort. This contains:
     *
     * - Estimated Effort of I-SW.
     *
     * - Estimated Effort of all workitems on the PVER/PVAR.
     *
     * @return The Total Estimated Effort as integer.
     */
    public int calculateTotalEstimatedEffort() {
        int totalEffort = 0;
        //totalEffort += parseIntNoException(this.ESTIMATED_EFFORT.getValueAsText());
        for (DmRq1WorkItem pverpvar_wi : this.WORKITEMS.getElementList()) {
            if (pverpvar_wi.isCanceled() == false) {
                totalEffort += parseIntNoException(pverpvar_wi.EFFORT_ESTIMATION.getValueAsText());
            }
        }
        return (totalEffort);
    }

    /*
     * Parses a number stored as a string to an integer.
     * @param value, the string to be parsed to an integer.
     * @return Integer
     */
    static int parseIntNoException(String value) {
        int parI = 0;
        try {
            parI = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            parI = 0;
        }
        return (parI);
    }

}
