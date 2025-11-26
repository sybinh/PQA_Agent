/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.Flow.FlowBlocker;
import DataModel.Flow.FlowBlockerImpl;
import DataModel.Flow.FlowBoardTaskSeverity;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableColumnI;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author mos8cob
 */
public class Rq1XmlTable_FlowBlocker extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String ID;
    final public Rq1XmlTableColumn_String BLOCKERID;
    final public Rq1XmlTableColumn_String ASSIGNEE;
    final public Rq1XmlTableColumn_String START_DATE;
    final public Rq1XmlTableColumn_String END_DATE;
    final public Rq1XmlTableColumn_ComboBox SEVERITY;
    final public Rq1XmlTableColumn_String COMMENT;
    final public Rq1XmlTableColumn_String NO_OF_DAYS;

    public Rq1XmlTable_FlowBlocker() {

        addXmlColumn(BLOCKERID = new Rq1XmlTableColumn_String("Blocker Id", "B_Id", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        BLOCKERID.setVisibility(EcvTableColumnI.Visibility.ALWAYS_HIDDEN);
        addXmlColumn(ID = new Rq1XmlTableColumn_String("Subtask Id", "STask_Id", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        ID.setVisibility(EcvTableColumnI.Visibility.ALWAYS_HIDDEN);
        addXmlColumn(ASSIGNEE = new Rq1XmlTableColumn_String("Assignee", "Assign", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        ASSIGNEE.setVisibility(EcvTableColumnI.Visibility.ALWAYS_HIDDEN);
        addXmlColumn(START_DATE = new Rq1XmlTableColumn_String("Blocker set Date", 10, "Strt_D", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(END_DATE = new Rq1XmlTableColumn_String("Blocker End Date", 10, "End_D", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(SEVERITY = new Rq1XmlTableColumn_ComboBox("Severity", 10, FlowBoardTaskSeverity.values(), "Sev", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String("Comment", 100, "Cmt", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NO_OF_DAYS = new Rq1XmlTableColumn_String("Number of days", "Nod", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        NO_OF_DAYS.setVisibility(EcvTableColumnI.Visibility.ALWAYS_HIDDEN);
        END_DATE.setOptional();
        COMMENT.setOptional();
        NO_OF_DAYS.setOptional();

    }

    /**
     * To get all blocker
     *
     * @param row
     * @return
     */
    final public FlowBlocker createFlowBlocker(EcvTableRow row) {
        assert (row.getValueAt(BLOCKERID) != null);
        assert (row.getValueAt(ID) != null);
        assert (row.getValueAt(ASSIGNEE) != null);
        assert (row.getValueAt(START_DATE) != null);
        assert (row.getValueAt(END_DATE) != null);
        assert (row.getValueAt(SEVERITY) != null);
        assert (row.getValueAt(COMMENT) != null);
        assert (row.getValueAt(NO_OF_DAYS) != null);

        FlowBlocker subtask = new FlowBlockerImpl(row.getValueAt(BLOCKERID).toString(), row.getValueAt(ID).toString(), row.getValueAt(ASSIGNEE).toString(), row.getValueAt(START_DATE).toString(), row.getValueAt(END_DATE).toString(), FlowBoardTaskSeverity.valueOf(row.getValueAt(SEVERITY).toString()), row.getValueAt(COMMENT).toString(), row.getValueAt(NO_OF_DAYS).toString());

        return subtask;
    }

    final public List<FlowBlocker> createFlowBlocker(EcvTableData data) {
        assert (data != null);

        List<FlowBlocker> flowBlocker = new ArrayList<>();

        for (EcvTableRow row : data.getRows()) {
            assert (row.getValueAt(BLOCKERID) != null);
            assert (row.getValueAt(ID) != null);
            assert (row.getValueAt(ASSIGNEE) != null);
            assert (row.getValueAt(SEVERITY) != null);
            assert (row.getValueAt(START_DATE) != null);

            String startDate = "";
            String endDate = "";
            FlowBoardTaskSeverity severity = FlowBoardTaskSeverity.NONE;
            String comment = "";

            if (row.getValueAt(START_DATE) != null) {
                startDate = row.getValueAt(START_DATE).toString();
            }
            if (row.getValueAt(END_DATE) != null) {
                endDate = row.getValueAt(END_DATE).toString();
            }
            if (row.getValueAt(SEVERITY) != null) {
                severity = getSeverity(row.getValueAt(SEVERITY).toString());
            }
            if (row.getValueAt(COMMENT) != null) {
                comment = row.getValueAt(COMMENT).toString();
            }

            FlowBlocker subtask = new FlowBlockerImpl(row.getValueAt(BLOCKERID).toString(), row.getValueAt(ID).toString(), row.getValueAt(ASSIGNEE).toString(), startDate, endDate, severity, comment, row.getValueAt(NO_OF_DAYS).toString());
            flowBlocker.add(subtask);

        }
        return flowBlocker;

    }

    public void removeBlockerEntries(EcvTableData data) {
        assert (data != null);
        data.clearRows();
    }

    final public EcvTableRow updateBlocker(FlowBlocker blocker, EcvTableData data, String subTaskId) {
        assert (data != null);
        assert (blocker != null);
        EcvTableRow updatedRow = null;

        for (EcvTableRow row : data.getRows()) {

            if (ID.getValue(row).equals(subTaskId) && BLOCKERID.getValue(row).equals(blocker.getBlockerId())) {
                ASSIGNEE.setValue(row, blocker.getAssignee());
                START_DATE.setValue(row, blocker.getStartDate());
                END_DATE.setValue(row, blocker.getEndDate());
                if (blocker.getSeverity() != null) {
                    SEVERITY.setValue(row, blocker.getSeverity().getText());
                }
                COMMENT.setValue(row, blocker.getComment());
                NO_OF_DAYS.setValue(row, blocker.getNoOfDays());

                updatedRow = row;
                break;
            }

        }
        return updatedRow;

    }

    final public List<FlowBlocker> getBlocker(String subTaskId, EcvTableData data) {
        assert (data != null);
        List<FlowBlocker> blockerList = new ArrayList<>();

        FlowBlocker blocker = null;
        for (EcvTableRow row : data.getRows()) {
            assert (row.getValueAt(BLOCKERID) != null);
            assert (row.getValueAt(ID) != null);
            assert (row.getValueAt(ASSIGNEE) != null);
            assert (row.getValueAt(START_DATE) != null);
            assert (row.getValueAt(SEVERITY) != null);

            String startDate = "";
            String endDate = "";
            FlowBoardTaskSeverity severity = FlowBoardTaskSeverity.NONE;
            String comment = "";

            if (row.getValueAt(START_DATE) != null) {
                startDate = row.getValueAt(START_DATE).toString();
            }
            if (row.getValueAt(END_DATE) != null) {
                endDate = row.getValueAt(END_DATE).toString();
            }
            if (row.getValueAt(SEVERITY) != null) {
                severity = getSeverity(row.getValueAt(SEVERITY).toString());
            }
            if (row.getValueAt(COMMENT) != null) {
                comment = row.getValueAt(COMMENT).toString();
            }
            if (row.getValueAt(ID).toString().equals(subTaskId)) {
                blocker = new FlowBlockerImpl(row.getValueAt(BLOCKERID).toString(), row.getValueAt(ID).toString(), row.getValueAt(ASSIGNEE).toString(), startDate, endDate, severity, comment, row.getValueAt(NO_OF_DAYS).toString());
                blockerList.add(blocker);
            }

        }
        return blockerList;

    }

    final public EcvTableRow addBlocker(FlowBlocker blocker) {
        assert (blocker != null);

        EcvTableData data = createTableData();

        EcvTableRow row = data.createRow();
        BLOCKERID.setValue(row, blocker.getBlockerId());
        ID.setValue(row, blocker.getSubTaskId());
        ASSIGNEE.setValue(row, blocker.getAssignee());
        START_DATE.setValue(row, blocker.getStartDate());
        END_DATE.setValue(row, blocker.getEndDate());
        SEVERITY.setValue(row, blocker.getSeverity().getText());
        COMMENT.setValue(row, blocker.getComment());
        NO_OF_DAYS.setValue(row, blocker.getNoOfDays());

        data.addRow(row);

        return (row);
    }

    private FlowBoardTaskSeverity getSeverity(String strSeverity) {
        FlowBoardTaskSeverity severity = FlowBoardTaskSeverity.NONE;
        switch (strSeverity) {
            case "None":
                severity = FlowBoardTaskSeverity.NONE;
                break;
            case "Internal":
                severity = FlowBoardTaskSeverity.INTERNAL;
                break;
            case "External":
                severity = FlowBoardTaskSeverity.EXTERNAL;
                break;

        }
        return severity;
    }

}
