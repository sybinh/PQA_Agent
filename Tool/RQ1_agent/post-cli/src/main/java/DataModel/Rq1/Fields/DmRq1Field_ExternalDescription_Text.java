/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;

public class DmRq1Field_ExternalDescription_Text extends DmRq1Field_Text {

    final DmRq1Project project;

    public DmRq1Field_ExternalDescription_Text(DmRq1Project parent, Rq1FieldI_Text rq1TextField, String nameForUserInterface) {
        super(rq1TextField, nameForUserInterface);

        this.project = parent;
    }

    @Override
    public boolean isReadOnly() {
        if (DmRq1Project.Customer.getCustomer(project) != DmRq1Project.Customer.VW_GROUP) {
            return (false);
        } else {
            return (true);
        }
    }

    @Override
    public String getValue() {
        if (isReadOnly() == false) {
            return (super.getValue());
        } else {
            return ("");
        }
    }

    @Override
    public String getValueAsText() {
        if (isReadOnly() == false) {
            return (super.getValueAsText());
        } else {
            return ("");
        }
    }

}
