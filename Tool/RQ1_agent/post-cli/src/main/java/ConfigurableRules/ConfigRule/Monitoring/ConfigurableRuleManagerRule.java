/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Monitoring;

import ConfigurableRules.ConfigRule.Enumerations.UtilEnum;
import ConfigurableRules.ConfigRule.Fields.DmRq1RecordTypeList;
import ConfigurableRules.ConfigRule.Query.Criteria;
import ConfigurableRules.ConfigRule.Query.DateCriteriaValue;
import ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord;
import ConfigurableRules.ConfigRule.Records.MarkerContent;
import ConfigurableRules.ConfigRule.util.Constants;
import ConfigurableRules.ConfigRule.util.Utils;
import ConfigurableRules.ConfigurableRulesCustomizer;
import DataModel.DmElementI;
import DataModel.DmField;
import DataModel.DmValueFieldI;
import DataModel.DmValueFieldI_Xml;
import DataModel.Doors.Records.DmDoorsObject;
import DataModel.Doors.Records.DmDoorsObject_PTSA_1x_L1_REQ;
import DataModel.Doors.Records.DmDoorsObject_PTSA_1x_Requirement;
import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1AssignedRecord;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1MapElement;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1SubjectElement;
import Monitoring.Marker;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author RHO2HC
 * @param <T> The type of element to which the rule is assigned.
 */
public abstract class ConfigurableRuleManagerRule<T extends DmElementI> extends DmRule<T> implements ConfigurableRuleListener {

    public static final String PROJECT = "Project";
    public final Map<String, List<Marker>> MARKER_CACHE = new HashMap<>();

    protected final Map<String, ConfigurableRuleRecord> RECORDS_CACHE = new HashMap<>();

