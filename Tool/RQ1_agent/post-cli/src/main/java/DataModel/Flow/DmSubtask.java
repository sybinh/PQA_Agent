/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

/**
 * Interface for a subtask in the flow framework.
 *
 * @author jea2cob
 */
public interface DmSubtask {

    String getTitle();

    TaskStatus getTaskStatus();

    String getAssignee();

    String getID();

    String getStartDate();

    String getEndDate();

    String getComment();

    String getBlockerStartDate();

    String getNoOfDays();

    String getTitleToolTip();

    String getAssigneeToolTip();

    String getBlockerToolTip();

    FlowBoardTaskSeverity getSeverity();

    TaskRemovalStatus getTaskRemovalStatus();

    String getEfforts();

    String getRemEfforts();

    String getTargetDate();

    FlowBlockerBg getBlockerBg();

    String getInProgDate();

    String getInRevDate();
    
    String getExpertState();
    
    String isDisabled();
    
    String getSprint();

    void setEfforts(String efforts);

    void setRemEfforts(String remEfforts);

    void setTargetDate(String targetDate);

    void setId(String id);

    void setTitle(String title);

    void setAssignee(String newAssignee);

    void setStatus(TaskStatus newStatus);

    void setStartDate(String startDate);

    void setEndDate(String endDate);

    void setSeverity(FlowBoardTaskSeverity severity);

    void setComment(String comment);

    void setBlockerStartDate(String comment);

    void setNoOfDays(String comment);

    void setTitleToolTip(String titleToolTip);

    void setAssigneeToolTip(String assigneeToolTip);

    void setBlockerToolTip(String blockerToolTip, String severity);

    void setTaskRemovalStatus(TaskRemovalStatus removalStatus);

    void setBlockerBg(FlowBlockerBg blockerBg);

    void setInProgDate(String inProgDate);

    void setInRevDate(String inRevDate);
    
    void setExpertState(String isExpert);
    
    void setDisabled(String isDisabled);

    void setSprint(String sprint);
}
