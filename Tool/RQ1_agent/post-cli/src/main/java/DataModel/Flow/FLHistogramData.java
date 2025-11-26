/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

import DataModel.Rq1.Records.DmRq1SubjectElement;

/**
 *
 * @author mos8cob
 */
public class FLHistogramData {

    private String size = "";
    private DmRq1SubjectElement task = null;
    private String noOfDays = "";
    private String totalEffort = "";

    public FLHistogramData(String size, DmRq1SubjectElement task, String noOfDays, String totalEffort) {
        this.size = size;
        this.task = task;
        this.noOfDays = noOfDays;
        this.totalEffort = totalEffort;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public DmRq1SubjectElement getTask() {
        return task;
    }

    public void setTask(DmRq1SubjectElement task) {
        this.task = task;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getTotalEffort() {
        return totalEffort;
    }

    public void setTotalEffort(String totalEffort) {
        this.totalEffort = totalEffort;
    }

}
