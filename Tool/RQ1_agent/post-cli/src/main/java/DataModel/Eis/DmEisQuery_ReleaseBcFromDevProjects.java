/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Eis;

import DataModel.Rq1.Records.DmRq1BcRelease;
import DataModel.Rq1.Types.DmRq1Query;
import Rq1Cache.Records.Rq1Release;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.ClassificationBcRelease;
import java.util.EnumSet;
import java.util.List;

/**
 * RQ1 query to load all BCs that are assigned to the projects in the project list
 * and where the planned date is later or equal today.
 * @author hfi5wi
 */
public class DmEisQuery_ReleaseBcFromDevProjects implements DmEisQueryI<DmRq1BcRelease> {

    final static private EnumSet<ClassificationBcRelease> TYPE_BC_BX = EnumSet.of(ClassificationBcRelease.BC, ClassificationBcRelease.BX);
    final private DmRq1Query<DmRq1BcRelease> query;

    public DmEisQuery_ReleaseBcFromDevProjects(String[] projectList) {
        assert (projectList != null);
        assert (projectList.length != 0);
        
        query = new DmRq1Query<>(Rq1NodeDescription.BC_RELEASE.getRecordType());
        query.addCriteria_FixedValues(Rq1NodeDescription.BC_RELEASE.getFixedRecordValues());
        query.addCriteria_ValueList(Rq1Release.ATTRIBUTE_BELONGS_TO_PROJECT, projectList);
        query.addCriteria_ValueList(Rq1Release.ATTRIBUTE_TYPE, TYPE_BC_BX);
    }

    @Override
    public List<DmRq1BcRelease> getResult() {
        return (query.getResult());
    }
}
