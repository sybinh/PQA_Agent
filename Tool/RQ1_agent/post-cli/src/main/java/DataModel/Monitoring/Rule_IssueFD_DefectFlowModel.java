/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1IssueFD;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.SoftwareIssueCategory;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.EnumSet;

/**
 *
 * @author frt83wi
 */
public class Rule_IssueFD_DefectFlowModel extends DmRule<DmRq1IssueFD> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmExcitedCpCRule(
            "I_FD_05__DefectFlowModel",
            EnumSet.of(RuleExecutionGroup.EXCITED_CPC),
            "I-FD Check DefectFlowModel",
            "Creates a warning if:\n"
            + "...");

    public Rule_IssueFD_DefectFlowModel(DmRq1IssueFD issueFD) {
        super(description, issueFD);
    }

    @Override
    public void executeRule() {

        // rule is NOT for canceled or conflicted IFDs
        if (dmElement.isCanceledOrConflicted()) {
            return;
        }

        // rule affects only IFDs with category "Defect"
        if (dmElement.CATEGORY.getValue() != SoftwareIssueCategory.DEFECT) {
            return;
        }

        // check if ANY of the defect detection attributes is empty
        if (dmElement.DEFECT_DETECTION_LOCATION.getValue().isEmpty()
                || dmElement.DEFECT_DETECTION_PROCESS.getValue().isEmpty()
                || dmElement.DEFECT_DETECTION_ORGANISATION.getValue().isEmpty()
                || dmElement.DEFECT_DETECTION_DATE.getValue().isEmpty()) {

            Warning warning = new Warning(this, "Defect detection attributes are not completely filled out.",
                    "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + "\n"
                    + "At least one of the following fields is empty:\n"
                    + "Defect Detection Location, Defect Detection Process, Defect Detection Organisation, Defect Detection Date");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }

        // check if occurrence or severity is empty
        if (dmElement.OCCURRENCE.getValue() == null
                || dmElement.SEVERITY.getValue() == null) {

            Warning warning = new Warning(this, "Occurrence or Severity are not completely filled out.",
                    "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + "\n"
                    + "At least one of the following fields is empty:\n"
                    + "Occurrence, Severity");
            warning.addAffectedElement(dmElement);
            addMarker(dmElement, warning);
        }

        // check of defect correction attributes is omitted for new IFDs
        if (dmElement.LIFE_CYCLE_STATE.getValue() != LifeCycleState_Issue.NEW) {

            // check if ANY of the defect correction attributes is empty
            if (dmElement.DEFECT_WORKPRODUCT_TYPE.getValue().isEmpty()
                    || dmElement.DEFECT_CLASSIFICATION.getValue().isEmpty()
                    || dmElement.DEFECT_INJECTION_ORGANISATION.getValue().isEmpty()
                    || dmElement.DEFECT_INJECTION_DATE.getValue().isEmpty()) {

                Warning warning = new Warning(this, "Defect correction attributes are not completely filled out.",
                        "LifeCycleState " + dmElement.LIFE_CYCLE_STATE.getValueAsText() + "\n"
                        + "At least one of the following fields is empty:\n"
                        + "Defective Work Product Type, Defect Classification, Defect Injection Organisation, Defect Injection Date");
                warning.addAffectedElement(dmElement);
                addMarker(dmElement, warning);
            }
        }
    }
}
