/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.ClassificationPstCollection;

/**
 *
 * @author GUG2WI
 */
public class Rq1PverCollection extends Rq1Pver implements Rq1PstCollectionI {

    final public Rq1DatabaseField_Enumeration CLASSIFICATION;

    public Rq1PverCollection() {
        super(Rq1NodeDescription.PVER_COLLECTION);

        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", ClassificationPstCollection.values()));
    }
}
