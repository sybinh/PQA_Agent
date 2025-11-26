/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Rq1LinkDescription;
import Rq1Cache.Types.Rq1XmlTable_HardwareMappingToDerivatives;

/**
 *
 * @author gug2wi
 */
public class Rq1HardwareIrm extends Rq1Irm {

    final private Rq1DatabaseField_Xml MAPPING_TO_DERIVATIVES_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_HardwareMappingToDerivatives> MAPPING_TO_DERIVATIVES;

    public Rq1HardwareIrm(Rq1LinkDescription mapDescription) {
        super(mapDescription);

        addField(MAPPING_TO_DERIVATIVES_XML = new Rq1DatabaseField_Xml(this, "MappingToDerivatives"));
        addField(MAPPING_TO_DERIVATIVES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_HardwareMappingToDerivatives(), MAPPING_TO_DERIVATIVES_XML, "MAPINT2EXT"));
        MAPPING_TO_DERIVATIVES.setReadOnly();
    }

    @Override
    protected boolean createInDatabase(Rq1AttributeName[] fieldOrder) {
        Rq1AttributeName[] order = {ATTRIBUTE_HAS_MAPPED_ISSUE, ATTRIBUTE_HAS_MAPPED_RELEASE, ATTRIBUTE_OPERATION_MODE};
        return (super.createInDatabase(order));
    }
}
