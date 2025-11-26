/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Fields;

import ConfigurableRules.ConfigRule.Enumerations.CriteriaFieldType;
import ConfigurableRules.ConfigRule.Enumerations.SorterOrder;
import ConfigurableRules.ConfigRule.Query.Criteria;
import ConfigurableRules.ConfigRule.util.Constants;
import ConfigurableRules.ConfigRule.util.KeyComparator;
import DataModel.DmElement;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmFieldI;
import DataModel.DmValueFieldI;
import DataModel.DmValueFieldI_Date;
import DataModel.DmValueFieldI_Enumeration;
import DataModel.Doors.Records.DmDoorsElement.ElementType;
import DataModel.Doors.Records.DmDoorsObject_PTSA_1x_L1_REQ;
import DataModel.Doors.Records.DmDoorsObject_PTSA_1x_Requirement;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1Attachment;
import DataModel.Rq1.Records.DmRq1BcCollection;
import DataModel.Rq1.Records.DmRq1BcRelease;
import DataModel.Rq1.Records.DmRq1FcCollection;
import DataModel.Rq1.Records.DmRq1FcRelease;
import DataModel.Rq1.Records.DmRq1Irm_Bc_IssueFd;
import DataModel.Rq1.Records.DmRq1Irm_Bc_IssueSw;
import DataModel.Rq1.Records.DmRq1Irm_Fc_IssueFd;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1PvarCollection;
import DataModel.Rq1.Records.DmRq1PvarRelease;
import DataModel.Rq1.Records.DmRq1PverCollection;
import DataModel.Rq1.Records.DmRq1PverRelease;
import DataModel.Rq1.Records.DmRq1Rrm_Bc_Fc;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Doors.DoorsObject;
import Rq1Cache.Records.Rq1Attachment;
import Rq1Cache.Records.Rq1FcRelease;
import Rq1Cache.Records.Rq1Irm_Bc_IssueFd;
import Rq1Cache.Records.Rq1Irm_Bc_IssueSw;
import Rq1Cache.Records.Rq1Irm_Fc_IssueFd;
import Rq1Cache.Records.Rq1Irm_Pst_IssueSw;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1PvarCollection;
import Rq1Cache.Records.Rq1PverCollection;
import Rq1Cache.Records.Rq1Rrm_Bc_Fc;
import Rq1Cache.Records.Rq1Rrm_Pst_Bc;
import Rq1Cache.Records.Rq1WorkItem;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.Category_WorkItem;
import Rq1Data.Enumerations.Domain_WorkItem;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author RHO2HC
 */
public class DmRq1RecordTypeList {

    public static final String BRACKET = "(";
    public static final String HYPHEN_SYMBOL = "_";
    private static Map<String, DmElement> recordTypes = null;

    static public Map<String, DmElement> getRecordTypes() {

        if (recordTypes == null) {
            recordTypes = new TreeMap<>();

            // Add RQ1 Record Type -------------------------------------------
            addToMap(DmRq1BcRelease.create());
            addToMap(DmRq1BcCollection.create());
            addToMap(DmRq1FcCollection.create());
            addToMap(new DmRq1FcRelease(new Rq1FcRelease()));
            addToMap(new DmRq1Irm_Bc_IssueFd(new Rq1Irm_Bc_IssueFd()));
            addToMap(new DmRq1Irm_Bc_IssueSw(new Rq1Irm_Bc_IssueSw()));
            addToMap(new DmRq1Irm_Fc_IssueFd(new Rq1Irm_Fc_IssueFd()));
            addToMap(new DmRq1Irm_Pst_IssueSw(new Rq1Irm_Pst_IssueSw()));
            addToMap(new DmRq1IssueFD(new Rq1IssueFD()));
            addToMap(DmRq1IssueSW.create());
            addToMap(new DmRq1PvarCollection(new Rq1PvarCollection()));
            addToMap(new DmRq1PverCollection(new Rq1PverCollection()));
            addToMap(DmRq1PvarRelease.create());
            addToMap(DmRq1PverRelease.create());
            addToMap(new DmRq1Rrm_Bc_Fc(new Rq1Rrm_Bc_Fc()));
            addToMap(new DmRq1Rrm_Pst_Bc(new Rq1Rrm_Pst_Bc()));
            addToMap(new DmRq1WorkItem("Workitem", new Rq1WorkItem(Rq1NodeDescription.BC_COLLECTION, Domain_WorkItem.EMPTY, Category_WorkItem.EMPTY, SubCategory_WorkItem.CUST_PRJ)) {
                @Override
                public DmRq1WorkItem cloneWorkItem() {
                    return this;
                }
            });

            
            // L1 Objects
            DoorsObject doorsObjL1 = new DoorsObject();            
            doorsObjL1.addUserDefinedFields(createUserDefinedFields());
            DmDoorsObject_PTSA_1x_L1_REQ doors1xL1FuncReq = new DmDoorsObject_PTSA_1x_L1_REQ(ElementType.REQUIREMENT_WITHOUT_URL, doorsObjL1);
            recordTypes.put(String.format(Constants.DOORS_RECORD_TYPE, Constants.SW, 1), doors1xL1FuncReq);

            // L2 L3 L4 Objects (They use the same model)
            DoorsObject doorsObjLx = new DoorsObject();            
            doorsObjLx.addUserDefinedFields(createUserDefinedFields());
            DmDoorsObject_PTSA_1x_Requirement doors1xReq = new DmDoorsObject_PTSA_1x_Requirement(ElementType.REQUIREMENT_WITHOUT_URL, doorsObjLx);
            recordTypes.put(String.format(Constants.DOORS_RECORD_TYPE, Constants.SW, 2), doors1xReq);
            recordTypes.put(String.format(Constants.DOORS_RECORD_TYPE, Constants.FD, 3), doors1xReq);
            recordTypes.put(String.format(Constants.DOORS_RECORD_TYPE, Constants.FD, 4), doors1xReq);
        }
        return (recordTypes);

    }

