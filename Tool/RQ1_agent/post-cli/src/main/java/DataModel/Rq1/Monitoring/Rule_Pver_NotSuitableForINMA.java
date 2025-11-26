/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1Pver;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import RestClient.Rq1Poller.RestClient_Rq1PollerResponse;
import Rq1Data.Enumerations.InMaBuildState;
import java.util.EnumSet;

/**
 *
 * @author hfi5wi
 */
public class Rule_Pver_NotSuitableForINMA extends DmRule<DmRq1Pver> implements DmRq1Pver.Rq1PollerListener {
    
    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "PVER ist not suitable for INMA",
            "Creates a warning if PVER is ready but not suitable for INMA.\n");
    private static String WARNING_TITLE = "PVER ist not suitable for INMA";
    
    public Rule_Pver_NotSuitableForINMA(DmRq1Pver pver) {
        super(description, pver);
        pver.addListener(this);
    }
    
    private boolean isReadyForInMa() {
        return (dmElement.IN_MA_BUILD_STATUS.getValueAsText().equals(InMaBuildState.READY_FOR_IN_MA.getText()));
    }
    
    @Override
    protected void executeRule() {
        //
        // Execute rule only on PVER if "InMa build Status" is "Ready for InMa"
        //
        addDependency(dmElement.getProject());
        
        if (isReadyForInMa()) {
            dmElement.checkPverForInMa();
        }
    }

    @Override
    public void checkedPverForInMa(RestClient_Rq1PollerResponse response) {
        Warning inMaWarning = new Warning(this, WARNING_TITLE, response != null ? response.getInmaComment(): "No Response from Rq1 Poller");
        if (dmElement.getWarnings().stream().filter((warning) -> (warning.getTitle().equals(WARNING_TITLE))).count() > 0) {
            return; // Prevent adding multiple Warnings of the same type
        }
        if (response != null && response.isSutitableForINMA() == false && isReadyForInMa()) {
            inMaWarning.addAffectedElement(dmElement);
            addMarker(dmElement, inMaWarning);
        }
        dmElement.RQ1_POLLER_RESPONSE.setValue(response);
    }
}
