/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_DateTime;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1HistoryLog;
import Rq1Data.Enumerations.LifeCycleState_HistoryLog;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import util.EcvEnumeration;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlParser;

/**
 *
 * @author gug2wi
 */
public class DmRq1HistoryLog extends DmRq1Element {
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DmRq1HistoryLog.class.getCanonicalName());
    
    final public DmRq1Field_Text ACTION_NAME;
    final public DmRq1Field_Text HISTORY_ID;
    final public DmRq1Field_Text HISTORY_LOG;
    final public DmRq1Field_DateTime LAST_MODIFIED_DATE;
    final public DmRq1Field_Text LAST_MODIFIED_USER;
    final public DmRq1Field_Text LIFE_CYCLE_STATE;
    final public DmRq1Field_Text PREVIOUS_LIFE_CYCLE_STATE;
    final public DmRq1Field_Text OPERATION_CONTEXT;
    final public DmRq1Field_Text OPERATION_MODE;
    
    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1HistoryLog(Rq1HistoryLog rq1HistoryLog) {
        super("RQ1-HISTORY-LOG", rq1HistoryLog);

        //
        // Create and add fields
        //
        addField(ACTION_NAME = new DmRq1Field_Text(this, rq1HistoryLog.ACTION_NAME, "Action Name"));
        addField(HISTORY_ID = new DmRq1Field_Text(this, rq1HistoryLog.HISTORY_ID, "Id"));
        addField(HISTORY_LOG = new DmRq1Field_Text(this, rq1HistoryLog.HISTORY_LOG, "Log"));
        addField(LAST_MODIFIED_DATE = new DmRq1Field_DateTime(this, rq1HistoryLog.LAST_MODIFIED_DATE, "Date"));
        addField(LAST_MODIFIED_USER = new DmRq1Field_Text(this, rq1HistoryLog.LAST_MODIFIED_USER, "User"));
        addField(LIFE_CYCLE_STATE = new DmRq1Field_Text(this, rq1HistoryLog.LIFE_CYCLE_STATE, "LifeCycleState"));
        addField(PREVIOUS_LIFE_CYCLE_STATE = new DmRq1Field_Text(this, rq1HistoryLog.PREVIOUS_LIFE_CYCLE_STATE, "Previous LifeCycleState"));
        addField(OPERATION_CONTEXT = new DmRq1Field_Text(this, rq1HistoryLog.OPERATION_CONTEXT, "Operation Context"));
        addField(OPERATION_MODE = new DmRq1Field_Text(this, rq1HistoryLog.OPERATION_MODE, "Operation Mode"));
    }
    
    @Override
    public boolean isCanceled() {
        return (false);
    }
    
    @Override
    public EcvEnumeration getLifeCycleState() {
        return (LifeCycleState_HistoryLog.CLOSED);
    }
    
    @Override
    public String getTitle() {
        return (ACTION_NAME.getValue());
    }
    
    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LifeCycleState_HistoryLog.values());
    }
    
    public String extractChangesFromHistoryLog() {
        
        StringBuilder historyLogParsed = new StringBuilder(50);
        String historyLog = HISTORY_LOG.getValue();
        String historyLogEnveloped = "<HISTORY_LOG>" + historyLog + "</HISTORY_LOG>";
        EcvXmlParser parser = new EcvXmlParser(historyLogEnveloped);
        try {
            EcvXmlElement root = parser.parse();
            if (root instanceof EcvXmlContainerElement) {
                for (EcvXmlContainerElement field : ((EcvXmlContainerElement) root).getContainerElementList("cq:FIELD")) {
                    if (field.containsElement("cq:SET")) {
                        historyLogParsed.append(field.getAttribute("name")).append("=\"").append(field.getText("cq:SET")).append("\" ");
                    } else if (field.containsElement("cq:ADDED")) {
                        historyLogParsed.append(field.getAttribute("name")).append(" added:").append(field.getText("cq:ADDED")).append(" ");
                    } else if (field.containsElement("cq:REMOVED")) {
                        historyLogParsed.append(field.getAttribute("name")).append(" removed:").append(field.getText("cq:REMOVED")).append(" ");
                    } else {
                        LOGGER.warning("Unexpected content: " + field.getXmlString());
                    }
                }
            }
        } catch (EcvXmlParser.ParseException | EcvXmlElement.NotfoundException ex) {
            LOGGER.warning("Couldn't parse HistoryLog for HistoryID = " + HISTORY_ID.getValue() + ": " + ex.getMessage());
            LOGGER.warning("=== HistoryLog:\n" + historyLog + "\n===============");
        }
        if (historyLogParsed.length() > 0) {
            return (historyLogParsed.toString());
        }
        
        return ("Couldn't parse entry.");
    }
    
    public Map<String, String> extractFieldsFromHistoryLog() {
        
        Map<String, String> fieldMap = new TreeMap<>();
        
        String historyLogEnveloped = "<HISTORY_LOG>" + HISTORY_LOG.getValue() + "</HISTORY_LOG>";
        EcvXmlParser parser = new EcvXmlParser(historyLogEnveloped);
        try {
            EcvXmlElement root = parser.parse();
            if (root instanceof EcvXmlContainerElement) {
                for (EcvXmlContainerElement field : ((EcvXmlContainerElement) root).getContainerElementList("cq:FIELD")) {
                    if (field.containsElement("cq:SET")) {
                        fieldMap.put(field.getAttribute("name"), field.getText("cq:SET"));
                    } else if (field.containsElement("cq:ADDED")) {
                        fieldMap.put(field.getAttribute("name"), field.getText("cq:ADDED"));
                    } else if (field.containsElement("cq:REMOVED")) {
                        fieldMap.put(field.getAttribute("name"), field.getText("cq:REMOVED"));
                    }
                }
            }
        } catch (EcvXmlParser.ParseException | EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.WARNING, "Couldn't parse HistoryLog for HistoryID = " + HISTORY_ID.getValue(), ex);
            LOGGER.warning("=== HistoryLog:");
            LOGGER.warning(HISTORY_LOG.getValue());
            LOGGER.warning("===============");
        }
        
        return (fieldMap);
    }
    
    @Override
    public String toString() {
        return ("HistoryLog: " + HISTORY_ID.getValueAsText() + "-" + LAST_MODIFIED_DATE.getValue().getRq1Value() + "-" + ACTION_NAME.getValueAsText());
    }
    
}