    static private void addToMap(DmElement newElement) {
        recordTypes.put(newElement.getElementType(), newElement);
    }

    /**
     * Get a map <String, FieldNameObject> contain field names. These field
     * names will be showed in UI, in combo box Field Name
     *
     * @param selectedRecordType
     * @return
     */
    @SuppressWarnings("unchecked")
    static public Map<String, FieldNameObject> getFieldNameObjectsSuggestedForUser(String selectedRecordType) {
        Map<String, FieldNameObject> tempMap = new HashMap<>();
        
        if (!selectedRecordType.isEmpty()) {
            
            for (DmFieldI f : getRecordTypes().get(selectedRecordType).getFields()) {

                DmFieldI field = (DmFieldI) f;
                
                // Not support field with type Module (Doors)
                if(field.hasAttribute(DmFieldI.Attribute.IGNORE_FOR_CONFIG_RULES)) {
                    continue;
                }
                
                // If 'field' is a list of data (Ex: Attachment has list of file) ----------------------------------------
                if (field instanceof DmRq1Field_ReferenceList) {
                    DmRq1Field_ReferenceList list = (DmRq1Field_ReferenceList) field;

                    // Only add field to list if relate to Attachment data
                    if (field.hasAttribute(DmFieldI.Attribute.FIELD_ATTACHMENT_FOR_CONFIG_RULES)) {
                        list.addElement(new DmRq1Attachment(new Rq1Attachment()));
                    }

                    if (!list.getElementList().isEmpty()) {
                        ((DmElement) list.getElementList().get(0)).getFields().stream()
                                .forEach(e -> {
                                    putNewElementIntoSuggestedFieldNameObjectsMap(tempMap, list, e, true);
                                });
                    }

                // If 'field' is instanceof DmElementField_ReadOnlyI -----------------------------------------------------
                } else if (field instanceof DmElementField_ReadOnlyI) {
                    DmElementField_ReadOnlyI elementField = (DmElementField_ReadOnlyI) field;

                    if (elementField.getElement() != null) {
                        elementField.getElement().getFields().stream()
                                .forEach(e -> {
                                    putNewElementIntoSuggestedFieldNameObjectsMap(tempMap, elementField, e, true);
                                });
                    }

                // If 'field' is a list of mapped RQ1 artifact -> out of scope -> not show this rule to UI------------------
                } else if (field instanceof DmRq1Field_MappedReferenceList) {

                // Text / Date / Enum / Table / ... ------------------------------------------------------------------------
                } else {
                    putNewElementIntoSuggestedFieldNameObjectsMap(tempMap, field, field, false);
                }
            }
        }
        Map<String, FieldNameObject> result = tempMap.entrySet().stream().sorted(new KeyComparator<String, FieldNameObject>(SorterOrder.ASC))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return result;
    }

