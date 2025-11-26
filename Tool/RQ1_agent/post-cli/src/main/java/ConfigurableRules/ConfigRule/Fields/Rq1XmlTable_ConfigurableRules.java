/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Fields;

import ConfigurableRules.ConfigRule.Query.Query;
import ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord;
import ConfigurableRules.ConfigRule.util.KeyComparator;
import Rq1Cache.Monitoring.Rq1ParseFieldException;
import Rq1Cache.Types.Rq1XmlTable;
import Rq1Cache.Types.Rq1XmlTableColumn;
import Rq1Cache.Types.Rq1XmlTableColumn_CheckBox;
import Rq1Cache.Types.Rq1XmlTableColumn_String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvTableColumnI;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlParser;

/**
 *
 * @author RHO2HC
 */
public class Rq1XmlTable_ConfigurableRules extends Rq1XmlTable {

    public static final String TABLE_ATTRIBUTE_MARKERS = "Markers";
    public static final String TABLE_ATTRIBUTE_CRITERIAS = "Criterias";
    public static final String TABLE_ATTRIBUTE_COMMENT = "Comment";
    public static final String TABLE_ATTRIBUTE_CRITERIA_EDITOR = "Criteria Editor";
    public static final String TABLE_ATTRIBUTE_RECORD_TYPE = "Record Type";
    public static final String TABLE_ATTRIBUTE_NAME = "Name";
    public static final String TABLE_ATTRIBUTE_IS_APPLIED_ONLY_TO_BELOGING_PROJECT = "Applied Only To The Belonging Project";
    public static final String TABLE_ATTRIBUTE_ID = "ID";

    // Add column in Import or Export dialog
    public static final List<String> COLUMN_NAMES = Arrays.asList(TABLE_ATTRIBUTE_ID, TABLE_ATTRIBUTE_NAME, TABLE_ATTRIBUTE_RECORD_TYPE, TABLE_ATTRIBUTE_COMMENT,
            TABLE_ATTRIBUTE_CRITERIAS,
            TABLE_ATTRIBUTE_IS_APPLIED_ONLY_TO_BELOGING_PROJECT, TABLE_ATTRIBUTE_MARKERS);

    static final private String FIELDNAME_RULE_ID = "ruleId";
    static final private String FIELDNAME_NAME = "name";
    static final private String FIELDNAME_COMMENT = "comment";
    static final private String FIELDNAME_CRITERIA_EDITOR = "CriteriaEditor";
    static final private String FIELDNAME_RECORD_TYPE = "recordType";
    static final private String FIELDNAME_CRITERIA = "Criteria";
    static final private String FIELDNAME_MARKER = "Marker";
    static final private String FIELDNAME_IS_APPLIED_ONLY_TO_BELOGING_PROJECT = "isAppliedOnlyToBelongingProject";

    final public Rq1XmlTableColumn_String ID;
    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String RECORD_TYPE;
    final public Rq1XmlTableColumn_String COMMENT;
    final public Rq1XmlTableColumn_String CRITERIA_EDITOR;
    final public Rq1XmlTableColumn_String CRITERIAS;
    final public Rq1XmlTableColumn_String MARKERS;
    final public Rq1XmlTableColumn_CheckBox IS_APPLIED_ONLY_TO_BELONGING_PROJECT;

    private EcvTableData data;
    private Map<String, Map<String, ConfigurableRuleRecord>> recordsGroupByType_Id = new HashMap<>();
    private Map<String, String> ruleNamesIds = new HashMap<>();
    private List<String> recordTypes = new ArrayList<>();

