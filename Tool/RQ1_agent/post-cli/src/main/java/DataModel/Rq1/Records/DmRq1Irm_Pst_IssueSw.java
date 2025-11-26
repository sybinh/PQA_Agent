/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import static DataModel.DmFieldI.Attribute.MULTILINE_TEXT;
import DataModel.Rq1.Requirements.DmRq1Field_DoorsRequirementsOnIrmFromTables;
import DataModel.Rq1.Requirements.DmRq1LinkToRequirement_Type;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_IrmCustomerResponse;
import DataModel.Rq1.Fields.DmRq1Field_MappingToDerivatives_ComboBox;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_Irm_OnlyOnePilotDerivative;
import DataModel.Rq1.Monitoring.Rule_Irm_Prg_HintForExclude;
import DataModel.Rq1.Monitoring.Rule_Irm_PstRelease_IssueSw_Severity;
import DataModel.DmMappedElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Irm_Pst_IssueSw;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIRM;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.EcvDate;
import util.EcvListenerManager;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Irm_Pst_IssueSw extends DmRq1Irm {

    final public DmRq1Field_Text CUSTOMER_PRIORITY;
    final public DmRq1Field_Date PVER_F_PLANNED_DATE;
    final public DmRq1Field_Text RB_COMMENT;
    final public DmRq1Field_Text QUALIFICATION_STATUS_CHANGE_COMMENT;
    final public DmRq1Field_Text PVER_F_COMMENT;
    final public DmRq1Field_Text CALIBRATION_COMMENT;
    final public DmRq1Field_Text CALIBRATION_STATE;
    final public DmRq1Field_Text INTEGRATOR_COMMENT;
    final public DmRq1Field_Enumeration INTEGRATOR_STATE;
    final public DmRq1Field_Enumeration LAST_MINUTE_CHANGE;
    final public DmRq1Field_Enumeration LATE_CHANGE;

    final public DmRq1Field_Text EXCHANGE_WORKFLOW;
    final public DmRq1Field_Date LAST_IMPORT_DATE;
    final public DmRq1Field_Date LAST_EXPORTED_DATE;
    final public DmRq1Field_Text EXTERNAL_ID;
    final public DmRq1Field_Text EXTERNAL_REVIEW;
    final public DmRq1Field_Enumeration EXTERNAL_NEXT_STATE;
    final public DmRq1Field_Text EXTERNAL_COMMENT;
    final public DmRq1Field_Text EXTERNAL_CONVERSATION;
    final public DmRq1Field_Text EXTERNAL_STATES;
    final public DmRq1Field_Text EXTERNAL_TAGS;

    final public DmRq1Field_Reference<DmRq1Contact> EXTERNAL_ASSIGNEE;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_NAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_LASTNAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_FIRSTNAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_PHONE;
    
    final public DmRq1Field_Text SUPPLIER_ID;
    final public DmRq1Field_Text SUPPLIER_STATE;
    
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> L2_PLANNED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> L2_IMPACTED_REQUIREMENT_TABLE;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> L2_REMOVED_REQUIREMENT_TABLE;

    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> MO_IMPACTED_REQUIREMENTS;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> MO_PLANNED_REQUIREMENTS;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> MO_AFFECTED_ARTEFACT;
    final public DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> MO_REMOVED_REQUIREMENTS;

    final public DmRq1Field_DoorsRequirementsOnIrmFromTables MAPPED_REQUIREMENTS;

    final public DmRq1Field_MappingToDerivatives_ComboBox MAPPING_TO_DERIVATIVES;

    final public DmRq1Field_IrmCustomerResponse CUSTOMER_RESPONSE;

    final public DmRq1Field_Text INT_HINT_COMMENT;

    final public DmRq1Field_Enumeration IN_MA_INTEGRATION_STATUS;

    //
    // Fields for ECB
    //
    final public DmRq1Field_Text ECB_INT_HINT_COMMENT_TEXT;
    final public DmRq1Field_Text ECB_INT_HINT_DIDINFO;
    final public DmRq1Field_Text ECB_INT_HINT_REQUESTER;
    final public DmRq1Field_Enumeration ECB_INT_HINT_STATE;
    final public DmRq1Field_Enumeration ECB_COMMERCIAL_PILOT;
    final public DmRq1Field_Text ECB_INTEGRATOR;
    final public DmRq1Field_Enumeration ECB_PVAR_F_QSTATUS;
    //
    final public DmRq1Field_Text PROJECT_CONFIG_CONFIRM;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Irm_Pst_IssueSw(Rq1Irm_Pst_IssueSw rq1Irm_Prg_IssueSw) {
        super("IRM-PST-ISSUE_SW", rq1Irm_Prg_IssueSw);

        //
        // Create and add fields
        //
        PROJECT_CONFIG_CONFIRM = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.PROJECT_CONFIG_CONFIRM, "Project Config Confirmation");

        addField(CUSTOMER_PRIORITY = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.CUSTOMER_PRIORITY, "Customer Priority"));
        addField(PVER_F_PLANNED_DATE = new DmRq1Field_Date(this, rq1Irm_Prg_IssueSw.PVER_F_PLANNED_DATE, "PVER-F Planned Date"));
        addField(RB_COMMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.RB_COMMENT, "RB Comment (visible to customer)"));
        RB_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);
        RB_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(QUALIFICATION_STATUS_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.QUALIFICATION_STATUS_CHANGE_COMMENT, "Qualification Status Change Comment"));
        QUALIFICATION_STATUS_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        QUALIFICATION_STATUS_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        QUALIFICATION_STATUS_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        QUALIFICATION_STATUS_CHANGE_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(PVER_F_COMMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.PVER_F_COMMENT, "PVER-F Comment"));
        PVER_F_COMMENT.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        PVER_F_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        PVER_F_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        PVER_F_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(CALIBRATION_COMMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.CALIBRATION_COMMENT, "Calibration Comment"));
        CALIBRATION_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);
        CALIBRATION_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(CALIBRATION_STATE = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.CALIBRATION_STATE, "Calibration State"));
        CALIBRATION_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        CALIBRATION_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(INTEGRATOR_COMMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.INTEGRATOR_COMMENT, "Integrator Comment"));
        INTEGRATOR_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.SINGLELINE_TEXT);
        INTEGRATOR_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(INTEGRATOR_STATE = new DmRq1Field_Enumeration(this, rq1Irm_Prg_IssueSw.INTEGRATOR_STATE, "Integrator State"));
        INTEGRATOR_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        INTEGRATOR_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(LAST_MINUTE_CHANGE = new DmRq1Field_Enumeration(this, rq1Irm_Prg_IssueSw.LAST_MINUTE_CHANGE, "Last Minute Change"));
        LAST_MINUTE_CHANGE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LAST_MINUTE_CHANGE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(LATE_CHANGE = new DmRq1Field_Enumeration(this, rq1Irm_Prg_IssueSw.LATE_CHANGE, "Late Change"));
        LATE_CHANGE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        LATE_CHANGE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(MAPPING_TO_DERIVATIVES = new DmRq1Field_MappingToDerivatives_ComboBox(this, rq1Irm_Prg_IssueSw.MAPPING_TO_DERIVATIVES, HAS_MAPPED_RELEASE, "Mapping To Derivatives"));
        MAPPING_TO_DERIVATIVES.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(EXCHANGE_WORKFLOW = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXCHANGE_WORKFLOW, "Exchange Workflow"));
        addField(LAST_IMPORT_DATE = new DmRq1Field_Date(this, rq1Irm_Prg_IssueSw.LAST_IMPORT_DATE, "Last Imported Date"));
        addField(LAST_EXPORTED_DATE = new DmRq1Field_Date(this, rq1Irm_Prg_IssueSw.LAST_EXPORTED_DATE, "Last Exported Date"));
        addField(EXTERNAL_ID = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ID, "External ID"));
        addField(EXTERNAL_REVIEW = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_REVIEW, "External Review"));
        addField(EXTERNAL_NEXT_STATE = new DmRq1Field_Enumeration(this, rq1Irm_Prg_IssueSw.EXTERNAL_NEXT_STATE, "External Next State"));
        addField(EXTERNAL_COMMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_COMMENT, "New External Comment"));
        EXTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        EXTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION, DmFieldI.Attribute.MULTILINE_TEXT);
        addField(EXTERNAL_CONVERSATION = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_CONVERSATION, "External Conversation"));
        EXTERNAL_CONVERSATION.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        EXTERNAL_CONVERSATION.setAttribute(FIELD_FOR_BULK_OPERATION, MULTILINE_TEXT);
        addField(EXTERNAL_STATES = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_STATES, "External State"));
        addField(EXTERNAL_TAGS = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_TAGS, "External Tags"));
        EXTERNAL_TAGS.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(EXTERNAL_ASSIGNEE = new DmRq1Field_Reference<>(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE, "External Assignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE_NAME, "External Assignee Name"));
        addField(EXTERNAL_ASSIGNEE_LASTNAME = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE_LASTNAME, "External Assignee Last Name"));
        addField(EXTERNAL_ASSIGNEE_FIRSTNAME = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE_FIRSTNAME, "External Assignee First Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE_ORGANIZATION, "External Assignee Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE_DEPARTMENT, "External Assignee Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE_EMAIL, "External Assignee E-Mail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.EXTERNAL_ASSIGNEE_PHONE, "External Assignee Phone Number"));
        
        addField(SUPPLIER_ID = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.SUPPLIER_ID, "Supplier ID"));
        addField(SUPPLIER_STATE = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.SUPPLIER_STATE, "Supplier State"));

        addField(L2_PLANNED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Irm_Prg_IssueSw.L2_PLANNED_REQUIREMENTS, "L2 Planned Requirements "));
        addField(L2_IMPACTED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Irm_Prg_IssueSw.L2_IMPACTED_REQUIREMENTS, "L2 Impacted Requirements "));
        addField(L2_REMOVED_REQUIREMENT_TABLE = new DmRq1Field_Table<>(this, rq1Irm_Prg_IssueSw.L2_REMOVED_REQUIREMENTS, "L2 Removed Requirements "));

        addField(MO_IMPACTED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1Irm_Prg_IssueSw.MO_IMPACTED_REQUIREMENTS, "MO Impacted Requirements "));
        addField(MO_PLANNED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1Irm_Prg_IssueSw.MO_PLANNED_REQUIREMENTS, "MO Planned Requirements "));
        addField(MO_AFFECTED_ARTEFACT = new DmRq1Field_Table<>(this, rq1Irm_Prg_IssueSw.MO_AFFECTED_ARTEFACT, "MO Affected Artefact "));
        addField(MO_REMOVED_REQUIREMENTS = new DmRq1Field_Table<>(this, rq1Irm_Prg_IssueSw.MO_REMOVED_REQUIREMENTS, "MO Removed Requirements "));

        addField(MAPPED_REQUIREMENTS = new DmRq1Field_DoorsRequirementsOnIrmFromTables(this, "Mapped Requirements"));
        MAPPED_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_PLANNED, L2_PLANNED_REQUIREMENT_TABLE);
        MAPPED_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_IMPACTED, L2_IMPACTED_REQUIREMENT_TABLE);
        MAPPED_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.L2_REMOVED, L2_REMOVED_REQUIREMENT_TABLE);
        MAPPED_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_IMPACTED, MO_IMPACTED_REQUIREMENTS);
        MAPPED_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_PLANNED, MO_PLANNED_REQUIREMENTS);
        MAPPED_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_AFFECTED, MO_AFFECTED_ARTEFACT);
        MAPPED_REQUIREMENTS.addTableField(DmRq1LinkToRequirement_Type.MO_REMOVED, MO_REMOVED_REQUIREMENTS);

        addField(CUSTOMER_RESPONSE = new DmRq1Field_IrmCustomerResponse(this, rq1Irm_Prg_IssueSw.CUSTOMER_RESPONSE, "Customer Response"));

        addField(INT_HINT_COMMENT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.INT_HINT_COMMENT, "Integration Comment (IntHint)"));
        INT_HINT_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);
        INT_HINT_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(IN_MA_INTEGRATION_STATUS = new DmRq1Field_Enumeration(rq1Irm_Prg_IssueSw.IN_MA_INTEGRATION_STATUS, "InMa Int Status"));

        //
        // Fields for ECB
        //
        addField(ECB_INT_HINT_COMMENT_TEXT = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.ECB_INT_HINT_COMMENT_TEXT, "ECB: Integration Comment"));
        ECB_INT_HINT_COMMENT_TEXT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        ECB_INT_HINT_COMMENT_TEXT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INT_HINT_COMMENT_TEXT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        ECB_INT_HINT_COMMENT_TEXT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(ECB_INT_HINT_DIDINFO = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.ECB_INT_HINT_DIDINFO, "ECB: DIDinfo"));
        ECB_INT_HINT_DIDINFO.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT);
        ECB_INT_HINT_DIDINFO.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INT_HINT_DIDINFO.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        ECB_INT_HINT_DIDINFO.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(ECB_INT_HINT_REQUESTER = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.ECB_INT_HINT_REQUESTER, "ECB: Integration Requester"));
        ECB_INT_HINT_REQUESTER.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        ECB_INT_HINT_REQUESTER.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INT_HINT_REQUESTER.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_INT_HINT_STATE = new DmRq1Field_Enumeration(this, rq1Irm_Prg_IssueSw.ECB_INT_HINT_STATE, "ECB: Integration State"));
        ECB_INT_HINT_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INT_HINT_STATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_COMMERCIAL_PILOT = new DmRq1Field_Enumeration(this, rq1Irm_Prg_IssueSw.ECB_COMMERCIAL_PILOT, "ECB: Commercial-Pilot"));
        ECB_COMMERCIAL_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_COMMERCIAL_PILOT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_PVAR_F_QSTATUS = new DmRq1Field_Enumeration(this, rq1Irm_Prg_IssueSw.ECB_PVAR_F_QSTATUS, "ECB: PVER_F_QStatus"));
        ECB_PVAR_F_QSTATUS.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_PVAR_F_QSTATUS.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ECB_INTEGRATOR = new DmRq1Field_Text(this, rq1Irm_Prg_IssueSw.ECB_INTEGRATOR, "ECB: Integrator"));
        ECB_INTEGRATOR.setAttribute(DmFieldI.Attribute.SINGLELINE_TEXT);
        ECB_INTEGRATOR.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ECB_INTEGRATOR.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        //
        // Create and add rules
        //
