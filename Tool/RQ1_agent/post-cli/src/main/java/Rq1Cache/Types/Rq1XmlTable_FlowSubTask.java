/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.Flow.DMFlowSubtaskImpl;
import DataModel.Flow.DmSubtask;
import DataModel.Flow.FlowBlockerBg;
import DataModel.Flow.FlowBoardTaskSeverity;
import DataModel.Flow.TaskRemovalStatus;
import DataModel.Flow.TaskStatus;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1SubjectElement;
import DataModel.Rq1.Records.DmRq1WorkItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;
import util.EcvTableColumnI.Visibility;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * <p>
 * - Encoding and decoding of the hardware field in the ProPlaTo-Tags
 * </p>
 * <p>
 * - Facility methods to access the hardware settings for ProPlaTo.
 * </p>
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_FlowSubTask extends Rq1XmlTable {

    //
    // Example of SubTask structure:
    //
    //    <FLOW>
    //        <SubTask Status="ToDo" Assignee="gey3si">Title of the subtask number 1</SubTask>
    //        <SubTask Status="Done" Assignee="gey3si">Title of the subtask number 2</SubTask>
    //    </FLOW>
    //
    final public Rq1XmlTableColumn_ComboBox STATUS;
    final public Rq1XmlTableColumn_String ASSIGNEE;
    final public Rq1XmlTableColumn_String TITLE;
    final public Rq1XmlTableColumn_String ID;
    final public Rq1XmlTableColumn_String START_DATE;
    final public Rq1XmlTableColumn_String END_DATE;
    final public Rq1XmlTableColumn_ComboBox SEVERITY;
    final public Rq1XmlTableColumn_String COMMENT;
    final public Rq1XmlTableColumn_String BLOCKER_START_DATE;
    final public Rq1XmlTableColumn_String NO_OF_DAYS;
    final public Rq1XmlTableColumn_ComboBox TASK_REMOVAL_STATUS;
    final public Rq1XmlTableColumn_String EFFORTS;
    final public Rq1XmlTableColumn_ComboBox BLOCKER_BG;
    final public Rq1XmlTableColumn_String IN_PROG_DATE;
    final public Rq1XmlTableColumn_String IN_REVW_DATE;
    final public Rq1XmlTableColumn_String REM_EFFORTS;
    final public Rq1XmlTableColumn_String TARGET_DATE;
    final public Rq1XmlTableColumn_String EXPERT_STATE;
    final public Rq1XmlTableColumn_String IS_DISABLED;
    final public Rq1XmlTableColumn_String SPRINT;

    public Rq1XmlTable_FlowSubTask() {

        addXmlColumn(STATUS = new Rq1XmlTableColumn_ComboBox("Task Status", 60, TaskStatus.values(), "Status", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(ASSIGNEE = new Rq1XmlTableColumn_String("Assignee", 70, "Assignee", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(TITLE = new Rq1XmlTableColumn_String("Title", 100, "-", ColumnEncodingMethod.CONTENT));
        addXmlColumn(ID = new Rq1XmlTableColumn_String("Id", "Id", ColumnEncodingMethod.ATTRIBUTE));
        ID.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(START_DATE = new Rq1XmlTableColumn_String("Creation Date", "Start_Date", ColumnEncodingMethod.ATTRIBUTE));
        START_DATE.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(END_DATE = new Rq1XmlTableColumn_String("End Date", 50, "End_Date", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(SEVERITY = new Rq1XmlTableColumn_ComboBox("Blocker", 20, FlowBoardTaskSeverity.values(), "Severity", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String("Comment", 50, "Comment", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(BLOCKER_START_DATE = new Rq1XmlTableColumn_String("Blocker Start Date", "Blocker_Start_Date", ColumnEncodingMethod.ATTRIBUTE));
        BLOCKER_START_DATE.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(NO_OF_DAYS = new Rq1XmlTableColumn_String("No Of Days", "No_Of_Days", ColumnEncodingMethod.ATTRIBUTE));
        NO_OF_DAYS.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(TASK_REMOVAL_STATUS = new Rq1XmlTableColumn_ComboBox("Task Removal Status", TaskRemovalStatus.values(), "Task_Removal_Status", ColumnEncodingMethod.ATTRIBUTE));
        TASK_REMOVAL_STATUS.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(EFFORTS = new Rq1XmlTableColumn_String("Total Effort", 30, "R_Eff", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(BLOCKER_BG = new Rq1XmlTableColumn_ComboBox("Blocker Background Color", FlowBlockerBg.values(), "Bg", ColumnEncodingMethod.ATTRIBUTE));
        BLOCKER_BG.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(IN_PROG_DATE = new Rq1XmlTableColumn_String("Start Date", 40, "Prog_D", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(IN_REVW_DATE = new Rq1XmlTableColumn_String("Start review Date", "Rev_D", ColumnEncodingMethod.ATTRIBUTE));
        IN_REVW_DATE.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(REM_EFFORTS = new Rq1XmlTableColumn_String("Remaining Effort", 60, "Rem_Eff", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(TARGET_DATE = new Rq1XmlTableColumn_String("Planned-Date", "T_Dt", ColumnEncodingMethod.ATTRIBUTE));
        TARGET_DATE.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(EXPERT_STATE = new Rq1XmlTableColumn_String("Experts State", "Expert", ColumnEncodingMethod.ATTRIBUTE));
        EXPERT_STATE.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(IS_DISABLED = new Rq1XmlTableColumn_String("Disable", "Disable", ColumnEncodingMethod.ATTRIBUTE));
        IS_DISABLED.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(SPRINT = new Rq1XmlTableColumn_String("Sprint", "Sprint", ColumnEncodingMethod.ATTRIBUTE));
        SPRINT.setVisibility(Visibility.ALWAYS_HIDDEN);
        START_DATE.setOptional();
        END_DATE.setOptional();
        SEVERITY.setOptional();
        COMMENT.setOptional();
        BLOCKER_START_DATE.setOptional();
        NO_OF_DAYS.setOptional();
        TASK_REMOVAL_STATUS.setOptional();
        EFFORTS.setOptional();
        BLOCKER_BG.setOptional();
        IN_PROG_DATE.setOptional();
        IN_REVW_DATE.setOptional();
        REM_EFFORTS.setOptional();
        TARGET_DATE.setOptional();
        EXPERT_STATE.setOptional();
        IS_DISABLED.setOptional();
        SPRINT.setOptional();
        //  setMaxRowCount(100);
    }

//    /**
//     * Extracts a map that contains the hardware assigned to derivatives from
//     * the given table data.
//     *
//     * @param data Table data that contain the mapping.
//     * @return Map with the derivate name as key and the hardware string as
//     * value.
//     */
//    final public Map<String, String> createSubTaskMap(EcvTableData data) {
//        assert (data != null);
//        assert (data.getDescription() instanceof Rq1XmlTable_FlowSubTask);
//
//        Map<String, String> map = new TreeMap<>();
//
//        for (Row row : data.getRows()) {
//            String statusName = row.getValueAt(STATUS).toString();
//            assert (statusName != null);
//            String assignee = row.getValueAt(ASSIGNEE).toString();
//            if (assignee != null) {
//                map.put(derivateName, hardware);
//            }
//        }
//
//        return (map);
//    }
    /**
     * Creates data based on the given subtask object.
     *
     * @param subtasks
     * @return Table data containing the settings from the given map.
     */
    final public EcvTableData setSubTasks(Set<DmSubtask> subtasks) {
        assert (subtasks != null);

        EcvTableData data = createTableData();

        for (DmSubtask entry : subtasks) {
            EcvTableRow row = data.createRow();
            ID.setValue(row, entry.getID());
            STATUS.setValue(row, entry.getTaskStatus().getText());
            ASSIGNEE.setValue(row, entry.getAssignee());
            TITLE.setValue(row, entry.getTitle());
            START_DATE.setValue(row, entry.getStartDate());
            END_DATE.setValue(row, entry.getEndDate());
            SEVERITY.setValue(row, entry.getSeverity().getText());
            COMMENT.setValue(row, entry.getComment());
            BLOCKER_START_DATE.setValue(row, entry.getBlockerStartDate());
            NO_OF_DAYS.setValue(row, entry.getNoOfDays());
            TASK_REMOVAL_STATUS.setValue(row, entry.getTaskRemovalStatus().getText());
            EFFORTS.setValue(row, entry.getEfforts());
            BLOCKER_BG.setValue(row, entry.getBlockerBg().getText());
            IN_PROG_DATE.setValue(row, entry.getInProgDate());
            IN_REVW_DATE.setValue(row, entry.getInRevDate());
            REM_EFFORTS.setValue(row, entry.getRemEfforts());
            TARGET_DATE.setValue(row, entry.getTargetDate());
            EXPERT_STATE.setValue(row, entry.getExpertState());
            IS_DISABLED.setValue(row, entry.isDisabled());
            SPRINT.setValue(row, entry.getSprint());

            data.addRow(row);
        }

        return (data);
    }

    /**
     * Creates data based on the given subtask object.
     *
     * @param subtask
     * @return Table data containing the settings from the given map.
     */
    final public EcvTableRow addSubTask(DmSubtask subtask) {
        assert (subtask != null);

        EcvTableData data = createTableData();

        EcvTableRow row = data.createRow();
        ID.setValue(row, subtask.getID());
        STATUS.setValue(row, subtask.getTaskStatus().getText());
        ASSIGNEE.setValue(row, subtask.getAssignee());
        TITLE.setValue(row, subtask.getTitle());
        START_DATE.setValue(row, subtask.getStartDate());
        END_DATE.setValue(row, subtask.getEndDate());
        SEVERITY.setValue(row, subtask.getSeverity().getText());
        COMMENT.setValue(row, subtask.getComment());
        BLOCKER_START_DATE.setValue(row, subtask.getBlockerStartDate());
        NO_OF_DAYS.setValue(row, subtask.getNoOfDays());
        TASK_REMOVAL_STATUS.setValue(row, subtask.getTaskRemovalStatus().getText());
        EFFORTS.setValue(row, subtask.getEfforts());
        BLOCKER_BG.setValue(row, subtask.getBlockerBg().getText());
        IN_PROG_DATE.setValue(row, subtask.getInProgDate());
        IN_REVW_DATE.setValue(row, subtask.getInRevDate());
        REM_EFFORTS.setValue(row, subtask.getRemEfforts());
        TARGET_DATE.setValue(row, subtask.getTargetDate());
        EXPERT_STATE.setValue(row, subtask.getExpertState());
        IS_DISABLED.setValue(row, subtask.isDisabled());
        SPRINT.setValue(row, subtask.getSprint());

        data.addRow(row);

        return (row);
    }

    /**
     * To get all subtasks
     *
     * @param row
     * @return
     */
    final public DmSubtask createDMFlowSubTask(EcvTableRow row) {
        assert (row.getValueAt(ID) != null);
        assert (row.getValueAt(ASSIGNEE) != null);
        assert (row.getValueAt(TITLE) != null);
        assert (row.getValueAt(STATUS) != null);
        assert (row.getValueAt(START_DATE) != null);
        assert (row.getValueAt(END_DATE) != null);
        assert (row.getValueAt(SEVERITY) != null);
        assert (row.getValueAt(COMMENT) != null);
        assert (row.getValueAt(BLOCKER_START_DATE) != null);
        assert (row.getValueAt(NO_OF_DAYS) != null);
        assert (row.getValueAt(TASK_REMOVAL_STATUS) != null);
        assert (row.getValueAt(EFFORTS) != null);
        assert (row.getValueAt(BLOCKER_BG) != null);
        assert (row.getValueAt(IN_PROG_DATE) != null);
        assert (row.getValueAt(IN_REVW_DATE) != null);
        assert (row.getValueAt(REM_EFFORTS) != null);
        assert (row.getValueAt(TARGET_DATE) != null);
        assert (row.getValueAt(EXPERT_STATE) != null);
        assert (row.getValueAt(IS_DISABLED) != null);

        DmSubtask subtask = new DMFlowSubtaskImpl(row.getValueAt(ID).toString(), row.getValueAt(TITLE).toString(), row.getValueAt(ASSIGNEE).toString(), TaskStatus.valueOf(row.getValueAt(STATUS).toString()), row.getValueAt(START_DATE).toString(), row.getValueAt(END_DATE).toString(), FlowBoardTaskSeverity.valueOf(row.getValueAt(SEVERITY).toString()), row.getValueAt(COMMENT).toString(), row.getValueAt(BLOCKER_START_DATE).toString(), row.getValueAt(NO_OF_DAYS).toString(), TaskRemovalStatus.valueOf(row.getValueAt(TASK_REMOVAL_STATUS).toString()), row.getValueAt(EFFORTS).toString(), FlowBlockerBg.valueOf(row.getValueAt(BLOCKER_BG).toString()), row.getValueAt(IN_PROG_DATE).toString(), row.getValueAt(IN_REVW_DATE).toString(), row.getValueAt(REM_EFFORTS).toString(), row.getValueAt(TARGET_DATE).toString(), row.getValueAt(EXPERT_STATE).toString(), row.getValueAt(IS_DISABLED).toString(), row.getValueAt(SPRINT).toString());

        return subtask;
    }

    final public List<DmSubtask> createDMFlowSubTasks(EcvTableData data) {
        assert (data != null);

        Set<DmSubtask> subtasks = new HashSet<>();

        List<DmSubtask> orderedSubtasks = new ArrayList<>();

        for (EcvTableRow row : data.getRows()) {
            assert (row.getValueAt(ID) != null);
            assert (row.getValueAt(ASSIGNEE) != null);
            assert (row.getValueAt(TITLE) != null);
            assert (row.getValueAt(STATUS) != null);

            String title = "";
            String assignee = "";
            String startDate = "";
            String endDate = "";
            FlowBoardTaskSeverity severity = FlowBoardTaskSeverity.NONE;
            String comment = "";
            String blockerStartDate = "";
            String noOfDays = "";
            TaskRemovalStatus taskRemovalStatus = TaskRemovalStatus.ALIVE;
            String efforts = "";
            FlowBlockerBg blockerBg = FlowBlockerBg.WHITE;
            String inProgDate = "";
            String inRevDate = "";
            String remEfforts = "";
            String targetDate = "";
            String expertState = "";
            String isDisabled = "";
            String sprint = "";

            DmRq1WorkItem workItem = null;
            if (row.getValueAt(ID).toString().contains("RQONE")) {
                DmRq1SubjectElement element = (DmRq1SubjectElement) DmRq1ElementCache.getElement(row.getValueAt(ID).toString());
                if (element instanceof DmRq1WorkItem) {
                    workItem = (DmRq1WorkItem) element;
                }
            }

            if (row.getValueAt(TITLE) != null) {
                if (row.getValueAt(ID).toString().contains("RQONE") && workItem != null) {
                    title = workItem.TITLE.getValue();
                } else {
                    title = row.getValueAt(TITLE).toString();
                }
            }
            if (row.getValueAt(ASSIGNEE) != null) {
                if (row.getValueAt(ID).toString().contains("RQONE") && workItem != null) {
                    assignee = workItem.ASSIGNEE.getElement().FULLNAME.getValueAsText();
                } else {
                    assignee = row.getValueAt(ASSIGNEE).toString();
                }
            }
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
            if (row.getValueAt(BLOCKER_START_DATE) != null) {
                blockerStartDate = row.getValueAt(BLOCKER_START_DATE).toString();
            }
            if (row.getValueAt(NO_OF_DAYS) != null) {
                noOfDays = row.getValueAt(NO_OF_DAYS).toString();
            }
            if (row.getValueAt(EFFORTS) != null) {
                if (row.getValueAt(ID).toString().contains("RQONE") && workItem != null) {
                    efforts = workItem.EFFORT_ESTIMATION.getValue();
                } else {
                    efforts = row.getValueAt(EFFORTS).toString();
                }
            }

//            if (row.getValueAt(EFFORTS) != null) {
//                efforts = row.getValueAt(EFFORTS).toString();
//            }
            if (row.getValueAt(REM_EFFORTS) != null) {
                remEfforts = row.getValueAt(REM_EFFORTS).toString();
            }
            if (row.getValueAt(BLOCKER_BG) != null) {
                blockerBg = getBlockerBg(row.getValueAt(BLOCKER_BG).toString());
            }

            if (row.getValueAt(IN_PROG_DATE) != null) {
                inProgDate = row.getValueAt(IN_PROG_DATE).toString();
            }

            if (row.getValueAt(IN_REVW_DATE) != null) {
                inRevDate = row.getValueAt(IN_REVW_DATE).toString();
            }

            if (row.getValueAt(TARGET_DATE) != null) {
                if (row.getValueAt(ID).toString().contains("RQONE") && workItem != null) {
                    targetDate = getFormattedDate(workItem.PLANNED_DATE.getValue());
                } else {
                    targetDate = row.getValueAt(TARGET_DATE).toString();
                }
            }

            if (row.getValueAt(EXPERT_STATE) != null) {
                expertState = row.getValueAt(EXPERT_STATE).toString();
            }

            if (row.getValueAt(IS_DISABLED) != null) {
                isDisabled = row.getValueAt(IS_DISABLED).toString();
            }

            if (row.getValueAt(SPRINT) != null) {
                sprint = row.getValueAt(SPRINT).toString();
            }

            if (row.getValueAt(TASK_REMOVAL_STATUS) != null) {
                taskRemovalStatus = getTaskremovalStatus(row.getValueAt(TASK_REMOVAL_STATUS).toString());
                if (!taskRemovalStatus.equals(TaskRemovalStatus.DELETED)) {
                    DmSubtask subtask = new DMFlowSubtaskImpl(row.getValueAt(ID).toString(), title, assignee, getTaskStatus(row.getValueAt(STATUS).toString()), startDate, endDate, severity, comment, blockerStartDate, noOfDays, taskRemovalStatus, efforts, blockerBg, inProgDate, inRevDate, remEfforts, targetDate, expertState, isDisabled, sprint);
                    if (subtasks.add(subtask)) {
                        orderedSubtasks.add(subtask);
                    }
                }
            } else {
                DmSubtask subtask = new DMFlowSubtaskImpl(row.getValueAt(ID).toString(), title, assignee, getTaskStatus(row.getValueAt(STATUS).toString()), startDate, endDate, severity, comment, blockerStartDate, noOfDays, taskRemovalStatus, efforts, blockerBg, inProgDate, inRevDate, remEfforts, targetDate, expertState, isDisabled, sprint);
                if (subtasks.add(subtask)) {
                    orderedSubtasks.add(subtask);
                }
            }

        }
        return orderedSubtasks;

    }

    final public DmSubtask getDMFlowSubTask(String subtaskID, EcvTableData data) {
        assert (data != null);

        DmSubtask subtask = null;
        for (EcvTableRow row : data.getRows()) {
            assert (row.getValueAt(ID) != null);
            assert (row.getValueAt(ASSIGNEE) != null);
            assert (row.getValueAt(TITLE) != null);
            assert (row.getValueAt(STATUS) != null);

            String title = "";
            String assignee = "";
            String startDate = "";
            String endDate = "";
            FlowBoardTaskSeverity severity = FlowBoardTaskSeverity.NONE;
            String comment = "";
            String blockerStartDate = "";
            String noOfDays = "";
            TaskRemovalStatus taskRemovalStatus = TaskRemovalStatus.ALIVE;
            String efforts = "";
            FlowBlockerBg blockerBg = FlowBlockerBg.WHITE;
            String inProgDate = "";
            String inRevDate = "";
            String remEfforts = "";
            String targetDate = "";
            String expertState = "";
            String isDisabled = "";
            String sprint = "";

            DmRq1WorkItem workItem = null;
            if (row.getValueAt(ID).toString().contains("RQONE")) {
                DmRq1SubjectElement element = (DmRq1SubjectElement) DmRq1ElementCache.getElement(row.getValueAt(ID).toString());
                if (element instanceof DmRq1WorkItem) {
                    workItem = (DmRq1WorkItem) element;
                }
            }

            if (row.getValueAt(ID).toString().contains("RQONE") && workItem != null) {
                title = workItem.TITLE.getValue();
                assignee = workItem.ASSIGNEE.getElement().FULLNAME.getValueAsText();
            } else {
                title = row.getValueAt(TITLE).toString();
                assignee = row.getValueAt(ASSIGNEE).toString();
            }
            if (row.getValueAt(START_DATE) != null) {
                startDate = row.getValueAt(START_DATE).toString();
            }
            if (row.getValueAt(END_DATE) != null) {
                endDate = row.getValueAt(END_DATE).toString();
            }
            if (row.getValueAt(IN_PROG_DATE) != null) {
                inProgDate = row.getValueAt(IN_PROG_DATE).toString();
            }
            if (row.getValueAt(IN_REVW_DATE) != null) {
                inRevDate = row.getValueAt(IN_REVW_DATE).toString();
            }
            if (row.getValueAt(SEVERITY) != null) {
                severity = getSeverity(row.getValueAt(SEVERITY).toString());
            }
            if (row.getValueAt(COMMENT) != null) {
                comment = row.getValueAt(COMMENT).toString();
            }
            if (row.getValueAt(BLOCKER_START_DATE) != null) {
                blockerStartDate = row.getValueAt(BLOCKER_START_DATE).toString();
            }
            if (row.getValueAt(NO_OF_DAYS) != null) {
                noOfDays = row.getValueAt(NO_OF_DAYS).toString();
            }
            if (row.getValueAt(ID).toString().contains("RQONE") && workItem != null) {
                if (workItem.EFFORT_ESTIMATION.getValue() != null) {
                    efforts = workItem.EFFORT_ESTIMATION.getValue();
                }
            } else {
                if (row.getValueAt(EFFORTS) != null) {
                    efforts = row.getValueAt(EFFORTS).toString();
                }
            }

            if (row.getValueAt(REM_EFFORTS) != null) {
                remEfforts = row.getValueAt(REM_EFFORTS).toString();
            }
            if (row.getValueAt(TASK_REMOVAL_STATUS) != null) {
                taskRemovalStatus = getTaskremovalStatus(row.getValueAt(TASK_REMOVAL_STATUS).toString());
            }
            if (row.getValueAt(BLOCKER_BG) != null) {
                blockerBg = getBlockerBg(row.getValueAt(BLOCKER_BG).toString());
            }
            if (row.getValueAt(ID).toString().contains("RQONE") && workItem != null) {
                if (workItem.PLANNED_DATE.getValue() != null) {
                    targetDate = getFormattedDate(workItem.PLANNED_DATE.getValue());
                }
            } else {
                if (row.getValueAt(TARGET_DATE) != null) {
                    targetDate = row.getValueAt(TARGET_DATE).toString();
                }
            }

            if (row.getValueAt(EXPERT_STATE) != null) {
                expertState = row.getValueAt(EXPERT_STATE).toString();
            }

            if (row.getValueAt(IS_DISABLED) != null) {
                isDisabled = row.getValueAt(IS_DISABLED).toString();
            }

            if (row.getValueAt(SPRINT) != null) {
                sprint = row.getValueAt(SPRINT).toString();
            }

            if (row.getValueAt(ID).equals(subtaskID)) {
                subtask = new DMFlowSubtaskImpl(row.getValueAt(ID).toString(), title, assignee, getTaskStatus(row.getValueAt(STATUS).toString()), startDate, endDate, severity, comment, blockerStartDate, noOfDays, taskRemovalStatus, efforts, blockerBg, inProgDate, inRevDate, remEfforts, targetDate, expertState, isDisabled, sprint);
                break;
            }

        }
        return subtask;

    }

    final public EcvTableRow updateDMFlowSubTask(DmSubtask dmSubTask, EcvTableData data) {
        assert (data != null);
        assert (dmSubTask != null);
        EcvTableRow updatedRow = null;

        for (EcvTableRow row : data.getRows()) {

            if (ID.getValue(row).equals(dmSubTask.getID())) {

                STATUS.setValue(row, dmSubTask.getTaskStatus().getText());
                START_DATE.setValue(row, dmSubTask.getStartDate());
                END_DATE.setValue(row, dmSubTask.getEndDate());
                if (dmSubTask.getSeverity() != null) {
                    SEVERITY.setValue(row, dmSubTask.getSeverity().getText());
                }
                COMMENT.setValue(row, dmSubTask.getComment());
                BLOCKER_START_DATE.setValue(row, dmSubTask.getBlockerStartDate());
                NO_OF_DAYS.setValue(row, dmSubTask.getNoOfDays());
                if (dmSubTask.getTaskRemovalStatus() != null) {
                    TASK_REMOVAL_STATUS.setValue(row, dmSubTask.getTaskRemovalStatus().getText());
                }
                if (dmSubTask.getID().contains("RQONE")) {
                    DmRq1SubjectElement element = (DmRq1SubjectElement) DmRq1ElementCache.getElement(dmSubTask.getID());
                    if (element instanceof DmRq1WorkItem) {
                        DmRq1WorkItem workItem = (DmRq1WorkItem) element;
                        TITLE.setValue(row, workItem.TITLE.getValue());
                        EFFORTS.setValue(row, workItem.EFFORT_ESTIMATION.getValue());
                        //row.setValueAt(EFFORTS, dmSubTask.getEfforts());
                        ASSIGNEE.setValue(row, workItem.ASSIGNEE.getElement().FULLNAME.getValueAsText());
                        TARGET_DATE.setValue(row, getFormattedDate(workItem.PLANNED_DATE.getValue()));
                    }
                } else {
                    TITLE.setValue(row, dmSubTask.getTitle());
                    ASSIGNEE.setValue(row, dmSubTask.getAssignee());
                    EFFORTS.setValue(row, dmSubTask.getEfforts());
                    TARGET_DATE.setValue(row, dmSubTask.getTargetDate());
                }

                if (dmSubTask.getBlockerBg() != null) {
                    BLOCKER_BG.setValue(row, dmSubTask.getBlockerBg().getText());
                }
                IN_PROG_DATE.setValue(row, dmSubTask.getInProgDate());
                IN_REVW_DATE.setValue(row, dmSubTask.getInRevDate());
                REM_EFFORTS.setValue(row, dmSubTask.getRemEfforts());
                EXPERT_STATE.setValue(row, dmSubTask.getExpertState());
                IS_DISABLED.setValue(row, dmSubTask.isDisabled());
                SPRINT.setValue(row, dmSubTask.getSprint());
                updatedRow = row;
                break;
            }

        }
        return updatedRow;

    }

    public boolean removeRq1WIEntries(EcvTableData data) {
        boolean isDataUpdated = false;
        assert (data != null);
        final Iterator<EcvTableRow> rowIterator = data.getRows().iterator();
        for (Iterator iterator = rowIterator; iterator.hasNext();) {
            EcvTableRow curRow = (EcvTableRow) iterator.next();
            if (((String) curRow.getValueAt(ID)).startsWith("RQONE")) {
                iterator.remove();
                isDataUpdated = true;
            }
        }
        return isDataUpdated;
    }

    public void removeFlowSubTaskData(EcvTableData data) {
        assert (data != null);
        data.clearRows();
    }

    private TaskStatus getTaskStatus(String strTaskStatus) {
        TaskStatus taskstatus = TaskStatus.TODO;
        switch (strTaskStatus) {
            case "ToDo":
                taskstatus = TaskStatus.TODO;
                break;
            case "In-Progress":
                taskstatus = TaskStatus.INPROGRESS;
                break;
            case "In-Review":
                taskstatus = TaskStatus.INREVIEW;
                break;
            case "Done":
                taskstatus = TaskStatus.DONE;
                break;

        }
        return taskstatus;
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
            case "RQONE":
                severity = FlowBoardTaskSeverity.RQONE;
                break;
        }
        return severity;
    }

    private TaskRemovalStatus getTaskremovalStatus(String removalStatus) {
        TaskRemovalStatus taskRemovalStatus = TaskRemovalStatus.ALIVE;
        switch (removalStatus) {
            case "Alive":
                taskRemovalStatus = TaskRemovalStatus.ALIVE;
                break;
            case "Deleted":
                taskRemovalStatus = TaskRemovalStatus.DELETED;
                break;
            default:
                break;
        }
        return taskRemovalStatus;
    }

    private FlowBlockerBg getBlockerBg(String bg) {
        FlowBlockerBg flowBlockerBg = FlowBlockerBg.WHITE;
        switch (bg) {
            case "Blue":
                flowBlockerBg = FlowBlockerBg.BLUE;
                break;

            default:
                break;
        }
        return flowBlockerBg;
    }

    private String getFormattedDate(EcvDate date) {
        String formattedDate = "";
        try {
            if (date != null && !date.isEmpty()) {
                //for month, its less than 9 as it will start from 0 for Jan
                if (date.getDayOfMonth() < 10 && date.getMonth() < 9) {
                    formattedDate = "0" + date.getDayOfMonthAsString() + '.' + "0" + date.getMonthAsString() + '.' + date.getYearAsString();
                } else if (date.getDayOfMonth() < 10) {
                    //if date is single digit then add 0 before date value for solving xmlParser.
                    formattedDate = "0" + date.getDayOfMonthAsString() + '.' + date.getMonthAsString() + '.' + date.getYearAsString();
                } else if (date.getMonth() < 9) {
                    formattedDate = date.getDayOfMonthAsString() + '.' + "0" + date.getMonthAsString() + '.' + date.getYearAsString();
                } else {
                    formattedDate = date.getDayOfMonthAsString() + '.' + date.getMonthAsString() + '.' + date.getYearAsString();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(Rq1XmlTable_FlowSubTask.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(Rq1XmlTable_FlowSubTask.class.getName(), e);
        }
        return formattedDate;
    }

}
