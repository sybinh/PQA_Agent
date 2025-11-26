/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataModel.Flow.BoardWiMode;
import DataModel.Flow.RestrictBc;
import DataStore.DsField_Xml;
import DataStore.DsField_Xml.ContentMode;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_EnumerationSet;
import Rq1Cache.Fields.Rq1DatabaseField_MappedReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Products;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Cache.Fields.Rq1QueryField;
import Rq1Cache.Fields.Rq1QueryField_belongsToProject;
import Rq1Cache.Fields.Rq1ReferenceListField_FilterByClass;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1XmlTable_AccessGrantConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ClusterConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ContractedTeam;
import Rq1Cache.Types.Rq1XmlTable_CrpCombinedProjects;
import Rq1Cache.Types.Rq1XmlTable_CrpSubTeamConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ExcItedDepartmentMap;
import Rq1Cache.Types.Rq1XmlTable_Milestones;
import Rq1Cache.Types.Rq1XmlTable_RelativeProjectConfiguration;
import Rq1Data.CPC.CpcXmlTable_DNG;
import Rq1Data.CPC.CpcXmlTable_DOORS;
import Rq1Data.CPC.CpcXmlTable_FMEA_DB;
import Rq1Data.CPC.CpcXmlTable_Provisor2;
import Rq1Data.CPC.CpcXmlTable_RQ1;
import Rq1Data.CPC.CpcXmlTable_SMARTweb;
import Rq1Data.CPC.CpcXmlTable_TrackAndRelease;
import Rq1Data.CPC.CpcXmlTable_HIS;
import Rq1Data.CPC.CpcXmlTable_ILM_AT_Sharepoint;
import Rq1Data.CPC.CpcXmlTable_NT_Share;
import Rq1Data.CPC.CpcXmlTable_iGPM_oneQ;
import Rq1Data.CPC.CpcXmlTable_EnterpriseDashboard;
import Rq1Data.CPC.CpcXmlTable_SVN;
import Rq1Data.CPC.CpcXmlTable_SuperOPL;
import Rq1Data.CPC.CpcXmlTable_iCDM;
import Rq1Data.CPC.CpcXmlTable_iGPM_MCR_ECU;
import Rq1Data.CPC.CpcXmlTable_iGPM_MCR_System_Parent_Project;
import Rq1Data.CPC.CpcXmlTable_iGPM_QUO;
import Rq1Data.Enumerations.AspiceScope;
import Rq1Data.Enumerations.BbmMetricsInstalled;
import Rq1Data.Enumerations.BbmMetricsNeeded;
import Rq1Data.Enumerations.BbmMetricsNeededManualOverride;
import Rq1Data.Enumerations.CompanyDataId;
import Rq1Data.Enumerations.DevelopmentMethod;
import Rq1Data.Enumerations.ImpactCategory;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.LifeCycleState_Problem;
import Rq1Data.Enumerations.LifeCycleState_Project;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.LifeCycleState_WorkItem;
import Rq1Data.Enumerations.PlanningMethod;
import Rq1Data.Enumerations.RBDProjectCategory;
import Rq1Data.Enumerations.Scope;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import Rq1Data.GPM.GpmXmlTable_GpmUserRoles;
import Rq1Data.GPM.GpmXmlTable_TasksOnMilestones;
import Rq1Data.Templates.Rq1ProjectTemplate;
import Rq1Data.Templates.Rq1ProjectTemplateDefinition;
import Rq1Data.Templates.Rq1ProjectTemplateFactory;
import Rq1Data.Templates.Rq1ProjectTemplateSet;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class Rq1Project extends Rq1Subject {

    final static public Rq1AttributeName ATTRIBUTE_CUSTOMER = new Rq1AttributeName("Customer");
    final static public Rq1AttributeName ATTRIBUTE_HAS_RELEASES = new Rq1AttributeName("hasReleases");
    final static public Rq1AttributeName ATTRIBUTE_GENERATIONS = new Rq1AttributeName("Generations");
    final static public Rq1AttributeName ATTRIBUTE_OPEN_WORKITEMS = new Rq1AttributeName("OpenWorkItem");

    static final public String FIELDNAME_DEFAULT_DEVELOPMENT_METHOD = "DefaultDevelopmentMethod";
    static final public String FIELDNAME_DEFAULT_PLANNING_METHOD = "DefaultPlanningMethod";
    static final public String FIELDNAME_ISSUES = "hasIssues";
    static final public String FIELDNAME_TYPE = "Type";
    static final public String FIELDNAME_LIFE_CYCLE_STATE = "LifeCycleState";

    final public Rq1DatabaseField_Text CUSTOMER;
    final public Rq1DatabaseField_Enumeration DEFAULT_DEVELOPMENT_METHOD;
    final public Rq1XmlSubField_Text DEFAULT_DEVELOPMENT_METHOD_COMMENT;
    final public Rq1DatabaseField_Enumeration DEFAULT_PLANNING_METHOD;
    final public Rq1DatabaseField_Text EXTERNAL_ID;
    final public Rq1DatabaseField_Text EXTERNAL_TITLE;
    final public Rq1DatabaseField_Text EXTERNAL_DESCRIPTION;
    final public Rq1DatabaseField_Text GENERATION;
    final public Rq1DatabaseField_EnumerationSet GENERATIONS;
    final public Rq1DatabaseField_Text CLASSIFICATION;
    final public Rq1DatabaseField_Text RESPONSIBLE_AT_BOSCH;
    final public Rq1DatabaseField_Text TEMPLATES;

    final public Rq1DatabaseField_Enumeration LIFE_CYCLE_STATE;
    final public Rq1DatabaseField_Enumeration SCOPE;
    final private Rq1DatabaseField_Xml MILESTONE_FIELD;
    final public Rq1XmlSubField_Table<Rq1XmlTable_Milestones> MILESTONES;

    final private Rq1XmlSubField_Xml ASPICE;
    final public Rq1XmlSubField_Enumeration ASPICESCOPE;
    final public Rq1XmlSubField_Enumeration RBD_PROJECT_CATEGORY;
    final public Rq1XmlSubField_Date ASPICE_CALC_DATE;
    final public Rq1XmlSubField_Enumeration BBM_METRICS_NEEDED;
    final public Rq1XmlSubField_Enumeration BBM_METRICS_INSTALLED;
    final public Rq1XmlSubField_Date BBM_METRICS_PLANNED_DATE;
    final public Rq1XmlSubField_Text BBM_METRICS_COMMENT;
    final public Rq1XmlSubField_Enumeration BBM_METRICS_NEEDED_MANUAL_OVERRIDE;
    final public Rq1XmlSubField_Enumeration BBM_METRICS_INSTALLED_MANUAL_OVERRIDE;

    final public Rq1DatabaseField_Reference BELONGS_TO_POOL_PROJECT;
    final public Rq1DatabaseField_Reference HAS_PARENT;
    final public Rq1DatabaseField_Reference PROJECT_LEADER;
    final public Rq1DatabaseField_Text PROJECT_LEADER_FULLNAME;
    final public Rq1DatabaseField_Text PROJECT_LEADER_EMAIL;
    final public Rq1DatabaseField_Text PROJECT_LEADER_LOGIN_NAME;

    final public Rq1ReferenceListField_FilterByClass OPEN_PST_COLLECTIONS;
    final public Rq1ReferenceListField_FilterByClass ALL_PST_COLLECTIONS;

    final public Rq1DatabaseField_ReferenceList ALL_RELEASES;
    final public Rq1QueryField_belongsToProject OPEN_RELEASES;

    final public Rq1ReferenceListField_FilterByClass ALL_MILESTONES;
    final public Rq1ReferenceListField_FilterByClass OPEN_MILESTONES;

    final public Rq1ReferenceListField_FilterByClass ALL_HW_ECU_RELEASES;
    final public Rq1ReferenceListField_FilterByClass ALL_HW_MOD_RELEASES;
    final public Rq1ReferenceListField_FilterByClass ALL_HW_COMP_RELEASES;
    final public Rq1ReferenceListField_FilterByClass ALL_BUNDLES;
    final public Rq1ReferenceListField_FilterByClass OPEN_HW_ECU_RELEASES;
    final public Rq1ReferenceListField_FilterByClass OPEN_HW_MOD_RELEASES;
    final public Rq1ReferenceListField_FilterByClass OPEN_HW_COMP_RELEASES;
    final public Rq1ReferenceListField_FilterByClass OPEN_BUNDLES;

    final protected Rq1DatabaseField_ReferenceList ALL_ISSUES;
    final protected Rq1QueryField_belongsToProject OPEN_ISSUES;

    final public Rq1ReferenceListField_FilterByClass OPEN_ISSUE_ECU;
    final public Rq1ReferenceListField_FilterByClass OPEN_ISSUE_MOD;
    final public Rq1ReferenceListField_FilterByClass ALL_ISSUE_ECU;
    final public Rq1ReferenceListField_FilterByClass ALL_ISSUE_MOD;

    final public Rq1XmlSubField_Xml EXCITEDCPC;
    final public Rq1XmlSubField_Text EXCITEDCPC_TEAM;
    final public Rq1XmlSubField_Text EXCITEDCPC_GROUP;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ExcItedDepartmentMap> EXCITEDCPC_DEPARTMENTMAP;

    final public Rq1DatabaseField_MappedReferenceList HAS_PROJECT_MEMBERS;

    final public Rq1DatabaseField_Products PRODUCTS;

    final public Rq1XmlSubField_Enumeration COMPANY_DATA_ID;

    //
    // GPM - Guided project management
    //
    final private Rq1XmlSubField_Xml GPM;
    final public Rq1XmlSubField_Enumeration IMPACT_CATEGORY;
    final public Rq1XmlSubField_Table<GpmXmlTable_GpmUserRoles> GPM_USER_ROLES;
    final public Rq1XmlSubField_Xml GPM_ORG_DATA;
    final private Rq1XmlSubField_Xml MILESTONES_IN_TAGS;
    final public Rq1XmlSubField_Table<GpmXmlTable_TasksOnMilestones> TASKS_ON_MILESTONES;
    final private Rq1XmlSubField_Xml DATA_OF_EXTERNAL_MILESTONES_IN_TAGS;
    final public Rq1XmlSubField_Table<GpmXmlTable_DataOfExternalMilestones> DATA_OF_EXTERNAL_MILESTONES;

    //
    // CPC - Central Project Configuration
    //
    final public Rq1XmlSubField_Table<CpcXmlTable_DNG> CPC_DNG;
    final public Rq1XmlSubField_Table<CpcXmlTable_DOORS> CPC_DOORS;
    final public Rq1XmlSubField_Table<CpcXmlTable_FMEA_DB> CPC_FMEA_DB;
    final public Rq1XmlSubField_Table<CpcXmlTable_HIS> CPC_HIS;
    final public Rq1XmlSubField_Table<CpcXmlTable_iCDM> CPC_iCDM;
    final public Rq1XmlSubField_Table<CpcXmlTable_iGPM_MCR_ECU> CPC_iGPM_MCR_ECU;
    final public Rq1XmlSubField_Table<CpcXmlTable_iGPM_MCR_System_Parent_Project> CPC_iGPM_MCR_System_Parent_Project;
    final public Rq1XmlSubField_Table<CpcXmlTable_iGPM_oneQ> CPC_iGPM_ONE_Q;
    final public Rq1XmlSubField_Table<CpcXmlTable_iGPM_QUO> CPC_iGPM_QUO;
    final public Rq1XmlSubField_Table<CpcXmlTable_ILM_AT_Sharepoint> CPC_ILM_AT_SHAREPOINT;
    final public Rq1XmlSubField_Table<CpcXmlTable_NT_Share> CPC_NT_SHARE;
    final public Rq1XmlSubField_Table<CpcXmlTable_EnterpriseDashboard> CPC_ENTERPRISE_DASHBOARD;
    final public Rq1XmlSubField_Table<CpcXmlTable_Provisor2> CPC_PROVISOR2;
    final public Rq1XmlSubField_Table<CpcXmlTable_RQ1> CPC_RQ1;
    final public Rq1XmlSubField_Table<CpcXmlTable_SMARTweb> CPC_SMART_WEB;
    final public Rq1XmlSubField_Table<CpcXmlTable_SuperOPL> CPC_SUPER_OPL;
    final public Rq1XmlSubField_Table<CpcXmlTable_SVN> CPC_SVN;
    final public Rq1XmlSubField_Table<CpcXmlTable_TrackAndRelease> CPC_TRACK_AND_RELEASE;

    //
    //IPE-FLOW
    //
    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Text FLOW_CONTRACTED_PROJECTS;
    final public Rq1XmlSubField_Text FLOW_CODEX_PROJECTS;
    final public Rq1XmlSubField_Text FLOW_TEAM_NAME;
    final public Rq1XmlSubField_Text FLOW_TEAM_LEADER;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ClusterConfiguration> CLUSTER_TABLE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_AccessGrantConfiguration> ACCESS_GRANTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RelativeProjectConfiguration> RELATIVE_PROJECT;
    final public Rq1XmlSubField_Text RELATIVE_PL;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ContractedTeam> FLOW_CONTRACTED_TEAM;
    final public Rq1XmlSubField_Enumeration FLOW_RESTRICT_BC;
    final public Rq1XmlSubField_Enumeration FLOW_RESTRICT_HW_MOD;
    final public Rq1XmlSubField_Enumeration FLOW_BOARD_WI_MODE;
    final public Rq1XmlSubField_Text FLOW_FETCH_RQ1_EFFORT;
    final public Rq1XmlSubField_Text FLOW_BOARD_VIEW_TYPE;
    final public Rq1XmlSubField_Text FLOW_SHOW_IN_REVIEW_COLUMN;
    final public Rq1XmlSubField_Text FLOW_CONSIDER_PLAN_DATE;
    final public Rq1XmlSubField_Text FLOW_CONSIDER_IRM_CHANGES;
    final public Rq1XmlSubField_Text PO_ACC_GRNT;
    final public Rq1XmlSubField_Text FLOW_SPRINT_INFO;

    //
    //IPE-CRP 
    //
    final public Rq1XmlSubField_Xml CRP;
    final public Rq1XmlSubField_Text CRP_VERSION;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CrpSubTeamConfiguration> CRP_CLUSTER_TABLE;
    final public Rq1XmlSubField_Text CRP_INCLUDE_TASK_OUTSIDE_PRJ;
    final public Rq1XmlSubField_Text CRP_PROMOTE_RED_TASK;
    final public Rq1XmlSubField_Text CRP_LAST_CLEANUP_DT;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CrpCombinedProjects> CRP_COMBINED_PROJECTS;

    //
    // Hierarchie
    //
    final public Rq1DatabaseField_ReferenceList HAS_CHILDREN;
    final public Rq1DatabaseField_ReferenceList HAS_POOL_PROJECT_MEMBERS;

    //
    // Workitems
    // 
    final public Rq1DatabaseField_ReferenceList ALL_WORKITEMS;
    final public Rq1QueryField OPEN_WORKITEMS;

    //
    // Problems
    //
    final public Rq1DatabaseField_ReferenceList ALL_PROBLEMS;
    final public Rq1QueryField OPEN_PROBLEMS;

    final public Rq1XmlSubField_Text TUL_ACTIVE;
    final public Rq1XmlSubField_Text IPE;

    public Rq1Project(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        //
        // Simple fields
        //
        addField(BELONGS_TO_POOL_PROJECT = new Rq1DatabaseField_Reference(this, "belongsToPoolProject", Rq1RecordType.PROJECT));
        BELONGS_TO_POOL_PROJECT.setOptional();
        addField(CUSTOMER = new Rq1DatabaseField_Text(this, ATTRIBUTE_CUSTOMER));
        addField(DEFAULT_DEVELOPMENT_METHOD = new Rq1DatabaseField_Enumeration(this, FIELDNAME_DEFAULT_DEVELOPMENT_METHOD, DevelopmentMethod.values(), DevelopmentMethod.EMPTY));
        DEFAULT_DEVELOPMENT_METHOD.acceptInvalidValuesInDatabase();
        addField(DEFAULT_DEVELOPMENT_METHOD_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "DefaultDevelopmentMethod_Comment"));
        addField(DEFAULT_PLANNING_METHOD = new Rq1DatabaseField_Enumeration(this, FIELDNAME_DEFAULT_PLANNING_METHOD, PlanningMethod.values(), PlanningMethod.EMPTY));
        addField(EXTERNAL_ID = new Rq1DatabaseField_Text(this, "External_ID"));
        addField(EXTERNAL_TITLE = new Rq1DatabaseField_Text(this, "ExternalTitle"));
        addField(EXTERNAL_DESCRIPTION = new Rq1DatabaseField_Text(this, "ExternalDescription"));
        addField(GENERATION = new Rq1DatabaseField_Text(this, "Generation"));
        addField(GENERATIONS = new Rq1DatabaseField_EnumerationSet(this, ATTRIBUTE_GENERATIONS, ""));
        addField(CLASSIFICATION = new Rq1DatabaseField_Text(this, "Classification"));
        addField(HAS_PARENT = new Rq1DatabaseField_Reference(this, "hasParent", Rq1RecordType.PROJECT));
        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Enumeration(this, FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_Project.values(), LifeCycleState_Project.NEW));
        addField(MILESTONE_FIELD = new Rq1DatabaseField_Xml(this, "Milestones"));
        addField(MILESTONES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_Milestones(), MILESTONE_FIELD));

        addField(RESPONSIBLE_AT_BOSCH = new Rq1DatabaseField_Text(this, "ResponsibleAtBosch"));
        addField(SCOPE = new Rq1DatabaseField_Enumeration(this, "Scope", Scope.values()));
        addField(ASPICE = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "ASPICE"));
        addField(ASPICESCOPE = new Rq1XmlSubField_Enumeration(this, ASPICE, "AspiceScope", AspiceScope.values(), AspiceScope.NOT_CALCULATED));
        addField(RBD_PROJECT_CATEGORY = new Rq1XmlSubField_Enumeration(this, ASPICE, "RbdProjectCategory", RBDProjectCategory.values(), RBDProjectCategory.NOT_CALCULATED));
        addField(ASPICE_CALC_DATE = new Rq1XmlSubField_Date(this, ASPICE, "AspiceCalcDate"));
        addField(BBM_METRICS_NEEDED = new Rq1XmlSubField_Enumeration(this, ASPICE, "BbmMetricsNeeded", BbmMetricsNeeded.values(), BbmMetricsNeeded.EMPTY));
        addField(BBM_METRICS_INSTALLED = new Rq1XmlSubField_Enumeration(this, ASPICE, "BbmMetricsInstalled", BbmMetricsInstalled.values(), BbmMetricsInstalled.NO));
        addField(BBM_METRICS_PLANNED_DATE = new Rq1XmlSubField_Date(this, ASPICE, "BbmMetricsPlannedDate"));
        addField(BBM_METRICS_COMMENT = new Rq1XmlSubField_Text(this, ASPICE, "BbmMetricsComment"));
        addField(BBM_METRICS_NEEDED_MANUAL_OVERRIDE = new Rq1XmlSubField_Enumeration(this, ASPICE, "BbmMetricsNeededManualOverride", BbmMetricsNeededManualOverride.values(), BbmMetricsNeededManualOverride.EMPTY));
        addField(BBM_METRICS_INSTALLED_MANUAL_OVERRIDE = new Rq1XmlSubField_Enumeration(this, ASPICE, "BbmMetricsInstalledManualOverride", BbmMetricsNeededManualOverride.values(), BbmMetricsNeededManualOverride.EMPTY));

        addField(TEMPLATES = new Rq1DatabaseField_Text(this, "Templates"));
        TEMPLATES.setReadOnly();
        addField(HAS_PROJECT_MEMBERS = new Rq1DatabaseField_MappedReferenceList(this, "hasProjectMembers", "LinkedUser", Rq1RecordType.USER_ROLE, Rq1RecordType.USER));
        addField(PRODUCTS = new Rq1DatabaseField_Products(this, "Products"));

        addField(PROJECT_LEADER = new Rq1DatabaseField_Reference(this, "ProjectLeader", Rq1RecordType.USER));
        addField(PROJECT_LEADER_FULLNAME = new Rq1DatabaseField_Text(this, "ProjectLeader.fullname"));
        addField(PROJECT_LEADER_EMAIL = new Rq1DatabaseField_Text(this, "ProjectLeader.email"));
        addField(PROJECT_LEADER_LOGIN_NAME = new Rq1DatabaseField_Text(this, "ProjectLeader.login_name"));

        PROJECT_LEADER_FULLNAME.setNoWriteBack().setOptional();
        PROJECT_LEADER_LOGIN_NAME.setNoWriteBack().setOptional();
        PROJECT_LEADER_EMAIL.setNoWriteBack().setOptional();

        addField(COMPANY_DATA_ID = new Rq1XmlSubField_Enumeration(this, TAGS, "COMPANY_DATA_ID", CompanyDataId.values(), CompanyDataId.EMPTY));
        COMPANY_DATA_ID.acceptInvalidValuesInDatabase();

        //
        // Issues
        //
        addField(ALL_ISSUES = new Rq1DatabaseField_ReferenceList(this, FIELDNAME_ISSUES, Rq1RecordType.ISSUE));
        addField(OPEN_ISSUES = new Rq1QueryField_belongsToProject(this, "OpenIssues", Rq1RecordType.ISSUE));
        OPEN_ISSUES.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_Issue.getAllOpenState());

        addField(ALL_ISSUE_ECU = new Rq1ReferenceListField_FilterByClass(this, ALL_ISSUES, Rq1IssueHwEcu.class));
        addField(ALL_ISSUE_MOD = new Rq1ReferenceListField_FilterByClass(this, ALL_ISSUES, Rq1IssueMod.class));
        addField(OPEN_ISSUE_ECU = new Rq1ReferenceListField_FilterByClass(this, OPEN_ISSUES, Rq1IssueHwEcu.class));
        addField(OPEN_ISSUE_MOD = new Rq1ReferenceListField_FilterByClass(this, OPEN_ISSUES, Rq1IssueMod.class));

        //
        // Releases
        //
        addField(ALL_RELEASES = new Rq1DatabaseField_ReferenceList(this, ATTRIBUTE_HAS_RELEASES, Rq1RecordType.RELEASE));
        addField(OPEN_RELEASES = new Rq1QueryField_belongsToProject(this, "OpenReleases", Rq1RecordType.RELEASE));
        OPEN_RELEASES.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_Release.getAllOpenState());

        addField(ALL_MILESTONES = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1Milestone.class));
        addField(OPEN_MILESTONES = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1Milestone.class));

        addField(ALL_HW_ECU_RELEASES = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1EcuRelease.class));
        addField(ALL_HW_MOD_RELEASES = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1ModRelease.class));
        addField(ALL_HW_COMP_RELEASES = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1CompRelease.class));
        addField(ALL_BUNDLES = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1BundleRelease.class));

        addField(OPEN_HW_ECU_RELEASES = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1EcuRelease.class));
        addField(OPEN_HW_MOD_RELEASES = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1ModRelease.class));
        addField(OPEN_HW_COMP_RELEASES = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1CompRelease.class));
        addField(OPEN_BUNDLES = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1BundleRelease.class));

        //
        // Collections
        //
        addField(OPEN_PST_COLLECTIONS = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1PstCollectionI.class));
        addField(ALL_PST_COLLECTIONS = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1PstCollectionI.class));

        //
        // excITED CPC
        //
        addField(EXCITEDCPC = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "excITEDCPC"));
        EXCITEDCPC.setOptional();
        addField(EXCITEDCPC_TEAM = new Rq1XmlSubField_Text(this, EXCITEDCPC, "TEAM"));
        EXCITEDCPC_TEAM.setOptional();
        addField(EXCITEDCPC_GROUP = new Rq1XmlSubField_Text(this, EXCITEDCPC, "GROUP"));
        EXCITEDCPC_GROUP.setOptional();
        addField(EXCITEDCPC_DEPARTMENTMAP = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_ExcItedDepartmentMap(), EXCITEDCPC, "DEPARTMENTMAP"));
        EXCITEDCPC_DEPARTMENTMAP.setOptional();

        //
        // GPM - Guided project management
        //
        addField(GPM = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "GPM"));
        GPM.setOptional();
        addField(IMPACT_CATEGORY = new Rq1XmlSubField_Enumeration(this, GPM, "ImpactCategory", ImpactCategory.values(), ImpactCategory.EMPTY));
        IMPACT_CATEGORY.acceptInvalidValuesInDatabase();
        IMPACT_CATEGORY.setOptional();
        IMPACT_CATEGORY.addAlternativName("IMPACT_CATEGORY");
        addField(GPM_USER_ROLES = new Rq1XmlSubField_Table<>(this, new GpmXmlTable_GpmUserRoles(this), GPM, "UserRole"));
        GPM_USER_ROLES.addAlternativName("UserRoles");
        GPM_USER_ROLES.setOptional();
        addField(MILESTONES_IN_TAGS = new Rq1XmlSubField_Xml(this, TAGS, "Milestones"));
        MILESTONES_IN_TAGS.setOptional();
        addField(TASKS_ON_MILESTONES = new Rq1XmlSubField_Table<>(this, new GpmXmlTable_TasksOnMilestones(), MILESTONES_IN_TAGS, "Task"));
        addField(DATA_OF_EXTERNAL_MILESTONES_IN_TAGS = new Rq1XmlSubField_Xml(this, TAGS, "ExternalMilestones"));
        DATA_OF_EXTERNAL_MILESTONES_IN_TAGS.setOptional();
        addField(DATA_OF_EXTERNAL_MILESTONES = new Rq1XmlSubField_Table<>(this, new GpmXmlTable_DataOfExternalMilestones(), DATA_OF_EXTERNAL_MILESTONES_IN_TAGS, "ExternalMilestone"));

        addField(CPC_DNG = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_DNG(), GPM, "DNG"));
        CPC_DNG.setOptional();
        addField(CPC_DOORS = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_DOORS(), GPM, "DOORS"));
        CPC_DOORS.setOptional();
        addField(CPC_ENTERPRISE_DASHBOARD = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_EnterpriseDashboard(), GPM, "EnterpriseDashboard"));
        CPC_ENTERPRISE_DASHBOARD.addAlternativName("Promotion");
        CPC_ENTERPRISE_DASHBOARD.setOptional();
        addField(CPC_FMEA_DB = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_FMEA_DB(), GPM, "FMEA-DB"));
        CPC_FMEA_DB.setOptional();
        addField(CPC_HIS = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_HIS(), GPM, "EcuHwId"));
        CPC_HIS.setOptional();
        addField(CPC_iCDM = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_iCDM(), GPM, "iCDM"));
        CPC_iCDM.addAlternativName("iCDM_Id");
        CPC_iCDM.setOptional();
        addField(CPC_iGPM_MCR_ECU = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_iGPM_MCR_ECU(), GPM, "iGPM-MCR-ECU"));
        CPC_iGPM_MCR_ECU.setOptional();
        addField(CPC_iGPM_MCR_System_Parent_Project = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_iGPM_MCR_System_Parent_Project(), GPM, "iGPM-MCR"));
        CPC_iGPM_MCR_System_Parent_Project.addAlternativName("McrPlanId");
        CPC_iGPM_MCR_System_Parent_Project.setOptional();
        addField(CPC_iGPM_ONE_Q = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_iGPM_oneQ(), GPM, "OneQ"));
        CPC_iGPM_ONE_Q.setOptional();
        addField(CPC_iGPM_QUO = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_iGPM_QUO(), GPM, "iGPM-QUO"));
        CPC_iGPM_QUO.setOptional();
        addField(CPC_ILM_AT_SHAREPOINT = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_ILM_AT_Sharepoint(), GPM, "ILM_SP"));
        CPC_ILM_AT_SHAREPOINT.setOptional();
        addField(CPC_NT_SHARE = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_NT_Share(), GPM, "NT-Share"));
        CPC_NT_SHARE.setOptional();
        addField(CPC_PROVISOR2 = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_Provisor2(), GPM, "Provisor2"));
        CPC_PROVISOR2.setOptional();
        addField(CPC_RQ1 = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_RQ1(), GPM, "RQ1"));
        CPC_RQ1.setOptional();
        addField(CPC_SMART_WEB = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_SMARTweb(), GPM, "SMARTweb"));
        CPC_SMART_WEB.setOptional();
        addField(CPC_SUPER_OPL = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_SuperOPL(), GPM, "SuperOPL"));
        CPC_SUPER_OPL.setOptional();
        addField(CPC_SVN = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_SVN(), GPM, "SVN"));
        CPC_SVN.setOptional();
        addField(CPC_TRACK_AND_RELEASE = new Rq1XmlSubField_Table<>(this, new CpcXmlTable_TrackAndRelease(), GPM, "TnR"));
        CPC_TRACK_AND_RELEASE.setOptional();

        //
        //IPE-FLOW
        //
        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        addField(FLOW_VERSION = new Rq1XmlSubField_Text(this, FLOW, "V"));
        addField(FLOW_CONTRACTED_TEAM = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_ContractedTeam(), FLOW, "CONT_TEAM"));
        addField(FLOW_TEAM_NAME = new Rq1XmlSubField_Text(this, FLOW, "TEAM_NAME"));
        addField(FLOW_CONTRACTED_PROJECTS = new Rq1XmlSubField_Text(this, FLOW, "CONTRACT_PRJ"));
        addField(FLOW_CODEX_PROJECTS = new Rq1XmlSubField_Text(this, FLOW, "CODEX_PRJ"));
        addField(CLUSTER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_ClusterConfiguration(), FLOW, "CLUSTER"));
        addField(ACCESS_GRANTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_AccessGrantConfiguration(), FLOW, "ACC_GRNT"));
        addField(PO_ACC_GRNT = new Rq1XmlSubField_Text(this, FLOW, "PO_ACC_GRNT"));
        addField(FLOW_RESTRICT_BC = new Rq1XmlSubField_Enumeration(this, FLOW, "EXCLUDE_BC", RestrictBc.values(), null));
        addField(FLOW_RESTRICT_HW_MOD = new Rq1XmlSubField_Enumeration(this, FLOW, "EXCLUDE_HW_MOD", RestrictBc.values(), null));
        addField(RELATIVE_PROJECT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RelativeProjectConfiguration(), FLOW, "RELATED_PRJ"));
        addField(RELATIVE_PL = new Rq1XmlSubField_Text(this, FLOW, "RELATIVE_PL"));
        addField(FLOW_BOARD_WI_MODE = new Rq1XmlSubField_Enumeration(this, FLOW, "FB_WI_MODE", BoardWiMode.values(), null));
        addField(FLOW_TEAM_LEADER = new Rq1XmlSubField_Text(this, FLOW, "TEAM_LEADER"));
        addField(FLOW_FETCH_RQ1_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "FETCH_RQ1_EFF"));
        addField(FLOW_BOARD_VIEW_TYPE = new Rq1XmlSubField_Text(this, FLOW, "BOARD_VIEW_TYPE"));
        addField(FLOW_SHOW_IN_REVIEW_COLUMN = new Rq1XmlSubField_Text(this, FLOW, "SHOW_IN_REVIEW"));
        addField(FLOW_CONSIDER_PLAN_DATE = new Rq1XmlSubField_Text(this, FLOW, "CONSIDER_PLAN_DT"));
        addField(FLOW_CONSIDER_IRM_CHANGES = new Rq1XmlSubField_Text(this, FLOW, "IGNORE_IFD"));
        addField(FLOW_SPRINT_INFO = new Rq1XmlSubField_Text(this, FLOW, "SPRINT_INFO"));

        FLOW.setOptional();
        FLOW_CONTRACTED_TEAM.setOptional();
        FLOW_TEAM_NAME.setOptional();
        ACCESS_GRANTS.setOptional();
        PO_ACC_GRNT.setOptional();
        FLOW_RESTRICT_BC.setOptional();
        FLOW_RESTRICT_HW_MOD.setOptional();
        FLOW_BOARD_WI_MODE.setOptional();
        FLOW_TEAM_LEADER.setOptional();
        FLOW_FETCH_RQ1_EFFORT.setOptional();
        FLOW_BOARD_VIEW_TYPE.setOptional();
        FLOW_SHOW_IN_REVIEW_COLUMN.setOptional();
        FLOW_CONSIDER_PLAN_DATE.setOptional();
        FLOW_CONSIDER_IRM_CHANGES.setOptional();
        FLOW_SPRINT_INFO.setOptional();

        //
        //IPE-CRP
        //
        addField(CRP = new Rq1XmlSubField_Xml(this, TAGS, "CRP"));
        addField(CRP_VERSION = new Rq1XmlSubField_Text(this, CRP, "CRP_V"));
        addField(CRP_CLUSTER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CrpSubTeamConfiguration(), CRP, "CRP_CLUSTER"));
        addField(CRP_INCLUDE_TASK_OUTSIDE_PRJ = new Rq1XmlSubField_Text(this, CRP, "INC_OUT_TASK"));
        addField(CRP_PROMOTE_RED_TASK = new Rq1XmlSubField_Text(this, CRP, "PROMOTE_RED_TASK"));
        addField(CRP_LAST_CLEANUP_DT = new Rq1XmlSubField_Text(this, CRP, "CRP_LAST_CLEANUP_DT"));
        addField(CRP_COMBINED_PROJECTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CrpCombinedProjects(), CRP, "CRP_COMBINED"));

        CRP_COMBINED_PROJECTS.setOptional();

        //
        // OrgChart
        //
        addField(GPM_ORG_DATA = new Rq1XmlSubField_Xml(this, GPM, ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "OrgData"));
        GPM_ORG_DATA.setOptional();
        GPM_ORG_DATA.setBounds(0, 1);

        //
        // Hierarchie
        //
        addField(HAS_CHILDREN = new Rq1DatabaseField_ReferenceList(this, "hasChildren", Rq1RecordType.PROJECT));
        addField(HAS_POOL_PROJECT_MEMBERS = new Rq1DatabaseField_ReferenceList(this, "hasPoolProjectMembers", Rq1RecordType.PROJECT));

        //
        // Workitems
        //
        addField(ALL_WORKITEMS = new Rq1DatabaseField_ReferenceList(this, "hasWorkitems", Rq1RecordType.WORKITEM));
        addField(OPEN_WORKITEMS = new Rq1QueryField_belongsToProject(this, ATTRIBUTE_OPEN_WORKITEMS.getName(), Rq1NodeDescription.WORKITEM.getRecordType()));
        OPEN_WORKITEMS.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_WorkItem.getAllOpenState());

        //
        // Problems
        //
        addField(ALL_PROBLEMS = new Rq1DatabaseField_ReferenceList(this, "hasProblems", Rq1RecordType.PROBLEM));
        addField(OPEN_PROBLEMS = new Rq1QueryField_belongsToProject(this, "OpenProblem", Rq1RecordType.PROBLEM));
        OPEN_PROBLEMS.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_Problem.getAllOpenState());

        addField(TUL_ACTIVE = new Rq1XmlSubField_Text(this, TAGS, "TULactive"));
        TUL_ACTIVE.setOptional();

        addField(IPE = new Rq1XmlSubField_Text(this, TAGS, "IPE"));
        IPE.setOptional();

    }

    /**
     * Returns a list of all active projects.
     *
     * @return
     */
    static public List<Rq1Project> getOpenProjectList() {
        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1RecordType.PROJECT);
        query.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, EnumSet.of(LifeCycleState_Project.NEW, LifeCycleState_Project.OPEN));

        //
        // Execute query and build result
        //
        List<Rq1Project> result = new ArrayList<>();
        for (Rq1Reference ref : query.getReferenceList(true)) {
            assert (ref.getRecord() instanceof Rq1Project) : ref.getRecord().getId();
            result.add((Rq1Project) ref.getRecord());
        }
        return (result);
    }

    //--------------------------------------------------------------------------
    //
    // Support for Templates
    //
    //--------------------------------------------------------------------------
    private Rq1ProjectTemplateDefinition templateDefinition = null;

    public Rq1ProjectTemplateDefinition getTemplateDefinition(Rq1RecordType recordType, String user) {
        assert (recordType != null);
        assert (user != null);
        assert (user.isEmpty() == false);

        if (templateDefinition == null) {
            templateDefinition = Rq1ProjectTemplateFactory.parseTemplateDefinition(getId(), TEMPLATES.getDataModelValue());
        }

        List<Rq1ProjectTemplate> result = new ArrayList<>();
        for (Rq1ProjectTemplate template : templateDefinition.getTemplates()) {
            if (template.match(recordType, user) == true) {
                result.add(template);
            }
        }

        List<Rq1ProjectTemplateSet> sets = new ArrayList<>();
        for (Rq1ProjectTemplateSet set : templateDefinition.getTemplateSets()) {
            if (set.match(recordType, user) == true) {
                sets.add(set);
            }
        }

        return new Rq1ProjectTemplateDefinition(sets, result);
    }

    @Override
    final public void reload() {
        templateDefinition = null;
        HAS_PROJECT_MEMBERS.reload();
        super.reload();
    }
}