    /**
     * Create newFieldNameObject then put to list. The list will be showed in
     * combo box Field Name
     *
     * @param resultMap
     * @param field
     * @param subField
     * @param hasSubField
     */
    static private void putNewElementIntoSuggestedFieldNameObjectsMap(Map<String, FieldNameObject> resultMap, DmFieldI field, DmFieldI subField, boolean hasSubField) {
        FieldNameObject newFieldNameObject = new FieldNameObject();
        String subType = getTypeOfField(subField);
        String nameInDatabase = hasSubField ? field.getNameForUserInterface() + HYPHEN_SYMBOL + subField.getNameForUserInterface() : subField.getNameForUserInterface();
        newFieldNameObject.setName(nameInDatabase);
        String uiName = field.getNameForUserInterface().contains(BRACKET)
                ? field.getNameForUserInterface().substring(0, field.getNameForUserInterface().indexOf(BRACKET))
                : field.getNameForUserInterface();
        String nameForUserInterface = hasSubField ? uiName + HYPHEN_SYMBOL + subField.getNameForUserInterface() : uiName;
        newFieldNameObject.setNameForUserInterface(nameForUserInterface);
        newFieldNameObject.setType(subType);
        newFieldNameObject.setOperators(getOperatorOfFieldType(newFieldNameObject));

        boolean canGetValue = false;

        if (hasSubField) {
            // In case a field doesn't exist in RQ1 but defy only for IPE
            if (subField instanceof DmValueFieldI_Enumeration) {
                newFieldNameObject.setRangeForEnumObject(((DmValueFieldI_Enumeration) subField).getValidInputValues());
                canGetValue = true;

            } else if (subField instanceof DmValueFieldI) {
                newFieldNameObject.setRangeForEnumObject(Criteria.getValuesArrayOfEcvEnumerationClass(((DmValueFieldI) subField).getValue()));
                canGetValue = true;
            }

        } else {
            // In case a field doesn't exist in RQ1 but defy only for IPE
            if (field instanceof DmValueFieldI_Enumeration) {
                newFieldNameObject.setRangeForEnumObject(((DmValueFieldI_Enumeration) field).getValidInputValues());
                canGetValue = true;

            } else if (field instanceof DmValueFieldI /*&& ((DmValueFieldI) field).getValue() != null*/) {
                newFieldNameObject.setRangeForEnumObject(Criteria.getValuesArrayOfEcvEnumerationClass(((DmValueFieldI) field).getValue()));
                canGetValue = true;

            }

            // Recheck if still not get enum values
            if (field instanceof DmRq1Field_Enumeration
                    && newFieldNameObject.getRangeForEnumObject() != null && newFieldNameObject.getRangeForEnumObject().length == 1
                    && newFieldNameObject.getRangeForEnumObject()[0].getText().isEmpty()) {

                newFieldNameObject.setRangeForEnumObject(Criteria.getValuesArrayOfEcvEnumerationClass(((DmValueFieldI) field).getValue()));
                canGetValue = true;
            }
        }

        if (canGetValue) {
            resultMap.put(newFieldNameObject.getNameForUserInterface(), newFieldNameObject);
        }
    }

    static private String getTypeOfField(DmFieldI field) {
        if (field instanceof DmRq1Field_Date || field instanceof DmValueFieldI_Date) {
            return CriteriaFieldType.Date.getName();
        } else if (field instanceof DmRq1Field_Enumeration || field instanceof DmValueFieldI_Enumeration) {
            return CriteriaFieldType.Enum.getName();
        }
        return CriteriaFieldType.Text.getName();
    }

    static private List<String> getOperatorOfFieldType(FieldNameObject fieldNameObject) {
        CriteriaFieldType type = CriteriaFieldType.enumOf(fieldNameObject.getType());
        return type != null ? Arrays.asList(type.getOperators()) : new ArrayList<>();
    }
    
    /**
     * When create temp doors requirement object to get fields, it show unexpected logging
     * "WARNUNG: Description: The attributes .... are missing in the DOORS object."
     * To solve this, these fields have to be set to DoorsObject (input into DmDoorsObject_PTSA_1x_L1_REQ & DmDoorsObject_PTSA_1x_Requirement)
     * Because unable to automatically get all the fields -> manually create
     * @return List of userDefiedField for DoorsObject.
     */
    static private Map<String, String> createUserDefinedFields() {
        Map<String, String> userDefinedFields = new HashMap<>();
        userDefinedFields.put("Safety Classification", "");
        userDefinedFields.put("ASIL", "");
        userDefinedFields.put("Comment", "");
        userDefinedFields.put("Internal Comment", "");
        userDefinedFields.put("Acceptance Criteria", "");
        userDefinedFields.put("AffectedComponent_PS-EC", "");
        userDefinedFields.put("Allocation", "");
        userDefinedFields.put("CRQ_PS-EC", "");
        userDefinedFields.put("Description (main language)", "");
        userDefinedFields.put("EditState_PS-EC", "");
        userDefinedFields.put("Status", "");
        userDefinedFields.put("Elicitation Comment", "");
        userDefinedFields.put("Elicitation Features", "");
        userDefinedFields.put("External Review Comment", "");
        userDefinedFields.put("ObjectType_PS-EC", "");
        userDefinedFields.put("ReviewComment", "");
        userDefinedFields.put("Tags", "");
        userDefinedFields.put("VAR_FUNC_SW", "");
        userDefinedFields.put("VAR_FUNC_SYS", "");
        userDefinedFields.put("ObjectType", "");
        userDefinedFields.put("CRQ", "");
        userDefinedFields.put("Description (en)", "");
        userDefinedFields.put("Qualified for Regression Test", "");
        userDefinedFields.put("VerificationCriteria", "");
        return userDefinedFields;
    }
}
