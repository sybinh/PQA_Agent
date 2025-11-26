/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1MappedReferenceListField_FilterByClass;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_CATEGORY;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.CategoryEcuRelease;
import Rq1Data.Enumerations.HardwareEcuReleaseClassification;

/**
 *
 * @author gug2wi
 */
public class Rq1BundleRelease extends Rq1HardwareRelease {

    final static public Rq1AttributeName ATTRIBUTE_HAS_MAPPED_CHILDREN = new Rq1AttributeName("hasMappedChildren");

    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Enumeration CLASSIFICATION;

    final public Rq1MappedReferenceListField_FilterByClass HAS_MAPPED_PST;
    final public Rq1MappedReferenceListField_FilterByClass HAS_MAPPED_ECU;

    public Rq1BundleRelease() {
        super(Rq1NodeDescription.HW_BUNDLE_RELEASE);

        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CATEGORY, CategoryEcuRelease.values()));

        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", HardwareEcuReleaseClassification.values()));
        CLASSIFICATION.acceptInvalidValuesInDatabase();

        addField(HAS_MAPPED_PST = new Rq1MappedReferenceListField_FilterByClass(this, HAS_MAPPED_CHILDREN, Rq1Pst.class));
        addField(HAS_MAPPED_ECU = new Rq1MappedReferenceListField_FilterByClass(this, HAS_MAPPED_CHILDREN, Rq1EcuRelease.class));

    }

}
