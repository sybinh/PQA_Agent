/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules;

import ConfigurableRules.ConfigRule.Fields.DmRq1Field_ConfigurableRulesTable;
import ConfigurableRules.ConfigRule.Fields.Rq1XmSubField_ConfigurableRulesTable;
import ConfigurableRules.ConfigRule.Fields.Rq1XmlTable_ConfigurableRules;
import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleListener;
import ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord;
import ConfigurableRules.ConfigRule.util.Constants;
import ConfigurableRules.ConfigRule.util.FileHandler;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Records.Rq1Project;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import util.UiWorker;
import util.UiWorkerManager;

/**
 *
 * @author GUG2WI
 */
public class ConfigurableRule_ProjectManager {

    private final DmRq1Project project;
    private final DmRq1Field_ConfigurableRulesTable tableConfigurableRule;
    private final Map<String, List<ConfigurableRuleListener>> configRuleListenerMap = new HashMap<>();
    private boolean isImportRules = false;
    
    
    public ConfigurableRule_ProjectManager(DmRq1Project project, Rq1Project rq1Project) {
        assert (project != null);
        this.project = project;
        
        Rq1XmlTable_ConfigurableRules configRulesTable = new Rq1XmlTable_ConfigurableRules();
        Rq1XmlSubField_Table<Rq1XmlTable_ConfigurableRules> configurableRules 
                = new Rq1XmSubField_ConfigurableRulesTable(rq1Project, configRulesTable, rq1Project.TAGS, ConfigurableRuleRecord.XML_GROUP_NAME);
        
        tableConfigurableRule = new DmRq1Field_ConfigurableRulesTable(project, configurableRules, Constants.USER_ASSISTANT_RULES);
    }

    public DmRq1Project getProject() {
        return project;
    }

    public synchronized void  importUserAssistantRules(boolean isOpenFromProject) {
        if(isImportRules) {
            return;
        }
        
        File ruleFile = new File(Constants.RULE_CACH_PATH);
        
        if(!ruleFile.exists()) {
            return;
        }
        
        // Import xml file.
        Map<String, ConfigurableRuleRecord> ruleRecordsMap = FileHandler.collectRuleRecordMap(project, ruleFile, false);

        // Set isImportRules = true -> mean already import rule
        isImportRules = true;
        
        // Import selected rules into Table
        List<ConfigurableRuleRecord> ruleRecordList = new ArrayList<>();
        ruleRecordsMap.keySet().stream().forEach(k -> ruleRecordList.add(ruleRecordsMap.get(k)));
        importRecordsListIntoTable(ruleRecordList, isOpenFromProject);
    }
    
    public void importRecordsListIntoTable(List<ConfigurableRuleRecord> ruleRecordList, boolean isOpenFromProject) {
        if (!ruleRecordList.isEmpty()) {
            ruleRecordList.stream().forEach(record -> {
                
                if (tableConfigurableRule.getTableDescription().getRuleNamesIds().containsKey(record.getName())) {
                    // Append " (Duplicate Name)" to the rule name
                    record.setName(record.getName() + Constants.SPACE + Constants.DUPLICATE_NAME);
                }
                
                if(isOpenFromProject) {
                    putARuleIntoConfigRuleTable(record);
                } else {
                    tableConfigurableRule.getTableDescription().addARowIntoDataModel(record);
                }
            });
        }
    }

    public synchronized void putARuleIntoConfigRuleTable(ConfigurableRuleRecord newRecord) {
        //save new rule into project object

        tableConfigurableRule.getTableDescription().addARowIntoDataModel(newRecord);

        UiWorker<Void> worker = new UiWorker<Void>("Add rule: " + newRecord.getName()) {
            @Override
            protected Void backgroundTask() {

                UiWorkerManager.setMyTaskAction(UiWorkerManager.getMyTaskAction());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                }

                if (configRuleListenerMap.containsKey(newRecord.getRecordType())) {
                    configRuleListenerMap.get(newRecord.getRecordType()).stream().forEach(listener -> {
                        listener.addARule(newRecord, project);
                    });
                }
                return null;
            }
        };
        worker.execute();

    }

    public synchronized void editARuleInConfigRuleTable(ConfigurableRuleRecord editedRecord) {
        //save edited rule into project object

        tableConfigurableRule.getTableDescription().editARowInDataModel(editedRecord);

        UiWorker<Void> worker = new UiWorker<Void>("Edit rule: " + editedRecord.getName()) {
            @Override
            protected Void backgroundTask() {

                UiWorkerManager.setMyTaskAction(UiWorkerManager.getMyTaskAction());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                }

                if (configRuleListenerMap.containsKey(editedRecord.getRecordType())) {
                    configRuleListenerMap.get(editedRecord.getRecordType()).stream().forEach(listener -> {
                        listener.editARule(editedRecord, project);
                    });
                }
                return null;
            }

        };
        worker.execute();
    }

    public synchronized void removeARuleOutConfigRuleTable(String ruleId) {
        //remove this rule out of project object

        ConfigurableRuleRecord ruleRecord = tableConfigurableRule.getTableDescription().findARecordInRuleRecordsMapByRuleId(ruleId);
        if (ruleRecord != null) {
            String recordType = ruleRecord.getRecordType();
            tableConfigurableRule.getTableDescription().deleteARowOutDataModel(ruleId);

            UiWorker<Void> worker = new UiWorker<Void>("Remove rule: " + ruleRecord.getName()) {
                @Override
                protected Void backgroundTask() {

                    UiWorkerManager.setMyTaskAction(UiWorkerManager.getMyTaskAction());
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (Exception e) {
                    }

                    if (configRuleListenerMap.containsKey(recordType)) {
                        configRuleListenerMap.get(recordType).stream().forEach(listener -> {
                            listener.removeARule(recordType, ruleId);
                        });
                    }
                    return null;
                }
            };
            worker.execute();
        }

    }

    //--------------------------------------------------------------------------
    //
    // Listener management
    //
    //--------------------------------------------------------------------------
    public void addConfigurableRuleListener(String recordType, ConfigurableRuleListener listener) {
        if (configRuleListenerMap.containsKey(recordType)) {
            configRuleListenerMap.get(recordType).add(listener);
        } else {
            List<ConfigurableRuleListener> newList = new ArrayList<>();
            newList.add(listener);
            configRuleListenerMap.put(recordType, newList);
        }
    }

    public void setdActiveConfigurableRule(boolean activate) {
        configRuleListenerMap.values().stream().forEach(listeners -> {
            listeners.stream().forEach(listener -> {
                if (activate) {
                    listener.active();
                } else {
                    listener.deactive();
                }
            });
        });
    }

    public DmRq1Field_ConfigurableRulesTable getTableConfigurableRule() {
        return tableConfigurableRule;
    }
    
    public boolean isImportRules() {
        return isImportRules;
    }
}
