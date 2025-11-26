/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Rq1LinkDescription;
import Rq1Cache.Fields.Rq1XmlSubField_Text;

/**
 *
 * @author gug2wi
 */
public class Rq1Irm_Fc_IssueFd extends Rq1SoftwareIrm {

    final public Rq1XmlSubField_Text QUALIFICATION_STATUS_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text FCF_COMMENT;

    public Rq1Irm_Fc_IssueFd() {
        super(Rq1LinkDescription.IRM_FC_REL_ISSUE_FD);

        addField(FCF_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "FCF_Comment"));
        addField(QUALIFICATION_STATUS_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "QualificationStatus_ChangeComment"));
    }
}
