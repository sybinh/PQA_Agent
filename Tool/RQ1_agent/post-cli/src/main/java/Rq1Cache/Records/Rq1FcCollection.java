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
import Rq1Data.Enumerations.ClassificationFcCollection;

/**
 *
 * @author GUG2WI
 */
public class Rq1FcCollection extends Rq1Fc {

    final public Rq1DatabaseField_Enumeration CLASSIFICATION;

    public Rq1FcCollection() {
        super(Rq1NodeDescription.FC_COLLECTION);

        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", ClassificationFcCollection.values()));
    }

}
