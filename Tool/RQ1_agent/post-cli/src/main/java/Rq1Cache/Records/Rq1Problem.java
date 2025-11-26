/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_EnumerationSet;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_ReferenceList;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIssue;
import Rq1Data.Enumerations.LifeCycleState_Problem;

/**
 *
 * @author GUG2WI
 */
public class Rq1Problem extends Rq1AssignedRecord {

    final public static Rq1AttributeName ATTRIBUTE_AFFECTED_ECU_GENERATIONS = new Rq1AttributeName("AffectedECUGenerations");
    final public static Rq1AttributeName ATTRIBUTE_ALERT_NOTIFICATION = new Rq1AttributeName("AlertNotification");
    final public static Rq1AttributeName ATTRIBUTE_CONTACT_INFORMATION = new Rq1AttributeName("ContactInformation");
    final public static Rq1AttributeName ATTRIBUTE_CONTACT_DOMAIN = new Rq1AttributeName("Domain");
    final static public Rq1AttributeName ATTRIBUTE_DEFECT_ID = new Rq1AttributeName("hasBackground", "DefectID");
    final public static Rq1AttributeName ATTRIBUTE_EFFECT_OF_THE_DEFECT = new Rq1AttributeName("EffectOfTheDefect");
    final public static Rq1AttributeName ATTRIBUTE_ISSUER_CLASS = new Rq1AttributeName("IssuerClass");
    final public static Rq1AttributeName ATTRIBUTE_JUSTIFICATION = new Rq1AttributeName("Justification");
    final static public Rq1AttributeName ATTRIBUTE_PRIMARY_ROOT_CAUSE = new Rq1AttributeName("PrimaryRootCause");
    final static public Rq1AttributeName ATTRIBUTE_PRIMARY_ROOT_CAUSE_FROM_BACKGROUND = new Rq1AttributeName("hasBackground", "PrimaryRootCause");
    final public static Rq1AttributeName ATTRIBUTE_PRIORITY = new Rq1AttributeName("Priority");
    final public static Rq1AttributeName ATTRIBUTE_REPORT_ID_8D = new Rq1AttributeName("ReportID8D");
    final public static Rq1AttributeName ATTRIBUTE_SEVERITY = new Rq1AttributeName("Severity");
    final public static Rq1AttributeName ATTRIBUTE_TYPE = new Rq1AttributeName("Type");
    final public static Rq1AttributeName ATTRIBUTE_URGENT_ALERT_NOTIFICATION_QMM = new Rq1AttributeName("UrgentAlertNotification");
    final public static Rq1AttributeName ATTRIBUTE_URGENT_ALERT_NOTIFICATION_COMMENT = new Rq1AttributeName("UrgentAlertNotification_Comment");
    final public static Rq1AttributeName ATTRIBUTE_URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT = new Rq1AttributeName("UrgentAlertNotification_ChangeComment");
    final public static Rq1AttributeName ATTRIBUTE_VERSION_HISTORY = new Rq1AttributeName("VersionHistory");

