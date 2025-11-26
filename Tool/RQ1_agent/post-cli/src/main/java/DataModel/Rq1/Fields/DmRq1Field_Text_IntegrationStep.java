/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1SoftwareRelease;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Data.Enumerations.IntegrationStep;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author MIW83WI
 */
public class DmRq1Field_Text_IntegrationStep extends DmRq1Field_Text {

    private DmElementI map;

    public DmRq1Field_Text_IntegrationStep(DmElementI map, Rq1FieldI_Text rq1TextField, String nameForUserInterface) {
        super(map, rq1TextField, nameForUserInterface);
        this.map = map;
    }

    public List<DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord> calculateIntegrationSteps() {
        if (map != null) {
            DmRq1SoftwareRelease release = null;
            String setIntegrationStepValue = "";
            if (map instanceof DmRq1Irm) {
                DmRq1Irm irm = (DmRq1Irm) map;
                release = (DmRq1SoftwareRelease) irm.HAS_MAPPED_RELEASE.getElement();
                setIntegrationStepValue = irm.INTEGRATION_STEP.getValue();
            } else if (map instanceof DmRq1Rrm) {
                DmRq1Rrm rrm = (DmRq1Rrm) map;
                DmRq1Release parentRelease = rrm.getParent();
                if (parentRelease instanceof DmRq1SoftwareRelease) {
                    release = (DmRq1SoftwareRelease) parentRelease;
                }
                setIntegrationStepValue = rrm.INTEGRATION_STEP.getValue();
            }
            if (release == null) {
                return null;
            }
            List<DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord> stepList = DmRq1Field_IntegrationStepsTableRelease.extract(release.INTEGRATION_STEP_TABLE.getValue());
            if (stepList != null) {
                if (stepList.isEmpty()) {
                    stepList.clear();
                    for (IntegrationStep element : release.INTEGRATION_STEPS_MILESTONES.keySet()) {
                        stepList.add(new DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord(release.INTEGRATION_STEPS_MILESTONES.get(element).getDate(), element.getText(), ""));
                    }
                }
                for (DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord step : stepList) {
                    String stepName = step.getName();
                    if (stepName == null) {
                        stepName = "";
                    }
                    if (stepName.equals(setIntegrationStepValue)) {
                        return stepList;
                    }
                }
                stepList.add(new DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord(EcvDate.getEmpty(), setIntegrationStepValue, ""));
            }
            return stepList;
        }
        return null;
    }

}
