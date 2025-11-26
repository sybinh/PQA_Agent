/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Records;

import ConfigurableRules.ConfigRule.Fields.Rq1XmlTable_ConfigurableRules;
import ConfigurableRules.ConfigRule.Query.Criteria;
import static ConfigurableRules.ConfigRule.Records.AbstractConfigurableRuleRecord.ATTRIBUTE_NAME;
import static ConfigurableRules.ConfigRule.Records.AbstractConfigurableRuleRecord.ATTRIBUTE_RULE_ID;
import ConfigurableRules.ConfigRule.util.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;

/**
 *
 * @author RHO2HC
 */
public class ConfigurableRuleRecord extends AbstractConfigurableRuleRecord implements Comparable<ConfigurableRuleRecord> {
   
    public static final String XML_GROUP_NAME = "IpeConfigurableRules";
    public static final String ATTRIBUTE_RECORD_TYPE = "recordType";
    public static final String ATTRIBUTE_COMMENT = "comment";
    public static final String ATTRIBUTE_IS_APPLIED_ONLY_TO_BELONGING_PROJECT = "isAppliedOnlyToBelongingProject";
    
    public static final List<String> fieldNames = Arrays.asList(ATTRIBUTE_NAME, ATTRIBUTE_RULE_ID, ATTRIBUTE_RECORD_TYPE, ATTRIBUTE_COMMENT);
    public static final String ATTRIBUTE_CRITERIA_EDITOR = "CriteriaEditor";
    public static final String ATTRIBUTE_CRITERIAS = "Criterias";
    public static final String ATTRIBUTE_MARKERS = "Markers";
    
    public static final Map<String, List<String>> fieldAndSubFieldNameMap = new HashMap<String, List<String>>() {{
        put(ATTRIBUTE_NAME, new ArrayList<>());
        put(ATTRIBUTE_RULE_ID, new ArrayList<>());
        put(ATTRIBUTE_RECORD_TYPE, new ArrayList<>());
        put(ATTRIBUTE_COMMENT, new ArrayList<>());
        put(ATTRIBUTE_CRITERIAS, Criteria.fieldNames);
        put(ATTRIBUTE_MARKERS, MarkerContent.fieldNames);
    }};
    
    private boolean isAppliedOnlyToBelongingProject = true;
    private String criteriaEditor;
    private String recordType;
    private String comment;
    private List<Criteria> criterias = new ArrayList<>();
    private List<MarkerContent> markers = new ArrayList<>();
    private EcvXmlEmptyElement xmlObject;
    
    public ConfigurableRuleRecord(List<Criteria> criterias, List<MarkerContent> markers, String ruleId
            , String name, String recordType, String comment, String criteriaEditor, boolean isAppliedOnlyToBelongingProject) {
        super(ruleId, name);
        this.recordType = recordType;
        this.comment = comment;
        this.criteriaEditor = criteriaEditor == null ? Constants.BLANK : criteriaEditor;
        this.criterias = criterias;
        this.markers = markers;
        this.isAppliedOnlyToBelongingProject = isAppliedOnlyToBelongingProject;
        convertRuleToXmlObject();
    }
    
    public ConfigurableRuleRecord(String ruleId, String name, String recordType, String comment, String criteriaEditor
            , boolean isAppliedOnlyToBelongingProject, List<EcvXmlElement> criterias, List<EcvXmlElement> markers) {
        super(ruleId, name);
        this.recordType = recordType;
        this.comment = comment;
        this.criteriaEditor = criteriaEditor == null ? Constants.BLANK : criteriaEditor;
        this.isAppliedOnlyToBelongingProject = isAppliedOnlyToBelongingProject;
        
        criterias.stream().forEach(criteria -> {
            if (criteria != null) {
                this.criterias.add(new Criteria((EcvXmlEmptyElement) criteria));
            }
        });
        markers.stream().forEach(marker -> {
            if (marker != null) {
                this.markers.add(new MarkerContent((EcvXmlEmptyElement) marker));
            }
        });
        
        convertRuleToXmlObject();
    }
    
    public ConfigurableRuleRecord(String ruleId, String name, String recordType
            , String comment, String criteriaEditor, boolean isAppliedOnlyToBelongingProject) {
        super(ruleId, name);
        this.recordType = recordType;
        this.comment = comment;
        this.criteriaEditor = criteriaEditor == null ? Constants.BLANK : criteriaEditor;
        this.isAppliedOnlyToBelongingProject = isAppliedOnlyToBelongingProject;
        convertRuleToXmlObject();
    }
    
