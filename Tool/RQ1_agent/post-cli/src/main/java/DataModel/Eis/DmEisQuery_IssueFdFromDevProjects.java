/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Eis;

import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Types.DmRq1Query;
import Rq1Cache.Records.Rq1Issue;
import Rq1Cache.Records.Rq1Release;
import Rq1Cache.Rq1NodeDescription;
import java.util.List;

/**
 * RQ1 query to load all I-FDs that have assigned one of this Projects as "Belongs to 
 * Project" with the condition that LifeCycleState is New or Conflicted
 * @author hfi5wi
 */
public class DmEisQuery_IssueFdFromDevProjects implements DmEisQueryI<DmRq1IssueFD> {

    final private DmRq1Query<DmRq1IssueFD> query;

    public DmEisQuery_IssueFdFromDevProjects(String[] projectList) {
        assert (projectList != null);
        assert (projectList.length != 0);
        
        query = new DmRq1Query<>(Rq1NodeDescription.ISSUE_FD.getRecordType());
        query.addCriteria_FixedValues(Rq1NodeDescription.ISSUE_FD.getFixedRecordValues());
        query.addCriteria_ValueList(Rq1Release.ATTRIBUTE_BELONGS_TO_PROJECT, projectList);
        query.addCriteria_ValueList(Rq1Issue.ATTRIBUTE_LIFE_CYCLE_STATE, LCS_NEW_CONFLICTED);
    }

    @Override
    public List<DmRq1IssueFD> getResult() {
        return (query.getResult());
    }
}
