/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Eis;

import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Types.DmRq1Query;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Records.Rq1Issue;
import Rq1Cache.Records.Rq1IssueSW;
import Rq1Cache.Records.Rq1Release;
import Rq1Cache.Rq1NodeDescription;
import java.util.List;

/**
 * RQ1 query to load all I-SWs with LifeCycleState is New or Conflicted assigneed 
 * to spezified user or users from the user list
 * @author hfi5wi
 */
public class DmEisQuery_IssueSwFromUsers implements DmEisQueryI<DmRq1IssueSW> {

    final private DmRq1Query<DmRq1IssueSW> query;
    
    public DmEisQuery_IssueSwFromUsers(String[] userList) {
        assert (userList != null);
        assert (userList.length != 0);
        
        query = new DmRq1Query<>(Rq1NodeDescription.ISSUE_SW.getRecordType());
        query.addCriteria_FixedValues(Rq1NodeDescription.ISSUE_SW.getFixedRecordValues());
        query.addCriteria_ValueList(Rq1Release.ATTRIBUTE_ASSIGNEE_LOGIN_NAME.getName(), userList);
        query.addCriteria_ValueList(Rq1Issue.ATTRIBUTE_LIFE_CYCLE_STATE, LCS_NEW_CONFLICTED);
        OslcLoadHint loadEisIssues = new OslcLoadHint(true);
        loadEisIssues.followField(Rq1IssueSW.FIELDNAME_HAS_CHILDREN, true);
        loadEisIssues.followField(Rq1IssueSW.ATTRIBUTE_HAS_WORKITEMS, true);
        loadEisIssues.followField(Rq1IssueSW.ATTRIBUTE_BELONGS_TO_PROJECT, true);
        query.setLoadHint(loadEisIssues);
    }

    @Override
    public List<DmRq1IssueSW> getResult() {
        return this.query.getResult();
    }
}
