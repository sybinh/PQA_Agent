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
 * @author bel5cob
 */
public class CriticalResourceImpl implements CriticalResource {

    private String startDate;
    private String endDate;
    private String comment;
    private String crTime;
    private String dPhase1;
    private String dPhase2;
    private String dPhase;
    private String resConflict;

    public CriticalResourceImpl(String startDate, String endDate, String comment, String crTime, String dPhase1, String dPhase2, String dPhase, String resConflict) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.comment = comment;
        this.crTime = crTime;
        this.dPhase1 = dPhase1;
        this.dPhase2 = dPhase2;
        this.dPhase = dPhase;
        this.resConflict = resConflict;

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
    public String getComment() {
        return comment;
    }

    @Override
    public String getCrTime() {
        return crTime;
    }

    @Override
    public String getDPhase1() {
        return dPhase1;
    }

    @Override
    public String getDPhase2() {
        return dPhase2;
    }

    @Override
    public String getDPhase() {
        return dPhase;
    }

    @Override
    public String getResConflict() {
        return resConflict;
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
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setCrTime(String crTime) {
        this.crTime = crTime;
    }

    @Override
    public void setDPhase1(String dPhase1) {
        this.dPhase1 = dPhase1;
    }

    @Override
    public void setDPhase2(String dPhase2) {
        this.dPhase2 = dPhase2;
    }

    @Override
    public void setDPhase(String dPhase) {
        this.dPhase = dPhase;
    }

    @Override
    public void setResConflict(String resConflict) {
        this.resConflict = resConflict;
    }

}