//        addRule(new Rule_Irm_Prg_IssueSw_PilotSet(this));
        addRule(new Rule_Irm_Prg_HintForExclude(this));
        addRule(new Rule_Irm_PstRelease_IssueSw_Severity(this));
        addRule(new Rule_Irm_OnlyOnePilotDerivative(this));

    }

    public String getProjectConfigValue() {
        return PROJECT_CONFIG_CONFIRM.getValueAsText();
    }

    public void setProjectConfigValue(String projectConfigText) {
        assert (projectConfigText != null);
        PROJECT_CONFIG_CONFIRM.setValue(projectConfigText);
        save();
    }

    final public DmRq1Pst getMappedPst() {
        if (HAS_MAPPED_RELEASE.getElement() instanceof DmRq1Pst) {
            return ((DmRq1Pst) HAS_MAPPED_RELEASE.getElement());
        } else {
            return (null);
        }
    }

    final public DmRq1IssueSW getMappedIssueSW() {
        if (HAS_MAPPED_ISSUE.getElement() instanceof DmRq1IssueSW) {
            return ((DmRq1IssueSW) HAS_MAPPED_ISSUE.getElement());
        } else {
            return (null);
        }
    }
    //
    final static private Pattern irrelevanteIssueFDsAndBCsPattern = Pattern.compile("\\G\\s*(RQONE[0-9]{8})*.*");

    /**
     * Returns the RQ1-IDs of the I-FD and BC that shall be ignored in the PST
     * planning rules.<br>
     * <br>
     * The IDs are extracted from the field CHANGES_TO_ISSUES and from the tag
     * "Irrelevante_IssueFDs". The tag is depreciated but still recognized for
     * old IRM.
     *
     * @return List of RQ1-ID as list of string.
     */
    final public List<String> getIssueFdAndBcToIgnore() {
        List<String> result = new ArrayList<>(10);
        //
        // Read "old style" ignore settings
        //
        try {
            String irrelevanteIssueFDs = ((EcvXmlContainerElement) (TAGS.getValue())).getTextElement("Irrelevante_IssueFDs").getText();
        } catch (EcvXmlElement.NotfoundException ex) {
        }

        //
        // Read new style ignore settings
        //
        result.addAll(scanChangesToIssues(CHANGES_TO_ISSUES.getValue()));

        return (result);
    }

    //
    // static public to simplify testing
    //
    static public List<String> scanChangesToIssues(String value) {
        assert (value != null);
        List<String> result = new ArrayList<>(10);
        if (value.isEmpty() == false) {
            Matcher matcher = irrelevanteIssueFDsAndBCsPattern.matcher(value);
            matcher.useAnchoringBounds(false);
            while (matcher.find()) {
                String group = matcher.group(1);
                if ((group != null) && (group.isEmpty() == false)) {
                    result.add(matcher.group(1));
                }
            }
        }
        return (result);
    }

    static private DmRq1Irm_Pst_IssueSw create() {
        Rq1Irm_Pst_IssueSw rq1Record = new Rq1Irm_Pst_IssueSw();
        DmRq1Irm_Pst_IssueSw dmElement = new DmRq1Irm_Pst_IssueSw(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmElement);
        return (dmElement);
    }

    public static DmRq1Irm_Pst_IssueSw create(DmRq1Pst release, DmRq1IssueSW dmIssueSW) throws DmRq1MapExistsException {
        assert (release != null);
        assert (release instanceof DmRq1Pst) : release.getClass().getCanonicalName();
        assert (dmIssueSW != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : release.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == dmIssueSW) {
                throw (new DmRq1MapExistsException());
            }
        }

        DmRq1Irm_Pst_IssueSw irm = create();

        //
        // Take over content from PVAR
        //
        irm.ACCOUNT_NUMBERS.setValue(release.ACCOUNT_NUMBERS.getValue());
        irm.MAPPING_TO_DERIVATIVES.updateForPst((DmRq1Pst) release);

        //
        // Add mapping on map
        //
        irm.HAS_MAPPED_RELEASE.setElement(release);
        irm.HAS_MAPPED_ISSUE.setElement(dmIssueSW);

        //
        // Add mapping on release
        //
        release.MAPPED_ISSUES.addElement(irm, dmIssueSW);

        //
        // Add mapping on issue
        //
        dmIssueSW.HAS_MAPPED_PST.addElement(irm, release);

        return (irm);
    }

    public static DmRq1Irm_Pst_IssueSw createBasedOnIrm(DmRq1Pst release, DmRq1Irm_Pst_IssueSw dropedIrm, DmRq1IssueSW dmIssueSW) throws DmRq1MapExistsException {
        assert (release != null);
        assert (dropedIrm != null);
        assert (dmIssueSW != null);

        //
        // Create new IRM
        //
        DmRq1Irm_Pst_IssueSw newIrm = create(release, dmIssueSW);

        //
        // Adapt derivatives for PVAR
        //
        if (release instanceof DmRq1Pst) {
            newIrm.MAPPING_TO_DERIVATIVES.updateForPst((DmRq1Pst) release);
        }

        return (newIrm);
    }

    public static DmRq1Irm_Pst_IssueSw moveFromPstToPst(DmRq1Pst newPstRelease, DmRq1Irm_Pst_IssueSw dropedIrm, DmRq1IssueSW dmIssueSW) throws DmRq1MapExistsException {
        assert (newPstRelease != null);
        assert (newPstRelease instanceof DmRq1Pst) : newPstRelease.getClass().getCanonicalName();
        assert (dropedIrm != null);
        assert (dmIssueSW != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : newPstRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == dmIssueSW) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1Pst oldPstRelease = (DmRq1Pst) dropedIrm.HAS_MAPPED_RELEASE.getElement();

        //
        // Start transaction
        //
        EcvListenerManager.startTransaction();

        //
        // Change mapping on PST
        //
        oldPstRelease.MAPPED_ISSUES.removeElement(dmIssueSW);
        newPstRelease.MAPPED_ISSUES.addElement(dropedIrm, dmIssueSW);

        //
        // Change mapping on issue
        //
        dmIssueSW.HAS_MAPPED_PST.removeElement(oldPstRelease);
        dmIssueSW.HAS_MAPPED_PST.addElement(dropedIrm, newPstRelease);

        //
        // Change mapping on IRM and adapt derivatives for PST *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedIrm.HAS_MAPPED_RELEASE.setElement(newPstRelease);
        dropedIrm.MAPPING_TO_DERIVATIVES.updateForPst((DmRq1Pst) newPstRelease);
        dropedIrm.changed(null); // Initiate GUI update.

        //
        // End transaction
        //
        EcvListenerManager.endTransaction();

        return (dropedIrm);
    }

    public static DmRq1Irm_Pst_IssueSw moveFromIssueToIssue(DmRq1Pst pstRelease, DmRq1Irm_Pst_IssueSw dropedIrm, DmRq1IssueSW newIssue) throws DmRq1MapExistsException {
        assert (pstRelease != null);
        assert (dropedIrm != null);
        assert (newIssue != null);

        //
        // Ensure that RRM does not yet exist
        //
        if (newIssue.HAS_MAPPED_PST.isLoaded() == true) {
            for (DmMappedElement<DmRq1Irm_Pst_IssueSw, DmRq1Pst> m : newIssue.HAS_MAPPED_PST.getElementList()) {
                if (m.getTarget() == pstRelease) {
                    throw (new DmRq1MapExistsException());
                }
            }
        } else {
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : pstRelease.MAPPED_ISSUES.getElementList()) {
                if (m.getTarget() == newIssue) {
                    throw (new DmRq1MapExistsException());
                }
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1Issue oldIssue = dropedIrm.HAS_MAPPED_ISSUE.getElement();

        //
        // Change mapping on Pst
        //
        pstRelease.MAPPED_ISSUES.removeElement(oldIssue);
        pstRelease.MAPPED_ISSUES.addElement(dropedIrm, newIssue);

        //
        // Change mapping on issue
        //
        oldIssue.HAS_MAPPED_RELEASES.removeElement(pstRelease);
        newIssue.HAS_MAPPED_PST.addElement(dropedIrm, pstRelease);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedIrm.HAS_MAPPED_ISSUE.setElement(newIssue);

        return (dropedIrm);
    }

    /**
     * used to add a a new element to the changes to issue list
     *
     * @param element RQ1 id of the element
     * @param comment can be empty
     */
    public void addToChangesToIssue(DmRq1Element element, String comment) {
        assert (element != null);
        assert (comment != null);

        String cti = CHANGES_TO_ISSUES.getValue();

        if (cti.equals("") || cti.equals("\n")) {
            cti = element.getId() + " " + comment;
        } else {
            if (cti.endsWith("\n")) {
                cti += element.getId() + " " + comment;
            } else {
                cti += "\n" + element.getId() + " " + comment;
            }
        }

        CHANGES_TO_ISSUES.setValue(cti.trim());
    }

    public void deleteChangesToIssue(DmRq1Element element) {
        String[] split = CHANGES_TO_ISSUES.getValue().split("\n");
        ArrayList<String> ids = new ArrayList<>();
        String[] newCTI = split;

        //Get RQ1 IDs
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith(element.getId())) {
                newCTI[i] = "";
            }
        }
        String newValue = "";
        int length = newCTI.length;
        int counter = 0;

        for (String s : newCTI) {
            counter++;
            if (!s.equals("") && counter != length) {
                newValue += s + "\n";
            } else {
                if (!(s.equals("")) && counter == length) {
                    newValue += s;
                }
            }
        }

        CHANGES_TO_ISSUES.setValue(newValue.trim());
    }

    /**
     * This methods calculates if a submit date warning is needed for move of
     * new IRMs between Issue and PST.
     *
     * @param issue
     * @param release
     * @param link
     * @return
     */
    public static boolean submitDateCheckForMoveAction(DmRq1IssueSW issue, DmRq1Pst release, DmRq1Irm_Pst_IssueSw link) {
        if (release instanceof DmRq1PvarRelease == true || release instanceof DmRq1PverRelease == true) {
            if (link.HAS_MAPPED_RELEASE.getElement() instanceof DmRq1PstCollectionI == true) {
                return submitDateWarningForCreateAction(issue);
            } else {
                EcvDate dateIssue = null;
                if (link.HAS_MAPPED_ISSUE.isElementSet() == true) {
                    dateIssue = issue.SUBMIT_DATE.getDate();
                }
                if (dateIssue != null) {
                    if (dateIssue.getDaysBetweenExcludingWeekends(EcvDate.getToday()) > 5) {
                        EcvDate dateFromRelease = null;
                        EcvDate dateToRelease = null;
                        if (link.HAS_MAPPED_RELEASE.getElement() != null) {
                            dateFromRelease = link.HAS_MAPPED_RELEASE.getElement().PLANNED_DATE.getDate();
                        }
                        if (release instanceof DmRq1PstReleaseI) {
                            dateToRelease = ((DmRq1Pst) release).PLANNED_DATE.getDate();
                        }
                        if (dateFromRelease != null && dateToRelease != null) {
                            if (dateToRelease.isEarlierThen(dateFromRelease)) {
                                return (true);
                            }
                        }
                    }
                }
            }

        }
        return (false);
    }

    /**
     * This methods calculates if a submit date warning is needed for creation
     * of new IRMs between Issue and PST.
     *
     * @param issue
     * @return
     */
    public static boolean submitDateWarningForCreateAction(DmRq1IssueSW issue) {
        EcvDate dateIssue = issue.SUBMIT_DATE.getValue();
        if (dateIssue != null) {
            if (dateIssue.getDaysBetweenExcludingWeekends(EcvDate.getToday()) > 5) {
                return (true);
            }
        }
        return (false);
    }
}
