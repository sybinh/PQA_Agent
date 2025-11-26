/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Taglist;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1XmlTable_IntegrationSteps;
import Rq1Data.Enumerations.IntegrationStep;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gug2wi
 */
public class Rq1SoftwareRelease extends Rq1Release {

    final public Rq1XmlSubField_Table<Rq1XmlTable_IntegrationSteps> INTEGRATION_STEP_TAGS;
    //
    // Special construction for milestones of integration steps.
    // The tags have different names. So we cannot use a table.
    // Use a map of fields instead.
    final public Map<IntegrationStep, Rq1XmlSubField_Date> INTEGRATION_STEPS_MILESTONES;

    final public Rq1XmlSubField_Taglist TAGLIST;

    public Rq1SoftwareRelease(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        Rq1XmlSubField_Xml integrationStepXml;
        addField(integrationStepXml = new Rq1XmlSubField_Xml(this, TAGS, "IntegrationSteps"));
        integrationStepXml.setOptional();
        addField(INTEGRATION_STEP_TAGS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_IntegrationSteps(), integrationStepXml, "IntegrationStep"));
        INTEGRATION_STEP_TAGS.setOptional();

        INTEGRATION_STEPS_MILESTONES = new TreeMap<>();
        for (IntegrationStep step : IntegrationStep.values()) {
            if (step.getMilestoneName() != null) {
                Rq1XmlSubField_Date newField = new Rq1XmlSubField_Date(this, MILESTONES, step.getMilestoneName());
                INTEGRATION_STEPS_MILESTONES.put(step, newField);
                addField(newField);
            }
        }

        addField(TAGLIST = new Rq1XmlSubField_Taglist(this, TAGS, "Taglist"));
    }
}
