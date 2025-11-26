/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_IntegrationStepsRelease;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Records.Rq1SoftwareRelease;
import Rq1Cache.Types.Rq1XmlTable_IntegrationSteps;
import Rq1Data.Enumerations.IntegrationStep;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gug2wi
 */
public class DmRq1SoftwareRelease extends DmRq1Release {

    final public Map<IntegrationStep, DmRq1Field_Date> INTEGRATION_STEPS_MILESTONES;
    final private DmRq1Field_Table<Rq1XmlTable_IntegrationSteps> INTEGRATION_STEP_TAGS;
    final public DmRq1Field_IntegrationStepsRelease INTEGRATION_STEP_COMPLETE;

    final public DmRq1Field_Text TAGLIST;

    public DmRq1SoftwareRelease(String subjectType, Rq1SoftwareRelease release) {
        super(subjectType, release);

        IMPLEMENTATION_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        IMPLEMENTATION_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        PLANNING_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        PLANNING_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        INTEGRATION_STEPS_MILESTONES = new TreeMap<>();
        for (Map.Entry<IntegrationStep, Rq1XmlSubField_Date> entry : release.INTEGRATION_STEPS_MILESTONES.entrySet()) {
            DmRq1Field_Date newField = new DmRq1Field_Date(this, entry.getValue(), entry.getKey().getLongName());
            INTEGRATION_STEPS_MILESTONES.put(entry.getKey(), newField);
            addField(newField);
        }

        addField(INTEGRATION_STEP_TAGS = new DmRq1Field_Table<>(this, release.INTEGRATION_STEP_TAGS, "Integration Steps Tags"));

        addField(INTEGRATION_STEP_COMPLETE = new DmRq1Field_IntegrationStepsRelease(this, INTEGRATION_STEPS_MILESTONES, INTEGRATION_STEP_TAGS, "Integration Steps"));

        addField(TAGLIST = new DmRq1Field_Text(release.TAGLIST, "Taglist"));
    }

    @Override
    public Class<? extends DmRq1Project> getTargetProjectClass() {
        return DmRq1SoftwareProject.class;
    }
}
