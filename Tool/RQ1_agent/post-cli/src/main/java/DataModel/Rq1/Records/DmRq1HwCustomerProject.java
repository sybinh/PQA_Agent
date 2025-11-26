/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1HwCustomerProject;

/**
 *
 * @author gug2wi
 */
public class DmRq1HwCustomerProject extends DmRq1HardwareProject {

    final public DmRq1Field_Text EXTERNAL_ID;
    final public DmRq1Field_Text EXTERNAL_TITLE;

    public DmRq1HwCustomerProject(String subjectType, Rq1HwCustomerProject rq1Project) {
        super(subjectType, rq1Project);

        addField(EXTERNAL_ID = new DmRq1Field_Text(this, rq1Project.EXTERNAL_ID, "External ID"));
        EXTERNAL_ID.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(EXTERNAL_TITLE = new DmRq1Field_Text(this, rq1Project.EXTERNAL_TITLE, "External Title"));
        EXTERNAL_TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
    }

}
