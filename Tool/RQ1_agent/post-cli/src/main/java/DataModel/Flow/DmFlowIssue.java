/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

import util.EcvDate;

/**
 * Interface for a issue in the flow framework.
 *
 * @author gug2wi
 */
public interface DmFlowIssue {

    String getTitle();

    String getRank();

    String getFlowVersion();

    String getRequestedDate();

    String getRemainingEffort();

    FullKitStatus getStatus();

    TaskStatus getTaskStatus();

    String getClusterName();

    EcvDate getToRedDate();

    String getClusterID();

    FullKitSize getSize();

    String getGroupId();

    String getNoDevelopers();

    void reload();

    InternalRank getInternalRank() throws InternalRank.BuildException;

    InternalRank rankFirst(InternalRank currentFirst) throws InternalRank.BuildException;

    InternalRank rankBetween(InternalRank before, InternalRank after) throws InternalRank.BuildException;

    InternalRank rankLast(InternalRank currentLast) throws InternalRank.BuildException;

    EcvDate getTargetDate();

//    DmFlowIssue splitIssue(); // Not yet implemented in the sub classes.
}