    final public Rq1DatabaseField_EnumerationSet AFFECTED_ECU_GENERATIONS;
    final public Rq1DatabaseField_Enumeration ALERT_NOTIFICATION;
    final public Rq1DatabaseField_Text CALIBRATION_DATA;
    final public Rq1DatabaseField_Text DEFECT_ID;
    final public Rq1DatabaseField_Text DEPENDENCIES;
    final public Rq1DatabaseField_Text DEVELOPMENT_ENVIRONMENT;
    final public Rq1DatabaseField_Enumeration DOMAIN;
    final public Rq1DatabaseField_Date DUE_DATE;
    final public Rq1DatabaseField_Text ECU_HARDWARE;
    final public Rq1DatabaseField_EnumerationSet EFFECT_OF_THE_DEFECT;
    final public Rq1DatabaseField_Text ERROR_DESCRIPTION;
    final public Rq1DatabaseField_Text EXTERNAL_DESCRIPTION;
    final public Rq1DatabaseField_Text EXTERNAL_ECU_CALIBRATION;
    final public Rq1DatabaseField_Text EXTERNAL_ECU_SOFTWARE;
    final public Rq1DatabaseField_Text EXTERNAL_HISTORY;
    final public Rq1DatabaseField_Text EXTERNAL_ID;
    final public Rq1DatabaseField_Text FINAL_RESOLUTION;
    final public Rq1DatabaseField_Text HARDWARE;
    final public Rq1DatabaseField_Text ID;
    final public Rq1DatabaseField_Text ID_Q_CENTRAL_ORGANIZATION;
    final public Rq1DatabaseField_Text IMPACT_ANALYSIS_RESULT;
    final public Rq1DatabaseField_Text INSERT_STATEMENT;
    final public Rq1DatabaseField_Text INTERNAL_ECU_CALIBRATION;
    final public Rq1DatabaseField_Text INTERNAL_ECU_SOFTWARE;
    final public Rq1DatabaseField_Text IS_DUPLICATE;
    final public Rq1DatabaseField_Text ISSUED_BY;
    final public Rq1DatabaseField_Enumeration ISSUER_CLASS;
    final public Rq1DatabaseField_Enumeration JUSTIFICATION;
    final public Rq1DatabaseField_Enumeration LIFE_CYCLE_STATE;
    final public Rq1DatabaseField_Text OTHER_COMPONENTS;
    final public Rq1DatabaseField_Text PRIMARY_ROOT_CAUSE;
    final public Rq1DatabaseField_Text PRIMARY_ROOT_CAUSE_FROM_BACKGROUND;
    final public Rq1DatabaseField_Enumeration PRIORITY;
    final public Rq1DatabaseField_Text PROBLEM_REPRODUCTION;
    final public Rq1DatabaseField_Text REPORT_ID_8D;
    final public Rq1DatabaseField_Text RECOMMENDATIONS;
    final public Rq1DatabaseField_Text ROOT_CAUSE_ANALYSIS_RESULT;
    final public Rq1DatabaseField_Enumeration SEVERITY;
    final public Rq1DatabaseField_Text STATE;
    final public Rq1DatabaseField_Text SYSTEM_CONSTANTS;
    final public Rq1DatabaseField_Enumeration TYPE;
    final public Rq1DatabaseField_Text TESTING_ENVIRONMENT;
    final public Rq1DatabaseField_Enumeration URGENT_ALERT_NOTIFICATION_QMM;
    final public Rq1XmlSubField_Text URGENT_ALERT_NOTIFICATION_COMMENT;
    final public Rq1XmlSubField_Text URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT;
    final public Rq1DatabaseField_Text URGENT_RESOLUTION;
    final public Rq1DatabaseField_Enumeration VERSION_HISTORY;

    final public Rq1DatabaseField_Reference DUPLICATE_OF;
    final public Rq1DatabaseField_ReferenceList HAS_DUPLICATES;
    final public Rq1DatabaseField_ReferenceList HAS_ISSUES;
    final public Rq1DatabaseField_ReferenceList HAS_WORKITEMS;
    final public Rq1DatabaseField_Reference CONTACT_INFORMATION;

    final public static String FIELDNAME_EXTERNAL_ID = "ExternalID";
    //
    // Fields for DOORS requirements
    //
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L1_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L1_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L2_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L2_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L2_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L3_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L3_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L3_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L4_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L4_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> L4_REMOVED_REQUIREMENTS;

    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> STAKEHOLDER_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> MO_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> MO_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> MO_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> SC_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> SC_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> SC_REMOVED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_IMPACTED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_PLANNED_REQUIREMENTS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RequirementOnIssue> BC_FC_REMOVED_REQUIREMENTS;

