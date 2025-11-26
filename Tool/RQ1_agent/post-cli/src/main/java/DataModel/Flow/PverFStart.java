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
public class PverFStart {

    private PverFStarted pverFState;
    private String pverFDate;

    public PverFStart(PverFStarted pverFState, String pverFDate) {
        this.pverFState = pverFState;
        this.pverFDate = pverFDate;
    }

    /**
     * @return the pverFState
     */
    public PverFStarted getPverFState() {
        return pverFState;
    }

    /**
     * @param pverFState the pverFState to set
     */
    public void setPverFState(PverFStarted pverFState) {
        this.pverFState = pverFState;
    }

    /**
     * @return the pverFDate
     */
    public String getPverFDate() {
        return pverFDate;
    }

    /**
     * @param pverFDate the pverFDate to set
     */
    public void setPverFDate(String pverFDate) {
        this.pverFDate = pverFDate;
    }

}
