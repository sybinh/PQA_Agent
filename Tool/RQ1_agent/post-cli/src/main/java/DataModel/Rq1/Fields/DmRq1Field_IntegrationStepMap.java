/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1SoftwareRelease;
import DataModel.Rq1.Types.DmRq1Table_IntegrationStepsRelease;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Data.Enumerations.IntegrationStep;
import java.util.Set;
import java.util.TreeSet;
import util.EcvComparator;

/**
 *
 * @author MIW83WI
 */
public class DmRq1Field_IntegrationStepMap extends DmRq1Field_Text {

    public static class Step implements Comparable<Step> {

        final private static int MAX_COMMENT_LENGTH = 15;

        final private String valueInRq1;
        final private String valueToDisplay;
        final private String valueForTooltip;

        public Step() {
            this.valueInRq1 = "";
            this.valueToDisplay = "";
            this.valueForTooltip = "";
        }

        public Step(String name) {
            assert (name != null);
            assert (name.isEmpty() == false);

            this.valueInRq1 = name;
            this.valueToDisplay = name;
            this.valueForTooltip = name;
        }

        public Step(String name, String comment) {
            assert (name != null);
            assert (name.isEmpty() == false);

            this.valueInRq1 = name;

            if ((comment != null) && (comment.isEmpty() == false)) {
                if (comment.length() > MAX_COMMENT_LENGTH) {
                    this.valueToDisplay = name + " (" + comment.substring(0, MAX_COMMENT_LENGTH) + " ...)";
                } else {
                    this.valueToDisplay = name + " (" + comment + ")";
                }
                this.valueForTooltip = name + " (" + comment + ")";

            } else {
                this.valueToDisplay = name;
                this.valueForTooltip = name;
            }
        }

        public String getValueInRq1() {
            return valueInRq1;
        }

        public String getValueToDisplay() {
            return valueToDisplay;
        }

        public String getValueForTooltip() {
            return valueForTooltip;
        }

        @Override
        public String toString() {
            return ("(" + valueInRq1 + "/" + valueToDisplay + "/" + valueForTooltip + ")");
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Step) {
                return (valueInRq1.equals(((Step) o).valueInRq1));
            }
            return (false);
        }

        @Override
        public int compareTo(Step t) {
            return (EcvComparator.compareObjects(valueInRq1, t.valueInRq1));
        }

    }

    final private DmRq1Rrm rrm;
    private DmRq1Field_Reference<? extends DmRq1Release> dmReleaseField;

    public DmRq1Field_IntegrationStepMap(DmRq1Irm irm, Rq1FieldI_Text rq1IntegrationStepField, String nameForUserInterface) {
        super(rq1IntegrationStepField, nameForUserInterface);
        assert (irm != null);
        assert (irm.HAS_MAPPED_RELEASE != null);

        this.rrm = null;
        this.dmReleaseField = irm.HAS_MAPPED_RELEASE;
    }

    public DmRq1Field_IntegrationStepMap(DmRq1Rrm rrm, Rq1FieldI_Text rq1TextField, String nameForUserInterface) {
        super(rq1TextField, nameForUserInterface);
        assert (rrm != null);

        this.rrm = rrm;
        this.dmReleaseField = null;
    }

    public Step getValueAsStep() {
        String currentRq1Value = getValue();
        for (Step step : getStepsFromRelease()) {
            if (step.getValueInRq1().equals(currentRq1Value)) {
                return (step);
            }
        }
        if ((currentRq1Value == null) || (currentRq1Value.isEmpty())) {
            return (new Step());
        } else {
            return (new Step(currentRq1Value));
        }
    }

    public void setValueAsStep(Step newValue) {
        Step oldValue = getValueAsStep();
        if (oldValue.equals(newValue) == false) {
            setValue(newValue.valueInRq1);
        }
    }

    private Set<Step> getStepsFromRelease() {

        Set<Step> result = new TreeSet<>();

        //
        // Determine release from which the available integrations steps will be loaded.
        //
        if (dmReleaseField == null) {
            dmReleaseField = rrm.getParentField();
        }
        if (dmReleaseField.getElement() instanceof DmRq1SoftwareRelease == false) {
            return (result);
        }
        DmRq1SoftwareRelease release = (DmRq1SoftwareRelease) dmReleaseField.getElement();

        //
        // Load steps from release
        //
        for (DmRq1Table_IntegrationStepsRelease.Record record : release.INTEGRATION_STEP_COMPLETE.getValueAsRecords()) {
            String name = record.getGenericLongName();
            String comment = record.getComment();
            if ((name != null) && (name.isEmpty() == false)) {
                if ((comment != null) && (comment.isEmpty() == false)) {
                    result.add(new Step(name, comment));
                } else {
                    result.add(new Step(name));
                }
            }
        }

        //
        // Use default values if no steps are configured for the release
        //
        if (result.isEmpty() == true) {
            for (IntegrationStep v : IntegrationStep.values()) {
                if ((v.getLongName() != null) && (v.getLongName().isEmpty() == false)) {
                    result.add(new Step(v.getLongName()));
                }
            }
        }

        return (result);
    }

    /**
     * Returns all steps that are valid according to the current value in the
     * field and the steps configured in the parent release.
     *
     * The empty value is only added, if it is the current value.
     *
     * @return
     */
    public Set<Step> getValidSteps() {

        Set<Step> result = getStepsFromRelease();

        //
        // Add current value
        //
        result.add(getValueAsStep());

        return (result);
    }

}