    public Rq1Problem() {
        super(Rq1NodeDescription.PROBLEM, ATTRIBUTE_DESCRIPTION);

        addField(AFFECTED_ECU_GENERATIONS = new Rq1DatabaseField_EnumerationSet(this, ATTRIBUTE_AFFECTED_ECU_GENERATIONS, ""));
        addField(ALERT_NOTIFICATION = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_ALERT_NOTIFICATION, "", ""));
        ALERT_NOTIFICATION.acceptInvalidValuesInDatabase();
        addField(CALIBRATION_DATA = new Rq1DatabaseField_Text(this, "CalibrationData"));
        addField(DEFECT_ID = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_ID));
        DEFECT_ID.setReadOnly();
        addField(CONTACT_INFORMATION = new Rq1DatabaseField_Reference(this, ATTRIBUTE_CONTACT_INFORMATION, Rq1RecordType.USER));
        addField(DEPENDENCIES = new Rq1DatabaseField_Text(this, "Dependencies"));
        addField(DEVELOPMENT_ENVIRONMENT = new Rq1DatabaseField_Text(this, "DevelopmentEnvironment"));
        addField(DOMAIN = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CONTACT_DOMAIN, "", ""));
        DOMAIN.acceptInvalidValuesInDatabase();
        addField(DUE_DATE = new Rq1DatabaseField_Date(this, "DueDate"));
        addField(DUPLICATE_OF = new Rq1DatabaseField_Reference(this, "duplicateOf", Rq1RecordType.PROBLEM));
        addField(ECU_HARDWARE = new Rq1DatabaseField_Text(this, "ECUHardware"));
        addField(EFFECT_OF_THE_DEFECT = new Rq1DatabaseField_EnumerationSet(this, ATTRIBUTE_EFFECT_OF_THE_DEFECT, ""));
        addField(ERROR_DESCRIPTION = new Rq1DatabaseField_Text(this, "ErrorDescription"));
        addField(EXTERNAL_DESCRIPTION = new Rq1DatabaseField_Text(this, "ExternalDescription"));
        addField(EXTERNAL_ECU_CALIBRATION = new Rq1DatabaseField_Text(this, "ExternalECUCalibration"));
        addField(EXTERNAL_ECU_SOFTWARE = new Rq1DatabaseField_Text(this, "ExternalECUSoftware"));
        addField(EXTERNAL_HISTORY = new Rq1DatabaseField_Text(this, "ExternalHistory"));
        addField(EXTERNAL_ID = new Rq1DatabaseField_Text(this, FIELDNAME_EXTERNAL_ID));        
        addField(FINAL_RESOLUTION = new Rq1DatabaseField_Text(this, "FinalResolution"));
        addField(HARDWARE = new Rq1DatabaseField_Text(this, "Hardware"));
        addField(HAS_DUPLICATES = new Rq1DatabaseField_ReferenceList(this, "hasDuplicates", Rq1RecordType.PROBLEM));
        addField(HAS_ISSUES = new Rq1DatabaseField_ReferenceList(this, "hasIssues", Rq1RecordType.ISSUE));
        addField(HAS_WORKITEMS = new Rq1DatabaseField_ReferenceList(this, "hasWorkitems", Rq1RecordType.WORKITEM));
        addField(ID = new Rq1DatabaseField_Text(this, "id"));
        addField(ID_Q_CENTRAL_ORGANIZATION = new Rq1DatabaseField_Text(this, "IDQCentralOrganization"));
        addField(IMPACT_ANALYSIS_RESULT = new Rq1DatabaseField_Text(this, "ImpactAnalysisResult"));
        addField(INSERT_STATEMENT = new Rq1DatabaseField_Text(this, "InsertStatement"));
        addField(INTERNAL_ECU_CALIBRATION = new Rq1DatabaseField_Text(this, "InternalECUCalibration"));
        addField(INTERNAL_ECU_SOFTWARE = new Rq1DatabaseField_Text(this, "InternalECUSoftware"));
        addField(IS_DUPLICATE = new Rq1DatabaseField_Text(this, "is_duplicate"));
        addField(ISSUED_BY = new Rq1DatabaseField_Text(this, "IssuedBy"));
        addField(ISSUER_CLASS = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_ISSUER_CLASS, "", ""));
        ISSUER_CLASS.acceptInvalidValuesInDatabase();
        addField(JUSTIFICATION = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_JUSTIFICATION, "", ""));
        JUSTIFICATION.acceptInvalidValuesInDatabase();
        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Enumeration(this, "LifeCycleState", LifeCycleState_Problem.values(), LifeCycleState_Problem.NEW));
        addField(OTHER_COMPONENTS = new Rq1DatabaseField_Text(this, "OtherComponents"));
        addField(PRIMARY_ROOT_CAUSE = new Rq1DatabaseField_Text(this, ATTRIBUTE_PRIMARY_ROOT_CAUSE));
        addField(PRIMARY_ROOT_CAUSE_FROM_BACKGROUND = new Rq1DatabaseField_Text(this, ATTRIBUTE_PRIMARY_ROOT_CAUSE_FROM_BACKGROUND));
        PRIMARY_ROOT_CAUSE_FROM_BACKGROUND.setReadOnly();
        addField(PRIORITY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_PRIORITY, "", ""));
        PRIORITY.acceptInvalidValuesInDatabase();
        addField(PROBLEM_REPRODUCTION = new Rq1DatabaseField_Text(this, "ProblemReproduction"));
        addField(REPORT_ID_8D = new Rq1DatabaseField_Text(this, ATTRIBUTE_REPORT_ID_8D));
        addField(RECOMMENDATIONS = new Rq1DatabaseField_Text(this, "Recommendations"));
        addField(ROOT_CAUSE_ANALYSIS_RESULT = new Rq1DatabaseField_Text(this, "RootCauseAnalysisResult"));
        addField(SEVERITY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_SEVERITY, "", ""));
        SEVERITY.acceptInvalidValuesInDatabase();
        addField(STATE = new Rq1DatabaseField_Text(this, "State"));
        addField(SYSTEM_CONSTANTS = new Rq1DatabaseField_Text(this, "SystemConstants"));
        addField(TESTING_ENVIRONMENT = new Rq1DatabaseField_Text(this, "TestingEnvironment"));
        addField(TYPE = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_TYPE, "", ""));
        TYPE.acceptInvalidValuesInDatabase();
        
        addField(URGENT_ALERT_NOTIFICATION_QMM = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_URGENT_ALERT_NOTIFICATION_QMM, "", ""));
        URGENT_ALERT_NOTIFICATION_QMM.acceptInvalidValuesInDatabase();
        addField(URGENT_ALERT_NOTIFICATION_COMMENT = new Rq1XmlSubField_Text(this, TAGS, ATTRIBUTE_URGENT_ALERT_NOTIFICATION_COMMENT.getName()));
        addField(URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, ATTRIBUTE_URGENT_ALERT_NOTIFICATION_CHANGE_COMMENT.getName()));
        
        addField(URGENT_RESOLUTION = new Rq1DatabaseField_Text(this, "UrgentResolution"));
        addField(VERSION_HISTORY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_VERSION_HISTORY, "", ""));
        VERSION_HISTORY.acceptInvalidValuesInDatabase();

        //
        // Fields for DOORS requirements
        //
        addField(L1_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L1_planned_requirements"));
        L1_PLANNED_REQUIREMENTS.addAlternativName("L1_Planned_requirements");
        L1_PLANNED_REQUIREMENTS.addAlternativName("L1_Planned_Requirements");
        L1_PLANNED_REQUIREMENTS.addAlternativName("L1_planned_requirement");

        addField(L1_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L1_removed_requirements"));
        L1_REMOVED_REQUIREMENTS.addAlternativName("L1_Removed_requirements");
        L1_REMOVED_REQUIREMENTS.addAlternativName("L1_Removed_Requirements");
        L1_REMOVED_REQUIREMENTS.addAlternativName("L1_removed_requirement");

        addField(L2_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L2_impacted_requirements"));
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_Impacted_requirements");
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_Impacted_Requirements");
        L2_IMPACTED_REQUIREMENTS.addAlternativName("L2_impacted_requirement");

        addField(L2_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L2_planned_requirements"));
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_Planned_requirements");
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_Planned_Requirements");
        L2_PLANNED_REQUIREMENTS.addAlternativName("L2_planned_requirement");

        addField(L2_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L2_removed_requirements"));
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_Removed_requirements");
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_Removed_Requirements");
        L2_REMOVED_REQUIREMENTS.addAlternativName("L2_removed_requirement");

        addField(L3_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L3_impacted_requirements"));
        L3_IMPACTED_REQUIREMENTS.addAlternativName("L3_Impacted_requirements");
        L3_IMPACTED_REQUIREMENTS.addAlternativName("L3_Impacted_Requirements");
        L3_IMPACTED_REQUIREMENTS.addAlternativName("L3_impacted_requirement");

        addField(L3_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L3_planned_requirements"));
        L3_PLANNED_REQUIREMENTS.addAlternativName("L3_Planned_requirements");
        L3_PLANNED_REQUIREMENTS.addAlternativName("L3_Planned_Requirements");
        L3_PLANNED_REQUIREMENTS.addAlternativName("L3_planned_requirement");

        addField(L3_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L3_removed_requirements"));
        L3_REMOVED_REQUIREMENTS.addAlternativName("L3_Removed_requirements");
        L3_REMOVED_REQUIREMENTS.addAlternativName("L3_Removed_Requirements");
        L3_REMOVED_REQUIREMENTS.addAlternativName("L3_removed_requirement");

        addField(L4_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L4_impacted_requirements"));
        L4_IMPACTED_REQUIREMENTS.addAlternativName("L4_Impacted_requirements");
        L4_IMPACTED_REQUIREMENTS.addAlternativName("L4_Impacted_Requirements");
        L4_IMPACTED_REQUIREMENTS.addAlternativName("L4_impacted_requirement");

        addField(L4_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L4_planned_requirements"));
        L4_PLANNED_REQUIREMENTS.addAlternativName("L4_Planned_requirements");
        L4_PLANNED_REQUIREMENTS.addAlternativName("L4_Planned_Requirements");
        L4_PLANNED_REQUIREMENTS.addAlternativName("L4_planned_requirement");

        addField(L4_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "L4_removed_requirements"));
        L4_REMOVED_REQUIREMENTS.addAlternativName("L4_Removed_requirements");
        L4_REMOVED_REQUIREMENTS.addAlternativName("L4_Removed_Requirements");
        L4_REMOVED_REQUIREMENTS.addAlternativName("L4_removed_requirement");

        addField(STAKEHOLDER_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "Stakeholder_planned_requirements"));
        addField(STAKEHOLDER_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "Stakeholder_removed_requirements"));

        addField(MO_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "MO_impacted_requirements"));
        addField(MO_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "MO_planned_requirements"));
        addField(MO_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "MO_removed_requirements"));

        addField(SC_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "SC_impacted_requirements"));
        addField(SC_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "SC_planned_requirements"));
        addField(SC_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "SC_removed_requirements"));

        addField(BC_FC_IMPACTED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "BC-FC_impacted_requirements"));
        addField(BC_FC_PLANNED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "BC-FC_planned_requirements"));
        addField(BC_FC_REMOVED_REQUIREMENTS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RequirementOnIssue(), TAGS, "BC-FC_removed_requirements"));
    }

}
