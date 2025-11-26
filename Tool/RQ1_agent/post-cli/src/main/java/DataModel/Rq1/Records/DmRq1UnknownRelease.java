/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1UnknownRelease;

/**
 *
 * @author GUG2WI
 */
public class DmRq1UnknownRelease extends DmRq1Release {

    final public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> MAPPED_PARENTS;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1UnknownRelease(Rq1UnknownRelease rq1UnknownRelease) {
        super("Unknown-Release", rq1UnknownRelease);

        IMPLEMENTATION_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        IMPLEMENTATION_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        PLANNING_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        PLANNING_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        //
        // Create and add fields
        //
        addField(new DmRq1Field_Text(this, rq1UnknownRelease.TYPE, "Type"));
        addField(new DmRq1Field_Text(this, rq1UnknownRelease.DOMAIN, "Domain"));
        addField(MAPPED_PARENTS = new DmRq1Field_MappedReferenceList<>(this, rq1UnknownRelease.HAS_MAPPED_PARENTS, "Parents"));

        //
        // Create and add rules
        //
        addRule(new Rule_UnknownRelease_Exists(this));
    }

}
