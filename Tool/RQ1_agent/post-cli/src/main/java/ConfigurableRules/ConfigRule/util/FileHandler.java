/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.util;

import ConfigurableRules.ConfigRule.Query.Criteria;
import static ConfigurableRules.ConfigRule.Records.AbstractConfigurableRuleRecord.ATTRIBUTE_NAME;
import ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord;
import static ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord.ATTRIBUTE_COMMENT;
import static ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord.ATTRIBUTE_CRITERIA_EDITOR;
import static ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord.ATTRIBUTE_RECORD_TYPE;
import ConfigurableRules.ConfigRule.Records.MarkerContent;
import DataModel.Rq1.Records.DmRq1Project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlParser;

/**
 *
 * @author RHO2HC
 */
public class FileHandler {
    private static Map<String, Object> fileContentMap = new HashMap<>();

    public static Map<String, Object> getFileContentMap() {
        synchronized(fileContentMap) {
            return fileContentMap;
        }
    }
    
    public static URL getFileURL(String fileName, Class classObject) {
        return classObject.getResource(fileName);
    }
    
    public static String readAllByFileReader(String filePath) {
        String result = "";
        File file = new File(filePath);

        if (file.exists()) {
            try {
                result = new String(Files.readAllBytes(Paths.get(file.toURI())));
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(FileHandler.class.getName(), ex);
            }
        }
        
        synchronized(fileContentMap) {
            fileContentMap.put(file.getName(), result);
        }
        return result;
    }
    
