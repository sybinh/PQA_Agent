/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToHardware;

/**
 *
 * @author gug2wi
 */
public class Rq1Pvar extends Rq1Pst {

    final public static String TYPE = "PVAR/PFAM";

    final public Rq1XmlSubField_Table<Rq1XmlTable_ProPlaToHardware> PROPLATO_HARDWARE;

    public Rq1Pvar(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        addField(PROPLATO_HARDWARE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_ProPlaToHardware(), PROPLATO, "Hardware"));
    }

}
