/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CRP;

import DataModel.Flow.InternalRank;

/**
 *
 * @author HRN4KOR
 */
public interface DmCrpIssue {

    String getCrpRank();

    String getCrpVersion();

    String getCrpRemainingEffort();

    String getCrpClusterName();

    String getCrpClusterID();

    void reload();

    InternalRank getCrpInternalRank() throws InternalRank.BuildException;

    InternalRank crpRankFirst(InternalRank currentFirst) throws InternalRank.BuildException;

    InternalRank crpRankBetween(InternalRank before, InternalRank after) throws InternalRank.BuildException;

    InternalRank crpRankLast(InternalRank currentLast) throws InternalRank.BuildException;

}
