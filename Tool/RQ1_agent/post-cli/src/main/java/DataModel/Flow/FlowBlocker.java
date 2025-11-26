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
public interface FlowBlocker {

    String getBlockerId();

    String getSubTaskId();

    String getAssignee();

    String getStartDate();

    String getEndDate();

    String getComment();
    
    String getNoOfDays();

    FlowBoardTaskSeverity getSeverity();

    void setBlockerId(String blockerId);

    void setSubTaskId(String id);

    void setAssignee(String newAssignee);

    void setStartDate(String startDate);

    void setEndDate(String endDate);

    void setSeverity(FlowBoardTaskSeverity severity);

    void setComment(String comment);
    
    void setNoOfDays(String noOfDays);

}
