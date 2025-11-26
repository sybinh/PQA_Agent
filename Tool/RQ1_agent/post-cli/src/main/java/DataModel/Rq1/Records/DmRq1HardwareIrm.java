/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Table;
import Rq1Cache.Records.Rq1HardwareIrm;
import Rq1Cache.Types.Rq1XmlTable_HardwareMappingToDerivatives;

/**
 *
 * @author GUG2WI
 */
public class DmRq1HardwareIrm extends DmRq1Irm {

    final public DmRq1Field_Table<Rq1XmlTable_HardwareMappingToDerivatives> MAPPING_TO_DERIVATIVES;

    public DmRq1HardwareIrm(String elementType, Rq1HardwareIrm irm) {
        super(elementType, irm);

        addField(MAPPING_TO_DERIVATIVES = new DmRq1Field_Table<>(this, irm.MAPPING_TO_DERIVATIVES, "Mapping to Derivatives"));
    }

}
