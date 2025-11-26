/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.InMaBuildState;

/**
 *
 * @author gug2wi
 */
public class Rq1Pver extends Rq1Pst {

    final public static String TYPE = "PVER";

    final public Rq1XmlSubField_Text INTEGRATOR;
    final public Rq1XmlSubField_Text PROPLATO_HARDWARE;

    final public Rq1XmlSubField_Enumeration IN_MA_BUILD_STATUS;

    public Rq1Pver(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        addField(INTEGRATOR = new Rq1XmlSubField_Text(this, TAGS, "Integrator"));
        addField(PROPLATO_HARDWARE = new Rq1XmlSubField_Text(this, PROPLATO, "Hardware"));

        addField(IN_MA_BUILD_STATUS = new Rq1XmlSubField_Enumeration(this, TAGS, "InMaBuildState", InMaBuildState.values(), InMaBuildState.EMPTY));
    }

}
