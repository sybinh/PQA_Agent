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
public class AnalysisDone {

    private AnalysisDoneState doneState;
    private String doneDate;

    public AnalysisDone(AnalysisDoneState doneState, String doneDate) {
        this.doneState = doneState;
        this.doneDate = doneDate;
    }

    /**
     * @return the state
     */
    public AnalysisDoneState getDoneState() {
        return doneState;
    }

    /**
     * @param doneState
     */
    public void setDoneState(AnalysisDoneState doneState) {
        this.doneState = doneState;
    }

    /**
     * @return the doneDate
     */
    public String getDoneDate() {
        return doneDate;
    }

    /**
     * @param doneDate the doneDate to set
     */
    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

}