    public Rq1XmlTable_ConfigurableRules() {

        // Add column in User Assistant dialog
        addXmlColumn(ID = new Rq1XmlTableColumn_String(TABLE_ATTRIBUTE_ID, 15, FIELDNAME_RULE_ID, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NAME = new Rq1XmlTableColumn_String(TABLE_ATTRIBUTE_NAME, 15, FIELDNAME_NAME, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(RECORD_TYPE = new Rq1XmlTableColumn_String(TABLE_ATTRIBUTE_RECORD_TYPE, 15, FIELDNAME_RECORD_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String(TABLE_ATTRIBUTE_COMMENT, 15, FIELDNAME_COMMENT, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(CRITERIA_EDITOR = new Rq1XmlTableColumn_String(TABLE_ATTRIBUTE_CRITERIA_EDITOR, 15, FIELDNAME_CRITERIA_EDITOR, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(CRITERIAS = new Rq1XmlTableColumn_String(TABLE_ATTRIBUTE_CRITERIAS, 15, FIELDNAME_CRITERIA, ColumnEncodingMethod.ELEMENT_LIST));
        addXmlColumn(MARKERS = new Rq1XmlTableColumn_String(TABLE_ATTRIBUTE_MARKERS, 15, FIELDNAME_MARKER, ColumnEncodingMethod.ELEMENT_LIST));
        addXmlColumn(IS_APPLIED_ONLY_TO_BELONGING_PROJECT = new Rq1XmlTableColumn_CheckBox(TABLE_ATTRIBUTE_IS_APPLIED_ONLY_TO_BELOGING_PROJECT, 7, FIELDNAME_IS_APPLIED_ONLY_TO_BELOGING_PROJECT, ColumnEncodingMethod.ATTRIBUTE,
                String.valueOf(true), String.valueOf(false), new String[]{String.valueOf(true)}, new String[]{String.valueOf(false)}));
        IS_APPLIED_ONLY_TO_BELONGING_PROJECT.setOptional();

        initRecordTypesMap();
    }

    @Override
    public void loadRowFromDb(EcvTableData data, EcvXmlElement rowElement) throws Rq1ParseFieldException {
        assert (data != null);
        assert (rowElement != null);

        if (this.data != null) {
            data = this.data;
        } else {
            //
            // Create new, empty row
            //
            if (rowElement instanceof EcvXmlContainerElement) {
                List<EcvXmlElement> rules = ((EcvXmlContainerElement) rowElement).getElementList("Rule");
                for (EcvXmlElement rule : rules) {
                    EcvTableRow newRow = data.createRow();

                    for (EcvTableColumnI ipeColumn : getColumns()) {
                        assert (ipeColumn instanceof Rq1XmlTableColumn) : ipeColumn.getUiName() + "- " + ipeColumn.getClass().getCanonicalName();
                        Rq1XmlTableColumn xmlColumn = (Rq1XmlTableColumn) ipeColumn;

                        switch (xmlColumn.getEncodingMethod()) {
                            case ATTRIBUTE:
                                //
                                // The value of the column is stored in an attribute of the row element.
                                //
                                String stringValue = rule.getAttribute(xmlColumn.getSourceName());
                                Object objectValue = xmlColumn.loadValueFromDatabase(stringValue);
                                if (objectValue == null) {
                                    if (xmlColumn.isOptional() == true) {
                                        if (xmlColumn instanceof Rq1XmlTableColumn_CheckBox) {
                                            ((Rq1XmlTableColumn_CheckBox)ipeColumn).setValue(newRow, false);
                                        } else {
                                            ((Rq1XmlTableColumn_CheckBox)ipeColumn).setValue(newRow, null);
                                        }
                                    } else {
                                        try {
                                            //
                                            // Attribute not set but mandatory field -> error
                                            //
                                            StringBuilder b = new StringBuilder(100);
                                            b.append("Error when parsing field ").append(ipeColumn.getUiName()).append(" in element ").append(rule.getName()).append(":\n");
                                            b.append("Attribute ").append(xmlColumn.getSourceName()).append(" missing.");
                                            throw (new Rq1ParseFieldException(b.toString()));
                                        } catch (Rq1ParseFieldException ex) {
                                            Logger.getLogger(Rq1XmlTable_ConfigurableRules.class.getName()).log(Level.SEVERE, null, ex);
                                            ToolUsageLogger.logError(Rq1XmlTable_ConfigurableRules.class.getName(), ex);
                                        }
                                    }
                                }
                                if (xmlColumn instanceof Rq1XmlTableColumn_CheckBox) {
                                    try {
                                        boolean booleanValue = ((Rq1XmlTableColumn_CheckBox) xmlColumn).convertToBoolean(stringValue != null ? stringValue : String.valueOf(false));
                                        ((Rq1XmlTableColumn_CheckBox)ipeColumn).setValue(newRow, booleanValue);
                                    } catch (Rq1ParseFieldException ex) {
                                        Logger.getLogger(Rq1XmlTable_ConfigurableRules.class.getName()).log(Level.SEVERE, null, ex);
                                        ToolUsageLogger.logError(Rq1XmlTable_ConfigurableRules.class.getName(), ex);
                                    }
                                } else {
                                    ((Rq1XmlTableColumn_String)ipeColumn).setValue(newRow, objectValue.toString());
                                }
                                break;

                            case ELEMENT_LIST:
                                //
                                // The value of the column is a list of strings stored in sub elements of the row element.
                                //
                                List<EcvXmlElement> l = ((EcvXmlContainerElement) rowElement).getElementList(xmlColumn.getSourceName())
                                        .stream().filter(element -> element.getAttribute(FIELDNAME_RULE_ID)
                                        .equals(newRow.getValueAt(0)))
                                        .collect(Collectors.toList());
                                ((Rq1XmlTableColumn_String)ipeColumn).setValue(newRow, convertXmlObjectsListToString(l));
                                break;

                            default:

                        }
                    }

                    data.addRow(newRow);
                }
            }
            if (data != null) {
                this.data = data;
            } else {
                this.data = createTableData();
            }
            initRecordsMap();
        }
    }

    private void initRecordTypesMap() {
        recordTypes = DmRq1RecordTypeList.getRecordTypes().keySet().stream().collect(Collectors.toList());
    }

    public void initRecordsMap() {
        ruleNamesIds = new HashMap<>();
        recordTypes.stream().forEach(type -> {
            recordsGroupByType_Id.put(type, getRowRecordsListByRecordType(type));
            recordsGroupByType_Id.get(type).values().stream().forEach(e -> {
                ruleNamesIds.put(e.getName(), e.getRuleId());
            });
        });
    }

    public List<ConfigurableRuleRecord> getRowRecordsListByColumn(EcvTableColumnI column, String value) {
        List<ConfigurableRuleRecord> result = new ArrayList<>();
        List<EcvTableRow> rows = data.getRows()
                .stream().filter(row -> String.valueOf(row.getValueAt(column)).equals(value))
                .collect(Collectors.toList());
        rows.stream().forEach(row -> {
            List<String> criteriaXmlStrings = Arrays.asList(String.valueOf(row.getValueAt(CRITERIAS)).split(",\n"));
            List<EcvXmlElement> criteriaXmlObjects = new ArrayList<>();
            EcvXmlParser parser = null;
            for (String xmlString : criteriaXmlStrings) {
                try {
                    parser = new EcvXmlParser(xmlString);
                    criteriaXmlObjects.add(parser.parse());
                } catch (Exception ex) {
                    Logger.getLogger(Rq1XmlTable_ConfigurableRules.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(Rq1XmlTable_ConfigurableRules.class.getName(), ex);
                }
            }

            List<String> markerXmlStrings = Arrays.asList(String.valueOf(row.getValueAt(MARKERS)).split(",\n"));
            List<EcvXmlElement> markerXmlObjects = new ArrayList<>();
            for (String xmlString : markerXmlStrings) {
                try {
                    parser = new EcvXmlParser(xmlString);
                    markerXmlObjects.add(parser.parse());
                } catch (Exception ex) {
                    Logger.getLogger(Rq1XmlTable_ConfigurableRules.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(Rq1XmlTable_ConfigurableRules.class.getName(), ex);
                }
            }

            boolean isAppliedOnlyToBelongingProject = String.valueOf(true).equals(String.valueOf(row.getValueAt(IS_APPLIED_ONLY_TO_BELONGING_PROJECT))) ? true : false;

            result.add(new ConfigurableRuleRecord(String.valueOf(row.getValueAt(ID)), String.valueOf(row.getValueAt(NAME)),
                    String.valueOf(row.getValueAt(RECORD_TYPE)), String.valueOf(row.getValueAt(COMMENT)), String.valueOf(row.getValueAt(CRITERIA_EDITOR)),
                    isAppliedOnlyToBelongingProject, criteriaXmlObjects, markerXmlObjects));
        });
        return result;
    }

    public Map<String, ConfigurableRuleRecord> getRowRecordsListByRecordType(String recordType) {
        List<ConfigurableRuleRecord> rules = getRowRecordsListByColumn(RECORD_TYPE, recordType);

        Map<String, ConfigurableRuleRecord> result = new HashMap<>();
        rules.stream().forEach(rule -> {
            result.put(rule.getRuleId(), rule);
        });
        return result;
    }

    public List<ConfigurableRuleRecord> getRowRecordsListByColumn(String columnName, String value) {
        return getRowRecordsListByColumn(getColumn(Arrays.asList(getColumnNames()).indexOf(columnName)), value);
    }

    public Map<String, String> getRuleNamesIds() {
        return ruleNamesIds;
    }

    public List<String> getRecordTypes() {
        return recordTypes;
    }

    public void setRecordTypes(List<String> recordTypesMap) {
        this.recordTypes = recordTypesMap;
    }

    public Map<String, Map<String, ConfigurableRuleRecord>> getRecordsGroupByType_Id() {
        return recordsGroupByType_Id;
    }

    public void setRecordsGroupByType_Id(Map<String, Map<String, ConfigurableRuleRecord>> recordsGroupByType_Name) {
        this.recordsGroupByType_Id = recordsGroupByType_Name;
    }

    public void addARowIntoDataModel(ConfigurableRuleRecord newRecord) {
        assert (newRecord != null);
        EcvTableRow newRow = createAnEcvTableRowFromRecord(newRecord);
        if (data == null) {
            data = createTableData();
        }
        data.addRow(newRow);
        initRecordsMap();
    }

    public void editARowInDataModel(ConfigurableRuleRecord oldRecord) {
        EcvTableRow row = createAnEcvTableRowFromRecord(oldRecord);
        data.getRows().remove(findIndexOfARowById(oldRecord.getRuleId()));
        data.addRow(row);
        initRecordsMap();
    }

    public void deleteARowOutDataModel(String id) {
        ConfigurableRuleRecord oldRecord = findARecordInRuleRecordsMapByRuleId(id);
        if (oldRecord != null) {
            data.getRows().remove(findIndexOfARowById(id));
            initRecordsMap();
        }
    }

    public int findIndexOfARowById(String id) {
        for (int i = 0; i < data.getRowCount(); i++) {
            if (data.getRows().get(i).getValueAt(ID).equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public ConfigurableRuleRecord findARecordInRuleRecordsMapByRuleId(String id) {
        for (Map<String, ConfigurableRuleRecord> map : recordsGroupByType_Id.values()) {
            if (map.containsKey(id)) {
                return map.get(id);
            }
        }
        return null;
    }

    public EcvTableRow createAnEcvTableRowFromRecord(ConfigurableRuleRecord record) {
//        EcvTableRow newRow = new EcvTableRow(this, data);
        EcvTableRow newRow = new EcvTableRow(this);
        ID.setValue(newRow, record.getRuleId());
        NAME.setValue(newRow, record.getName());
        RECORD_TYPE.setValue(newRow, record.getRecordType());
        COMMENT.setValue(newRow, record.getComment());
        CRITERIA_EDITOR.setValue(newRow, record.getCriteriaEditor());
        IS_APPLIED_ONLY_TO_BELONGING_PROJECT.setValue(newRow, record.isIsAppliedOnlyToBelongingProject());
        addEmptyXmlElementsListIntoARow(newRow, CRITERIAS, record.convertCriteriasToXmlObjects());
        addEmptyXmlElementsListIntoARow(newRow, MARKERS, record.convertMarkersToXmlObjects());
        return newRow;
    }

    private void addEmptyXmlElementsListIntoARow(EcvTableRow row, Rq1XmlTableColumn_String ipeColumn, List<EcvXmlEmptyElement> elements) {
        StringBuilder combinedValue = new StringBuilder();
        for (EcvXmlEmptyElement currentValue : elements) {
            if (combinedValue.length() > 0) {
                combinedValue.append(",\n");
            }
            combinedValue.append(((EcvXmlEmptyElement) currentValue).getXmlString());
        }

        ipeColumn.setValue(row, combinedValue.toString());
    }

    public EcvTableData getData() {
        return data;
    }

    public void setData(EcvTableData data) {
        this.data = data;
    }

    @Override
    public EcvXmlElement provideRowAsXmlElementForDb(String elementName, EcvTableRow row) {
        assert (elementName != null);
        assert (elementName.isEmpty() == false);
        assert (row != null);
        //
        // Create row element
        //
        EcvXmlElement rowElement = new EcvXmlContainerElement(elementName);
        recordsGroupByType_Id.values().stream().forEach(e -> {
            e.keySet().stream().forEach(key -> {
                ((EcvXmlContainerElement) rowElement).addElement(e.get(key).convertRuleToXmlObject());
                e.get(key).convertCriteriasToXmlObjects().stream().forEach(o -> {
                    ((EcvXmlContainerElement) rowElement).addElement(o);
                });
                e.get(key).convertMarkersToXmlObjects().stream().forEach(o -> {
                    ((EcvXmlContainerElement) rowElement).addElement(o);
                });
            });
        });

        return rowElement;
    }

    public Map<String, Map<String, ConfigurableRuleRecord>> getAllConfigRuleRecordsByQuery(Query query) {
        Map<String, Map<String, ConfigurableRuleRecord>> result = new HashMap<>();
        Map<String, List<ConfigurableRuleRecord>> tempMap = new HashMap<>();
        List<ConfigurableRuleRecord> configurableRuleRecords = new ArrayList<>();
        
        recordsGroupByType_Id.values().stream().forEach(map -> {
            configurableRuleRecords.addAll(map.values());
        });
        
        configurableRuleRecords.stream().forEach(configurableRuleRecord -> {
            
            if (configurableRuleRecord.checkCriterias(query.getCriterias())) {
                String groupName = configurableRuleRecord.getElementValueInXmlObject(query.getGrouping().getGroupedBy());
                
                if (groupName != null) {
                    if (tempMap.containsKey(groupName)) {
                        tempMap.get(groupName).add(configurableRuleRecord);
                    } else {
                        tempMap.put(groupName, new ArrayList<>());
                        tempMap.get(groupName).add(configurableRuleRecord);
                    }
                }
            }
        });
        tempMap.keySet().stream().forEach(key -> {
            Map<String, ConfigurableRuleRecord> values = tempMap.get(key).stream().collect(Collectors.toMap(ConfigurableRuleRecord::getName, value -> {
                return value;
            }));
            values = values.entrySet().stream().sorted(new KeyComparator<String, ConfigurableRuleRecord>(query.getSorter().getOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            result.put(key, values);
        });
        return result;
    }

    public String getRandomRuleId() {
        return String.valueOf(randomRuleId());
    }

    public int randomRuleId() {
        int id = 0;
        Random rand = new Random();
        if (getRuleNamesIds().isEmpty()) {
            id = rand.nextInt(100000);
        } else {
            List<String> ids = getRuleNamesIds().values().stream().collect(Collectors.toList());
            ids = ids.stream().sorted().collect(Collectors.toList());
            id = Integer.parseInt(ids.get(ids.size() - 1)) + 1;
        }
        return id;
    }

    public static String convertXmlObjectsListToString(List<? extends EcvXmlElement> xmObjects) {
        StringBuilder combinedValue = new StringBuilder();
        for (EcvXmlElement currentValue : xmObjects) {
            if (currentValue instanceof EcvXmlEmptyElement) {
                if (combinedValue.length() > 0) {
                    combinedValue.append(",\n");
                }
                combinedValue.append(((EcvXmlEmptyElement) currentValue).getXmlString());
            }
        }
        return combinedValue.toString();
    }

    public String[] getColumnNames() {
        List<EcvTableColumnI> columns = getColumns();
        String[] names = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            names[i] = columns.get(i).getUiName();
        }
        return (names);
    }
}
