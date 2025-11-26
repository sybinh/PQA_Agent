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
 * @author mos8cob
 */
public class FlowBlockerImpl implements FlowBlocker {

    private String noOfDays;
    private String comment;
    private FlowBoardTaskSeverity severity;
    private String endDate;
    private String startDate;
    private String assignee;
    private String blockerId;
    private String subTaskId;

    public FlowBlockerImpl(String blockerId, String subTaskId, String assignee, String startDate, String endDate, FlowBoardTaskSeverity severity, String comment,String noOfDays) {

        this.blockerId = blockerId;
        this.subTaskId = subTaskId;
        this.assignee = assignee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.severity = severity;
        this.comment = comment;
        this.noOfDays = noOfDays;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public FlowBoardTaskSeverity getSeverity() {
        return severity;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getAssignee() {
        return assignee;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setSeverity(FlowBoardTaskSeverity severity) {
        this.severity = severity;
    }

    @Override
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public void setSubTaskId(String subTaskId) {
        this.subTaskId = subTaskId;
    }

    @Override
    public String getSubTaskId() {
        return subTaskId;
    }

    @Override
    public void setBlockerId(String blockerId) {
        this.blockerId = blockerId;
    }

    @Override
    public String getBlockerId() {
        return blockerId;
    }
    
    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

}
