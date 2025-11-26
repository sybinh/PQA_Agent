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
import OslcAccess.OslcLoadHint;
import Rq1Cache.Records.Rq1Issue;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1Release;
import Rq1Cache.Rq1NodeDescription;
import java.util.List;

/**
 * RQ1 query to load all I-FDs that have assigned this Projet as "Belongs to Project"
 * with the conditions that the assignee is from the list IssueSwAssignees 
 * and LifeCycleState is New or Conflicted
 * @author hfi5wi
 */
public class DmEisQuery_IssueFdFromPoolProject implements DmEisQueryI<DmRq1IssueFD> {

    final private DmRq1Query<DmRq1IssueFD> query;

    public DmEisQuery_IssueFdFromPoolProject(String[] userList, String project) {
        assert (userList != null);
        assert (userList.length != 0);
        assert (project != null);
        
        query = new DmRq1Query<>(Rq1NodeDescription.ISSUE_FD.getRecordType());
        query.addCriteria_ValueList(Rq1Issue.ATTRIBUTE_LIFE_CYCLE_STATE, LCS_NEW_CONFLICTED);
        query.addCriteria_ValueList(Rq1Release.ATTRIBUTE_ASSIGNEE_LOGIN_NAME, userList);
        query.addCriteria_FixedValues(Rq1NodeDescription.ISSUE_FD.getFixedRecordValues());
        query.addCriteria_Value(Rq1Release.ATTRIBUTE_BELONGS_TO_PROJECT, project);
        OslcLoadHint oslcLoadHint = new OslcLoadHint(true);
        oslcLoadHint.followField(Rq1IssueFD.ATTRIBUTE_HAS_WORKITEMS, true);
        oslcLoadHint.followField(Rq1IssueFD.ATTRIBUTE_BELONGS_TO_PROJECT, true);
        query.setLoadHint(oslcLoadHint);
    }

    @Override
    public List<DmRq1IssueFD> getResult() {
        return (query.getResult());
    }
}