    protected static final RuleDescription ruleManagerDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "The Manager for User Assistant Rules.",
            "This rules is a pseudo rule that does not create any warnings or other markers.\n"
            + "It is used IPE internally to load the configurable rules of User Assistant Rules for a element as soon as the element comes visible.");

    public ConfigurableRuleManagerRule(T assignedDmElement) {
        super(ruleManagerDescription, assignedDmElement);
    }

    /**
     * Check the assignee of current RQ1 artifact -> decide if the rule can be process on this artifact
     * If Login User is RBEI/RBVH -> Full Mode. Else -> Restricted Mode
     * In Full Mode
     *      If user is Project Leader -> all artifacts in the project will be checked. (EXCEPT artifact assigned to non RBEI/RBVH)
     *      Else -> only check user's assigned artifact.
     * In Restricted Mode
     *      Only check user's assigned artifact.
     */
    private boolean checkAssigneeBeforeExecute() {
        try {
            // Get Login User Full Name
            String userLoginFullName = EcvLoginManager.getCurrentUserFullName();
            
            // Get Assigenee Full Name of current RQ1 artifact
            String currAssigneeFullName = Constants.BLANK;
            
            // if checked object is normal RQ1 item
            if(this.dmElement instanceof DmRq1AssignedRecord) {
                currAssigneeFullName = ((DmRq1AssignedRecord) this.dmElement).ASSIGNEE_FULLNAME.getValue().trim();
            // if checked object is mapping item    
            } else if (this.dmElement instanceof DmRq1MapElement) {
                currAssigneeFullName = ((DmRq1MapElement) this.dmElement).ASSIGNEE_FULLNAME.getValue().trim();
            }
            
            // Check if user is allowed to use Full Mode
            boolean isFullMode = Utils.isUsingFullMode();
            
            // Consider Project Leader role when 
            // _ Login User can use Full Mode
            // _ Assignee of current RQ1 artifact is RBEI / RBVH
            if(isFullMode) {
                
                // Get current Assignee's Company name
                String currAssigneeComp = Utils.getCompanyFromName(currAssigneeFullName);
                
                // If Assisgnee of current artifact is not RBEI and RBVH -> return False
                if(!currAssigneeComp.equals(Constants.MS) && !currAssigneeComp.equals(Constants.RBEI) && !currAssigneeComp.equals(Constants.RBVH)) {
                    return false;
                }
            
                // Current project which current dmElement belong to.
                DmRq1Project currProject = ((DmRq1AssignedRecord) this.dmElement).getProject();
                
                // Check if login user has Project Leader role
                boolean isProjectLeader = Utils.isProjectLeader(currProject, userLoginFullName);
                
                return isProjectLeader || userLoginFullName.equals(currAssigneeFullName);
                
            } else {
                return userLoginFullName.equals(currAssigneeFullName);
            }

        } catch (Exception e) {
            Logger.getLogger(ConfigurableRuleManagerRule.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(ConfigurableRuleManagerRule.class.getName(), e);
            return false;
        }
    }

    @Override
    protected synchronized void executeRule() {
        if (ConfigurableRulesCustomizer.getListOfActiveProjects().size() <= 0) {
            return;
        }

        reset();

        // In each project, update the map <recordType : List<this>>
        registerConfigurableRuleListener();

        // In each project, execute all rule.
        getAppliedProjects().stream().forEach(project -> {

            List<ConfigurableRuleRecord> records = getAllRecordsFromAProject(project);

            records.stream().forEach(record -> {
                executeARuleRecord(record, project);
            });
        });
    }

    @Override
    public void addARule(ConfigurableRuleRecord record, DmRq1Project belongsToProject) {
        String elementType = getElementType(dmElement);

        if (record.getRecordType().equals(elementType)) {
            executeARuleRecord(record, belongsToProject);
        }
    }

    @Override
    public void editARule(ConfigurableRuleRecord record, DmRq1Project belongsToProject) {
        String elementType = getElementType(dmElement);

        if (record.getRecordType().equals(elementType)) {
            if (MARKER_CACHE.containsKey(record.getRuleId())) {
                // clear markers 
                this.dmElement.removeMarkers(this);
                // remove old marker in the cache
                MARKER_CACHE.remove(record.getRuleId());
                // add all markers with the marker for the old record
                addAllMarkersFromCache();
            }
            executeARuleRecord(record, belongsToProject);
        }
    }

    @Override
    public void removeARule(String recordType, String ruleId) {
        String elementType = getElementType(dmElement);

        if (recordType.equals(elementType)) {
            if (MARKER_CACHE.containsKey(ruleId)) {
                // clear markers 
                this.dmElement.removeMarkers(this);
                // remove old marker in the cache
                MARKER_CACHE.remove(ruleId);
                // add all markers with the marker for the old record
                addAllMarkersFromCache();
            }
        }
    }

    private void addAllMarkersFromCache() {
        MARKER_CACHE.values().stream().forEach(list -> {
            list.stream().forEach(marker -> {
                this.dmElement.addMarker(marker);
            });
        });
    }

    /**
     * Update map <recordType : List<this>> in DmRq1Project In each project,
     * there are several recordTyle each recordType map a list of
     * ConfigurableRuleManagerRule Each ConfigurableRuleManagerRule manage a
     * specific rule.
     */
    private boolean isRegister = false;

    @SuppressWarnings("unchecked")
    public void registerConfigurableRuleListener() {
        if (isRegister == false && getAppliedProjects() != null && !getAppliedProjects().isEmpty()) {
            ConfigurableRuleManagerRule thisRuleManager = this;

            String elementType = getElementType(dmElement);

            for (DmRq1Project project : getAppliedProjects()) {
                project.getConfigurableRuleProjectManager().addConfigurableRuleListener(elementType, thisRuleManager);
            }
            isRegister = true;
        }
    }

    protected void reset() {
        this.dmElement.removeMarkers(this);
    }

    @Override
    public void deactive() {
        reset();
        deactivateRule();
    }

    @Override
    public void active() {
        activateRule();
    }

    /**
     * Check a rule record and add markers.
     *
     * @param record : a rule to check
     * @param belongsToProject : user right click, create rule on this project
     */
    private void executeARuleRecord(ConfigurableRuleRecord record, DmRq1Project belongsToProject) {
        // Check if assignee of RQ1 Artifact != login user -> not execute rule
        if (!checkAssigneeBeforeExecute()) {
            return;
        }

        try {
            // If rule record is applied for all project / applied for 'belongsToProject' -> Keep checking
            if (!record.isIsAppliedOnlyToBelongingProject() || (record.isIsAppliedOnlyToBelongingProject() && checkAssignedElementIsInTheGivenProject(belongsToProject))) {

                // Check and update id for criterias if missing
                Utils.updateMissingCriteriaId(record.getCriterias());

                // If rule's criteria editor empty => default as "And" condition
                if (record.getCriteriaEditor().isEmpty()) {
                    String defaultCriteriaEditor = Utils.createDefaultCriteriaEditor(record.getCriterias());
                    record.setCriteriaEditor(defaultCriteriaEditor);
                }

                // Check every criterias of a rule.
                List<String> criteriaEditor = Utils.convertCriteriaEditorToList(record.getCriteriaEditor());

                Integer checkResult = 0;
                try {
                    checkResult = checkCriteria(criteriaEditor.get(0), criteriaEditor, record.getCriterias()).get(0);

                } catch (Exception e) {
                    Logger.getLogger(ConfigurableRuleManagerRule.class.getName()).log(Level.SEVERE, null, e);
                    ToolUsageLogger.logError(ConfigurableRuleManagerRule.class.getName(), e);
                }

                // If final check result of rule record = 1 -> Add the marker
                if (checkResult == 1) {

                    List<Marker> markers = new ArrayList<>();
                    for (MarkerContent makerContent : record.getMarkers()) {

                        Marker marker = makerContent.execute(this);
                        this.dmElement.addMarker(marker);
                        markers.add(marker);
                    }
                    MARKER_CACHE.put(record.getRuleId(), markers);
                }
                RECORDS_CACHE.put(record.getRuleId(), record);
            }

        } catch (Exception ex) {
            Logger.getLogger(ConfigurableRuleManagerRule.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(ConfigurableRuleManagerRule.class.getName(), ex);
        }
    }

    /**
     * Check all criteria of a rule record. Final result is result of the rule.
     * From 'criteriaEditor', go throw each element _ If criteria id -> Check
     * that criteria _ If open '(' or '[' -> recursive _ If close ')' or ']' ->
     * summary result of current level. Summary result of current recursive
     * level is like a rule result of above level
     *
     * @param open
     * @param criteriaEditor
     * @param criterias
     * @return <result : end bracket index>
     */
    private List<Integer> checkCriteria(String open, List<String> criteriaEditor, List<Criteria> criterias) {

        // Check condition by open bracket
        boolean andCond = true;

        if (open.equals(Constants.OPEN_AND)) {
            andCond = true;
        } else if (open.equals(Constants.OPEN_OR)) {
            andCond = false;
        }

        // Go throw each criteria id
        List<Integer> sumResult = new ArrayList<>();

        for (int i = 1; i < criteriaEditor.size(); i++) {
            String e = criteriaEditor.get(i);

            // Element is criteria id
            if (Utils.isNumeric(e)) {
                Criteria criteria = Utils.getCriteriaById(criterias, e);

                // Get real value from RQ1 field (field is get from current criteria)
                Object comparedValue = getCriteriaValues(criteria);
                
                // Compare real value of RQ1 with expect value from criteria
                boolean checkReult = criteria.checkValue(comparedValue);

                if (checkReult) {
                    sumResult.add(1);
                } else {
                    sumResult.add(0);
                }

                // Element is open brack ket -> recursive
            } else if (e.equals(Constants.OPEN_AND) || e.equals(Constants.OPEN_OR)) {
                List<String> subList = criteriaEditor.subList(i, criteriaEditor.size() - 1);
                List<Integer> recursiveResult = checkCriteria(e, subList, criterias);
                sumResult.add(recursiveResult.get(0));
                i += recursiveResult.get(1);

                // Element is close bracket -> return summary result + current index
            } else if (e.equals(Constants.CLOSE)) {

                List<Integer> returnResult = new ArrayList<>();

                if (andCond && !sumResult.contains(0)) {
                    returnResult.add(1);
                    returnResult.add(i);
                    return returnResult;
                }
                if (!andCond && sumResult.contains(1)) {
                    returnResult.add(1);
                    returnResult.add(i);
                    return returnResult;
                }
                returnResult.add(0);
                returnResult.add(i);
                return returnResult;
            }
        }
        return null;
    }

    private Object getCriteriaValues(Criteria criteria) {
        // Criteria for compare fields in 1 object -> Get value of field and compared field
        if(criteria.getXmlObject().hasAttribute(DateCriteriaValue.ATTRIBUTE_COMPARED_FIELD)) {

            Object field = getValueFromDmElementByFieldName(criteria.getField());
            Object comparedField = getValueFromDmElementByFieldName(((DateCriteriaValue)criteria.getValueToCompare()).getComparedFieldName());

            List<Object> fields = new ArrayList<>();            
            fields.add(field);
            fields.add(comparedField);
            return fields;
        
        // Criteria for check 1 field in 1 object -> Get value of field
        } else {
            Object field = getValueFromDmElementByFieldName(criteria.getField());
            return field;
        }
    }
    
    /**
     * From Criteria of the rule check -> Get the field name -> get the real
     * value of field from RQ1 artifact.
     *
     * @param criteria
     * @return
     */
    @SuppressWarnings("unchecked")
    private Object getValueFromDmElementByFieldName(String criteriaField) {
        // Get fieldName and subFieldName =============================================================================
        // If fieldName contain "_" => subFieldName = sub string of fieldName from "_"
        String subFieldName = criteriaField.contains(DmRq1RecordTypeList.HYPHEN_SYMBOL) ? criteriaField.substring(criteriaField.indexOf(DmRq1RecordTypeList.HYPHEN_SYMBOL) + 1) : null;
        // If fieldName contain "_" => fieldName = fieldName remove subFieldName
        String fieldName = criteriaField.contains(DmRq1RecordTypeList.HYPHEN_SYMBOL) ? criteriaField.substring(0, criteriaField.indexOf(DmRq1RecordTypeList.HYPHEN_SYMBOL)).trim() : criteriaField;
        // If fieldName = "Attachments" & this RQ1 data model is instance of DmRq1SubjectElement -> fieldName = get  name from user interface
        fieldName = fieldName.equals("Attachments") ? (this.dmElement instanceof DmRq1SubjectElement ? ((DmRq1SubjectElement) this.dmElement).ATTACHMENTS.getNameForUserInterface() : fieldName) : fieldName;
        // If fieldName is "Specification Review", it should be "Specification Review (with Customer)"
        fieldName = fieldName.equals("Specification Review ") ? fieldName + "(with Customer)" : fieldName;

        // From current RQ1 data model, get value of field relate to criteria ==========================================
        // Get object of RQ1 field by fieldName 
        DmField field = (DmField) this.dmElement.getFieldByName(fieldName);

        if (field == null) {
            return null;
        }

        if (criteriaField.contains(DmRq1RecordTypeList.HYPHEN_SYMBOL)) {

            // If 'field' is a list of data (Ex: Attachment has list of file)
            // In this condition, field is a list of objects
            if (field instanceof DmRq1Field_ReferenceList) {
                DmRq1Field_ReferenceList list = (DmRq1Field_ReferenceList) field;
                List<Object> resultList = new ArrayList<>();
                try {
                    if (subFieldName != null && list.getElementList() != null) {
                        list.getElementList().stream().forEach(element -> {
                            if (element instanceof DmRq1ElementInterface) {
                                DmRq1ElementInterface dmRq1Element = (DmRq1ElementInterface) element;
                                DmValueFieldI subField = (DmValueFieldI) dmRq1Element.getFieldByName(subFieldName);
                                resultList.add(subField.getValue());
                            }
                        });
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ConfigurableRuleManagerRule.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(ConfigurableRuleManagerRule.class.getName(), ex);
                }
                return resultList;
            }

        } else {
            // Reference field
            if (field instanceof DmRq1Field_Reference) {
                DmRq1Field_Reference ref = (DmRq1Field_Reference) field;
                DmValueFieldI subField = (DmValueFieldI) ref.getElement().getFieldByName(subFieldName);
                Object value = subField.getValue();
                
                if(value == null) return UtilEnum.EMPTY;
                else return value;
            
            // Field is XML format
            } else if (field instanceof DmValueFieldI_Xml) {
                DmValueFieldI_Xml valueField = (DmValueFieldI_Xml) field;
                Object value = valueField.getValue().getUiString_WithoutContainer();
                
                if(value == null) return UtilEnum.EMPTY; // In case External field has no value (null data)
                else return value;
            
            // Normal value field (most of the IPE fields)
            } else if (field instanceof DmValueFieldI) {
                DmValueFieldI valueField = (DmValueFieldI) field;
                Object value = valueField.getValue();
                
                if(value == null) return UtilEnum.EMPTY; // In case External field has no value (null data)
                else return value;
            }
        }
        return null;
    }

    private synchronized List<ConfigurableRuleRecord> getAllRecordsFromAProject(DmRq1Project project) {
        List<ConfigurableRuleRecord> result = new ArrayList<>();
        
        // Import Rules from User Assistant cache file
        project.getConfigurableRuleProjectManager().importUserAssistantRules(false);
        
        Map<String, Map<String, ConfigurableRuleRecord>> recordsGroupByType_Id = project.getConfigurableRuleProjectManager().getTableConfigurableRule().getTableDescription().getRecordsGroupByType_Id();

        // Get Type of current element (FD, SW, FC,BC, ....)
        String elementType = getElementType(dmElement);

        // Base on elementType, collect list of rule that support this type.
        if (recordsGroupByType_Id.containsKey(elementType)) {

            recordsGroupByType_Id.get(elementType).values().stream().forEach(record -> {
                result.add(record);
            });
        }
        return result;
    }

    /**
     * Return Element Type of data model object Because Record Type showed in
     * User Assistant is not same with model class -> Have to check instance of
     * Doors object and return the relate element type
     *
     * @param dmElement
     * @return
     */
    private String getElementType(DmElementI dmElement) {
        // Check if dmElement is supported type of Doors object
        if (dmElement instanceof DmDoorsObject_PTSA_1x_L1_REQ || dmElement instanceof DmDoorsObject_PTSA_1x_Requirement) {

            updateRq1Level(); // If this object is instance of CondigurationManagerRule_DoorsObject -> Rq1Level will be update
            String doorsRecordType = Utils.createDoorsRecordType((DmDoorsObject) dmElement);
            return doorsRecordType;
        }
        return dmElement.getElementConfigurationType();
    }

    private List<DmRq1Project> getAppliedProjects() {
        return ConfigurableRulesCustomizer.getAppliedProjectsGroupedById().values().stream().collect(Collectors.toList());
    }

    protected abstract boolean checkAssignedElementIsInTheGivenProject(DmRq1Project project);

    protected void updateRq1Level() {};
}
