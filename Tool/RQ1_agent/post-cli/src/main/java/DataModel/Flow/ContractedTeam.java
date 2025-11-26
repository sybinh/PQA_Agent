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
public class ContractedTeam {

    private IncludeBcState includeBcState;
    private String contractedTasks;

    public ContractedTeam(IncludeBcState includeBcState) {
        this.includeBcState = includeBcState;
        this.contractedTasks = "";
    }

    public ContractedTeam(IncludeBcState includeBcState, String contractedTasks) {
        this.includeBcState = includeBcState;
        this.contractedTasks = contractedTasks;
    }

    /**
     * @return the includeBcState
     */
    public IncludeBcState getIncludeBcState() {
        return includeBcState;
    }

    /**
     * @param includeBcState the includeBcState to set
     */
    public void setIncludeBcState(IncludeBcState includeBcState) {
        this.includeBcState = includeBcState;
    }

    public String getContractedTasks() {
        return contractedTasks;
    }

    public void setContractedTasks(String contractedTasks) {
        this.contractedTasks = contractedTasks;
    }

}
