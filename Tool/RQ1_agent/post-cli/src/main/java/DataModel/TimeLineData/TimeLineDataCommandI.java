/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

/**
 *
 * @author RJB5COB
 */
public interface TimeLineDataCommandI {

    public String getRQ1CommandName();

    public String getIpeTabCommandName();

    public void executeOpenRQ1Command();

    public void executeOpenIpeTabCommand();

    public void exceuteSuccessorCommand();

    public void executePredecessorCommand();

    public String getSuccessorCommandName();

    public String getPredecessorCommandName();

    public String isEditWorkItemCommand();

    public void executeOpenWiEditDialogCommand();

    public String isStartWorItemCommand();

    public void executeStartWiCommand();

    public String isCloseWorkItemCommand();

    public void executeCloseWiCommand();

}
