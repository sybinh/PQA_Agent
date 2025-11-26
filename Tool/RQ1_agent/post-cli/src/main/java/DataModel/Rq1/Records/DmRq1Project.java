/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigurableRule_ProjectManager;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_DNG;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_DOORS;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_FMEA_DB;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_HIS;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_Provisor2;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_RQ1;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_SMARTweb;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_TrackAndRelease;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_ILM_AT_Sharepoint;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_NT_Share;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_iGPM_oneQ;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_EnterpriseDashboard;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_SVN;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_SuperOPL;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_iCDM;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_iGPM_MCR_System_Parent_Project;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_iGPM_MCR_ECU;
import DataModel.CPC.Fields.DmRq1Field_CpcRepositoryTable_iGPM_QUO;
import DataModel.DmFieldI;
import DataModel.DmToDsValueField;
import DataModel.GPM.ConfigData.DmGpmConfigManager_Project;
import DataModel.GPM.DmGpmFieldOnProject_DataOfExternalMilestones;
import DataModel.GPM.DmGpmFieldOnProject_TimeScheduleOfProject;
import DataModel.GPM.DmGpmFieldOnProject_UserRoles;
import DataModel.GPM.DmGpmFieldOnProject_WorkitemsOnMilestones;
import DataModel.GPM.OrgData.DmGpmField_OrgData;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_EnumerationSet;
import DataModel.Rq1.Fields.DmRq1Field_MilestoneTable;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Records.DmRq1UserRole.Role;
import DataModel.Rq1.Fields.DmRq1Field_ActiveProjectMembers;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.DmMappedElement;
import Monitoring.RuleExecutionGroup;
import OslcAccess.OslcLoadHint;
import Qmm.BbmMetricsAPI;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Enumeration;
import Rq1Cache.Records.Rq1Project;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Records.Rq1Release;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1XmlTable_AccessGrantConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ClusterConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ContractedTeam;
import Rq1Cache.Types.Rq1XmlTable_CrpCombinedProjects;
import Rq1Cache.Types.Rq1XmlTable_CrpSubTeamConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ExcItedDepartmentMap;
import Rq1Cache.Types.Rq1XmlTable_RelativeProjectConfiguration;
import Rq1Data.Enumerations.AspiceScope;
import Rq1Data.Enumerations.BbmMetricsInstalled;
import Rq1Data.Enumerations.BbmMetricsNeeded;
import Rq1Data.Enumerations.LifeCycleState_Project;
import Rq1Data.Enumerations.RBDProjectCategory;
import Rq1Data.Templates.Rq1ProjectTemplateDefinition;
import java.util.*;
import util.EcvEnumeration;
import util.MailSendable;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Project extends DmRq1SubjectElement implements MailSendable, Comparable<DmRq1Project> {

    public enum PoolProject {
        VW("RQONE00000009"),
        JLR("RQONE00638801");

        final private String rq1Id;

        private PoolProject(String rq1Id) {
            this.rq1Id = rq1Id;
        }

        public String getRq1Id() {
            return (rq1Id);
        }

        /**
         * Checks, if the given project is the pool project or a project within
         * the pool project.
         *
         * @param project Project that shall be checked.
         * @return true, if the given project is within the pool project or the
         * pool project itself; false otherwise
         */
        public boolean isInPoolProject(DmRq1Project project) {
            if (isPoolProject(project) == true) {
                return (true);
            } else if ((project != null) && (isPoolProject(project.POOL_PROJECT.getElement()) == true)) {
                return (true);
            } else {
                return (false);
            }
        }

        /**
         * Checks, if the given project is the pool project.
         *
         * @param project Project that shall be checked.
         * @return true, if the given project is the pool project; false
         * otherwise
         */
        public boolean isPoolProject(DmRq1Project project) {
            if ((project != null) && (rq1Id.equals(project.getRq1Id()) == true)) {
                return (true);
            } else {
                return (false);
            }
        }

    }

    public enum Customer {
        UNKNOWN(""),
        VW_GROUP("VW Group"),
        TATA_GROUP("Tata Group");

        final private String customerString;

        private Customer(String customerString) {
            this.customerString = customerString;
        }

        @Override
        public String toString() {
            return (customerString);
        }

        static public Customer getCustomer(String customerString) {
            for (Customer value : values()) {
                if (value.customerString.equals(customerString) == true) {
                    return (value);
                }
            }
            return (UNKNOWN);
        }

        static public Customer getCustomer(DmRq1Project project) {
            if (project != null) {
                return (getCustomer(project.CUSTOMER.getValueAsText()));
            }
            return (UNKNOWN);
        }

    }

    protected class DmRq1AspiceScopeField_Enumeration extends DmRq1Field_Enumeration {

        public DmRq1AspiceScopeField_Enumeration(Rq1FieldI_Enumeration rq1Field, String nameForUserInterface) {
            super(rq1Field, nameForUserInterface);
        }

        @Override
        public AspiceScope getValue() {
            if (super.getValue() instanceof AspiceScope) {
                AspiceScope scope = (AspiceScope) super.getValue();
                if (scope == AspiceScope.AMBIGUOUS) {
                    return AspiceScope.NOT_CALCULATED;
                } else {
                    return scope;
                }
            }
            return AspiceScope.NOT_CALCULATED;
        }

        @Override
        public void setValue(EcvEnumeration v) {
            if (v instanceof AspiceScope) {
                AspiceScope scope = (AspiceScope) v;
                if (scope != AspiceScope.AMBIGUOUS) {
                    super.setValue(scope);
                }
            }
        }
    }

    public class DmRq1BbmMetricsNeededField_Enumeration extends DmRq1Field_Enumeration {

        public DmRq1BbmMetricsNeededField_Enumeration(Rq1FieldI_Enumeration rq1Field, String nameForUserInterface) {
            super(rq1Field, nameForUserInterface);
        }

        @Override
        public BbmMetricsNeeded getValue() {
            if (super.getValue() instanceof BbmMetricsNeeded) {
                BbmMetricsNeeded scope = (BbmMetricsNeeded) super.getValue();
                if (scope == BbmMetricsNeeded.AMBIGUOUS) {
                    return BbmMetricsNeeded.EMPTY;
                } else {
                    return scope;
                }
            }
            return BbmMetricsNeeded.EMPTY;
        }

        @Override
        public void setValue(EcvEnumeration v) {
            if (v instanceof BbmMetricsNeeded) {
                BbmMetricsNeeded scope = (BbmMetricsNeeded) v;
                if (scope != BbmMetricsNeeded.AMBIGUOUS) {
                    super.setValue(scope);
                }
            }
        }
    }

    protected class DmRq1RbdProjectCategoryField_Enumeration extends DmRq1Field_Enumeration {

        public DmRq1RbdProjectCategoryField_Enumeration(Rq1FieldI_Enumeration rq1Field, String nameForUserInterface) {
            super(rq1Field, nameForUserInterface);
        }

        @Override
        public RBDProjectCategory getValue() {
            if (super.getValue() instanceof RBDProjectCategory) {
                RBDProjectCategory category = (RBDProjectCategory) super.getValue();
                if (category == RBDProjectCategory.AMBIGUOUS) {
                    return RBDProjectCategory.NOT_CALCULATED;
                } else {
                    return category;
                }
            }
            return RBDProjectCategory.NOT_CALCULATED;
        }

        @Override
        public void setValue(EcvEnumeration v) {
            if (v instanceof RBDProjectCategory) {
                RBDProjectCategory category = (RBDProjectCategory) v;
                if (category != RBDProjectCategory.AMBIGUOUS) {
                    super.setValue(category);
                }
            }
        }
    }

    final public DmRq1Field_Text CUSTOMER;
    final public DmRq1Field_Enumeration DEFAULT_DEVELOPMENT_METHOD;
    final public DmRq1Field_Text DEFAULT_DEVELOPMENT_METHOD_COMMENT;
    final public DmRq1Field_Enumeration DEFAULT_PLANNING_METHOD;
    final public DmRq1Field_Enumeration LIFE_CYCLE_STATE;
    final public DmRq1Field_Text TEMPLATES;
    final public DmRq1Field_MilestoneTable MILESTONES_TABLE;
    final public DmGpmFieldOnProject_TimeScheduleOfProject TIME_SCHEDULE_OF_PROJECT;
    final public DmGpmFieldOnProject_WorkitemsOnMilestones WORKITEMS_ON_MILESTONES;
    final public DmRq1Field_Text GENERATION;
    final public DmRq1Field_EnumerationSet GENERATIONS;
    final public DmRq1Field_Text CLASSIFICATION;
    final public DmRq1Field_Enumeration SCOPE;

    final public DmRq1AspiceScopeField_Enumeration ASPICESCOPE;
    final public DmRq1RbdProjectCategoryField_Enumeration RBD_PROJECT_CATEGORY;
    final public DmRq1Field_Date ASPICE_CALC_DATE;
    final public DmRq1BbmMetricsNeededField_Enumeration BBM_METRICS_NEEDED;
    final public DmRq1Field_Enumeration BBM_METRICS_INSTALLED;
    final public DmRq1Field_Date BBM_METRICS_PLANNED_DATE;
    final public DmRq1Field_Text BBM_METRICS_COMMENT;
    final public DmRq1Field_Enumeration BBM_METRICS_NEEDED_MANUAL_OVERRIDE;
    final public DmRq1Field_Enumeration BBM_METRICS_INSTALLED_MANUAL_OVERRIDE;

    //
    final public DmRq1Field_Text EXCITEDCPC_TEAM;
    final public DmRq1Field_Text EXCITEDCPC_GROUP;
    final public DmRq1Field_Table<Rq1XmlTable_ExcItedDepartmentMap> EXCITEDCPC_DEPARTMENTMAP;

    //
    final public DmRq1Field_Text RESPONSIBLE_AT_BOSCH;
    final public DmRq1Field_Reference<DmRq1User> PROJECT_LEADER;
    final public DmRq1Field_Text PROJECT_LEADER_FULLNAME;
    final public DmRq1Field_Text PROJECT_LEADER_EMAIL;
    final public DmRq1Field_Text PROJECT_LEADER_LOGIN_NAME;
    final public DmRq1Field_ActiveProjectMembers HAS_PROJECT_MEMBERS;

    final public DmToDsValueField<Rq1RecordInterface, Set<String>> PRODUCTS;
    final public DmRq1Field_Enumeration COMPANY_DATA_ID;

    final public DmRq1Field_ReferenceList<DmRq1ReleaseRecord> ALL_RELEASES;
    final public DmRq1Field_ReferenceList<DmRq1ReleaseRecord> OPEN_RELEASES;

    final public DmRq1Field_ReferenceList<DmRq1Milestone> ALL_MILESTONES;
    final public DmRq1Field_ReferenceList<DmRq1Milestone> OPEN_MILESTONES;

    final public DmRq1Field_ReferenceList<DmRq1EcuRelease> ALL_HW_ECU_RELEASES;
    final public DmRq1Field_ReferenceList<DmRq1ModRelease> ALL_HW_MOD_RELEASES;
    final public DmRq1Field_ReferenceList<DmRq1CompRelease> ALL_HW_COMP_RELEASES;
    final public DmRq1Field_ReferenceList<DmRq1BundleRelease> ALL_BUNDLES;
    final public DmRq1Field_ReferenceList<DmRq1EcuRelease> OPEN_HW_ECU_RELEASES;
    final public DmRq1Field_ReferenceList<DmRq1ModRelease> OPEN_HW_MOD_RELEASES;
    final public DmRq1Field_ReferenceList<DmRq1CompRelease> OPEN_HW_COMP_RELEASES;
    final public DmRq1Field_ReferenceList<DmRq1BundleRelease> OPEN_BUNDLES;

    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> OPEN_ISSUE_ECU;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> OPEN_ISSUE_MOD;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> ALL_ISSUE_ECU;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> ALL_ISSUE_MOD;

    final public DmRq1Field_Reference<DmRq1Project> POOL_PROJECT;

    final public DmRq1Field_ReferenceList<DmRq1Pst> ALL_PST_COLLECTIONS;
    final public DmRq1Field_ReferenceList<DmRq1Pst> OPEN_PST_COLLECTIONS;

    final public DmRq1Field_ReferenceList<DmRq1WorkItem> OPEN_WORKITEMS;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> ALL_WORKITEMS;

    final public DmRq1Field_ReferenceList<DmRq1Problem> OPEN_PROBLEMS;
    final public DmRq1Field_ReferenceList<DmRq1Problem> ALL_PROBLEMS;

    final public DmRq1Field_Text IPE;
    final public DmRq1Field_Text TUL_ACTIVE;

    //
    // Fields for GPM - Guided project management
    //
    final public DmRq1Field_Enumeration IMPACT_CATEGORY;
    final public DmGpmFieldOnProject_UserRoles GPM_USER_ROLES;
    final public DmGpmField_OrgData GPM_ORG_DATA;
    final public DmGpmFieldOnProject_DataOfExternalMilestones GPM_DATA_OF_EXTERNAL_MILESTONES;

    final public DmRq1Field_CpcRepositoryTable_DNG CPC_DNG;
    final public DmRq1Field_CpcRepositoryTable_DOORS CPC_DOORS;
    final public DmRq1Field_CpcRepositoryTable_EnterpriseDashboard CPC_ENTERPRISE_DASHBOARD;
    final public DmRq1Field_CpcRepositoryTable_FMEA_DB CPC_FMEA_DB;
    final public DmRq1Field_CpcRepositoryTable_HIS CPC_HIS;
    final public DmRq1Field_CpcRepositoryTable_iCDM CPC_iCDM;
    final public DmRq1Field_CpcRepositoryTable_iGPM_MCR_ECU CPC_iGPM_MCR_ECU;
    final public DmRq1Field_CpcRepositoryTable_iGPM_MCR_System_Parent_Project CPC_iGPM_MCR_System_Parent_Project;
    final public DmRq1Field_CpcRepositoryTable_iGPM_oneQ CPC_iGPM_ONE_Q;
    final public DmRq1Field_CpcRepositoryTable_iGPM_QUO CPC_iGPM_QUO;
    final public DmRq1Field_CpcRepositoryTable_ILM_AT_Sharepoint CPC_ILM_AT_SHAREPOINT;
    final public DmRq1Field_CpcRepositoryTable_NT_Share CPC_NT_SHARE;
    final public DmRq1Field_CpcRepositoryTable_Provisor2 CPC_PROVISOR2;
    final public DmRq1Field_CpcRepositoryTable_RQ1 CPC_RQ1;
    final public DmRq1Field_CpcRepositoryTable_SMARTweb CPC_SMART_WEB;
    final public DmRq1Field_CpcRepositoryTable_SuperOPL CPC_SUPER_OPL;
    final public DmRq1Field_CpcRepositoryTable_SVN CPC_SVN;
    final public DmRq1Field_CpcRepositoryTable_TrackAndRelease CPC_TRACK_AND_RELEASE;

    //
    //IPE-FLOW
    //
    final public DmRq1Field_Table<Rq1XmlTable_ClusterConfiguration> CLUSTER_CONFIGURATION;
    final public DmRq1Field_Table<Rq1XmlTable_AccessGrantConfiguration> ACCESS_GRANT_CONFIGURATION;
    final public DmRq1Field_Text PO_ACC_GRNT;
    final public DmRq1Field_Table<Rq1XmlTable_RelativeProjectConfiguration> RELATIVE_PROJECT_CONFIGURATION;
    final public DmRq1Field_Text RELATIVE_PL;
    final public DmRq1Field_Table<Rq1XmlTable_ContractedTeam> FLOW_CONTRACTED_TEAM;
    final public DmRq1Field_Enumeration FLOW_RESTRICT_BC;
    final public DmRq1Field_Enumeration FLOW_RESTRICT_HW_MOD;
    final public DmRq1Field_Text FLOW_VERSION;
    final public DmRq1Field_Text FLOW_CONTRACT_PROJECT;
    final public DmRq1Field_Text FLOW_CODEX_PROJECT;
    final public DmRq1Field_Text FLOW_TEAM_NAME;
    final public DmRq1Field_Enumeration FLOW_BOARD_WI_MODE;
    final public DmRq1Field_Text FLOW_TEAM_LEADER;
    final public DmRq1Field_Text FLOW_FETCH_RQ1_EFFORT;
    final public DmRq1Field_Text FLOW_BOARD_VIEW_TYPE;
    final public DmRq1Field_Text FLOW_SHOW_IN_REVIEW_COLUMN;
    final public DmRq1Field_Text FLOW_CONSIDER_PLAN_DATE;
    final public DmRq1Field_Text FLOW_CONSIDER_IRM_CHANGES;
    final public DmRq1Field_Text FLOW_SPRINT_INFO;

    //
    //IPE-CRP
    //
    final public DmRq1Field_Table<Rq1XmlTable_CrpSubTeamConfiguration> CRP_SUBTEAM_CONFIGURATION;
    final public DmRq1Field_Text CRP_VERSION;
    final public DmRq1Field_Text CRP_INCLUDE_TASK_OUTSIDE_PRJ;
    final public DmRq1Field_Text CRP_PROMOTE_RED_TASK;
    final public DmRq1Field_Text CRP_LAST_CLEANUP_DT;
    final public DmRq1Field_Table<Rq1XmlTable_CrpCombinedProjects> CRP_COMBINED_PROJECTS;

    //
    //
    //
    final private ConfigurableRule_ProjectManager configurableRuleProjectManager;

    public DmRq1Project(String subjectType, Rq1Project rq1Project) {
        super(subjectType, rq1Project);

        //
        // Create and add fields
        //
        addField((CUSTOMER = new DmRq1Field_Text(this, rq1Project.CUSTOMER, "Customer")));
        addField(DEFAULT_DEVELOPMENT_METHOD = new DmRq1Field_Enumeration(this, rq1Project.DEFAULT_DEVELOPMENT_METHOD, "Default Development Method"));
        addField(DEFAULT_DEVELOPMENT_METHOD_COMMENT = new DmRq1Field_Text(this, rq1Project.DEFAULT_DEVELOPMENT_METHOD_COMMENT, "Default Development Method Comment"));
        addField(DEFAULT_PLANNING_METHOD = new DmRq1Field_Enumeration(this, rq1Project.DEFAULT_PLANNING_METHOD, "Default Planning Method"));

        addField(TEMPLATES = new DmRq1Field_Text(this, rq1Project.TEMPLATES, "Templates"));
        TEMPLATES.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(MILESTONES_TABLE = new DmRq1Field_MilestoneTable(rq1Project.MILESTONES, "Milestones"));
        MILESTONES_TABLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(WORKITEMS_ON_MILESTONES = new DmGpmFieldOnProject_WorkitemsOnMilestones(this, rq1Project.TASKS_ON_MILESTONES, "Workitems on Milestone"));
        addField(GENERATION = new DmRq1Field_Text(this, rq1Project.GENERATION, "Generation"));
        addField(GENERATIONS = new DmRq1Field_EnumerationSet(rq1Project.GENERATIONS, "Generations"));
        addField(CLASSIFICATION = new DmRq1Field_Text(this, rq1Project.CLASSIFICATION, "Classification"));
        addField(LIFE_CYCLE_STATE = new DmRq1Field_Enumeration(this, rq1Project.LIFE_CYCLE_STATE, "Life Cycle State"));
        LIFE_CYCLE_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.VISIBLE_BY_DEFAULT);

        addField(POOL_PROJECT = new DmRq1Field_Reference<>(this, rq1Project.BELONGS_TO_POOL_PROJECT, "Pool Project"));
        addField(ALL_PST_COLLECTIONS = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_PST_COLLECTIONS, "All PVER/PVAR Collections"));
        addField(OPEN_PST_COLLECTIONS = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_PST_COLLECTIONS, "Open PVER/PVAR Collections"));

        addField(SCOPE = new DmRq1Field_Enumeration(this, rq1Project.SCOPE, "Scope"));

        addField(ASPICESCOPE = new DmRq1AspiceScopeField_Enumeration(rq1Project.ASPICESCOPE, "Aspice Scope"));
        addField(RBD_PROJECT_CATEGORY = new DmRq1RbdProjectCategoryField_Enumeration(rq1Project.RBD_PROJECT_CATEGORY, "RBD Project Category"));
        addField(ASPICE_CALC_DATE = new DmRq1Field_Date(rq1Project.ASPICE_CALC_DATE, "Date of calculation"));
        addField(BBM_METRICS_NEEDED = new DmRq1BbmMetricsNeededField_Enumeration(rq1Project.BBM_METRICS_NEEDED, "BBM Metrics mandatory"));
        addField(BBM_METRICS_INSTALLED = new DmRq1Field_Enumeration(rq1Project.BBM_METRICS_INSTALLED, "BBM Metrics installed"));
        addField(BBM_METRICS_PLANNED_DATE = new DmRq1Field_Date(rq1Project.BBM_METRICS_PLANNED_DATE, "BBM Metrics planned date"));
        addField(BBM_METRICS_COMMENT = new DmRq1Field_Text(rq1Project.BBM_METRICS_COMMENT, "BBM Metrics comment"));
        addField(BBM_METRICS_NEEDED_MANUAL_OVERRIDE = new DmRq1Field_Enumeration(rq1Project.BBM_METRICS_NEEDED_MANUAL_OVERRIDE, "BBM Metrics not needed?"));
        addField(BBM_METRICS_INSTALLED_MANUAL_OVERRIDE = new DmRq1Field_Enumeration(rq1Project.BBM_METRICS_INSTALLED_MANUAL_OVERRIDE, "Is PS-EC Infotool used for BBM-Metrics?"));

        addField(ALL_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_RELEASES, "All Releases"));
        addField(OPEN_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_RELEASES, "Open Releases"));

        addField(ALL_MILESTONES = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_MILESTONES, "All Milestone"));
        addField(OPEN_MILESTONES = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_MILESTONES, "Open Milestones"));

        addField(ALL_HW_ECU_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_HW_ECU_RELEASES, "All HW-ECU"));
        addField(ALL_HW_MOD_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_HW_MOD_RELEASES, "All HW-MOD"));
        addField(ALL_HW_COMP_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_HW_COMP_RELEASES, "All HW-COMP"));
        addField(ALL_BUNDLES = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_BUNDLES, "All Bundles"));
        addField(OPEN_HW_ECU_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_HW_ECU_RELEASES, "Open HW-ECU"));
        addField(OPEN_HW_MOD_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_HW_MOD_RELEASES, "Open HW-MOD"));
        addField(OPEN_HW_COMP_RELEASES = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_HW_COMP_RELEASES, "Open HW-COMP"));
        addField(OPEN_BUNDLES = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_BUNDLES, "Open Bundles"));

        addField(OPEN_ISSUE_ECU = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_ISSUE_ECU, "Open I-HW ECU"));
        addField(OPEN_ISSUE_MOD = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_ISSUE_MOD, "Open I-HW MOD"));
        addField(ALL_ISSUE_ECU = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_ISSUE_ECU, "All I-HW ECU"));
        addField(ALL_ISSUE_MOD = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_ISSUE_MOD, "All I-HW MOD"));

        addField(EXCITEDCPC_TEAM = new DmRq1Field_Text(this, rq1Project.EXCITEDCPC_TEAM, "excITED CPC Team"));
        addField(EXCITEDCPC_GROUP = new DmRq1Field_Text(this, rq1Project.EXCITEDCPC_GROUP, "excITED CPC Group"));
        addField(EXCITEDCPC_DEPARTMENTMAP = new DmRq1Field_Table<>(this, rq1Project.EXCITEDCPC_DEPARTMENTMAP, "excITED CPC Department Map"));
        //QAMi Flow-Tag
        // addField(FLOW_TEAM_NAME = new DmRq1Field_Text(this, rq1Project.FLOW_TEAM_NAME, "Flow Team Name"));

        addField((RESPONSIBLE_AT_BOSCH = new DmRq1Field_Text(this, rq1Project.RESPONSIBLE_AT_BOSCH, "Responsible at Bosch")));

        addField(OPEN_WORKITEMS = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_WORKITEMS, "Open Workitem"));
        addField(ALL_WORKITEMS = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_WORKITEMS, "All Workitem"));

        addField(OPEN_PROBLEMS = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_PROBLEMS, "Open Problem"));
        addField(ALL_PROBLEMS = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_PROBLEMS, "All Problem"));

        addField(PROJECT_LEADER = new DmRq1Field_Reference<>(this, rq1Project.PROJECT_LEADER, "Project Leader"));
        addField(PROJECT_LEADER_FULLNAME = new DmRq1Field_Text(this, rq1Project.PROJECT_LEADER_FULLNAME, "Fullname Project Leader"));
        addField(PROJECT_LEADER_EMAIL = new DmRq1Field_Text(this, rq1Project.PROJECT_LEADER_EMAIL, "E-Mail Project Leader"));
        addField(PROJECT_LEADER_LOGIN_NAME = new DmRq1Field_Text(this, rq1Project.PROJECT_LEADER_LOGIN_NAME, "Shortcut Project Leader"));

        addField(HAS_PROJECT_MEMBERS = new DmRq1Field_ActiveProjectMembers(this, rq1Project.HAS_PROJECT_MEMBERS, "Project Members"));

        addField(PRODUCTS = new DmToDsValueField<>(rq1Project.PRODUCTS, "Products"));

        addField(COMPANY_DATA_ID = new DmRq1Field_Enumeration(this, rq1Project.COMPANY_DATA_ID, "Company Data Id"));

        addField(IPE = new DmRq1Field_Text(this, rq1Project.IPE, "IPE"));
        addField(TUL_ACTIVE = new DmRq1Field_Text(this, rq1Project.TUL_ACTIVE, "TULactive"));

        //
        // GPM - Guided project management
        //
        addField(IMPACT_CATEGORY = new DmRq1Field_Enumeration(rq1Project.IMPACT_CATEGORY, "PIC ECU"));
        addField(GPM_USER_ROLES = new DmGpmFieldOnProject_UserRoles(rq1Project.GPM_USER_ROLES, "User Roles"));
        addField(GPM_DATA_OF_EXTERNAL_MILESTONES = new DmGpmFieldOnProject_DataOfExternalMilestones(rq1Project.DATA_OF_EXTERNAL_MILESTONES, "Data of external milestones"));
        addField(GPM_ORG_DATA = new DmGpmField_OrgData(this, rq1Project.GPM_ORG_DATA, "Org Chart"));

        //
        // CPC - Central project configuration
        //
        addField(CPC_DNG = new DmRq1Field_CpcRepositoryTable_DNG(this, rq1Project.CPC_DNG, "DNG", "Customer Requirements"));
        addField(CPC_DOORS = new DmRq1Field_CpcRepositoryTable_DOORS(this, rq1Project.CPC_DOORS, "DOORS", "Customer Requirements"));
        addField(CPC_ENTERPRISE_DASHBOARD = new DmRq1Field_CpcRepositoryTable_EnterpriseDashboard(this, rq1Project.CPC_ENTERPRISE_DASHBOARD, "Enterprise Dashboard"));
        addField(CPC_FMEA_DB = new DmRq1Field_CpcRepositoryTable_FMEA_DB(this, rq1Project.CPC_FMEA_DB, "FMEA-DB"));
        addField(CPC_HIS = new DmRq1Field_CpcRepositoryTable_HIS(this, rq1Project.CPC_HIS, "HIS", "HW Sub-Projects"));
        addField(CPC_iCDM = new DmRq1Field_CpcRepositoryTable_iCDM(this, rq1Project.CPC_iCDM, "iCDM", "CAL Sub-Projects"));
        addField(CPC_iGPM_MCR_ECU = new DmRq1Field_CpcRepositoryTable_iGPM_MCR_ECU(this, rq1Project.CPC_iGPM_MCR_ECU, "iGPM-MCR ECU", "ECU Project incl. HW / SW / CAL"));
        addField(CPC_iGPM_MCR_System_Parent_Project = new DmRq1Field_CpcRepositoryTable_iGPM_MCR_System_Parent_Project(this, rq1Project.CPC_iGPM_MCR_System_Parent_Project, "iGPM-MCR", "System Parent Project"));
        addField(CPC_iGPM_ONE_Q = new DmRq1Field_CpcRepositoryTable_iGPM_oneQ(this, rq1Project.CPC_iGPM_ONE_Q, "iGPM oneQ", "ECU Project"));
        addField(CPC_iGPM_QUO = new DmRq1Field_CpcRepositoryTable_iGPM_QUO(this, rq1Project.CPC_iGPM_QUO, "iGPM-QUO"));
        addField(CPC_ILM_AT_SHAREPOINT = new DmRq1Field_CpcRepositoryTable_ILM_AT_Sharepoint(this, rq1Project.CPC_ILM_AT_SHAREPOINT, "ILM@Sharepoint", "ECU Project"));
        addField(CPC_NT_SHARE = new DmRq1Field_CpcRepositoryTable_NT_Share(this, rq1Project.CPC_NT_SHARE, "NT-Share", "ECU Project"));
        addField(CPC_PROVISOR2 = new DmRq1Field_CpcRepositoryTable_Provisor2(this, rq1Project.CPC_PROVISOR2, "Provisor2"));
        addField(CPC_RQ1 = new DmRq1Field_CpcRepositoryTable_RQ1(this, rq1Project.CPC_RQ1, "RQ1", "Linked RQ1 Projects"));
        addField(CPC_SMART_WEB = new DmRq1Field_CpcRepositoryTable_SMARTweb(this, rq1Project.CPC_SMART_WEB, "SMARTweb"));
        addField(CPC_SUPER_OPL = new DmRq1Field_CpcRepositoryTable_SuperOPL(this, rq1Project.CPC_SUPER_OPL, "Super OPL"));
        addField(CPC_SVN = new DmRq1Field_CpcRepositoryTable_SVN(this, rq1Project.CPC_SVN, "SVN"));
        addField(CPC_TRACK_AND_RELEASE = new DmRq1Field_CpcRepositoryTable_TrackAndRelease(this, rq1Project.CPC_TRACK_AND_RELEASE, "Track & Release OPL"));

        //
        // Time Schedule - Depends on other fields
        //
        addField(TIME_SCHEDULE_OF_PROJECT = new DmGpmFieldOnProject_TimeScheduleOfProject(this, "Milestones on Project"));

        //
        //IPE-FLOW
        //
        addField(FLOW_VERSION = new DmRq1Field_Text(this, rq1Project.FLOW_VERSION, "Flow Version"));
        addField(FLOW_TEAM_NAME = new DmRq1Field_Text(this, rq1Project.FLOW_TEAM_NAME, "Team Name"));
        addField(FLOW_CONTRACTED_TEAM = new DmRq1Field_Table<>(this, rq1Project.FLOW_CONTRACTED_TEAM, "Contracted Team Configuration"));
        addField(FLOW_CONTRACT_PROJECT = new DmRq1Field_Text(this, rq1Project.FLOW_CONTRACTED_PROJECTS, "Contracted Projects"));
        addField(FLOW_CODEX_PROJECT = new DmRq1Field_Text(this, rq1Project.FLOW_CODEX_PROJECTS, "Codex Projects"));
        addField(CLUSTER_CONFIGURATION = new DmRq1Field_Table<>(this, rq1Project.CLUSTER_TABLE, "Cluster Configuration"));
        addField(ACCESS_GRANT_CONFIGURATION = new DmRq1Field_Table<>(this, rq1Project.ACCESS_GRANTS, "Access Grant Configuration"));
        addField(RELATIVE_PROJECT_CONFIGURATION = new DmRq1Field_Table<>(this, rq1Project.RELATIVE_PROJECT, "Combined Projects"));
        addField(RELATIVE_PL = new DmRq1Field_Text(this, rq1Project.RELATIVE_PL, "Combined ProjectList Configuration"));
        addField(FLOW_RESTRICT_BC = new DmRq1Field_Enumeration(this, rq1Project.FLOW_RESTRICT_BC, "Exclude BC from flow"));
        addField(FLOW_RESTRICT_HW_MOD = new DmRq1Field_Enumeration(this, rq1Project.FLOW_RESTRICT_HW_MOD, "Exclude HW-Mod from flow"));
        addField(FLOW_BOARD_WI_MODE = new DmRq1Field_Enumeration(this, rq1Project.FLOW_BOARD_WI_MODE, "Board WorkItem Mode"));
        addField(FLOW_TEAM_LEADER = new DmRq1Field_Text(this, rq1Project.FLOW_TEAM_LEADER, "Team Leader"));
        addField(FLOW_FETCH_RQ1_EFFORT = new DmRq1Field_Text(this, rq1Project.FLOW_FETCH_RQ1_EFFORT, "Rq1 Effort for Predictions"));
        addField(FLOW_BOARD_VIEW_TYPE = new DmRq1Field_Text(this, rq1Project.FLOW_BOARD_VIEW_TYPE, "Board View Type"));
        addField(FLOW_SHOW_IN_REVIEW_COLUMN = new DmRq1Field_Text(this, rq1Project.FLOW_SHOW_IN_REVIEW_COLUMN, "Display In-Review column in board"));
        addField(FLOW_CONSIDER_PLAN_DATE = new DmRq1Field_Text(this, rq1Project.FLOW_CONSIDER_PLAN_DATE, "Considers planned date for all computations"));
        addField(FLOW_CONSIDER_IRM_CHANGES = new DmRq1Field_Text(this, rq1Project.FLOW_CONSIDER_IRM_CHANGES, "Consider IRM changes to Issues"));
        addField(PO_ACC_GRNT = new DmRq1Field_Text(this, rq1Project.PO_ACC_GRNT, "PO Access Grant Information"));
        addField(FLOW_SPRINT_INFO = new DmRq1Field_Text(this, rq1Project.FLOW_SPRINT_INFO, "Sprint information"));

        //
        //IPE-CRP
        //
        addField(CRP_VERSION = new DmRq1Field_Text(this, rq1Project.CRP_VERSION, "CRP Version"));
        addField(CRP_SUBTEAM_CONFIGURATION = new DmRq1Field_Table<>(this, rq1Project.CRP_CLUSTER_TABLE, "CRP Cluster Configuration"));
        addField(CRP_INCLUDE_TASK_OUTSIDE_PRJ = new DmRq1Field_Text(this, rq1Project.CRP_INCLUDE_TASK_OUTSIDE_PRJ, "CRP Include Task Outside Prj"));
        addField(CRP_PROMOTE_RED_TASK = new DmRq1Field_Text(this, rq1Project.CRP_PROMOTE_RED_TASK, "CRP Promote Red Task"));
        addField(CRP_LAST_CLEANUP_DT = new DmRq1Field_Text(this, rq1Project.CRP_LAST_CLEANUP_DT, "CRP Last Cleanup Operation Date"));
        addField(CRP_COMBINED_PROJECTS = new DmRq1Field_Table<>(this, rq1Project.CRP_COMBINED_PROJECTS, "CRP Combined Projects"));

        //
        // Add switchable rule groups
        //
        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.PROJECT));

        // Add configurable Rule Project Manager for USER ASSISTANT
        configurableRuleProjectManager = new ConfigurableRule_ProjectManager(this, rq1Project);
    }

    @Override
    final public String getElementClass() {
        return ("Project");
    }

    @Override
    final public boolean isCanceled() {
        return (LIFE_CYCLE_STATE.getValue() == LifeCycleState_Project.CANCELED);
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    @Override
    final public String getResponsible() {
        return (RESPONSIBLE_AT_BOSCH.getValue());
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LIFE_CYCLE_STATE.getValidInputValues());
    }

    public Rq1ProjectTemplateDefinition getTemplateDefinition(Rq1RecordType recordType) {
        assert (recordType != null);

        return (((Rq1Project) rq1Record).getTemplateDefinition(recordType, DmRq1User.getLoginName()));
    }

    /**
     * Compares the two projects via the type, id and title
     *
     * @param p1 The first project. null is allowed.
     * @param p2 The second project. null is allowed.
     * @return
     */
    static public int compareByTypeIdTitle(DmRq1Project p1, DmRq1Project p2) {
        if (p1 == p2) {
            return (0);
        } else {
            String s1 = p1 != null ? p1.getTitle() : "";
            String s2 = p2 != null ? p2.getTitle() : "";
            return (s1.compareToIgnoreCase(s2));
        }
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTypeIdTitle();
    }

    @Override
    public String getIdForSubject() {
        return getId();
    }

    @Override
    public List<MailActionType> getActionName() {
        List<MailActionType> mailActionTypes = new ArrayList<>();
        mailActionTypes.add(MailActionType.PROJECTLEADER);
        return mailActionTypes;
    }

    @Override
    public int compareTo(DmRq1Project t) {
        assert (t.getId() != null);
        assert (this.getId() != null);

        return this.getId().compareTo(t.getId());
    }

    public Customer getCustomer() {
        return (Customer.getCustomer(CUSTOMER.getValue()));
    }

    @Override
    public String getAssigneeMail() {
        return null;
    }

    @Override
    public String getProjectLeaderMail() {
        return PROJECT_LEADER_EMAIL.getValueAsText();
    }

    @Override
    public String getRequesterMail() {
        return null;
    }

    @Override
    public String getContactMail() {
        return null;
    }

    public ConfigurableRule_ProjectManager getConfigurableRuleProjectManager() {
        return configurableRuleProjectManager;
    }

    //-------------------------------------------------------------------------------------
    //
    // Load optimization
    //
    //-------------------------------------------------------------------------------------
    private boolean isLoadCacheFor_WorkitemsOnAllReleases_Done = false;
    private boolean isLoadCacheFor_GpmView_Done = false;

    public void loadCacheFor_WorkitemsOnAllReleases() {

        if (isLoadCacheFor_WorkitemsOnAllReleases_Done == false) {

            OslcLoadHint loadHint = new OslcLoadHint(true);
            loadHint.followField(Rq1Release.ATTRIBUTE_HAS_WORKITEMS, true);

            ALL_RELEASES.loadCache(loadHint);

            isLoadCacheFor_WorkitemsOnAllReleases_Done = true;
        }
    }

    public void loadCacheForGpmView() {
        if (isLoadCacheFor_GpmView_Done == false) {
            ALL_WORKITEMS.getElementList();
            loadCacheFor_WorkitemsOnAllReleases();
            isLoadCacheFor_GpmView_Done = true;
        }
    }

    //-------------------------------------------------------------------------------------
    //
    // Support for user handling
    //
    //-------------------------------------------------------------------------------------
    /**
     * Returns the user object for the given user id, if the user with this user
     * id is a project member.
     *
     * @param userId User Id of a user that is project member.
     * @return The user object, of the first project member that fits to the
     * user id. null, if no project member fits to the user id.
     */
    public DmRq1User getAssigneeForUserId(String userId) {

        List<DmMappedElement<DmRq1UserRole, DmRq1User>> projectMembers = HAS_PROJECT_MEMBERS.getElementList();
        for (DmMappedElement<DmRq1UserRole, DmRq1User> member : projectMembers) {
            DmRq1User rq1User = (DmRq1User) member.getTarget();
            if (rq1User.LOGIN_NAME.getValueAsText().equals(userId) == true) {
                return (rq1User);
            }
        }

        return (null);
    }

    public String getFullNameForUserId(String userId) {

        List<DmMappedElement<DmRq1UserRole, DmRq1User>> projectMembers = HAS_PROJECT_MEMBERS.getElementList();
        for (DmMappedElement<DmRq1UserRole, DmRq1User> member : projectMembers) {
            DmRq1User rq1User = (DmRq1User) member.getTarget();
            if (rq1User.LOGIN_NAME.getValueAsText().equals(userId) == true) {
                return rq1User.FULLNAME.getValue();
            }
        }

        return (null);
    }

    /**
     * Check whether or not the current login user has the rights of a project
     * leader on this project.
     *
     * @return true, if the login user has project leader rights.
     */
    public Role getRoleForLoginUser() {
        DmRq1User loginUser = DmRq1User.getLoginUser();
        if (loginUser == null) {
            return (Role.NONE);
        }

        return (getRoleForUser(loginUser));
    }

    /**
     * Check which role the user has in the project.
     *
     * @param userToCheck User for which the test shall be done.
     * @return The role of the user in the project.
     */
    public Role getRoleForUser(DmRq1User userToCheck) {
        assert (userToCheck != null);

        if (PROJECT_LEADER.isElementEqual(userToCheck)) {
            return (Role.PROJECT_LEADER);
        }

        Role bestRole = Role.NONE;

        for (DmMappedElement<DmRq1UserRole, DmRq1User> member : this.HAS_PROJECT_MEMBERS.getElementList()) {
            DmRq1User rq1User = member.getTarget();
            if (rq1User.equals(userToCheck)) {
                DmRq1UserRole rq1UserRole = member.getMap();
                bestRole = bestRole.getRoleWithMoreAccessRights(rq1UserRole.getRole());
            }
        }

        return (bestRole);

    }

    //-------------------------------------------------------------------------------------
    //
    // Support for GPM - Guided project management
    //
    //-------------------------------------------------------------------------------------
    @Override
    public void reload() {
        DmGpmConfigManager_Project.clearCacheForProjectConfigData(this);
        HAS_PROJECT_MEMBERS.reload(); // For the PrjOrgChart the ProjectMembers has also to be reloaded
        super.reload();
    }

    public void setBbmMetricsInstalled() {
        BbmMetricsAPI api = new BbmMetricsAPI();
        String projectNumbers = api.getProjectsBbmMetricsInstalled();
        if (projectNumbers != null && projectNumbers.contains(getIdForSubject())) {
            this.BBM_METRICS_INSTALLED.setValue(BbmMetricsInstalled.YES);
        } else {
            this.BBM_METRICS_INSTALLED.setValue(BbmMetricsInstalled.NO);
        }
    }

    //-------------------------------------------------------------------------------------
    //
    // Support for loading of projects
    //
    //-------------------------------------------------------------------------------------
    static private List<DmRq1Project> projectList = null;

    /**
     * Returns all open projects from the database.
     *
     * @return A list containing all current open project.
     */
    static public List<DmRq1Project> getOpenProjectList() {
        if (projectList == null) {
            projectList = new ArrayList<>();
            for (Rq1Project rq1Record : Rq1Project.getOpenProjectList()) {
                projectList.add((DmRq1Project) DmRq1ElementCache.getElement(rq1Record));
            }
        }
        return (projectList);
    }

    public String getFlowVersion() {
        return (FLOW_VERSION.getValueAsText());

    }

    public String getFlowContractedProjects() {
        return (FLOW_CONTRACT_PROJECT.getValueAsText());

    }

    public String getCodexProjectStatus() {
        return (FLOW_CODEX_PROJECT.getValueAsText());

    }

    public String getFlowTeamName() {
        return (FLOW_TEAM_NAME.getValueAsText());
    }

    public String getFlowBoardType() {
        return (FLOW_BOARD_VIEW_TYPE.getValueAsText());
    }

    public String getFlowTeamLeaders() {
        return (FLOW_TEAM_LEADER.getValueAsText());
    }

    public String getFlowSprintInfo() {
        return (FLOW_SPRINT_INFO.getValueAsText());
    }

    public DmRq1Field_Text getCrpVersion() {
        return CRP_VERSION;
    }

    public DmRq1Field_Text getCRPLastCleanupDt() {
        return CRP_LAST_CLEANUP_DT;
    }

}
