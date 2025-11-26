/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1LinkDescription;

/**
 *
 * @author gug2wi
 */
public class Rq1Rrm_Bc_Fc extends Rq1Rrm {

    final public Rq1DatabaseField_Text CHANGES_TO_ISSUES;

    public Rq1Rrm_Bc_Fc() {
        super(Rq1LinkDescription.RRM_BC_REL_FC_REL);

        addField(CHANGES_TO_ISSUES = new Rq1DatabaseField_Text(this, "ChangesToIssues"));
    }
}