    private List<String> importModifyDates(String modifyDatesAsString) {
        if(modifyDatesAsString == null || modifyDatesAsString.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(modifyDatesAsString.split(",")));
        }
    }

    public boolean isIsAppliedOnlyToBelongingProject() {
        return isAppliedOnlyToBelongingProject;
    }

    public void setIsAppliedOnlyToBelongingProject(boolean isAppliedOnlyToBelongingProject) {
        this.isAppliedOnlyToBelongingProject = isAppliedOnlyToBelongingProject;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getCriteriaEditor() {
        return criteriaEditor;
    }

    public void setCriteriaEditor(String criteriaEditor) {
        this.criteriaEditor = criteriaEditor;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

    public List<MarkerContent> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerContent> markers) {
        this.markers = markers;
    }
    
    
    public EcvXmlEmptyElement convertRuleToXmlObject() {
        EcvXmlEmptyElement result = new EcvXmlEmptyElement("Rule");
        result.addAttribute(ATTRIBUTE_RULE_ID, ruleId);
        result.addAttribute(ATTRIBUTE_NAME, name);
        result.addAttribute(ATTRIBUTE_RECORD_TYPE, recordType);
        result.addAttribute(ATTRIBUTE_COMMENT, comment);
        result.addAttribute(ATTRIBUTE_CRITERIA_EDITOR, criteriaEditor);
        result.addAttribute(ATTRIBUTE_IS_APPLIED_ONLY_TO_BELONGING_PROJECT, String.valueOf(isAppliedOnlyToBelongingProject));
        this.xmlObject = result;
        return result;
    }
    
    public List<EcvXmlEmptyElement> convertCriteriasToXmlObjects() {
        List<EcvXmlEmptyElement> result = new ArrayList<>();
        criterias.stream().forEach(criteria -> { 
            result.add(criteria.convertToXmlObject(ruleId));
        });
        return result;
    }
    
    public List<EcvXmlEmptyElement> convertMarkersToXmlObjects() {
        List<EcvXmlEmptyElement> result = new ArrayList<>();
        markers.stream().forEach(marker -> { 
            result.add(marker.convertToXmlObject(ruleId));
        });
        return result;
    }

    public boolean checkCriterias(List<Criteria> criterias) {
        boolean check = true;
        for(Criteria criteria : criterias) {
            check = criteria.checkValue(getElementValueInXmlObject(criteria.getField()));
            if (!check) {
                return false;
            }
        }
        return true;
    }
    
    public String getElementValueInXmlObject(String field) {
        if (xmlObject == null) {
            convertRuleToXmlObject();
        }
        if (xmlObject.hasAttribute(field)) {
            return xmlObject.getAttribute(field);
        }
        return null;
    }
    
    public List<String> getElementValuesListInXmlObject(String field, String subField) {
        List<String> result = new ArrayList<>();
        List<EcvXmlEmptyElement> xmlElements = new ArrayList<>();
        if (field.equals(ATTRIBUTE_CRITERIAS)) {
            xmlElements = convertCriteriasToXmlObjects();
        } else {
            xmlElements = convertMarkersToXmlObjects();
        }
        xmlElements.stream().forEach(element -> { 
            if (element.hasAttribute(subField)) {
                result.add(element.getAttribute(subField));
            }
        });
                 
        return result;
    }

    public String getUserActionSuggested() {
        // TO DO: We can add new field to define user suggested action
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.recordType);
        hash = 83 * hash + Objects.hashCode(this.comment);
        hash = 83 * hash + Objects.hashCode(this.criteriaEditor);
        hash = 83 * hash + Objects.hashCode(this.criterias);
        hash = 83 * hash + Objects.hashCode(this.markers);
        hash = 83 * hash + Objects.hashCode(this.xmlObject);
        hash = 83 * hash + Objects.hashCode(this.isAppliedOnlyToBelongingProject);
        return hash;
    }

    @Override
    public int compareTo(ConfigurableRuleRecord o) {
        return getName().compareTo(o.getName());
    }
    
    public EcvXmlContainerElement convertToXmlContainer() {
        EcvXmlContainerElement result = new EcvXmlContainerElement("Rule");
        result.addAttribute(ATTRIBUTE_RULE_ID, ruleId);
        result.addAttribute(ATTRIBUTE_NAME, name);
        result.addAttribute(ATTRIBUTE_RECORD_TYPE, recordType);
        result.addAttribute(ATTRIBUTE_COMMENT, comment);
        result.addAttribute(ATTRIBUTE_CRITERIA_EDITOR, criteriaEditor);
        result.addAttribute(ATTRIBUTE_IS_APPLIED_ONLY_TO_BELONGING_PROJECT, String.valueOf(isAppliedOnlyToBelongingProject));
        
        convertCriteriasToXmlObjects().stream().forEach(e -> {
            result.addElement(e);
        });
        convertMarkersToXmlObjects().stream().forEach(e -> {
            result.addElement(e);
        });
        return result;
    }
    
    public String[] convertToArray() {
        String criteriasString = Rq1XmlTable_ConfigurableRules.convertXmlObjectsListToString(convertCriteriasToXmlObjects());
        String markersString = Rq1XmlTable_ConfigurableRules.convertXmlObjectsListToString(convertMarkersToXmlObjects());
        
        return new String [] {getRuleId(), getName(), getRecordType(), getComment()
                , criteriasString, markersString, String.valueOf(isIsAppliedOnlyToBelongingProject())};
    }
}