    public static List<String> readLinesByInputStream(String fileName, Class classObject) {
        List<String> lines = new ArrayList<>();
        InputStream inputStream = classObject.getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = null;

        try {
            if (inputStreamReader.ready()) {
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    if (!"".equals(line)) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(FileHandler.class.getName(), ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(FileHandler.class.getName(), ex);
            } 
        }
        
        synchronized(fileContentMap) {
            fileContentMap.put(fileName, lines);
        }
        return lines;
    }
    
    public static List<String> readLinesByFileReader(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            if (file.exists()) {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    if (!"".equals(line)) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(FileHandler.class.getName(), ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(FileHandler.class.getName(), ex);
            } 
        }
        
        synchronized(fileContentMap) {
            fileContentMap.put(file.getName(), lines);
        }
        return lines;
    }
    
    public static void writeAppend(String filePath, String content) {
        File file = new File(filePath);
        synchronized(file) {
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;

            try {
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } 
                fileWriter = new FileWriter(file, true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.append(content);
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(FileHandler.class.getName(), ex);
            } finally {
                try {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException ex) { 
                    Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(FileHandler.class.getName(), ex);
                } 
            }
        }
    }
    
    public static void writeMultiLines(String filePath, List<String> lines) {
        File file = new File(filePath);
        synchronized(file) {
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;

            try {
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                fileWriter = new FileWriter(filePath);
                bufferedWriter = new BufferedWriter(fileWriter);
                for(String line: lines) {
                    bufferedWriter.append(line + "\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(FileHandler.class.getName(), ex);
            } finally {
                try {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException ex) { 
                    Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(FileHandler.class.getName(), ex);
                } 
            }
        }
    }
    
    public static Object readXmlToListObject(String filePath, Class ouputClass) {
//        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance(ouputClass);
//            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//            File importFile = new File(filePath);
//            String contentFile = readAllByFileReader(filePath);
//            if (importFile.exists() && !contentFile.isEmpty()) {
//                Object result = unmarshaller.unmarshal(importFile);
//                synchronized(fileContentMap) {
//                    fileContentMap.put(importFile.getName(), result);
//                }
//                return result;
//            }
//        } catch (JAXBException ex) {
//            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return null;
    }
    
    public static void writeXmlToListObject(String filePath, Object object, Class ouputClass) {
//        try {
//            JAXBContext jc = JAXBContext.newInstance(ouputClass);
//
//            Marshaller marshaller = jc.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            File file = new File(filePath);
//            if (!file.exists()) {
//                file.getParentFile().mkdirs();
//                file.createNewFile();
//            }
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//            marshaller.marshal(object, writer);
//            writer.close();
//        } catch (JAXBException | IOException ex) {
//            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public static List<String> removeRulesOfProjectById(List<String> lines, String projectId) {
        boolean projectFound = false;
        List<String> newLines = new ArrayList<>();
        
        for(String line : lines) {
            
            // If found the tag of input project, set projectFound = true -> ignore these lines
            if(line.equals(String.format(Constants.OPEN_TAG, projectId))) {
                projectFound = true;
            }
            
            // Keep the line that not belong to input project
            if(!projectFound) {
                newLines.add(line);
            }
            
            // If found the end tag of input project, set projectFound = false -> keep the next lines
            if(line.equals(String.format(Constants.END_TAG, projectId))) {
                projectFound = false;
            }
        }
        return newLines;
    }
    
    
    public static void exportRulesTableToXmlFile(String filePath, List<String> ruleIds, DmRq1Project project) {
        if (ruleIds == null) {
            return;
        }
            
        String configRuleOpenTag = String.format(Constants.OPEN_TAG, ConfigurableRuleRecord.XML_GROUP_NAME);
        String configRuleEndTag = String.format(Constants.END_TAG, ConfigurableRuleRecord.XML_GROUP_NAME);
        List<String> lines = new ArrayList<>();

        lines.add(configRuleOpenTag);

        ruleIds.stream().forEach(id -> {
            ConfigurableRuleRecord record = project.getConfigurableRuleProjectManager().getTableConfigurableRule().getTableDescription().findARecordInRuleRecordsMapByRuleId(id);
            if (record != null) {
                lines.add(record.convertToXmlContainer().getXmlString());
            }
        });

        lines.add(configRuleEndTag);

        FileHandler.writeMultiLines(filePath, lines);
    }
    
    
    public static void exportRulesTableToXmlCacheFile(String filePath, List<String> ruleIds, DmRq1Project project) {
        List<String> fileContent = new ArrayList<>();
        String configRuleEndTag = String.format(Constants.END_TAG, ConfigurableRuleRecord.XML_GROUP_NAME);
        
        // Read file, remove rules of current project
        File file = new File(filePath);
        if(file.isFile()) {
            fileContent = ConfigurableRules.ConfigRule.util.FileHandler.readLinesByFileReader(filePath);
            fileContent = ConfigurableRules.ConfigRule.util.FileHandler.removeRulesOfProjectById(fileContent, project.getId());
            
        }
        
        // If file not exist -> need to write new -> add Open Tag to first line
        if(fileContent.isEmpty()) {
            String configRuleOpenTag = String.format(Constants.OPEN_TAG, ConfigurableRuleRecord.XML_GROUP_NAME);
            fileContent.add(configRuleOpenTag);
        }
        
        
        // If current project has no rule -> write without append anything
        if(ruleIds != null && ruleIds.isEmpty()) {
            
            fileContent.remove(configRuleEndTag); // Remove the old end tag (in any place of the file)
            fileContent.add(configRuleEndTag);   // Add end tag (in the end of file)
            ConfigurableRules.ConfigRule.util.FileHandler.writeMultiLines(filePath, fileContent);
        
        // If current project has rule -> append new rule -> write
        } else if (ruleIds != null) {
            
            fileContent.add(String.format(Constants.OPEN_TAG, project.getId()));
            
            for(String id : ruleIds) {
                ConfigurableRuleRecord record = project.getConfigurableRuleProjectManager().getTableConfigurableRule().getTableDescription().findARecordInRuleRecordsMapByRuleId(id);
                if (record != null) {
                    fileContent.add(record.convertToXmlContainer().getXmlString());
                }
            }

            fileContent.add(String.format(Constants.END_TAG, project.getId()));

            fileContent.remove(configRuleEndTag); // Remove the old end tag (in any place of the file)
            fileContent.add(configRuleEndTag);   // Add end tag (in the end of file)
            ConfigurableRules.ConfigRule.util.FileHandler.writeMultiLines(filePath, fileContent);
        }
    }
    
    public static List<EcvXmlElement> readRuleList(File file) {
        try {
            EcvXmlParser parser = new EcvXmlParser(file);
            EcvXmlElement fileElement = parser.parse();

            if (!(fileElement instanceof EcvXmlContainerElement)) {
                return null;
            }

            // Get container element <IpeConfigurableRules>
            EcvXmlContainerElement configRulesContainerElement = (EcvXmlContainerElement) fileElement;

            // Get list of element <Rule>
            if(!configRulesContainerElement.containsElement(Constants.TAG_NAME_RULE)) {
                return null;
            }
            List<EcvXmlElement> ruleElements = configRulesContainerElement.getElementList(Constants.TAG_NAME_RULE);
            return ruleElements;
            
        } catch (Exception e) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(FileHandler.class.getName(), e);
        }
        
        return null;
    }
    
    public static List<EcvXmlElement> readRuleListByProjectId(File file, String projectId) {
        if(!file.isFile()) {
            return null;
        }
        
        try {
            EcvXmlParser parser = new EcvXmlParser(file);
            EcvXmlElement fileElement = parser.parse();

            if (!(fileElement instanceof EcvXmlContainerElement)) {
                return null;
            }

            // Get container element <IpeConfigurableRules>
            EcvXmlContainerElement configRulesContainerElement = (EcvXmlContainerElement) fileElement;

            // Get element <project Id>
            if (!configRulesContainerElement.containsContainer(projectId)) {
                return null;
            }
            EcvXmlContainerElement projectContainerElement = configRulesContainerElement.getContainerElement(projectId);

            // Get list of element <Rule>
            if(!projectContainerElement.containsElement(Constants.TAG_NAME_RULE)) {
                return null;
            }
            List<EcvXmlElement> ruleElements = projectContainerElement.getElementList(Constants.TAG_NAME_RULE);
            return ruleElements;
            
        } catch (Exception e) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(FileHandler.class.getName(), e);
        }
        return null;
    }
    
    
    public static Map<String, ConfigurableRuleRecord> collectRuleRecordMap(DmRq1Project project, File file, boolean isManualImport) {
        // Map of rule records <rule id : rule>
        Map<String, ConfigurableRuleRecord> ruleRecordsMap = new HashMap<>();
        
        try {
            List<EcvXmlElement> ruleElements;
            
            // If user manually import rule file, get all rule from file
            if(isManualImport) {
                ruleElements = readRuleList(file);
            // If user open the tool, get rules belong to input project
            } else {
                ruleElements = readRuleListByProjectId(file, project.getId());
            }
            
            if (ruleElements == null || ruleElements.isEmpty()) {
                return ruleRecordsMap;
            }
            
            // Import rules
            int id = project.getConfigurableRuleProjectManager().getTableConfigurableRule().getTableDescription().randomRuleId() - 1;

            for (EcvXmlElement ruleElement: ruleElements) {
                if (ruleElement instanceof EcvXmlContainerElement) {
                    EcvXmlContainerElement container = (EcvXmlContainerElement) ruleElement;
                    List<EcvXmlElement> criterias = container.getElementList(Criteria.CRITERIA);
                    List<EcvXmlElement> markers = container.getElementList(MarkerContent.MARKER);
                    id++;
                    String idString = String.valueOf(id);
                    String ruleName = container.getAttribute(ATTRIBUTE_NAME);

                    boolean isAppliedOnlyToBelongingProject = String.valueOf(true).equals(container.getAttribute(ConfigurableRuleRecord.ATTRIBUTE_IS_APPLIED_ONLY_TO_BELONGING_PROJECT)) ? true : false;

                    ConfigurableRuleRecord newRecord = new ConfigurableRuleRecord(idString, ruleName, container.getAttribute(ATTRIBUTE_RECORD_TYPE), 
                                                                                  container.getAttribute(ATTRIBUTE_COMMENT), container.getAttribute(ATTRIBUTE_CRITERIA_EDITOR),
                                                                                    isAppliedOnlyToBelongingProject, criterias, markers);
                    ruleRecordsMap.put(idString, newRecord);
                }
            }
            
        } catch (Exception e) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(FileHandler.class.getName(), e);
        }
        return ruleRecordsMap;
    }
}
