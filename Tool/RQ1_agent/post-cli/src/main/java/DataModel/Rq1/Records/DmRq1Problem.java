/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmElementListField_ReadOnlyFromSource;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_EnumerationSet;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnIssue;
import DataModel.Rq1.Requirements.DmRq1Field_DngRequirementsOnIssuesFromExternalLinks;
import DataModel.Rq1.Requirements.DmRq1Field_DoorsRequirementsOnIssueFromTables;
import DataModel.Rq1.Requirements.DmRq1LinkToRequirement_Type;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1Problem;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIssue;
import Rq1Data.Enumerations.LifeCycleState_Problem;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import util.EcvEnumeration;
import util.MailSendable;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Problem extends DmRq1AssignedRecord implements MailSendable {

    final public DmRq1Field_EnumerationSet AFFECTED_ECU_GENERATIONS;
    final public DmRq1Field_Enumeration ALERT_NOTIFICATION;
    final public DmRq1Field_Text CALIBRATION_DATA;
    final public DmRq1Field_Text DEFECT_ID;
    final public DmRq1Field_Text DEPENDENCIES;
    final public DmRq1Field_Text DEVELOPMENT_ENVIRONMENT;
    final public DmRq1Field_Enumeration DOMAIN;
    final public DmRq1Field_Date DUE_DATE;
    final public DmRq1Field_Text ECU_HARDWARE;
    final public DmRq1Field_EnumerationSet EFFECT_OF_THE_DEFECT;
    final public DmRq1Field_Text EXTERNAL_DESCRIPTION;
    final public DmRq1Field_Text EXTERNAL_HISTORY;
    final public DmRq1Field_Text EXTERNAL_ID;
    final public DmRq1Field_Text ERROR_DESCRIPTION;
    final public DmRq1Field_Text EXTERNAL_ECU_CALIBRATION;
    final public DmRq1Field_Text EXTERNAL_ECU_SOFTWARE;
    final public DmRq1Field_Text FINAL_RESOLUTION;
    final public DmRq1Field_Text HARDWARE;
    final public DmRq1Field_Text ID;
    final public DmRq1Field_Text ID_Q_CENTRAL_ORGANIZATION;
    final public DmRq1Field_Text IMPACT_ANALYSIS_RESULT;
    final public DmRq1Field_Text INSERT_STATEMENT;
    final public DmRq1Field_Text INTERNAL_ECU_CALIBRATION;
    final public DmRq1Field_Text INTERNAL_ECU_SOFTWARE;
    final public DmRq1Field_Enumeration ISSUER_CLASS;
    final public DmRq1Field_Text IS_DUPLICATE;
    final public DmRq1Field_Text ISSUED_BY;
    final public DmRq1Field_Enumeration JUSTIFICATION;
    final public DmRq1Field_Enumeration LIFE_CYCLE_STATE;
    final public DmRq1Field_Text OTHER_COMPONENTS;
    final public DmRq1Field_Text PRIMARY_ROOT_CAUSE;
    final public DmRq1Field_Enumeration PRIORITY;
    final public DmRq1Field_Text PROBLEM_REPRODUCTION;
    final public DmRq1Field_Text RECOMMENDATIONS;
    final public DmRq1Field_Text REPORT_ID_8D;
    final public DmRq1Field_Text ROOT_CAUSE_ANALYSIS_RESULT;
    final public DmRq1Field_Enumeration SEVERITY;
    final public DmRq1Field_Text STATE;
    final public DmRq1Field_Text SYSTEM_CONSTANTS;
    final public DmRq1Field_Text TESTING_ENVIRONMENT;
    final public DmRq1Field_Enumeration TYPE;
    final public DmRq1Field_Enumeration URGENT_ALERT_NOTIFICATION_QMM;
    final public DmRq1Field_Text URGENT_ALERT_NOTIFICATION_COMMENT;
    final public DmRq1Field_Text URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT;
    final public DmRq1Field_Text URGENT_RESOLUTION;
    final public DmRq1Field_Enumeration VERSION_HISTORY;

    final public DmRq1Field_Reference<DmRq1User> CONTACT_INFORMATION;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> WORKITEMS;
    final public DmRq1Field_Reference<DmRq1Problem> DUPLICATE_OF;
    final public DmRq1Field_ReferenceList<DmRq1Problem> HAS_DUPLICATES;
    final public DmRq1Field_ReferenceList<DmRq1Issue> ISSUES;
    final public DmElementListField_ReadOnlyFromSource<DmRq1ExternalLink> EXTERNAL_LINKS_FROM_DUPLICATES;

    //
    // Fields for DOORS requirements
    //
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L1_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L1_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L2_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L2_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L2_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L3_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L3_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L3_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L4_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L4_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> L4_REMOVED_REQUIREMENT_TABLE;

    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> MO_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> MO_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> MO_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> SC_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> SC_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> SC_REMOVED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_REMOVED_REQUIREMENT_TABLE;

    final public DmRq1Field_DoorsRequirementsOnIssueFromTables MAPPED_DOORS_REQUIREMENTS;
    final public DmRq1Field_DngRequirementsOnIssuesFromExternalLinks MAPPED_DNG_REQUIREMENTS;
    final public DmRq1Field_AllRequirementsOnIssue MAPPED_REQUIREMENTS;

    private DmRq1Issue issueToSave = null;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Problem(Rq1Problem rq1Problem) {
        super("Problem", rq1Problem);

        addField(AFFECTED_ECU_GENERATIONS = new DmRq1Field_EnumerationSet(rq1Problem.AFFECTED_ECU_GENERATIONS, "Affected ECU Generations"));
        addField(ALERT_NOTIFICATION = new DmRq1Field_Enumeration(rq1Problem.ALERT_NOTIFICATION, "Alert Notification"));
        addField(CALIBRATION_DATA = new DmRq1Field_Text(rq1Problem.CALIBRATION_DATA, "Calibration Data"));
        addField(DEFECT_ID = new DmRq1Field_Text(rq1Problem.DEFECT_ID, "Defect ID"));
        addField(DEPENDENCIES = new DmRq1Field_Text(rq1Problem.DEPENDENCIES, "Dependencies"));
        addField(DEVELOPMENT_ENVIRONMENT = new DmRq1Field_Text(rq1Problem.DEVELOPMENT_ENVIRONMENT, "Development Environment"));
        addField(DOMAIN = new DmRq1Field_Enumeration(rq1Problem.DOMAIN, "Domain"));
        addField(DUE_DATE = new DmRq1Field_Date(rq1Problem.DUE_DATE, "Due Date"));
        addField(ECU_HARDWARE = new DmRq1Field_Text(rq1Problem.ECU_HARDWARE, "ECU-Hardware"));
        addField(EFFECT_OF_THE_DEFECT = new DmRq1Field_EnumerationSet(rq1Problem.EFFECT_OF_THE_DEFECT, "Effect of the Defect"));
        addField(EXTERNAL_DESCRIPTION = new DmRq1Field_Text(rq1Problem.EXTERNAL_DESCRIPTION, "External Description"));
        addField(EXTERNAL_HISTORY = new DmRq1Field_Text(rq1Problem.EXTERNAL_HISTORY, "External History"));
        addField(EXTERNAL_ID = new DmRq1Field_Text(rq1Problem.EXTERNAL_ID, "External ID"));
        addField(ERROR_DESCRIPTION = new DmRq1Field_Text(rq1Problem.ERROR_DESCRIPTION, "Error Description EXTERNAL"));
        addField(EXTERNAL_ECU_CALIBRATION = new DmRq1Field_Text(rq1Problem.EXTERNAL_ECU_CALIBRATION, "External ECU-Calibration"));
        addField(EXTERNAL_ECU_SOFTWARE = new DmRq1Field_Text(rq1Problem.EXTERNAL_ECU_SOFTWARE, "External ECU-Software"));
        addField(FINAL_RESOLUTION = new DmRq1Field_Text(rq1Problem.FINAL_RESOLUTION, "Final Resolution"));
        addField(HARDWARE = new DmRq1Field_Text(rq1Problem.HARDWARE, "Hardware"));
        addField(ID = new DmRq1Field_Text(rq1Problem.ID, "ID"));
        addField(ID_Q_CENTRAL_ORGANIZATION = new DmRq1Field_Text(rq1Problem.ID_Q_CENTRAL_ORGANIZATION, "ID Q-/CentralOrganization"));
        addField(IMPACT_ANALYSIS_RESULT = new DmRq1Field_Text(rq1Problem.IMPACT_ANALYSIS_RESULT, "Impact Analysis Result"));
        addField(INSERT_STATEMENT = new DmRq1Field_Text(rq1Problem.INSERT_STATEMENT, "Insert Statement (for high priority, which is blocking)"));
        addField(INTERNAL_ECU_CALIBRATION = new DmRq1Field_Text(rq1Problem.INTERNAL_ECU_CALIBRATION, "Internal ECU-Calibration"));
        addField(INTERNAL_ECU_SOFTWARE = new DmRq1Field_Text(rq1Problem.INTERNAL_ECU_SOFTWARE, "Internal ECU-Software"));
        addField(IS_DUPLICATE = new DmRq1Field_Text(rq1Problem.IS_DUPLICATE, "Is Duplicate"));
        addField(ISSUED_BY = new DmRq1Field_Text(rq1Problem.ISSUED_BY, "Issued By"));
        addField(ISSUER_CLASS = new DmRq1Field_Enumeration(rq1Problem.ISSUER_CLASS, "Issuer Class"));
        addField(JUSTIFICATION = new DmRq1Field_Enumeration(rq1Problem.JUSTIFICATION, "Justification"));
        addField(LIFE_CYCLE_STATE = new DmRq1Field_Enumeration(rq1Problem.LIFE_CYCLE_STATE, "Life Cycle State"));
        addField(OTHER_COMPONENTS = new DmRq1Field_Text(rq1Problem.OTHER_COMPONENTS, "Other Components"));
        addField(PRIMARY_ROOT_CAUSE = new DmRq1Field_Text(rq1Problem.PRIMARY_ROOT_CAUSE, "Primary Root Cause"));
        PRIMARY_ROOT_CAUSE.addAlternativeField(rq1Problem.PRIMARY_ROOT_CAUSE_FROM_BACKGROUND);
        addField(PRIORITY = new DmRq1Field_Enumeration(rq1Problem.PRIORITY, "Priority"));
        addField(PROBLEM_REPRODUCTION = new DmRq1Field_Text(rq1Problem.PROBLEM_REPRODUCTION, "Problem Reproduction"));
        addField(RECOMMENDATIONS = new DmRq1Field_Text(rq1Problem.RECOMMENDATIONS, "Recommendations for Projects (CustPrj) to handle this Problem"));
        addField(REPORT_ID_8D = new DmRq1Field_Text(rq1Problem.REPORT_ID_8D, "Report ID 8D"));
        addField(ROOT_CAUSE_ANALYSIS_RESULT = new DmRq1Field_Text(rq1Problem.ROOT_CAUSE_ANALYSIS_RESULT, "Root Cause Analysis Result"));
        addField(SEVERITY = new DmRq1Field_Enumeration(rq1Problem.SEVERITY, "Severity"));
        addField(STATE = new DmRq1Field_Text(rq1Problem.STATE, "State"));
        addField(SYSTEM_CONSTANTS = new DmRq1Field_Text(rq1Problem.SYSTEM_CONSTANTS, "System Constants"));
        addField(TESTING_ENVIRONMENT = new DmRq1Field_Text(rq1Problem.TESTING_ENVIRONMENT, "Testing Environment"));
        addField(TYPE = new DmRq1Field_Enumeration(rq1Problem.TYPE, "Type"));
        addField(URGENT_ALERT_NOTIFICATION_QMM = new DmRq1Field_Enumeration(rq1Problem.URGENT_ALERT_NOTIFICATION_QMM, "Urgent Alert Notification from QMM"));
        addField(URGENT_ALERT_NOTIFICATION_COMMENT = new DmRq1Field_Text(this, rq1Problem.URGENT_ALERT_NOTIFICATION_COMMENT, "Urgent Alert Notification Comment"));
        addField(URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Problem.URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT, "Urgent Alert Notification Change Comment"));
        addField(URGENT_RESOLUTION = new DmRq1Field_Text(rq1Problem.URGENT_RESOLUTION, "Urgent Resolution"));
        addField(VERSION_HISTORY = new DmRq1Field_Enumeration(rq1Problem.VERSION_HISTORY, "Version History checked"));

        addField(CONTACT_INFORMATION = new DmRq1Field_Reference<>(this, rq1Problem.CONTACT_INFORMATION, "Contact for Information"));
        addField(WORKITEMS = new DmRq1Field_ReferenceList<>(this, rq1Problem.HAS_WORKITEMS, "Workitems"));
        addField(DUPLICATE_OF = new DmRq1Field_Reference<>(this, rq1Problem.DUPLICATE_OF, "Duplicate of"));
        addField(HAS_DUPLICATES = new DmRq1Field_ReferenceList<>(this, rq1Problem.HAS_DUPLICATES, "Duplicates"));
        addField(ISSUES = new DmRq1Field_ReferenceList<>(this, rq1Problem.HAS_ISSUES, "Issues"));

        addField(EXTERNAL_LINKS_FROM_DUPLICATES = new DmElementListField_ReadOnlyFromSource<DmRq1ExternalLink>("External Links From Duplicates"){
            @Override
            protected Collection<DmRq1ExternalLink> loadElementList() {
                addDependency(HAS_DUPLICATES);
                var list = new ArrayList<DmRq1ExternalLink>();
                for (DmRq1Problem duplicateProblem : HAS_DUPLICATES.getElementList()) {
                    list.addAll(duplicateProblem.HAS_EXTERNAL_LINKS.getElementList());
                    addDependency(duplicateProblem.HAS_EXTERNAL_LINKS);
                }
                return (list);
            }
        }); 

        //
        // Fields for DOORS requirements
        //
        addField(L1_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L1_PLANNED_REQUIREMENTS, "L1 Planned Requirements "));  // Blank at the end of the name to differ it from the list below.
        addField(L1_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L1_REMOVED_REQUIREMENTS, "L1 Removed Requirements "));
        addField(L2_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L2_IMPACTED_REQUIREMENTS, "L2 Impacted Requirements "));
        addField(L2_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L2_PLANNED_REQUIREMENTS, "L2 Planned Requirements "));
        addField(L2_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L2_REMOVED_REQUIREMENTS, "L2 Removed Requirements "));
        addField(L3_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L3_IMPACTED_REQUIREMENTS, "L3 Impacted Requirements "));
        addField(L3_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L3_PLANNED_REQUIREMENTS, "L3 Planned Requirements "));
        addField(L3_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L3_REMOVED_REQUIREMENTS, "L3 Removed Requirements "));
        addField(L4_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L4_IMPACTED_REQUIREMENTS, "L4 Impacted Requirements "));
        addField(L4_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L4_PLANNED_REQUIREMENTS, "L4 Planned Requirements "));
        addField(L4_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.L4_REMOVED_REQUIREMENTS, "L4 Removed Requirements "));

        addField(STAKEHOLDER_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.STAKEHOLDER_PLANNED_REQUIREMENTS, "Stakeholder Planned Requirements "));
        addField(STAKEHOLDER_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.STAKEHOLDER_REMOVED_REQUIREMENTS, "Stakeholder Removed Requirements "));
        addField(MO_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.MO_IMPACTED_REQUIREMENTS, "MO Impacted Requirements "));
        addField(MO_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.MO_PLANNED_REQUIREMENTS, "MO Planned Requirements "));
        addField(MO_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.MO_REMOVED_REQUIREMENTS, "MO Removed Requirements "));
        addField(SC_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.SC_IMPACTED_REQUIREMENTS, "SC Impacted Requirements "));
        addField(SC_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.SC_PLANNED_REQUIREMENTS, "SC Planned Requirements "));
        addField(SC_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.SC_REMOVED_REQUIREMENTS, "SC Removed Requirements "));
        addField(BC_FC_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.BC_FC_IMPACTED_REQUIREMENTS, "BC-FC Impacted Requirements "));
        addField(BC_FC_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.BC_FC_PLANNED_REQUIREMENTS, "BC-FC Planned Requirements "));
        addField(BC_FC_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Problem.BC_FC_REMOVED_REQUIREMENTS, "BC-FC Removed Requirements "));

        addField(MAPPED_DOORS_REQUIREMENTS = new DmRq1Field_DoorsRequirementsOnIssueFromTables(this, "Mapped Doors Requirements"));
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L1_PLANNED, L1_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L1_REMOVED, L1_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_IMPACTED, L2_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_PLANNED, L2_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_REMOVED, L2_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L3_IMPACTED, L3_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L3_PLANNED, L3_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L3_REMOVED, L3_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L4_IMPACTED, L4_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L4_PLANNED, L4_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L4_REMOVED, L4_REMOVED_REQUIREMENT_TABLE);

        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.STAKEHOLDER_PLANNED, STAKEHOLDER_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.STAKEHOLDER_REMOVED, STAKEHOLDER_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_IMPACTED, MO_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_PLANNED, MO_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_REMOVED, MO_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.SC_IMPACTED, SC_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.SC_PLANNED, SC_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.SC_REMOVED, SC_REMOVED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.BC_FC_IMPACTED, BC_FC_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.BC_FC_PLANNED, BC_FC_PLANNED_REQUIREMENT_TABLE);
        MAPPED_DOORS_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.BC_FC_REMOVED, BC_FC_REMOVED_REQUIREMENT_TABLE);

        addField(MAPPED_DNG_REQUIREMENTS = new DmRq1Field_DngRequirementsOnIssuesFromExternalLinks(this, "DNG Requirements"));

        addField(MAPPED_REQUIREMENTS = new DmRq1Field_AllRequirementsOnIssue(this, MAPPED_DOORS_REQUIREMENTS, MAPPED_DNG_REQUIREMENTS, "Mapped Requirements"));

    }

    @Override
    final public boolean isCanceled() {
        return (LIFE_CYCLE_STATE.getValue() == LifeCycleState_Problem.CANCELED);
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Problem creation
    //
    //--------------------------------------------------------------------------
    static DmRq1Problem create() {
        Rq1Problem rq1Problem = new Rq1Problem();
        DmRq1Problem dmProblem = new DmRq1Problem(rq1Problem);
        DmRq1ElementCache.addElement(rq1Problem, dmProblem);
        return (dmProblem);
    }

    static public DmRq1Problem createBasedOnProject(DmRq1Project project, Rq1TemplateI template) {
        assert (project != null);

        DmRq1Problem problem = create();
        problem.PROJECT.setElement(project);
        if (project.OPEN_PROBLEMS.isLoaded()) {
            project.OPEN_PROBLEMS.addElement(problem);
        }
        if (project.ALL_PROBLEMS.isLoaded()) {
            project.ALL_PROBLEMS.addElement(problem);
        }

        if (template != null) {
            template.execute(problem);
        }

        return (problem);
    }

    static public DmRq1Problem createBasedOnIssue(DmRq1Issue issue, DmRq1Project project, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {
        assert (issue != null);
        assert (project != null);
        assert (suffixSet != null);

        DmRq1Problem problem = createBasedOnProject(project, template);

        problem.ISSUES.addElement(issue);
        issue.PROBLEM.setElement(problem);
        problem.issueToSave = issue;

        String suffix = TitleSuffix.generateSuffix(suffixSet, suffixSeparator, issue);
        if (suffix != null && suffix.isEmpty() == false) {
            problem.TITLE.setValue(problem.TITLE.getValue() + suffix);
        }

        return (problem);
    }

    public void addDuplicate(DmRq1Problem duplicateProblem) throws DmRq1MapExistsException {
        assert (duplicateProblem != null);

        for (DmRq1Problem mappedDuplicate : HAS_DUPLICATES.getElementList()) {
            if (mappedDuplicate == duplicateProblem) {
                throw (new DmRq1MapExistsException());
            }
        }

        DmRq1Problem oldDuplicateOf = duplicateProblem.DUPLICATE_OF.getElement();
        if (oldDuplicateOf != null) {
            oldDuplicateOf.HAS_DUPLICATES.removeElement(duplicateProblem);
        }
        HAS_DUPLICATES.addElement(duplicateProblem);
        duplicateProblem.DUPLICATE_OF.setElement(this);
    }
    
     /**
     * Support for sorting by external id.
     *
     * @param i2
     * @return
     */
    public int compareByExternalId(DmRq1Problem p2) {
        assert (p2 != null);

        String extId1 = EXTERNAL_ID.getValue();
        String extId2 = p2.EXTERNAL_ID.getValue();

        if ((extId1 != null) && (extId1.isEmpty() == false)) {
            if ((extId2 != null) && (extId2.isEmpty() == false)) {
                return (extId1.compareTo(extId2));
            } else {
                return (-1);
            }
        } else {
            if ((extId2 != null) && (extId2.isEmpty() == false)) {
                return (1);
            } else {
                return (getTitle().compareTo(p2.getTitle()));
            }
        }
    }

    //--------------------------------------------------------------------------
    //
    // Implement DmRq1ForwardableElementI
    //
    //--------------------------------------------------------------------------
    @Override
    public DmRq1Field_Reference<DmRq1User> getRequesterField() {
        return (null);
    }

    @Override
    public boolean save(List<Rq1AttributeName> fieldOrder) {
        if (super.save(fieldOrder) == true) {
            saveIssue();
            return (true);
        } else {
            return (false);
        }
    }

    @Override
    public boolean save(Rq1AttributeName... fieldOrder) {
        if (super.save(fieldOrder) == true) {
            saveIssue();
            return (true);
        } else {
            return (false);
        }
    }

    @Override
    public boolean save() {

        List<Rq1AttributeName> fieldList = null;
        fieldList = new ArrayList<>();
        
        if (LIFE_CYCLE_STATE.isValueEqualTo(LifeCycleState_Problem.CANCELED)) {
            
            fieldList.add(Rq1Problem.ATTRIBUTE_LIFE_CYCLE_STATE);
        }
        
        System.out.println("Rq1Problem.ATTRIBUTE_URGENT_ALERT_NOTIFICATION_QMM.getName(): " + Rq1Problem.ATTRIBUTE_URGENT_ALERT_NOTIFICATION_QMM.getName());
        fieldList.add(Rq1Problem.ATTRIBUTE_REPORT_ID_8D);
        fieldList.add(Rq1Problem.ATTRIBUTE_URGENT_ALERT_NOTIFICATION_QMM);
        fieldList.add(Rq1Problem.ATTRIBUTE_URGENT_ALERT_NOTIFICATION_COMMENT);
        fieldList.add(Rq1Problem.ATTRIBUTE_URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT);
        

        
        if (super.save(fieldList) == true) {
            saveIssue();
            return (true);
        } else {
            return (false);
        }
    }

    private void saveIssue() {
        if (issueToSave != null) {
            issueToSave.save();
            issueToSave = null;
        }
    }

    @Override
    public String getAssigneeMail() {
        return ASSIGNEE_EMAIL.getValueAsText();
    }

    @Override
    public String getProjectLeaderMail() {
       return null;
    }

    @Override
    public String getRequesterMail() {
        return null;
    }
    
    @Override
    public String getContactMail() {
        if(CONTACT_INFORMATION.getElement() != null)
        {
            return ((DmRq1User)(CONTACT_INFORMATION.getElement())).EMAIL.getValueAsText();
        }
        return null;
    }

    @Override
    public List<MailActionType> getActionName() {
        List<MailActionType> mailActionTypes = new ArrayList<>();
        mailActionTypes.add(MailActionType.ASSIGNEE);
        mailActionTypes.add(MailActionType.CONTACT);
        return mailActionTypes;
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTypeIdTitle();
    }

    @Override
    public String getIdForSubject() {
        return getId();
    }

}
