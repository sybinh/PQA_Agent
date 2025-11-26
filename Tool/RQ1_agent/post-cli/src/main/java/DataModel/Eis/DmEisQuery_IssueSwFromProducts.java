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
import Rq1Cache.Records.Rq1Issue;
import Rq1Cache.Records.Rq1Release;
import Rq1Cache.Rq1NodeDescription;
import java.util.List;

/**
 * RQ1 query to load I-SWs where the Product is one of the products from the 
 * product list and where LifeCycleState is New or Conflicted 
 * @author hfi5wi
 */
public class DmEisQuery_IssueSwFromProducts implements DmEisQueryI<DmRq1IssueSW> {

    final private DmRq1Query<DmRq1IssueSW> query;
    
    public DmEisQuery_IssueSwFromProducts(String[] productList) {
        assert (productList != null);
        assert (productList.length != 0);
        
        query = new DmRq1Query<>(Rq1NodeDescription.ISSUE_SW.getRecordType());
        query.addCriteria_FixedValues(Rq1NodeDescription.ISSUE_SW.getFixedRecordValues());
        query.addCriteria_ValueList(Rq1Issue.ATTRIBUTE_LIFE_CYCLE_STATE, LCS_NEW_CONFLICTED);
        query.addCriteria_ValueList(Rq1Release.ATTRIBUTE_PRODUCT, productList);
    }

    @Override
    public List<DmRq1IssueSW> getResult() {
        return (query.getResult());
    }
}
