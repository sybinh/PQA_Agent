/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

/**
 *
 * @author jea2cob
 */
public class DMFlowSubtaskImpl implements DmSubtask {

    private String title;
    private String assignee;
    private String id;
    private TaskStatus taskstatus;
    private String startDate;
    private String endDate;
    private FlowBoardTaskSeverity severity;
    private String comment;
    private String blockerStartDate;
    private String noOfDays;
    private TaskRemovalStatus taskRemovalStatus;
    private String efforts;
    private String remEfforts;
    private FlowBlockerBg blockerBg;
    private String inProgDate;
    private String inRevDate;
    private String targetDate;
    private String expertState;
    private String isDisabled;
    private String sprint;

    public DMFlowSubtaskImpl(String id, String title, String assignee, TaskStatus taskStatus, String startDate, String endDate, FlowBoardTaskSeverity severity, String comment, String blockerStartDate, String noOfDays, TaskRemovalStatus taskRemovalStatus, String efforts, FlowBlockerBg blockerBg, String inProgDate, String inRevDate, String remEfforts, String targetDate, String expertState, String isDisabled, String sprint) {
        this.title = title;
        this.assignee = assignee;
        this.id = id;
        this.taskstatus = taskStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.severity = severity;
        this.comment = comment;
        this.blockerStartDate = blockerStartDate;
        this.noOfDays = noOfDays;
        this.taskRemovalStatus = taskRemovalStatus;
        this.efforts = efforts;
        this.blockerBg = blockerBg;
        this.inProgDate = inProgDate;
        this.inRevDate = inRevDate;
        this.remEfforts = remEfforts;
        this.targetDate = targetDate;
        this.expertState = expertState;
        this.isDisabled = isDisabled;
        this.sprint = sprint;
    }

    public DMFlowSubtaskImpl(String id, String title, String assignee, TaskStatus taskStatus, String efforts, String targetDate) {
        this.title = title;
        this.assignee = assignee;
        this.id = id;
        this.taskstatus = taskStatus;
        this.startDate = "";
        this.endDate = "";
        this.severity = FlowBoardTaskSeverity.NONE;
        this.comment = "";
        this.blockerStartDate = "";
        this.noOfDays = "";
        this.taskRemovalStatus = TaskRemovalStatus.ALIVE;
        this.efforts = "";
        this.blockerBg = FlowBlockerBg.WHITE;
        this.inProgDate = "";
        this.inRevDate = "";
        this.efforts = efforts;
        this.remEfforts = "";
        this.targetDate = targetDate;
        this.expertState = "";
        this.isDisabled = "";
        this.sprint = "";
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return this.title;

    }

    @Override
    public TaskStatus getTaskStatus() {
        return this.taskstatus;
    }

    @Override
    public String getAssignee() {
        return this.assignee;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setAssignee(String newAssignee) {
        this.assignee = newAssignee;
    }

    @Override
    public void setStatus(TaskStatus newtaskstatus) {
        this.taskstatus = newtaskstatus;
    }

    @Override
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public void setSeverity(FlowBoardTaskSeverity severity) {
        this.severity = severity;
    }

    @Override
    public FlowBoardTaskSeverity getSeverity() {
        return severity;

    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setBlockerStartDate(String blockerStartDate) {
        this.blockerStartDate = blockerStartDate;
    }

    @Override
    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    @Override
    public String getBlockerStartDate() {
        return blockerStartDate;
    }

    @Override
    public String getNoOfDays() {
        return noOfDays;
    }

    @Override
    public void setTaskRemovalStatus(TaskRemovalStatus taskRemovalStatus) {
        this.taskRemovalStatus = taskRemovalStatus;
    }

    @Override
    public TaskRemovalStatus getTaskRemovalStatus() {
        return taskRemovalStatus;
    }

    @Override
    public String getTitleToolTip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAssigneeToolTip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTitleToolTip(String titleToolTip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAssigneeToolTip(String assigneeToolTip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getBlockerToolTip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBlockerToolTip(String blockerToolTip, String severity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEfforts() {
        return efforts;
    }

    @Override
    public void setEfforts(String efforts) {
        this.efforts = efforts;
    }

    @Override
    public void setBlockerBg(FlowBlockerBg blockerBg) {
        this.blockerBg = blockerBg;
    }

    @Override
    public FlowBlockerBg getBlockerBg() {
        return blockerBg;
    }

    @Override
    public String getInProgDate() {
        return inProgDate;
    }

    @Override
    public void setInProgDate(String inProgDate) {
        this.inProgDate = inProgDate;
    }

    @Override
    public String getInRevDate() {
        return inRevDate;
    }

    @Override
    public void setInRevDate(String inRevDate) {
        this.inRevDate = inRevDate;
    }

    @Override
    public String getRemEfforts() {
        return remEfforts;
    }

    @Override
    public void setRemEfforts(String remEfforts) {
        this.remEfforts = remEfforts;
    }

    @Override
    public String getTargetDate() {
        return targetDate;
    }

    @Override
    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    @Override
    public String getExpertState() {
        return expertState;
    }

    @Override
    public void setExpertState(String expertState) {
        this.expertState = expertState;
    }

    @Override
    public String isDisabled() {
        return isDisabled;
    }

    @Override
    public void setDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    @Override
    public void setSprint(String sprint) {
        this.sprint = sprint;
    }

    @Override
    public String getSprint() {
        return sprint;
    }
}
