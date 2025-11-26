/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_MappingToDerivatives_String;
import Rq1Cache.Records.Rq1Irm_Bc_IssueSw;
import Ipe.Annotations.IpeFactoryConstructor;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Irm_Bc_IssueSw extends DmRq1Irm {

    final public DmRq1Field_MappingToDerivatives_String MAPPING_TO_DERIVATIVES;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Irm_Bc_IssueSw(Rq1Irm_Bc_IssueSw rq1Irm_Bc_IssueSw) {
        super("IRM-BC-ISSUE_SW", rq1Irm_Bc_IssueSw);

        addField(MAPPING_TO_DERIVATIVES = new DmRq1Field_MappingToDerivatives_String(this, rq1Irm_Bc_IssueSw.MAPPING_TO_DERIVATIVES, "Mapping To Derivatives"));

        //
        // Create and add fields
        //
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Bc_IssueSw.HAS_MAPPED_ISSUE, "Issue SW"));
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Bc_IssueSw.HAS_MAPPED_RELEASE, "BC Release"));
    }
}
