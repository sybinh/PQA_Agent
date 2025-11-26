/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_DerivativeMapping;
import Rq1Cache.Rq1LinkDescription;

/**
 *
 * @author gug2wi
 */
public class Rq1SoftwareIrm extends Rq1Irm {

    final public Rq1DatabaseField_DerivativeMapping MAPPING_TO_DERIVATIVES;

    public Rq1SoftwareIrm(Rq1LinkDescription mapDescription) {
        super(mapDescription);

        addField(MAPPING_TO_DERIVATIVES = new Rq1DatabaseField_DerivativeMapping(this, "MappingToDerivatives"));
    }

    @Override
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        Rq1AttributeName[] order = {ATTRIBUTE_HAS_MAPPED_ISSUE, ATTRIBUTE_HAS_MAPPED_RELEASE, ATTRIBUTE_OPERATION_MODE};
        return (super.createInDatabase(order));
    }
}
