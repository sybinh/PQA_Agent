/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvEnumeration;
import util.EcvTableData;
import util.EcvTableRow;
import DataModel.Dgs.DmDgsIssueSW_I;
import DataModel.Rq1.Records.DmRq1Bc;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_CustomerResponse_Workitem extends Rq1XmlTable_CustomerResponse {

    //--------------------------------------------------------------------------
    //
    // Table definition
    //
    //--------------------------------------------------------------------------
    static public enum Type implements EcvEnumeration {

        ISSUE("I-SW"),
        BC("BC");

        //
        private final String dbText;

        private Type(String dbText) {
            assert (dbText != null);
            this.dbText = dbText;
        }

        @Override
        public String getText() {
            return dbText;
        }

        static public Type get(String typeString) {
            for (Type value : values()) {
                if (value.getText().equals(typeString) == true) {
                    return (value);
                }
            }
            return (null);
        }

    }

    static public enum State implements EcvEnumeration {

        OPEN("open"),
        REJECTED("rejected"),
        DONE("done");
        //
        private final String dbText;

        private State(String dbText) {
            assert (dbText != null);
            this.dbText = dbText;
        }

        @Override
        public String getText() {
            return dbText;
        }

        static public State get(String stateString) {
            for (State value : values()) {
                if (value.getText().equals(stateString) == true) {
                    return (value);
                }
            }
            return (null);
        }

    }

    final public Rq1XmlTableColumn_String REQUESTED_ACTION;
    final public Rq1XmlTableColumn_String AFFECTED_RQ1_ID;
    final public Rq1XmlTableColumn_ComboBox AFFECTED_RQ1_TYPE;
    final public Rq1XmlTableColumn_String AFFECTED_RQ1_TITLE;
    final public Rq1XmlTableColumn_ComboBox STATE;

    public Rq1XmlTable_CustomerResponse_Workitem() {

        addXmlColumn(AFFECTED_RQ1_TYPE = new Rq1XmlTableColumn_ComboBox("Type", 5, Type.values(), "type", ColumnEncodingMethod.ATTRIBUTE));
        AFFECTED_RQ1_TYPE.setOptional().setReadOnly();

        addXmlColumn(AFFECTED_RQ1_TITLE = new Rq1XmlTableColumn_String("Name", 10, "name", ColumnEncodingMethod.ATTRIBUTE));
        AFFECTED_RQ1_TITLE.setOptional().setReadOnly();

        addXmlColumn(AFFECTED_RQ1_ID = new Rq1XmlTableColumn_String("RQ1-ID", 10, "rq1id", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        AFFECTED_RQ1_ID.setOptional().setReadOnly();

        addXmlColumn(REQUESTED_ACTION = new Rq1XmlTableColumn_String("Action", 10, "requestedAction", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        REQUESTED_ACTION.setOptional().setReadOnly();

        addXmlColumn(STATE = new Rq1XmlTableColumn_ComboBox("Internal State", 5, State.values(), "internalState", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        STATE.setOptional();
    }

    //--------------------------------------------------------------------------
    //
    // Support for record based access
    //
    //--------------------------------------------------------------------------
    static public class Record extends Rq1XmlTable_CustomerResponse.Record {

        final private Type type;
        final private String title;
        final private String rq1Id;
        final private State state;
        final private String requestedAction;

        @SuppressWarnings("unchecked")
        private Record(EcvTableData data, EcvTableRow row) {
            super(data, row);
            assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_Workitem);

            Rq1XmlTable_CustomerResponse_Workitem d = (Rq1XmlTable_CustomerResponse_Workitem) data.getDescription();

            type = Type.get(d.AFFECTED_RQ1_TYPE.getValue(row));
            title = d.AFFECTED_RQ1_TITLE.getValue(row);
            rq1Id = d.AFFECTED_RQ1_ID.getValue(row);
            state = State.get(d.STATE.getValue(row));
            requestedAction = d.REQUESTED_ACTION.getValue(row);
        }

        private Record(Response response, DmDgsIssueSW_I affectedElement, Derivatives derivatives, String requestedAction) {
            super(response, derivatives);
            type = Type.ISSUE;
            title = affectedElement.getTitle();
            rq1Id = affectedElement.getId();
            state = State.OPEN;
            this.requestedAction = requestedAction;

        }

        private Record(Response response, DmRq1Bc affectedElement, Derivatives derivatives, String requestedAction) {
            super(response, derivatives);
            type = Type.BC;
            title = affectedElement.getTitle();
            rq1Id = affectedElement.getId();
            state = State.OPEN;
            this.requestedAction = requestedAction;

        }

        private Record(Record template, Derivatives derivatives) {
            super(template, derivatives);

            this.type = template.type;
            this.title = template.title;
            this.rq1Id = template.rq1Id;
            this.state = template.state;
            this.requestedAction = template.requestedAction;
        }

        /**
         * Appends the record to the given tableData.
         *
         * @param data
         */
        final public void appendToData(EcvTableData data) {
            assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_Workitem);

            Rq1XmlTable_CustomerResponse_Workitem d = (Rq1XmlTable_CustomerResponse_Workitem) data.getDescription();
            EcvTableRow newRow = data.createAndAddRow();
            setValues(newRow, d);
            d.AFFECTED_RQ1_TYPE.setValue(newRow, type.getText());
            d.AFFECTED_RQ1_TITLE.setValue(newRow, title);
            d.AFFECTED_RQ1_ID.setValue(newRow, rq1Id);
            d.STATE.setValue(newRow, state.getText());
            d.REQUESTED_ACTION.setValue(newRow, requestedAction);
        }

        final public Type getType() {
            return type;
        }

        final public String getTitle() {
            return title;
        }

        final public String getRq1Id() {
            return rq1Id;
        }

        final public State getState() {
            return state;
        }

        final public String getAction() {
            return requestedAction;
        }

        private List<Record> splitByDerivatives() {
            List<Record> result = new ArrayList<>();
            for (Derivatives derivative : getDerivatives().splitByDerivative()) {
                result.add(new Record(this, derivative));
            }
            return (result);
        }

        private boolean matchesResponse(Record other) {
            return (super.matchesResponse(other)
                    && (type == other.type)
                    && title.equals(other.title)
                    && rq1Id.equals(other.rq1Id)
                    && requestedAction.equals(other.requestedAction));
        }

        @Override
        public void addContentAsAttributes(EcvXmlElement xmlElement) {
            super.addContentAsAttributes(xmlElement);
            exportNonEmpty(xmlElement, "title", getTitle());
            exportNonEmpty(xmlElement, "internalState", getState().getText());
            exportNonEmpty(xmlElement, "id", getRq1Id());
            exportNonEmpty(xmlElement, "action", getAction());
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_Workitem);

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(new Record(data, row));
        }
        return (result);
    }

    static public boolean set(EcvTableData data, Collection<Record> newValues) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_Workitem);
        assert (newValues != null);

        //
        // Check if values are changed
        //
        if (newValues.size() == data.getRowCount()) {
            List<Record> oldValues = extract(data);
            oldValues.removeAll(newValues);
            if (oldValues.isEmpty() == true) {
                return (false);
            }
        }

        //
        // Set new value
        //
        data.clearRows();
        for (Record newRecord : newValues) {
            newRecord.appendToData(data);
        }

        return (true);
    }

    //--------------------------------------------------------------------------
    //
    // Support for setting of customer response
    //
    //--------------------------------------------------------------------------
    public static boolean addResponse(EcvTableData tableData, Response response, DmDgsIssueSW_I affectedIssue, Derivatives responseDerivatives, String requestedAction) {
        assert (tableData != null);
        assert (response != null);
        assert (affectedIssue != null);
        assert (responseDerivatives != null);
        assert (requestedAction != null);

        //
        // Extract records and split them by derivatives
        //
        List<Record> splitRecords = new ArrayList<>();
        for (Record record : extract(tableData)) {
            splitRecords.addAll(record.splitByDerivatives());
        }

        List<Derivatives> notProcessedDerivatives = responseDerivatives.splitByDerivative();
        List<Record> updatedRecords = new ArrayList<>();

        //
        // Loop over existing records and process them
        //
        for (Record record : splitRecords) {
            if ((record.getType() == Type.ISSUE) && (record.getRq1Id().equals(affectedIssue.getId()))) {
                if (responseDerivatives.contains(record.getDerivatives()) == true) {
                    notProcessedDerivatives.remove(record.getDerivatives());
                    if (record.matchesResponse(response) == true) {
                        // No change, because it fits to the response
                        updatedRecords.add(record);
                    } else {
                        // Replace with new record
                        updatedRecords.add(new Record(response, affectedIssue, record.getDerivatives(), requestedAction));
                    }
                } else {
                    // Not changed because not fitting by derivatives
                    updatedRecords.add(record);
                }
            } else {
                // Not changed because not the same I-SW
                updatedRecords.add(record);
            }
        }

        //
        // Create new records for missing derivatives
        //
        for (Derivatives derivative : notProcessedDerivatives) {
            updatedRecords.add(new Record(response, affectedIssue, derivative, requestedAction));
        }

        //
        // Consolidate records
        //
        for (int consolidateIndex = 0; consolidateIndex < updatedRecords.size(); consolidateIndex++) {
            Record consolidateRecord = updatedRecords.get(consolidateIndex);
            List<Record> toDelete = new ArrayList<>();
            for (int compareIndex = consolidateIndex + 1; compareIndex < updatedRecords.size(); compareIndex++) {
                Record compareRecord = updatedRecords.get(compareIndex);
                if (consolidateRecord.matchesResponse(compareRecord) == true) {
                    consolidateRecord.addDerivatives(compareRecord.getDerivatives());
                    toDelete.add(compareRecord);
                }
            }
            updatedRecords.removeAll(toDelete);
        }

        //
        // Create new table data
        //
        EcvTableData oldTableData = tableData.copy();
        set(tableData, updatedRecords);

        return (oldTableData.equals(tableData) == false);
    }

    public static boolean removeAction(EcvTableData tableData, Response response, DmDgsIssueSW_I affectedIssue, Derivatives responseDerivatives, String action) {
        assert (tableData != null);
        assert (response != null);
        assert (affectedIssue != null);
        assert (responseDerivatives != null);
        assert (action != null);

        //
        // Extract records and split them by derivatives
        //
        List<Record> splitRecords = new ArrayList<>();
        for (Record record : extract(tableData)) {
            splitRecords.addAll(record.splitByDerivatives());
        }

        List<Record> updatedRecords = new ArrayList<>();

        //
        // Loop over existing records and process them
        //
        for (Record record : splitRecords) {
            if ((record.getType() == Type.ISSUE) && (record.getRq1Id().equals(affectedIssue.getId()))) {
                if (responseDerivatives.contains(record.getDerivatives()) == true) {
                    if (action.equals(record.getAction()) == true) {
                        // Do not add record to updated record, because it is obsolet.
                    } else {
                        // No change, because it fits to the response
                        updatedRecords.add(record);
                    }
                } else {
                    // Not changed because not fitting by derivatives
                    updatedRecords.add(record);
                }
            } else {
                // Not changed because not the same I-SW
                updatedRecords.add(record);
            }
        }

        //
        // Consolidate records
        //
        for (int consolidateIndex = 0; consolidateIndex < updatedRecords.size(); consolidateIndex++) {
            Record consolidateRecord = updatedRecords.get(consolidateIndex);
            List<Record> toDelete = new ArrayList<>();
            for (int compareIndex = consolidateIndex + 1; compareIndex < updatedRecords.size(); compareIndex++) {
                Record compareRecord = updatedRecords.get(compareIndex);
                if (consolidateRecord.matchesResponse(compareRecord) == true) {
                    consolidateRecord.addDerivatives(compareRecord.getDerivatives());
                    toDelete.add(compareRecord);
                }
            }
            updatedRecords.removeAll(toDelete);
        }

        //
        // Create new table data
        //
        EcvTableData oldTableData = tableData.copy();
        set(tableData, updatedRecords);

        return (oldTableData.equals(tableData) == false);
    }

    public static boolean addResponse(EcvTableData tableData, Response response, DmRq1Bc affectedBc, Derivatives responseDerivatives, String requestedAction) {
        assert (tableData != null);
        assert (response != null);
        assert (affectedBc != null);
        assert (responseDerivatives != null);
        assert (requestedAction != null);

        //
        // Extract records and split them by derivatives
        //
        List<Record> splitRecords = new ArrayList<>();
        for (Record record : extract(tableData)) {
            splitRecords.addAll(record.splitByDerivatives());
        }

        List<Derivatives> notProcessedDerivatives = responseDerivatives.splitByDerivative();
        List<Record> updatedRecords = new ArrayList<>();

        //
        // Loop over existing records and process them
        //
        for (Record record : splitRecords) {
            if ((record.getType() == Type.BC) && (record.getRq1Id().equals(affectedBc.getId()))) {
                if (responseDerivatives.contains(record.getDerivatives()) == true) {
                    notProcessedDerivatives.remove(record.getDerivatives());
                    if (record.matchesResponse(response) == true) {
                        // No change, because it fits to the response
                        updatedRecords.add(record);
                    } else {
                        // Replace with new record
                        updatedRecords.add(new Record(response, affectedBc, record.getDerivatives(), requestedAction));
                    }
                } else {
                    // Not changed because not fitting by derivatives
                    updatedRecords.add(record);
                }
            } else {
                // Not changed because not the same I-SW
                updatedRecords.add(record);
            }
        }

        //
        // Create new records for missing derivatives
        //
        for (Derivatives derivative : notProcessedDerivatives) {
            updatedRecords.add(new Record(response, affectedBc, derivative, requestedAction));
        }

        //
        // Consolidate records
        //
        for (int consolidateIndex = 0; consolidateIndex < updatedRecords.size(); consolidateIndex++) {
            Record consolidateRecord = updatedRecords.get(consolidateIndex);
            List<Record> toDelete = new ArrayList<>();
            for (int compareIndex = consolidateIndex + 1; compareIndex < updatedRecords.size(); compareIndex++) {
                Record compareRecord = updatedRecords.get(compareIndex);
                if (consolidateRecord.matchesResponse(compareRecord) == true) {
                    consolidateRecord.addDerivatives(compareRecord.getDerivatives());
                    toDelete.add(compareRecord);
                }
            }
            updatedRecords.removeAll(toDelete);
        }

        //
        // Create new table data
        //
        EcvTableData oldTableData = tableData.copy();
        set(tableData, updatedRecords);

        return (oldTableData.equals(tableData) == false);
    }

    public static boolean removeAction(EcvTableData tableData, Response response, DmRq1Bc affectedBc, Derivatives responseDerivatives, String action) {
        assert (tableData != null);
        assert (response != null);
        assert (affectedBc != null);
        assert (responseDerivatives != null);
        assert (action != null);

        //
        // Extract records and split them by derivatives
        //
        List<Record> splitRecords = new ArrayList<>();
        for (Record record : extract(tableData)) {
            splitRecords.addAll(record.splitByDerivatives());
        }

        List<Record> updatedRecords = new ArrayList<>();

        //
        // Loop over existing records and process them
        //
        for (Record record : splitRecords) {
            if ((record.getType() == Type.BC) && (record.getRq1Id().equals(affectedBc.getId()))) {
                if (responseDerivatives.contains(record.getDerivatives()) == true) {
                    if (action.equals(record.getAction()) == true) {
                        // Do not add record to updated record, because it is obsolet.
                    } else {
                        // No change, because it fits to the response
                        updatedRecords.add(record);
                    }
                } else {
                    // Not changed because not fitting by derivatives
                    updatedRecords.add(record);
                }
            } else {
                // Not changed because not the same I-SW
                updatedRecords.add(record);
            }
        }

        //
        // Consolidate records
        //
        for (int consolidateIndex = 0; consolidateIndex < updatedRecords.size(); consolidateIndex++) {
            Record consolidateRecord = updatedRecords.get(consolidateIndex);
            List<Record> toDelete = new ArrayList<>();
            for (int compareIndex = consolidateIndex + 1; compareIndex < updatedRecords.size(); compareIndex++) {
                Record compareRecord = updatedRecords.get(compareIndex);
                if (consolidateRecord.matchesResponse(compareRecord) == true) {
                    consolidateRecord.addDerivatives(compareRecord.getDerivatives());
                    toDelete.add(compareRecord);
                }
            }
            updatedRecords.removeAll(toDelete);
        }

        //
        // Create new table data
        //
        EcvTableData oldTableData = tableData.copy();
        set(tableData, updatedRecords);

        return (oldTableData.equals(tableData) == false);
    }

}
